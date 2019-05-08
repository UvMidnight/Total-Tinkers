package uvmidnight.totaltinkers.tinkers.newweapons;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.entity.EntityProjectileBase;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tools.IAmmoUser;
import slimeknights.tconstruct.library.tools.ProjectileNBT;
import slimeknights.tconstruct.library.tools.SwordCore;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.ToolHelper;
import uvmidnight.totaltinkers.tinkers.entities.EntityBullet;

import javax.annotation.Nonnull;
import java.util.List;

//inbred cousin to javelin. requires ammo to be shot.
public class WeaponGunblade extends SwordCore implements IAmmoUser {
  @Override
  protected ToolNBT buildTagData(List<Material> materials) {
    return null;
  }

  @Override
  public float damagePotential() {
    return 0;
  }

  @Override
  public double attackSpeed() {
    return 0;
  }

  @Nonnull
  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
    ItemStack itemStackIn = playerIn.getHeldItem(hand);
    if (ToolHelper.isBroken(itemStackIn)) {
      return ActionResult.newResult(EnumActionResult.FAIL, itemStackIn);
    }
    playerIn.getCooldownTracker().setCooldown(itemStackIn.getItem(), 16);

    if (!worldIn.isRemote) {
      boolean usedAmmo = !findAmmo(itemStackIn, playerIn).isEmpty();
      EntityProjectileBase projectile = getProjectile(itemStackIn, itemStackIn, worldIn, playerIn, 2.1f, 0f, 1f, usedAmmo);
      worldIn.spawnEntity(projectile);
    }

    return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
  }

  public EntityProjectileBase getProjectile(@Nonnull ItemStack stack, @Nonnull ItemStack launcher, World world, EntityPlayer player, float speed, float inaccuracy, float power, boolean usedAmmo) {
    inaccuracy *= ProjectileNBT.from(stack).accuracy;
    return new EntityBullet(world, player, speed, inaccuracy, getProjectileStack(stack, world, player, usedAmmo), launcher);
  }

  @Nonnull
  @Override
  public ItemStack findAmmo(@Nonnull ItemStack weapon, EntityLivingBase player) {
    return null;
  }

  @Nonnull
  @Override
  public ItemStack getAmmoToRender(@Nonnull ItemStack weapon, EntityLivingBase player) {
    return null;
  }

  protected ItemStack getProjectileStack(ItemStack itemStack, World world, EntityPlayer player, boolean usedAmmo) {
    ItemStack reference = itemStack.copy();
    reference.setCount(1);
    setAmmo(1, reference);

    // prevent a positive feedback loop with picking up ammo + durability retaining modifiers like reinforced
    if (!player.capabilities.isCreativeMode && !world.isRemote && !usedAmmo) {
      setAmmo(0, reference);
    }

    // never broken
    ToolHelper.unbreakTool(reference);
    return reference;
  }

  public void setAmmo(int count, ItemStack stack) {
    // we are setting ammo remaining, but damage of 0 is full ammo
    stack.setItemDamage((getMaxAmmo(stack) - count) * 10);
  }

  public int getMaxAmmo(ItemStack stack) {
    return ToolHelper.getMaxDurability(stack) / 10;
  }
}
