package com.dyonovan.modernalchemy.common.blocks.machines;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.common.blocks.BlockModernAlchemy;
import com.dyonovan.modernalchemy.lib.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import openmods.block.BlockRotationMode;
import openmods.infobook.BookDocumentation;

@BookDocumentation
public class BlockAmalgamator extends BlockModernAlchemy {

    @SideOnly(Side.CLIENT)
    public static class Icons {
        public static IIcon side;
        public static IIcon back;
        public static IIcon top;
    }

    public BlockAmalgamator() {
        super(Material.iron);
        this.setHardness(1.5F);
        this.setCreativeTab(ModernAlchemy.tabModernAlchemy);
        setRotationMode(BlockRotationMode.FOUR_DIRECTIONS);
        setPlacementMode(BlockPlacementMode.ENTITY_ANGLE);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconregister) {
        this.blockIcon = iconregister.registerIcon(Constants.MODID + ":amalgamator_front");
        Icons.side = iconregister.registerIcon(Constants.MODID + ":machineSide");
        Icons.back = iconregister.registerIcon(Constants.MODID + ":machineBack");
        Icons.top = iconregister.registerIcon(Constants.MODID + ":machineTop");

        setTexture(ForgeDirection.NORTH, Icons.back);
        setTexture(ForgeDirection.SOUTH, blockIcon);
        setTexture(ForgeDirection.EAST, Icons.side);
        setTexture(ForgeDirection.WEST, Icons.side);
        setTexture(ForgeDirection.UP, Icons.top);
        setTexture(ForgeDirection.DOWN, Icons.top);
    }
}
