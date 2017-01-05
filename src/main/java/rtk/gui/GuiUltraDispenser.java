package rtk.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import rtk.inventory.ContainerUltraDispenser;
import rtk.inventory.InventoryUltraDispenser;

public class GuiUltraDispenser extends GuiContainer {
    private static final ResourceLocation DISPENSER_GUI_TEXTURES = new ResourceLocation("rtk:textures/gui/container/ultraDispenser.png");
    /** The player inventory bound to this GUI. */
    private final InventoryPlayer playerInventory;
    /** The inventory contained within the corresponding Dispenser. */
    public InventoryUltraDispenser dispenserInventory;

    public GuiUltraDispenser(InventoryPlayer playerInv, InventoryUltraDispenser dispenserInv)
    {
        super(new ContainerUltraDispenser(playerInv, dispenserInv));
        this.playerInventory = playerInv;
        this.dispenserInventory = dispenserInv;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String s = this.dispenserInventory.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);

        int fuelWidth = fontRendererObj.getStringWidth("Fuel");
        int payloadWidth = fontRendererObj.getStringWidth("Payload");
        int modifierWidth = fontRendererObj.getStringWidth("Modifier");

        int slotRadius = 7;

        fontRendererObj.drawString("Fuel", 26 + slotRadius - fuelWidth / 2, 21, 4210752);
        fontRendererObj.drawString("Payload", 80 + slotRadius - payloadWidth / 2, 21, 4210752);
        fontRendererObj.drawString("Modifier", 134 + slotRadius - modifierWidth / 2, 21, 4210752);
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(DISPENSER_GUI_TEXTURES);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected boolean checkHotbarKeys(int keyCode) {
        if(dispenserInventory.stack == null)
            return super.checkHotbarKeys(keyCode);

        int stackIndex = dispenserInventory.stackIndex; //Don't put the tool box in itself!
        if(stackIndex >= 0 && stackIndex < 9 && this.mc.gameSettings.keyBindsHotbar[stackIndex].isActiveAndMatches(keyCode))
            return false;
        return super.checkHotbarKeys(keyCode);
    }
}
