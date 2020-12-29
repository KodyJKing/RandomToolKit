package rtk.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import rtk.RTK;
import rtk.common.CMath;
import rtk.common.CWorld;

public class BlockTent extends Block {

    protected BlockState wall;
    protected BlockState light;
    protected int width = 9;
    protected boolean worksInWater = false;
    protected Item fuelType = Items.COAL;
    protected int fuelCost = 8;

    public BlockTent(BlockState wall, BlockState light) {
        super(
                Properties.create(Material.WOOL)
                        .sound(SoundType.CLOTH)
                        .hardnessAndResistance(.5F, .5F)
        );
        this.wall = wall;
        this.light = light;
    }

    public boolean hasFuel(PlayerEntity player) {
        if (player.isCreative())
            return true;
        PlayerInventory inv = player.inventory;
        ItemStack cur = inv.getCurrentItem();
        int cost = fuelCost;
        if (cost == 0)
            return true;
        return cur.getItem() == fuelType && cur.getCount() >= cost;
    }

    public void spendFuel(PlayerEntity player) {
        if (player.isCreative())
            return;
        PlayerInventory inv = player.inventory;
        ItemStack cur = inv.getCurrentItem();
        if (fuelCost == 0 || cur.getItem() == fuelType && cur.getCount() >= fuelCost) {
            inv.decrStackSize(inv.currentItem, fuelCost);
        }
    }

    public boolean shouldReplace(World world, BlockPos pos) {
        BlockState bs = world.getBlockState(pos);
        Block block = bs.getBlock();
        if (block instanceof LeavesBlock)
            return true;
        if (!CWorld.shouldReplace(world, pos))
            return false;
        if (!worksInWater)
            if (bs.getBlock() == Blocks.WATER)
                return false;
        return true;
    }

    public boolean hasRoom(World world, BlockPos pos) {
        for (BlockPos otherPos : tentCuboid(pos))
            if (!pos.equals(otherPos) && !shouldReplace(world, otherPos))
                return false;
        return true;
    }

    public boolean canBuildTent(World world, BlockPos pos, PlayerEntity player) {
        if (!hasRoom(world, pos)) {
//            Common.message(player, Common.localize("tile.basetent.blocked"));
            return false;
        }

        if (!hasFuel(player)) {
//            Common.message(player, Common.localize("tile.basetent.insufficientfuel"));
            return false;
        }

        return true;
    }

    public boolean tryBuildTent(World world, BlockPos pos, PlayerEntity player) {

        if (!canBuildTent(world, pos, player))
            return false;

        spendFuel(player);

        for (BlockPos otherPos : tentCuboid(pos))
            if (!otherPos.equals(pos) && !world.isAirBlock(otherPos))
                world.destroyBlock(otherPos, true);

        int h = width - 1; //Height
        int r = h / 2; //Radius

        fillCuboid(world, pos.add(-r, h, -r), pos.add(r, h, r), wall); //fixed y high
        fillCuboid(world, pos.add(-r, 0, -r), pos.add(r, 0, r), wall); //fixed y low

        fillCuboid(world, pos.add(r, 0, -r), pos.add(r, h, r), wall);  //fixed x high
        fillCuboid(world, pos.add(-r, 0, -r), pos.add(-r, h, r), wall);//fixed x low

        fillCuboid(world, pos.add(-r, 0, r), pos.add(r, h, r), wall);  //fixed z high
        fillCuboid(world, pos.add(-r, 0, -r), pos.add(r, h, -r), wall);//fixed z low

        fillCuboid(world, pos.add(1 - r, 1, 1 - r), pos.add(r - 1, h - 1, r - 1), Blocks.AIR.getDefaultState());

        world.setBlockState(pos.add(r - 2, 0, r - 2), light);
        world.setBlockState(pos.add(- r + 2, 0, r - 2), light);
        world.setBlockState(pos.add(- r + 2, 0, - r + 2), light);
        world.setBlockState(pos.add(r - 2, 0, - r + 2), light);

        world.setBlockState(pos.add(r - 2, h, r - 2), light);
        world.setBlockState(pos.add(- r + 2, h, r - 2), light);
        world.setBlockState(pos.add(- r + 2, h, - r + 2), light);
        world.setBlockState(pos.add(r - 2, h, - r + 2), light);

//        if (CMath.random.nextInt(365) == 0) //Happy Birthday!
//            world.setBlockState(pos.add(3, 1, 3), Blocks.CAKE.getDefaultState());

        onBuilt(world, pos, player);
        return true;
    }


    public void onBuilt(World world, BlockPos pos, PlayerEntity player) {
//        world.createExplosion(player, pos.getX(), pos.getY() + width / 2, pos.getZ(), 0.0F, Explosion.Mode.NONE);
        world.playSound(player, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, .25f, .8f);
    }

    public void fillCuboid(World world, BlockPos a, BlockPos b, BlockState bs) {
        for (BlockPos pos : CMath.cuboid(a, b)) {
            if (world.getBlockState(pos).getBlock().getClass().equals(getClass()))
                continue;
            world.setBlockState(pos, bs, 3);
        }
    }

    public BlockPos[] tentCuboid(BlockPos pos) {
        int h = width - 1; //Height
        int r = h / 2; //Radius

        return CMath.cuboid(pos.add(-r, 0, -r), pos.add(r, h, r));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (tryBuildTent(worldIn,pos, player))
            return ActionResultType.SUCCESS;
        return ActionResultType.FAIL;
    }

    @Override
    public void onPlayerDestroy(IWorld world, BlockPos pos, BlockState state) {
        cleanup(world, pos);
        super.onPlayerDestroy(world, pos, state);
    }

    private void cleanup(IWorld world, BlockPos pos) {
        for (BlockPos otherPos : tentCuboid(pos)){
            BlockState bs = world.getBlockState(otherPos);
            Block block = bs.getBlock();
            if (block == wall.getBlock() || block == light.getBlock())
                world.setBlockState(otherPos, Blocks.AIR.getDefaultState(), 3);
        }
    }
}
