package uvmidnight.totaltinkers;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
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
  public static final String VERSION = "0.1";

  public static Configuration config;

  public static Logger logger = LogManager.getLogger(MODID);

  @SidedProxy(serverSide = "uvmidnight.totaltinkers.proxy.CommonProxy", clientSide = "uvmidnight.totaltinkers.proxy.ClientProxy")
  public static CommonProxy proxy;

  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent e) {
    File directory = e.getModConfigurationDirectory();
    config = new Configuration(new File(directory.getPath(), "totaltinkers.cfg"));
    Config.readConfig();
  }

  @Mod.EventHandler
  public void init(FMLInitializationEvent event) {
    proxy.initToolGuis();
  }
}
