package rtk.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import rtk.ModDimensions;

@Optional.Interface(modid = "baubles", iface = "baubles.api.IBauble")
public class ItemVoidStrider extends ItemBase implements IBauble {
    public ItemVoidStrider(String name) {
        super(name);
        setCreativeTab(CreativeTabs.TRANSPORTATION);
        setMaxStackSize(1);
    }

    @Override
    @Optional.Method(modid = "baubles")
    public BaubleType getBaubleType(ItemStack itemstack) { return BaubleType.RING; }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
        doUpdate(player);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entity, int itemSlot, boolean isSelected) {
        doUpdate(entity);
    }

    public void doUpdate(Entity entity) {
        World world = entity.world;
        if (world.isRemote)
            return;
        if (world.provider.getDimension() != 0)
            return;
        EntityPlayerMP player = (EntityPlayerMP) entity;
        if (player == null)
            return;
        if (player.posY > 256)
            player.getServer().getPlayerList().transferPlayerToDimension((EntityPlayerMP)player, ModDimensions.lightVoidId, ModDimensions.getTeleporterLight(world));
        else if (player.posY < -30)
            player.getServer().getPlayerList().transferPlayerToDimension((EntityPlayerMP)player, ModDimensions.darkVoidId, ModDimensions.getTeleporterDark(world));
    }
}
