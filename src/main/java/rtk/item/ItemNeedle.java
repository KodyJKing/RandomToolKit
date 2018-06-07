package rtk.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySlime;
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
        setMaxDamage(3);
        setCreativeTab(CreativeTabs.TOOLS);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        BlockTentWall.tryPop(world, pos);
        return EnumActionResult.SUCCESS;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (target instanceof EntitySlime){
            target.world.createExplosion(attacker, target.posX,  target.posY,  target.posZ, ((EntitySlime) target).getSlimeSize(), false);
            stack.damageItem(1, attacker);
            return true;
        }

        return false;
    }
}
