package com.dyonovan.itemreplication.items;

import com.dyonovan.itemreplication.ItemReplication;
import com.dyonovan.itemreplication.lib.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/**
 * Created by Tim on 2/5/2015.
 */
public class ItemPattern extends Item {

    protected static IIcon iconBlankPattern, iconRecordedPattern;

    public ItemPattern() {
        this.setUnlocalizedName(Constants.MODID + ":pattern");
        this.setCreativeTab(ItemReplication.tabItemReplication);
        this.setMaxStackSize(1);
    }


    @Override
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        return super.getIcon(stack, renderPass, player, usingItem, useRemaining);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        if(iconBlankPattern == null)
            iconBlankPattern = register.registerIcon(Constants.MODID + ":blank_pattern");

        if(iconRecordedPattern == null)
            iconRecordedPattern = register.registerIcon(Constants.MODID + ":recorded_pattern");

        this.itemIcon = iconBlankPattern;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World par2World, EntityPlayer player)
    {

        if(player.isSneaking())
        {
            if(itemstack.stackTagCompound != null)
            {
                itemstack.stackTagCompound = null;
                this.itemIcon = iconBlankPattern;
                //if(!par2World.isRemote)
                //    player.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Auto-Lighting Enabled"));
            }
//            else
//            {
//                // testing only
//                itemstack.stackTagCompound = new NBTTagCompound();
//                this.itemIcon = iconRecordedPattern;
//            }
        }
        return itemstack;
    }

    public static void recordPattern(ItemStack pattern, ItemStack item) {
        if(pattern != null && item != null) {
            pattern.stackTagCompound = new NBTTagCompound();
            pattern.stackTagCompound.setString("Item", item.getUnlocalizedName());
        }
    }

    public static String getRecordedPattern(ItemStack pattern){
        if(pattern != null && pattern.stackTagCompound != null)
            return pattern.stackTagCompound.getString("Item");
        return "";
    }

}