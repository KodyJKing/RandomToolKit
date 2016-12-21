package rtk.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rtk.common.Common;

public class BlockTentWall extends BlockBase {

    public BlockTentWall(String name){
        super(Material.CLOTH, name);
        //setCreativeTab(CreativeTabs.TOOLS);
        setSoundType(SoundType.CLOTH);
        setHardness(0.5F);
        setResistance(0.5F);
    }

    public static boolean isTentWall(World world, BlockPos pos){
        return world.getBlockState(pos).getBlock() instanceof BlockTentWall;
    }

    public static void tryPop(World world, EntityPlayer player, BlockPos pos){
        if(isTentWall(world, pos)){

            world.setBlockToAir(pos);
            if(!world.isRemote && Common.random.nextInt(15) == 0)
                world.createExplosion(player, pos.getX(), pos.getY(), pos.getZ(), 1.0F, false);

            for(BlockPos otherPos : Common.cuboid(pos.add(-1, -1, -1), pos.add(1, 1, 1))){
                tryPop(world, player, otherPos);
            }
        }
    }
}
