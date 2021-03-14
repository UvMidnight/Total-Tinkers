package uvmidnight.totaltinkers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import slimeknights.tconstruct.common.ModelRegisterUtil;
import slimeknights.tconstruct.library.TinkerRegistryClient;
import slimeknights.tconstruct.library.client.MaterialRenderInfo;
import slimeknights.tconstruct.library.client.ToolBuildGuiInfo;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.library.tools.ToolCore;
import uvmidnight.totaltinkers.explosives.Explosives;
import uvmidnight.totaltinkers.newweapons.NewWeapons;
import uvmidnight.totaltinkers.oldweapons.OldWeapons;

//I need to remove the proxy from existence
public class ClientProxy extends CommonProxy {

    public void initToolGuis() {
        if (NewWeapons.greatblade != null) {
            ToolBuildGuiInfo greatbladeInfo = new ToolBuildGuiInfo(NewWeapons.greatblade);
            greatbladeInfo.addSlotPosition(33 - 10 - 14, 42 + 10 + 12); // handle
            greatbladeInfo.addSlotPosition(33 - 8 + 6, 42 - 10 + 4 - 4); // head
            greatbladeInfo.addSlotPosition(33 + 14 + 6, 42 - 10 - 2 - 4); // head 2
            greatbladeInfo.addSlotPosition(33 + 10 - 10, 42 + 10 + 6); //core
            greatbladeInfo.addSlotPosition( 33 - 10 - 12, 42); //guard
            TinkerRegistryClient.addToolBuilding(greatbladeInfo);
        }
        if (OldWeapons.battleaxe != null) {
            ToolBuildGuiInfo battleaxeInfo = new ToolBuildGuiInfo(OldWeapons.battleaxe);
            battleaxeInfo.addSlotPosition(33 - 10 - 2, 42 + 10); // handle
            battleaxeInfo.addSlotPosition(33 + 10 + 16 - 2, 42 - 10 + 16); // head 1
            battleaxeInfo.addSlotPosition(33 + 10 - 16 - 2, 42 - 10 - 16); // head 2
            battleaxeInfo.addSlotPosition(33 + 13 - 2, 42 - 13); // binding
            TinkerRegistryClient.addToolBuilding(battleaxeInfo);
        }
        if (OldWeapons.cutlass != null) {
            ToolBuildGuiInfo cutlassInfo = new ToolBuildGuiInfo(OldWeapons.cutlass);
            cutlassInfo.addSlotPosition(33 - 20 - 1, 42 + 20); // handle
            cutlassInfo.addSlotPosition(33 + 20 - 5, 42 - 20 + 4); // blade
            cutlassInfo.addSlotPosition(33 - 2 - 1, 42 + 2); // guard
            TinkerRegistryClient.addToolBuilding(cutlassInfo);
        }
        if (OldWeapons.javelin != null) {
            ToolBuildGuiInfo javelinInfo = new ToolBuildGuiInfo(OldWeapons.javelin);
            javelinInfo.addSlotPosition(33 + 20 - 1, 42 + 20); // handle
            javelinInfo.addSlotPosition(33 - 20 + 5, 42 - 20 + 4); // blade
            javelinInfo.addSlotPosition(33 - 2 + 1, 42 + 2); // guard
            TinkerRegistryClient.addToolBuilding(javelinInfo);
        }
        if (OldWeapons.dagger != null) {
            ToolBuildGuiInfo daggerInfo = new ToolBuildGuiInfo(OldWeapons.dagger);
            daggerInfo.addSlotPosition(33 - 20 - 1, 42 + 20); // handle
            daggerInfo.addSlotPosition(33 + 20 - 5, 42 - 20 + 4); // blade
            daggerInfo.addSlotPosition(33 - 2 - 1, 42 + 2); // guard
            TinkerRegistryClient.addToolBuilding(daggerInfo);
        }
        if (NewWeapons.weaponScimitar != null) {
            ToolBuildGuiInfo scimitarInfo = new ToolBuildGuiInfo(NewWeapons.weaponScimitar);
            scimitarInfo.addSlotPosition(33 - 20 - 1, 42 + 20); // handle
            scimitarInfo.addSlotPosition(33 + 20 - 5, 42 - 20 + 4); // blade
            scimitarInfo.addSlotPosition(33 - 2 - 1, 42 + 2); // guard
            TinkerRegistryClient.addToolBuilding(scimitarInfo);
        }
        if (Explosives.explosiveBow != null) {
            ToolBuildGuiInfo explosiveBowInfo = new ToolBuildGuiInfo(Explosives.explosiveBow);
            explosiveBowInfo.addSlotPosition(32 + 12, 41 - 22); // top limb
            explosiveBowInfo.addSlotPosition(32 - 22, 41 + 12); // left limb
            explosiveBowInfo.addSlotPosition(32 - 15, 41 - 15); // grip
            explosiveBowInfo.addSlotPosition(32 + 6, 41 + 6); // center bowstring
            TinkerRegistryClient.addToolBuilding(explosiveBowInfo);
        }
        if (Explosives.explosiveArrow != null) {
            ToolBuildGuiInfo explosiveArrowInfo = new ToolBuildGuiInfo(Explosives.explosiveArrow);
            explosiveArrowInfo.addSlotPosition(32, 41); // center
            explosiveArrowInfo.addSlotPosition(32 + 18, 41 - 18); // top right
            explosiveArrowInfo.addSlotPosition(32 - 18, 41 + 18); // bot left
            TinkerRegistryClient.addToolBuilding(explosiveArrowInfo);
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
