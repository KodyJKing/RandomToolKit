package rtk.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import rtk.ModItems;
import rtk.inventory.ContainerToolbox;
import rtk.inventory.InventoryToolbox;

public class RTKGuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        ItemStack stack = player.getHeldItemMainhand();
        if(stack == null || stack.getItem() != ModItems.toolbox){
            stack = player.getHeldItemOffhand();
            if(stack == null || stack.getItem() != ModItems.toolbox)
                return null;
        }
        return new ContainerToolbox(player.inventory, new InventoryToolbox(stack), player);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        ItemStack stack = player.getHeldItemMainhand();
        if(stack == null || stack.getItem() != ModItems.toolbox){
            stack = player.getHeldItemOffhand();
            if(stack == null || stack.getItem() != ModItems.toolbox)
                return null;
        }
        return new GuiToolbox(player.inventory,  new InventoryToolbox(stack));
    }
}
