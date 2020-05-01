package uvmidnight.totaltinkers.explosives;

import com.google.common.collect.ImmutableList;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.client.crosshair.Crosshairs;
import slimeknights.tconstruct.library.client.crosshair.ICrosshair;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.ProjectileLauncherNBT;
import slimeknights.tconstruct.tools.TinkerMaterials;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.ranged.TinkerRangedWeapons;
import slimeknights.tconstruct.tools.ranged.item.ShortBow;

import java.util.List;

public class ExplosiveBow extends ShortBow {

    // little more durability due to the plate
    public static final float DURABILITY_MODIFIER = 1.4f;

    public ExplosiveBow() {
        super(PartMaterialType.bow(TinkerTools.bowLimb),
                PartMaterialType.bow(TinkerTools.bowLimb),
                PartMaterialType.extra(TinkerTools.toughBinding),
                PartMaterialType.bowstring(TinkerTools.bowString));
        addCategory(Category.LAUNCHER, Category.WEAPON);
        setTranslationKey("explosive_bow").setRegistryName("explosive_bow");
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if(this.isInCreativeTab(tab)) {
            addDefaultSubItems(subItems, null, null, null, TinkerMaterials.string);
        }
    }

    /* Tic Tool Stuff */

    @Override
    public double attackSpeed() {
        return 1.3;
    }

    @Override
    public float baseProjectileDamage() {
        return 0f;
    }

    @Override
    protected float baseProjectileSpeed() {
        return 3.5f;
    }

    @Override
    protected float baseInaccuracy() {
        return 1.2f;
    }

    @Override
    public float projectileDamageModifier() {
        return 1;
    }

    @Override
    public int getDrawTime() {
        return 50;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        // no speedup on charging
        onUpdateTraits(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    private ImmutableList<Item> arrowMatches = null;

    @Override
    protected List<Item> getAmmoItems() {
        if(arrowMatches == null) {
            ImmutableList.Builder<Item> builder = ImmutableList.builder();
            if(Explosives.explosiveArrow != null) {
                builder.add(Explosives.explosiveArrow);
            }
            arrowMatches = builder.build();
        }
        return arrowMatches;
    }

    /* Data Stuff */

    @Override
    public ProjectileLauncherNBT buildTagData(List<Material> materials) {
        ProjectileLauncherNBT data = new ProjectileLauncherNBT();
        HeadMaterialStats head1 = materials.get(0).getStatsOrUnknown(MaterialTypes.HEAD);
        HeadMaterialStats head2 = materials.get(1).getStatsOrUnknown(MaterialTypes.HEAD);
        BowMaterialStats limb1 = materials.get(0).getStatsOrUnknown(MaterialTypes.BOW);
        BowMaterialStats limb2 = materials.get(1).getStatsOrUnknown(MaterialTypes.BOW);
        ExtraMaterialStats grip = materials.get(2).getStatsOrUnknown(MaterialTypes.EXTRA);
        BowStringMaterialStats bowstring = materials.get(3).getStatsOrUnknown(MaterialTypes.BOWSTRING);

        data.head(head1, head2);
        data.limb(limb1, limb2);
        data.extra(grip);
        data.bowstring(bowstring);

        data.durability *= DURABILITY_MODIFIER;

        return data;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ICrosshair getCrosshair(ItemStack itemStack, EntityPlayer player) {
        return Crosshairs.INVERSE;
    }
}
