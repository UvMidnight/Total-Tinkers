package uvmidnight.totaltinkers.explosives;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.events.MaterialEvent;
import slimeknights.tconstruct.library.materials.IMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.Pattern;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.tools.ToolPart;
import slimeknights.tconstruct.library.traits.ITrait;
import slimeknights.tconstruct.tools.TinkerTools;
import uvmidnight.totaltinkers.IModule;
import uvmidnight.totaltinkers.TotalTinkers;
import uvmidnight.totaltinkers.explosives.materials.ExplosiveMaterialStats;
import uvmidnight.totaltinkers.explosives.materials.ExplosivePartType;
import uvmidnight.totaltinkers.explosives.modifiers.ModRange;
import uvmidnight.totaltinkers.explosives.modifiers.ModTrueExplosive;
import uvmidnight.totaltinkers.newweapons.NewWeapons;
import uvmidnight.totaltinkers.oldweapons.OldWeapons;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Explosives extends IModule {
    public static final HashMap<String, Set<IMaterialStats>> MASTER_STATS = new HashMap<>();

    final static String CategoryName = "Explosives Module";

    public static Property explosiveCoresFromConfig;

//    public static Property bombEnabled;
//    public static WeaponBomb weaponbomb;

    public static ExplosiveBow explosiveBow;
    public static ExplosiveArrow explosiveArrow;
    public static Property explosiveBowEnabled;

    public static ToolPart explosiveCore;
    public static Modifier EXPLOSIVERANGE;
    public static Modifier TRUEEXPLOSIVE;

    public static Property trueExplosionEnabled;
    public static Property trueExplosionMult;

//    public static Modifier EXTENSION;
//    public static Modifier EXTENSION;

    public Explosives(boolean isEnabled) {
        super(isEnabled);
    }

    @Override
    public void buildConfig(Configuration cfg) {
//        bombEnabled = cfg.get(CategoryName, "Bomb Enabled", true, "If the bomb should be enabled");
        explosiveBowEnabled = cfg.get(CategoryName,"Explosive Bow Enabled", true, "If the explosive bow and ammunition should be enabled");
        explosiveCoresFromConfig = cfg.get(CategoryName,"Explosive Cores from Config", false, "If explosive core materials should be loaded from config directory instead of the mod's resource folder. Disables default materials. For examples check the assets.");
//        trueExplosionEnabled = cfg.get(CategoryName,"True Explosion modifier Enabled", true, "Enable a modifier to spawn an actual explosion. Damages terrain and requires a dragon egg.");
//        trueExplosionMult = cfg.get(CategoryName,"True Explosion modifier radius ", 0.8, "How large the radius of true explosion should be.");
    }

    public void initItems(RegistryEvent.Register<Item> event) {

        Material.UNKNOWN.addStats(new ExplosiveMaterialStats(1F, 1F, 400));

        ConfigExplosiveMaterials.load();

        Iterator<Map.Entry<String, Set<IMaterialStats>>> iterator = MASTER_STATS.entrySet().iterator();
        while (iterator.hasNext())
        {
            Map.Entry<String, Set<IMaterialStats>> entry = iterator.next();
            System.out.println(entry.getKey());
            Material material = TinkerRegistry.getMaterial(entry.getKey());

            if (material == Material.UNKNOWN)
                continue;

            for (IMaterialStats stat : entry.getValue())
                TinkerRegistry.addMaterialStats(material, stat);

            iterator.remove();
        }

        explosiveCore = new ToolPart(Material.VALUE_Ingot * 5);
        explosiveCore.setTranslationKey("explosiveCore").setRegistryName("explosiveCore");
        event.getRegistry().register(explosiveCore);
        TinkerRegistry.registerToolPart(explosiveCore);
        TotalTinkers.proxy.registerToolPartModel(explosiveCore);
        for (ToolCore toolCore : TinkerRegistry.getTools())
        {
            for (PartMaterialType partMaterialType : toolCore.getRequiredComponents())
            {
                if (partMaterialType.getPossibleParts().contains(explosiveCore))
                {
                    ItemStack stencil = new ItemStack(TinkerTools.pattern);
                    Pattern.setTagForPart(stencil, explosiveCore);
                    TinkerRegistry.registerStencilTableCrafting(stencil);
                    return;
                }
            }
        }
        EXPLOSIVERANGE = new ModRange();
        EXPLOSIVERANGE.addItem(new ItemStack(Blocks.TNT), 1,1);
//        TRUEEXPLOSIVE = new ModTrueExplosive();
//        TRUEEXPLOSIVE.addItem(new ItemStack(Blocks.DRAGON_EGG), 1, 1);
//
//        weaponbomb = new WeaponBomb();
//        event.getRegistry().register(weaponbomb);
        if (explosiveBowEnabled.getBoolean()) {
            explosiveBow = new ExplosiveBow();
            event.getRegistry().register(explosiveBow);
            TinkerRegistry.registerToolForgeCrafting(explosiveBow);
            TotalTinkers.proxy.registerToolModel(explosiveBow);

            explosiveArrow = new ExplosiveArrow();
            event.getRegistry().register(explosiveArrow);
            TotalTinkers.proxy.registerToolModel(explosiveArrow);
            TinkerRegistry.registerToolForgeCrafting(explosiveArrow);
        }
    }

    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(Explosives.class);
    }

    @SubscribeEvent
    public static void addMaterialStats(MaterialEvent.MaterialRegisterEvent event) {
        Set<IMaterialStats> stats = MASTER_STATS.get(event.material.identifier);

        if (stats == null) return;

        for (IMaterialStats stat : stats)
            TinkerRegistry.addMaterialStats(event.material, stat);

        MASTER_STATS.remove(event.material.identifier);
    }

    public void postInit(FMLPostInitializationEvent event) {
        MASTER_STATS.clear();

        for (Material material : TinkerRegistry.getAllMaterials()) {
            for (ITrait trait : material.getAllTraitsForStats(ExplosivePartType.HANDLE))
                material.addTrait(trait, ExplosivePartType.EXPLOSIVE_CORE);
        }
    }
}
