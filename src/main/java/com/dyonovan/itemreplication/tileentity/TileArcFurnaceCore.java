package com.dyonovan.itemreplication.tileentity;

import com.dyonovan.itemreplication.blocks.BlockDummy;
import com.dyonovan.itemreplication.handlers.BlockHandler;
import com.dyonovan.itemreplication.helpers.Location;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class TileArcFurnaceCore extends BaseCore implements IFluidHandler, IInventory {

    private FluidTank outputTank;
    private FluidTank airTank;

    private ItemStack inventory[];
    private static final int INPUT_SLOT = 0;
    private static final int CATALYST_SLOT = 1;
    private static final int OUTPUT_SLOT = 2;

    public TileArcFurnaceCore() {
        outputTank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);
        airTank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);
        outputTank.setFluid(new FluidStack(BlockHandler.fluidActinium, FluidContainerRegistry.BUCKET_VOLUME * 10));
        airTank.setFluid(new FluidStack(BlockHandler.fluidCompressedAir, FluidContainerRegistry.BUCKET_VOLUME * 10));

        inventory = new ItemStack[3];
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
    }

    @Override
    public boolean isWellFormed() {
        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                for(int k = -1; k <= 1; k++) {
                    if(i == 0 && j == 0 && k == 0)
                        continue;
                    if(!(worldObj.getBlock(xCoord + i, yCoord + j, zCoord + k) instanceof BlockDummy))
                        return false;
                }
            }
        }
        buildStructure();
        return true;
    }

    @Override
    public void buildStructure() {
        for(int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    if (i == 0 && j == 0 && k == 0)
                        continue;
                    TileDummy dummy = (TileDummy)worldObj.getTileEntity(xCoord + i, yCoord + j, zCoord + k);
                    dummy.setCoreLocation(new Location(xCoord, yCoord, zCoord));
                    worldObj.setBlockMetadataWithNotify(xCoord + i, yCoord + j, zCoord + k, 1, 2);
                }
            }
        }
    }

    @Override
    public void deconstructStructure() {
        for(int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    if (i == 0 && j == 0 && k == 0)
                        continue;
                    TileDummy dummy = (TileDummy)worldObj.getTileEntity(xCoord + i, yCoord + j, zCoord + k);
                    if(dummy != null) {
                        worldObj.setBlockMetadataWithNotify(xCoord + i, yCoord + j, zCoord + k, 0, 2);
                        dummy.setCoreLocation(new Location(-100, -100, -100));
                    }
                }
            }
        }
    }

    public FluidTank getOutputTank() {
        return outputTank;
    }

    public FluidTank getAirTank() {
        return airTank;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        int amount = 0;
        if(canFill(from, resource.getFluid())) {
            amount = outputTank.fill(resource, doFill);
        }
        return amount;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return this.drain(from, resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return outputTank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return fluid == BlockHandler.fluidActinium;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[0];
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        if(tagCompound.getBoolean("hasOutputFluid")) {
            outputTank.setFluid(FluidRegistry.getFluidStack(tagCompound.getString("outputFluid"), tagCompound.getInteger("outputFluidAmount")));
        }
        else
            outputTank.setFluid(null);

        if(tagCompound.getBoolean("hasAir")) {
            airTank.setFluid(FluidRegistry.getFluidStack(tagCompound.getString("air"), tagCompound.getInteger("airAmount")));
        }
        else
            airTank.setFluid(null);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setBoolean("hasOutputFluid", outputTank.getFluid() != null);
        tagCompound.setBoolean("hasAir", airTank.getFluid() != null);
        if(outputTank.getFluid() != null) {
            tagCompound.setString("outputFluid", outputTank.getFluid().getFluid().getName());
            tagCompound.setInteger("outputFluidAmount", outputTank.getFluid().amount);
        }
        if(airTank.getFluid() != null) {
            tagCompound.setString("air", airTank.getFluid().getFluid().getName());
            tagCompound.setInteger("airAmount", airTank.getFluid().amount);
        }
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
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory[slot] = stack;
        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }
        markDirty();
    }

    @Override
    public ItemStack decrStackSize(int slot, int amt) {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null) {
            if (stack.stackSize <= amt) {
                setInventorySlotContents(slot, null);
            } else {
                stack = stack.splitStack(amt);
                if (stack.stackSize == 0) {
                    setInventorySlotContents(slot, null);
                }
            }
        }
        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null) {
            setInventorySlotContents(slot, null);
        }
        return stack;
    }

    @Override
    public String getInventoryName() {
        return null;
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
        return true;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        switch(slot) {
        case INPUT_SLOT :
            return itemStack.getItem() == Item.getItemFromBlock(BlockHandler.blockOreActinium);
        case CATALYST_SLOT :
            return itemStack.getItem() == Items.coal;

        }
        return false;
    }
}
