package rtk.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityRtkArrow extends EntityTippedArrow {

    public boolean explodeOnHit;

    public EntityRtkArrow(World worldIn) {
        super(worldIn);
    }

    public EntityRtkArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    protected void onHit(RayTraceResult trace) {
        if(explodeOnHit)
            explode();

        if(trace.entityHit != null)
            trace.entityHit.hurtResistantTime = 0;

        super.onHit(trace);
    }

    public void explode(){
        setDead();
        if(!worldObj.isRemote)
            this.worldObj.createExplosion(this, this.posX, this.posY + (double)(this.height / 16.0F), this.posZ, 4.0F, true);
    }
}
