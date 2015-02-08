package com.dyonovan.itemreplication.blocks;

import com.dyonovan.itemreplication.ItemReplication;
import com.dyonovan.itemreplication.handlers.GuiHandler;
import com.dyonovan.itemreplication.lib.Constants;
import com.dyonovan.itemreplication.tileentity.TileTeslaCoil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockTeslaCoil extends BlockContainer {

    public BlockTeslaCoil() {
        super(Material.iron);

        this.setCreativeTab(ItemReplication.tabItemReplication);
        this.setHardness(1.5F);
        this.setBlockName(Constants.MODID + ":blockTeslaCoil");

    }

    //temp till model is done
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister) {
        this.blockIcon = iconregister.registerIcon(Constants.MODID + ":teslaCoil");
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }


    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileTeslaCoil();
    }

    /*@Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }*/

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.getBlock(x, y - 1, z) instanceof BlockTeslaStand;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        if (world.isRemote)
        {
            return true;
        }
        else
        {
            TileTeslaCoil tile = (TileTeslaCoil)world.getTileEntity(x, y, z);
            if(tile != null) {
                player.openGui(ItemReplication.instance, GuiHandler.TESLA_COIL_GUI_ID, world, x, y, z);
            }
            return true;
        }
    }
}
