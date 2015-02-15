package com.dyonovan.modernalchemy.tileentity.teslacoil;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.dyonovan.modernalchemy.energy.ITeslaHandler;
import com.dyonovan.modernalchemy.energy.TeslaBank;
import com.dyonovan.modernalchemy.handlers.ConfigHandler;
import com.dyonovan.modernalchemy.tileentity.BaseTile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class TileTeslaCoil extends BaseTile implements IEnergyHandler, ITeslaHandler {

    protected EnergyStorage energyRF;
    private TeslaBank energyTesla;

    public TileTeslaCoil() {
        super();
        energyRF = new EnergyStorage(10000, 1000, 0);
        energyTesla = new TeslaBank(1000);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        energyRF.readFromNBT(tag);
        energyTesla.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        energyRF.writeToNBT(tag);
        energyTesla.writeToNBT(tag);
    }

    @Override
    public int receiveEnergy(ForgeDirection side, int maxReceive, boolean simulate) {
        return energyRF.receiveEnergy(maxReceive, simulate);
    }

    public void setRFEnergyStored(int i) {
        energyRF.setEnergyStored(i);
    }

    @Override
    public int extractEnergy(ForgeDirection forgeDirection, int maxReceive, boolean simulate) {
        return energyRF.extractEnergy(maxReceive, simulate);
    }

    public void removeEnergy(int amount) {
       energyRF.setEnergyStored(energyRF.getEnergyStored() - amount);
    }

    @Override
    public int getEnergyStored(ForgeDirection forgeDirection) {
        return energyRF.getEnergyStored();
    }

    public int getRFEnergyStored() {
        return energyRF.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection forgeDirection) {
        return energyRF.getMaxEnergyStored();
    }

    public int getRFMaxEnergyStored() {
        return energyRF.getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection side) {
        return side == ForgeDirection.DOWN; //TODO Why BC Pipes dont update on load
    }

    @Override
    public void addEnergy(int maxAmount) {
        energyTesla.addEnergy(maxAmount);
    }

    @Override
    public int drainEnergy(int maxAmount) {
        return energyTesla.drainEnergy(maxAmount);
    }

    @Override
    public int getEnergyLevel() {
        return energyTesla.getEnergyLevel();
    }

    @Override
    public TeslaBank getEnergyBank() {
        return energyTesla;
    }

    public void setTeslaEnergyStored(int i) {
        energyTesla.setEnergyLevel(i);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (worldObj.isRemote) return;

        if (energyRF.getEnergyStored() > 0 && energyTesla.getEnergyLevel()  < energyTesla.getMaxCapacity()) {
            int actualRF = Math.min(ConfigHandler.maxCoilGenerate * ConfigHandler.rfPerTesla, energyRF.getEnergyStored());
            int actualTesla = Math.min(ConfigHandler.maxCoilGenerate, energyTesla.getMaxCapacity() - energyTesla.getEnergyLevel());

            if (actualTesla * ConfigHandler.rfPerTesla < actualRF) {
                removeEnergy(actualTesla * 10);
                energyTesla.addEnergy(actualTesla);
            } else if (actualTesla * ConfigHandler.rfPerTesla > actualRF && actualRF > 100) {
                removeEnergy(actualRF);
                energyTesla.addEnergy(actualRF / ConfigHandler.rfPerTesla);
            } else if (actualTesla * ConfigHandler.rfPerTesla == actualRF) {
                removeEnergy(actualRF);
                energyTesla.addEnergy(actualTesla);
            }
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }
}
