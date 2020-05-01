package uvmidnight.totaltinkers.explosives.materials;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.tools.ProjectileNBT;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.Tags;

public class ExplosiveProjectileNBT extends ProjectileNBT {
    public static final String LOC_RADIUS = "stat.totaltinkers.explosiveprojectile.radius.name";
    public static final String RADIUS = "Radius";


    public static final String LOC_Suffix = "stat.totaltinkers.explosive.duration.suffix";

    public float radius; //in meters

    public ExplosiveProjectileNBT() {super();}
    public ExplosiveProjectileNBT(NBTTagCompound tag) {
        super(tag);
    }
    public ExplosiveProjectileNBT explosiveCores(ExplosiveMaterialStats... explosiveMaterialStats) {
        for (ExplosiveMaterialStats core : explosiveMaterialStats) {
            this.attack += core.attack;
            this.durability += core.durability;
            this.radius += core.radius;
        }
        this.radius /= explosiveMaterialStats.length;

        return this;
    }

    @Override
    public void read(NBTTagCompound tag) {
        super.read(tag);
        this.radius = tag.getFloat("tt.explosionRadius");
    }

    @Override
    public void write(NBTTagCompound tag) {
        super.write(tag);
        tag.setFloat("tt.explosionRadius", radius);
    }
    public static ExplosiveProjectileNBT from(ItemStack itemStack) {
        return new ExplosiveProjectileNBT(TagUtil.getToolTag(itemStack));
    }
}
