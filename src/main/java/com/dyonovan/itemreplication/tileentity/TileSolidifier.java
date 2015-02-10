package com.dyonovan.itemreplication.tileentity;

import com.dyonovan.itemreplication.blocks.BlockCompressor;
import com.dyonovan.itemreplication.energy.ITeslaHandler;
import com.dyonovan.itemreplication.energy.TeslaBank;
import com.dyonovan.itemreplication.handlers.BlockHandler;
import com.dyonovan.itemreplication.handlers.ConfigHandler;
import com.dyonovan.itemreplication.handlers.ItemHandler;
import com.dyonovan.itemreplication.util.RenderUtils;
import com.dyonovan.itemreplication.lib.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import java.util.ArrayList;
import java.util.List;

public class TileSolidifier extends BaseTile implements IFluidHandler, ITeslaHandler, ISidedInventory {

    private static final int PROCESS_TIME = 500;

    public FluidTank tank;
    private TeslaBank energy;
    private boolean isActive;
    private int currentSpeed;
    public ItemStack inventory[];
    private static final int OUTPUT_SLOT = 0;
    private int timeProcessed;

    public TileSolidifier() {
        tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);
        tank.setFluid(new FluidStack(BlockHandler.fluidActinium, 0));
        this.energy = new TeslaBank(1000);
        this.isActive = false;
        inventory = new ItemStack[1];
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        energy.readFromNBT(tag);
        tank.readFromNBT(tag);
        timeProcessed = tag.getInteger("TimeProcessed");

        setInventorySlotContents(0, ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Item")));

    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        energy.writeToNBT(tag);
        tank.writeToNBT(tag);
        tag.setInteger("TimeProcessed", timeProcessed);

        ItemStack itemstack = getStackInSlot(0);
        NBTTagCompound item = new NBTTagCompound();
        if (itemstack != null) {
            //item.setByte("Slot", (byte) 0);
            itemstack.writeToNBT(item);
        }
        tag.setTag("Item", item);
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (resource.getFluid() != BlockHandler.fluidActinium) return 0;
        int amount = tank.fill(resource, doFill);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        return amount;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if (!resource.isFluidEqual(tank.getFluid()))
        {
            return null;
        }
        return tank.drain(resource.amount, true);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return tank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return true;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] { tank.getInfo() };
    }

    @Override
    public void addEnergy(int maxAmount) {
        energy.addEnergy(maxAmount);
    }

    @Override
    public int drainEnergy(int maxAmount) {
        return energy.drainEnergy(maxAmount);
    }

    @Override
    public int getEnergyLevel() {
        return energy.getEnergyLevel();
    }

    @Override
    public TeslaBank getEnergyBank() {
        return energy;
    }

    public void setEnergy(int amount) {
        energy.setEnergyLevel(amount);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (energy.canAcceptEnergy()) {
            chargeFromCoils();
        }
        //if (worldObj.isRemote) return;

        if (isPowered()) {
            if (this.inventory[0] != null && this.inventory[0].stackSize >= 64) return;
            if (energy.getEnergyLevel() > 0 && tank.getFluid() != null && tank.getFluidAmount() > 0) {
                updateSpeed();
                if (!isActive)
                    isActive = BlockCompressor.toggleIsActive(this.worldObj, this.xCoord, this.yCoord, this.zCoord);

                if (timeProcessed < PROCESS_TIME) {
                    if (energy.getEnergyLevel() > 0) {
                        energy.drainEnergy(currentSpeed);
                    }
                }
                if (tank.getFluid() != null || tank.getFluidAmount() > 10 * currentSpeed) {
                    tank.drain(10, true);
                    timeProcessed += currentSpeed;
                }
                if (timeProcessed >= PROCESS_TIME) {
                    if (inventory[0] == null) setInventorySlotContents(0, new ItemStack(ItemHandler.itemCube));
                    else inventory[0].stackSize++;
                    timeProcessed = 0;
                }
                super.markDirty();
            } else if (isActive) {
                isActive = BlockCompressor.toggleIsActive(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
                timeProcessed = 0;
            }
        } else timeProcessed = 0;
    }

    public void updateSpeed() {
        if(energy.getEnergyLevel() == 0) {
            currentSpeed = 0;
            return;
        }

        currentSpeed = (energy.getEnergyLevel() * 20) / energy.getMaxCapacity();
        if(currentSpeed == 0)
            currentSpeed = 1;
    }

    public void chargeFromCoils() {
        int maxFill = energy.getMaxCapacity() - energy.getEnergyLevel();
        List<TileTeslaCoil> coils = findCoils(worldObj, this);
        int currentDrain = 0;
        for (TileTeslaCoil coil : coils) {
            if (coil.getEnergyLevel() <= 0) continue;
            int fill = coil.getEnergyLevel() > ConfigHandler.tickTesla ? ConfigHandler.tickTesla : coil.getEnergyLevel();
            if (currentDrain + fill > maxFill)
                fill = maxFill - currentDrain;
            currentDrain += fill;
            coil.drainEnergy(fill);

            if (worldObj.isRemote)
                RenderUtils.renderLightningBolt(worldObj, xCoord, yCoord, zCoord, coil, fill);
        }
        while (currentDrain > 0) {
            energy.addEnergy(ConfigHandler.tickTesla);
            currentDrain--;
        }
    }

    public static List<TileTeslaCoil> findCoils(World world, TileEntity tile) {

        List<TileTeslaCoil> list = new ArrayList<TileTeslaCoil>();

        int tileX = tile.xCoord;
        int tileY = tile.yCoord;
        int tileZ = tile.zCoord;

        for (int x = -ConfigHandler.searchRange; x <= ConfigHandler.searchRange; x++) {
            for (int y = -ConfigHandler.searchRange; y <= ConfigHandler.searchRange; y++) {
                for (int z = -ConfigHandler.searchRange; z <= ConfigHandler.searchRange; z++) {
                    if (world.getTileEntity(tileX + x, tileY + y, tileZ + z) instanceof TileTeslaCoil) {
                        list.add((TileTeslaCoil) world.getTileEntity(tileX + x, tileY + y, tileZ + z));
                    }
                }
            }
        }
        return list;
    }


    @Override
    public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
        return new int[] {0};
    }

    @Override
    public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_) {
        return false;
    }

    @Override
    public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
        return true;
    }

    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int count) {
        ItemStack itemstack = getStackInSlot(slot);

        if(itemstack != null) {
            if(itemstack.stackSize <= count) {
                setInventorySlotContents(slot, null);
            }
            itemstack = itemstack.splitStack(count);

        }
        super.markDirty();
        return itemstack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack itemStack = getStackInSlot(slot);
        setInventorySlotContents(slot, null);
        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemstack) {
        inventory[slot] = itemstack;
    }

    @Override
    public String getInventoryName() {
        return Constants.MODID + ":blockSolidifier";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        return true;
    }

    public int getCookTimeScaled(int scale) {
        return (timeProcessed * scale) / PROCESS_TIME;
    }
}
