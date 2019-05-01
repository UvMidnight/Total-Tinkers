package uvmidnight.totaltinkers.tinkers.oldweapons;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.AoeToolCore;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.tools.TinkerTools;

import javax.annotation.Nonnull;
import java.util.List;


//Hey, apparently this was in the tinkers construct codebase for 1.12
//Not really sure why it was not implemented
//But here it is now

//most of this code is stolen/used from tinkers construct. Description below is directly from it. Vampirism mod has a known overlay that is basically exactly this

// Ability: Berserk. Can be activated on demand, gives a speedboost, jump boost, mining boost, damage boost. Also makes you take more damage
// Screen turns red/with a red border (steal from thaumcraft) and you can't switch item while berserk is active
public class WeaponBattleAxe extends AoeToolCore {
  public WeaponBattleAxe() {
    super(PartMaterialType.handle(TinkerTools.toughToolRod),
            PartMaterialType.head(TinkerTools.broadAxeHead),
            PartMaterialType.head(TinkerTools.broadAxeHead),
            PartMaterialType.extra(TinkerTools.toughBinding));

    addCategory(Category.WEAPON);

    setHarvestLevel("axe", 0);
    setTranslationKey("battleaxe").setRegistryName("battleaxe");
  }

  @Override
  public ImmutableList<BlockPos> getAOEBlocks(ItemStack stack, World world, EntityPlayer player, BlockPos origin) {
    return ToolHelper.calcAOEBlocks(stack, world, player, origin, 2, 2, 1);
  }

  @Override
  public float damagePotential() {
    return 2.0f;
  }

  @Override
  public float damageCutoff() {
    return 30f;
  }

  @Override
  public double attackSpeed() {
    return 1f;
  }

  @Override
  public int[] getRepairParts() {
    return new int[]{1, 2};
  }

  // no offhand for you
  @Nonnull
  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
    ItemStack itemStackIn = playerIn.getHeldItem(hand);
    return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
  }

  @Nonnull
  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    // todo: special action - beserk rage stuff
    return EnumActionResult.FAIL;
  }

  @Override
  protected ToolNBT buildTagData(List<Material> materials) {
    HandleMaterialStats handle = materials.get(0).getStatsOrUnknown(MaterialTypes.HANDLE);
    HeadMaterialStats head1 = materials.get(1).getStatsOrUnknown(MaterialTypes.HEAD);
    HeadMaterialStats head2 = materials.get(2).getStatsOrUnknown(MaterialTypes.HEAD);
    ExtraMaterialStats binding = materials.get(3).getStatsOrUnknown(MaterialTypes.EXTRA);

    ToolNBT data = new ToolNBT();
    data.head(head1, head2);
    data.extra(binding);
    data.handle(handle);

//    data.harvestLevel = Math.max(head1.harvestLevel, head2.harvestLevel);

//    data.durability = (head1.durability + head2.durability) / 2;
    //data.handle(handle).extra(binding);

    //data.durability *= 1f + 0.15f * (binding.extraQuality - 0.5f);
    //data.speed *= 1f + 0.1f * (handle.modifier * handle.miningspeed);
    data.speed *= 0.5f; // slower because AOE
    // no base damage but higher damage potential
//    data.attack = (head1.attack + head2.attack) * 3f / 2f;
    //data.attack *= 1f + 0.1f * handle.modifier * binding.extraQuality;

    /*
    data.durability += head1.durability * (0.2f * head2.extraQuality + 0.2f * binding.extraQuality + 0.1f * handle.modifier);
    data.durability += head2.durability * (0.2f * head1.extraQuality + 0.2f * binding.extraQuality + 0.1f * handle.modifier);
    data.durability += binding.durability * binding.extraQuality * 0.5f;
    data.durability += handle.durability * 0.1f;
    data.attack = (head1.attack + head2.attack)*2f/3f;
    data.attack += (0.2f + 0.7f * handle.modifier * binding.extraQuality) * (head1.attack + head2.attack) / 3f;
    data.speed = head1.miningspeed/2f + head2.miningspeed/2f;
    data.speed *= 0.3f + 0.3f * handle.modifier * binding.extraQuality;
*/

    return data;
  }
}