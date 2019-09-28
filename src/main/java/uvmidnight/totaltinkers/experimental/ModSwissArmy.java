package uvmidnight.totaltinkers.experimental;

import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.tools.modifiers.ToolModifier;

public class ModSwissArmy extends ToolModifier {
    public static ModSwissArmy modSwissArmy = new ModSwissArmy();

    public ModSwissArmy() {
        super("swissarmy_halberd", 0xC0C0C0);
        addAspects(new ModifierAspect.LevelAspect(this, 1), new ModifierAspect.DataAspect(this));
    }


    @Override
    public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
        ModifierNBT.IntegerNBT modData = ModifierNBT.readInteger(modifierTag);

    }
}
