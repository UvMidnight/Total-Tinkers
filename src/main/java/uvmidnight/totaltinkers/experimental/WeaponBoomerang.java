package uvmidnight.totaltinkers.experimental;

import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tools.SwordCore;
import slimeknights.tconstruct.library.tools.ToolNBT;

import java.util.List;

public class WeaponBoomerang extends SwordCore {
    @Override
    protected ToolNBT buildTagData(List<Material> materials) {
        return null;
    }

    @Override
    public float damagePotential() {
        return 0;
    }

    @Override
    public double attackSpeed() {
        return 0;
    }
}
