package rtk.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rtk.ModBlocks;
import rtk.common.CMath;
import rtk.common.CWorld;

public class BlockEmergencyTent extends BlockBaseTent {
    public BlockEmergencyTent(String name){
        super(name);
    }

    @Override
    public IBlockState wall() {
        return ModBlocks.emergencyTentWall.getDefaultState();
    }

    @Override
    public boolean replaceSelf() {
        return true;
    }

    @Override
    public void decorate(World world, BlockPos pos, EntityPlayer player, EnumFacing side){
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        int h = width() - 1;
        int r = h / 2;

        Block light = ModBlocks.emergencyTentLight;

        CWorld.setBlock(world,x + r - 2, y,z + r - 2, ModBlocks.emergencyTentLight);
        CWorld.setBlock(world,- r + 2, y, z + r - 2, ModBlocks.emergencyTentLight);
        CWorld.setBlock(world,x + r - 2, y, z - r + 2, ModBlocks.emergencyTentLight);
        CWorld.setBlock(world,x - r + 2, y, z - r + 2, ModBlocks.emergencyTentLight);

        CWorld.setBlock(world,x + r - 2, y + h, z + r - 2, ModBlocks.emergencyTentLight);
        CWorld.setBlock(world,x - r + 2, y + h, z + r - 2, ModBlocks.emergencyTentLight);
        CWorld.setBlock(world,x + r - 2, y + h, z - r + 2, ModBlocks.emergencyTentLight);
        CWorld.setBlock(world,x - r + 2, y + h, z - r + 2, ModBlocks.emergencyTentLight);
        world.createExplosion(player, x, y, z, 2.0F, false);

        if(CMath.random.nextInt(365) == 0){ //Happy Birthday!
            CWorld.setBlock(world, x + 3, y + 1, z + 3, Blocks.CAKE);
        }
    }
}
