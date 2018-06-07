package rtk.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import rtk.common.CMath;

public class ItemHotplate extends ItemBase {

    public boolean etched;

    public ItemHotplate(String name, boolean etched){
        super(name);
        this.etched = etched;
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.TOOLS);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        int dx = facing.getFrontOffsetX();
        int dy = facing.getFrontOffsetY();
        int dz = facing.getFrontOffsetZ();

        dx = dx == 0 ? 1 : 0;
        dy = dy == 0 ? 1 : 0;
        dz = dz == 0 ? 1 : 0;

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        boolean didMelt = false;
        for (int xx = x - dx; xx <= x + dx; xx++){
            for (int yy = y - dy; yy <= y + dy; yy++){
                for (int zz = z - dz; zz <= z + dz; zz++){
                    BlockPos pos2 = new BlockPos(xx, yy, zz);

                    Block block = world.getBlockState(pos2).getBlock();
                    Block replace = null;

                    if (block == Blocks.COBBLESTONE || block == Blocks.GRAVEL || (etched && block == Blocks.STONE))
                        replace = etched ? Blocks.STONEBRICK : Blocks.STONE;
                    if (etched && block == Blocks.STONE_STAIRS)
                        replace = Blocks.STONE_BRICK_STAIRS;
                    if (etched && block == Blocks.NETHERRACK)
                        replace = Blocks.NETHER_BRICK;

                    if (replace != null){
                        didMelt = true;
                        IBlockState inBS = world.getBlockState(pos2);
                        IBlockState outBS = replace.getDefaultState();
                        EnumFacing blockDir;
                        try {
                            blockDir = inBS.getValue(BlockHorizontal.FACING);
                            outBS = outBS.withProperty(BlockHorizontal.FACING, blockDir);
                        } catch (Exception e){}
                        world.setBlockState(pos2, outBS);
                        spawnSmoke(world, xx, yy, zz);
                    }
                }
            }
        }

        if (didMelt)
            world.playSound(null, x, y, z, SoundEvents.BLOCK_REDSTONE_TORCH_BURNOUT, SoundCategory.BLOCKS, 0.1F + CMath.random.nextFloat() * 0.2F, 1.0F);

        player.swingArm(hand);
        return EnumActionResult.SUCCESS;
    }

    public void spawnSmoke(World world, int x, int y, int z){
        for (int i = 0; i <= 10; i++){
            Vec3d pos = CMath.randomVector(0.5F);
            Vec3d dir = CMath.randomVector(0.1F);
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
                    x + 0.5 + pos.x, y + 0.5 + pos.y, z + 0.5 + pos.z,
                    dir.z, dir.y, dir.z);
        }
    }
}
