package uvmidnight.totaltinkers.tinkers.newweapons;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.TinkersItem;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.tools.ToolPart;
import uvmidnight.totaltinkers.TotalTinkers;
import uvmidnight.totaltinkers.tinkers.oldweapons.WeaponBattleAxe;
import uvmidnight.totaltinkers.tinkers.oldweapons.WeaponCutlass;
import uvmidnight.totaltinkers.util.Config;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class NewWeapons{
  public static WeaponGreatblade greatblade;

  public static ToolPart greatbladeCore;

  public static WeaponBattleAxe battleaxe;
  public static ToolPart fullGuard;
  public static WeaponCutlass cutlass;

  private static final List<ToolCore> tools = new ArrayList<>();
  private static final List<IToolPart> toolParts = new ArrayList<>();

  @SubscribeEvent
  public static void initItems(RegistryEvent.Register<Item> event) {
    greatbladeCore = new ToolPart(Material.VALUE_Ingot * 12);
    greatbladeCore.setTranslationKey("greatbladeCore").setRegistryName("greatbladeCore");
    event.getRegistry().register(greatbladeCore);
    TinkerRegistry.registerToolPart(greatbladeCore);
    TotalTinkers.proxy.registerToolPartModel(greatbladeCore);

    fullGuard = new ToolPart(Material.VALUE_Ingot * 5);
    fullGuard.setTranslationKey("fullGuard").setRegistryName("fullGuard");
    event.getRegistry().register(fullGuard);
    TinkerRegistry.registerToolPart(fullGuard);
    TotalTinkers.proxy.registerToolPartModel(fullGuard);

    if (Config.greatbladeEnabled) {
      greatblade = new WeaponGreatblade();
      event.getRegistry().register(greatblade);
      TinkerRegistry.registerToolForgeCrafting(greatblade);
      TotalTinkers.proxy.registerToolModel(greatblade);

//      tools.add(greatblade);
    }

    battleaxe = new WeaponBattleAxe();
    event.getRegistry().register(battleaxe);
    TinkerRegistry.registerToolForgeCrafting(battleaxe);
    TotalTinkers.proxy.registerToolModel(battleaxe);

    cutlass = new WeaponCutlass();
    event.getRegistry().register(cutlass);
    TinkerRegistry.registerToolForgeCrafting(cutlass);
    TotalTinkers.proxy.registerToolModel(cutlass);
  }
}
