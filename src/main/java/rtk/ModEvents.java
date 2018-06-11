package rtk;

import net.minecraft.block.Block;
import net.minecraft.block.BlockJukebox;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import rtk.block.ModBlocks;
import rtk.common.CNBT;
import rtk.common.CWorld;
import rtk.common.Common;
import rtk.dimension.ModDimensions;
import rtk.entity.EntitySkeletonPriest;
import rtk.item.ItemToolbelt;
import rtk.item.ModItems;
import rtk.misc.SkeletonRitualScanner;

public class ModEvents {
    // Toolbelt
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
            if (Common.isBaubleEquipped(player, ModItems.toolbelt) > -1)
                ItemToolbelt.selectBestTool(player, -1);
    }

    // Skeleton Ritual
    @SubscribeEvent
    public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event){
        BlockPos pos = event.getPos();
        World world = event.getWorld();

        if (world.isRemote)
            return;

        if (world.getDifficulty() == EnumDifficulty.PEACEFUL)
            return;

        Item item = event.getItemStack().getItem();
        Block block = event.getWorld().getBlockState(pos).getBlock();
        boolean creepyDisk = item == Items.RECORD_11 || item == Items.RECORD_13;
        if (!creepyDisk || block != Blocks.JUKEBOX)
            return;

        EnumFacing ritualDirection = SkeletonRitualScanner.scan(world, pos);
        if (ritualDirection == null)
            return;

        BlockPos spawnPosition = pos.offset(ritualDirection, 6);
//        float spawnAngle = ritualDirection.getHorizontalAngle();

        EntitySkeletonPriest priest = new EntitySkeletonPriest(world);
        priest.setPosition(spawnPosition.getX() + 0.5, spawnPosition.getY() + 0.5, spawnPosition.getZ() + 0.5);
        priest.onInitialSpawn(world.getDifficultyForLocation(spawnPosition), null);
        world.spawnEntity(priest);

        world.addWeatherEffect(new EntityLightningBolt(world, pos.getX(), pos.getY() + 1, pos.getZ(), false));

        event.setCanceled(true);
        world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), ((ItemRecord) item).getSound(), SoundCategory.RECORDS, 1, 1);

        EntityPlayer player = event.getEntityPlayer();
        player.setHeldItem(event.getHand(), ItemStack.EMPTY);
        world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 1, 1);

        world.setWorldTime(18000);
    }

    // Give tent on first spawn.
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

    // No
//    @SubscribeEvent
//    public void onPlayerSleepInBed(PlayerSleepInBedEvent event) {
//        EntityPlayer player = event.getEntityPlayer();
//        World world = player.world;
//        int dimensionID = world.provider.getDimension();
//        if (dimensionID == ModDimensions.darkVoidId || dimensionID == ModDimensions.lightVoidId)
//            event.setResult(EntityPlayer.SleepResult.OK);
//    }
}
