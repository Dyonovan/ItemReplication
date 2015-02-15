package com.dyonovan.modernalchemy.helpers;

import cofh.api.item.IToolHammer;
import cpw.mods.fml.common.Loader;
import net.minecraft.item.Item;

public class WrenchHelper {

    /**
     * Test if item if a known mod wrench
     * @param item Wrench in question
     * @return true if it is a wrench that we like
     */
    public static boolean isWrench(Item item) {
        if(Loader.isModLoaded("BuildCraft|Core")) {
            if(item.getUnlocalizedName().equalsIgnoreCase("item.wrenchItem"))
                return true;
        }
        return (item instanceof IToolHammer);
    }
}
