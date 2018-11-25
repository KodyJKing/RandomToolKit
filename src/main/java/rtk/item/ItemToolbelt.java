package rtk.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.Optional;
import rtk.common.CWorld;

@Optional.Interface(modid = "baubles", iface = "baubles.api.IBauble")
public class ItemToolbelt extends ItemBase implements IBauble {
    public ItemToolbelt(String name) {
        super(name);
        setCreativeTab(CreativeTabs.TOOLS);
        setMaxStackSize(1);
    }

    public static void selectBestTool(EntityPlayer player, int itemSlot) {
        if (itemSlot > 8)
            return;

        RayTraceResult trace = CWorld.traceBlocks(player, 5F);
        if (trace.getBlockPos() == null)
            return;
        IBlockState bs = player.getEntityWorld().getBlockState(trace.getBlockPos());

        int toolSlot = bestTool(bs, player);
        if (toolSlot == -1 || toolSlot == itemSlot)
            return;

        InventoryPlayer inv = player.inventory;

        if (toolSlot >= 0 && toolSlot <= 8) {
            inv.currentItem = toolSlot;
            return;
        }

        int empty = inv.getFirstEmptyStack();
        if (empty >= 0 && empty <= 8) {
            ItemStack tool = inv.removeStackFromSlot(toolSlot);
            inv.setInventorySlotContents(empty, tool);
            inv.currentItem = empty;
            return;
        }

        if (inv.currentItem == itemSlot)
            return;

        ItemStack tool = inv.getStackInSlot(toolSlot);
        ItemStack held = inv.getStackInSlot(inv.currentItem);
        inv.setInventorySlotContents(inv.currentItem, tool);
        inv.setInventorySlotContents(toolSlot, held);
    }

    public static int bestTool(IBlockState bs, EntityPlayer player) {
        InventoryPlayer inv = player.inventory;

        float bestStrength = Float.MIN_VALUE;
        int bestIndex = -1;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack == null)
                continue;

            float currStrength = stack.getDestroySpeed(bs);
            if (currStrength > bestStrength) {
                bestStrength = currStrength;
                bestIndex = i;
            }

        }
        return bestIndex;
    }

    @Override
    @Optional.Method(modid = "baubles")
    public BaubleType getBaubleType(ItemStack itemstack) { return BaubleType.BELT; }
}
