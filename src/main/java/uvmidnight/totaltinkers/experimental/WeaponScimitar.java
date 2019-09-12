package uvmidnight.totaltinkers.experimental;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.SwordCore;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.Tags;
import slimeknights.tconstruct.tools.TinkerTools;
import uvmidnight.totaltinkers.experimental.potion.PotionHemorrhageEffect;
import uvmidnight.totaltinkers.newweapons.NewWeapons;

import java.util.List;

public class WeaponScimitar extends SwordCore {
    public WeaponScimitar() {
        super(PartMaterialType.handle(TinkerTools.toughToolRod),
                PartMaterialType.head(TinkerTools.swordBlade),
                PartMaterialType.extra(TinkerTools.binding));


        this.addCategory(Category.WEAPON);

        setTranslationKey("scimitar").setRegistryName("scimitar");

    }


    @Override
    protected ToolNBT buildTagData(List<Material> materials) {
        HandleMaterialStats handle = materials.get(0).getStatsOrUnknown(MaterialTypes.HANDLE);
        HeadMaterialStats head0 = materials.get(1).getStatsOrUnknown(MaterialTypes.HEAD);
        ExtraMaterialStats binding = materials.get(2).getStatsOrUnknown(MaterialTypes.EXTRA);

        ToolNBT data = new ToolNBT();
        data.head(head0);
        data.extra(binding);
        data.handle(handle);

        return data;
    }

    @Override
    public float damagePotential() {
        return 0.45F;
    }

    @Override
    public double attackSpeed() {
        return 1.7;
    }

    @Override
    public boolean dealDamage(ItemStack stack, EntityLivingBase player, Entity entity, float damage) {
        if (readyForSpecialAttack(player)) {
            if (entity instanceof EntityLivingBase) {
                int amp = 0;
                if (((EntityLivingBase) entity).isPotionActive(Experimental.potionHemorrhage)){
                    amp = ((EntityLivingBase) entity).getActivePotionEffect(Experimental.potionHemorrhage).getAmplifier();
                }

                NBTTagCompound tag = TagUtil.getToolTag(TagUtil.getTagSafe(stack));
                int duration = (int) tag.getFloat(Tags.ATTACK) * 6;

                ((EntityLivingBase) entity).addPotionEffect(new PotionHemorrhageEffect(Experimental.potionHemorrhage, duration, amp + 1));
            }
        }
        return super.dealDamage(stack, player, entity, damage);
    }
}