package com.dyonovan.modernalchemy.model.teslacoil;

import com.dyonovan.modernalchemy.lib.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ModelTeslaBase {
    private IModelCustom modelBase;

    public ModelTeslaBase() {
        modelBase = AdvancedModelLoader.loadModel(new ResourceLocation(Constants.MODID + ":models/teslaBase.obj"));
    }
    
    public void renderMain() {
        modelBase.renderAllExcept("Rotor_Rotor_Cylinder.019");
    }

    public void renderRotor() {
        modelBase.renderPart("Rotor_Rotor_Cylinder.019");
    }
}
