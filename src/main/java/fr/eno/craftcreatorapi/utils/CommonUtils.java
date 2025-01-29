package fr.eno.craftcreatorapi.utils;

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
import java.util.stream.Stream;

public class CommonUtils
{
    /**
     * Check if the tile entity is an instance of the tile entity class
     * @param tileEntity The tile entity
     * @param tileEntityClass The tile entity class
     * @return True if the tile entity is an instance of the tile entity class, false otherwise
     */
    public static <TE> boolean isBlockEntity(TE tileEntity, Class<? extends TE> tileEntityClass)
    {
        return tileEntity != null && tileEntity.getClass().equals(tileEntityClass);
    }

    /**
     * Return the recipe type with the given resource location
     *
     * @param resourceLocation the resource location
     * @return the recipe type
     */
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
    public static Fluid getFluid(ResourceLocation resourceLocation)
    {
        return ForgeRegistries.FLUIDS.getValue(resourceLocation);
    }

    public static ITag<Item> getTag(ResourceLocation resourceLocation)
    {
        return ItemTags.getAllTags().getTagOrEmpty(resourceLocation);
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
     * @see Identifier#parse(String)
     */
    public static Identifier parse(String location)
    {
        return Identifier.parse(location);
    }

    /**
     * Create a resource location with the specified namespace and path
     * @param namespace the namespace
     * @param path the path
     * @return the resource location created
     */
    public static Identifier parse(String namespace, String path)
    {
        return new Identifier(namespace, path);
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
        for (int i = 0; i < text.length(); i += size)
            parts.add(text.substring(i, Math.min(text.length(), i + size)));
        return parts;
    }
}
