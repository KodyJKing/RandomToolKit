package rtk.common;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
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

    public static int ensureInt(NBTTagCompound nbt, String name){
        return ensureInt(nbt, name, 0);
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

    public static <T extends NBTBase> T ensure(NBTTagCompound nbt, String name, T defaultValue){
        if(!nbt.hasKey(name))
            nbt.setTag(name, defaultValue);
        return (T)nbt.getTag(name);
    }

}
