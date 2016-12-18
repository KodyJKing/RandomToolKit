package rtk.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemDevTool extends ItemBase {
    public ItemDevTool(String name) {
        super(name);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
        if(world.isRemote && player.isSneaking())
            player.inventory.addItemStackToInventory(new ItemStack(Items.DIAMOND));
        return ActionResult.newResult(EnumActionResult.SUCCESS, itemStack);
    }
}
