package com.dyonovan.itemreplication.handlers;

import com.dyonovan.itemreplication.items.ItemPattern;
import com.dyonovan.itemreplication.items.buckets.ItemBucketActinium;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by Tim on 2/5/2015.
 */
public class ItemHandler {

    public static ItemPattern itemPattern;
    public static Item itemBucketActinium;

    public static void init() {
        itemPattern = new ItemPattern();
        GameRegistry.registerItem(itemPattern, "pattern");

        //Actinium Bucket
        itemBucketActinium = new ItemBucketActinium(BlockHandler.blockFluidActinium);
        GameRegistry.registerItem(itemBucketActinium, "bucketActinium");
        FluidContainerRegistry.registerFluidContainer(new FluidStack(BlockHandler.fluidActinium, FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(itemBucketActinium), new ItemStack(Items.bucket));
    }


}