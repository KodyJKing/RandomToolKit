package rtk.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import rtk.ModItems;
import rtk.common.CMath;

import javax.annotation.Nullable;
import java.awt.color.CMMException;
import java.util.List;

public class EntityEyeOfNether extends EntityLiving {

    int cooldown = 0;

    public EntityEyeOfNether(World worldIn) {
        super(worldIn);
        this.setSize(0.5F, 0.5F);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        motionX = 0;
        motionY = 0;
        motionZ = 0;

        List<EntityEnderman> endermen = world.getEntitiesWithinAABB(EntityEnderman.class, getEntityBoundingBox().grow(100));
        for(EntityEnderman entity: endermen){
            if(cooldown-- > 0 || entity.getAttackTarget() == this)
                continue;

            Vec3d pos = getPositionVector();
            Vec3d eye = entity.getPositionVector().addVector(0, entity.getEyeHeight(), 0);

            RayTraceResult hit = world.rayTraceBlocks(pos, eye);
            if(hit != null && hit.typeOfHit != RayTraceResult.Type.MISS && hit.hitVec.squareDistanceTo(pos) < eye.squareDistanceTo(pos))
                continue;

            entity.setAttackTarget(this);
            cooldown = 100;
        }


        if(world.isRemote){
            Vec3d pos = getPositionVector().add(CMath.randomVector(0.2));
            Vec3d vel = CMath.randomVector(0.1).addVector(0, 0.2, 0);
            world.spawnParticle(EnumParticleTypes.FLAME, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z);

            pos = getPositionVector().add(CMath.randomVector(0.3));
            vel = CMath.randomVector(0.1).addVector(0, 0.1, 0);
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z);
        }
    }

    @Override
    public float getCollisionBorderSize()
    {
        return 0.2F;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(!world.isRemote && isEntityAlive()){
            setDead();
            entityDropItem(new ItemStack(ModItems.eyeOfNether), 0);
            world.playSound(null, posX, posY, posZ, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1, 1);
        }
        return true;
    }

    @Override
    public float getBrightness() {
        return 1.0F;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getBrightnessForRender() {
        return 15728880;
    }
}
