package uvmidnight.totaltinkers.explosives.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.entity.EntityProjectileBase;
import slimeknights.tconstruct.tools.common.entity.EntityArrow;
import uvmidnight.totaltinkers.explosives.ExplosionTinkersBase;

import javax.annotation.Nonnull;

public class EntityExplosiveArrow extends EntityArrow {
    // animation
    public int roll = 0;
    public int rollSpeed = 50;

    private float radius;
    public EntityExplosiveArrow(World world) {
        super(world);
    }
    public EntityExplosiveArrow(World world, double d, double d1, double d2) {
        super(world, d, d1, d2);
    }

    public EntityExplosiveArrow(World world, EntityPlayer player, float speed, float inaccuracy, float power, ItemStack stack, ItemStack launchingStack, float radius) {
        super(world, player, speed, inaccuracy, power, stack, launchingStack);
        this.radius = radius;
    }

    public void onHitBlock(RayTraceResult raytraceResult) {
        if (world.getBlockState(raytraceResult.getBlockPos()).getCollisionBoundingBox(world, raytraceResult.getBlockPos()) != null){
            BlockPos blockpos = raytraceResult.getBlockPos();
            ExplosionTinkersBase explosion = new ExplosionTinkersBase(this.world, this, this.posX, this.posY, this.posZ, radius, tinkerProjectile, (EntityLivingBase) this.shootingEntity);
            explosion.doExplosionA();
            explosion.doExplosionB(true);
            this.setDead();
        }
    }

    //does no bonus damage on hitting a target directly.
    public void onHitEntity(RayTraceResult raytraceResult) {
//        super.onHitEntity(raytraceResult);
        if (raytraceResult.entityHit != null && raytraceResult.entityHit != this.shootingEntity) {
            //snaps onto the enemy that is hit
            ExplosionTinkersBase explosion = new ExplosionTinkersBase(this.world, this, raytraceResult.entityHit.posX, this.posY, raytraceResult.entityHit.posZ, radius, tinkerProjectile, (EntityLivingBase) this.shootingEntity);
            explosion.doExplosionA();
            explosion.doExplosionB(true);
            this.setDead();
        }
    }

    @Nonnull
    @Override
    protected ItemStack getArrowStack() {
        return tinkerProjectile.getItemStack();
    }

    @Override
    public double getStuckDepth() {
        return 0.0f;
    }

    @Override
    public void readSpawnData(ByteBuf data) {
        super.readSpawnData(data);
//        int rollDir = rand.nextBoolean() ? -1 : 1;
//        rollSpeed = (int)((getSpeed() * 80) / 3) * rollDir;
    }

    @Override
    public double getGravity() {
        return 0.16;
    }

}
