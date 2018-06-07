package rtk.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import rtk.common.Common;
import rtk.inventory.ContainerToolbox;
import rtk.inventory.InventoryToolbox;

public class GuiToolbox extends GuiContainer {
    /** The ResourceLocation containing the chest GUI texture. */
    private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
    private final InventoryPlayer playerInv;
    private final InventoryToolbox toolboxInv;
    /** window height is calculated with these values; the more rows, the higher */
    private final int inventoryRows;

    public GuiToolbox(InventoryPlayer upperInv, InventoryToolbox lowerInv)
    {
        super(new ContainerToolbox(upperInv, lowerInv, Minecraft.getMinecraft().player));
        this.playerInv = upperInv;
        this.toolboxInv = lowerInv;
        this.allowUserInput = false;
        int i = 222;
        int j = 114;
        this.inventoryRows = lowerInv.getSizeInventory() / 9;
        this.ySize = 114 + this.inventoryRows * 18;
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRenderer.drawString(this.toolboxInv.getDisplayName().getUnformattedText(), 8, 6, 4210752);
        this.fontRenderer.drawString(this.playerInv.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.drawTexturedModalRect(i, j + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }

    @Override
    protected boolean checkHotbarKeys(int keyCode) {
        int toolboxIndex = toolboxInv.stackIndex; //Don't put the tool box in itself!
        if (toolboxIndex >= 0 && toolboxIndex < 9 && this.mc.gameSettings.keyBindsHotbar[toolboxIndex].isActiveAndMatches(keyCode))
            return false;
        return super.checkHotbarKeys(keyCode);
    }


}
