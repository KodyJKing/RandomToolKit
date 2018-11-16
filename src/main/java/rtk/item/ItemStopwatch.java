package rtk.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import rtk.common.CNBT;
import rtk.common.Common;

import java.util.*;

public class ItemStopwatch extends ItemBase {

    public ItemStopwatch(String name) {
        super(name);
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.TOOLS);
    }

    static String startTimeKey = "startTime";
    static String startResourcesKey = "startResources";

    static String targetKey = "target";
    static String targetSideKey = "targetSide";

//    String resourcesDiffKey = "resourcesDiff";

    static String rfKey= "RF";

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote)
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
        tryToggleTimer(world, player, CNBT.ensureCompound(stack), null, null);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (world.isRemote)
            return EnumActionResult.SUCCESS;
        ItemStack stack = player.getHeldItem(hand);
        tryToggleTimer(world, player, CNBT.ensureCompound(stack), pos, side);
        return EnumActionResult.SUCCESS;
    }

    void tryToggleTimer(World world, EntityPlayer player, NBTTagCompound nbt, BlockPos target, EnumFacing side) {
        try {
            toggleTimer(world, player, nbt, target, side);
        } catch (Exception e) {
            System.err.println("An exception occurred while toggling stopwatch. Resetting state...");
            nbt.removeTag(startTimeKey);
            nbt.removeTag(startResourcesKey);
            nbt.removeTag(targetKey);
            nbt.removeTag(targetSideKey);
            e.printStackTrace();
        }
    }

    void toggleTimer(World world, EntityPlayer player, NBTTagCompound nbt, BlockPos target, EnumFacing side) {
        if (nbt.hasKey(startTimeKey)) {

            long dt = world.getTotalWorldTime() - nbt.getLong(startTimeKey);
            player.sendMessage( new TextComponentString(getStopMessage(dt) ) );


            if (nbt.hasKey(targetKey)) {
                BlockPos oldTarget = BlockPos.fromLong(nbt.getLong(targetKey));
                EnumFacing oldSide = EnumFacing.byName(nbt.getString(targetSideKey));
                NBTTagCompound resources = getResources(world, oldTarget, oldSide);
                NBTTagCompound oldResources = nbt.getCompoundTag(startResourcesKey);
                NBTTagCompound diff = diffResources(oldResources, resources);
                List<String> lines = documentDiff(diff, dt);
                if (lines.size() > 0)
                    player.sendMessage( new TextComponentString( String.join("\n", lines) ) );
            }

            nbt.removeTag(startTimeKey);
            nbt.removeTag(startResourcesKey);
            nbt.removeTag(targetKey);

        } else {

            player.sendMessage(new TextComponentTranslation("item.stopwatch.start"));
            nbt.setLong(startTimeKey, world.getTotalWorldTime());

            if (target != null) {
                NBTTagCompound resources = getResources(world, target, side);
                if (resources != null) {
                    nbt.setLong(targetKey, target.toLong());
                    nbt.setString(targetSideKey, side.getName());
                    nbt.setTag(startResourcesKey, resources);
                }
            }

        }
    }

    public NBTTagCompound getResources(World world, BlockPos pos, EnumFacing side) {
        NBTTagCompound result = new NBTTagCompound();

        TileEntity te = world.getTileEntity(pos);
        if (te == null)
            return null;

        IEnergyStorage energy = te.getCapability(CapabilityEnergy.ENERGY, side);
        if (energy != null)
            result.setInteger(rfKey, energy.getEnergyStored());


        IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
        if (itemHandler != null) {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                ItemStack stack = itemHandler.getStackInSlot(i);
                String name = stack.getDisplayName();
                int value = CNBT.ensureInt(result,name, 0);
                result.setInteger(name, value + stack.getCount());
            }
        }

        IFluidHandler fluidHandler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
        if (fluidHandler != null) {
            for (IFluidTankProperties tank: fluidHandler.getTankProperties()) {
                FluidStack stack = tank.getContents();
                if (stack == null)
                    continue;
                String name = stack.getLocalizedName();
                int value = CNBT.ensureInt(result,name, 0);
                result.setInteger(name, value + stack.amount);
            }
        }

        return result;
    }

    public NBTTagCompound diffResources(NBTTagCompound before, NBTTagCompound after) {
        Set<String> keys = new HashSet<>();
        for (String key: before.getKeySet())
            keys.add(key);
        for (String key: after.getKeySet())
            keys.add(key);

        NBTTagCompound result = new NBTTagCompound();

        for (String key: keys) {
            int b = CNBT.ensureInt(before, key, 0);
            int a = CNBT.ensureInt(after, key, 0);
            int d = a - b;
            if (d != 0)
                result.setInteger(key, a - b);
        }

        return result;
    }

    public List<String> documentDiff(NBTTagCompound diff, long ticks) {
        List<String> result = new ArrayList<>();
        for (String key: diff.getKeySet()) {
            int d = diff.getInteger(key);
            String message = String.format(
                    "%s: %s%d, %.2f/%s, %.2f/%s",
                    key,
                    d > 0 ? "+" : "",
                    d,
                    d / (float) ticks,
                    Common.localize("rtk.common.tick"),
                    d * 20.0f / ticks,
                    Common.localize("rtk.common.second")

            );
            result.add(message);
        }
        return result;
    }

    String getStopMessage(long dt) {
        return String.format(
                "%s %s, %s %s",
                Long.toString(dt),
                Common.localize("rtk.common.ticks"),
                Float.toString(dt / 20f),
                Common.localize("rtk.common.seconds")
        );
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        NBTTagCompound nbt = CNBT.ensureCompound(stack);
        if (world.isRemote || !nbt.hasKey(startTimeKey))
            return;
        long dt = world.getTotalWorldTime() - nbt.getLong(startTimeKey);

        if (dt % 20 == 0) {
            SoundEvent sound = dt % 40 == 0 ? SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON : SoundEvents.BLOCK_WOOD_BUTTON_CLICK_OFF;
            world.playSound(null, entity.posX, entity.posY, entity.posZ, sound, SoundCategory.BLOCKS, 0.5f, 1.25f);
        }
    }
}
