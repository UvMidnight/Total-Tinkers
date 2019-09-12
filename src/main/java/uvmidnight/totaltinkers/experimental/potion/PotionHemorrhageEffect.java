package uvmidnight.totaltinkers.experimental.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

public class PotionHemorrhageEffect extends PotionEffect {
    public PotionHemorrhageEffect(PotionHemorrhage potion, int effectDuration, int amplifier) {
        super(potion, effectDuration, amplifier, false, true);
    }

    //Every level of hemorrhage on the target deals 1 damage every second.
    @Override
    public boolean onUpdate(EntityLivingBase entityIn) {
        entityIn.attackEntityFrom(DamageSource.MAGIC, 2.0F * getAmplifier());

        return super.onUpdate(entityIn);
    }
}