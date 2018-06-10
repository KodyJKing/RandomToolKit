package rtk.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import rtk.item.ModItems;
//import rtk.udispenser.*;
import rtk.common.Common;
import rtk.inventory.ContainerToolbox;
import rtk.inventory.InventoryToolbox;

public class ModGuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return getGuiElement(ID, player, world, new BlockPos(x, y, z), true);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return getGuiElement(ID, player, world, new BlockPos(x, y, z), false);
    }

    public Object getGuiElement(int ID, EntityPlayer player, World world, BlockPos pos, boolean serverSide){
        if (ID == 0)
            return toolboxGuiElement(player, serverSide);
//        if (ID == 1)
//            return ultraDispenserGuiElement(player, serverSide);
//        if (ID == 2)
//            return ultraDispenserGuiTEElement(player, pos, serverSide);
        return null;
    }

    public Object toolboxGuiElement(EntityPlayer player, boolean serverSide){
        ItemStack stack = player.getHeldItemMainhand();
        if (stack.isEmpty() || stack.getItem() != ModItems.toolbox){
            stack = player.getHeldItemOffhand();
            if (stack.isEmpty() || stack.getItem() != ModItems.toolbox)
                return null;
        }

        InventoryToolbox inv = new InventoryToolbox(stack, Common.findExactStack(player.inventory, stack));

        if (serverSide)
            return new ContainerToolbox(player.inventory, inv, player);

        return new GuiToolbox(player.inventory,  inv);
    }

//    public Object ultraDispenserGuiElement(EntityPlayer player, boolean serverSide){
//        ItemStack stack = player.getHeldItemMainhand();
//        if (stack == null || !(stack.getItem() instanceof ItemUDispenser)){
//            stack = player.getHeldItemOffhand();
//            if (stack == null || !(stack.getItem() instanceof ItemUDispenser))
//                return null;
//        }
//
//        InventoryUDispenser inv = new InventoryUDispenser(stack, Common.findExactStack(player.inventory, stack));
//
//        if (serverSide)
//            return new ContainerUDispenser(player.inventory, inv);
//
//        return new GuiUDispenser(player.inventory, inv);
//    }
//
//    public Object ultraDispenserGuiTEElement(EntityPlayer player, BlockPos pos, boolean serverSide){
//        TileUDispenser tile = (TileUDispenser) player.worldObj.getTileEntity(pos);
//
//        if (serverSide)
//            return new ContainerUDispenser(player.inventory, tile);
//
//        return new GuiUDispenser(player.inventory, tile);
//    }
}
