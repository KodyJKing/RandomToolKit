package rtk;

import baubles.api.BaublesApi;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import rtk.common.CNBT;
import rtk.entity.EntitySkeletonPriest;
import rtk.item.ItemToolbelt;
import rtk.misc.SkeletonRitualScanner;

public class ModEvents {
    @SubscribeEvent
    public void onPlayerLeftClickBlock(PlayerInteractEvent.LeftClickBlock event){
        EntityPlayer player = event.getEntityPlayer();

        // Check their main inventory.
        for (int i = 0; i < player.inventory.getSizeInventory(); i++){
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemToolbelt){
                ItemToolbelt.selectBestTool(player, i);
                return;
            }
        }

        if (RTK.baublesLoaded)
            if (BaublesApi.isBaubleEquipped(player, ModItems.toolbelt) > -1)
                ItemToolbelt.selectBestTool(player, -1);
    }

    @SubscribeEvent
    public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event){
        BlockPos pos = event.getPos();
        World world = event.getWorld();
        EntityPlayer player = event.getEntityPlayer();

        if (world.isRemote)
            return;

        Item item = event.getItemStack().getItem();
        Block block = event.getWorld().getBlockState(pos).getBlock();
        boolean creepyDisk = item == Items.RECORD_11 || item == Items.RECORD_13;
        if (!creepyDisk || block != Blocks.JUKEBOX)
            return;

        EnumFacing ritualDirection = SkeletonRitualScanner.scan(world, pos);
        if (ritualDirection == null)
            return;

        BlockPos spawnPosition = pos.offset(ritualDirection, 3);
        float spawnAngle = ritualDirection.getHorizontalAngle();

        EntitySkeletonPriest priest = new EntitySkeletonPriest(world);
        priest.setPosition(spawnPosition.getX() + 0.5, spawnPosition.getY() + 0.5, spawnPosition.getZ() + 0.5);
        world.spawnEntity(priest);
        System.out.println(ritualDirection.toString());
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event){
        if (event.player.world.isRemote || event.player.capabilities.isCreativeMode)
            return;

        NBTTagCompound playerData = event.player.getEntityData();
        NBTTagCompound persist = CNBT.ensureCompound(playerData, EntityPlayer.PERSISTED_NBT_TAG);

        if (!persist.getBoolean("GivenTent")){
            event.player.inventory.addItemStackToInventory(new ItemStack(ModBlocks.emergencyTent));
            persist.setBoolean("GivenTent", true);
            playerData.setTag(EntityPlayer.PERSISTED_NBT_TAG, persist);
        }
    }
}
