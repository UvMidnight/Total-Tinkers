package uvmidnight.totaltinkers.util;


import net.minecraftforge.common.config.Configuration;
import uvmidnight.totaltinkers.TotalTinkers;

public class Config {
  public static final String CATEGORY_ENABLED = "Enabled Tools";
  public static final String CATEGORY_TOOLCONFIG = "Tools Configuration";
  public static final String CATEGORY_BATTLEAXE = "Battleaxe Configuration";

  public static final String CATEGORY_CROSSBOW = "Crossbow configuration";
  //What tools are enabled
  public static boolean greatbladeEnabled;
  public static boolean battleaxeEnabled;
  public static boolean cutlassEnabled;
  public static boolean javelinEnabled;
  public static boolean daggerEnabled;

  public static float greatbladeBossMultiplier;

  public static int berserkerSpeed;
  public static int berserkerHaste;
  public static int berserkerResistance;
  public static int berserkerStrength;
  public static int berserkerJumpBoost;

  public static int cutlassSpeedDuration;
  public static int cutlassSpeedStrength;

  public static boolean isReplacingCrossbow;
  public static boolean crossbowOldCrosshair;
  public static boolean autoCrossbowReload;

  public static boolean fullGuardFromVillages;
  public static boolean greatbladeCoreFromEndShip;

  public static boolean disable_screen_overlay = false;

  public static void readConfig() {
    Configuration cfg = TotalTinkers.config;
    try {
      cfg.load();
      initGeneralConfig(cfg);
    } catch (Exception e) {
      TotalTinkers.logger.warn("Problem loading config file!", e);
    } finally {
      if (cfg.hasChanged()) {
        cfg.save();
      }
    }
  }

  private static void initGeneralConfig(Configuration cfg) {
    cfg.addCustomCategoryComment(CATEGORY_ENABLED, "Enabled Tools Configuration");
    cfg.addCustomCategoryComment(CATEGORY_TOOLCONFIG, "Tools Configuration");
    cfg.addCustomCategoryComment(CATEGORY_BATTLEAXE, "Battleaxe Configuration");

    greatbladeEnabled = cfg.getBoolean("greatbladeEnabled", CATEGORY_ENABLED, true, "If the percentage hp monster of death is enabled");
    battleaxeEnabled = cfg.getBoolean("battleaxeEnabled", CATEGORY_ENABLED, true, "If the swirling whirlwind of death will slay");
    cutlassEnabled = cfg.getBoolean("cuttlassEnabled", CATEGORY_ENABLED, true, "Here be the sword of the seas.");
    javelinEnabled = cfg.getBoolean("javelinEnabled", CATEGORY_ENABLED, true, "If the warframe's worst throwing weapon is enabled");
    daggerEnabled =  cfg.getBoolean("daggerEnabled", CATEGORY_ENABLED, true, "Should the rouge's weapon, the dagger, be enabled");
    isReplacingCrossbow = cfg.getBoolean("replacingCrossbow", CATEGORY_ENABLED, false, "Should the crossbow be replaced by a custom version. This is REQUIRED for any of the crossbow tweaks");
    greatbladeBossMultiplier = cfg.getFloat("greatbladeBossMultiplier", CATEGORY_TOOLCONFIG, 1, 0, 20,"Multiplier for the percent damage dealt to bosses.");

    cutlassSpeedDuration = cfg.getInt("cutlassSpeedDuration", CATEGORY_TOOLCONFIG, 30, 0, Integer.MAX_VALUE, "How many ticks of speed to give");
    cutlassSpeedStrength = cfg.getInt("cutlassSpeedStrength", CATEGORY_TOOLCONFIG, 2, -1, Short.MAX_VALUE, "What speed amplitude to give. 3 is default");

    disable_screen_overlay = cfg.getBoolean("battleaxeOverlayDisabled", CATEGORY_BATTLEAXE, false, "If the red overlay for battleaxe's berserker is dis abled");
    berserkerSpeed = cfg.getInt("berserkerSpeed", CATEGORY_BATTLEAXE, 1, Short.MIN_VALUE, Short.MAX_VALUE, "Level of speed berserker gives. 1 is speed 2.");
    berserkerHaste = cfg.getInt("berserkerHaste", CATEGORY_BATTLEAXE, 2, Short.MIN_VALUE, Short.MAX_VALUE, "Level of haste berserker gives. 2 is haste 3.");
    berserkerResistance = cfg.getInt("berserkerResistance", CATEGORY_BATTLEAXE, -5, Short.MIN_VALUE, Short.MAX_VALUE, "Level of resistance berserker gives. -4 is +80% damage dealt, 20% per level of resistance");
    berserkerStrength = cfg.getInt("berserkerStrength", CATEGORY_BATTLEAXE, 1, Short.MIN_VALUE, Short.MAX_VALUE, "Level of Strength berserker gives. 1 is Strength 2, or 3 hearts extra of damage per hit.");
    berserkerJumpBoost = cfg.getInt("berserkerJumpBoost", CATEGORY_BATTLEAXE, 1, Short.MIN_VALUE, Short.MAX_VALUE, "Level of jump boost berserker gives. 1 is jump boost 2.");

    crossbowOldCrosshair = cfg.getBoolean("crossbowOldCrosshair", CATEGORY_CROSSBOW, true, "If the old crossbow cursor should be used");
    autoCrossbowReload = cfg.getBoolean("autoCrossbowReload", CATEGORY_CROSSBOW, false, "EXPERIMENTAL FEATURE. If enabled, the crossbow will automatically reload after being shot. Works mechanically fine, but animations are broken");
  }

}
