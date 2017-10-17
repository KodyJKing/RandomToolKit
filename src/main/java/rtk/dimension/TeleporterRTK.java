package rtk.dimension;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleporterRTK extends Teleporter{
    WorldServer world;

    public TeleporterRTK(WorldServer worldIn) {
        super(worldIn);
        world = worldIn;
    }

//    @Override
//    public void placeInPortal(Entity entityIn, float rotationYaw) {
//        super.placeInPortal(entityIn, rotationYaw);
//    }

    @Override
    public boolean placeInExistingPortal(Entity entityIn, float rotationYaw) {
        int i = MathHelper.floor_double(entityIn.posX);
        int k = MathHelper.floor_double(entityIn.posZ);
        world.setBlockState(new BlockPos(i, 64, k), Blocks.STONE.getDefaultState());
        entityIn.setPosition(i + 0.5, 65, k + 0.5);
        return true;
    }

    @Override
    public boolean makePortal(Entity entityIn) {
//        return super.makePortal(entityIn);
        return true;
    }

//    @Override
//    public void removeStalePortalLocations(long worldTime) {
//        super.removeStalePortalLocations(worldTime);
//    }
}
