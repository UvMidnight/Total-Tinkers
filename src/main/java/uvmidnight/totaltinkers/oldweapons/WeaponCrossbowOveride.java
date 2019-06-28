package uvmidnight.totaltinkers.oldweapons;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.common.Sounds;
import slimeknights.tconstruct.library.client.BooleanItemPropertyGetter;
import slimeknights.tconstruct.library.client.crosshair.Crosshairs;
import slimeknights.tconstruct.library.client.crosshair.ICrosshair;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tools.ProjectileLauncherNBT;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.tools.ranged.item.CrossBow;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class WeaponCrossbowOveride extends CrossBow {

    private static final String TAG_ReloadProgress = "ReloadProgress";
    private static final String TAG_Reloading = "getReloadingProgress";

    private static boolean autoCrossbowReload;
    private static boolean crossbowOldCrosshair;

    public WeaponCrossbowOveride() {
        super();

        autoCrossbowReload = OldWeapons.autoCrossbowReload.getBoolean();
        crossbowOldCrosshair = OldWeapons.crossbowOldCrosshair.getBoolean();

        if (autoCrossbowReload) {
            this.addPropertyOverride(PROPERTY_PULL_PROGRESS, new IItemPropertyGetter() {
                @Override
                @SideOnly(Side.CLIENT)
                public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                    if (entityIn != null) {
                        if (isLoaded(stack)) {
                            return 1f;
                        }
                        return isReloading(stack) ? getDrawbackProgress(stack, getReloadingProgress(stack)) : 0;
                    }
                    return 0f;
                }
            });

            this.addPropertyOverride(PROPERTY_IS_PULLING, new BooleanItemPropertyGetter() {
                @Override
                public boolean applyIf(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                    return true;
                }
            });

            this.addPropertyOverride(PROPERTY_IS_LOADED, new BooleanItemPropertyGetter() {
                @Override
                public boolean applyIf(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                    return entityIn != null && isLoaded(stack) && !isReloading(stack);
                }
            });
        }
        addCategory(Category.LAUNCHER);
        setTranslationKey("tconstruct:crossbow").setRegistryName("tconstruct:crossbow");

    }


    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (autoCrossbowReload) {

            ItemStack itemStackIn = playerIn.getHeldItem(hand);

            NBTTagCompound tags = TagUtil.getTagSafe(itemStackIn);

            // loaded
            if (isLoaded(itemStackIn)) {
                if (!findAmmo(itemStackIn, playerIn).isEmpty() || playerIn.capabilities.isCreativeMode) {
                    super.onItemRightClick(worldIn, playerIn, hand);
                    initiateReload(itemStackIn, playerIn, tags);
                    return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
                }
            } else if (!isReloading(itemStackIn) && (!findAmmo(itemStackIn, playerIn).isEmpty() || playerIn.capabilities.isCreativeMode)) {
                initiateReload(itemStackIn, playerIn, tags);
            }
            return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
        } else {
            return super.onItemRightClick(worldIn, playerIn, hand);
        }
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (autoCrossbowReload) {
            onUpdateTraits(stack, world, entity, itemSlot, isSelected);

            if (!stack.hasTagCompound()) {
                return;//no idea how this would happen
            }

            if (!(entity instanceof EntityPlayer)) {
                return;
            }

            NBTTagCompound tags = TagUtil.getTagSafe(stack);
            EntityPlayer player = (EntityPlayer) entity;

            if (player.inventory.getCurrentItem() != stack) {
//        preventSlowDown(player, 0.195f);
                if (!isLoaded(stack)) {
                    tags.setInteger(TAG_ReloadProgress, 0);
                }
                return;
            }

            if (isReloading(stack)) {

                int timeLeft = getReloadingProgress(stack);
                if (timeLeft == -1) {
                    timeLeft++;
                }
                timeLeft++;
                tags.setInteger(TAG_ReloadProgress, timeLeft);
                if (getDrawbackProgress(stack, timeLeft) - 1.0F >= -1E-7) {
                    setLoaded(stack, true);
                    tags.setBoolean(TAG_Reloading, false);
                    Sounds.PlaySoundForPlayer(player, Sounds.crossbow_reload, 1.5f, 0.9f + itemRand.nextFloat() * 0.1f);
                }
            }
            if (!isReloading(stack) && tags.getInteger(TAG_ReloadProgress) == -1) {
                tags.setBoolean(TAG_Reloading, true);
            }
        } else {
            super.onUpdate(stack, world, entity, itemSlot, isSelected);
        }
    }

    @Override
    protected float getDrawbackProgress(ItemStack itemStack, int timePassed) {
        if (autoCrossbowReload) {
            float drawProgress = ProjectileLauncherNBT.from(itemStack).drawSpeed * (float) timePassed;
            return Math.min(1f, drawProgress / (float) getDrawTime());
        }
        return super.getDrawbackProgress(itemStack, timePassed);
    }

    @Override
    public float getDrawbackProgress(ItemStack itemstack, EntityLivingBase entityIn) {
        if (autoCrossbowReload) {
            int timePassed = getReloadingProgress(itemstack);
            return getDrawbackProgress(itemstack, timePassed);
        }
        return super.getDrawbackProgress(itemstack, entityIn);
    }

    public int getReloadingProgress(ItemStack stack) {
        return TagUtil.getTagSafe(stack).getInteger(TAG_ReloadProgress);
    }

    public boolean isReloading(ItemStack stack) {
        return TagUtil.getTagSafe(stack).getBoolean(TAG_Reloading);
    }

    public void initiateReload(ItemStack itemStackIn, EntityPlayer player, NBTTagCompound tags) {
        if (ToolHelper.isBroken(itemStackIn)) {
            return;
        }

        // has ammo?
        if (!findAmmo(itemStackIn, player).isEmpty() || player.capabilities.isCreativeMode) {
            if (!isLoaded(itemStackIn)) {
                tags.setBoolean(TAG_Reloading, true);
                tags.setInteger(TAG_ReloadProgress, 0);
            }
        }
    }

    @Nonnull
    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.NONE;
    }


    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
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

    @SideOnly(Side.CLIENT)
    @Override
    public float getCrosshairState(ItemStack itemStack, EntityPlayer player) {
        if (autoCrossbowReload) {
            if (isLoaded(itemStack)) {
                return 1f;
            }
            if (isReloading(itemStack)) {
                return getDrawbackProgress(itemStack, getReloadingProgress(itemStack));
            }
            return 0;
        } else {
            return super.getCrosshairState(itemStack, player);
        }
    }
}
