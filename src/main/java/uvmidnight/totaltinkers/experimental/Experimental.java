package uvmidnight.totaltinkers.experimental;

import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.RegistryEvent;
import slimeknights.tconstruct.library.TinkerRegistry;
import uvmidnight.totaltinkers.IModule;
import uvmidnight.totaltinkers.TotalTinkers;
import uvmidnight.totaltinkers.experimental.potion.PotionHemorrhage;

public class Experimental implements IModule {
    final static String CategoryName = "Experimental Module";

    public static Property boomerangEnabled;
    public static Property bombEnabled;
    public static Property scimitarEnabled;

    public static PotionHemorrhage potionHemorrhage;
    public static WeaponScimitar weaponScimitar;

    @Override
    public void buildConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CategoryName, "Configuration for new experimental weapons.");
        //boomerangEnabled = cfg.get(CategoryName, "Boomerang Enabled", false, "If the boomerang should be enabled");
        //bombEnabled = cfg.get(CategoryName, "Bomb Enabled", false, "If the bomb should be enabled");
        scimitarEnabled = cfg.get(CategoryName, "Scimitar Enabled", true, "If the scimitar is enabled");
    }

    public void initItems(RegistryEvent.Register<Item> event) {
        if (scimitarEnabled.getBoolean()) {
            weaponScimitar = new WeaponScimitar();
            event.getRegistry().register(weaponScimitar);
            TinkerRegistry.registerToolForgeCrafting(weaponScimitar);
            TinkerRegistry.registerToolStationCrafting(weaponScimitar);
            TotalTinkers.proxy.registerToolModel(weaponScimitar);
        }
    }
}
