package uvmidnight.totaltinkers.newweapons;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;
import slimeknights.tconstruct.common.TinkerNetwork;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.SwordCore;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.traits.ITrait;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import slimeknights.tconstruct.tools.TinkerTools;

import java.util.List;

import static slimeknights.tconstruct.library.utils.ToolHelper.calcCutoffDamage;
import static slimeknights.tconstruct.library.utils.ToolHelper.isBroken;

public class WeaponDualDagger extends SwordCore {
    public WeaponDualDagger() {
        super(PartMaterialType.head(TinkerTools.knifeBlade), PartMaterialType.handle(TinkerTools.toolRod));

        this.addCategory(Category.WEAPON);

        setTranslationKey("Dual Daggers").setRegistryName("Dual Daggers");
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        if (player.getHeldItemOffhand().getItem() instanceof WeaponDualDagger) {
//      public static boolean attackEntity(ItemStack stack, ToolCore tool, EntityLivingBase attacker, Entity targetEntity, Entity projectileEntity, boolean applyCooldown) {
            // nothing to do, no target?
            if (entity == null || !entity.canBeAttackedWithItem() || entity.hitByEntity(player) || !stack.hasTagCompound()) {
                return false;
            }
            if (isBroken(stack)) {
                return false;
            }
            if (entity == null) {
                return false;
            }
//        boolean isProjectile = projectileEntity != null;
            EntityLivingBase target = null;
            if (entity instanceof EntityLivingBase) {
                target = (EntityLivingBase) entity;
            }
            if (target instanceof EntityPlayer) {
                if (!player.canAttackPlayer((EntityPlayer) target)) {
                    return false;
                }
            }

            ItemStack offHandDagger = player.getHeldItemOffhand();

            // traits on the tool
            List<ITrait> traits = TinkerUtil.getTraitsOrdered(stack);
            List<ITrait> offHandTraits = TinkerUtil.getTraitsOrdered(offHandDagger);

            // players base damage (includes tools damage stat)
            float baseDamage = (float) player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();

//        baseDamage += offHandDagger.getAttributeModifiers(EntityEquipmentSlot.OFFHAND).
            // missing because not supported by tcon tools: vanilla damage enchantments, we have our own modifiers
            // missing because not supported by tcon tools: vanilla knockback enchantments, we have our own modifiers
            float baseKnockback = player.isSprinting() ? 1 : 0;

            // calculate if it's a critical hit
            boolean isCritical = player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater() && !player.isPotionActive(MobEffects.BLINDNESS) && !player.isRiding();
            for (ITrait trait : traits) {
                if (trait.isCriticalHit(stack, player, target)) {
                    isCritical = true;
                }
            }

            // calculate actual damage
            float damage = baseDamage;
            if (target != null) {
                for (ITrait trait : traits) {
                    damage = trait.damage(stack, player, target, baseDamage, damage, isCritical);
                }
            }

            // apply critical damage
            if (isCritical) {
                damage *= 1.5f;
            }

            // calculate cutoff
            damage = calcCutoffDamage(damage, this.damageCutoff());

            // calculate actual knockback
            float knockback = baseKnockback;
            if (target != null) {
                for (ITrait trait : traits) {
                    knockback = trait.knockBack(stack, player, target, damage, baseKnockback, knockback, isCritical);
                }
            }

            // missing because not supported by tcon tools: vanilla fire aspect enchantments, we have our own modifiers

            float oldHP = 0;

            double oldVelX = entity.motionX;
            double oldVelY = entity.motionY;
            double oldVelZ = entity.motionZ;

            if (target != null) {
                oldHP = target.getHealth();
            }

            // apply cooldown damage decrease
            if (player != null) {
                float cooldown = player.getCooledAttackStrength(0.5F);
                damage *= (0.2F + cooldown * cooldown * 0.8F);
            }

            // deal the damage
            if (target != null) {
                int hurtResistantTime = target.hurtResistantTime;
                for (ITrait trait : traits) {
                    trait.onHit(stack, player, target, damage, isCritical);
                    // reset hurt reristant time
                    target.hurtResistantTime = hurtResistantTime;
                }
            }

            boolean hit = false;
//        if(isProjectile && tool instanceof IProjectile) {
//          hit = ((IProjectile) tool).dealDamageRanged(stack, projectileEntity, attacker, targetEntity, damage);
//        }
//        else {
            hit = this.dealDamage(stack, player, entity, damage);
//        }

            // did we hit?
            if (hit && target != null) {
                // actual damage dealt
                float damageDealt = oldHP - target.getHealth();

                // apply knockback modifier
                oldVelX = target.motionX = oldVelX + (target.motionX - oldVelX) * this.knockback();
                oldVelY = target.motionY = oldVelY + (target.motionY - oldVelY) * this.knockback() / 3f;
                oldVelZ = target.motionZ = oldVelZ + (target.motionZ - oldVelZ) * this.knockback();

                // apply knockback
                if (knockback > 0f) {
                    double velX = -MathHelper.sin(player.rotationYaw * (float) Math.PI / 180.0F) * knockback * 0.5F;
                    double velZ = MathHelper.cos(player.rotationYaw * (float) Math.PI / 180.0F) * knockback * 0.5F;
                    player.addVelocity(velX, 0.1d, velZ);

                    // slow down player
                    player.motionX *= 0.6f;
                    player.motionZ *= 0.6f;
                    player.setSprinting(false);
                }

                // Send movement changes caused by attacking directly to hit players.
                // I guess this is to allow better handling at the hit players side? No idea why it resets the motion though.
                if (entity instanceof EntityPlayerMP && entity.velocityChanged) {
                    TinkerNetwork.sendPacket(player, new SPacketEntityVelocity(entity));
                    entity.velocityChanged = false;
                    entity.motionX = oldVelX;
                    entity.motionY = oldVelY;
                    entity.motionZ = oldVelZ;
                }

                if (player != null) {
                    // vanilla critical callback
                    if (isCritical) {
                        player.onCriticalHit(target);
                    }

                    // "magical" critical damage? (aka caused by modifiers)
                    if (damage > baseDamage) {
                        // this usually only displays some particles :)
                        player.onEnchantmentCritical(entity);
                    }

                    // vanilla achievement support :D
                    // TODO: READD
                    //if(damage >= 18f) {
                    //  player.addStat(AchievementList.OVERKILL);
                    //}
                }

                player.setLastAttackedEntity(target);
                // Damage indicator particles

                // we don't support vanilla thorns or antispider enchantments
                //EnchantmentHelper.applyThornEnchantments(target, player);
                //EnchantmentHelper.applyArthropodEnchantments(player, target);

                // call post-hit callbacks before reducing the durability
                for (ITrait trait : traits) {
                    trait.afterHit(stack, player, target, damageDealt, isCritical, true); // hit is always true
                }

                // damage the tool
                if (player != null) {
                    stack.hitEntity(target, player);
                    if (!player.capabilities.isCreativeMode) {
                        this.reduceDurabilityOnHit(stack, player, damage);
                    }

                    player.addStat(StatList.DAMAGE_DEALT, Math.round(damageDealt * 10f));
                    player.addExhaustion(0.3f);

                    if (player.getEntityWorld() instanceof WorldServer && damageDealt > 2f) {
                        int k = (int) (damageDealt * 0.5);
                        ((WorldServer) player.getEntityWorld()).spawnParticle(EnumParticleTypes.DAMAGE_INDICATOR, entity.posX, entity.posY + entity.height * 0.5F, entity.posZ, k, 0.1D, 0.0D, 0.1D, 0.2D);
                    }

                    player.resetCooldown();
                } else {
                    this.reduceDurabilityOnHit(stack, null, damage);
                }
            }
            return true;
        } else {
            return super.onLeftClickEntity(stack, player, entity);
        }
    }

    @Override
    protected ToolNBT buildTagData(List<Material> materials) {
        return null;
    }

    @Override
    public float damagePotential() {
        return 0.4F;
    }

    @Override
    public double attackSpeed() {
        return 1.8F;
    }
}
