package uvmidnight.totaltinkers.newweapons;

import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.SwordCore;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.tools.TinkerTools;

import java.util.List;

public class WeaponDualDagger extends SwordCore {
    public WeaponDualDagger() {
        super(PartMaterialType.head(TinkerTools.knifeBlade), PartMaterialType.handle(TinkerTools.toolRod));

        this.addCategory(Category.WEAPON);

        setTranslationKey("Dual Daggers").setRegistryName("Dual Daggers");
    }

    @Override
    protected ToolNBT buildTagData(List<Material> materials) {
        return null;
    }

    @Override
    public float damagePotential() {
        return 0.4F;
    }

    @Override
    public double attackSpeed() {
        return 1.8F;
    }
}
