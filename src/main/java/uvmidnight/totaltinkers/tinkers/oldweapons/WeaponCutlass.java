package uvmidnight.totaltinkers.tinkers.oldweapons;

import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.SwordCore;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.tools.TinkerTools;
import uvmidnight.totaltinkers.tinkers.newweapons.NewWeapons;

import java.util.List;

//New cutlass behavior, since blocking was removed. Cutlass has a 10% chance to automatically crit
//Crits with the cutlass will give a fleeting burst of speed (1 second)
//not sure about the behavior right now, if you have any ideas how to make it less.. op like the old cutlass, but hit me up on discord or wherever else you can find me
//not sure if it will be private like the old cutlass
public class WeaponCutlass extends SwordCore {
  public static final float DURABILITY_MODIFIER = 1.2f;


  public WeaponCutlass() {
    super(PartMaterialType.handle(TinkerTools.toolRod),
            PartMaterialType.head(TinkerTools.swordBlade),
            PartMaterialType.extra(TinkerTools.wideGuard));

    addCategory(Category.WEAPON);

    setTranslationKey("cutlass").setRegistryName("cutlass");
  }

  @Override
  public float damagePotential() {
    return 1.0F;
  }

  @Override
  public double attackSpeed() {
    return 1.6F;
  }

  @Override
  public ToolNBT buildTagData(List<Material> materials) {
    HandleMaterialStats handle = materials.get(0).getStatsOrUnknown(MaterialTypes.HANDLE);
    HeadMaterialStats head1 = materials.get(1).getStatsOrUnknown(MaterialTypes.HEAD);
    ExtraMaterialStats binding = materials.get(2).getStatsOrUnknown(MaterialTypes.EXTRA);

    ToolNBT data = new ToolNBT();
    data.head(head1);
    data.extra(binding);
    data.handle(handle);

    data.attack += 1f;
    data.durability *= DURABILITY_MODIFIER;
    return data;
  }
}
