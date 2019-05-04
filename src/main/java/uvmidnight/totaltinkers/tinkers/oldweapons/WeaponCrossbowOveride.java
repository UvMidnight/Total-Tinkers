package uvmidnight.totaltinkers.tinkers.oldweapons;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.client.crosshair.Crosshairs;
import slimeknights.tconstruct.library.client.crosshair.ICrosshair;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.tools.ranged.item.CrossBow;

import javax.annotation.Nullable;

import static uvmidnight.totaltinkers.util.Config.crossbowOldCrosshair;

//I have no idea how to use asm. so this is made instead.
public class WeaponCrossbowOveride extends CrossBow {

  public WeaponCrossbowOveride() {
    super();

    addCategory(Category.LAUNCHER);
    setTranslationKey("tconstruct:crossbow").setRegistryName("tconstruct:crossbow");
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
