package uvmidnight.totaltinkers.tinkers.oldweapons.entities;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.entity.EntityProjectileBase;

public class EntityJavelin extends EntityProjectileBase {
  // animation
  public int roll = 0;
  public int rollSpeed = 80;

  public EntityJavelin(World world) {
    super(world);
  }

  public EntityJavelin(World world, double d, double d1, double d2) {
    super(world, d, d1, d2);
  }

  public EntityJavelin(World world, EntityPlayer player, float speed, float inaccuracy, ItemStack stack, ItemStack launchingStack) {
    super(world, player, speed, inaccuracy, 1f, stack, launchingStack);
  }

  @Override
  protected void playHitEntitySound() {

  }

  @Override
  public void readSpawnData(ByteBuf data) {
    super.readSpawnData(data);

    // animation stuff, it sometimes rotates left
    int rollDir = rand.nextBoolean() ? -1 : 1;
    rollSpeed = (int) ((getSpeed() * 80) / 3) * rollDir;
  }

}
