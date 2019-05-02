package uvmidnight.totaltinkers.tinkers.oldweapons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import uvmidnight.totaltinkers.util.Config;


//Yeah I have no idea how this code works
//Just took like most of it from the vamprism mod
//This implementation how I did it is literally about the worst it could be
@SideOnly(Side.CLIENT)
public class LayerBattleaxe extends Gui {
  private final Minecraft mc;
  private int screenColor = 0;
  private boolean rendering = false;
  private int renderFullColor;
  public LayerBattleaxe(Minecraft mc) {
    this.mc = mc;
  }

  @SubscribeEvent
  public void onClientTick(TickEvent.ClientTickEvent event) {
    if (mc.player == null)
      return;
    if (event.phase == TickEvent.Phase.END)
      return;
    if (rendering) {
      screenColor = renderFullColor;
    } else {
      screenColor = 0;
    }
  }

  public void makeRenderFullColor(int color) {
    renderFullColor = color;
    rendering = true;
  }

  public void makeRenderFullColor(int color, boolean startRendering) {
    renderFullColor = color;
    rendering = startRendering;
  }

  public void makeRenderFullColor(boolean startRendering) {
    rendering = startRendering;
  }

  public boolean isRendering() {
    return rendering;
  }

  @SubscribeEvent(priority = EventPriority.LOW)
  public void onRenderWorldLast(RenderWorldLastEvent event) {

    if (!Config.disable_screen_overlay && screenColor != 0) {
      // Set the working matrix/layer to a layer directly on the screen/in front of
      // the player
      ScaledResolution scaledresolution = new ScaledResolution(this.mc);
      // int factor=scaledresolution.getScaleFactor();
      GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
      GlStateManager.matrixMode(GL11.GL_PROJECTION);
      GlStateManager.loadIdentity();
      GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth_double(),
              scaledresolution.getScaledHeight_double(), 0.0D, 1D, -1D);
      GlStateManager.matrixMode(GL11.GL_MODELVIEW);
      GlStateManager.loadIdentity();
      GlStateManager.pushMatrix();
      GL11.glDisable(GL11.GL_DEPTH_TEST);
      int w = (scaledresolution.getScaledWidth());
      int h = (scaledresolution.getScaledHeight());
//      if (fullScreen) {
      // Render a see through colored square over the whole screen
      float r = (float) (screenColor >> 16 & 255) / 255.0F;
      float g = (float) (screenColor >> 8 & 255) / 255.0F;
      float b = (float) (screenColor & 255) / 255.0F;
      float a = 0.35F * (screenColor >> 24 & 255) / 255F;

      GlStateManager.disableTexture2D();
      GlStateManager.enableBlend();
      GlStateManager.disableAlpha();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.shadeModel(7425);
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder worldrenderer = tessellator.getBuffer();
      worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
      worldrenderer.pos(0, h, (double) this.zLevel).color(r, g, b, a).endVertex();
      worldrenderer.pos(w, h, (double) this.zLevel).color(r, g, b, a).endVertex();
      worldrenderer.pos(w, 0, (double) this.zLevel).color(r, g, b, a).endVertex();
      worldrenderer.pos(0, 0, (double) this.zLevel).color(r, g, b, a).endVertex();

      tessellator.draw();
      GlStateManager.shadeModel(7424);
      GlStateManager.disableBlend();
      GlStateManager.enableAlpha();
      GlStateManager.enableTexture2D();

      /*
       * Try later this.drawGradientRect(0, 0, w,Math.round(h/(2/renderRed)),
       * 0xfff00000, 0x000000); this.drawGradientRect(0,
       * h-Math.round(h/(2/renderRed)), w, h, 0x00000000, 0xfff00000);
       * this.drawGradientRect2(0, 0, w/6, h, 0x000000, 0xfff00000);
       * this.drawGradientRect2(w-w/6, 0, w, h, 0xfff00000, 0x000000);
       */

//      } else {
      int bw = 0;
      int bh = 0;

      bh = Math.round(h / (float) 4 * 0.5F);
      bw = Math.round(w / (float) 8 * 0.5F);

      this.drawGradientRect(0, 0, w, bh, screenColor, 0x000);
      this.drawGradientRect(0, h - bh, w, h, 0x00000000, screenColor);
      this.drawGradientRect2(0, 0, bw, h, 0x000000, screenColor);
      this.drawGradientRect2(w - bw, 0, w, h, screenColor, 0x00);
//      }
      GL11.glEnable(GL11.GL_DEPTH_TEST);
      GlStateManager.popMatrix();
    }
  }

  protected void drawGradientRect2(int left, int top, int right, int bottom, int startColor, int endColor) {
    float f = (float) (startColor >> 24 & 255) / 255.0F;
    float f1 = (float) (startColor >> 16 & 255) / 255.0F;
    float f2 = (float) (startColor >> 8 & 255) / 255.0F;
    float f3 = (float) (startColor & 255) / 255.0F;
    float f4 = (float) (endColor >> 24 & 255) / 255.0F;
    float f5 = (float) (endColor >> 16 & 255) / 255.0F;
    float f6 = (float) (endColor >> 8 & 255) / 255.0F;
    float f7 = (float) (endColor & 255) / 255.0F;
    GlStateManager.disableTexture2D();
    GlStateManager.enableBlend();
    GlStateManager.disableAlpha();
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    GlStateManager.shadeModel(7425);
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder worldrenderer = tessellator.getBuffer();
    worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
    worldrenderer.pos((double) right, (double) top, (double) this.zLevel).color(f1, f2, f3, f).endVertex();
    worldrenderer.pos((double) left, (double) top, (double) this.zLevel).color(f5, f6, f7, f4).endVertex();
    worldrenderer.pos((double) left, (double) bottom, (double) this.zLevel).color(f5, f6, f7, f4).endVertex();
    worldrenderer.pos((double) right, (double) bottom, (double) this.zLevel).color(f1, f2, f3, f).endVertex();
    tessellator.draw();
    GlStateManager.shadeModel(7424);
    GlStateManager.disableBlend();
    GlStateManager.enableAlpha();
    GlStateManager.enableTexture2D();
  }
}
