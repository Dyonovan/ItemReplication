package com.dyonovan.modernalchemy.events;

import com.dyonovan.modernalchemy.blocks.BlockBase;
import com.dyonovan.modernalchemy.handlers.ConfigHandler;
import com.dyonovan.modernalchemy.helpers.WrenchHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.BlockEvent;

public class OnBreakEvent {

    @SubscribeEvent
    public void onBreakEvent(BlockEvent.BreakEvent event) {

        if (event.block instanceof BlockBase && !WrenchHelper.isWrench(event.getPlayer().getCurrentEquippedItem().getItem()) &&
                ConfigHandler.machineExplodes && !event.getPlayer().capabilities.isCreativeMode) {
            event.world.createExplosion(event.getPlayer(), event.x, event.y, event.z, 2.0F, true);
        }
    }
}
