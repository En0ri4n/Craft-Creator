package fr.eno.craftcreator.utils;

import fr.eno.craftcreator.CraftCreator;
import fr.eno.craftcreator.utils.CustomRunnable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommonUtils
{
    /**
     * Check if the tile entity is an instance of the tile entity class
     * @param tileEntity The tile entity
     * @param tileEntityClass The tile entity class
     * @return True if the tile entity is an instance of the tile entity class, false otherwise
     */
    public static boolean isBlockEntity(TileEntity tileEntity, Class<? extends TileEntity> tileEntityClass)
    {
        return tileEntity != null && tileEntity.getClass().equals(tileEntityClass);
    }

    /**
     * Return the recipe type with the given resource location
     *
     * @param resourceLocation the resource location
     * @return the recipe type
     */
    @SuppressWarnings("unchecked")
    public static <T extends IRecipe<C>, C extends IInventory> IRecipeType<T> getRecipeTypeByName(ResourceLocation resourceLocation)
    {
        return (IRecipeType<T>) Registry.RECIPE_TYPE.getOptional(resourceLocation).orElse(null);
    }

    /**
     * Return the ID of the recipe type
     * @param recipeType the recipe type
     * @return the ID of the recipe type
     */
    public static ResourceLocation getRecipeTypeName(IRecipeType<?> recipeType)
    {
        return Registry.RECIPE_TYPE.getKey(recipeType);
    }

    @Nullable
    public static Item getItem(ResourceLocation resourceLocation)
    {
        return ForgeRegistries.ITEMS.getValue(resourceLocation);
    }

    @Nullable
    public static Fluid getFluid(ResourceLocation resourceLocation)
    {
        return ForgeRegistries.FLUIDS.getValue(resourceLocation);
    }

    public static ITag<Item> getTag(ResourceLocation resourceLocation)
    {
        return ItemTags.getAllTags().getTagOrEmpty(resourceLocation);
    }

    /**
     * Check if the two entries are equals
     *
     * @param entry1 the first entry
     * @param entry2 the second entry
     * @return true if the two entries are equals
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

    public static void sendMessage(PlayerEntity player, ITextComponent message)
    {
        player.sendMessage(message, player.getUUID());
    }

    public static void sendMessageToServer(ITextComponent message)
    {
        CraftCreator.SERVER.getPlayerList().getPlayers().forEach(player -> player.sendMessage(message, player.getUUID()));
    }

    /**
     * Execute a task on the client thread
     *
     * @param clientTask the task to execute
     */
    public static void clientTask(CustomRunnable clientTask)
    {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> clientTask);
    }

    /**
     * Parse a string to a resource location<br>
     * If the string can't be parsed, return null
     *
     * @param location the string to parse
     * @return the resource location
     *
     * @see ResourceLocation#tryParse(String)
     */
    public static ResourceLocation parse(String location)
    {
        return ResourceLocation.tryParse(location);
    }

    /**
     * Create a resource location with the specified namespace and path
     * @param namespace the namespace
     * @param path the path
     * @return the resource location created
     */
    public static ResourceLocation parse(String namespace, String path)
    {
        return new ResourceLocation(namespace, path);
    }

    /**
     * Split a string with the specified size
     *
     * @param text the string to split
     * @param size the size to split
     * @return a non-null list with the split string
     */
    public static List<String> splitToListWithSize(String text, int size)
    {
        List<String> parts = new ArrayList<>();

        int length = text.length();

        for(int i = 0; i < length; i += size)
        {
            parts.add(text.substring(i, Math.min(length, i + size)));
        }

        return parts;
    }
}
