package uvmidnight.totaltinkers.newweapons;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
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
import uvmidnight.totaltinkers.newweapons.potion.PotionHemorrhageEffect;

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

        data.durability *= 1.4;
        data.attack += 1;

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

    @SuppressWarnings("unchecked")
    @Override
    public boolean dealDamage(ItemStack stack, EntityLivingBase player, Entity entity, float damage){
        boolean attack =  super.dealDamage(stack, player, entity, damage);

        if (readyForSpecialAttack(player) && attack && entity instanceof EntityLivingBase && (entity.isNonBoss() || NewWeapons.canScimitarBleedDamageBosses.getBoolean())) {
            boolean isBlocking = false;

            DamageSource damageSource;

            if(player instanceof EntityPlayer) {
                damageSource =  DamageSource.causePlayerDamage((EntityPlayer) player);
            } else {
                damageSource =  DamageSource.causeMobDamage(player);
            }

            if (!damageSource.isUnblockable() && ((EntityLivingBase) entity).isActiveItemStackBlocking()) {
                Vec3d vec3d = damageSource.getDamageLocation();

                if (vec3d != null)
                {
                    Vec3d vec3d1 = entity.getLook(1.0F);
                    Vec3d vec3d2 = vec3d.subtractReverse(new Vec3d(entity.posX, entity.posY, entity.posZ)).normalize();
                    vec3d2 = new Vec3d(vec3d2.x, 0.0D, vec3d2.z);

                    if (vec3d2.dotProduct(vec3d1) < 0.0D)
                    {
                        isBlocking = true;
                    }
                }
            }
            if (!isBlocking) {
                int amp = 0;
                int oldDuration = 0;
                if (((EntityLivingBase) entity).isPotionActive(NewWeapons.potionHemorrhage)) {
                    amp = ((EntityLivingBase) entity).getActivePotionEffect(NewWeapons.potionHemorrhage).getAmplifier();
                    oldDuration = ((EntityLivingBase) entity).getActivePotionEffect(NewWeapons.potionHemorrhage).getDuration();
                }
                NBTTagCompound tag = TagUtil.getToolTag(TagUtil.getTagSafe(stack));
                int duration = Math.max((int) (tag.getFloat(Tags.ATTACK) * 8F), 0);

                if (NewWeapons.scimitarBleedDurationRefreshable.getBoolean() || !((EntityLivingBase) entity).isPotionActive(NewWeapons.potionHemorrhage)){
                    if (NewWeapons.scimitarDamageStackable.getBoolean()) {
                        ((EntityLivingBase) entity).addPotionEffect(new PotionHemorrhageEffect(NewWeapons.potionHemorrhage, duration, amp + 1));
                    } else {
                        ((EntityLivingBase) entity).addPotionEffect(new PotionHemorrhageEffect(NewWeapons.potionHemorrhage, duration, amp));
                    }
                } else {
                    if (NewWeapons.scimitarDamageStackable.getBoolean()) {
                        ((EntityLivingBase) entity).addPotionEffect(new PotionHemorrhageEffect(NewWeapons.potionHemorrhage, oldDuration, amp + 1));
                    }
                }
            }
        }
        return attack;
    }

    @Override
    public List<String> getInformation(ItemStack stack, boolean detailed) {
        List<String> list = new ArrayList<>();
        TooltipBuilder info = new TooltipBuilder(stack);
        String out;
        NBTTagCompound tag = TagUtil.getToolTag(TagUtil.getTagSafe(stack));

        float bleedduration = Math.max((tag.getFloat(Tags.ATTACK) * 8F) / 20F, 0);

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
        float bleedduration = Math.max((tag.getFloat(Tags.ATTACK) * 8F - 12F) / 20F, 0);
        tooltips.add(I18n.format("tinkers.scimitar.bleed.hover", bleedduration));
    }
}