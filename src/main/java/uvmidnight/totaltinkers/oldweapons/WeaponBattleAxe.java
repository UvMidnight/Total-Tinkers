package uvmidnight.totaltinkers.oldweapons;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.AoeToolCore;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.Tags;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.tools.TinkerTools;
import uvmidnight.totaltinkers.TotalTinkers;

import javax.annotation.Nonnull;
import java.util.List;


//Simplified due to junk code: Deal more damage, take more damage.
public class WeaponBattleAxe extends AoeToolCore {
    public WeaponBattleAxe() {
        super(PartMaterialType.handle(TinkerTools.toughToolRod),
                PartMaterialType.head(TinkerTools.broadAxeHead),
                PartMaterialType.head(TinkerTools.broadAxeHead),
                PartMaterialType.extra(TinkerTools.toughBinding));

        addCategory(Category.WEAPON);

        setHarvestLevel("axe", 0);
        setTranslationKey("battleaxe").setRegistryName("battleaxe");
    }

    @Override
    public ImmutableList<BlockPos> getAOEBlocks(ItemStack stack, World world, EntityPlayer player, BlockPos origin) {
        return ToolHelper.calcAOEBlocks(stack, world, player, origin, 3, 3, 1);
    }

    @Override
    public float damagePotential() {
        return 1.9f;
    }

    @Override
    public float damageCutoff() {
        return 120f;
    }

    @Override
    public double attackSpeed() {
        return 1f;
    }

    @Override
    public int[] getRepairParts() {
        return new int[]{1, 2};
    }

    @Override
    protected ToolNBT buildTagData(List<Material> materials) {
        HandleMaterialStats handle = materials.get(0).getStatsOrUnknown(MaterialTypes.HANDLE);
        HeadMaterialStats head1 = materials.get(1).getStatsOrUnknown(MaterialTypes.HEAD);
        HeadMaterialStats head2 = materials.get(2).getStatsOrUnknown(MaterialTypes.HEAD);
        ExtraMaterialStats binding = materials.get(3).getStatsOrUnknown(MaterialTypes.EXTRA);

        ToolNBT data = new ToolNBT();
        data.head(head1, head2);
        data.extra(binding);
        data.handle(handle);

        data.attack += 1;

        return data;
    }
    @Override
    public void getTooltip(ItemStack stack, List<String> tooltips) {
        super.getTooltip(stack, tooltips);
        tooltips.add(I18n.format("tinkers.battleaxe.extradamage.hover", OldWeapons.battleaxeIncreasedDamage.getDouble()));
    }
}