package com.dyonovan.modernalchemy.tileentity.misc;

import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.tileentity.BaseTile;
import com.dyonovan.teambrcore.notification.GuiColor;
import com.dyonovan.teambrcore.notification.Notification;
import com.dyonovan.teambrcore.notification.NotificationHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import java.util.List;

public class TileTank extends BaseTile implements IFluidHandler
{
    public FluidTank tank;
    public Fluid lastFluid;
    public int renderOffset;
    public double transferOffset;
    private ForgeDirection fillingFrom = ForgeDirection.UNKNOWN;
    private Fluid lockedFluid;

    public TileTank() {
        tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 16);
    }

    /*******************************************************************************************************************
     **************************************** MultiBlock Functions *****************************************************
     *******************************************************************************************************************/

    public void distributeFluids() {
        if (containsFluid()) {
            if (getTileInDirection(ForgeDirection.DOWN) instanceof TileTank) {
                TileTank otherTank = (TileTank) getTileInDirection(ForgeDirection.DOWN);
                int capacity = getCapacity(otherTank);
                int drainAmount = drainAmount();
                if (canFill(otherTank) && capacity > 0) {
                    FluidStack drained = new FluidStack(tank.getFluid().getFluid(), drainAmount);
                    drain(ForgeDirection.UNKNOWN, drained, true);
                    otherTank.fill(ForgeDirection.UP, drained, true);
                    return;
                }
            }
            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                if(dir == ForgeDirection.UP || dir == ForgeDirection.DOWN)
                    continue;
                TileEntity tile = getTileInDirection(dir);
                if (tile instanceof TileTank && containsFluid()) {
                    TileTank otherTank = (TileTank) tile;
                    int capacity = getCapacity(otherTank);
                    int drainAmount = drainAmount(otherTank);
                    if (canFill(otherTank) && capacity > 0 && drainAmount > 0) {
                        FluidStack drained = new FluidStack(tank.getFluid().getFluid(), drainAmount);
                        otherTank.fill(dir, drained, true);
                        drain(ForgeDirection.UNKNOWN, drained, true);
                    }
                }
            }
        }
    }

    public boolean canFill(TileTank otherTank) {
        return otherTank.canFill(ForgeDirection.UNKNOWN, tank.getFluid().getFluid());
    }

    public static boolean hasCapacity(TileTank otherTank) {
        return otherTank.tank.getFluidAmount() < otherTank.tank.getCapacity();
    }

    public int drainRate() {
        return Math.max(14000 / tank.getFluid().getFluid().getViscosity(), 5);
    }

    private int drainAmount() {
        return Math.min(drainRate(), tank.getFluidAmount());
    }

    public static int getCapacity(TileTank otherTank) {
        return otherTank.tank.getCapacity() - otherTank.tank.getFluidAmount();
    }

    public int drainAmount(TileTank otherTank) {
        return Math.min(drainAmount(), this.tank.getFluidAmount() - otherTank.tank.getFluidAmount());
    }

    /*******************************************************************************************************************
     ********************************************* Fluid Functions *****************************************************
     *******************************************************************************************************************/

    @Override
    public int fill (ForgeDirection from, FluidStack resource, boolean doFill)
    {
        int amount = 0;
        if(canFill(from, resource.getFluid())) {
            amount = tank.fill(resource, doFill);
            if(amount > 0) {
                fillingFrom = from;
                lastFluid = resource.getFluid();
                if (amount > 0 && doFill) {
                    renderOffset = resource.amount;
                    transferOffset = resource.amount;
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                }
                return amount;
            } else {
                TileTank above = getTankInDirection(ForgeDirection.UP);
                if(above != null && above.canFill(ForgeDirection.DOWN, resource.getFluid()))
                    amount = above.fill(ForgeDirection.DOWN, resource, doFill);
                return amount;
            }
        }
        return amount;
    }

    @Override
    public FluidStack drain (ForgeDirection from, int maxDrain, boolean doDrain)
    {
        FluidStack amount = tank.drain(maxDrain, doDrain);
        if (amount != null && doDrain)
        {
            renderOffset = -maxDrain;
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
        return amount;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        if (tank.getFluidAmount() == 0)
            return null;
        if (tank.getFluid().getFluid() != resource.getFluid())
            return null;

        // same fluid, k
        return this.drain(from, resource.amount, doDrain);
    }

    @Override
    public boolean canFill (ForgeDirection from, Fluid fluid)
    {
        if(isLocked())
            return fluid == lockedFluid;
        else if(tank.getFluid() == null || tank.getFluid().getFluid() == fluid)
            return true;
        else
            return getTankInDirection(ForgeDirection.UP) != null && getTankInDirection(ForgeDirection.UP).canFill(from, fluid);
    }

    @Override
    public boolean canDrain (ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo (ForgeDirection from)
    {
        FluidStack fluid = null;
        if (tank.getFluid() != null)
            fluid = tank.getFluid().copy();
        return new FluidTankInfo[] { new FluidTankInfo(fluid, tank.getCapacity()) };
    }

    public ForgeDirection getDirectionFillingFrom() {
        return fillingFrom;
    }

    public boolean isFilling() {
        return renderOffset > 0 || transferOffset > 0;
    }

    public Fluid getFillingLiquid() {
        return lastFluid;
    }

    public float getFluidAmountScaled () {
        return (float) (tank.getFluid().amount - renderOffset) / (tank.getCapacity() * 1.01F);
    }

    public double getTransferAmountScaled() {
        return Math.min(0.3, (transferOffset / 10) * 0.3);
    }

    public boolean containsFluid ()
    {
        return tank.getFluid() != null;
    }

    public boolean lockTank() {
        if(containsFluid())
            lockedFluid = tank.getFluid().getFluid();
        markDirty();
        return isLocked();
    }

    public void unlockTank() {
        lockedFluid = null;
        markDirty();
    }

    public boolean isLocked() {
        return lockedFluid != null;
    }

    public Fluid getLockedFluid() {
        return lockedFluid;
    }

    public int getBrightness () {
        if (containsFluid()) {
            return (tank.getFluid().getFluid().getLuminosity() * tank.getFluidAmount()) / tank.getCapacity();
        }
        return 0;
    }
    /*******************************************************************************************************************
     ********************************************** Tile Functions *****************************************************
     *******************************************************************************************************************/

    @Override
    public void readFromNBT (NBTTagCompound tags) {
        super.readFromNBT(tags);
        readCustomNBT(tags);
    }

    @Override
    public void writeToNBT (NBTTagCompound tags) {
        super.writeToNBT(tags);
        writeCustomNBT(tags);
    }

    public void readCustomNBT (NBTTagCompound tags) {
        if (tags.getBoolean("hasFluid")) {
            if (tags.getInteger("itemID") != 0) {
                tank.setFluid(new FluidStack(tags.getInteger("itemID"), tags.getInteger("amount")));
            }
            else {
                tank.setFluid(FluidRegistry.getFluidStack(tags.getString("fluidName"), tags.getInteger("amount")));
            }
        }
        else
            tank.setFluid(null);

        if(tags.getBoolean("isLocked")) {
            lockedFluid = FluidRegistry.getFluid(tags.getString("lockedFluid"));
        }
        else
            lockedFluid = null;
    }

    public void writeCustomNBT (NBTTagCompound tags) {
        FluidStack liquid = tank.getFluid();
        tags.setBoolean("hasFluid", liquid != null);
        if (liquid != null) {
            tags.setString("fluidName", liquid.getFluid().getName());
            tags.setInteger("amount", liquid.amount);
        }
        tags.setBoolean("isLocked", lockedFluid != null);
        if(lockedFluid != null) {
            tags.setString("lockedFluid", lockedFluid.getName());
        }
    }

    /* Packets */
    @Override
    public Packet getDescriptionPacket () {
        NBTTagCompound tag = new NBTTagCompound();
        writeCustomNBT(tag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tag);
    }

    @Override
    public void onDataPacket (NetworkManager net, S35PacketUpdateTileEntity packet) {
        readCustomNBT(packet.func_148857_g());
        worldObj.func_147479_m(xCoord, yCoord, zCoord);
    }

    /* Updating */
    @Override
    public boolean canUpdate () {
        return true;
    }

    @Override
    public void updateEntity () {
        if (renderOffset > 0) {
            renderOffset -= 8;
            worldObj.func_147479_m(xCoord, yCoord, zCoord);
        }

        if(transferOffset > 0) {
            transferOffset -= 0.2;
            worldObj.func_147479_m(xCoord, yCoord, zCoord);
        }

        distributeFluids();
    }

    /*******************************************************************************************************************
     ********************************************** Misc Functions *****************************************************
     *******************************************************************************************************************/

    public int comparatorStrength () {
        return 15 * tank.getFluidAmount() / tank.getCapacity();
    }

    @Override
    public void returnWailaHead(List<String> currenttip) {
        if(isLocked()) {
            currenttip.add(GuiColor.RED + "Tank Locked: " + getLockedFluid().getLocalizedName());
        }
        if(containsFluid()) {
            currenttip.add(GuiColor.TURQUISE + "Fluid: " + tank.getFluid().getFluid().getLocalizedName());
            currenttip.add(GuiColor.ORANGE + "" + tank.getFluidAmount() + " / " + tank.getCapacity() + "mb");
        }
        else
            currenttip.add("Empty");
    }

    @Override
    public void onWrench(EntityPlayer player) {
        if(!isLocked()) {
            if (lockTank()) {
                if (worldObj.isRemote)
                    NotificationHelper.addNotification(new Notification(new ItemStack(BlockHandler.blockTank), GuiColor.RED + "Tank Locked", GuiColor.TURQUISE + "Fuel: " + getLockedFluid().getLocalizedName()));
            }
        }
        else {
            unlockTank();
            if (containsFluid()) {
                if (worldObj.isRemote)
                    NotificationHelper.addNotification(new Notification(new ItemStack(BlockHandler.blockTank), GuiColor.GREEN + "Tank Unlocked", GuiColor.TURQUISE + "Fuel: " + tank.getFluid().getLocalizedName()));
            }
        }
    }

    public static final int DIR_NORTH = 1;
    public static final int DIR_SOUTH = 2;

    public static final int DIR_WEST = 4;
    public static final int DIR_EAST = 8;

    public static final int DIR_UP = 16;
    public static final int DIR_DOWN = 32;


    public interface IRenderNeighbours {
        public boolean hasDirectNeighbour(int direction);

        public boolean hasDiagonalNeighbour(int direction1, int direction2);
    }

    public static final IRenderNeighbours NO_NEIGHBOURS = new IRenderNeighbours() {
        @Override
        public boolean hasDirectNeighbour(int dir) {
            return false;
        }

        @Override
        public boolean hasDiagonalNeighbour(int direction1, int direction2) {
            return false;
        }
    };

    private boolean accepts(FluidStack liquid) {
        if (liquid == null) return true;
        final FluidStack ownFluid = tank.getFluid();
        return ownFluid == null || ownFluid.isFluidEqual(liquid);
    }

    private class NeighbourProvider implements IRenderNeighbours {
        public boolean[] neighbors = new boolean[64];

        private void testNeighbour(FluidStack ownFluid, int dx, int dy, int dz, int flag) {
            TileTank tank = getTankInDirection(dx, dy, dz);
            if (tank != null && tank.accepts(ownFluid) && tank.isLocked() == isLocked()) neighbors[flag] = true;
        }

        public NeighbourProvider() {
            final FluidStack fluid = tank.getFluid();

            testNeighbour(fluid, 0, 1, 0, DIR_UP);
            testNeighbour(fluid, 0, -1, 0, DIR_DOWN);
            testNeighbour(fluid, +1, 0, 0, DIR_EAST);
            testNeighbour(fluid, -1, 0, 0, DIR_WEST);
            testNeighbour(fluid, 0, 0, +1, DIR_SOUTH);
            testNeighbour(fluid, 0, 0, -1, DIR_NORTH);

            testNeighbour(fluid, +1, 1, 0, DIR_UP | DIR_EAST);
            testNeighbour(fluid, -1, 1, 0, DIR_UP | DIR_WEST);
            testNeighbour(fluid, 0, 1, +1, DIR_UP | DIR_SOUTH);
            testNeighbour(fluid, 0, 1, -1, DIR_UP | DIR_NORTH);

            testNeighbour(fluid, +1, -1, 0, DIR_DOWN | DIR_EAST);
            testNeighbour(fluid, -1, -1, 0, DIR_DOWN | DIR_WEST);
            testNeighbour(fluid, 0, -1, +1, DIR_DOWN | DIR_SOUTH);
            testNeighbour(fluid, 0, -1, -1, DIR_DOWN | DIR_NORTH);

            testNeighbour(fluid, -1, 0, -1, DIR_WEST | DIR_NORTH);
            testNeighbour(fluid, -1, 0, +1, DIR_WEST | DIR_SOUTH);
            testNeighbour(fluid, +1, 0, +1, DIR_EAST | DIR_SOUTH);
            testNeighbour(fluid, +1, 0, -1, DIR_EAST | DIR_NORTH);
        }

        @Override
        public boolean hasDirectNeighbour(int direction) {
            return neighbors[direction];
        }

        @Override
        public boolean hasDiagonalNeighbour(int direction1, int direction2) {
            return neighbors[direction1 | direction2];
        }
    }

    public IRenderNeighbours getRenderConnections() {
        return new NeighbourProvider();
    }

    private static TileTank getValidTank(final TileEntity neighbor) {
        return (neighbor instanceof TileTank && !neighbor.isInvalid()) ? (TileTank)neighbor : null;
    }

    protected TileTank getNeighborTank(final int x, final int y, final int z) {
        if (!worldObj.blockExists(x, y, z)) return null;

        Chunk chunk = worldObj.getChunkFromBlockCoords(x, z);
        TileEntity te = chunk.getTileEntityUnsafe(x & 0xF, y, z & 0xF);

        return (te instanceof TileTank)? (TileTank)te : null;
    }

    private TileTank getTankInDirection(ForgeDirection direction) {
        final TileEntity neighbor = getTileInDirection(direction);
        return getValidTank(neighbor);
    }

    private TileTank getTankInDirection(int dx, int dy, int dz) {
        final TileEntity neighbor = getNeighbour(dx, dy, dz);
        return getValidTank(neighbor);
    }
    public TileEntity getTileInDirection(ForgeDirection direction) {
        return getNeighbour(direction.offsetX, direction.offsetY, direction.offsetZ);
    }

    public TileEntity getNeighbour(int dx, int dy, int dz) {
        return getTileEntity(xCoord + dx, yCoord + dy, zCoord + dz);
    }

    private TileEntity getTileEntity(int x, int y, int z) {
        return (worldObj != null && worldObj.blockExists(x, y, z))? worldObj.getTileEntity(x, y, z) : null;
    }
}
