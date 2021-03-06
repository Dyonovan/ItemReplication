package com.dyonovan.modernalchemy.common.blocks;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.common.blocks.teslacoil.BlockTeslaBase;
import com.dyonovan.modernalchemy.common.blocks.teslacoil.BlockTeslaCoil;
import com.dyonovan.modernalchemy.common.blocks.teslacoil.BlockTeslaStand;
import com.dyonovan.modernalchemy.helpers.WrenchHelper;
import com.dyonovan.modernalchemy.util.Location;
import com.dyonovan.modernalchemy.util.WorldUtils;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import openmods.block.OpenBlock;
import openmods.infobook.BookDocumentation;

public abstract class BlockModernAlchemy extends OpenBlock {

    public BlockModernAlchemy(Material mat) {
        super(mat);
        this.setHardness(2.0F);
        this.setCreativeTab(ModernAlchemy.tabModernAlchemy);
    }

    @Override
    protected Object getModInstance() {
        return ModernAlchemy.instance;
    }

    public boolean useWrench(World world, int x, int y, int z) {
        if (!world.isRemote) {
            if (this instanceof BlockTeslaBase || this instanceof BlockTeslaStand) {
                Location loc = new Location(x, y + 1, z);
                int count = 0;
                while (true) {
                    if (world.getBlock(loc.x, loc.y, loc.z) instanceof BlockTeslaStand || world.getBlock(loc.x, loc.y, loc.z) instanceof BlockTeslaCoil) {
                        loc.y += 1;
                        count += 1;
                    } else {
                        doBreak(world, loc.x, loc.y - 1, loc.z, count);
                        break;
                    }
                }
            }
            WorldUtils.expelItem(world, x, y, z, new ItemStack(Item.getItemFromBlock(this)));
            world.setBlockToAir(x, y, z);
        }
        return true;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        if (player.isSneaking() && player.getCurrentEquippedItem() != null && WrenchHelper.isWrench(player.getCurrentEquippedItem().getItem())) {
            useWrench(world, x, y, z);
            return true;
        }
        return super.onBlockActivated(world, x, y, z, player, par6, par7, par8, par9);
    }

    private void doBreak(World world, int x, int y, int z, int count) {
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                WorldUtils.expelItem(world, x, y - i, z, new ItemStack(Item.getItemFromBlock(world.getBlock(x, y - i, z))));
                world.setBlockToAir(x, y - i, z);
            }
        }
    }
}