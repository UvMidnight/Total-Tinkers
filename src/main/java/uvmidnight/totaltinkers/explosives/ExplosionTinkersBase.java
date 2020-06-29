package uvmidnight.totaltinkers.explosives;

import com.google.common.collect.ImmutableSet;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.capability.projectile.TinkerProjectileHandler;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.tools.ranged.ILauncher;
import slimeknights.tconstruct.library.tools.ranged.IProjectile;
import slimeknights.tconstruct.library.traits.IProjectileTrait;
import slimeknights.tconstruct.library.utils.AmmoHelper;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.Tags;
import slimeknights.tconstruct.library.utils.ToolHelper;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

//terrain damage-less explosion. Attacks by the explosion essentially do melee strikes, much in the same way that projectiles do.
public class ExplosionTinkersBase{
    protected static final UUID PROJECTILE_POWER_MODIFIER = UUID.fromString("c6aefc21-081a-4c4a-b076-8f9d6cef9122");

    private final Random random;
    private final World world;
    private final double x;
    private final double y;
    private final double z;
    private TinkerProjectileHandler tinkerProjectile;
    private EntityLivingBase attacker;
    //the entity that causes the explosion and thus should be ignored by it.
    private final Entity explosionSource;
    private final float size;

    /**
     * A list of ChunkPositions of blocks affected by this explosion
     */
    private final List<BlockPos> affectedBlockPositions;
    /**
     * Maps players to the knockback vector applied by the explosion, to send to the client
     */
    private final Map<EntityPlayer, Vec3d> playerKnockbackMap;
    private final Vec3d position;

    @SideOnly(Side.CLIENT)
    public ExplosionTinkersBase(World worldIn, Entity entityIn, double x, double y, double z, float size, List<BlockPos> affectedPositions) {
        this(worldIn, entityIn, x, y, z, size, new TinkerProjectileHandler(), null);
        this.affectedBlockPositions.addAll(affectedPositions);
    }

    public ExplosionTinkersBase(World worldIn, Entity entityIn, double x, double y, double z, float size, TinkerProjectileHandler tinkerProjectile, EntityLivingBase attacker) {
        this.random = new Random();
        this.affectedBlockPositions = Lists.<BlockPos>newArrayList();
        this.playerKnockbackMap = Maps.<EntityPlayer, Vec3d>newHashMap();
        this.world = worldIn;
        this.explosionSource = entityIn;
        this.size = size;
        this.x = x;
        this.y = y;
        this.z = z;
        this.tinkerProjectile = tinkerProjectile;
        this.position = new Vec3d(this.x, this.y, this.z);
        this.attacker = attacker;
    }

    /**
     * Does the first part of the explosion (damage and move players)
     */
    public void doExplosionA() {
        float f3 = this.size;
        int k1 = MathHelper.floor(this.x - (double) f3 - 2.0F);
        int l1 = MathHelper.floor(this.x + (double) f3 + 2.0F);
        int i2 = MathHelper.floor(this.y - (double) f3 - 2.0F);
        int i1 = MathHelper.floor(this.y + (double) f3 + 2.0F);
        int j2 = MathHelper.floor(this.z - (double) f3 - 2.0F);
        int j1 = MathHelper.floor(this.z + (double) f3 + 2.0F);

        f3 *= 2.0f;

        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this.explosionSource, new AxisAlignedBB((double) k1, (double) i2, (double) j2, (double) l1, (double) i1, (double) j1));
        Vec3d vec3d = new Vec3d(this.x, this.y, this.z);

