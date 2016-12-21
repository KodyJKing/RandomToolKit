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
import rtk.common.Common;

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

        for(BlockPos other : tentCuboid(pos)){
            IBlockState bs = world.getBlockState(other);
            if(bs.getBlock().getClass().equals(getClass()))
                continue;
            if(!Common.shouldReplace(world, other))
                return false;
            if(!worksInWater()){
                if(bs.getBlock() == Blocks.WATER || bs.getBlock() == Blocks.FLOWING_WATER)
                    return false;
            }
        }

        return true;
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

        fillCuboid(world, pos.add(-r, h, -r), pos.add(r, h, r), wall()); //fixed y high (top)
        fillCuboid(world, pos.add(-r, 0, -r), pos.add(r, 0, r), wall()); //fixed y low (bottom)

        fillCuboid(world, pos.add(r, 0, -r), pos.add(r, h, r), wall());  //fixed x high
        fillCuboid(world, pos.add(-r, 0, -r), pos.add(-r, h, r), wall());//fixed x low

        fillCuboid(world, pos.add(-r, 0, r), pos.add(r, h, r), wall());  //fixed z high
        fillCuboid(world, pos.add(-r, 0, -r), pos.add(r, h, -r), wall());//fixed z low

        fillCuboid(world, pos.add(1 - r, 1, 1 - r), pos.add(r - 1, h - 1, r - 1), Blocks.AIR.getDefaultState());

        decorate(world, pos, player, side);
        return true;
    }

    public void decorate(World world, BlockPos pos, EntityPlayer player, EnumFacing side){
        world.createExplosion(player, pos.getX(), pos.getY(), pos.getZ(), 2.0F, false);
    }

    public void fillCuboid(World world, BlockPos a, BlockPos b, IBlockState bs){
        for(BlockPos pos : Common.cuboid(a, b)){
            if(!replaceSelf() && world.getBlockState(pos).getBlock().getClass().equals(getClass()))
                continue;
            world.setBlockState(pos, bs, 3);
        }
    }

    public BlockPos[] tentCuboid(BlockPos pos){
        int h = width() - 1; //Height
        int r = h / 2; //Radius

        return Common.cuboid(pos.add(-r, 0, -r), pos.add(r, h, r));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote)
            return tryBuildTent(world, pos, player, side);
        return true;
    }
}
