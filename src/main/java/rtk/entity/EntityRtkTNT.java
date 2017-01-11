package rtk.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import rtk.common.Common;

public class EntityRtkTNT extends EntityTNTPrimed {

    public boolean explodeOnImpact;

    public EntityRtkTNT(World worldIn) {
        super(worldIn);
    }

    public EntityRtkTNT(World worldIn, double x, double y, double z, EntityLivingBase igniter) {
        super(worldIn, x, y, z, igniter);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(explodeOnImpact && isCollided)
            explode();
    }

    public void explode(){
        setDead();
        if(!worldObj.isRemote)
            this.worldObj.createExplosion(this, this.posX, this.posY + (double)(this.height / 16.0F), this.posZ, 4.0F, true);
    }


}
