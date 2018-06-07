package rtk.entity;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import rtk.common.CMath;

import javax.annotation.Nullable;
import java.util.List;

public class EntitySkeletonPriest extends EntityMob {

    private static final Predicate<Entity> NOT_UNDEAD = new Predicate<Entity>()
    {
        public boolean apply(@Nullable Entity p_apply_1_)
        {
            return p_apply_1_ instanceof EntityLivingBase && ((EntityLivingBase)p_apply_1_).getCreatureAttribute() != EnumCreatureAttribute.UNDEAD;
        }
    };

    private final BossInfoServer bossInfo = (BossInfoServer)(new BossInfoServer(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS)).setDarkenSky(true);

    int summonCooldown = 0;
    int individualSummonCoolDown = 0;

    public EntitySkeletonPriest(World worldIn) {
        super(worldIn);
        setSize(0.8F, 2.4F);
        experienceValue = 50;
    }

    protected void initEntityAI()
    {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByPlayerTarget(this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, false));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.225D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(80);
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_WITHER_SKELETON_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_WITHER_SKELETON_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_WITHER_SKELETON_DEATH;
    }

    protected SoundEvent getStepSound()
    {
        return SoundEvents.ENTITY_WITHER_SKELETON_STEP;
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }

    @Override
    public boolean attackEntityAsMob(Entity target) {
        boolean hit = target.attackEntityFrom(DamageSource.causeMobDamage(this), (float)(14 + this.rand.nextInt(30)));

        if (hit) {
            if (target instanceof EntityLivingBase){
                ((EntityLivingBase) target).addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 400));
                ((EntityLivingBase) target).addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 400));
                ((EntityLivingBase) target).addPotionEffect(new PotionEffect(MobEffects.WITHER, 400));
            }


            Vec3d dir = target.getPositionVector().subtract(getPositionVector()).normalize().addVector(0, 0.5, 0);
            target.addVelocity(dir.x, dir.y, dir.z);
        }

        return hit;
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {

        if (!world.isRemote){
            for (int i = 0; i < 4; i++){
                EntityWitherSkeleton mob = new EntityWitherSkeleton(world);
                mob.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
                mob.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);

                BlockPos pos = pickSpawnPoint();
                mob.setLocationAndAngles(0.5 + pos.getX(), pos.getY(), 0.5 + pos.getZ(), CMath.random.nextFloat() * 360,0);

                world.spawnEntity(mob);
            }
        }

        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (world.isRemote)
            return;

        bossInfo.setPercent(getHealth() / getMaxHealth());

        List<EntitySkeleton> mobs = world.getEntitiesWithinAABB(EntitySkeleton.class, getEntityBoundingBox().grow(30, 10, 30));
        for (EntitySkeleton mob: mobs){
            mob.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, Integer.MAX_VALUE));
            mob.addPotionEffect(new PotionEffect(MobEffects.SPEED, Integer.MAX_VALUE));

            addFollowAIIfAbsent(mob);

            mob.setAttackTarget(getAttackTarget());
            mob.setRevengeTarget(getRevengeTarget());
        }

        summonCooldown--;
        individualSummonCoolDown--;
        if (mobs.size() >= 14)
            summonCooldown = 1200;
        if (summonCooldown > 0  || individualSummonCoolDown > 0)
            return;

        try {
            EntitySkeleton mob = new EntitySkeleton(world);

            mob.onInitialSpawn(world.getDifficultyForLocation(getPosition()), null);
            if (CMath.random.nextFloat() < 0.5)
                mob.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));

            BlockPos pos = pickSpawnPoint();
            mob.setLocationAndAngles(0.5 + pos.getX(), pos.getY(), 0.5 + pos.getZ(), CMath.random.nextFloat() * 360,0);

            world.spawnEntity(mob);
            individualSummonCoolDown = 20;
        } catch (NullPointerException e) {
            System.out.println("Warning! Null pointer exception while spawning skeleton. Why is this happening?");
            e.printStackTrace();
        }
    }

    public BlockPos pickSpawnPoint(){
        for (int i = 0; i < 60; i++){
            BlockPos pos = new BlockPos(CMath.randomVector(5).add(getPositionVector()));

            for (int j = 0; j < 10; j++){
                if (world.isSideSolid(pos.down(), EnumFacing.UP) && world.isAirBlock(pos) && world.isAirBlock(pos.up()))
                    return pos;
                pos = pos.up();
            }
        }

        return null;
    }

    public void addFollowAIIfAbsent(EntityLiving entity){
        boolean absent = true;
        for (EntityAITasks.EntityAITaskEntry entry: entity.tasks.taskEntries)
            if (entry.action instanceof EntityAIFollowPriest)
                absent = false;

        if (absent)
            entity.tasks.addTask(4, new EntityAIFollowPriest(entity, this));
    }

    public boolean isNonBoss()
    {
        return false;
    }

    protected void despawnEntity()
    {
    }

//    public EnumCreatureAttribute getCreatureAttribute()
//    {
//        return EnumCreatureAttribute.UNDEAD;
//    }

    public void addTrackingPlayer(EntityPlayerMP player)
    {
        super.addTrackingPlayer(player);
        bossInfo.addPlayer(player);
    }

    public void removeTrackingPlayer(EntityPlayerMP player)
    {
        super.removeTrackingPlayer(player);
        bossInfo.removePlayer(player);
    }

    class EntityAIHurtByPlayerTarget extends EntityAIHurtByTarget {
        public EntityAIHurtByPlayerTarget(EntityCreature creatureIn, boolean entityCallsForHelpIn, Class<?>[] targetClassesIn) {
            super(creatureIn, entityCallsForHelpIn, targetClassesIn);
        }

        @Override
        protected boolean isSuitableTarget(EntityLivingBase target, boolean includeInvincibles) {
            return target instanceof EntityPlayer && super.isSuitableTarget(target, includeInvincibles);
        }
    }

    class EntityAIFollowPriest extends EntityAIBase {
        EntityLiving theEntity;
        EntityLiving priest;

        int maxDist;

        int pathingCooldown = 0;

        public EntityAIFollowPriest(EntityLiving theEntity, EntityLiving priest) {
            setMutexBits(3);
            this.theEntity = theEntity;
            this.priest = priest;

            maxDist = 10;
        }


        @Override
        public void updateTask() {
            if (pathingCooldown-- > 0)
                return;

            pathingCooldown = 10;
            theEntity.getNavigator().tryMoveToEntityLiving(priest, 1);
        }

        @Override
        public boolean shouldExecute() {
            return theEntity.getDistanceSq(priest) > maxDist * maxDist;
        }

        @Override
        public void resetTask() {
            theEntity.getNavigator().clearPath();
        }
    }
}
