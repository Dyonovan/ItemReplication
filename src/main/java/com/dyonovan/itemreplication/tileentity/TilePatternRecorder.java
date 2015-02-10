package com.dyonovan.itemreplication.tileentity;

import com.dyonovan.itemreplication.energy.TeslaBank;
import com.dyonovan.itemreplication.energy.TeslaConsumer;
import com.dyonovan.itemreplication.energy.TeslaReceiver;
import com.dyonovan.itemreplication.handlers.ItemHandler;
import com.dyonovan.itemreplication.items.ItemPattern;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * Created by Tim on 2/5/2015.
 */
public class TilePatternRecorder extends BaseTile implements IInventory {

    public ItemStack inventory[];
    public static final int ITEM_SLOT = 0;
    public static final int PATTERN_INPUT_SLOT = 1;
    public static final int PATTERN_OUTPUT_SLOT = 2;

    private TeslaBank energyTank;
    private TeslaConsumer teslaConsumer;
    private TeslaReceiver teslaReceiver;

    private static int totalProcessTime = TeslaConsumer.secondsToSpeed(15);
    private int currentProcessTime;

    public TilePatternRecorder() {
        inventory = new ItemStack[3];
        currentProcessTime = 0;

        energyTank = new TeslaBank(0, 100000);
        teslaReceiver = new TeslaReceiver(energyTank);
        teslaConsumer = new TeslaConsumer(energyTank, 1);
    }

    public TeslaBank getEnergyTank() {return energyTank; }

    public int getProgressScaled(int scale) { return currentProcessTime / totalProcessTime * scale; }

    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        energyTank.readFromNBT(tag);

        NBTTagList itemsTag = tag.getTagList("Items", 10);
        this.inventory = new ItemStack[getSizeInventory()];
        for (int i = 0; i < itemsTag.tagCount(); i++)
        {
            NBTTagCompound nbtTagCompound1 = itemsTag.getCompoundTagAt(i);
            NBTBase nbt = nbtTagCompound1.getTag("Slot");
            int j = -1;
            if ((nbt instanceof NBTTagByte)) {
                j = nbtTagCompound1.getByte("Slot") & 0xFF;
            } else {
                j = nbtTagCompound1.getShort("Slot");
            }
            if ((j >= 0) && (j < this.inventory.length)) {
                this.inventory[j] = ItemStack.loadItemStackFromNBT(nbtTagCompound1);
            }
        }
    }

    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        energyTank.writeToNBT(tag);

        NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < this.inventory.length; i++) {
            if (this.inventory[i] != null)
            {
                NBTTagCompound nbtTagCompound1 = new NBTTagCompound();
                nbtTagCompound1.setShort("Slot", (short)i);
                this.inventory[i].writeToNBT(nbtTagCompound1);
                nbtTagList.appendTag(nbtTagCompound1);
            }
        }
        tag.setTag("Items", nbtTagList);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        // accept energy first
        teslaReceiver.chargeFromCoils(worldObj, this, energyTank);

        if(canStartWork()) {
            startWorking();
            currentProcessTime = 1;
        }

        if(currentProcessTime > 0) {
            int progress = teslaConsumer.consumeEnergy();
            if (progress < 0) {
                // fail
                currentProcessTime = 0;
            } else {
                // do work
                currentProcessTime += progress;
            }
            if(currentProcessTime >= totalProcessTime) {
                finishWorking();
                currentProcessTime = 0;
            }
        }
    }

    private boolean canStartWork() {
        return inventory[PATTERN_INPUT_SLOT] != null && inventory[ITEM_SLOT] != null && inventory[PATTERN_OUTPUT_SLOT] != null &&
                inventory[PATTERN_INPUT_SLOT].getItem() == ItemHandler.itemPattern &&  // must have a pattern - doesn't matter if it is already recorded
                inventory[PATTERN_OUTPUT_SLOT].getItem() == null &&  // output must be empty
                inventory[ITEM_SLOT].getItem() != null; // must have an item - TODO: restrict items?
    }

    private void startWorking() {
        // consume resources
        inventory[PATTERN_INPUT_SLOT].stackSize--;
        if(inventory[PATTERN_INPUT_SLOT].stackSize == 0)
            inventory[PATTERN_INPUT_SLOT] = null;
    }

    private void finishWorking() {
        // create products
        inventory[PATTERN_OUTPUT_SLOT] = new ItemStack(ItemHandler.itemPattern);
        ItemPattern.recordPattern(inventory[PATTERN_OUTPUT_SLOT], inventory[ITEM_SLOT]);
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
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
        return null;
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
        return 1;
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
    public boolean isItemValidForSlot(int slot, ItemStack stack) {

        switch(slot){
            case TilePatternRecorder.ITEM_SLOT:
                return stack.getItem() != ItemHandler.itemPattern; // TODO: how should this be limited?
            case TilePatternRecorder.PATTERN_INPUT_SLOT:
                return stack.getItem() == ItemHandler.itemPattern;
            default:
                return false;
        }
    }
}
