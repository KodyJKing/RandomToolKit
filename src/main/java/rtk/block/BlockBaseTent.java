package rtk.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockBaseTent extends BlockBase {

    public BlockBaseTent(String name) {
        super(Material.CLOTH, name);
        setCreativeTab(CreativeTabs.TOOLS);
        setSoundType(SoundType.CLOTH);
        setHardness(0.5F);
        setResistance(0.5F);
    }

    public IBlockState wall(){
        return Blocks.WOOL.getDefaultState();
    }

    public int width(){
        return 9;
    }

    public boolean replaceSelf(){
        return false;
    }

    public boolean worksInWater(){
        return false;
    }

    public boolean canBuildTent(World world, BlockPos pos){
        int h = width() - 1; //Height
        int r = h / 2; //Radius

        return isCuboidEmpty(world, pos.getX() + r, pos.getY() + h, pos.getZ() + r, pos.getX() - r, pos.getY(), pos.getZ() - r);
    }

    public boolean tryBuildTent(World world, BlockPos pos, EntityPlayer player, EnumFacing side){
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        if(!canBuildTent(world, pos)){
            player.addChatComponentMessage(new TextComponentTranslation("tile.baseTent.blocked"));
            return false;
        }

        int h = width() - 1; //Height
        int r = h / 2; //Radius

        fillCuboid(world, x + r, y + h, z + r, x - r, y + h, z - r, wall());//y flat high (top)
        fillCuboid(world, x + r, y, z + r, x - r, y, z - r, wall());//y flat low (bottom)

        fillCuboid(world, x + r, y, z + r, x + r, y + h, z - r, wall());//x flat high
        fillCuboid(world, x - r, y, z + r, x - r, y + h, z - r, wall());//x flat low

        fillCuboid(world, x + r, y, z + r, x - r, y + h, z + r, wall());//z flat high
        fillCuboid(world, x + r, y, z - r, x - r, y + h, z - r, wall());//z flat low

        h -= 1;
        r -= 1;

        fillCuboid(world, x + r, y + 1, z + r, x - r, y + h, z - r, Blocks.AIR.getDefaultState());

        decorate(world, pos, player, side);
        return true;
    }

    public void decorate(World world, BlockPos pos, EntityPlayer player, EnumFacing side){
        world.createExplosion(player, pos.getX(), pos.getY(), pos.getZ(), 2.0F, false);
    }

    public void fillCuboid(World world, int x, int y, int z, int dx, int dy, int dz, IBlockState bs){
        for(int i = Math.min(x, dx); i <= Math.max(x, dx); i++){
            for(int j = Math.min(y, dy); j <= Math.max(y, dy); j++){
                for(int k = Math.min(z, dz); k <= Math.max(z, dz); k++){
                    BlockPos pos = new BlockPos(i, j, k);
                    if(!replaceSelf() && world.getBlockState(pos).getBlock().getClass().equals(getClass())){
                        continue;
                    }
                    world.setBlockState(pos, bs, 3);
                }
            }
        }
    }

    public boolean isCuboidEmpty(World world, int x, int y, int z, int dx, int dy, int dz){
        for(int i = Math.min(x, dx); i <= Math.max(x, dx); i++){
            for(int j = Math.min(y, dy); j <= Math.max(y, dy); j++){
                for(int k = Math.min(z, dz); k <= Math.max(z, dz); k++){
                    IBlockState bs = world.getBlockState(new BlockPos(i, j, k));
                    if(bs.getBlock().getClass().equals(getClass()))
                        continue;
                    if(bs.isOpaqueCube())
                        return false;
                    if(!worksInWater()){
                        if(bs.getBlock() == Blocks.WATER || bs.getBlock() == Blocks.FLOWING_WATER)
                            return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote)
            return tryBuildTent(world, pos, player, side);
        return true;
    }
}
