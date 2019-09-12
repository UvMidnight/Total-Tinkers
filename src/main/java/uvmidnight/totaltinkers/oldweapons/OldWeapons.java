package uvmidnight.totaltinkers.oldweapons;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.RegistryEvent;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tools.Pattern;
import slimeknights.tconstruct.library.tools.ToolPart;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.ranged.TinkerRangedWeapons;
import uvmidnight.totaltinkers.IModule;
import uvmidnight.totaltinkers.TotalTinkers;
import uvmidnight.totaltinkers.TotalTinkersRegister;
import uvmidnight.totaltinkers.oldweapons.potion.PotionBerserker;

public class OldWeapons implements IModule {
    final static String CategoryName = "Old Tools";

    public static Property battleaxeEnabled;
    public static Property cutlassEnabled;
    public static Property javelinEnabled;
    public static Property daggerEnabled;
    public static Property isReplacingCrossbow;

    public static Property cutlassSpeedDuration;
    public static Property cutlassSpeedStrength;

    public static Property disable_screen_overlay;

    public static Property berserkerSpeed;
    public static Property berserkerHaste;
    public static Property berserkerResistance;
    public static Property berserkerStrength;
    public static Property berserkerJumpBoost;

    public static Property crossbowOldCrosshair;
    public static Property autoCrossbowReload;
    public static Property autoCrossbowSlowdown;
    public static Property autoCrossbowDualWield;

    public static Property fullGuardEnabled;
    public static Property fullGuardFromVillages;
    public static ToolPart fullGuard;

    public static WeaponBattleAxe battleaxe;
    public static WeaponCutlass cutlass;
    public static PotionBerserker potionBerserker;
    public static WeaponJavelin javelin;
    public static WeaponDagger dagger;

    @Override
    public void buildConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CategoryName, "Configuration for the old 1.7 weapons, reborn anew");

        battleaxeEnabled = cfg.get(CategoryName, "Battle Axe Enabled?", true, "If the swirling whirlwind of death will slay");
        cutlassEnabled = cfg.get(CategoryName, "Cutlass Enabled?", true, "Here be the sword of the seas.");
        javelinEnabled = cfg.get(CategoryName, "Javelin Enabled", true, "If the warframe's worst throwing weapon is enabled");
        daggerEnabled = cfg.get(CategoryName, "Dagger Enabled?", true, "Should the rouge's weapon, the dagger, be enabled");
        isReplacingCrossbow = cfg.get(CategoryName, "Replace Tinker's Crossbow", false, "Should the crossbow be replaced by a custom version. This is REQUIRED for any of the crossbow tweaks");

        cutlassSpeedDuration = cfg.get(CategoryName, "Cutlass Speed Effect Duration", 30, "How many ticks of speed to give", 0, Integer.MAX_VALUE);
        cutlassSpeedStrength = cfg.get(CategoryName, "Cutlass Speed Effect Strength", 2, "What speed amplitude to give. 3 is default", -1, Short.MAX_VALUE);

        fullGuardFromVillages = cfg.get(CategoryName, "fullGuardFromVillages", true, "Should the full guard pattern come from villages. Disable to make it craftable in the stencil table.");

        disable_screen_overlay = cfg.get(CategoryName, "Battle Axe Overlay Disabled", false, "If the red overlay for battleaxe's berserker is disabled");
        berserkerSpeed = cfg.get(CategoryName, "Berserker Effect Speed", 1, "Level of speed berserker gives. 1 is speed 2.", Short.MIN_VALUE, Short.MAX_VALUE);
        berserkerHaste = cfg.get(CategoryName, "Berserker Effect Haste", 2, "Level of haste berserker gives. 2 is haste 3.", Short.MIN_VALUE, Short.MAX_VALUE);
        berserkerResistance = cfg.get(CategoryName, "Berserker Effect Resistance", -5, "Level of resistance berserker gives. -4 is +80% damage dealt, 20% per level of resistance");
        berserkerStrength = cfg.get(CategoryName, "Berserker Effect Strength", 0, "Level of Strength berserker gives. 1 is Strength 2, or 3 hearts extra of damage per hit.");
        berserkerJumpBoost = cfg.get(CategoryName, "Berserker Effect Jump Boost", 1, "Level of jump boost berserker gives. 1 is jump boost 2.", Short.MIN_VALUE, Short.MAX_VALUE);

        crossbowOldCrosshair = cfg.get(CategoryName, "Crossbow Crosshair", true, "If the old crossbow cursor should be used");
        autoCrossbowReload = cfg.get(CategoryName, "Crossbow Automatic Reload After Shooting", false, "If enabled, the crossbow will automatically reload after being shot.");
        autoCrossbowDualWield = cfg.get(CategoryName, "Crossbow Apply automatic behavior to dual wield", true, "If enabled, the crossbow will still automatically reload while in offhand");
//    autoCrossbowSlowdown = cfg.getBoolean("autoCrossbowSlowdown", CATEGORY_CROSSBOW, true, "If the automatic reload function is enabled, the crossbow will slow the player while reloading");
    }

    @Override
    public void initItems(RegistryEvent.Register<Item> event) {
        if (OldWeapons.cutlassEnabled.getBoolean()) {
            fullGuard = new ToolPart(Material.VALUE_Ingot * 3);
            fullGuard.setTranslationKey("fullGuard").setRegistryName("fullGuard");
            event.getRegistry().register(fullGuard);
            TinkerRegistry.registerToolPart(fullGuard);
            TotalTinkers.proxy.registerToolPartModel(fullGuard);
            if (!OldWeapons.fullGuardFromVillages.getBoolean()) {
                TinkerRegistry.registerStencilTableCrafting(Pattern.setTagForPart(new ItemStack(TinkerTools.pattern), fullGuard));
            }
        }
        if (OldWeapons.battleaxeEnabled.getBoolean()) {
            OldWeapons.battleaxe = new WeaponBattleAxe();
            TotalTinkersRegister.initForgeTool(OldWeapons.battleaxe, event);
        }
        if (OldWeapons.cutlassEnabled.getBoolean()) {
            OldWeapons.cutlass = new WeaponCutlass();
            TotalTinkersRegister.initForgeTool(OldWeapons.cutlass, event);
        }
        if (OldWeapons.javelinEnabled.getBoolean()) {
            OldWeapons.javelin = new WeaponJavelin();
            event.getRegistry().register(OldWeapons.javelin);
            TinkerRegistry.registerToolStationCrafting(OldWeapons.javelin);
            TinkerRegistry.registerToolForgeCrafting(OldWeapons.javelin);
            TotalTinkers.proxy.registerToolModel(OldWeapons.javelin);
        }
        if (OldWeapons.daggerEnabled.getBoolean()) {
            OldWeapons.dagger = new WeaponDagger();
            event.getRegistry().register(OldWeapons.dagger);
            TinkerRegistry.registerToolForgeCrafting(OldWeapons.dagger);
            TinkerRegistry.registerToolStationCrafting(OldWeapons.dagger);
            TotalTinkers.proxy.registerToolModel(OldWeapons.dagger);
        }
        if (OldWeapons.isReplacingCrossbow.getBoolean()) {
            TinkerRangedWeapons.crossBow = new WeaponCrossbowOveride();
            TotalTinkersRegister.initForgeTool(TinkerRangedWeapons.crossBow, event);
        }
    }


    @Override
    public String getCategoryName() {
        return null;
    }

    @Override
    public Property getConfigProperty() {
        return null;
    }
}
