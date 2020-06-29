package uvmidnight.totaltinkers;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.library.tools.ToolCore;
import uvmidnight.totaltinkers.oldweapons.BattleaxeHandler;
import uvmidnight.totaltinkers.oldweapons.OldWeapons;

//I need to remove the proxy from existence
public class CommonProxy {
    public void init() {
        if (OldWeapons.battleaxe != null) {
            MinecraftForge.EVENT_BUS.register(BattleaxeHandler.INSTANCE);
        }
    }
    public void initToolGuis() {
    }

    public void registerToolModel(ToolCore tc) {
    }

    public <T extends Item & IToolPart> void registerToolPartModel(T part) {
    }

    public void registerSubscriptions() {
    }

    public void renderScreenFullColor(int color, boolean bool) {
    }

    public boolean hasBattleaxeOverlay() {
        return false;
    }

    public void toggleBattleAxeOverlay(int color) {
    }

    public void registerModifierModel(IModifier mod, ResourceLocation rl) {
    }

}
