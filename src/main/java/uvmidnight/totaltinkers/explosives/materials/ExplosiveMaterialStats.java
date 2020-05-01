package uvmidnight.totaltinkers.explosives.materials;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.JsonUtils;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.client.CustomFontColor;
import slimeknights.tconstruct.library.materials.AbstractMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;

import javax.annotation.Nullable;
import java.util.List;

public class ExplosiveMaterialStats extends AbstractMaterialStats {
    public static final String LOC_RADIUS = "stat.totaltinkers.explosive.radius.name";

    public static final String LOC_RADIUSDesc = "stat.totaltinkers.explosive.radius.desc";

    public static final String COLOR_RADIUS = CustomFontColor.encodeColor(74, 232, 102);

    public final float radius;
    public final float attack;
    public final int durability;

    public ExplosiveMaterialStats(float attack, float radius, int durability) {
        super(ExplosivePartType.EXPLOSIVE_CORE);
        this.radius = radius;
        this.attack = attack;
        this.durability = durability;
    }
    @Override
    public List<String> getLocalizedInfo()
    {
        return ImmutableList.of(HeadMaterialStats.formatAttack(this.attack), formatRadius(this.radius), HeadMaterialStats.formatDurability(this.durability));
    }

    @Override
    public List<String> getLocalizedDesc()
    {
        return ImmutableList.of(Util.translate(HeadMaterialStats.LOC_AttackDesc), Util.translate(LOC_RADIUSDesc), Util.translate(HeadMaterialStats.LOC_DurabilityDesc));
    }

    public static String formatRadius(float radius)
    {
        return formatNumber(LOC_RADIUS, COLOR_RADIUS, radius);
    }

    @Nullable
    public static ExplosiveMaterialStats deserialize(JsonObject material) throws JsonParseException
    {
        if (!JsonUtils.hasField(material, "explosivecore")) {
            return null;
        }

        JsonObject explosiveCore = JsonUtils.getJsonObject(material, "explosivecore");

        float attack = JsonUtils.getFloat(explosiveCore, "attack");
        int durability = JsonUtils.getInt(explosiveCore, "durability");
        float radius = JsonUtils.getFloat(explosiveCore, "radius");

        System.out.print("Material stats: " + "attack: " + attack + "durability: " + durability + "radius" + radius);
        return new ExplosiveMaterialStats(attack, radius, durability);
    }
}
