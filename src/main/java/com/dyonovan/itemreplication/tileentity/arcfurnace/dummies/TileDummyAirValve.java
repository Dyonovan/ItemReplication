package com.dyonovan.itemreplication.tileentity.arcfurnace.dummies;

import com.dyonovan.itemreplication.handlers.BlockHandler;
import com.dyonovan.itemreplication.tileentity.arcfurnace.TileArcFurnaceCore;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileDummyAirValve extends TileDummy implements IFluidHandler {

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        TileArcFurnaceCore core = (TileArcFurnaceCore) getCore();
        if(core != null)
            return resource.getFluid() == BlockHandler.fluidCompressedAir ? core.fill(ForgeDirection.NORTH, resource, doFill) : 0;
        else
            return 0;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        TileArcFurnaceCore core = (TileArcFurnaceCore) getCore();
        if(core != null)
            return core.canFill(from, fluid);
        else
            return false;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[0];
    }
}
