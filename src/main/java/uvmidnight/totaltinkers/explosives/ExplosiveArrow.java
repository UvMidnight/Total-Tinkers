package uvmidnight.totaltinkers.explosives;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentKeybind;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.entity.EntityProjectileBase;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.ProjectileNBT;
import slimeknights.tconstruct.library.tools.ranged.ProjectileCore;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.library.utils.TooltipBuilder;
import slimeknights.tconstruct.tools.TinkerMaterials;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.common.entity.EntityArrow;
import uvmidnight.totaltinkers.explosives.entity.EntityExplosiveArrow;
import uvmidnight.totaltinkers.explosives.materials.ExplosiveMaterialStats;
import uvmidnight.totaltinkers.explosives.materials.ExplosivePartType;
import uvmidnight.totaltinkers.explosives.materials.ExplosiveProjectileNBT;

import javax.annotation.Nullable;
import java.util.List;

public class ExplosiveArrow extends ProjectileCore {
    private static PartMaterialType pmt = new PartMaterialType(Explosives.explosiveCore, ExplosivePartType.EXPLOSIVE_CORE);

    public ExplosiveArrow() {
        super(PartMaterialType.arrowShaft(TinkerTools.arrowShaft),
                pmt,
                PartMaterialType.fletching(TinkerTools.fletching));

        addCategory(Category.NO_MELEE, Category.PROJECTILE);
        setTranslationKey("explosive_arrow").setRegistryName("explosive_arrow");
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if(this.isInCreativeTab(tab)) {
            addDefaultSubItems(subItems, TinkerMaterials.wood, null, TinkerMaterials.feather);
        }
    }

    @Override
    public float damagePotential() {
        return 1f;
    }

    @Override
    public double attackSpeed() {
        return 1;
    }

    @Override
    public ProjectileNBT buildTagData(List<Material> materials) {
        ExplosiveProjectileNBT data = new ExplosiveProjectileNBT();

        ArrowShaftMaterialStats shaft = materials.get(0).getStatsOrUnknown(MaterialTypes.SHAFT);
        ExplosiveMaterialStats head = materials.get(1).getStatsOrUnknown(ExplosivePartType.EXPLOSIVE_CORE);
        FletchingMaterialStats fletching = materials.get(2).getStatsOrUnknown(MaterialTypes.FLETCHING);

        data.explosiveCores(head);
        data.fletchings(fletching);
        data.shafts(this, shaft);

//        data.attack += 2;

        return data;
    }

    @Override
    public EntityProjectileBase getProjectile(ItemStack stack, ItemStack bow, World world, EntityPlayer player, float speed, float inaccuracy, float power, boolean usedAmmo) {
        inaccuracy -= (1f - 1f / ProjectileNBT.from(stack).accuracy) * speed / 2f;
        float radius = ExplosiveProjectileNBT.from(stack).radius;
        return new EntityExplosiveArrow(world, player, speed, inaccuracy, power, getProjectileStack(stack, world, player, usedAmmo), bow, radius);
    }

    @Override
    public List<String> getInformation(ItemStack stack, boolean detailed)
    {
        TooltipBuilder info = new TooltipBuilder(stack);

        ExplosiveProjectileNBT nbt = ExplosiveProjectileNBT.from(stack);
        info.addDurability(!detailed);
        info.addAttack();
        info.add(ExplosiveMaterialStats.formatRadius(nbt.radius));

//        info.add(String.format("%s: %s%s %s", Util.translate(ExplosiveProjectileNBT.LOC_RADIUS), ExplosiveMaterialStats.COLOR_RADIUS, Util.df.format(nbt.radius), Util.translate(ExplosiveProjectileNBT.LOC_Suffix)) + TextFormatting.RESET);

        if (ToolHelper.getFreeModifiers(stack) > 0)
            info.addFreeModifiers();

        if (detailed)
            info.addModifierInfo();

        return info.getTooltip();
    }
}
