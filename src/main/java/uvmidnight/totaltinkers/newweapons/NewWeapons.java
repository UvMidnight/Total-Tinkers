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
import uvmidnight.totaltinkers.newweapons.potion.PotionHemorrhage;

public class NewWeapons extends IModule {
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

    public static WeaponScimitar weaponScimitar;
    public static PotionHemorrhage potionHemorrhage;

    public static Property scimitarEnabled;
    public static Property scimitarDamageBase;
    public static Property scimitarDamagePerStack;
    public static Property scimitarDamageStackable;
    public static Property scimitarBleedDurationRefreshable;
    public static Property canScimitarBleedDamageBosses;
    public static Property scimitarCraftableInToolStation;
    public NewWeapons(boolean isEnabled) {
        super(isEnabled);
    }

    @Override
    public void buildConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CategoryNew, "Configuration for New Weapons");

        greatbladeEnabled = cfg.get(CategoryNew, "Greatblade enabled", true, "If the percentage hp monster of death is enabled");
        greatbladeBossMultiplier = cfg.get(CategoryNew, "Greatblade Boss Multiplier", 1f, "Multiplier for the percent damage dealt to bosses.", 0f, 20f);
        greatbladeBossCap = cfg.get(CategoryNew, "Greatblade Boss Damage Cap", Integer.MAX_VALUE, "Cap for the damage that the hp damage does to bosses", 0, Integer.MAX_VALUE);
        greatbladeNormalCap = cfg.get(CategoryNew, "Greatblade Normal Enemy Damage Cap", Integer.MAX_VALUE, "Cap for the damage that the hp damage does to normal entities. This is separate of the cap to bosses", 0, Integer.MAX_VALUE);
        greatbladePercentCap = cfg.get(CategoryNew, "Greatblade percent HP Cap", 25.0F, "Cap for the percent of damage that the greatblade can deal per hit.");
        greatbladePercentCalc = cfg.get(CategoryNew, "Greatblade Diminishing Calculations", 100F, "Internal number used for calculating the diminishing returns for the relation of Attack Damage to Percent HP damage. Lower numbers means that the value diminishes faster, Higher values makes the percent HP scaling more linear.", 0,10000);
        greatbladeCoreCraftable = cfg.get(CategoryNew, "Greatblade Core Craftable", true, "If the greatblade core should be obtainable via one of the two normal methods. If you wish to add your own recipe, set this to false.");
        greatbladeCoreFromEndShip = cfg.get(CategoryNew, "Greatblade Core Stencil from End Cities?", true, "Should the greatblade core pattern come from end cities. Disable to make it craftable in the stencil table.");

        scimitarEnabled = cfg.get(CategoryNew, "Scimitar Enabled", true, "If the scimitar is enabled");
        scimitarDamageBase = cfg.get(CategoryNew, "Scimitar Bleed Base Damage", 2f, "The damage that the scimitar bleed does per tick with the first stack of bleed.", 0, Double.MAX_VALUE);
        scimitarDamageStackable = cfg.get(CategoryNew, "Scimitar Bleed Damage Stackable", true, "If the damage that the scimitar's bleed can be stacked by repeatedly damaging targets with mostly charged attacks. The bleed will tick every half a second.");
        scimitarDamagePerStack = cfg.get(CategoryNew, "Scimitar Bleed Damage Per Stack", 1f, "If damage is stackable, this is the damage that will be added to the damage per tick of the scimitar. The bleed will tick every half a second.", Double.MIN_VALUE, Double.MAX_VALUE);
        scimitarBleedDurationRefreshable = cfg.get(CategoryNew, "Scimitar Bleed Duration Refreshable", true, "If the duration that the scimitar's bleed lasts can be stacked by repeatedly damaging targets with mostly charged attacks. The bleed will tick every half a second.");
        canScimitarBleedDamageBosses = cfg.get(CategoryNew, "Can Scimitar Bleed Damage Bosses", true, "If the bleed from the scimitar can damage bosses at all. Note that both the Ender Dragon and Wither are immune to potion effects.");
        scimitarCraftableInToolStation = cfg.get(CategoryNew, "Can Scimitar be crafted in Tool Station", false, "If the scimitar should only be craftable in the Tool Forge, and not the Tool Station..");
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
            greatblade = new WeaponGreatblade();
            TotalTinkersRegister.initForgeTool(greatblade, event);
        }

        if (NewWeapons.scimitarEnabled.getBoolean()) {
            NewWeapons.weaponScimitar = new WeaponScimitar();
            event.getRegistry().register(NewWeapons.weaponScimitar);
            TinkerRegistry.registerToolForgeCrafting(NewWeapons.weaponScimitar);
            if (scimitarCraftableInToolStation.getBoolean()) {
                TinkerRegistry.registerToolStationCrafting(NewWeapons.weaponScimitar);
            }
            TotalTinkers.proxy.registerToolModel(NewWeapons.weaponScimitar);
        }
    }
}
