package uvmidnight.totaltinkers.newweapons.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import uvmidnight.totaltinkers.newweapons.NewWeapons;

public class PotionHemorrhageEffect extends PotionEffect {
    public static DamageSource bleedSource = new DamageSource("totaltinkers.hemorrhage");
    public PotionHemorrhageEffect(PotionHemorrhage potion, int effectDuration, int amplifier) {
        super(potion, effectDuration, amplifier, false, true);
    }

    //Every level of hemorrhage on the target deals 2 damage every second.
    @Override
    public boolean onUpdate(EntityLivingBase entityIn) {
        int bleh = getDuration() % 10;
        if (bleh == 0) {
            entityIn.attackEntityFrom(bleedSource, (float) (NewWeapons.scimitarDamagePerStack.getDouble() * (getAmplifier() - 1) + NewWeapons.scimitarDamageBase.getDouble()));
        }
        return super.onUpdate(entityIn);
    }
}