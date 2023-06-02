package fr.eno.craftcreator.tileentity;

import com.google.gson.JsonObject;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.container.CreateRecipeCreatorContainer;
import fr.eno.craftcreator.init.InitTileEntities;
import fr.eno.craftcreator.tileentity.base.MultiScreenRecipeCreatorTile;
import fr.eno.craftcreator.utils.PairValues;
import fr.eno.craftcreator.utils.SlotHelper;
import fr.eno.craftcreator.utils.Utils;
import io.netty.buffer.Unpooled;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CreateRecipeCreatorTile extends MultiScreenRecipeCreatorTile
{
    private final Map<String, List<JsonObject>> inputs;
    private final Map<String, List<JsonObject>> outputs;

    public CreateRecipeCreatorTile()
    {
        super(SupportedMods.CREATE, InitTileEntities.CREATE_RECIPE_CREATOR.get(), SlotHelper.CREATE_SLOTS_SIZE);
        this.inputs = new HashMap<>();
        this.outputs = new HashMap<>();
        SupportedMods.CREATE.getSupportedRecipeTypes().forEach(rl -> inputs.put(rl.getPath(), new ArrayList<>()));
        SupportedMods.CREATE.getSupportedRecipeTypes().forEach(rl -> outputs.put(rl.getPath(), new ArrayList<>()));
    }

    @Override
    public Object getData(String dataName)
    {
        if(dataName.startsWith("inputs"))
        {
            String recipeType = dataName.split("-")[1];
            return PairValues.create(recipeType, this.inputs.get(recipeType));
        }
        else if(dataName.startsWith("outputs"))
        {
            String recipeType = dataName.split("-")[1];
            return PairValues.create(recipeType, this.outputs.get(recipeType));
        }

        return super.getData(dataName);
    }

    @Override
    public void setData(String dataName, Object data)
    {
        super.setData(dataName, data);

        if(dataName.equals("inputs"))
        {
            PairValues<String, List<JsonObject>> value = (PairValues<String, List<JsonObject>>) data;
            inputs.put(value.getFirstValue(), value.getSecondValue());
        }
        else if(dataName.equals("outputs"))
        {
            PairValues<String, List<JsonObject>> value = (PairValues<String, List<JsonObject>>) data;
            outputs.put(value.getFirstValue(), value.getSecondValue());
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT compoundTag)
    {
        super.save(compoundTag);

        CompoundNBT inputStorage = new CompoundNBT();

        for(ResourceLocation recipeType : SupportedMods.CREATE.getSupportedRecipeTypes())
        {
            CompoundNBT recipeTypeNbt = new CompoundNBT();
            recipeTypeNbt.putInt("Count", inputs.get(recipeType.getPath()).size());

            int i = 0;
            for(JsonObject inputJson : inputs.get(recipeType.getPath()))
            {
                recipeTypeNbt.putString(recipeType.getPath() + "-" + i++, inputJson.toString());
            }

            inputStorage.put(recipeType.getPath(), recipeTypeNbt);
        }

        compoundTag.put("InputStorage", inputStorage);

        CompoundNBT outputStorage = new CompoundNBT();

        for(ResourceLocation recipeType : SupportedMods.CREATE.getSupportedRecipeTypes())
        {
            CompoundNBT recipeTypeNbt = new CompoundNBT();
            recipeTypeNbt.putInt("Count", outputs.get(recipeType.getPath()).size());

            int i = 0;
            for(JsonObject outputJson : outputs.get(recipeType.getPath()))
            {
                recipeTypeNbt.putString(recipeType.getPath() + "-" + i++, outputJson.toString());
            }

            outputStorage.put(recipeType.getPath(), recipeTypeNbt);
        }

        compoundTag.put("OutputStorage", outputStorage);

        return compoundTag;
    }

    @Override
    public void load(BlockState state, CompoundNBT compound)
    {
        super.load(state, compound);

        CompoundNBT inputStorage = compound.getCompound("InputStorage");

        for(ResourceLocation recipeType : SupportedMods.CREATE.getSupportedRecipeTypes())
        {
            CompoundNBT recipeTypeNbt = inputStorage.getCompound(recipeType.getPath());
            int count = recipeTypeNbt.getInt("Count");

            List<JsonObject> jsonList = new ArrayList<>();

            for(String name : recipeTypeNbt.getAllKeys().stream().filter(s -> s.contains(recipeType.getPath())).collect(Collectors.toList()))
            {
                jsonList.add(Utils.GSON.fromJson(recipeTypeNbt.getString(name), JsonObject.class));
            }

            inputs.put(recipeType.getPath(), jsonList);
        }

        CompoundNBT outputStorage = compound.getCompound("OutputStorage");

        for(ResourceLocation recipeType : SupportedMods.CREATE.getSupportedRecipeTypes())
        {
            CompoundNBT recipeTypeNbt = outputStorage.getCompound(recipeType.getPath());
            int count = recipeTypeNbt.getInt("Count");

            List<JsonObject> jsonList = new ArrayList<>();

            for(int i = 0; i < count; i++)
            {
                jsonList.add(Utils.GSON.fromJson(recipeTypeNbt.getString(recipeType.getPath() + "-" + i), JsonObject.class));
            }

            outputs.put(recipeType.getPath(), jsonList);
        }
    }

    public Map<String, List<JsonObject>> getInputs()
    {
        return inputs;
    }

    public Map<String, List<JsonObject>> getOutputs()
    {
        return outputs;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return References.getTranslate("container.create_recipe_creator.title");
    }

    @Nullable
    @Override
    public Container createMenu(int containerId, PlayerInventory playerInventory, PlayerEntity player)
    {
        return new CreateRecipeCreatorContainer(containerId, playerInventory, new PacketBuffer(Unpooled.buffer()).writeBlockPos(getBlockPos()));
    }
}
