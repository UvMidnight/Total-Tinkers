package uvmidnight.totaltinkers;

import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;

public abstract class IModule {
    protected boolean isEnabled;

    public IModule(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
    public abstract void buildConfig(Configuration cfg);

    public void preInit() {
    }

    public void init() {
    }
    public boolean isEnabled() {
        return isEnabled;
    }

    public void initItems(RegistryEvent.Register<Item> event) {
    }

}
