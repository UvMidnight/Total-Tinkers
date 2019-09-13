package uvmidnight.totaltinkers.experimental;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.SwordCore;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.Tags;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.library.utils.TooltipBuilder;
import slimeknights.tconstruct.tools.TinkerTools;
import uvmidnight.totaltinkers.experimental.potion.PotionHemorrhageEffect;

import java.util.ArrayList;
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

        data.durability *= 1.3;

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
                if (((EntityLivingBase) entity).isPotionActive(Experimental.potionHemorrhage)) {
                    amp = ((EntityLivingBase) entity).getActivePotionEffect(Experimental.potionHemorrhage).getAmplifier();
                }

                NBTTagCompound tag = TagUtil.getToolTag(TagUtil.getTagSafe(stack));
                int duration = (int) tag.getFloat(Tags.ATTACK) * 6;

                ((EntityLivingBase) entity).addPotionEffect(new PotionHemorrhageEffect(Experimental.potionHemorrhage, duration, amp + 1));
            }
        }
        return super.dealDamage(stack, player, entity, damage);
    }

    @Override
    public List<String> getInformation(ItemStack stack, boolean detailed) {
        List<String> list = new ArrayList<>();
        TooltipBuilder info = new TooltipBuilder(stack);
        String out;
        NBTTagCompound tag = TagUtil.getToolTag(TagUtil.getTagSafe(stack));

        float bleedduration = (tag.getFloat(Tags.ATTACK) * 6F) / 20F;

        info.addDurability(!detailed);

        out = I18n.format("tinkers.scimitar.bleed.duration", bleedduration);
        info.add(out);

        info.addAttack();

        if (ToolHelper.getFreeModifiers(stack) > 0) {
            info.addFreeModifiers();
        }
        if (detailed) {
            info.addModifierInfo();
        }

        list.addAll(info.getTooltip());

        return list;
    }

    @Override
    public void getTooltip(ItemStack stack, List<String> tooltips) {
        super.getTooltip(stack, tooltips);
        NBTTagCompound tag = TagUtil.getToolTag(TagUtil.getTagSafe(stack));
        float bleedduration = (tag.getFloat(Tags.ATTACK) * 6F) / 20F;
        tooltips.add(I18n.format("tinkers.scimitar.bleed.hover", bleedduration));
    }
}