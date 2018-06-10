package rtk.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.common.Optional;
import rtk.common.CNBT;
import rtk.dimension.ModDimensions;
import rtk.dimension.TeleporterExitDarkVoid;
import rtk.dimension.TeleporterExitLightVoid;
import rtk.dimension.TeleporterVoid;

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

    public void doUpdate(ItemStack stack, Entity entity) {
        World world = entity.world;
        if (world.isRemote)
            return;

        NBTTagCompound nbt = CNBT.ensureCompound(stack);
        nbt.setInteger("cooldown", nbt.getInteger("cooldown") - 1);

        EntityPlayerMP player = (EntityPlayerMP) entity;
        if (player == null)
            return;
        if (world.provider.getDimension() == 0) {
            if (player.posY > 255)
                tryTeleport(stack, player, ModDimensions.lightVoidId, new TeleporterVoid());
            else if (player.posY < -30)
                tryTeleport(stack, player, ModDimensions.darkVoidId, new TeleporterVoid());
        } else if (world.provider.getDimension() == ModDimensions.darkVoidId && player.posY > 255) {
            tryTeleport(stack, player, 0, new TeleporterExitDarkVoid());
        } else if (world.provider.getDimension() == ModDimensions.lightVoidId && player.posY < 0) {
            tryTeleport(stack, player, 0, new TeleporterExitLightVoid());
        }
    }

    public void tryTeleport(ItemStack stack, EntityPlayer player, int dimensionID, ITeleporter teleporter) {
        NBTTagCompound nbt = CNBT.ensureCompound(stack);
        if (nbt.getInteger("cooldown") > 0)
            return;
        ModDimensions.teleportPlayer(player, dimensionID, teleporter);
        nbt.setInteger("cooldown", 80);
    }
}
