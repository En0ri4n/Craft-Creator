package fr.eno.craftcreator.api;

import fr.eno.craftcreator.CraftCreator;
import fr.eno.craftcreator.utils.CustomRunnable;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.tags.ITag;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommonUtils
{
    @SuppressWarnings("unchecked")
    public static <T extends Recipe<C>, C extends Container> RecipeType<T> getRecipeTypeByName(ResourceLocation resourceLocation)
    {
        return (RecipeType<T>) Registry.RECIPE_TYPE.getOptional(resourceLocation).orElse(null);
    }

    public static ResourceLocation getRecipeTypeName(RecipeType<?> recipeType)
    {
        return Registry.RECIPE_TYPE.getKey(recipeType);
    }

    @Nullable
    public static Item getItem(ResourceLocation resourceLocation)
    {
        return ForgeRegistries.ITEMS.getValue(resourceLocation);
    }

    // TODO: check if method is suitable
    public static ITag<Item> getTag(ResourceLocation resourceLocation)
    {
        return ForgeRegistries.ITEMS.tags().getTag(ItemTags.create(resourceLocation));
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
    public static MutableComponent createComponentFileOpener(MutableComponent component, File toOpen)
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
    public static MutableComponent createComponentUrlOpener(MutableComponent component, String urlToOpen)
    {
        return component.copy().withStyle((msg) -> msg.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, urlToOpen)));
    }

    public static void sendMessage(Player player, MutableComponent message)
    {
        player.sendMessage(message, player.getUUID());
    }

    public static void sendMessageToServer(MutableComponent message)
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
