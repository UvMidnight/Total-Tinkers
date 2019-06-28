package uvmidnight.totaltinkers.oldweapons.potions;

import net.minecraft.potion.Potion;

public class PotionBerserker extends Potion {
    public PotionBerserker(boolean isBadEffectIn, int liquidColorIn) {
        super(isBadEffectIn, liquidColorIn);
        setPotionName("effect.berserker");
        this.setRegistryName("totaltinkers", "berserker");
    }

    //@SideOnly(Side.CLIENT)
    //  public boolean hasStatusIcon() {
    //    return this.statusIconIndex >= 0;
    //  }

}
