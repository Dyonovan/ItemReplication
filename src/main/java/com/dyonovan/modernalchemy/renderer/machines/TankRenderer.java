package com.dyonovan.modernalchemy.renderer.machines;

import com.dyonovan.modernalchemy.blocks.misc.BlockTank;
import com.dyonovan.modernalchemy.helpers.RenderHelper;
import com.dyonovan.modernalchemy.tileentity.misc.TileTank;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class TankRenderer implements ISimpleBlockRenderingHandler
{
    public static int tankModelID = RenderingRegistry.getNextAvailableRenderId();
    public static int renderPass = 0;

    @Override
    public void renderInventoryBlock (Block block, int metadata, int modelID, RenderBlocks renderer) {
    }

    @Override
    public boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, Block block, int modelID, RenderBlocks renderer)
    {
        if (modelID == tankModelID)
        {
            TileTank logic = (TileTank) world.getTileEntity(x, y, z);
            //Liquid
            if (renderPass == 0)
            {
                if (logic.containsFluid())
                {
                    FluidStack liquid = logic.tank.getFluid();
                    renderer.setRenderBounds(0.001, 0.001, 0.001, 0.999, logic.getFluidAmountScaled(), 0.999);
                    Fluid fluid = liquid.getFluid();
                    RenderHelper.renderLiquidBlock(fluid.getStillIcon(), fluid.getStillIcon(), x, y, z, renderer, world, true);

                    renderer.setRenderBounds(0, 0.001, 0.001, 0.999, logic.getFluidAmountScaled(), 0.999);
                }

                if(logic.isFilling()) {
                    if (logic.getDirectionFillingFrom() == ForgeDirection.UP) {
                        Fluid fluid = logic.getFillingLiquid();
                        renderer.setRenderBounds(0.5 - logic.getTransferAmountScaled(), 0.001, 0.5 - logic.getTransferAmountScaled(), 0.5 + logic.getTransferAmountScaled(), 0.999, 0.5 + logic.getTransferAmountScaled());
                        RenderHelper.renderLiquidBlock(fluid.getStillIcon(), fluid.getStillIcon(), x, y, z, renderer, world, true);
                    }
                    else if(logic.renderOffset > 0 && (logic.getDirectionFillingFrom() == ForgeDirection.DOWN || logic.getDirectionFillingFrom() == ForgeDirection.UNKNOWN)){
                        Fluid fluid = logic.getFillingLiquid();
                        double renderMax = 0;
                        if(logic.containsFluid())
                            renderMax= logic.getFluidAmountScaled() + 0.1;
                        if(renderMax > 0.999)
                            renderMax = 0.999;
                        renderer.setRenderBounds(0.5 - logic.getTransferAmountScaled(), 0.001, 0.5 - logic.getTransferAmountScaled(), 0.5 + logic.getTransferAmountScaled(), renderMax, 0.5 + logic.getTransferAmountScaled());
                        RenderHelper.renderLiquidBlock(fluid.getStillIcon(), fluid.getStillIcon(), x, y, z, renderer, world, true);
                    }
                }
                if(logic.isLocked())
                    renderer.renderBlockUsingTexture(Blocks.stone, x, y, z, BlockTank.locked);
            }

            //Block
            else
            {
                renderer.setRenderBounds(0, 0, 0, 1, 1, 1);
                renderer.renderStandardBlock(block, x, y, z);
            }
        }
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory (int modelID)
    {
        return true;
    }

    @Override
    public int getRenderId ()
    {
        return tankModelID;
    }
}
