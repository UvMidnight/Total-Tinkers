package uvmidnight.totaltinkers;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.library.tools.Pattern;

import javax.annotation.Nullable;
import java.util.List;

public class ToolTipPattern extends Pattern {
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        Item part = getPartFromTag(stack);
        String unloc = this.getUnlocalizedNameInefficiently(stack);

        if(part != null && part instanceof IToolPart) {
            tooltip.add(Util.translateFormatted(unloc + ".tooltip"/*, part.getItemStackDisplayName(ItemStack.EMPTY)*/));
        }
    }
}
