package uvmidnight.totaltinkers.oldweapons;

import net.minecraft.client.renderer.entity.RenderManager;
import org.lwjgl.opengl.GL11;
import slimeknights.tconstruct.library.client.renderer.RenderProjectileBase;
import uvmidnight.totaltinkers.oldweapons.entities.EntityJavelin;

public class RenderJavelin extends RenderProjectileBase<EntityJavelin> {
    public RenderJavelin(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void customRendering(EntityJavelin entity, double x, double y, double z, float entityYaw, float partialTicks) {

        //rotate it into direction thrown
        GL11.glRotatef(entity.rotationYaw, 0f, 1f, 0f);
        GL11.glRotatef(-entity.rotationPitch, 1f, 0f, 0f);

        GL11.glRotatef(60f, 1f, 0f, 0f);
        GL11.glRotatef(90f, 0f, 1f, 0f);

        //Long boi
        GL11.glScalef(1.5F, 3F, 1.5F);
        GL11.glTranslatef(0.35f, -0.25f, 0F);

//    GL11.glRotatef(90f, 1f, 0f, 0f);

    /*GL11.glRotatef(entity.roll, 0f, 0f, 1f);
    if(!entity.inGround) {
      entity.spin += 20 * partialTicks;
    }
    float r = entity.spin;*/

    }
}
