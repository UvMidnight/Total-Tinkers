package uvmidnight.totaltinkers.tinkers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.library.tools.Pattern;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.tools.ToolPart;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.ranged.TinkerRangedWeapons;
import uvmidnight.totaltinkers.TotalTinkers;
import uvmidnight.totaltinkers.potions.PotionBerserker;
import uvmidnight.totaltinkers.tinkers.entities.EntityJavelin;
import uvmidnight.totaltinkers.tinkers.newweapons.WeaponGreatblade;
import uvmidnight.totaltinkers.tinkers.oldweapons.*;
import uvmidnight.totaltinkers.util.Config;

@Mod.EventBusSubscriber
public class TotalTinkersRegister {
//  private static final List<ToolCore> tools = new ArrayList<>();
//  private static final List<IToolPart> toolParts = new ArrayList<>();

  public static ToolPart fullGuard;
  public static ToolPart greatbladeCore;

  public static WeaponGreatblade greatblade;
  public static WeaponBattleAxe battleaxe;
  public static WeaponCutlass cutlass;
  public static PotionBerserker potionBerserker;
  public static WeaponJavelin javelin;
  public static WeaponDagger dagger;

  @SubscribeEvent
  public static void initItems(RegistryEvent.Register<Item> event) {

    if (Config.greatbladeCoreEnabled || Config.greatbladeEnabled) {
      greatbladeCore = new ToolPart(Material.VALUE_Ingot * 12);
      greatbladeCore.setTranslationKey("greatbladeCore").setRegistryName("greatbladeCore");
      event.getRegistry().register(greatbladeCore);
      TinkerRegistry.registerToolPart(greatbladeCore);
      TotalTinkers.proxy.registerToolPartModel(greatbladeCore);
      if (!Config.greatbladeCoreFromEndShip) {
        TinkerRegistry.registerStencilTableCrafting(Pattern.setTagForPart(new ItemStack(TinkerTools.pattern), greatbladeCore));
      }
    }

    if (Config.fullGuardEnabled || Config.cutlassEnabled) {
      fullGuard = new ToolPart(Material.VALUE_Ingot * 3);
      fullGuard.setTranslationKey("fullGuard").setRegistryName("fullGuard");
      event.getRegistry().register(fullGuard);
      TinkerRegistry.registerToolPart(fullGuard);
      TotalTinkers.proxy.registerToolPartModel(fullGuard);
      if (!Config.fullGuardFromVillages) {
        TinkerRegistry.registerStencilTableCrafting(Pattern.setTagForPart(new ItemStack(TinkerTools.pattern), fullGuard));
      }
    }

    if (Config.greatbladeEnabled) {
      greatblade = new WeaponGreatblade();
      initForgeTool(greatblade, event);
    }

    if (Config.battleaxeEnabled) {
      battleaxe = new WeaponBattleAxe();
      initForgeTool(battleaxe, event);
    }
    if (Config.cutlassEnabled) {
      cutlass = new WeaponCutlass();
      initForgeTool(cutlass, event);
    }
    if (Config.javelinEnabled) {
      javelin = new WeaponJavelin();
      event.getRegistry().register(javelin);
      TinkerRegistry.registerToolStationCrafting(javelin);
      TinkerRegistry.registerToolForgeCrafting(javelin);
      TotalTinkers.proxy.registerToolModel(javelin);
    }
    if (Config.daggerEnabled) {
      dagger = new WeaponDagger();
      event.getRegistry().register(dagger);
      TinkerRegistry.registerToolForgeCrafting(dagger);
      TinkerRegistry.registerToolStationCrafting(dagger);
      TotalTinkers.proxy.registerToolModel(dagger);
    }

    //here we go...
    if (Config.isReplacingCrossbow) {
      TinkerRangedWeapons.crossBow = new WeaponCrossbowOveride();
      initForgeTool(TinkerRangedWeapons.crossBow, event);
    }

    for (IModifier modifier : new IModifier[]{
            TinkerModifiers.modBaneOfArthopods,
            TinkerModifiers.modBeheading,
            TinkerModifiers.modDiamond,
            TinkerModifiers.modEmerald,
            TinkerModifiers.modGlowing,
            TinkerModifiers.modHaste,
            TinkerModifiers.modKnockback,
            TinkerModifiers.modLuck,
            TinkerModifiers.modMendingMoss,
            TinkerModifiers.modNecrotic,
            TinkerModifiers.modReinforced,
            TinkerModifiers.modSharpness,
            TinkerModifiers.modShulking,
            TinkerModifiers.modSilktouch,
            TinkerModifiers.modSmite,
            TinkerModifiers.modSoulbound,
            TinkerModifiers.modWebbed,
    }) {
      TotalTinkers.proxy.registerModifierModel(modifier,
              new ResourceLocation(TotalTinkers.MODID, "models/item/modifiers/" + modifier.getIdentifier()));
    }
  }

  @SubscribeEvent
  public static void onRegisterPotions(RegistryEvent.Register<Potion> event) {
    potionBerserker = new PotionBerserker(false, 0xff0000);
    event.getRegistry().register(potionBerserker);
  }

  @SubscribeEvent
  public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
    EntityRegistry.registerModEntity(new ResourceLocation(TotalTinkers.MODID, "javelin"), EntityJavelin.class, "javelin", 1, TotalTinkers.instance, 64, 1, false);
  }

  @SubscribeEvent
  public static void registerModels(ModelRegistryEvent event) {
    RenderingRegistry.registerEntityRenderingHandler(EntityJavelin.class, RenderJavelin::new);
  }

  private static void initForgeTool(ToolCore core, RegistryEvent.Register<Item> event) {
    event.getRegistry().register(core);
    TinkerRegistry.registerToolForgeCrafting(core);
    TotalTinkers.proxy.registerToolModel(core);
  }
}