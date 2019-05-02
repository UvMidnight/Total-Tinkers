package uvmidnight.totaltinkers.proxy;

import net.minecraft.item.Item;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.library.tools.ToolCore;

public class CommonProxy {

  public void initToolGuis() {
  }

  public void registerToolModel(ToolCore tc) {
  }

  public <T extends Item & IToolPart> void registerToolPartModel(T part) {
  }
  public void registerSubscriptions() {}
  public void renderScreenFullColor(int color, boolean bool) {}

  public boolean hasBattleaxeOverlay() {
    return false;
  }
  public void toggleBattleAxeOverlay(int color) {
  }
}
