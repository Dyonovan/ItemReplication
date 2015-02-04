package com.dyonovan.itemreplication;

import com.dyonovan.itemreplication.handlers.ConfigHandler;
import com.dyonovan.itemreplication.lib.Constants;
import com.dyonovan.itemreplication.proxy.CommonProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

public class ItemReplication {

    @Instance(Constants.MODID)
    public static ItemReplication instance;

    @SidedProxy(clientSide = "com.dyonovan.itemreplication.proxy.ClientProxy", serverSide = "com.dyonovan.itemreplication.proxy.CommonProxy")
    public static CommonProxy proxy;

    public static CreativeTabs tabItemReplication = new CreativeTabs("tabItemReplication") {
        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return Items.cauldron;
        }
    };

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){

        ConfigHandler.init(new Configuration(event.getSuggestedConfigurationFile()));

    }

    @EventHandler
    public void init(FMLInitializationEvent event) {}

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {}
}
