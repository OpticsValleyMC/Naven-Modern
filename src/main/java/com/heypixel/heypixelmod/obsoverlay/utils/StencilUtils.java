package com.heypixel.heypixelmod.obsoverlay.utils;

import com.heypixel.heypixelmod.mixin.O.accessors.RenderTargetAccessor;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

public class StencilUtils {
   private static final Minecraft mc = Minecraft.getInstance();
   private static int stencilDepthBufferID = -1;

   public static void write(boolean renderClipLayer) {
      setupFBO();
      if (stencilDepthBufferID != -1) {
         EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencilDepthBufferID);
         EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencilDepthBufferID);
      }
      GL11.glClear(1024);
      GL11.glEnable(2960);
      GL11.glStencilFunc(519, 1, 65535);
      GL11.glStencilOp(7680, 7680, 7681);
      if (!renderClipLayer) {
         RenderSystem.colorMask(false, false, false, false);
      }
   }

   public static void erase(boolean invert) {
      RenderSystem.colorMask(true, true, true, true);
      GL11.glStencilFunc(invert ? 514 : 517, 1, 65535);
      GL11.glStencilOp(7680, 7680, 7681);
   }

   public static void dispose() {
      GL11.glDisable(2960);
   }

   public static void setupFBO() {
      if (stencilDepthBufferID == -1 || mc.getMainRenderTarget().getDepthTextureId() > -1) {
         setupFBO(mc.getMainRenderTarget());
         ((RenderTargetAccessor)mc.getMainRenderTarget()).setDepthBufferId(-1);
      }
   }

   public static void setupFBO(RenderTarget fbo) {
      if (fbo.getDepthTextureId() > -1) {
         EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.getDepthTextureId());
      } else if (stencilDepthBufferID > -1) {
         EXTFramebufferObject.glDeleteRenderbuffersEXT(stencilDepthBufferID);
      }
      int stencilDepthBufferID = EXTFramebufferObject.glGenRenderbuffersEXT();
      StencilUtils.stencilDepthBufferID = stencilDepthBufferID;
      EXTFramebufferObject.glBindRenderbufferEXT(36161, stencilDepthBufferID);
      EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, mc.getWindow().getWidth(), mc.getWindow().getHeight());
      EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencilDepthBufferID);
      EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencilDepthBufferID);
   }
}
