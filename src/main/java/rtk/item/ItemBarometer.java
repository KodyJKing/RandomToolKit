package rtk.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import rtk.ModItems;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBarometer extends ItemBase {
    public ItemBarometer(String name) {
        super(name);
        setMaxStackSize(1);
        setMaxDamage(256);
        setCreativeTab(CreativeTabs.TOOLS);
    }

    TextComponentTranslation getMessage(EntityPlayer player){
        return new TextComponentTranslation("item.barometer.elevation", new Object[]{Integer.toString((int)player.posY)});
    }

//    @Override
//    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
//        tooltip.add(getMessage(player).getFormattedText());
//        super.addInformation(stack, player, tooltip, advanced);
//    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        int y = (int)entity.posY;
        if (y < 0)
            y = 0;
        else if (y > 256)
            y = 256;
        stack.setItemDamage(256 - y);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote)
            player.sendMessage(getMessage(player));
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return newStack.getItem() != ModItems.barometer;
    }
}
