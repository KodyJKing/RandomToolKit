package rtk.common;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CNBT {
    public static NBTTagCompound ensureCompound(ItemStack stack){
        if(!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());
        return stack.getTagCompound();
    }

    public static int ensureInt(NBTTagCompound nbt, String name, int defaultValue){
        if(!nbt.hasKey(name))
            nbt.setInteger(name, defaultValue);
        return nbt.getInteger(name);
    }

    public static NBTTagCompound ensureCompound(NBTTagCompound nbt, String name){
        if(!nbt.hasKey(name))
            nbt.setTag(name, new NBTTagCompound());
        return nbt.getCompoundTag(name);
    }

    public static NBTTagCompound NBTFromBlock(World world, BlockPos pos){
        NBTTagCompound NBT = new NBTTagCompound();

        NBT.setInteger("stateID", Block.getStateId(world.getBlockState(pos)));

        TileEntity tile = world.getTileEntity(pos);
        if(tile != null){
            NBTTagCompound tileNBT = new NBTTagCompound();
            tile.writeToNBT(tileNBT);
            NBT.setTag("tileEntity", tileNBT);
        }

        return NBT;
    }

    public static void placeBlockFromNBT(World world, BlockPos pos, NBTTagCompound NBT){
        IBlockState bs = Block.getStateById(NBT.getInteger("stateID"));

        world.setBlockState(pos, bs, 2);

        if(NBT.hasKey("tileEntity")){
            TileEntity tile = TileEntity.create(world, (NBTTagCompound)NBT.getTag("tileEntity"));
            world.setTileEntity(pos, tile);
        }
    }

    public static void placeBlockFromNBT(World world, BlockPos pos, NBTTagCompound NBT, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ){
        IBlockState bs = Block.getStateById(NBT.getInteger("stateID"));

        int meta = bs.getBlock().getMetaFromState(bs);
        bs = bs.getBlock().getStateForPlacement(world, pos, side, hitX, hitY, hitZ, meta, player, new ItemStack(bs.getBlock(), 1, meta));

        world.setBlockState(pos, bs, 2);

        if(NBT.hasKey("tileEntity")){
            TileEntity tile = TileEntity.create(world, (NBTTagCompound)NBT.getTag("tileEntity"));
            world.setTileEntity(pos, tile);
        }

        try {
            bs.getBlock().onBlockPlacedBy(world, pos, bs, player, new ItemStack(bs.getBlock(), 1, meta));
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
