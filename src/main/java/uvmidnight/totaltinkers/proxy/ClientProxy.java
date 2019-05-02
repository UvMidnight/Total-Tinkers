package uvmidnight.totaltinkers.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.common.ModelRegisterUtil;
import slimeknights.tconstruct.library.TinkerRegistryClient;
import slimeknights.tconstruct.library.client.MaterialRenderInfo;
import slimeknights.tconstruct.library.client.ToolBuildGuiInfo;
import slimeknights.tconstruct.library.client.renderer.RenderProjectileBase;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.library.tinkering.TinkersItem;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.library.tools.ToolCore;
import uvmidnight.totaltinkers.TotalTinkers;
import uvmidnight.totaltinkers.tinkers.TinkerItems;
import uvmidnight.totaltinkers.tinkers.oldweapons.LayerBattleaxe;
import uvmidnight.totaltinkers.tinkers.oldweapons.RenderJavelin;
import uvmidnight.totaltinkers.tinkers.oldweapons.entities.EntityJavelin;

public class ClientProxy extends CommonProxy {
  private LayerBattleaxe layerBattleaxe;

  public void initToolGuis() {
    TotalTinkers.logger.info("initing tool guis");
    if (TinkerItems.greatblade != null) {
      ToolBuildGuiInfo greatbladeInfo = new ToolBuildGuiInfo(TinkerItems.greatblade);
      greatbladeInfo.addSlotPosition(7, 64);
      greatbladeInfo.addSlotPosition(25, 38);
      greatbladeInfo.addSlotPosition(49, 38);
      greatbladeInfo.addSlotPosition(7, 38);
      TinkerRegistryClient.addToolBuilding(greatbladeInfo);
    }
    if (TinkerItems.battleaxe != null) {
      ToolBuildGuiInfo battleaxeInfo = new ToolBuildGuiInfo(TinkerItems.battleaxe);
      battleaxeInfo.addSlotPosition(33, 42 + 18);
      battleaxeInfo.addSlotPosition(33 + 20, 42 - 20);
      battleaxeInfo.addSlotPosition(33, 42);
      battleaxeInfo.addSlotPosition(33 - 18, 42 + 18);
      TinkerRegistryClient.addToolBuilding(battleaxeInfo);
    }
    if (TinkerItems.cutlass != null) {
      ToolBuildGuiInfo cutlassInfo = new ToolBuildGuiInfo(TinkerItems.cutlass);
      cutlassInfo.addSlotPosition(33, 42 + 18);
      cutlassInfo.addSlotPosition(33 + 20, 42 - 20);
      cutlassInfo.addSlotPosition(33, 42);
      TinkerRegistryClient.addToolBuilding(cutlassInfo);
    }
    if (TinkerItems.javelin != null) {
      ToolBuildGuiInfo javelinInfo = new ToolBuildGuiInfo(TinkerItems.javelin);
      javelinInfo.addSlotPosition(33, 42 + 18);
      javelinInfo.addSlotPosition(33 + 20, 42 - 20);
      javelinInfo.addSlotPosition(33, 42);
      TinkerRegistryClient.addToolBuilding(javelinInfo);
    }

  }


  public void registerSubscriptions() {
    layerBattleaxe = new LayerBattleaxe(Minecraft.getMinecraft());
    MinecraftForge.EVENT_BUS.register(layerBattleaxe);
//    MinecraftForge.EVENT_BUS.register(new RenderHandler(Minecraft.getMinecraft()));
//    MinecraftForge.EVENT_BUS.register(new ClientEventHandler());

  }

  public void renderScreenFullColor(int color, boolean bool) {
    if (layerBattleaxe != null) layerBattleaxe.makeRenderFullColor(color, bool);
}
  public boolean hasBattleaxeOverlay() {
    if (layerBattleaxe != null) {
      return layerBattleaxe.isRendering();
    } else {
      return false;
    }
  }

  public void toggleBattleAxeOverlay(int color) {
    if (layerBattleaxe != null) {
      if (!layerBattleaxe.isRendering()) {
        layerBattleaxe.makeRenderFullColor(color);
      } else {
        layerBattleaxe.makeRenderFullColor(false);
      }
    }
  }

  public void registerItemRenderer(Item item, int meta, String id) {
    ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(TotalTinkers.MODID + ":" + id, "inventory"));
  }

  public void setRenderInfo(Material mat, int color) {
    mat.setRenderInfo(color);
  }

  public void setRenderInfo(Material mat, int lo, int mid, int hi) {
    mat.setRenderInfo(new MaterialRenderInfo.MultiColor(lo, mid, hi));
  }


  public void registerToolModel(ToolCore tc) {
    ModelRegisterUtil.registerToolModel(tc);
  }

  public void registerModifierModel(IModifier mod, ResourceLocation rl) {
    ModelRegisterUtil.registerModifierModel(mod, rl);
  }

  public <T extends Item & IToolPart> void registerToolPartModel(T part) {
    ModelRegisterUtil.registerPartModel(part);
  }
}
