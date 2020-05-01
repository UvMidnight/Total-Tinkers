package uvmidnight.totaltinkers.explosives.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import slimeknights.tconstruct.library.entity.EntityProjectileBase;
import uvmidnight.totaltinkers.explosives.ExplosionTinkersBase;

import javax.annotation.Nonnull;

//dummy thick
public class EntityBomb extends EntityProjectileBase {

    public EntityBomb(World world) {
        super(world);
    }

    public EntityBomb(World world, double d, double d1, double d2) {
        super(world, d, d1, d2);
    }

    public EntityBomb(World world, EntityPlayer player, float speed, float inaccuracy, ItemStack stack, ItemStack launchingStack) {
        super(world, player, speed, inaccuracy, 1f, stack, launchingStack);
    }

    public void onHitBlock(RayTraceResult raytraceResult) {
        BlockPos blockpos = raytraceResult.getBlockPos();
        ExplosionTinkersBase explosion = new ExplosionTinkersBase(this.world, this, this.posX, this.posY + (double) (this.height / 16.0F), this.posZ, 1.5F, tinkerProjectile, (EntityLivingBase) this.shootingEntity);
        explosion.doExplosionA();
        explosion.doExplosionB(true);
        this.setDead();
    }

    //does no bonus damage on hitting a target directly.
    public void onHitEntity(RayTraceResult raytraceResult) {
        ExplosionTinkersBase explosion = new ExplosionTinkersBase(this.world, this, this.posX, this.posY + (double) (this.height / 16.0F), this.posZ, 1.5F, tinkerProjectile, (EntityLivingBase) this.shootingEntity);
        explosion.doExplosionA();
        explosion.doExplosionB(true);
        this.setDead();
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
    }

    @Override
    public double getGravity() {
        return 0.16;
    }
}
