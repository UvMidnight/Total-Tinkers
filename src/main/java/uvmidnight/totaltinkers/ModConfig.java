package uvmidnight.totaltinkers;


import net.minecraftforge.common.config.Configuration;
import uvmidnight.totaltinkers.experimental.Experimental;
import uvmidnight.totaltinkers.explosives.Explosives;
import uvmidnight.totaltinkers.newweapons.NewWeapons;
import uvmidnight.totaltinkers.oldweapons.OldWeapons;

import java.util.ArrayList;

public class ModConfig {
    public static boolean oldWeapons = true;
    public static boolean newWeapons = true;
    public static boolean experimental = true;
    public static boolean explosives = true;

    public static void readConfig(ArrayList<IModule> Modules) {
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
        //big oof
        cfg.addCustomCategoryComment("Enabled Modules", "What Modules are enabled");
         oldWeapons = cfg.getBoolean("Old Weapons Enabled?", "Enabled Modules", true, "Should the old 1.7 weapons be added, born anew");
         newWeapons = cfg.getBoolean("New Weapons Enabled?", "Enabled Modules", true, "Should new relatively polished weapons be added");
         experimental = cfg.getBoolean("Experimental Enabled?", "Enabled Modules", false, "Should the experimental module be enabled. Nothing in the module is enabled by default");
         explosives = cfg.getBoolean("Explosives Enabled?", "Enabled Modules", true, "Should the explosives module be enabled. Note that all explosives are non-terrain damaging and cannot be used for griefing.");

        TotalTinkers.Modules.add(new OldWeapons(oldWeapons));
        TotalTinkers.Modules.add(new NewWeapons(newWeapons));
        TotalTinkers.Modules.add(new Experimental(experimental));
        TotalTinkers.Modules.add(new Explosives(explosives));

        for (IModule module : TotalTinkers.Modules) {
            module.buildConfig(cfg);
        }
    }
}
