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
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateRecipeCreatorTile extends MultiScreenRecipeCreatorTile
{
    private Map<String, List<JsonObject>> inputs;

    public CreateRecipeCreatorTile(BlockPos pos, BlockState state)
    {
        super(SupportedMods.CREATE, InitTileEntities.CREATE_RECIPE_CREATOR.get(), SlotHelper.CREATE_SLOTS_SIZE, pos, state);
        this.inputs = new HashMap<>();
        SupportedMods.CREATE.getSupportedRecipeTypes().forEach(rl -> inputs.put(rl.getPath(), new ArrayList<>()));
    }

    @Override
    public Object getData(String dataName)
    {
        if(dataName.startsWith("inputs"))
        {
            String recipeType = dataName.split("-")[1];
            return PairValues.create(recipeType, this.inputs.get(recipeType));
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
    }

    @Override
    public void saveAdditional(CompoundTag compoundTag)
    {
        super.saveAdditional(compoundTag);

        CompoundTag storage = new CompoundTag();

        for(ResourceLocation recipeType : SupportedMods.CREATE.getSupportedRecipeTypes())
        {
            CompoundTag recipeTypeNbt = new CompoundTag();
            recipeTypeNbt.putInt("Count", inputs.get(recipeType.getPath()).size());

            int i = 0;
            for(JsonObject inputJson : inputs.get(recipeType.getPath()))
            {
                recipeTypeNbt.putString(recipeType.getPath() + "-" + i++, inputJson.toString());
            }

            storage.put(recipeType.getPath(), recipeTypeNbt);
        }

        compoundTag.put("Storage", storage);
    }

    @Override
    public void load(CompoundTag compound)
    {
        super.load(compound);

        Map<String, List<JsonObject>> map = new HashMap<>();

        CompoundTag storage = compound.getCompound("Storage");

        for(ResourceLocation recipeType : SupportedMods.CREATE.getSupportedRecipeTypes())
        {
            CompoundTag recipeTypeNbt = storage.getCompound(recipeType.getPath());
            int count = recipeTypeNbt.getInt("Count");

            List<JsonObject> jsonList = new ArrayList<>();

            for(int i = 0; i < count; i++)
            {
                jsonList.add(Utils.GSON.fromJson(compound.getString(recipeType.getPath() + "-" + i), JsonObject.class));
            }

            map.put(recipeType.getPath(), jsonList);
        }

        inputs = map;
    }

    @Override
    public Component getDisplayName()
    {
        return References.getTranslate("container.create_recipe_creator.title");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player)
    {
        return new CreateRecipeCreatorContainer(containerId, playerInventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(getBlockPos()));
    }
}
