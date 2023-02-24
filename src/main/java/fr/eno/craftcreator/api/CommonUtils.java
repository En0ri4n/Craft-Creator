package fr.eno.craftcreator.api;

import fr.eno.craftcreator.utils.CustomRunnable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
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
    @SuppressWarnings("unchecked")
    public static <T extends IRecipe<C>, C extends IInventory> IRecipeType<T> getRecipeTypeByName(ResourceLocation resourceLocation)
    {
        return (IRecipeType<T>) Registry.RECIPE_TYPE.getOptional(resourceLocation).orElse(null);
    }

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
    public static ITag<Item> getTag(ResourceLocation resourceLocation)
    {
        return ItemTags.getAllTags().getTag(resourceLocation);
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

    /**
     * Execute a task on the client thread
     *
     * @param clientTask the task to execute
     */
    public static void clientTask(Runnable clientTask)
    {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> CustomRunnable.of(clientTask));
    }

    /**
     * Execute a task on the server thread
     *
     * @param serverTask the task to execute
     */
    public static void serverTask(Runnable serverTask)
    {
        DistExecutor.safeRunWhenOn(Dist.DEDICATED_SERVER, () -> CustomRunnable.of(serverTask));
    }

    /**
     * Parse a string to a resource location<br>
     * If the string can't be parsed, return the 'bug_empty' resource location
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
