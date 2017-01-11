package rtk.common;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class Common {
    public static Random random = new Random();

    public static void formatToolTip(String key, List<String> target){
        if(!I18n.hasKey(key))
            return;
        String[] lines = I18n.format(key).split(";");
        for(String line : lines)
            target.add(line);
    }

    public static boolean shouldReplace(World world, BlockPos pos){
        IBlockState bs = world.getBlockState(pos);
        return bs.getBlock().isReplaceable(world, pos) || !bs.isOpaqueCube() && bs.getBlockHardness(world, pos) < 0.01F || bs.getMaterial() == Material.AIR;
    }

    public static Vec3d randomVector(){
        return new Vec3d(Common.random.nextGaussian(), Common.random.nextGaussian(), Common.random.nextGaussian());
    }

    public static Vec3d randomVector(double length){
        return randomVector().normalize().scale(length);
    }

    public static Vec3d randomVector(double length, double angleSpread, Vec3d heading){
        if(Math.abs(angleSpread) < Math.PI / 180)
            return heading;

        double minDot = Math.cos(angleSpread) * length * heading.lengthVector();
        for(int i = 0; i < 100; i++){
            Vec3d result = randomVector(length);
            if(result.dotProduct(heading) >= minDot)
                return result;
        }

        return heading; //If we didn't hit it after 50 tries the target must be small so using the heading is fair.
    }

    public static Vec3d randomVector(Vec3d heading, double length, double inaccuracy){
        return heading.normalize().add(randomVector().scale(0.007499999832361937D * inaccuracy)).normalize().scale(length);
    }

    public static int findExactStack(IInventory inventory, ItemStack stack){
        for(int i = 0; i < inventory.getSizeInventory(); i++){
            if(inventory.getStackInSlot(i) == stack)
                return i;
        }
        return -1;
    }

    public static BlockPos[] cuboid(BlockPos a, BlockPos b){
        int xMin = Math.min(a.getX(), b.getX());
        int yMin = Math.min(a.getY(), b.getY());
        int zMin = Math.min(a.getZ(), b.getZ());

        int xMax = Math.max(a.getX(), b.getX());
        int yMax = Math.max(a.getY(), b.getY());
        int zMax = Math.max(a.getZ(), b.getZ());


        BlockPos[] positions = new BlockPos[(1 + xMax - xMin) * (1 + yMax - yMin) * (1 + zMax - zMin)];
        int i = 0;

        for(int x = xMin; x <= xMax; x++){
            for(int y = yMin; y <= yMax; y++){
                for(int z = zMin; z <= zMax; z++){
                    positions[i++] = new BlockPos(x, y, z);
                }
            }
        }

        return positions;
    }

    public static Vec3d getTrueCenter(Entity entity){
        AxisAlignedBB box = entity.getEntityBoundingBox();
        return new Vec3d(box.minX + 0.5 * (box.maxX - box.minX), box.minY + 0.5 * (box.maxY - box.minY), box.minZ + 0.5 * (box.maxZ - box.minZ));
    }

    public static void safeReplaceBlock(World world, BlockPos pos, IBlockState state, int flags){
        TileEntity te = world.getTileEntity(pos);
        if(te == null){
            world.setBlockState(pos, state, flags);
            return;
        }
        IBlockState formerState = world.getBlockState(pos);
        world.setTileEntity(pos, formerState.getBlock().createTileEntity(world, formerState));
        try {
            world.setBlockState(pos, state, flags);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ItemStack getRefill(EntityPlayer player, ItemStack stack, int amount){
        IInventory inv = player.inventory;
        for(int i = 0; i < inv.getSizeInventory(); i++){
            ItemStack other = inv.getStackInSlot(i);
            if(other != null && other.isItemEqual(stack)){
                player.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1, 2);
                return inv.decrStackSize(i, amount);
            }
        }
        return null;
    }
}
