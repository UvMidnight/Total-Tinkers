package uvmidnight.totaltinkers.util;


import net.minecraftforge.common.config.Configuration;
import uvmidnight.totaltinkers.TotalTinkers;

public class Config {
  public static final String CATEGORY_ENABLED = "Enabled Tools";
  public static final String Category_TOOLCONFIG = "Tools Configuration";

  //What tools are enabled
  public static boolean greatbladeEnabled;
  public static float greatbladeBossMultiplier;

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
    cfg.addCustomCategoryComment(Category_TOOLCONFIG, "Tools Configuration");

    greatbladeEnabled = cfg.getBoolean("greatbladeEnabled", CATEGORY_ENABLED, true, "If the percentage hp monster of death is enabled");
    greatbladeBossMultiplier = cfg.getFloat("greatbladeBossMultiplier", Category_TOOLCONFIG, 1, 0, 100,"Multiplier for the percent damage dealt to bosses.");
  }

}
