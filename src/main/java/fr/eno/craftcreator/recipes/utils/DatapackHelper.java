package fr.eno.craftcreator.recipes.utils;

import com.google.gson.*;
import fr.eno.craftcreator.CraftCreator;
import fr.eno.craftcreator.utils.CommonUtils;
import fr.eno.craftcreator.recipes.base.ModRecipeSerializer;
import fr.eno.craftcreator.utils.PairValues;
import fr.eno.craftcreator.utils.Utils;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.items.SlotItemHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatapackHelper
{
    // Keys for the shaped recipe
    private static final List<Character> keyList = Arrays.asList('G', 'E', 'X', 'M', 'B', 'D', 'W', 'O', 'A');
    private static final Gson gson = new GsonBuilder().serializeNulls().setLenient().setPrettyPrinting().create();

    /**
     * Serialize ingredients for the shapeless crafting recipe
     * @param slots the slots of the crafting grid
     * @return the ingredients in a JsonArray
     */
    public static JsonArray createShapelessIngredientsJsonArray(List<SlotItemHandler> slots, Map<Integer, ResourceLocation> taggedSlots)
    {
        JsonArray ingredients = new JsonArray();

        for(Slot slot : slots)
        {
            if(!slot.hasItem()) continue;

            JsonObject obj = new JsonObject();

            if(taggedSlots.containsKey(slot.getSlotIndex()))
            {
                obj.addProperty("tag", taggedSlots.get(slot.getSlotIndex()).toString());
                continue;
            }

            obj.addProperty("item", Utils.notNull(slot.getItem().getItem().getRegistryName()).toString());
            ingredients.add(obj);
        }

        return ingredients;
    }

    /**
     * Serialize the pattern for the shaped crafting recipe
     *
     * @param slots the slots of the crafting grid
     * @return the pattern in a JsonArray
     */
    public static JsonArray createPatternJson(List<SlotItemHandler> slots, Map<Integer, ResourceLocation> taggedSlot)
    {
        Map<ResourceLocation, PairValues<Boolean, Character>> patterns = new HashMap<>();
        JsonArray array = new JsonArray();

        pattern(slots, taggedSlot, patterns).forEach(array::add);

        return array;
    }

    private static Map<ResourceLocation, PairValues<Boolean, Character>> createPattern(List<SlotItemHandler> slots, Map<Integer, ResourceLocation> taggedSlot)
    {
        Map<ResourceLocation, PairValues<Boolean, Character>> patterns = new HashMap<>();

        pattern(slots, taggedSlot, patterns);

        return patterns;
    }

    private static List<String> pattern(List<SlotItemHandler> slots, Map<Integer, ResourceLocation> taggedSlot, Map<ResourceLocation, PairValues<Boolean, Character>> patterns)
    {
        StringBuilder pattern = new StringBuilder();

        for(int index = 0; index < 9; index++)
        {
            Slot slot = slots.get(index);

            if(!slot.hasItem())
            {
                pattern.append(' ');
                continue;
            }

            Character key = keyList.get(index);

            if(taggedSlot.containsKey(index))
            {
                if(!patterns.containsKey(taggedSlot.get(index)) || !patterns.get(taggedSlot.get(index)).getFirstValue())
                {
                    patterns.put(taggedSlot.get(index), PairValues.create(true, key));
                }

                pattern.append(patterns.get(taggedSlot.get(index)).getSecondValue());
                continue;
            }

            if(!patterns.containsKey(slot.getItem().getItem().getRegistryName()))
            {
                patterns.put(slot.getItem().getItem().getRegistryName(), PairValues.create(false, key));
            }

            pattern.append(patterns.get(slot.getItem().getItem().getRegistryName()).getSecondValue());
        }

        List<String> patternList = CommonUtils.splitToListWithSize(pattern.toString(), 3);

        for(int i = 2; i >= 0; i -= 2)
        {
            if(patternList.get(i).trim().isEmpty()) patternList.remove(i);
        }

        for(int k = 2; k >= 0; k--)
        {
            if(k == 1) continue;

            boolean removeRow = true;

            for(String s : patternList)
                if(s.charAt(k) != ' ')
                {
                    removeRow = false;
                    break;
                }

            if(removeRow) for(int line = 0; line < patternList.size(); line++)
            {
                StringBuilder sb = new StringBuilder(patternList.get(line));
                sb.deleteCharAt(k);
                patternList.set(line, sb.toString());
            }
        }

        return patternList;
    }

    /**
     * Serialize the keys for the shaped crafting recipe
     * @param slots the slots of the crafting grid
     * @param taggedSlot the tagged slots of the crafting grid
     * @return the keys in a JsonObject
     */
    public static JsonObject createSymbolKeys(List<SlotItemHandler> slots, Map<Integer, ResourceLocation> taggedSlot)
    {
        Map<ResourceLocation, PairValues<Boolean, Character>> patternSet = createPattern(slots, taggedSlot);

        JsonObject symbolListObj = new JsonObject();

        for(Map.Entry<ResourceLocation, PairValues<Boolean, Character>> entry : patternSet.entrySet())
        {
            JsonObject symbolObj = new JsonObject();
            symbolObj.addProperty(entry.getValue().getFirstValue() ? "tag" : "item", entry.getKey().toString());
            symbolListObj.add(String.valueOf(entry.getValue().getSecondValue()), symbolObj);
        }

        return symbolListObj;
    }

    /**
     * Serialize the result for the crafting recipe in the Craft-Creator Datapack
     *
     * @param type       the type of the recipe
     * @param output     the output of the recipe
     * @param recipeJson the JsonObject of the recipe
     * @return the feedback
     */
    public static ModRecipeSerializer.Feedback serializeRecipe(IRecipeType<?> type, ResourceLocation output, JsonObject recipeJson)
    {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(getOutputFile(type, output))))
        {
            gson.toJson(recipeJson, writer);
            return ModRecipeSerializer.Feedback.ADDED;
        }
        catch(JsonIOException | IOException e)
        {
            CraftCreator.LOGGER.error("Can't serialize the recipe in a json file !");
            return ModRecipeSerializer.Feedback.FILE_ERROR;
        }
    }

    /**
     * Get the output file of the recipe in datapack folders
     * @param type the type of the recipe
     * @param output the output of the recipe
     * @return the output file
     */
    private static File getOutputFile(IRecipeType<?> type, ResourceLocation output)
    {
        File directory = new File(FMLLoader.getGamePath().toFile(), "Craft-Creator");
        if(!directory.exists()) directory.mkdirs();

        return new File(directory, output.getPath() + "_from_" + CommonUtils.getRecipeTypeName(type).getPath() + ".json");
    }

    /**
     * Delete the recipe in the Craft-Creator Datapack
     *
     * @param recipe the recipe to delete
     * @return the feedback
     */
    public static ModRecipeSerializer.Feedback deleteRecipe(IRecipe<?> recipe)
    {
        File generatorFolder = new File(FMLLoader.getGamePath().toFile(), "Craft-Creator");
        if(!generatorFolder.exists()) return ModRecipeSerializer.Feedback.DONT_EXISTS;

        for(File file : generatorFolder.listFiles())
            if(file.getName().contains(recipe.getId().getPath() + ".json"))
                file.delete();

        return ModRecipeSerializer.Feedback.REMOVED;
    }
}