package uvmidnight.totaltinkers.explosives;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.entity.EntityProjectileBase;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.materials.MaterialTypes;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.ProjectileNBT;
import slimeknights.tconstruct.library.tools.ranged.ProjectileCore;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.tools.TinkerTools;
import uvmidnight.totaltinkers.explosives.entity.EntityBomb;

import javax.annotation.Nonnull;
import java.util.List;

public class WeaponBomb extends ProjectileCore {
    public static final float DURABILITY_MODIFIER = 0.7f;

    private static PartMaterialType rodPMT = new PartMaterialType(TinkerTools.toughToolRod, MaterialTypes.EXTRA, MaterialTypes.PROJECTILE);

    public WeaponBomb() {
        super(rodPMT, PartMaterialType.arrowHead(TinkerTools.arrowHead), rodPMT);
        durabilityPerAmmo = 50;
        addCategory(Category.PROJECTILE, Category.WEAPON);
        setTranslationKey("bomb").setRegistryName("bomb");
    }

    @Override
    public ProjectileNBT buildTagData(List<Material> materials) {
        ProjectileNBT data = new ProjectileNBT();
        HeadMaterialStats head = materials.get(1).getStatsOrUnknown(MaterialTypes.HEAD);
        data.head(head);
        data.extra(materials.get(0).getStatsOrUnknown(MaterialTypes.EXTRA),
                materials.get(2).getStatsOrUnknown(MaterialTypes.EXTRA));
        data.durability *= DURABILITY_MODIFIER;
        return data;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        if (ToolHelper.isBroken(itemStackIn)) {
            return ActionResult.newResult(EnumActionResult.FAIL, itemStackIn);
        }
        playerIn.getCooldownTracker().setCooldown(itemStackIn.getItem(), 16);

        if (!worldIn.isRemote) {
            boolean usedAmmo = useAmmo(itemStackIn, playerIn);
            EntityProjectileBase projectile = getProjectile(itemStackIn, itemStackIn, worldIn, playerIn, 1.6f, 0f, 1f, usedAmmo);
            worldIn.spawnEntity(projectile);
        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
    }
//    @Nonnull
//    @Override
//    public Multimap<String, AttributeModifier> getAttributeModifiers(@Nonnull EntityEquipmentSlot slot, ItemStack stack) {
//        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
//
//        if (slot == EntityEquipmentSlot.MAINHAND && !ToolHelper.isBroken(stack)) {
//            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", ToolHelper.getActualAttack(stack), 0));
//            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", ToolHelper.getActualAttackSpeed(stack) - 4d, 0));
//        }
//
//        TinkerUtil.getTraitsOrdered(stack).forEach(trait -> trait.getAttributeModifiers(slot, stack, multimap));
//
//        return multimap;
//    }

    @Override
    public float damagePotential() {
        return 0.9F;
    }

//    @Override
//    public int[] getRepairParts() {
//        return new int[]{1};
//    }

    @Override
    public EntityProjectileBase getProjectile(@Nonnull ItemStack stack, @Nonnull ItemStack launcher, World world, EntityPlayer player, float speed, float inaccuracy, float power, boolean usedAmmo) {
        inaccuracy *= ProjectileNBT.from(stack).accuracy;
        double damage = ProjectileNBT.from(stack).attack;
        return new EntityBomb(world, player, speed, inaccuracy, getProjectileStack(stack, world, player, usedAmmo), launcher);
    }
}
