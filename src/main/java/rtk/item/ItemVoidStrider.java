package rtk.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.common.Optional;
import rtk.dimension.ModDimensions;
import rtk.dimension.TeleporterExitDarkVoid;
import rtk.dimension.TeleporterExitLightVoid;
import rtk.dimension.TeleporterVoid;

import java.util.HashSet;
import java.util.UUID;

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
    public void onWornTick(ItemStack stack, EntityLivingBase player) {
        doUpdate(stack, player);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entity, int itemSlot, boolean isSelected) {
        doUpdate(stack, entity);
    }

    private static HashSet<UUID> dontTP = new HashSet<UUID>();

    public void doUpdate(ItemStack stack, Entity entity) {
        World world = entity.world;
        if (world.isRemote)
            return;

        EntityPlayerMP player = (EntityPlayerMP) entity;

        if (player == null)
            return;
        if (player.posY < 255 && player.posY > 0)
            dontTP.remove(player.getUniqueID());
        if (dontTP.contains(player.getUniqueID()))
            return;

        int dim = world.provider.getDimension();

        if (dim == 0) {
            if (player.posY > 255)
                teleport(player, ModDimensions.lightVoidId, new TeleporterVoid());
            else if (player.posY < -30)
                teleport(player, ModDimensions.darkVoidId, new TeleporterVoid());
        } else if (dim == ModDimensions.darkVoidId && player.posY > 255) {
            teleport(player, 0, new TeleporterExitDarkVoid());
        } else if (dim == ModDimensions.lightVoidId && player.posY < 0) {
            teleport(player, 0, new TeleporterExitLightVoid());
        }
    }

    public void teleport(EntityPlayer player, int dimensionID, ITeleporter teleporter) {
        dontTP.add(player.getUniqueID());
        ModDimensions.teleportPlayer(player, dimensionID, teleporter);
    }
}
