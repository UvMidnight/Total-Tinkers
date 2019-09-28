package uvmidnight.totaltinkers.experimental.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.entity.EntityProjectileBase;

import javax.annotation.Nonnull;

//dummy thick
public class EntityBomb extends EntityProjectileBase {
    private double damage = 0;

    public EntityBomb(World world) {
        super(world);
    }

    public EntityBomb(World world, double d, double d1, double d2) {
        super(world, d, d1, d2);
    }

    public EntityBomb(World world, EntityPlayer player, float speed, float inaccuracy, ItemStack stack, ItemStack launchingStack) {
        super(world, player, speed, inaccuracy, 1f, stack, launchingStack);
    }

    public EntityBomb(World world, EntityPlayer player, float speed, float inaccuracy, ItemStack stack, ItemStack launchingStack, double damage) {
        super(world, player, speed, inaccuracy, 1f, stack, launchingStack);
        this.damage = damage;
    }

    public void onHitBlock(RayTraceResult raytraceResult) {
        BlockPos blockpos = raytraceResult.getBlockPos();
//        this.xTile = blockpos.getX();
//        this.zTile = blockpos.getZ();
//        this.yTile = blockpos.getY();
//        defuse(); // defuse it so it doesn't hit stuff anymore, being weird
        this.world.createExplosion(this, this.posX, this.posY + (double) (this.height / 16.0F), this.posZ, (float) damage, true);
        this.setDead();
    }

    public void onHitEntity(RayTraceResult raytraceResult) {
        this.world.createExplosion(this, this.posX, this.posY + (double) (this.height / 16.0F), this.posZ, (float) damage, true);
        super.onHitEntity(raytraceResult);
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

        // animation stuff, it sometimes rotates left
//    int rollDir = rand.nextBoolean() ? -1 : 1;
//    rollSpeed = (int) ((getSpeed() * 80) / 3) * rollDir;
    }

    @Override
    public double getGravity() {
        return 0.16;
    }
}
