package rtk.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rtk.ModBlocks;
import rtk.common.Common;

import java.util.Random;

public class BlockEmergencyTent extends BlockTentBreakable {
    public BlockEmergencyTent(String name){
        super(name);
    }

    @Override
    public IBlockState wall() {
        return ModBlocks.tentWall.variant(0);
    }

    @Override
    public boolean hasFuel(EntityPlayer player) { return true; }

    @Override
    public void spendFuel(EntityPlayer player) {}

    @Override
    public void decorate(World world, BlockPos pos, EntityPlayer player, EnumFacing side){
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        int h = width() - 1;
        int r = h / 2;

        IBlockState light = ModBlocks.tentWall.variant(4);

        world.setBlockState(pos.add(r - 2, 0, r - 2), light);
        world.setBlockState(pos.add(- r + 2, 0, r - 2), light);
        world.setBlockState(pos.add(- r + 2, 0, - r + 2), light);
        world.setBlockState(pos.add(r - 2, 0, - r + 2), light);

        world.setBlockState(pos.add(r - 2, h, r - 2), light);
        world.setBlockState(pos.add(- r + 2, h, r - 2), light);
        world.setBlockState(pos.add(- r + 2, h, - r + 2), light);
        world.setBlockState(pos.add(r - 2, h, - r + 2), light);

        world.createExplosion(player, x, y, z, 2.0F, false);

        if(Common.random.nextInt(365) == 0) //Happy Birthday!
            world.setBlockState(pos.add(3, 1, 3), Blocks.CAKE.getDefaultState());
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }
}
