package rtk.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rtk.entity.EntityEyeOfNether;

public class ItemEyeOfNether extends ItemBase {
    public ItemEyeOfNether(String name) {
        super(name);
        setCreativeTab(CreativeTabs.TOOLS);
        setMaxStackSize(1);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);

        if (world.isRemote)
            return EnumActionResult.SUCCESS;

        EntityEyeOfNether e = new EntityEyeOfNether(world);
        pos = pos.offset(facing);
        e.setLocationAndAngles(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0);
        world.spawnEntity(e);

        stack.setCount(stack.getCount() - 1);

        return EnumActionResult.SUCCESS;
    }
}
