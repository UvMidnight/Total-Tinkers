package uvmidnight.totaltinkers;

import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public abstract class IModule {
    protected boolean isEnabled;

    public IModule(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
    public abstract void buildConfig(Configuration cfg);

    public void preInit(FMLPreInitializationEvent event) {
    }

    public void init(FMLInitializationEvent event) {
    }
    public boolean isEnabled() {
        return isEnabled;
    }

    public void initItems(RegistryEvent.Register<Item> event) {
    }
    public void postInit(FMLPostInitializationEvent event) {}
}
