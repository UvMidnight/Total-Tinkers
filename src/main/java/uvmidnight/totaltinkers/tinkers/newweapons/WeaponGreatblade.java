package uvmidnight.totaltinkers.tinkers.newweapons;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
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
import uvmidnight.totaltinkers.tinkers.TotalTinkersItems;
import uvmidnight.totaltinkers.util.Config;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

//The greatblade has halved base damage, but also does percentage hp damage equal to that.
//This weapon is very powerful, yes. Against unarmored bosses, they will be obliterated.
//But why would you make your 1000 hp boss have no armor?
public class WeaponGreatblade extends SwordCore{

  @Override
  public float damagePotential() {
    return 0.75F;
  }

  @Override
  public double attackSpeed() {
    return 0.9D;
  }
  @Override
  public float damageCutoff() {
    return 500F; //don't ask me wtf you are fighting that needs such a high cutoff
  }

  public WeaponGreatblade() {
    super(PartMaterialType.handle(TinkerTools.toughToolRod),
            PartMaterialType.head(TinkerTools.largeSwordBlade),
            PartMaterialType.head(TinkerTools.largeSwordBlade),
            PartMaterialType.extra(TotalTinkersItems.greatbladeCore));

    this.addCategory(Category.WEAPON);

    setTranslationKey("greatblade").setRegistryName("greatblade");
  }

  // no offhand for you
  @Nonnull
  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
    ItemStack itemStackIn = playerIn.getHeldItem(hand);
    return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
  }
  //TODO enable usage of percent current or missing hp
  @Override
  public boolean dealDamage(ItemStack stack, EntityLivingBase player, Entity entity, float damage) {
    NBTTagCompound tag = TagUtil.getToolTag(TagUtil.getTagSafe(stack));
    float percentHp = tag.getFloat(Tags.ATTACK);

    float percentDmg = 0;

    //apparently how the attack damage due to combat update works
    float cooledModifier = 0.0F;
    if (player instanceof EntityPlayer) {//no idea how this interacts with fake players nor do I care
      float f = ((EntityPlayer) player).getCooledAttackStrength(0.5F);
      cooledModifier = (0.2F + f * f * 0.8F);
    }

    if (entity instanceof EntityLivingBase) {
      EntityLivingBase e = (EntityLivingBase) entity;
      boolean isBoss = !e.isNonBoss();
      percentDmg = (isBoss ? (Config.greatbladeBossMultiplier * e.getMaxHealth() * percentHp / 100.0F) : (e.getMaxHealth() * percentHp / 100.0F));
    } else if (entity instanceof MultiPartEntityPart) {
      MultiPartEntityPart e = (MultiPartEntityPart) entity;
      if (e.parent instanceof EntityDragon) {// Must be DamageSource.Player for dragon
        percentDmg = Config.greatbladeBossMultiplier * ((EntityDragon) e.parent).getMaxHealth() * percentHp / 100.0F;
      } else if (e.parent != null && e.parent instanceof EntityLivingBase) {
        EntityLivingBase living = (EntityLivingBase) e.parent;
        boolean isBoss = !e.isNonBoss();
        percentDmg =  (isBoss ? (Config.greatbladeBossMultiplier * living.getMaxHealth() * percentHp / 100.0F) : (living.getMaxHealth() * percentHp / 100.0F));
      }
    }
    return super.dealDamage(stack, player, entity, damage + percentDmg * cooledModifier);
  }

  @Override
  public List<String> getInformation(ItemStack stack, boolean detailed) {
    List<String> list = new ArrayList<>();
    TooltipBuilder info = new TooltipBuilder(stack);
    String out;
    NBTTagCompound tag = TagUtil.getToolTag(TagUtil.getTagSafe(stack));

    float percentHp = tag.getFloat(Tags.ATTACK);

    info.addDurability(!detailed);

    out = I18n.format("tinkers.greatblade.bossmultiplier.percentMaxHp", percentHp);
    info.add(TextFormatting.BLUE + out);

    if (Math.abs(Config.greatbladeBossMultiplier - 1) >= 1E-4) { // if it is not equal to one, a multiplier is displayed
      if (Config.greatbladeBossMultiplier < 1) {
        out = I18n.format("tinkers.greatblade.bossmultiplier.negative", (1 - Config.greatbladeBossMultiplier)*100);
        info.add(TextFormatting.RED + out);
      } else {
        out = I18n.format("tinkers.greatblade.bossmultiplier.positive", (Config.greatbladeBossMultiplier)*100);
        info.add(TextFormatting.BLUE + out);
      }
    }

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
  public int[] getRepairParts() {
    return new int[] {1,2};
  }

  @Override
  protected ToolNBT buildTagData(List<Material> materials) {
    HandleMaterialStats handle = materials.get(0).getStatsOrUnknown(MaterialTypes.HANDLE);
    HeadMaterialStats head0 = materials.get(1).getStatsOrUnknown(MaterialTypes.HEAD);
    HeadMaterialStats head1 = materials.get(2).getStatsOrUnknown(MaterialTypes.HEAD);
    ExtraMaterialStats binding = materials.get(3).getStatsOrUnknown(MaterialTypes.EXTRA);

    ToolNBT data = new ToolNBT();
    data.head(head0, head1);
    data.extra(binding);
    data.handle(handle);

    data.attack += 4; //ensure you cannot create a totally garbage weapon
    return data;
  }
}
