package uvmidnight.totaltinkers.tinkers.oldweapons;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.common.Sounds;
import slimeknights.tconstruct.library.client.crosshair.Crosshairs;
import slimeknights.tconstruct.library.client.crosshair.ICrosshair;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tools.ranged.IAmmo;
import slimeknights.tconstruct.library.tools.ranged.ProjectileCore;
import slimeknights.tconstruct.library.tools.ranged.ProjectileLauncherCore;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.tools.ranged.item.CrossBow;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static uvmidnight.totaltinkers.util.Config.crossbowOldCrosshair;

public class WeaponCrossbowOveride extends CrossBow {

  private static final String TAG_Loaded = "Loaded";
  private static final String TAG_Reloading = "Reloading";

  public WeaponCrossbowOveride() {
    super();
    addCategory(Category.LAUNCHER);
    setTranslationKey("tconstruct:crossbow").setRegistryName("tconstruct:crossbow");
  }
//  @Override
//  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
//    // has to be done in onUpdate because onTickUsing is too early and gets overwritten. bleh.
//    preventSlowDown(entityIn, 0.195f);
//
//    super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
//  }
//
//  @Nonnull
//  @Override
//  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
//    ItemStack itemStackIn = playerIn.getHeldItem(hand);
//    if(isLoaded(itemStackIn) && !ToolHelper.isBroken(itemStackIn)) {
//      super.onPlayerStoppedUsing(itemStackIn, worldIn, playerIn, 0);
//      setLoaded(itemStackIn, false);
//    }
//    else {
//      return super.onItemRightClick(worldIn, playerIn, hand);
//    }
//    return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
//  }
//
//  @Override
//  public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
//    if(!ToolHelper.isBroken(stack) && (entityLiving instanceof EntityPlayer)) {
//      int useTime = this.getMaxItemUseDuration(stack) - timeLeft;
//      if(getDrawbackProgress(stack, useTime) >= 1f) {
//        Sounds.PlaySoundForPlayer(entityLiving, Sounds.crossbow_reload, 1.5f, 0.9f + itemRand.nextFloat() * 0.1f);
//        setLoaded(stack, true);
//      }
//    }
//  }

  public float getMinWindupProgress(ItemStack itemStack) {
    return 1.0f;
  }

  //method seems to be fine
  @Nonnull
  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
    ItemStack itemStackIn = playerIn.getHeldItem(hand);

    NBTTagCompound tags = TagUtil.getTagSafe(itemStackIn);

    // unload on shift-rightclick
    if(playerIn.isSneaking()) {
      if(unload(playerIn.getHeldItem(hand), playerIn, tags)){
        return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
      }
    }

    // loaded
    if(isLoaded(itemStackIn)) {
      super.onItemRightClick(worldIn,playerIn,hand);
      setLoaded(itemStackIn, false);
    }

    // reload (automatically after firing)
    initiateReload(itemStackIn, playerIn, tags);

    return ActionResult.newResult(EnumActionResult.FAIL, itemStackIn);
  }
  //Method seems to be fine.
  @Override
  public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) {
    super.onUpdate(stack, world, entity, par4, par5);

    if(!stack.hasTagCompound()) {
      return;//no idea how this would happen
    }

    if(!(entity instanceof EntityPlayer)) {
      return;
    }

    EntityPlayer player = (EntityPlayer) entity;
    if(player.inventory.getCurrentItem() != stack)
      return;

    NBTTagCompound tags =  TagUtil.getTagSafe(stack);
    if(!isLoaded(stack)) {
      int timeLeft = isReloading(stack);
      timeLeft--;
      if(timeLeft > 0) {
        tags.setInteger(TAG_Reloading, timeLeft);
      }
      else {
        tags.removeTag(TAG_Reloading);
        tags.setBoolean(TAG_Loaded, false);
        reload(stack, player, world, tags);
      }
    }
  }

  public boolean isLoaded(ItemStack stack) {
    return TagUtil.getTagSafe(stack).getBoolean(TAG_Loaded);
  }
  public int isReloading(ItemStack stack) {
    return TagUtil.getTagSafe(stack).getInteger(TAG_Reloading);
  }

  //this method doesnt seem to do anything at all
  public float getWindupProgress(ItemStack itemStack, EntityPlayer player) {
    NBTTagCompound tags = TagUtil.getTagSafe(itemStack);

    // loaded, full accuracy
    if (isLoaded(itemStack)) {
      return 1.0F;
    }
      // not loaded, but reloading -> progress
    else if(isReloading(itemStack) < 0)
      return 1.0f - ((float)tags.getInteger("Reloading"))/((float)getDrawbackProgress(itemStack, getDrawTime()));
      // not loaded and not reloading -> no accuracy!
    else
      return 0.0f;
  }

  public void initiateReload(ItemStack itemStackIn, EntityPlayer player, NBTTagCompound tags) {
    if(ToolHelper.isBroken(itemStackIn)) {
      return;
    }

    // has ammo?
    if(!findAmmo(itemStackIn, player).isEmpty()){
      if(!isLoaded(itemStackIn)) {
        tags.setInteger(TAG_Reloading, getDrawTime());
      }
    }
  }

  // called after the reloading is done. Does the actual loading
  public boolean reload(ItemStack weapon, EntityPlayer player, World world, NBTTagCompound tags)
  {
    ItemStack ammo = findAmmo(weapon, player);
    // no ammo present
    if(ammo.isEmpty()) {
      return false;
    }

    // already loaded
    if(tags.getBoolean("Loaded")){
      return false;
    }

    ItemStack copy = ammo.copy();
    TagUtil.getTagSafe(copy).setInteger("Ammo", 1); //this is a product of legacy code need to remove it

    // load ammo into nbt
    NBTTagCompound ammotag = new NBTTagCompound();
    copy.writeToNBT(ammotag);
    //ammotag.getCompoundTag("tag"; // set ammo count to 1
    tags.setTag("LoadedItem", ammotag);
    tags.setBoolean("Loaded", true);

    // remove loaded item
    if(ammo.getItem() instanceof IAmmo) {
      ((IAmmo) ammo.getItem()).useAmmo(ammo, player);
    }

    //this should not be here
    Sounds.PlaySoundForPlayer(player, Sounds.crossbow_reload, 1.5f, 0.9f + itemRand.nextFloat() * 0.1f);

    return true;
  }

  // stops reloading, or unloads the ammo if it has been loaded
  public boolean unload(ItemStack weapon, EntityPlayer player, NBTTagCompound tags)
  {
    // is reloading?
    if(isReloading(weapon) > 0) {
      // stop it
      tags.removeTag(TAG_Reloading);
      return true;
    }

    // loaded?
    if(isLoaded(weapon)) {
      // unload
      tags.removeTag(TAG_Reloading);
      ItemStack ammo = findAmmo( weapon, player);
      // try to pick it up
      ToolHelper.healTool(ammo, 10 ,player);

      // unloaded
      tags.setBoolean(TAG_Loaded, false);

      return true;
    }

    // nothing to unload
    return false;
  }


  @SideOnly(Side.CLIENT)
  @Override
  public ICrosshair getCrosshair(ItemStack itemStack, EntityPlayer player) {
    if (crossbowOldCrosshair) {
      return Crosshairs.SQUARE;
    } else {
      return Crosshairs.T;
    }
  }

}
