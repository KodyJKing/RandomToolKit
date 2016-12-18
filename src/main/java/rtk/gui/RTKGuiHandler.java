package rtk.gui;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import rtk.inventory.ContainerToolbox;
import rtk.inventory.InventoryToolBox;

public class RTKGuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerToolbox(player.inventory, new InventoryToolBox(player.getHeldItemMainhand()), player);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new GuiToolbox(player.inventory,  new InventoryToolBox(player.getHeldItemMainhand()));
    }
}
