package uvmidnight.totaltinkers.explosives.modifiers;

import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.Tags;
import slimeknights.tconstruct.tools.modifiers.ToolModifier;
import uvmidnight.totaltinkers.explosives.materials.ExplosiveProjectileNBT;

public class ModRange extends ToolModifier {
    public ModRange() {
        super("explosive_range", 0x4FDCFF);
        addAspects(new ModifierAspect.MultiAspect(this, 5, 15, 1));
    }

    @Override
    public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
        ModifierNBT.IntegerNBT data = ModifierNBT.readInteger(modifierTag);

        ExplosiveProjectileNBT toolData = new ExplosiveProjectileNBT(TagUtil.getTagSafe(rootCompound, Tags.TOOL_DATA));
        toolData.radius += data.level * 0.5;

        TagUtil.setToolTag(rootCompound, toolData.get());
    }
}
