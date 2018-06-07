package rtk.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import rtk.ModBlocks;
import rtk.ModConfig;
import rtk.common.CMath;
import rtk.common.CWorld;

public class BlockTent extends BlockBase {

    public BlockTent(String name) {
        super(Material.CLOTH, name);
        setCreativeTab(CreativeTabs.TRANSPORTATION);
        setSoundType(SoundType.CLOTH);
        setHardness(0.5F);
        setResistance(0.5F);
    }

    public IBlockState wall(){ return ModBlocks.tentWall.variant(1); }

    public int width(){
        return 9;
    }

    public boolean worksInWater(){
        return false;
    }

    public int fuelCost(){return ModConfig.tentFuelCost.tent;}

    public Item fuelType(){return Items.COAL;}

    public boolean hasFuel(EntityPlayer player){
        if (player.capabilities.isCreativeMode)
            return true;
        InventoryPlayer inv = player.inventory;
        ItemStack cur = inv.getCurrentItem();
        return cur != null && cur.getItem() == fuelType() && cur.getCount() >= fuelCost();
    }

    public void spendFuel(EntityPlayer player){
        if (player.capabilities.isCreativeMode)
            return;
        InventoryPlayer inv = player.inventory;
        ItemStack cur = inv.getCurrentItem();
        if (fuelCost() == 0 || cur.getItem() == fuelType() && cur.getCount() >= fuelCost()){
            inv.decrStackSize(inv.currentItem, fuelCost());
        }
    }

    public boolean shouldReplace(World world, BlockPos pos){
        IBlockState bs = world.getBlockState(pos);
        Block block = bs.getBlock();
        if (block instanceof BlockLeaves || block instanceof BlockDirt || block instanceof BlockGrass || block instanceof BlockSand || block instanceof BlockGravel)
            return true;
        if (!CWorld.shouldReplace(world, pos))
            return false;
        if (!worksInWater())
            if (bs.getBlock() == Blocks.WATER || bs.getBlock() == Blocks.FLOWING_WATER)
                return false;
        return true;
    }

    public boolean hasRoom(World world, BlockPos pos){
        for (BlockPos otherPos : tentCuboid(pos))
            if (!pos.equals(otherPos) && !shouldReplace(world, otherPos))
                return false;
        return true;
    }

    public boolean canBuildTent(World world, BlockPos pos, EntityPlayer player){
        if (!hasRoom(world, pos)){
            player.sendMessage(new TextComponentTranslation("tile.basetent.blocked"));
            return false;
        }

        if (!hasFuel(player)){
            player.sendMessage(new TextComponentTranslation("tile.basetent.insufficientfuel"));
            return false;
        }

        return true;
    }

    public boolean tryBuildTent(World world, BlockPos pos, EntityPlayer player, EnumFacing side){

        if (!canBuildTent(world, pos, player))
            return false;

        spendFuel(player);

        int h = width() - 1; //Height
        int r = h / 2; //Radius

        fillCuboid(world, pos.add(-r, h, -r), pos.add(r, h, r), wall()); //fixed y high
        fillCuboid(world, pos.add(-r, 0, -r), pos.add(r, 0, r), wall()); //fixed y low

        fillCuboid(world, pos.add(r, 0, -r), pos.add(r, h, r), wall());  //fixed x high
        fillCuboid(world, pos.add(-r, 0, -r), pos.add(-r, h, r), wall());//fixed x low

        fillCuboid(world, pos.add(-r, 0, r), pos.add(r, h, r), wall());  //fixed z high
        fillCuboid(world, pos.add(-r, 0, -r), pos.add(r, h, -r), wall());//fixed z low

        fillCuboid(world, pos.add(1 - r, 1, 1 - r), pos.add(r - 1, h - 1, r - 1), Blocks.AIR.getDefaultState());

        decorate(world, pos, player, side);
        return true;
    }

    public void decorate(World world, BlockPos pos, EntityPlayer player, EnumFacing side){
        world.createExplosion(player, pos.getX(), pos.getY(), pos.getZ(), 0.0F, false);
    }

    public void fillCuboid(World world, BlockPos a, BlockPos b, IBlockState bs){
        for (BlockPos pos : CMath.cuboid(a, b)){
            if (world.getBlockState(pos).getBlock().getClass().equals(getClass()))
                continue;
            world.setBlockState(pos, bs, 3);
        }
    }

    public BlockPos[] tentCuboid(BlockPos pos){
        int h = width() - 1; //Height
        int r = h / 2; //Radius

        return CMath.cuboid(pos.add(-r, 0, -r), pos.add(r, h, r));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote)
            return tryBuildTent(world, pos, player, side);
        return true;
    }
}
