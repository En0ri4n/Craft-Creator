package fr.eno.craftcreator.client.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import fr.eno.craftcreator.utils.CommonUtils;
import fr.eno.craftcreator.client.screen.widgets.SimpleListWidget;
import fr.eno.craftcreator.init.InitPackets;
import fr.eno.craftcreator.recipes.kubejs.KubeJSHelper;
import fr.eno.craftcreator.recipes.kubejs.KubeJSModifiedRecipe;
import fr.eno.craftcreator.client.screen.RecipeManagerScreen;
import fr.eno.craftcreator.utils.CustomRunnable;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * class contains some useful methods for the client.
 */
public class ClientUtils
{
    public static final KeyBinding KEY_OPEN_RECIPES_MENU = new KeyBinding("key.craftcreator.open_recipes_menu", GLFW.GLFW_KEY_K, "key.categories.craft_creator");
    public static final KeyBinding KEY_OPEN_TUTORIAL = new KeyBinding("key.craftcreator.open_tutorial", GLFW.GLFW_KEY_J, "key.categories.craft_creator");

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
    public static World getClientLevel()
    {
        return minecraftSupplier.get().level;
    }

    /**
     * @return the minecraft font
     * @see Minecraft#font
     */
    public static FontRenderer getFontRenderer()
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
    public static PlayerEntity getClientPlayer()
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
     *
     * @see net.minecraft.client.gui.ScreenManager#register(ContainerType, ScreenManager.IScreenFactory)
     */
    public static <M extends Container, U extends Screen & IHasContainer<M>> void registerScreen(ContainerType<? extends M> container, ScreenManager.IScreenFactory<M, U> screenConstructor)
    {
        ScreenManager.register(container, screenConstructor);
    }

    /**
     * Bind a texture in the texture manager
     *
     * @param texture the texture to bind
     * @see net.minecraft.client.renderer.texture.TextureManager#bind(ResourceLocation)
     */
    public static void bindTexture(ResourceLocation texture)
    {
        minecraftSupplier.get().getTextureManager().bind(texture);
    }

    /**
     * calls {@link net.minecraft.client.gui.FontRenderer#width(String)}
     *
     * @param str The string
     * @return width of the given string
     * @see net.minecraft.client.gui.FontRenderer#width(String)
     */
    public static int width(String str)
    {
        return getFontRenderer().width(str);
    }

    /**
     * Calls {@link RenderSystem#color4f(float, float, float, float)}
     */
    @SuppressWarnings("deprecation")
    public static void color4f(float r, float g, float b, float a)
    {
        RenderSystem.color4f(r, g, b, a);
    }

    /**
     * Render a quad with the specified color<br>
     * <b>Warning:</b> This method must be called between {@link BufferBuilder#begin(int, VertexFormat)} and {@link BufferBuilder#end()}
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
     * @param soundEvent        the sound event
     * @param pitch             the pitch
     * @param volume            the volume
     * @param soundCategory     the sound category
     * @param attenuationLinear if the sound attenuation is linear
     * @see net.minecraft.client.audio.SimpleSound
     */
    public static void playSound(SoundEvent soundEvent, float pitch, float volume, SoundCategory soundCategory, boolean attenuationLinear)
    {
        minecraftSupplier.get().getSoundManager().play(new SimpleSound(soundEvent.getLocation(), soundCategory, volume, pitch, false, 0, attenuationLinear ? ISound.AttenuationType.LINEAR : ISound.AttenuationType.NONE, 0.0D, 0.0D, 0.0D, true));
    }

    /**
     * Get the biggest string width
     *
     * @param strings list of string
     * @return the biggest string width
     */
    public static int getBiggestStringWidth(List<String> strings)
    {
        return strings.stream().mapToInt(ClientUtils::width).max().orElse(0);
    }

    public static void setBlockRender(Block block, Predicate<RenderType> render)
    {
        RenderTypeLookup.setRenderLayer(block, render);
    }

    public static void setDefaultBlockRender(Block block)
    {
        setBlockRender(block, DEFAULT_BLOCK_RENDER);
    }

    public static void addToList(InitPackets.RecipeList recipeList, String recipeId, String serializedRecipe)
    {
        CommonUtils.clientTask(CustomRunnable.of(() ->
        {
            if(ClientUtils.getCurrentScreen() instanceof RecipeManagerScreen)
            {
                RecipeManagerScreen screen = (RecipeManagerScreen) ClientUtils.getCurrentScreen();
                switch(recipeList)
                {
                    case ADDED_RECIPES:
                        final Gson gson = new GsonBuilder().setLenient().create();
                        JsonObject jsonObject = gson.fromJson(serializedRecipe, JsonObject.class);
                        IRecipeSerializer<IRecipe<IInventory>> serializer = KubeJSHelper.getSerializer(CommonUtils.parse(jsonObject.get("type").getAsString()));
                        ResourceLocation id = CommonUtils.parse(recipeId);
                        if(id == null)
                            id = new ResourceLocation("lalal", "recipe"); // Not supposed to happen
                        screen.addToList(recipeList, new SimpleListWidget.RecipeEntry(serializer.fromJson(id, jsonObject)));
                        break;
                    case MODIFIED_RECIPES:
                        screen.addToList(recipeList, new SimpleListWidget.ModifiedRecipeEntry(KubeJSModifiedRecipe.deserialize(serializedRecipe)));
                        break;
                }
            }
        }));
    }
}
