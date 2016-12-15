package rtk.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rtk.common.CMath;

public class BlockTentWall extends BlockBase {

    public BlockTentWall(String name){
        super(Material.CLOTH, name);
        setCreativeTab(CreativeTabs.TOOLS);
        setSoundType(SoundType.CLOTH);
        setHardness(0.5F);
        setResistance(0.5F);
    }

    public static boolean isTentWall(World world, int x, int y, int z){
        return world.getBlockState(new BlockPos(x, y, z)).getBlock() instanceof BlockTentWall;
    }

    public static void tryPop(World world, EntityPlayer player, int x, int y, int z, boolean silent){
        if(isTentWall(world, x, y, z)){

            world.setBlockToAir(new BlockPos(x, y, z));
            if(!silent && !world.isRemote && CMath.random.nextInt(15) == 0)
                world.createExplosion(player, x, y, z, 1.0F, false);

            for(int i = -1; i <= 1; i++){
                for(int j = -1; j <= 1; j++){
                    for(int k = -1; k <= 1; k++){
                        tryPop(world, player, x + i, y + j, z + k, silent);
                    }
                }
            }

            return;
        }
        return;
    }
}
