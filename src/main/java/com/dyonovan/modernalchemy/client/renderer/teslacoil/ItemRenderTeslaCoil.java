package com.dyonovan.modernalchemy.client.renderer.teslacoil;

import com.dyonovan.modernalchemy.client.gui.GuiManual;
import com.dyonovan.modernalchemy.client.model.teslacoil.ModelTeslaCoil;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class ItemRenderTeslaCoil implements IItemRenderer {

    private ModelTeslaCoil coil;
    private ResourceLocation image;

    public ItemRenderTeslaCoil(ResourceLocation texture) {
        coil = new ModelTeslaCoil();
        image = texture;
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        switch (type) {
        case ENTITY: {
            GL11.glScalef(2.0F, 2.0F, 2.0F);
            renderCoil(0.0F, 0.5F, 0.0F, item.getItemDamage());
            break;
        }
        case EQUIPPED: {
            if(Minecraft.getMinecraft().currentScreen == null || !(Minecraft.getMinecraft().currentScreen instanceof GuiManual)) {
                GL11.glScalef(2.0F, 2.0F, 2.0F);
                GL11.glRotatef(-45, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45, 0.0F, 0.0F, 1.0F);
                renderCoil(0.2F, 0.2F, 0.5F, item.getItemDamage());
            } else
                renderCoil(0F, 0.5F, 0F, item.getItemDamage());
            break;
        }
        case EQUIPPED_FIRST_PERSON: {
            GL11.glScalef(1.5F, 1.5F, 1.5F);
            renderCoil(1.0F, 0.6F, 0.6F, item.getItemDamage());
            break;
        }
        case INVENTORY: {
            GL11.glScalef(1.35F, 1.35F, 1.35F);
            renderCoil(0.0F, -0.1F, 0.0F, item.getItemDamage());
            break;
        }
        default:
            break;
        }
    }

    public void renderCoil(float x, float y, float z, int metaData)
    {
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(image);

        GL11.glPushMatrix(); //start
        GL11.glTranslatef(x, y, z); //size
        coil.render();
        GL11.glPopMatrix(); //end
    }
}
