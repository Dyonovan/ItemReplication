package com.dyonovan.modernalchemy.model.teslacoil;

import com.dyonovan.modernalchemy.lib.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ModelTeslaStand {
    private IModelCustom modelStand;

    public ModelTeslaStand() {
        modelStand = AdvancedModelLoader.loadModel(new ResourceLocation(Constants.MODID + ":models/teslaStand.obj"));
    }

    public void render() {
        modelStand.renderAll();
    }
}
