package uvmidnight.totaltinkers.tinkers.oldweapons;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.SwordCore;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.traits.ITrait;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import slimeknights.tconstruct.tools.TinkerTools;
import uvmidnight.totaltinkers.tinkers.TotalTinkersItems;

import java.util.List;

//New cutlass behavior, since blocking was removed.
// Crits with the cutlass will give a fleeting burst of speed, 1 second and speed 2 by default.

//not sure about the behavior right now, if you have any ideas how to make it less.. op like the old cutlass, but hit me up on discord or wherever else you can find me
public class WeaponCutlass extends SwordCore {
  public static final float DURABILITY_MODIFIER = 1.2f;


  public WeaponCutlass() {
    super(PartMaterialType.handle(TinkerTools.toolRod),
            PartMaterialType.head(TinkerTools.swordBlade),
            PartMaterialType.extra(TotalTinkersItems.fullGuard));

    addCategory(Category.WEAPON);

    setTranslationKey("cutlass").setRegistryName("cutlass");
  }

  @Override
  public float damagePotential() {
    return 0.7F;
  }

  @Override
  public double attackSpeed() {
    return 1.6F;
  }

  @Override
  public float damageCutoff() {
    return 20f;
  }// no ida why this is a fucking thing


  //TLDR: gives 1 second of speed on crit.
  // This probably breaks horribly a potion or something that forces crits or alters the crit system
  @SuppressWarnings("unchecked")
  @Override
  public boolean dealDamage(ItemStack stack, EntityLivingBase player, Entity entity, float damage) {
    if (entity instanceof EntityLivingBase) {
      List<ITrait> traits = TinkerUtil.getTraitsOrdered(stack);
      boolean isCritical = player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater() && !player.isPotionActive(MobEffects.BLINDNESS) && !player.isRiding();
      for (ITrait trait : traits) {
        if (trait.isCriticalHit(stack, player, (EntityLivingBase) entity)) {
          isCritical = true;
        }
      }
      if (isCritical) {
        player.addPotionEffect(new PotionEffect(Potion.getPotionById(1), 30, 2));
      }
    }
    return super.dealDamage(stack, player, entity, damage);
  }

  @Override
  public ToolNBT buildTagData(List<Material> materials) {
    HandleMaterialStats handle = materials.get(0).getStatsOrUnknown(MaterialTypes.HANDLE);
    HeadMaterialStats head1 = materials.get(1).getStatsOrUnknown(MaterialTypes.HEAD);
    ExtraMaterialStats binding = materials.get(2).getStatsOrUnknown(MaterialTypes.EXTRA);

    ToolNBT data = new ToolNBT();
    data.head(head1);
    data.extra(binding);
    data.handle(handle);

    data.attack += 1f;
    data.durability *= DURABILITY_MODIFIER;
    return data;
  }
}
