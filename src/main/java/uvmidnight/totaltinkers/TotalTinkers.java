package uvmidnight.totaltinkers;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uvmidnight.totaltinkers.newweapons.NewWeapons;
import uvmidnight.totaltinkers.oldweapons.OldWeapons;

import java.io.File;
import java.util.ArrayList;

import static uvmidnight.totaltinkers.newweapons.NewWeapons.greatbladeCoreCraftable;
import static uvmidnight.totaltinkers.oldweapons.OldWeapons.fullGuardCraftable;


@Mod.EventBusSubscriber
@Mod(name = TotalTinkers.NAME, modid = TotalTinkers.MODID, version = TotalTinkers.VERSION, dependencies = "after:tconstruct", acceptedMinecraftVersions = "[1.12, 1.13)")
public class TotalTinkers {
    public static final String NAME = "Total Tinkers";
    public static final String MODID = "totaltinkers";
    public static final String VERSION = "0.6.0";


    @Mod.Instance(MODID)
    public static TotalTinkers instance;

    public static Configuration config;

    public static File directory;

    public static Logger logger = LogManager.getLogger(MODID);

    @SidedProxy(serverSide = "uvmidnight.totaltinkers.CommonProxy", clientSide = "uvmidnight.totaltinkers.ClientProxy")
    public static CommonProxy proxy;
        
    public static ArrayList<IModule> Modules = new ArrayList<>();

    @SubscribeEvent
    public static void modifyLootTables(LootTableLoadEvent e) {
        if (e.getName().toString().equals("minecraft:chests/village_blacksmith") && OldWeapons.fullGuardFromVillages.getBoolean() && fullGuardCraftable.getBoolean()) {
            LootEntry entry = new LootEntryTable(new ResourceLocation("totaltinkers:inject/village_blacksmith"), 1, 0, new LootCondition[0], MODID + ":greatbladePatternEndTreasure");
            LootPool pool = new LootPool(new LootEntry[]{entry}, new LootCondition[0], new RandomValueRange(1F, 1F), new RandomValueRange(0F, 0F), MODID + ":greatbladePatternEndTreasure");
            e.getTable().addPool(pool);
        }
        if (e.getName().toString().equals("minecraft:chests/end_city_treasure") && NewWeapons.greatbladeCoreFromEndShip.getBoolean() && greatbladeCoreCraftable.getBoolean()) {
            LootEntry entry = new LootEntryTable(new ResourceLocation("totaltinkers:inject/end_city_treasure"), 1, 0, new LootCondition[0], MODID + ":fullGuardPatternVillageBlacksmith");
            LootPool pool = new LootPool(new LootEntry[]{entry}, new LootCondition[0], new RandomValueRange(1F, 1F), new RandomValueRange(0F, 0F), MODID + ":fullGuardPatternVillageBlacksmith");
            e.getTable().addPool(pool);
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        directory = e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "totaltinkers.cfg"));
        ModConfig.readConfig(Modules);
        proxy.registerSubscriptions();
        for (IModule module : TotalTinkers.Modules) {
            if (module.isEnabled()) {
                module.preInit(e);
            }
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.initToolGuis();
        proxy.init();
        for (IModule module : TotalTinkers.Modules) {
            if (module.isEnabled()) {
                module.init(event);
            }
        }
    }
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.initToolGuis();
        for (IModule module : TotalTinkers.Modules) {
            if (module.isEnabled()) {
                module.postInit(event);
            }
        }
    }
}
