package rtk.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import rtk.common.Common;

public class ItemToolbelt extends ItemBase {
    public ItemToolbelt(String name) {
        super(name);
        setCreativeTab(CreativeTabs.TOOLS);
        setMaxStackSize(1);
    }

    public static void selectBestTool(EntityPlayer player, int itemSlot){
        if(itemSlot > 8)
            return;

        RayTraceResult trace = Common.traceLook(player, 5F); //player.rayTrace(5, 0);
        if(trace.getBlockPos() == null)
            return;
        IBlockState bs = player.getEntityWorld().getBlockState(trace.getBlockPos());

        int toolSlot = bestTool(bs, player);
        if(toolSlot == -1 || toolSlot == itemSlot)
            return;

        InventoryPlayer inv = player.inventory;
       // System.out.println(inv.currentItem);

        if(toolSlot >= 0 && toolSlot <= 8){
            inv.currentItem = toolSlot;
            return;
        }

        int empty = inv.getFirstEmptyStack();
        if(empty >= 0 && empty <= 8){
            ItemStack tool = inv.removeStackFromSlot(toolSlot);
            inv.setInventorySlotContents(empty, tool);
            inv.currentItem = empty;
            return;
        }

        if(inv.currentItem == itemSlot)
            return;

        ItemStack tool = inv.getStackInSlot(toolSlot);
        ItemStack held = inv.getStackInSlot(inv.currentItem);
        inv.setInventorySlotContents(inv.currentItem, tool);
        inv.setInventorySlotContents(toolSlot, held);
    }

    public static int bestTool(IBlockState bs, EntityPlayer player){
        InventoryPlayer inv = player.inventory;

        float bestStrength = Float.MIN_VALUE;
        int bestIndex = -1;

        for(int i = 0; i < inv.getSizeInventory(); i++){
            ItemStack stack = inv.getStackInSlot(i);
            if(stack == null) //|| !(stack.getItem() instanceof ItemTool))
                continue;

//            ItemTool tool = (ItemTool)stack.getItem();
//            Item tool = stack.getItem();

//            float currStrength = tool.getStrVsBlock(stack, bs);
            float currStrength = stack.getDestroySpeed(bs);
            if(currStrength > bestStrength){
                bestStrength = currStrength;
                bestIndex = i;
            }

        }
        return bestIndex;
    }
}
