package uvmidnight.totaltinkers.oldweapons.potions;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import uvmidnight.totaltinkers.TotalTinkers;
import uvmidnight.totaltinkers.oldweapons.OldWeapons;
import uvmidnight.totaltinkers.oldweapons.WeaponBattleAxe;

//This implementation is total garbage.

public class PotionBerserkerEffect extends PotionEffect {

    public PotionBerserkerEffect(Potion potion, int effectDuration) {
        super(potion, effectDuration, 0, false, false);
    }

    @Override
    public boolean onUpdate(EntityLivingBase entityIn) {
        if (entityIn.getHeldItemMainhand().getItem() instanceof WeaponBattleAxe) {
            entityIn.addPotionEffect(new PotionEffect(Potion.getPotionById(1), 30, OldWeapons.berserkerSpeed.getInt(), false, false));
            entityIn.addPotionEffect(new PotionEffect(Potion.getPotionById(11), 30, OldWeapons.berserkerResistance.getInt(), false, false));
            entityIn.addPotionEffect(new PotionEffect(Potion.getPotionById(8), 30, OldWeapons.berserkerJumpBoost.getInt(), false, false));
            entityIn.addPotionEffect(new PotionEffect(Potion.getPotionById(5), 30, OldWeapons.berserkerStrength.getInt(), false, false));
            entityIn.addPotionEffect(new PotionEffect(Potion.getPotionById(3), 30, OldWeapons.berserkerHaste.getInt(), false, false));
        } else {
            entityIn.removePotionEffect(OldWeapons.potionBerserker);
            TotalTinkers.proxy.renderScreenFullColor(0xffff0000, false);
        }
        return super.onUpdate(entityIn);
    }
}
