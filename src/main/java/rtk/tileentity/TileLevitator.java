package rtk.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import rtk.block.BlockLevitator;
import rtk.common.CMath;

import java.util.List;

public class TileLevitator extends TileEntity implements ITickable {

    @Override
    public void update() {
        if (!world.isBlockPowered(pos))
            return;

        IBlockState bs = world.getBlockState(pos);
        EnumFacing dir = bs.getValue(BlockLevitator.FACING);

        int length = getLength();

        List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos, pos.south().east().up().offset(dir, length)));
        for (Entity e : entities){
            int sign = e.isSneaking() ? -1 : 1;

            e.motionY *= 0.1;
            e.motionY += 0.08;

            switch (dir.getAxis()){
                case X:
                    e.motionX *= 0.1;
                    e.motionX += 0.3 * dir.getFrontOffsetX();
                    break;
                case Y:
                    e.motionY += 0.3 * dir.getFrontOffsetY() * sign;
                    break;
                case Z:
                    e.motionZ *= 0.1;
                    e.motionZ += 0.3 * dir.getFrontOffsetZ();
            }

            e.fallDistance = 0;
        }

        if (!world.isRemote)
            return;

        for (int i = 1; i <= length; i++){
            BlockPos p = pos.offset(dir, i);
            Vec3d posVec = CMath.randomVector(0.5).addVector(0.5, 0.5, 0.5).addVector(p.getX(), p.getY(), p.getZ());
            Vec3d vel = CMath.randomVector(0.1).addVector(dir.getFrontOffsetX() * -1, dir.getFrontOffsetY() * -1, dir.getFrontOffsetZ() * -1);
            world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, posVec.x, posVec.y, posVec.z, vel.x, vel.y, vel.z);
        }

    }

    public int getLength(){
        IBlockState bs = world.getBlockState(pos);
        EnumFacing dir = bs.getValue(BlockLevitator.FACING);

        for (int i = 1; i <= 9; i++){
            BlockPos p = pos.offset(dir, i);
            if (world.getBlockState(p).isOpaqueCube())
                return i;
        }

        return 9;
    }
}
