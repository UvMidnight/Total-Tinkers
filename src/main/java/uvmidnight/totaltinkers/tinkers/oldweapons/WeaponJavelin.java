package uvmidnight.totaltinkers.tinkers.oldweapons;


import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.entity.EntityProjectileBase;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tools.ProjectileNBT;
import slimeknights.tconstruct.library.tools.ranged.ProjectileCore;
import slimeknights.tconstruct.library.utils.ToolHelper;

import javax.annotation.Nonnull;
import java.util.List;

//I loved the javelin, it was the reason for me creating this mod
//But lets be honest, it was total garbage
//Meele damage is significantly increased over throwing damage, to act as a sort of hybrid weapon.
//Alternative config option gives it similarity with the cutlass
public class WeaponJavelin extends ProjectileCore {
  @Override
  public ProjectileNBT buildTagData(List<Material> materials) {
    return null;
  }

  @Nonnull
  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
    ItemStack itemStackIn = playerIn.getHeldItem(hand);
    if(ToolHelper.isBroken(itemStackIn)) {
      return ActionResult.newResult(EnumActionResult.FAIL, itemStackIn);
    }
    playerIn.getCooldownTracker().setCooldown(itemStackIn.getItem(), 8);

    if(!worldIn.isRemote) {
      boolean usedAmmo = useAmmo(itemStackIn, playerIn);
      EntityProjectileBase projectile = getProjectile(itemStackIn, itemStackIn, worldIn, playerIn, 2.1f, 0f, 1f, usedAmmo);
      worldIn.spawnEntity(projectile);
    }

    return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
  }


  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    // has to be done in onUpdate because onTickUsing is too early and gets overwritten. bleh.
    preventSlowDown(entityIn, 0.195f);

    super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
  }

  @Override
  public float damagePotential() {
    return 0;
  }

  @Override
  public EntityProjectileBase getProjectile(@Nonnull ItemStack stack, @Nonnull ItemStack launcher, World world, EntityPlayer player, float speed, float inaccuracy, float power, boolean usedAmmo) {
    return null;
  }
}
