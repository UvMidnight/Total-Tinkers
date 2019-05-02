package uvmidnight.totaltinkers.tinkers;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.tools.ToolPart;
import uvmidnight.totaltinkers.TotalTinkers;
import uvmidnight.totaltinkers.potions.PotionBerserker;
import uvmidnight.totaltinkers.tinkers.newweapons.WeaponGreatblade;
import uvmidnight.totaltinkers.tinkers.oldweapons.RenderJavelin;
import uvmidnight.totaltinkers.tinkers.oldweapons.WeaponBattleAxe;
import uvmidnight.totaltinkers.tinkers.oldweapons.WeaponCutlass;
import uvmidnight.totaltinkers.tinkers.oldweapons.WeaponJavelin;
import uvmidnight.totaltinkers.tinkers.oldweapons.entities.EntityJavelin;
import uvmidnight.totaltinkers.util.Config;


import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class TinkerItems {
//  private static final List<ToolCore> tools = new ArrayList<>();
//  private static final List<IToolPart> toolParts = new ArrayList<>();
  public static WeaponGreatblade greatblade;
  public static ToolPart greatbladeCore;
  public static WeaponBattleAxe battleaxe;
  public static ToolPart fullGuard;
  public static WeaponCutlass cutlass;
  public static PotionBerserker potionBerserker;
  public static WeaponJavelin javelin;

  @SubscribeEvent
  public static void initItems(RegistryEvent.Register<Item> event) {
    TotalTinkers.logger.info("Initing items");

//    {
//      @SideOnly(Side.CLIENT)
//      @Override
//      public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
//      {
//        tooltip.add(I18n.format("tooltip.greatbladecore"));
//        super.addInformation(stack, worldIn, tooltip, flagIn);
//      }
//    }
    ;

//    {
//      @SideOnly(Side.CLIENT)
//      @Override
//      public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
//        tooltip.add(I18n.format("tooltip.fullguard"));
//        super.addInformation(stack, worldIn, tooltip, flagIn);
//      }
//    }
    ;
    if (Config.greatbladeEnabled) {
      greatbladeCore = new ToolPart(Material.VALUE_Ingot * 12);
      greatbladeCore.setTranslationKey("greatbladeCore").setRegistryName("greatbladeCore");
      event.getRegistry().register(greatbladeCore);
      TinkerRegistry.registerToolPart(greatbladeCore);
      TotalTinkers.proxy.registerToolPartModel(greatbladeCore);

      greatblade = new WeaponGreatblade();
      initForgeTool(greatblade, event);
    }

    if (Config.battleaxeEnabled) {
      battleaxe = new WeaponBattleAxe();
      initForgeTool(battleaxe, event);
    }
    if (Config.cutlassEnabled) {
      fullGuard = new ToolPart(Material.VALUE_Ingot * 3);
      fullGuard.setTranslationKey("fullGuard").setRegistryName("fullGuard");
      event.getRegistry().register(fullGuard);
      TinkerRegistry.registerToolPart(fullGuard);
      TotalTinkers.proxy.registerToolPartModel(fullGuard);

      cutlass = new WeaponCutlass();
      initForgeTool(cutlass, event);
    }
    if (Config.javelinEnabled) {
      javelin = new WeaponJavelin();
      initForgeTool(javelin, event);
    }
  }

  @SubscribeEvent
  public static void onRegisterPotions(RegistryEvent.Register<Potion> event) {
    potionBerserker = new PotionBerserker(false, 0xff0000);
    event.getRegistry().register(potionBerserker);
  }

  @SubscribeEvent
  public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
    EntityRegistry.registerModEntity(new ResourceLocation(TotalTinkers.MODID, "javelin"), EntityJavelin.class,"javelin", 1, TotalTinkers.instance, 64, 1, false);
  }

  @SubscribeEvent
  public static void registerModels(ModelRegistryEvent event) {
    RenderingRegistry.registerEntityRenderingHandler(EntityJavelin.class, RenderJavelin::new);
  }
  private static void initForgeTool(ToolCore core, RegistryEvent.Register<Item> event) {
    event.getRegistry().register(core);
    TinkerRegistry.registerToolForgeCrafting(core);
    TotalTinkers.proxy.registerToolModel(core);
  }
}