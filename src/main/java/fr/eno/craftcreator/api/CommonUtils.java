package fr.eno.craftcreator.api;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.io.File;
import java.util.Objects;
import java.util.function.Predicate;

public class CommonUtils
{
    public static final Predicate<RenderType> DEFAULT_BLOCK_RENDER = (r) -> r == RenderType.cutoutMipped();

    public static void setBlockRender(Block block, Predicate<RenderType> render)
    {
        RenderTypeLookup.setRenderLayer(block, render);
    }

    public static void setDefaultBlockRender(Block block)
    {
        setBlockRender(block, DEFAULT_BLOCK_RENDER);
    }

    public static <T extends IRecipe<C>, C extends IInventory> IRecipeType<T> getRecipeTypeByName(String resourceLocation)
    {
        return getRecipeTypeByName(ClientUtils.parse(resourceLocation));
    }

    @SuppressWarnings("unchecked")
    public static <T extends IRecipe<C>, C extends IInventory> IRecipeType<T> getRecipeTypeByName(ResourceLocation resourceLocation)
    {
        return (IRecipeType<T>) Registry.RECIPE_TYPE.getOptional(resourceLocation).orElse(null);
    }

    public static ResourceLocation getRecipeTypeName(IRecipeType<?> recipeType)
    {
        return Registry.RECIPE_TYPE.getKey(recipeType);
    }

    /**
     * Check if the two entries are equals
     * @param entry1 the first entry
     * @param entry2 the second entry
     * @return true if the two entries are equals
     *
     * @see Objects#equals(Object, Object)
     */
    public static boolean equals(IForgeRegistryEntry<?> entry1, IForgeRegistryEntry<?> entry2)
    {
        return Objects.equals(entry1.getRegistryName(), entry2.getRegistryName());
    }

    /**
     * Create a clickable component which open the specified file
     *
     * @param component the component to display
     * @param toOpen    the file to open
     * @return the clickable component
     */
    public static IFormattableTextComponent createComponentFileOpener(ITextComponent component, File toOpen)
    {
        return component.copy().withStyle((msg) -> msg.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, toOpen.getAbsolutePath())));
    }

    /**
     * Create a clickable component which open the specified url
     *
     * @param component the component to display
     * @param urlToOpen the url to open
     * @return the clickable component
     */
    public static IFormattableTextComponent createComponentUrlOpener(ITextComponent component, String urlToOpen)
    {
        return component.copy().withStyle((msg) -> msg.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, urlToOpen)));
    }
}
