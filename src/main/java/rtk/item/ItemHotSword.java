package rtk.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import rtk.RTK;

import javax.annotation.Nullable;

public class ItemHotSword extends ItemSword {

    String name;

    public ItemHotSword(String name) {
        super(ToolMaterial.IRON);
        this.name = name;
        setUnlocalizedName(name);
        setRegistryName(name);
        setMaxDamage(3);

        RTK.proxy.registerItemRenderer(this, 0, name);

        addPropertyOverride(new ResourceLocation("heat"), new IItemPropertyGetter() {
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
                return stack.getMetadata();
            }
        });
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    public int getHeat(ItemStack stack) {
        return 4 - stack.getMetadata();
    }

    public void setHeat(ItemStack stack, int heat) {
        stack.setItemDamage(4 - heat);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (!(attacker instanceof EntityPlayer))
            return true;

        EntityPlayer player = (EntityPlayer) attacker;

        target.hurtResistantTime = 0;
        target.attackEntityFrom(DamageSource.causePlayerDamage(player).setFireDamage(), getHeat(stack) * 4);

        target.setFire(5);

        if (getHeat(stack) == 1)
            player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.IRON_SWORD));
        else
            setHeat(stack, getHeat(stack) - 1);

        player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 1, 1);

        return true;
    }
}
