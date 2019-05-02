package uvmidnight.totaltinkers.potions;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import uvmidnight.totaltinkers.TotalTinkers;
import uvmidnight.totaltinkers.tinkers.TinkerItems;
import uvmidnight.totaltinkers.tinkers.oldweapons.WeaponBattleAxe;
import uvmidnight.totaltinkers.util.Config;

public class PotionBerserkerEffect extends PotionEffect {
  public PotionBerserkerEffect(Potion potion, int effectDuration) {
    super(potion, effectDuration, 0, false, false);
  }

  @Override
  public boolean onUpdate(EntityLivingBase entityIn) {
    if (entityIn.getHeldItemMainhand().getItem() instanceof WeaponBattleAxe) {
      entityIn.addPotionEffect(new PotionEffect(Potion.getPotionById(1), 30, Config.berserkerSpeed, false, false));
      entityIn.addPotionEffect(new PotionEffect(Potion.getPotionById(11), 30, Config.berserkerResistance, false, false));
      entityIn.addPotionEffect(new PotionEffect(Potion.getPotionById(8), 30, 1, false, false));
      entityIn.addPotionEffect(new PotionEffect(Potion.getPotionById(5), 30, 1, false, false));
      entityIn.addPotionEffect(new PotionEffect(Potion.getPotionById(3), 30, Config.berserkerHaste, false, false));
    } else {
      entityIn.removePotionEffect(TinkerItems.potionBerserker);
      TotalTinkers.proxy.renderScreenFullColor(0xffff0000, false);
    }
    return super.onUpdate(entityIn);
  }
}