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
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uvmidnight.totaltinkers.proxy.CommonProxy;
import uvmidnight.totaltinkers.util.Config;

import java.io.File;


@Mod.EventBusSubscriber
@Mod(name = TotalTinkers.NAME, modid = TotalTinkers.MODID, version = TotalTinkers.VERSION, dependencies = "after:tconstruct", acceptedMinecraftVersions = "[1.12, 1.13)")
public class TotalTinkers {
  public static final String NAME = "Total Tinkers";
  public static final String MODID = "totaltinkers";
  public static final String VERSION = "0.1.2";


  @Mod.Instance(MODID)
  public static TotalTinkers instance;

  public static Configuration config;

  public static Logger logger = LogManager.getLogger(MODID);

  @SidedProxy(serverSide = "uvmidnight.totaltinkers.proxy.CommonProxy", clientSide = "uvmidnight.totaltinkers.proxy.ClientProxy")
  public static CommonProxy proxy;

  @SubscribeEvent
  public static void modifyLootTables(LootTableLoadEvent e) {
    if (e.getName().toString().equals("minecraft:chests/village_blacksmith") && Config.fullGuardFromVillages && Config.fullGuardEnabled) {
      LootEntry entry = new LootEntryTable(new ResourceLocation("totaltinkers:inject/village_blacksmith"), 1, 0, new LootCondition[0], MODID + ":greatbladePatternEndTreasure");
      LootPool pool = new LootPool(new LootEntry[]{entry}, new LootCondition[0], new RandomValueRange(1F, 1F), new RandomValueRange(0F, 0F), MODID + ":greatbladePatternEndTreasure");
      e.getTable().addPool(pool);
    }
    if (e.getName().toString().equals("minecraft:chests/end_city_treasure") && Config.greatbladeCoreFromEndShip & Config.fullGuardEnabled) {
      LootEntry entry = new LootEntryTable(new ResourceLocation("totaltinkers:inject/end_city_treasure"), 1, 0, new LootCondition[0], MODID + ":fullGuardPatternVillageBlacksmith");
      LootPool pool = new LootPool(new LootEntry[]{entry}, new LootCondition[0], new RandomValueRange(1F, 1F), new RandomValueRange(0F, 0F), MODID + ":fullGuardPatternVillageBlacksmith");
      e.getTable().addPool(pool);
    }
  }

  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent e) {
    File directory = e.getModConfigurationDirectory();
    config = new Configuration(new File(directory.getPath(), "totaltinkers.cfg"));
    Config.readConfig();
    proxy.registerSubscriptions();
  }

  @Mod.EventHandler
  public void init(FMLInitializationEvent event) {
    proxy.initToolGuis();
  }
}
