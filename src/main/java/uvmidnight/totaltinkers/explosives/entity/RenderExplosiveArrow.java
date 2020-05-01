package uvmidnight.totaltinkers.explosives.entity;

import net.minecraft.client.renderer.entity.RenderManager;
import org.lwjgl.opengl.GL11;
import slimeknights.tconstruct.library.client.renderer.RenderProjectileBase;
import slimeknights.tconstruct.tools.common.entity.EntityArrow;

public class RenderExplosiveArrow extends RenderProjectileBase<EntityExplosiveArrow> {
    public RenderExplosiveArrow(RenderManager renderManager) {
        super(renderManager);
    }
}