        for (int k2 = 0; k2 < list.size(); ++k2) {
            Entity entity = list.get(k2);

            if (!entity.isImmuneToExplosions()) {
                attackEntity(entity);
                double d12 = entity.getDistance(this.x, this.y, this.z); /// (double) f3;

                if (d12 <= f3*1.5) {
                    double d5 = entity.posX - this.x;
                    double d7 = entity.posY + (double) entity.getEyeHeight() - this.y;
                    double d9 = entity.posZ - this.z;
                    double d13 = (double) MathHelper.sqrt(d5 * d5 + d7 * d7 + d9 * d9);

                    if (d13 != 0.0D) {
                        d5 = d5 / d13;
                        d7 = d7 / d13;
                        d9 = d9 / d13;
                        double d14 = (double) this.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
                        double d10 = (1.0D - (d12/f3)) * d14;
                        //TODO fix
//                        attackEntity(entity);
//                        entity.attackEntityFrom(DamageSource.GENERIC, 10);
//                        entity.attackEntityFrom(DamageSource.GENERIC, (float) ((int) ((d10 * d10 + d10) / 2.0D * 7.0D * (double) f3 + 1.0D)));
                        double d11 = d10;

                        if (entity instanceof EntityLivingBase) {
                            d11 = EnchantmentProtection.getBlastDamageReduction((EntityLivingBase) entity, d10);
                        }

                        entity.motionX += d5 * d11;
                        entity.motionY += d7 * d11;
                        entity.motionZ += d9 * d11;

                        if (entity instanceof EntityPlayer) {
                            EntityPlayer entityplayer = (EntityPlayer) entity;

                            if (!entityplayer.isSpectator() && (!entityplayer.isCreative() || !entityplayer.capabilities.isFlying)) {
                                this.playerKnockbackMap.put(entityplayer, new Vec3d(d5 * d10, d7 * d10, d9 * d10));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Does the second part of the explosion (sound, particles, drop spawn)
     */
    public void doExplosionB(boolean spawnParticles) {
        this.world.playSound((EntityPlayer) null, this.x, this.y, this.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);

        if (world instanceof WorldServer) {
            if (this.size >= 2.0F) {
                ((WorldServer) world).spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, true, this.x, this.y, this.z, 2, 0, 0, 0, 0d);
            } else {
                ((WorldServer) world).spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, true, this.x, this.y, this.z, 2, 0, 0, 0, 0d);
            }
        }

    }

    public void clearAffectedBlockPositions() {
        this.affectedBlockPositions.clear();
    }

    public List<BlockPos> getAffectedBlockPositions() {
        return this.affectedBlockPositions;
    }

    public Vec3d getPosition() {
        return this.position;
    }

    //horrible code do not touch
    public void attackEntity(Entity entityToHit) {
        ItemStack item = tinkerProjectile.getItemStack();
        ItemStack launcher = tinkerProjectile.getLaunchingStack();

        // deal damage if we have everything
        if(item.getItem() instanceof ToolCore && this.attacker != null) {
            EntityLivingBase attacker = (EntityLivingBase) this.attacker;
            //EntityLivingBase target = (EntityLivingBase) raytraceResult.entityHit;

            // find the actual itemstack in the players inventory
            ItemStack inventoryItem = AmmoHelper.getMatchingItemstackFromInventory(tinkerProjectile.getItemStack(), attacker, false);
            if(inventoryItem.isEmpty() || inventoryItem.getItem() != item.getItem()) {
                // backup, use saved itemstack
                inventoryItem = item;
            }

            // for the sake of dealing damage we always ensure that the impact itemstack has the correct broken state
            // since the ammo stack can break while the arrow travels/if it's the last arrow
            boolean brokenStateDiffers = ToolHelper.isBroken(inventoryItem) != ToolHelper.isBroken(item);
            if(brokenStateDiffers) {
                toggleBroken(inventoryItem);
            }

            Multimap<String, AttributeModifier> projectileAttributes = null;
            // remove stats from held items
            if(!world.isRemote) {
                unequip(attacker, EntityEquipmentSlot.OFFHAND);
                unequip(attacker, EntityEquipmentSlot.MAINHAND);

                // apply stats from projectile
                if(item.getItem() instanceof IProjectile) {
                    projectileAttributes = ((IProjectile) item.getItem()).getProjectileAttributeModifier(inventoryItem);

                    if(launcher.getItem() instanceof ILauncher) {
                        ((ILauncher) launcher.getItem()).modifyProjectileAttributes(projectileAttributes, tinkerProjectile.getLaunchingStack(), tinkerProjectile.getItemStack(), tinkerProjectile.getPower());
                    }

                    // factor in power
                    projectileAttributes.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
                            new AttributeModifier(PROJECTILE_POWER_MODIFIER, "Weapon damage multiplier", tinkerProjectile.getPower() - 1f, 2));

                    attacker.getAttributeMap().applyAttributeModifiers(projectileAttributes);
                }
                // deal the damage
//                ToolHelper.attackEntity(item, (ToolCore) item.getItem(), attacker, entityToHit, null);
//                float speed = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);

                boolean attack = ToolHelper.attackEntity(item, (ToolCore) item.getItem(), attacker, entityToHit, null);
//                if(attack) {
////                    for(IProjectileTrait trait : tinkerProjectile.getProjectileTraits()) {
////                        trait.afterHit(tinkerProjectile.get, this.world, inventoryItem, attacker, entityToHit, (double) 0.0);
////                    }
//
//                    // if on fire, set the entity on fire, like vanilla arrows
////                    if (this.isBurning() && !(entityToHit instanceof EntityEnderman)) {
////                        entityToHit.setFire(5);
////                    }
//                }
                if(brokenStateDiffers) {
                    toggleBroken(inventoryItem);
                }

                // remove stats from projectile
                // apply stats from projectile
                if(item.getItem() instanceof IProjectile) {
                    assert projectileAttributes != null;
                    attacker.getAttributeMap().removeAttributeModifiers(projectileAttributes);
                }

                // readd stats from held items
                equip(attacker, EntityEquipmentSlot.MAINHAND);
                equip(attacker, EntityEquipmentSlot.OFFHAND);
            }
        }else {
            System.out.println("something failed with attacking.");
        }
    }
    private void unequip(EntityLivingBase entity, EntityEquipmentSlot slot) {
        ItemStack stack = entity.getItemStackFromSlot(slot);
        if(!stack.isEmpty()) {
            entity.getAttributeMap().removeAttributeModifiers(stack.getAttributeModifiers(slot));
        }
    }

    private void equip(EntityLivingBase entity, EntityEquipmentSlot slot) {
        ItemStack stack = entity.getItemStackFromSlot(slot);
        if(!stack.isEmpty()) {
            entity.getAttributeMap().applyAttributeModifiers(stack.getAttributeModifiers(slot));
        }
    }
    private void toggleBroken(ItemStack stack) {
        NBTTagCompound tag = TagUtil.getToolTag(stack);
        tag.setBoolean(Tags.BROKEN, !tag.getBoolean(Tags.BROKEN));
        TagUtil.setToolTag(stack, tag);
    }


}