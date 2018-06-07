package rtk.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import rtk.RTK;
import rtk.common.CNBT;
import rtk.common.Common;

import javax.annotation.Nullable;

public class ItemToolbox extends ItemBase {
    public ItemToolbox(String name) {
        super(name);
        setCreativeTab(CreativeTabs.TOOLS);
        setMaxStackSize(1);
        addPropertyOverride(new ResourceLocation("open"), new IItemPropertyGetter() {
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
                boolean result = stack.getTagCompound() != null &&
                        stack.getTagCompound().hasKey("open") &&
                        stack.getTagCompound().getBoolean("open");
                return result ? 1 : 0;
            }
        });
    }

//    @Override
//    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand){
//        if (!world.isRemote)
//            player.openGui(RTK.instance, 0, world, (int)player.posX, (int)player.posY, (int)player.posZ);
//        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
//    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote)
            player.openGui(RTK.instance, 0, world, (int)player.posX, (int)player.posY, (int)player.posZ);
        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
    }
}
