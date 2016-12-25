package rtk.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rtk.block.BlockTentWall;

public class ItemNeedle extends ItemBase {
    public ItemNeedle(String name){
        super(name);
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.TOOLS);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        BlockTentWall.tryPop(world, pos);
        return EnumActionResult.SUCCESS;
    }
}
