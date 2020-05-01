package uvmidnight.totaltinkers.experimental;

import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.RegistryEvent;
import uvmidnight.totaltinkers.IModule;


public class Experimental extends IModule {
    final static String CategoryName = "Experimental Module";

    public static Property boomerangEnabled;


    public Experimental(boolean isEnabled) {
        super(isEnabled);
    }

    @Override
    public void buildConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CategoryName, "Configuration for new experimental weapons.");
        //boomerangEnabled = cfg.get(CategoryName, "Boomerang Enabled", false, "If the boomerang should be enabled");
        //bombEnabled = cfg.get(CategoryName, "Bomb Enabled", false, "If the bomb should be enabled");
    }


}
