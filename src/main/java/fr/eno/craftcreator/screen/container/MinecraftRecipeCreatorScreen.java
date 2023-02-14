package fr.eno.craftcreator.screen.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.base.ModRecipeCreator;
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
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import java.util.List;

public class MinecraftRecipeCreatorScreen extends MultiScreenModRecipeCreatorScreen<MinecraftRecipeCreatorContainer>
{
    private BooleanButton craftTypeButton;

    public MinecraftRecipeCreatorScreen(MinecraftRecipeCreatorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn, screenContainer.getTile().getBlockPos());
        isVanillaScreen = true;
    }

    @Override
    protected void init()
    {
        super.init();
        InitPackets.NetworkHelper.sendToServer(new RetrieveRecipeCreatorTileDataServerPacket("shaped", this.getMenu().getTile().getBlockPos(), InitPackets.PacketDataType.BOOLEAN));
        this.addButton(craftTypeButton = new BooleanButton("craftType", leftPos + 100, topPos + 60, 68, 20, true, NullPressable.get()));

        addNumberField(leftPos + 8, topPos + 30, 40, 0.1D, 1);
        addNumberField(leftPos + 8, topPos + 60, 40, 200, 1);

        updateScreen();
    }

    @Override
    protected List<ModRecipeCreator> getAvailableRecipesCreator()
    {
        return ModRecipeCreator.getRecipeCreatorScreens(SupportedMods.MINECRAFT);
    }

    @Override
    public void setData(String dataName, Object data)
    {
        super.setData(dataName, data);
        if(dataName.equals("shaped")) this.setShaped((boolean) data);
    }

    @Override
    protected void updateScreen()
    {
        super.updateScreen();

        this.craftTypeButton.visible = false;

        switch(getCurrentRecipe())
        {
            case CRAFTING_TABLE:
                this.craftTypeButton.visible = true;
                setExecuteButtonPos(leftPos + 86, topPos + 33);
                executeButton.setWidth(30);
                break;
            case STONECUTTER:
                setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 26, this.topPos + 31);
                executeButton.setWidth(42);
                break;
            case SMITHING_TABLE:
                setExecuteButtonPos(this.leftPos + this.imageWidth / 2 + 7, this.topPos + 45);
                executeButton.setWidth(36);
                break;
            case FURNACE_BLASTING:
            case FURNACE_SMOKING:
            case FURNACE_SMELTING:
            case CAMPFIRE_COOKING:
                setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 14, this.topPos + 33);
                executeButton.setWidth(35);
                showDataField(0, 1);
                setDataFieldPos(0, leftPos + 8, topPos + 30);
                setDataFieldPos(1, leftPos + 8, topPos + 60);
                break;
        }
    }

    @Override
    protected RecipeInfos getRecipeInfos()
    {
        super.getRecipeInfos();
        switch(getCurrentRecipe())
        {
            case CRAFTING_TABLE:
                recipeInfos.addParameter(new RecipeInfos.RecipeParameterBoolean(RecipeInfos.Parameters.SHAPED, this.craftTypeButton.isOn()));
                break;
            case FURNACE_BLASTING:
            case FURNACE_SMELTING:
            case FURNACE_SMOKING:
            case CAMPFIRE_COOKING:
                recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber(RecipeInfos.Parameters.EXPERIENCE, getDataField(0).getDoubleValue()));
                recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber(RecipeInfos.Parameters.COOKING_TIME, getDataField(1).getIntValue()));
                break;
        }
        return this.recipeInfos;
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        switch(getCurrentRecipe())
        {
            case CRAFTING_TABLE:
                craftTypeButton.render(matrixStack, mouseX, mouseY, partialTicks);
                break;
            case FURNACE_BLASTING:
            case FURNACE_SMELTING:
            case FURNACE_SMOKING:
            case CAMPFIRE_COOKING:
                renderDataFieldAndTitle(0, References.getTranslate("screen.minecraft_recipe_creator_screen.field.experience"), matrixStack, mouseX, mouseY, partialTicks);
                renderDataFieldAndTitle(1, References.getTranslate("screen.minecraft_recipe_creator_screen.field.cooking_time"), matrixStack, mouseX, mouseY, partialTicks);
                // Only for >> FLAMES <<
                ClientUtils.bindTexture(getCurrentRecipe().getGuiTexture());
                blit(matrixStack, this.leftPos + 57, this.topPos + 37, 176, 0, 14, 14, 256, 256);
                break;
        }

        renderTooltip(matrixStack, mouseX, mouseY);
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

    @Override
    protected Item getRecipeIcon(ModRecipeCreator modRecipeCreator)
    {
        switch(modRecipeCreator)
        {
            case CRAFTING_TABLE:
                return Items.CRAFTING_TABLE;
            case STONECUTTER:
                return Items.STONECUTTER;
            case SMITHING_TABLE:
                return Items.SMITHING_TABLE;
            case FURNACE_BLASTING:
                return Items.BLAST_FURNACE;
            case FURNACE_SMOKING:
                return Items.SMOKER;
            case FURNACE_SMELTING:
                return Items.FURNACE;
            case CAMPFIRE_COOKING:
                return Items.CAMPFIRE;
            default:
                return Items.COMMAND_BLOCK;
        }
    }

    public void setShaped(boolean isShaped)
    {
        this.craftTypeButton.setOn(isShaped);
    }

    @Override
    protected void updateServerData()
    {
        super.updateServerData();

        InitPackets.NetworkHelper.sendToServer(new UpdateRecipeCreatorTileDataServerPacket("shaped", this.getMenu().getTile().getBlockPos(), InitPackets.PacketDataType.BOOLEAN, this.craftTypeButton.isOn()));
    }
}