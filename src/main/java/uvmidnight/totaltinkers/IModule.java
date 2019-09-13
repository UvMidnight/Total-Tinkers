package uvmidnight.totaltinkers;

import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.RegistryEvent;

public interface IModule {
    void buildConfig(Configuration cfg);

    default void preInit() {
    }

    default void init() {
    }

    default void initItems(RegistryEvent.Register<Item> event) {
    }

}
