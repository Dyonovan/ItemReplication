package com.dyonovan.modernalchemy.client.renderer.replicator;

import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.client.model.replicator.ModelReplicatorStand;
import com.dyonovan.modernalchemy.common.tileentity.replicator.TileReplicatorStand;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderReplicatorStand extends TileEntitySpecialRenderer {

    public static final ResourceLocation texture = new ResourceLocation(Constants.MODID + ":textures/models/replicator_stand.png");

    private ModelReplicatorStand model;

    private float rotMod = 0.0F;
    public RenderReplicatorStand() {
        this.model = new ModelReplicatorStand();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {

        GL11.glPushMatrix();

        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        GL11.glRotatef(180, 0F, 0F, 1F);

        this.bindTexture(texture);

        this.model.renderModel(0.0625F, tileentity.xCoord, tileentity.yCoord, tileentity.zCoord, (tileentity.getWorldObj() == null));

        GL11.glPopMatrix();

        //Render Item on Top
        TileReplicatorStand rs = (TileReplicatorStand)tileentity;


        if (rs.getInventory().getStackInSlot(0) != null) {
            EntityItem entityItem = new EntityItem(rs.getWorldObj(), rs.xCoord, rs.yCoord, rs.zCoord, rs.getInventory().getStackInSlot(0));
            entityItem.hoverStart = 0;
            entityItem.rotationYaw = 0;
            entityItem.motionX = 0;
            entityItem.motionY = 0;
            entityItem.motionZ = 0;
            rotMod += ((rotMod % 360) / 360) + 1;
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glTranslatef((float) x + 0.5F, (float) y + 1.15F, (float) z + 0.5F);
            GL11.glRotatef(360, 0, 1, 1);
            GL11.glRotatef(rotMod, 0.0F, 1.0F, 0.0F);
            RenderManager.instance.renderEntityWithPosYaw(entityItem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glPopMatrix();
        }
    }

}
