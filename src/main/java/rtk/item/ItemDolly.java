package rtk.item;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rtk.common.CNBT;
import rtk.common.CWorld;

public class ItemDolly extends Item {
    public ItemDolly() {
        super(
                new Item.Properties()
                        .group(ItemGroup.TOOLS)
                        .maxStackSize(1)
        );
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        Hand hand = context.getHand();
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        Direction side = context.getFace();

        CompoundNBT nbt = stack.getOrCreateTag();

//        if (world.isRemote)
//            return ActionResultType.PASS;

        if (canPickUp(world, pos) && !nbt.contains("container")) {
            nbt.put("container", CNBT.NBTFromBlock(world, pos));
            CWorld.silentSetBlockStateAndUpdate(world, pos, Blocks.AIR.getDefaultState(), 3);
            return ActionResultType.SUCCESS;
        }

        if (nbt.contains("container") && CWorld.shouldReplace(world, pos.offset(side))) {
            // Todo: Create a context where the itemStack is the block's item rather than the dolly.
            boolean result = CNBT.placeBlockFromNBT(world, pos.offset(side), nbt.getCompound("container"), new BlockItemUseContext(context));
            if (result) {
                nbt.remove("container");
                return ActionResultType.SUCCESS;
            } else {
                return ActionResultType.FAIL;
            }
        }

        return ActionResultType.PASS;
    }

    public static boolean canPickUp(World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        boolean defaultResult = te != null && te instanceof IInventory;

        // TODO: Port configs.
//        String blockName = world.getBlockState(pos).getBlock().getRegistryName().toString();
//
//        if (defaultResult && Arrays.asList(ModConfig.dollyBlacklist).contains(blockName))
//            return false;
//
//        if (!defaultResult && Arrays.asList(ModConfig.dollyWhitelist).contains(blockName))
//            return true;

        return defaultResult;
    }

//
//    @Override
//    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
//        super.addInformation(stack, world, tooltip, flag);
//        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("container")) {
//            NBTTagCompound content = stack.getTagCompound().getCompoundTag("container");
//            IBlockState bs = Block.getStateById(content.getInteger("stateID"));
//            ItemStack fakeStack = new ItemStack(bs.getBlock(), 1, bs.getBlock().getMetaFromState(bs));
//            tooltip.add(TextFormatting.DARK_GRAY + "- " + fakeStack.getDisplayName());
//        }
//    }

}
