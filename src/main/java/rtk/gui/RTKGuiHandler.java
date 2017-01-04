package rtk.gui;

import net.minecraft.client.gui.inventory.GuiDispenser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import rtk.ModItems;
import rtk.block.BlockUltraDispenser;
import rtk.common.Common;
import rtk.inventory.ContainerToolbox;
import rtk.inventory.ContainerUltraDispenser;
import rtk.inventory.InventoryToolbox;
import rtk.inventory.InventoryUltraDispenser;
import rtk.item.ItemUltraDispenser;

public class RTKGuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return getGuiElement(ID, player, world, x, y, z, true);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return getGuiElement(ID, player, world, x, y, z, false);
    }

    public Object getGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z, boolean serverSide){
        if(ID == 0)
            return toolboxGuiElement(player, serverSide);
        if(ID == 1)
            return ultraDispenserGuiElement(player, serverSide);
        return null;
    }

    public Object toolboxGuiElement(EntityPlayer player, boolean serverSide){
        ItemStack stack = player.getHeldItemMainhand();
        if(stack == null || stack.getItem() != ModItems.toolbox){
            stack = player.getHeldItemOffhand();
            if(stack == null || stack.getItem() != ModItems.toolbox)
                return null;
        }

        InventoryToolbox inv = new InventoryToolbox(stack, Common.findExactStack(player.inventory, stack));

        if(serverSide)
            return new ContainerToolbox(player.inventory, inv, player);

        return new GuiToolbox(player.inventory,  inv);
    }

    public Object ultraDispenserGuiElement(EntityPlayer player, boolean serverSide){
        ItemStack stack = player.getHeldItemMainhand();
        if(stack == null || !(stack.getItem() instanceof ItemUltraDispenser)){
            stack = player.getHeldItemOffhand();
            if(stack == null || !(stack.getItem() instanceof ItemUltraDispenser))
                return null;
        }

        InventoryUltraDispenser inv = new InventoryUltraDispenser(stack, Common.findExactStack(player.inventory, stack));

        if(serverSide)
            return new ContainerUltraDispenser(player.inventory, inv);

        return new GuiUltraDispenser(player.inventory, inv);
    }
}
