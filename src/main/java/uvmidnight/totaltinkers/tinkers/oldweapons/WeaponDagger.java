package uvmidnight.totaltinkers.tinkers.oldweapons;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.entity.EntityProjectileBase;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.ProjectileNBT;
import slimeknights.tconstruct.library.tools.SwordCore;
import slimeknights.tconstruct.library.tools.TinkerToolCore;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.tools.TinkerTools;
import uvmidnight.totaltinkers.tinkers.oldweapons.entities.EntityDagger;

import java.util.List;

public class WeaponDagger extends SwordCore {
  private static PartMaterialType daggerpmt = new PartMaterialType(TinkerTools.knifeBlade, MaterialTypes.HEAD, MaterialTypes.PROJECTILE);

  public WeaponDagger() {
    super(PartMaterialType.handle(TinkerTools.toolRod), daggerpmt, PartMaterialType.extra(TinkerTools.crossGuard));

    addCategory(Category.WEAPON);

    setTranslationKey("dagger").setRegistryName("dagger");
  }
//  @Override
//  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
//  {
//    ItemStack stack = playerIn.getHeldItem(hand);
//
//
////    return ActionResult.newResult(EnumActionResult.FAIL, itemStackIn);
//  }


  @Override
  public float damagePotential() {
    return 1F;
  }

  @Override
  public double attackSpeed() {
    return 2F;
  }

  @Override
  public ProjectileNBT buildTagData(List<Material> materials) {
    HandleMaterialStats handle = materials.get(0).getStatsOrUnknown(MaterialTypes.HANDLE);
    HeadMaterialStats head = materials.get(1).getStatsOrUnknown(MaterialTypes.HEAD);
    ExtraMaterialStats extra = materials.get(2).getStatsOrUnknown(MaterialTypes.EXTRA);
    ProjectileNBT data = new ProjectileNBT();
    data.head(head);
    data.handle(handle);
    data.extra(extra);

    return data;
  }
}
