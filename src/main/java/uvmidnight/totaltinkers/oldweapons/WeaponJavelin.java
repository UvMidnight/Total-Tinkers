package uvmidnight.totaltinkers.oldweapons;


import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.client.crosshair.Crosshairs;
import slimeknights.tconstruct.library.client.crosshair.ICrosshair;
import slimeknights.tconstruct.library.client.crosshair.ICustomCrosshairUser;
import slimeknights.tconstruct.library.entity.EntityProjectileBase;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.materials.MaterialTypes;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.ProjectileLauncherNBT;
import slimeknights.tconstruct.library.tools.ProjectileNBT;
import slimeknights.tconstruct.library.tools.ranged.BowCore;
import slimeknights.tconstruct.library.tools.ranged.ILauncher;
import slimeknights.tconstruct.library.tools.ranged.ProjectileCore;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.ranged.TinkerRangedWeapons;
import uvmidnight.totaltinkers.oldweapons.entity.EntityJavelin;

import javax.annotation.Nonnull;
import java.util.List;

//I loved the javelin, it was the reason for me creating this mod
//But lets be honest, it was total garbage
//
//Alternative config option gives it similarity with the cutlass NYI
public class WeaponJavelin extends ProjectileCore{
    public static final float DURABILITY_MODIFIER = 0.7F;

    private static PartMaterialType rodPMT = new PartMaterialType(TinkerTools.toughToolRod, MaterialTypes.EXTRA, MaterialTypes.PROJECTILE);

    public WeaponJavelin() {
        super(PartMaterialType.handle(TinkerTools.toughToolRod), PartMaterialType.arrowHead(TinkerTools.arrowHead), rodPMT);
        durabilityPerAmmo = 2;
        addCategory(Category.PROJECTILE, Category.WEAPON);
        setTranslationKey("javelin").setRegistryName("javelin");
    }

    @Override
    public ProjectileNBT buildTagData(List<Material> materials) {
        ProjectileNBT data = new ProjectileNBT();
        HeadMaterialStats head = materials.get(1).getStatsOrUnknown(MaterialTypes.HEAD);
        data.head(head);
        data.extra(materials.get(2).getStatsOrUnknown(MaterialTypes.EXTRA));
        data.handle(materials.get(0).getStatsOrUnknown(MaterialTypes.HANDLE));
        data.durability *= DURABILITY_MODIFIER;
        return data;
    }

    //maybe with everything I overrid extending projectilecore was a bad idea
    //TODO: tool consumes stack of stuff instead of durability, which this tool should not be seen to have. Need to override getinformation and some other stuff
    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        return ToolHelper.attackEntity(stack, this, player, entity);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        float speed = ToolHelper.getActualAttackSpeed(stack);
        int time = Math.round(20f / speed);
        if (time < target.hurtResistantTime / 2) {
            target.hurtResistantTime = (target.hurtResistantTime + time) / 2;
            target.hurtTime = (target.hurtTime + time) / 2;
        }
        return super.hitEntity(stack, target, attacker);
    }

    @Nonnull
    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(@Nonnull EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

        if (slot == EntityEquipmentSlot.MAINHAND && !ToolHelper.isBroken(stack)) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", ToolHelper.getActualAttack(stack), 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", ToolHelper.getActualAttackSpeed(stack) - 4d, 0));
        }

        TinkerUtil.getTraitsOrdered(stack).forEach(trait -> trait.getAttributeModifiers(slot, stack, multimap));

        return multimap;
    }


    @Override
    public float damagePotential() {
        return 0.9F;
    }

    @Override
    public double attackSpeed() {
        return 1.5f;
    }

    @Override
    public int[] getRepairParts() {
        return new int[]{1};
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        preventSlowDown(entityIn, 1.0f);

        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Nonnull
    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        if (!OldWeapons.javelinLegacyMode.getBoolean()) {
            return EnumAction.BOW;
        } else {
            return super.getItemUseAction(stack);
        }
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        if (!OldWeapons.javelinLegacyMode.getBoolean()) {
            return 72000;
        } else {
            return super.getMaxItemUseDuration(stack);
        }
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {

        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        if (!OldWeapons.javelinLegacyMode.getBoolean()) {
            if (!ToolHelper.isBroken(itemStackIn)) {
                playerIn.setActiveHand(hand);
                return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
            }
        } else {
            if (ToolHelper.isBroken(itemStackIn)) {
                return ActionResult.newResult(EnumActionResult.FAIL, itemStackIn);
            }
            playerIn.getCooldownTracker().setCooldown(itemStackIn.getItem(), 16);

            if (!worldIn.isRemote) {
                boolean usedAmmo = useAmmo(itemStackIn, playerIn);
                EntityProjectileBase projectile = getProjectile(itemStackIn, itemStackIn, worldIn, playerIn, 2.1f, 0f, 1f, usedAmmo);
                worldIn.spawnEntity(projectile);
            }
            return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
        }

        return new ActionResult<>(EnumActionResult.FAIL, itemStackIn);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (!OldWeapons.javelinLegacyMode.getBoolean()) {
            if(ToolHelper.isBroken(stack) || !(entityLiving instanceof EntityPlayer)) {
                return;
            }
            EntityPlayer player = (EntityPlayer) entityLiving;
            if(stack.isEmpty() && !player.capabilities.isCreativeMode) {
                return;
            }

            int useTime = this.getMaxItemUseDuration(stack) - timeLeft;

            useTime = ForgeEventFactory.onArrowLoose(stack, worldIn, player, useTime, !stack.isEmpty());

            if(useTime < 4) {
                return;
            }
            float progress = Math.min(1f, useTime / (float) 15);
            float power = ItemBow.getArrowVelocity((int)(progress * 20f)) * progress * 1.6f;

            if (!worldIn.isRemote) {
                boolean usedAmmo = !player.capabilities.isCreativeMode && useAmmo(stack, entityLiving);
                EntityProjectileBase projectile = getProjectile(stack, stack, worldIn, player, power + 0.7f, 0f, /*Math.min(progress + 0.5f, 1f)*/ progress * progress, usedAmmo);
                worldIn.spawnEntity(projectile);
            }

            StatBase statBase = StatList.getObjectUseStats(this);
            assert statBase != null;
            player.addStat(statBase);

            TinkerRangedWeapons.proxy.updateEquippedItemForRendering(entityLiving.getActiveHand());
            TagUtil.setResetFlag(stack, true);
        }
        else {
            super.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
        }
    }

    @Override
    public EntityProjectileBase getProjectile(@Nonnull ItemStack stack, @Nonnull ItemStack launcher, World world, EntityPlayer player, float speed, float inaccuracy, float power, boolean usedAmmo) {
        inaccuracy *= ProjectileNBT.from(stack).accuracy;
        return new EntityJavelin(world, player, speed, inaccuracy, getProjectileStack(stack, world, player, usedAmmo), launcher);
    }
}
