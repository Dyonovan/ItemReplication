package com.dyonovan.modernalchemy.gui;

import com.dyonovan.modernalchemy.container.ContainerArcFurnace;
import com.dyonovan.modernalchemy.gui.widget.WidgetEnergyBank;
import com.dyonovan.modernalchemy.gui.widget.WidgetLiquidTank;
import com.dyonovan.modernalchemy.gui.widget.WidgetPulse;
import com.dyonovan.modernalchemy.helpers.GuiHelper;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.tileentity.arcfurnace.TileArcFurnaceCore;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiArcFurnace extends BaseGui {
    private TileArcFurnaceCore core;
    private ResourceLocation background = new ResourceLocation(Constants.MODID + ":textures/gui/blastfurnace.png");

    public GuiArcFurnace(InventoryPlayer inventoryPlayer, TileArcFurnaceCore core) {
        super(new ContainerArcFurnace(inventoryPlayer, core));
        this.core = core;
        widgets.add(new WidgetLiquidTank(this, core.getAirTank(),     37, 78, 52));
        widgets.add(new WidgetLiquidTank(this, core.getOutputTank(), 147, 78, 52));
        widgets.add(new WidgetEnergyBank(this, core.getEnergyBank(),   8, 78));
        widgets.add(new      WidgetPulse(this, core,                  67, 54));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        final String invTitle =  "Arc";
        final String invTitle2 = "Furnace";

        fontRendererObj.drawString(invTitle, 95 + ((fontRendererObj.getStringWidth(invTitle2) / 2) - (fontRendererObj.getStringWidth(invTitle) / 2)), 6, 4210752);
        fontRendererObj.drawString(invTitle2, 95, 17, 4210752);
        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 95, ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F); //Could do some fun colors and transparency here
        this.mc.renderEngine.bindTexture(background);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        //Draw Arrow
        int arrow = core.getCookTimeScaled(24);
        drawTexturedModalRect(x + 107, y + 35, 176, 22, arrow, 17);

        super.drawGuiContainerBackgroundLayer(f, i, j);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float par3) {
        super.drawScreen(mouseX, mouseY, par3);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        if(GuiHelper.isInBounds(mouseX, mouseY, x + 8, y + 26, x + 23, y + 77)) {
            List<String> toolTip = new ArrayList<String>();
            toolTip.add(GuiHelper.GuiColor.YELLOW + "Energy");
            toolTip.add(core.getEnergyBank().getEnergyLevel() + "/" + core.getEnergyBank().getMaxCapacity() + GuiHelper.GuiColor.ORANGE + "T");
            renderToolTip(mouseX, mouseY, toolTip);
        }
        if(GuiHelper.isInBounds(mouseX, mouseY, x + 37, y + 26, x + 52, y + 77)) {
            List<String> toolTip = new ArrayList<String>();
            toolTip.add(GuiHelper.GuiColor.YELLOW + "Compressed Air");
            toolTip.add(core.getAirTank().getFluidAmount() + "/" + core.getAirTank().getCapacity() + GuiHelper.GuiColor.ORANGE + "mb");
            renderToolTip(mouseX, mouseY, toolTip);
        }
        if(GuiHelper.isInBounds(mouseX, mouseY, x + 147, y + 26, x + 162, y + 77)) {
            List<String> toolTip = new ArrayList<String>();
            toolTip.add(GuiHelper.GuiColor.YELLOW + "Molten Actinium");
            toolTip.add(core.getOutputTank().getFluidAmount() + "/" + core.getOutputTank().getCapacity() + GuiHelper.GuiColor.ORANGE + "mb");
            renderToolTip(mouseX, mouseY, toolTip);
        }
    }
}
