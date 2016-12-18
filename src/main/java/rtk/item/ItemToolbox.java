package rtk.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import rtk.RTK;

public class ItemToolbox extends ItemBase {
    public ItemToolbox(String name) {
        super(name);
        setCreativeTab(CreativeTabs.TOOLS);
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand){
        if(!world.isRemote)
            player.openGui(RTK.instance, 0, world, (int)player.posX, (int)player.posY, (int)player.posZ);
        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
    }
}
