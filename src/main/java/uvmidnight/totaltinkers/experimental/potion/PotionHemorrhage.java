package uvmidnight.totaltinkers.experimental.potion;

import net.minecraft.potion.Potion;

public class PotionHemorrhage extends Potion {
    public PotionHemorrhage(boolean isBadEffectIn, int liquidColorIn) {
        super(isBadEffectIn, liquidColorIn);
        setPotionName("effect.hemorrhage");
        this.setRegistryName("totaltinkers", "hemorrhage");
    }
//    public double getAttributeModifierAmount(int amplifier, AttributeModifier modifier) {
//        return this.bonusPerLevel * (double)(amplifier + 1);
//    }
}
