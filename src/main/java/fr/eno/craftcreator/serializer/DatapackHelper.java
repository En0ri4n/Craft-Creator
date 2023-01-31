package fr.eno.craftcreator.serializer;

import com.google.gson.*;
import fr.eno.craftcreator.CraftCreator;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.recipes.utils.RecipeFileUtils;
import fr.eno.craftcreator.utils.PairValues;
import fr.eno.craftcreator.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;

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
    public static JsonArray createShapelessIngredientsJsonArray(List<Slot> slots)
    {
        JsonArray ingredients = new JsonArray();

        for(Slot slot : slots)
        {
            if(!slot.getHasStack()) continue;

            JsonObject obj = new JsonObject();
            obj.addProperty("item", slot.getStack().getItem().getRegistryName().getPath());
            ingredients.add(obj);
        }

        return ingredients;
    }

    /**
     * Serialize the pattern for the shaped crafting recipe
     * @param slots the slots of the crafting grid
     * @return the pattern in a JsonArray
     */
    public static JsonArray createPatternJson(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlot)
    {
        Map<ResourceLocation, PairValues<Boolean, Character>> patterns = new HashMap<>();
        JsonArray array = new JsonArray();

        pattern(slots, taggedSlot, patterns).forEach(array::add);

        return array;
    }

    private static Map<ResourceLocation, PairValues<Boolean, Character>> createPattern(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlot)
    {
        Map<ResourceLocation, PairValues<Boolean, Character>> patterns = new HashMap<>();

        pattern(slots, taggedSlot, patterns);

        return patterns;
    }

    private static List<String> pattern(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlot, Map<ResourceLocation, PairValues<Boolean, Character>> patterns)
    {
        StringBuilder pattern = new StringBuilder();

        for(int index = 0; index < 9; index++)
        {
            Slot slot = slots.get(index);

            if(!slot.getHasStack())
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

            if(!patterns.containsKey(slot.getStack().getItem().getRegistryName()))
            {
                patterns.put(slot.getStack().getItem().getRegistryName(), PairValues.create(false, key));
            }

            pattern.append(patterns.get(slot.getStack().getItem().getRegistryName()).getSecondValue());
        }

        List<String> patternList = Utils.splitToListWithSize(pattern.toString(), 3);

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
    public static JsonObject createSymbolKeys(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlot)
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
     * @param type the type of the recipe
     * @param output the output of the recipe
     * @param recipeJson the JsonObject of the recipe
     */
    public static void serializeRecipe(IRecipeType<?> type, ResourceLocation output, JsonObject recipeJson)
    {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(getOutputFile(type, output))))
        {
            gson.toJson(recipeJson, writer);
            ClientUtils.sendClientPlayerMessage(Utils.createComponentFileOpener(References.getTranslate("serializer.success", getOutputFile(type, output).getName()), getOutputFile(type, output)));
        }
        catch(JsonIOException | IOException e)
        {
            CraftCreator.LOGGER.error("Can't serialize the recipe in a json file !");
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
        File directory = new File(Minecraft.getInstance().gameDir, "Craft-Creator");
        if(!directory.exists()) directory.mkdirs();

        return new File(directory, output.getPath() + "_from_" + RecipeFileUtils.getName(type).getPath() + ".json");
    }
}