package uvmidnight.totaltinkers.oldweapons.singleuse;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.tools.TinkerToolCore;
import slimeknights.tconstruct.library.tools.ranged.IProjectile;
import slimeknights.tconstruct.library.tools.ranged.ProjectileCore;
import slimeknights.tconstruct.tools.traits.TraitEnderference;

public abstract class ThrowableCore extends TinkerToolCore implements IProjectile {
    public static final String DAMAGE_TYPE_PROJECTILE = "arrow";

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    //stuff that subclasses should probably override
    protected float baseInaccuracy() {
        return 0f;
    }

    protected float baseProjectileSpeed() {
        return 3f;
    }

    public int getDrawTime() {
        return 20;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {

    }

    @Override
    public boolean dealDamageRanged(ItemStack stack, Entity projectile, EntityLivingBase player, Entity entity, float damage) {
        DamageSource damageSource = new EntityDamageSourceIndirect(DAMAGE_TYPE_PROJECTILE, projectile, player).setProjectile();

        // friggin vanilla hardcode
        if(entity instanceof EntityEnderman && ((EntityEnderman) entity).getActivePotionEffect(TraitEnderference.Enderference) != null) {
            damageSource = new ProjectileCore.DamageSourceProjectileForEndermen(DAMAGE_TYPE_PROJECTILE, projectile, player);
        }

        return entity.attackEntityFrom(damageSource, damage);
    }
}
