package fr.eno.craftcreator.api;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * class contains some useful methods for the client.
 */
public class ClientUtils
{
    public static final KeyMapping KEY_OPEN_RECIPES_MENU = new KeyMapping("key.craftcreator.open_recipes_menu", GLFW.GLFW_KEY_K, "key.categories.craft_creator");
    public static final KeyMapping KEY_OPEN_TUTORIAL = new KeyMapping("key.craftcreator.open_tutorial", GLFW.GLFW_KEY_J, "key.categories.craft_creator");

    public static final Predicate<RenderType> DEFAULT_BLOCK_RENDER = (r) -> r == RenderType.cutoutMipped();
    private static final Supplier<Minecraft> minecraftSupplier = Minecraft::getInstance;

    /**
     * @return the minecraft instance
     * @see Minecraft#getInstance()
     */
    public static Minecraft getMinecraft()
    {
        return minecraftSupplier.get();
    }

    /**
     * @return the client level instance
     * @see Minecraft#level
     */
    public static Level getClientLevel()
    {
        return minecraftSupplier.get().level;
    }

    /**
     * @return the minecraft font
     * @see Minecraft#font
     */
    public static Font getFontRenderer()
    {
        return minecraftSupplier.get().font;
    }

	/**
     * @return the minecraft item renderer
     * @see Minecraft#getItemRenderer()
     */
    public static ItemRenderer getItemRenderer()
    {
        return minecraftSupplier.get().getItemRenderer();
    }

	/**
	 * @return the client minecraft player
	 * @see Minecraft#player
	 */
    public static Player getClientPlayer()
    {
        return minecraftSupplier.get().player;
    }

	/**
	 * @param screen the screen to open
	 * @see Minecraft#setScreen(Screen)
     */
    public static void openScreen(Screen screen)
    {
        minecraftSupplier.get().setScreen(screen);
    }

	/**
	 * @return the current displayed screen (can be null)
	 * @see Minecraft#screen
	 */
    public static Screen getCurrentScreen()
    {
        return minecraftSupplier.get().screen;
    }

	/**
     * Register the given container with the given screen
     * @see MenuScreens#register(MenuType, MenuScreens.ScreenConstructor)
     */
    public static <T extends AbstractContainerMenu, U extends Screen & MenuAccess<T>> void registerScreen(MenuType<T> container, MenuScreens.ScreenConstructor<T, U> screenConstructor)
    {
        MenuScreens.register(container, screenConstructor);
    }

    /**
     * Bind a texture in the texture manager
     *
     * @param texture the texture to bind
     * @see RenderSystem#setShaderTexture(int, int)
     */
    public static void bindTexture(ResourceLocation texture)
    {
        RenderSystem.setShaderTexture(0, texture);
    }

    /**
     * calls {@link net.minecraft.client.gui.Font#width(String)}
     *
     * @param str The string
     * @return width of the given string
     * @see net.minecraft.client.gui.Font#width(String)
     */
    public static int width(String str)
    {
        return getFontRenderer().width(str);
    }
    
    /**
     * Calls {@link RenderSystem#setShaderColor(float, float, float, float)}
     */
    @SuppressWarnings("deprecation")
    public static void color4f(float r, float g, float b, float a)
    {
        RenderSystem.setShaderColor(r, g, b, a);
    }

    /**
     * Render a quad with the specified color<br>
     * <b>Warning:</b> This method must be called between {@link BufferBuilder#begin(VertexFormat.Mode, VertexFormat)} and {@link BufferBuilder#end()}
     */
    public static void renderQuad(BufferBuilder bufferBuilder, int x0, int y0, int x1, int y1, int r, int g, int b, int alpha)
    {
        bufferBuilder.vertex(x0, y1, 0.0D).uv(0F, 1F).color(r, g, b, alpha).endVertex();
        bufferBuilder.vertex(x1, y1, 0.0D).uv(0F, 1F).color(r, g, b, alpha).endVertex();
        bufferBuilder.vertex(x1, y0, 0.0D).uv(1F, 0F).color(r, g, b, alpha).endVertex();
        bufferBuilder.vertex(x0, y0, 0.0D).uv(0F, 0F).color(r, g, b, alpha).endVertex();
    }

    /**
     * Play a sound with given pitch and volume
     *
     * @param soundEvent the sound event
     * @param pitch the pitch
     * @param volume the volume
     * @param soundCategory the sound category
     * @param attenuationLinear if the sound attenuation is linear
     * @see SimpleSoundInstance
     */
    public static void playSound(SoundEvent soundEvent, float pitch, float volume, SoundSource soundCategory, boolean attenuationLinear)
    {
        minecraftSupplier.get().getSoundManager().play(new SimpleSoundInstance(soundEvent.getLocation(), soundCategory, volume, pitch, false, 0, attenuationLinear ? SoundInstance.Attenuation.LINEAR : SoundInstance.Attenuation.NONE, 0.0D, 0.0D, 0.0D, true));
    }

    /**
     * Get the biggest string width
     *
     * @param strings the strings
     * @return the biggest string width
     */
    public static int getBiggestStringWidth(List<String> strings)
    {
        int biggest = 0;

        for(String s : strings)
        {
            int width = ClientUtils.width(s);

            if(width > biggest) biggest = width;
        }

        return biggest;
    }

    public static void setBlockRender(Block block, Predicate<RenderType> render)
    {
        ItemBlockRenderTypes.setRenderLayer(block, render);
    }

    public static void setDefaultBlockRender(Block block)
    {
        setBlockRender(block, DEFAULT_BLOCK_RENDER);
    }
}
