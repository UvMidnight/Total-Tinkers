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

import uvmidnight.totaltinkers.experimental.Experimental;
import uvmidnight.totaltinkers.experimental.potion.PotionHemorrhage;
import uvmidnight.totaltinkers.oldweapons.OldWeapons;
import uvmidnight.totaltinkers.oldweapons.RenderJavelin;
import uvmidnight.totaltinkers.oldweapons.entity.EntityJavelin;
import uvmidnight.totaltinkers.oldweapons.potion.PotionBerserker;

//THIS IS LEGACY CODE!!! I AM TOO LAZY TO FIX IT
@Mod.EventBusSubscriber
public class TotalTinkersRegister {

    //public static SemiPistol semiPistol;

    @SubscribeEvent
    public static void initItems(RegistryEvent.Register<Item> event) {
        /*
        semiPistol = new SemiPistol();
        event.getRegistry().register(semiPistol);
        TotalTinkers.proxy.registerToolModel(semiPistol);
        */
        for (IModule module : TotalTinkers.Modules) {
            module.initItems(event);
        }

        for (IModifier modifier : new IModifier[]{
                TinkerModifiers.modBaneOfArthopods,
                TinkerModifiers.modBeheading,
                TinkerModifiers.modDiamond,
                TinkerModifiers.modEmerald,
                TinkerModifiers.modGlowing,
                TinkerModifiers.modHaste,
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
        if (OldWeapons.battleaxeEnabled.getBoolean()) {
            OldWeapons.potionBerserker = new PotionBerserker(false, 0xff0000);
            event.getRegistry().register(OldWeapons.potionBerserker);
        }
        if (Experimental.scimitarEnabled.getBoolean()){
            Experimental.potionHemorrhage = new PotionHemorrhage(true, 0xff0000);
//            event.getRegistry().register(Experimental.potionHemorrhage);
        }
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        if (OldWeapons.javelinEnabled.getBoolean()) {
            EntityRegistry.registerModEntity(new ResourceLocation(TotalTinkers.MODID, "javelin"), EntityJavelin.class, "javelin", 1, TotalTinkers.instance, 64, 1, false);
        }
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        if (OldWeapons.javelinEnabled.getBoolean()) {
            RenderingRegistry.registerEntityRenderingHandler(EntityJavelin.class, RenderJavelin::new);
        }
    }

    public static void initForgeTool(ToolCore core, RegistryEvent.Register<Item> event) {
        event.getRegistry().register(core);
        TinkerRegistry.registerToolForgeCrafting(core);
        TotalTinkers.proxy.registerToolModel(core);
    }
}