package fr.eno.craftcreator.screen.container;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.base.ModRecipeCreators;
import fr.eno.craftcreator.base.RecipeCreator;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.container.MinecraftRecipeCreatorContainer;
import fr.eno.craftcreator.container.slot.utils.PositionnedSlot;
import fr.eno.craftcreator.init.InitPackets;
import fr.eno.craftcreator.packets.RetrieveRecipeCreatorTileDataServerPacket;
import fr.eno.craftcreator.packets.UpdateRecipeCreatorTileDataServerPacket;
import fr.eno.craftcreator.recipes.utils.RecipeInfos;
import fr.eno.craftcreator.screen.container.base.MultiScreenModRecipeCreatorScreen;
import fr.eno.craftcreator.screen.widgets.buttons.BooleanButton;
import fr.eno.craftcreator.screen.widgets.buttons.pressable.NullPressable;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;

import static fr.eno.craftcreator.base.ModRecipeCreators.*;

public class MinecraftRecipeCreatorScreen extends MultiScreenModRecipeCreatorScreen<MinecraftRecipeCreatorContainer>
{
    private BooleanButton craftTypeButton;

    public MinecraftRecipeCreatorScreen(MinecraftRecipeCreatorContainer screenContainer, Inventory inv, Component titleIn)
    {
        super(screenContainer, inv, titleIn, screenContainer.getTile().getBlockPos());
        isVanillaScreen = true;
    }

    @Override
    protected void initFields()
    {
        addNumberField(leftPos + 8, topPos + 30, 40, 0.1D, 1);
        addNumberField(leftPos + 8, topPos + 60, 40, 200, 1);
    }

    @Override
    protected void initWidgets()
    {
        this.addRenderableWidget(craftTypeButton = new BooleanButton("craftType", leftPos + 100, topPos + 60, 68, 20, true, NullPressable.get()));
    }

    @Override
    protected void retrieveExtraData()
    {
        InitPackets.NetworkHelper.sendToServer(new RetrieveRecipeCreatorTileDataServerPacket("shaped", this.getMenu().getTile().getBlockPos(), InitPackets.PacketDataType.BOOLEAN));
    }

    @Override
    protected List<RecipeCreator> getAvailableRecipesCreator()
    {
        return ModRecipeCreators.getRecipeCreatorScreens(SupportedMods.MINECRAFT);
    }

    @Override
    public void setData(String dataName, Object data)
    {
        super.setData(dataName, data);
        if(dataName.equals("shaped")) this.setShaped((boolean) data);
    }

    @Override
    protected void updateGui()
    {
        this.craftTypeButton.visible = false;

        RecipeCreator currentRecipe = getCurrentRecipe();

        if(currentRecipe.is(CRAFTING_TABLE))
        {
            this.craftTypeButton.visible = true;
            setExecuteButtonPosAndSize(leftPos + 86, topPos + 33, 30);
        }
        else if(currentRecipe.is(STONECUTTING))
        {
            setExecuteButtonPosAndSize(this.leftPos + this.imageWidth / 2 - 26, this.topPos + 31, 42);
        }
        else if(currentRecipe.is(SMITHING))
        {
            setExecuteButtonPosAndSize(this.leftPos + this.imageWidth / 2 + 7, this.topPos + 45, 36);
        }
        else if(currentRecipe.is(FURNACE_BLASTING, FURNACE_SMOKING, FURNACE_SMELTING, CAMPFIRE_COOKING))
        {
            setExecuteButtonPosAndSize(this.leftPos + this.imageWidth / 2 - 14, this.topPos + 33, 35);
            showDataField(0, 1);
            setDataFieldPos(0, leftPos + 8, topPos + 30);
            setDataFieldPos(1, leftPos + 8, topPos + 60);
        }
    }

    @Override
    protected RecipeInfos getExtraRecipeInfos(RecipeInfos recipeInfos)
    {
        RecipeCreator currentRecipe = getCurrentRecipe();

        if(currentRecipe.is(CRAFTING_TABLE))
        {
            recipeInfos.addParameter(new RecipeInfos.RecipeParameterBoolean(RecipeInfos.Parameters.SHAPED, this.craftTypeButton.isOn()));
        }
        else if(currentRecipe.is(FURNACE_BLASTING, FURNACE_SMELTING, FURNACE_SMOKING, CAMPFIRE_COOKING))
        {
            recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber(RecipeInfos.Parameters.EXPERIENCE, getDataField(0).getDoubleValue(), true));
            recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber(RecipeInfos.Parameters.COOKING_TIME, getDataField(1).getIntValue(), false));
        }

        return recipeInfos;
    }

    @Override
    public void renderGui(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        RecipeCreator currentRecipe = getCurrentRecipe();

        if(currentRecipe.is(CRAFTING_TABLE))
        {
            craftTypeButton.render(matrixStack, mouseX, mouseY, partialTicks);
        }
        else if(currentRecipe.is(FURNACE_BLASTING, FURNACE_SMELTING, FURNACE_SMOKING, CAMPFIRE_COOKING))
        {
            renderDataFieldAndTitle(0, References.getTranslate("screen.minecraft_recipe_creator_screen.field.experience"), matrixStack, mouseX, mouseY, partialTicks);
            renderDataFieldAndTitle(1, References.getTranslate("screen.minecraft_recipe_creator_screen.field.cooking_time"), matrixStack, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    protected List<PositionnedSlot> getTaggableSlots()
    {
        return SlotHelper.MINECRAFT_SLOTS_INPUT;
    }

    @Override
    protected List<PositionnedSlot> getNbtTaggableSlots()
    {
        return SlotHelper.CRAFTING_TABLE_SLOTS_OUTPUT;
    }

    public void setShaped(boolean isShaped)
    {
        this.craftTypeButton.setOn(isShaped);
    }

    @Override
    protected void updateExtraServerData()
    {
        InitPackets.NetworkHelper.sendToServer(new UpdateRecipeCreatorTileDataServerPacket("shaped", this.getMenu().getTile().getBlockPos(), InitPackets.PacketDataType.BOOLEAN, this.craftTypeButton.isOn()));
    }
}