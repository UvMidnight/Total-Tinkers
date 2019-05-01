package uvmidnight.totaltinkers.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import slimeknights.tconstruct.common.ModelRegisterUtil;
import slimeknights.tconstruct.library.TinkerRegistryClient;
import slimeknights.tconstruct.library.client.MaterialRenderInfo;
import slimeknights.tconstruct.library.client.ToolBuildGuiInfo;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.library.tools.ToolCore;
import uvmidnight.totaltinkers.TotalTinkers;
import uvmidnight.totaltinkers.tinkers.newweapons.NewWeapons;
import uvmidnight.totaltinkers.tinkers.oldweapons.LayerBattleaxe;

public class ClientProxy extends CommonProxy {
  public static LayerBattleaxe layerBattleaxe;

  public void initToolGuis() {
    TotalTinkers.logger.info("initing tool guis");
    if (NewWeapons.greatblade != null) {
      ToolBuildGuiInfo greatbladeInfo = new ToolBuildGuiInfo(NewWeapons.greatblade);
      greatbladeInfo.addSlotPosition(7, 64);
      greatbladeInfo.addSlotPosition(25, 38);
      greatbladeInfo.addSlotPosition(49, 38);
      greatbladeInfo.addSlotPosition(7, 38);
      TinkerRegistryClient.addToolBuilding(greatbladeInfo);
    }
    if (NewWeapons.battleaxe != null) {
      ToolBuildGuiInfo battleaxeInfo = new ToolBuildGuiInfo(NewWeapons.battleaxe);
      battleaxeInfo.addSlotPosition(33, 42 + 18);
      battleaxeInfo.addSlotPosition(33 + 20, 42 - 20);
      battleaxeInfo.addSlotPosition(33, 42);
      battleaxeInfo.addSlotPosition(33 - 18, 42 + 18);
      TinkerRegistryClient.addToolBuilding(battleaxeInfo);
    }
    if (NewWeapons.cutlass != null) {
      ToolBuildGuiInfo cutlassInfo = new ToolBuildGuiInfo(NewWeapons.cutlass);
      cutlassInfo.addSlotPosition(33, 42 + 18);
      cutlassInfo.addSlotPosition(33 + 20, 42 - 20);
      cutlassInfo.addSlotPosition(33, 42);
      TinkerRegistryClient.addToolBuilding(cutlassInfo);
    }

  }

  public void initBattleAxeOverlay() {

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
