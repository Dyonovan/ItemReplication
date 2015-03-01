package com.dyonovan.modernalchemy.blocks.teslacoil;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.blocks.BlockBase;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.manual.component.ComponentBase;
import com.dyonovan.modernalchemy.manual.component.ComponentItemRender;
import com.dyonovan.modernalchemy.manual.component.ComponentTextBox;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaStand;
import com.dyonovan.modernalchemy.util.Location;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class BlockTeslaStand extends BlockBase {

    public BlockTeslaStand() {
        super(Material.iron);
        this.setBlockName(Constants.MODID + ":blockTeslaStand");
        this.setCreativeTab(ModernAlchemy.tabModernAlchemy);
        this.setBlockBounds(0.34375F, 0F, 0.34375F, 0.65625F, 1F, 0.65625F);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileTeslaStand();
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
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
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.getBlock(x, y - 1, z) instanceof BlockTeslaBase || world.getBlock(x, y - 1, z) instanceof BlockTeslaStand;
    }

    //We want the block to have particle effects, so we need to register a block icon even if we don't render it (try falling onto the block and you'll see)
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister) {
        this.blockIcon = iconregister.registerIcon("minecraft:iron_block");
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        super.onBlockActivated(world, x, y, z, player, par6, par7, par8, par9);

        Location location = new Location(x, y, z);
        while(!world.isAirBlock(location.x, location.y, location.z)) {
            location.moveInDirection(ForgeDirection.UP);
            if(world.getBlock(location.x, location.y, location.z) instanceof BlockTeslaCoil) {
                BlockTeslaCoil coil = (BlockTeslaCoil) world.getBlock(location.x, location.y, location.z);
                coil.onBlockActivated(world, location.x, location.y, location.z, player, par6, par7, par8, par9);
                return true;
            }
        }
        return false;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block par5, int par6)
    {
        Location location = new Location(x, y + 1, z);
        while(!world.isAirBlock(location.x, location.y, location.z)) {
            //WorldUtils.breakBlock(world, location);
            world.setBlockToAir(location.x, location.y, location.z);
        }
        super.breakBlock(world, x, y, z, par5, par6);
    }
}
