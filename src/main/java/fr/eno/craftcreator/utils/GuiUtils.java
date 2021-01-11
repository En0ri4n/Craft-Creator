package fr.eno.craftcreator.utils;

import java.awt.Color;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;

public class GuiUtils
{
	public static Minecraft mc = Minecraft.getInstance();
	
	public static void closeAndGrabMouse()
	{
		close();
		mc.mouseHelper.grabMouse();
	}
	
	public static void close()
	{
		mc.displayGuiScreen((Screen) null);
	}
	
	public static boolean isMouseHover(int x, int y, int mouseX, int mouseY, int width, int height)
	{
		boolean flag = mouseX > x && mouseX < (x + width) && mouseY > y && mouseY < (y + height);
		
		return flag;
	}
	
	public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, LivingEntity p_228187_5_) {
	      float f = (float)Math.atan((double)(mouseX / 40.0F));
	      float f1 = (float)Math.atan((double)(mouseY / 40.0F));
	      RenderSystem.pushMatrix();
	      RenderSystem.translatef((float)posX, (float)posY, 1050.0F);
	      RenderSystem.scalef(1.0F, 1.0F, -1.0F);
	      MatrixStack matrixstack = new MatrixStack();
	      matrixstack.translate(0.0D, 0.0D, 1000.0D);
	      matrixstack.scale((float)scale, (float)scale, (float)scale);
	      Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
	      Quaternion quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
	      quaternion.multiply(quaternion1);
	      matrixstack.rotate(quaternion);
	      float f2 = p_228187_5_.renderYawOffset;
	      float f3 = p_228187_5_.rotationYaw;
	      float f4 = p_228187_5_.rotationPitch;
	      float f5 = p_228187_5_.prevRotationYawHead;
	      float f6 = p_228187_5_.rotationYawHead;
	      p_228187_5_.renderYawOffset = 180.0F + f * 20.0F;
	      p_228187_5_.rotationYaw = 180.0F + f * 40.0F;
	      p_228187_5_.rotationPitch = -f1 * 20.0F;
	      p_228187_5_.rotationYawHead = p_228187_5_.rotationYaw;
	      p_228187_5_.prevRotationYawHead = p_228187_5_.rotationYaw;
	      EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
	      quaternion1.conjugate();
	      entityrenderermanager.setCameraOrientation(quaternion1);
	      entityrenderermanager.setRenderShadow(false);
	      IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
	      entityrenderermanager.renderEntityStatic(p_228187_5_, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixstack, irendertypebuffer$impl, 15728880);
	      irendertypebuffer$impl.finish();
	      entityrenderermanager.setRenderShadow(true);
	      p_228187_5_.renderYawOffset = f2;
	      p_228187_5_.rotationYaw = f3;
	      p_228187_5_.rotationPitch = f4;
	      p_228187_5_.prevRotationYawHead = f5;
	      p_228187_5_.rotationYawHead = f6;
	      RenderSystem.popMatrix();
	   }
	
	public static boolean isPointInRegion(int rectX, int rectY, int rectWidth, int rectHeight, int pointX, int pointY)
    {
        int i = 0;
        int j = 0;
        pointX = pointX - i;
        pointY = pointY - j;
        return pointX >= rectX - 1 && pointX < rectX + rectWidth + 1 && pointY >= rectY - 1 && pointY < rectY + rectHeight + 1;
    }
	
	public static Color rainbowString()
	{
		Color color = new Color((Color.HSBtoRGB(System.currentTimeMillis() % 2000L / 2000.0f, 1.0f, 1.0f)));
		return color;
	}
	
	public static void drawHighlight(int x, int y)
	{
		RenderSystem.pushMatrix();
		RenderSystem.disableLighting();
		RenderSystem.disableDepthTest();
		GlStateManager.colorMask(true, true, true, false);
		RenderSystem.translated(x, y, 0);
		Screen.fill(x, y, x + 18, y + 18, 0x80fff400);
		GlStateManager.colorMask(true, true, true, true);
		RenderSystem.enableDepthTest();
		RenderSystem.popMatrix();
	}
}
