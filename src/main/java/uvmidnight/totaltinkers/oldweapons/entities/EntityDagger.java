package uvmidnight.totaltinkers.oldweapons.entities;

import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.entity.EntityProjectileBase;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.tools.ranged.ILauncher;
import slimeknights.tconstruct.library.tools.ranged.IProjectile;
import slimeknights.tconstruct.library.traits.IProjectileTrait;
import slimeknights.tconstruct.library.utils.AmmoHelper;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.Tags;
import slimeknights.tconstruct.library.utils.ToolHelper;

public class EntityDagger extends EntityProjectileBase {

    private int ticksInAir;

    public EntityDagger(World world) {
        super(world);
    }

    public EntityDagger(World world, double d, double d1, double d2) {
        super(world, d, d1, d2);
    }

    public EntityDagger(World world, EntityPlayer player, float speed, float inaccuracy, ItemStack stack, ItemStack launchingStack) {
        super(world, player, speed, inaccuracy, 1f, stack, launchingStack);
    }

    @Override
    public void onHitEntity(RayTraceResult raytraceResult) {
        ItemStack item = tinkerProjectile.getItemStack();
        ItemStack launcher = tinkerProjectile.getLaunchingStack();
        boolean bounceOff = false;
        Entity entityHit = raytraceResult.entityHit;
        // deal damage if we have everything
        if (item.getItem() instanceof ToolCore && this.shootingEntity instanceof EntityLivingBase) {
            EntityLivingBase attacker = (EntityLivingBase) this.shootingEntity;
            //EntityLivingBase target = (EntityLivingBase) raytraceResult.entityHit;

            // find the actual itemstack in the players inventory
            ItemStack inventoryItem = AmmoHelper.getMatchingItemstackFromInventory(tinkerProjectile.getItemStack(), attacker, false);
            if (inventoryItem.isEmpty() || inventoryItem.getItem() != item.getItem()) {
                // backup, use saved itemstack
                inventoryItem = item;
            }

            // for the sake of dealing damage we always ensure that the impact itemstack has the correct broken state
            // since the ammo stack can break while the arrow travels/if it's the last arrow
            boolean brokenStateDiffers = ToolHelper.isBroken(inventoryItem) != ToolHelper.isBroken(item);
            if (brokenStateDiffers) {
                toggleBroken(inventoryItem);
            }

            Multimap<String, AttributeModifier> projectileAttributes = null;
            // remove stats from held items
            if (!getEntityWorld().isRemote) {
                unequip(attacker, EntityEquipmentSlot.OFFHAND);
                unequip(attacker, EntityEquipmentSlot.MAINHAND);

                // apply stats from projectile
                if (item.getItem() instanceof IProjectile) {
                    projectileAttributes = ((IProjectile) item.getItem()).getProjectileAttributeModifier(inventoryItem);

                    if (launcher.getItem() instanceof ILauncher) {
                        ((ILauncher) launcher.getItem()).modifyProjectileAttributes(projectileAttributes, tinkerProjectile.getLaunchingStack(), tinkerProjectile.getItemStack(), tinkerProjectile.getPower());
                    }

                    // factor in power
                    projectileAttributes.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
                            new AttributeModifier(PROJECTILE_POWER_MODIFIER, "Weapon damage multiplier", tinkerProjectile.getPower() - 1f, 2));

                    attacker.getAttributeMap().applyAttributeModifiers(projectileAttributes);
                }

                // deal the damage
                float speed = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                bounceOff = !dealDamage(speed, inventoryItem, attacker, entityHit);
                if (!bounceOff) {
                    for (IProjectileTrait trait : tinkerProjectile.getProjectileTraits()) {
                        trait.afterHit(this, getEntityWorld(), inventoryItem, attacker, entityHit, speed);
                    }
                }
                if (brokenStateDiffers) {
                    toggleBroken(inventoryItem);
                }

                // remove stats from projectile
                // apply stats from projectile
                if (item.getItem() instanceof IProjectile) {
                    assert projectileAttributes != null;
                    attacker.getAttributeMap().removeAttributeModifiers(projectileAttributes);
                }

                // readd stats from held items
                equip(attacker, EntityEquipmentSlot.MAINHAND);
                equip(attacker, EntityEquipmentSlot.OFFHAND);
            }

            if (!bounceOff) {
                onEntityHit(entityHit);
            }
        }

        if (bounceOff) {
            if (!bounceOnNoDamage) {
                this.setDead();
            }

            // bounce off if we didn't deal damage
            this.motionX *= -0.10000000149011612D;
            this.motionY *= -0.10000000149011612D;
            this.motionZ *= -0.10000000149011612D;
            this.rotationYaw += 180.0F;
            this.prevRotationYaw += 180.0F;
            this.ticksInAir = 0;
        }
        playHitEntitySound();
    }

    private void toggleBroken(ItemStack stack) {
        NBTTagCompound tag = TagUtil.getToolTag(stack);
        tag.setBoolean(Tags.BROKEN, !tag.getBoolean(Tags.BROKEN));
        TagUtil.setToolTag(stack, tag);
    }

    private void unequip(EntityLivingBase entity, EntityEquipmentSlot slot) {
        ItemStack stack = entity.getItemStackFromSlot(slot);
        if (!stack.isEmpty()) {
            entity.getAttributeMap().removeAttributeModifiers(stack.getAttributeModifiers(slot));
        }
    }

    private void equip(EntityLivingBase entity, EntityEquipmentSlot slot) {
        ItemStack stack = entity.getItemStackFromSlot(slot);
        if (!stack.isEmpty()) {
            entity.getAttributeMap().applyAttributeModifiers(stack.getAttributeModifiers(slot));
        }
    }
}
