package uvmidnight.totaltinkers.explosives.modifiers;

import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.Tags;
import slimeknights.tconstruct.tools.modifiers.ToolModifier;
import uvmidnight.totaltinkers.explosives.materials.ExplosiveProjectileNBT;

public class ModTrueExplosive extends ToolModifier {
    public ModTrueExplosive() {
        super("true_explosive",  0xC70039 );
        addAspects(new ModifierAspect.DataAspect(this), new ModifierAspect.SingleAspect(this));
    }

    @Override
    public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
        ModifierNBT.IntegerNBT data = ModifierNBT.readInteger(modifierTag);

        ExplosiveProjectileNBT toolData = new ExplosiveProjectileNBT(TagUtil.getTagSafe(rootCompound, Tags.TOOL_DATA));
        toolData.radius -= 1;

        TagUtil.setToolTag(rootCompound, toolData.get());
    }
}
