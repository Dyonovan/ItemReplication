package com.dyonovan.itemreplication.tileentity;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.dyonovan.itemreplication.energy.ITeslaHandler;
import com.dyonovan.itemreplication.energy.TeslaBank;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class TileTeslaCoil extends BaseTile implements IEnergyHandler, ITeslaHandler {

    protected EnergyStorage energyRF = new EnergyStorage(10000, 1000, 0);
    private TeslaBank energyTesla = new TeslaBank(1000);

    public TileTeslaCoil() {

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
        return side == ForgeDirection.DOWN;
    }

    @Override
    public void addEnergy(int maxAmount) {
        energyTesla.addEnergy(maxAmount);
    }

    @Override
    public void drainEnergy(int maxAmount) {
        energyTesla.drainEnergy(maxAmount);
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

        //TODO add to config
        int MAX_T_TICK = 10;
        int RF_PER_T = 10;

        if (energyRF.getEnergyStored() > 0 && energyTesla.getEnergyLevel()  < energyTesla.getMaxCapacity()) {

            int actualRF = Math.min(MAX_T_TICK * RF_PER_T, energyRF.getEnergyStored());
            int actualTesla = Math.min(MAX_T_TICK, energyTesla.getMaxCapacity() - energyTesla.getEnergyLevel());

            if (actualTesla * RF_PER_T < actualRF) {
                removeEnergy(actualTesla * 10);
                energyTesla.addEnergy(actualTesla);
            } else if (actualTesla * RF_PER_T > actualRF && actualRF > 100) {
                removeEnergy(actualRF);
                energyTesla.addEnergy(actualRF / RF_PER_T);
            } else if (actualTesla * RF_PER_T == actualRF) {
                removeEnergy(actualRF);
                energyTesla.addEnergy(actualTesla);
            }
        }
    }
}
