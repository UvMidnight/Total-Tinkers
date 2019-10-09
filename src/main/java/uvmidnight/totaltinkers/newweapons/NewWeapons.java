package uvmidnight.totaltinkers.newweapons;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.RegistryEvent;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tools.Pattern;
import slimeknights.tconstruct.library.tools.ToolPart;
import slimeknights.tconstruct.tools.TinkerTools;
import uvmidnight.totaltinkers.IModule;
import uvmidnight.totaltinkers.TotalTinkers;
import uvmidnight.totaltinkers.TotalTinkersRegister;

public class NewWeapons implements IModule {
    public static String CategoryNew = "New Weapons";

    public static Property greatbladeEnabled;

    public static Property greatbladeBossMultiplier;
    public static Property greatbladeBossCap;
    public static Property greatbladeNormalCap;
    public static Property greatbladePercentCap;
    public static Property greatbladePercentCalc;

    public static Property greatbladeCoreCraftable;
    public static Property greatbladeCoreFromEndShip;

    public static ToolPart greatbladeCore;
    public static WeaponGreatblade greatblade;

    @Override
    public void buildConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CategoryNew, "Configuration for New Weapons");

        greatbladeEnabled = cfg.get(CategoryNew, "greatbladeEnabled", true, "If the percentage hp monster of death is enabled");
        greatbladeBossMultiplier = cfg.get(CategoryNew, "Greatblade Boss Multiplier", 1f, "Multiplier for the percent damage dealt to bosses.", 0f, 20f);
        greatbladeBossCap = cfg.get(CategoryNew, "Greatblade Boss Damage Cap", 9000, "Cap for the damage that the hp damage does to bosses", 0, 9000);
        greatbladeNormalCap = cfg.get(CategoryNew, "Greatblade Normal Enemy Damage Cap", 9000, "Cap for the damage that the hp damage does to normal entities. This is separate of the cap to bosses", 0, 9000);
        greatbladePercentCap = cfg.get(CategoryNew, "Greatblade percent HP Cap", 25.0F, "Cap for the percent of damage that the greatblade can deal per hit.");
        greatbladePercentCalc = cfg.get(CategoryNew, "Greatblade Diminishing Calculations", 100F, "Internal number used for calculating the diminishing returns for the relation of Attack Damage to Percent HP damage. Lower numbers means that the value diminishes faster, Higher values makes the percent HP scaling more linear.", 0,10000);
        greatbladeCoreCraftable = cfg.get(CategoryNew, "greatbladeCoreCraftable", true, "If the greatblade core should be obtainable normally. If you wish to add your own recipe, set this to false.");
        greatbladeCoreFromEndShip = cfg.get(CategoryNew, "Greatblade Core Stencil from End Cities?", true, "Should the greatblade core pattern come from end cities. Disable to make it craftable in the stencil table.");
    }

    @Override
    public void initItems(RegistryEvent.Register<Item> event) {
        if (NewWeapons.greatbladeEnabled.getBoolean()) {
            greatbladeCore = new ToolPart(Material.VALUE_Ingot * 12);
            greatbladeCore.setTranslationKey("greatbladeCore").setRegistryName("greatbladeCore");
            event.getRegistry().register(greatbladeCore);
            TinkerRegistry.registerToolPart(greatbladeCore);
            TotalTinkers.proxy.registerToolPartModel(greatbladeCore);
            if (!NewWeapons.greatbladeCoreFromEndShip.getBoolean() && greatbladeCoreCraftable.getBoolean()) {
                TinkerRegistry.registerStencilTableCrafting(Pattern.setTagForPart(new ItemStack(TinkerTools.pattern), greatbladeCore));
            }
        }

        if (NewWeapons.greatbladeEnabled.getBoolean()) {
            greatblade = new WeaponGreatblade();
            TotalTinkersRegister.initForgeTool(greatblade, event);
        }
    }
}
