package uvmidnight.totaltinkers.oldweapons.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.entity.EntityProjectileBase;

import javax.annotation.Nonnull;

public class EntityJavelin extends EntityProjectileBase {

    // animation
    public int roll = 0;
    public int rollSpeed = 80;
    public int rollAngle = 0;

    public EntityJavelin(World world) {
        super(world);
    }

    public EntityJavelin(World world, double d, double d1, double d2) {
        super(world, d, d1, d2);
    }

    @Nonnull
    @Override
    protected ItemStack getArrowStack() {
        return tinkerProjectile.getItemStack();
    }

    public EntityJavelin(World world, EntityPlayer player, float speed, float inaccuracy, ItemStack stack, ItemStack launchingStack) {
        super(world, player, speed, inaccuracy, 1f, stack, launchingStack);
    }

    @Override
    public double getStuckDepth() {
        return 0.5f;
    }

    @Override
    protected void playHitEntitySound() {//nothing here
    }

    //  @Override
//  public void onUpdate() {
//    // you turn me right round baby
//    if(!this.inGround)
//      roll = (roll + 13) % 360;
//
//    super.onUpdate();
//  }
    @Override
    public void readSpawnData(ByteBuf data) {
        super.readSpawnData(data);

        // animation stuff, it sometimes rotates left
//    int rollDir = rand.nextBoolean() ? -1 : 1;
//    rollSpeed = (int) ((getSpeed() * 80) / 3) * rollDir;
    }

    @Override
    public double getGravity() {
        return 0.07;
    }

}
