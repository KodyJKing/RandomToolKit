package rtk.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import rtk.RTK;
import rtk.common.CNBT;
import rtk.common.Common;

public class ItemToolbox extends ItemBase {
    public ItemToolbox(String name) {
        super(name);
        setCreativeTab(CreativeTabs.TOOLS);
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand){
        NBTTagCompound nbt = CNBT.ensureCompound(stack);
        nbt.setInteger("index", Common.findExactStack(player.inventory, stack));
        if(!world.isRemote)
            player.openGui(RTK.instance, 0, world, (int)player.posX, (int)player.posY, (int)player.posZ);
        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
    }
}
