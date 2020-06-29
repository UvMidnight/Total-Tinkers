package uvmidnight.totaltinkers.oldweapons;

import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BattleaxeHandler {
    public static final BattleaxeHandler INSTANCE = new BattleaxeHandler();
    @SubscribeEvent
    public void bonusAxeToSelf(LivingHurtEvent event) {
        if (event.getEntityLiving().getHeldItemMainhand().getItem().equals(OldWeapons.battleaxe)) {
            event.setAmount(event.getAmount() * (float) OldWeapons.battleaxeIncreasedDamage.getDouble());
        }
    }

}
