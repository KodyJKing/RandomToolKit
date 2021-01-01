package rtk.common;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rtk.RTK;

public class CNBT {
    @Deprecated // Prefer ItemStack.getOrCreateTag()
    public static CompoundNBT ensureCompound(ItemStack stack) {
        if (!stack.hasTag())
            stack.setTag(new CompoundNBT());
        return stack.getTag();
    }

    public static int ensureInt(CompoundNBT nbt, String name, int defaultValue) {
        if (!nbt.contains(name))
            nbt.putInt(name, defaultValue);
        return nbt.getInt(name);
    }

    public static CompoundNBT ensureCompound(CompoundNBT nbt, String name) {
        if (!nbt.contains(name))
            nbt.put(name, new CompoundNBT());
        return nbt.getCompound(name);
    }

    public static CompoundNBT NBTFromBlock(World world, BlockPos pos) {
        CompoundNBT NBT = new CompoundNBT();

        NBT.putInt("stateID", Block.getStateId(world.getBlockState(pos)));

        TileEntity tile = world.getTileEntity(pos);
        if (tile != null)
            NBT.put("tileEntity", tile.serializeNBT());

        return NBT;
    }

    public static void placeBlockFromNBT(World world, BlockPos pos, CompoundNBT NBT) {
        BlockState bs = Block.getStateById(NBT.getInt("stateID"));

//        // We need to use this to avoid onBlockAdded getting called
//        // causing things like furnaces to reset their facing direction.
        CWorld.silentSetBlockStateAndUpdate(world, pos, bs, 2);

        if (NBT.contains("tileEntity")) {
            TileEntity tile = TileEntity.readTileEntity(bs, NBT.getCompound("tileEntity"));
            world.setTileEntity(pos, tile);
        }
    }

    public static boolean placeBlockFromNBT(World world, BlockPos pos, CompoundNBT NBT, BlockItemUseContext ctx) {
        BlockState bs = Block.getStateById(NBT.getInt("stateID"));

        ItemStack stack = new ItemStack(bs.getBlock());
        Item item = stack.getItem();
        BlockItem blockItem = (BlockItem) item;
        if (blockItem == null)
            return false;

        ActionResultType result = blockItem.tryPlace(ctx);

        result.isSuccessOrConsume();
        if (!result.isSuccessOrConsume())
            return false;

        if (NBT.contains("tileEntity")) {
            TileEntity tile = TileEntity.readTileEntity(bs, NBT.getCompound("tileEntity"));
            world.setTileEntity(pos, tile);
        }

        return true;

//        bs.getBlock().getStateForPlacement(ctx);
//        world.setBlockState(pos, bs, 2);

//        try {
//            bs.getBlock().onBlockPlacedBy(world, pos, bs, ctx.getPlayer(), new ItemStack(bs.getBlock(), 1));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }
}
