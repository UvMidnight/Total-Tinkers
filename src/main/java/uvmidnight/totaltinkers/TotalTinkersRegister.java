package uvmidnight.totaltinkers;

import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.tools.TinkerModifiers;
import uvmidnight.totaltinkers.explosives.Explosives;
import uvmidnight.totaltinkers.explosives.entity.EntityBomb;
import uvmidnight.totaltinkers.explosives.entity.EntityExplosiveArrow;
import uvmidnight.totaltinkers.explosives.entity.RenderBomb;
import uvmidnight.totaltinkers.explosives.entity.RenderExplosiveArrow;
import uvmidnight.totaltinkers.newweapons.potion.PotionHemorrhage;
import uvmidnight.totaltinkers.newweapons.NewWeapons;
import uvmidnight.totaltinkers.oldweapons.OldWeapons;
import uvmidnight.totaltinkers.oldweapons.RenderJavelin;
import uvmidnight.totaltinkers.oldweapons.entity.EntityJavelin;

//THIS IS LEGACY CODE!
@Mod.EventBusSubscriber
public class TotalTinkersRegister {

    @SubscribeEvent
    public static void initItems(RegistryEvent.Register<Item> event) {
        for (IModule module : TotalTinkers.Modules) {
            if (module.isEnabled()) {
                module.initItems(event);
            }
        }

        for (IModifier modifier : new IModifier[]{
                TinkerModifiers.modBaneOfArthopods,
                TinkerModifiers.modBeheading,
                TinkerModifiers.modDiamond,
                TinkerModifiers.modEmerald,
                TinkerModifiers.modGlowing,
                TinkerModifiers.modHaste,
                TinkerModifiers.modFiery,
                TinkerModifiers.modKnockback,
                TinkerModifiers.modLuck,
                TinkerModifiers.modMendingMoss,
                TinkerModifiers.modNecrotic,
                TinkerModifiers.modReinforced,
                TinkerModifiers.modSharpness,
                TinkerModifiers.modShulking,
                TinkerModifiers.modSilktouch,
                TinkerModifiers.modSmite,
                TinkerModifiers.modSoulbound,
                TinkerModifiers.modWebbed,
        }) {
            TotalTinkers.proxy.registerModifierModel(modifier,
                    new ResourceLocation(TotalTinkers.MODID, "models/item/modifiers/" + modifier.getIdentifier()));
        }
    }

    @SubscribeEvent
    public static void onRegisterPotions(RegistryEvent.Register<Potion> event) {
        if (NewWeapons.scimitarEnabled.getBoolean() && ModConfig.newWeapons) {
            NewWeapons.potionHemorrhage = new PotionHemorrhage(true, 0xff0000);
            event.getRegistry().register(NewWeapons.potionHemorrhage);
        }
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        if (OldWeapons.javelinEnabled.getBoolean() && ModConfig.oldWeapons) {
            EntityRegistry.registerModEntity(new ResourceLocation(TotalTinkers.MODID, "javelin"), EntityJavelin.class, "javelin", 9937, TotalTinkers.instance, 64, 1, false);
        }
        if (Explosives.bombEnabled.getBoolean()) {
            EntityRegistry.registerModEntity(new ResourceLocation(TotalTinkers.MODID, "bomb"), EntityBomb.class, "bomb", 1843, TotalTinkers.instance, 64, 1, false);
        }
        if (Explosives.explosiveBowEnabled.getBoolean()) {
            EntityRegistry.registerModEntity(new ResourceLocation(TotalTinkers.MODID, "explosive_arrow"), EntityExplosiveArrow.class, "explosive_arrow", 2491, TotalTinkers.instance, 64, 1, false);
        }
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        if (OldWeapons.javelinEnabled.getBoolean() && ModConfig.oldWeapons) {
            RenderingRegistry.registerEntityRenderingHandler(EntityJavelin.class, RenderJavelin::new);
        }
        if (Explosives.bombEnabled.getBoolean()) {
            RenderingRegistry.registerEntityRenderingHandler(EntityBomb.class, RenderBomb::new);
        }

        if (Explosives.explosiveBowEnabled.getBoolean()) {
            RenderingRegistry.registerEntityRenderingHandler(EntityExplosiveArrow.class, RenderExplosiveArrow::new);
        }
    }


    public static void initForgeTool(ToolCore core, RegistryEvent.Register<Item> event) {
        event.getRegistry().register(core);
        TinkerRegistry.registerToolForgeCrafting(core);
        TotalTinkers.proxy.registerToolModel(core);
    }
}