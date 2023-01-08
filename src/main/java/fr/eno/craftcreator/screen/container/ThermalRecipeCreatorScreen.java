package fr.eno.craftcreator.screen.container;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.container.ThermalRecipeCreatorContainer;
import fr.eno.craftcreator.kubejs.utils.RecipeInfos;
import fr.eno.craftcreator.utils.PairValues;
import fr.eno.craftcreator.utils.PositionnedSlot;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;

public class ThermalRecipeCreatorScreen extends MultiScreenModRecipeCreatorScreen<ThermalRecipeCreatorContainer>
{
    private static final int ENERGY_FIELD = 0;
    private static final int EXPERIENCE_FIELD = 1;
    private static final int CHANCES_FIELD = 2;
    private static final int RESIN_FIELD = 2;

    public ThermalRecipeCreatorScreen(ThermalRecipeCreatorContainer screenContainer, Inventory inv, Component titleIn)
    {
        super(screenContainer, inv, titleIn, screenContainer.getTile().getBlockPos());
        this.guiTextureSize = 384;
        this.imageWidth = 296;
        this.imageHeight = 256;
    }

    @Override
    protected void init()
    {
        super.init();

        addNumberField( leftPos + imageWidth - 44, topPos + imageHeight / 2, 35, 10, -1, 6);

        updateScreen();
    }

    @Override
    public int getArrowXPos(boolean right)
    {
        return right ? super.getArrowXPos(true) - 60 : super.getArrowXPos(false) + 60;
    }

    @Override
    protected RecipeInfos getRecipeInfos()
    {
        this.recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber("experience", getNumberField(EXPERIENCE_FIELD).getDoubleValue()));
        this.recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber("energy", getNumberField(ENERGY_FIELD).getIntValue()));

        switch(getCurrentRecipe())
        {
            case TREE_EXTRACTOR -> this.recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber("resin_amount", getNumberField(0).getIntValue()));
            case PULVERIZER, SAWMILL, SMELTER, INSOLATOR ->
            {
                for(int i = 0; i < 4; i++)
                    this.recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber("chance_" + i, getNumberField(i + 1).getDoubleValue()));
            }
        }

        return this.recipeInfos;
    }

    @Override
    protected void updateScreen()
    {
        super.updateScreen();

        showDataField(ENERGY_FIELD, EXPERIENCE_FIELD);
        setDataField(ENERGY_FIELD, this.leftPos + 8, this.topPos + this.imageHeight / 2 + 23, 73, 16, 100);
        setDataField(EXPERIENCE_FIELD, this.leftPos + this.imageWidth - 73 - 8, this.topPos + this.imageHeight / 2 + 23, 73, 16, 0.1D);
        setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - this.executeButton.getWidth() / 2, this.topPos + this.imageHeight / 2 - this.executeButton.getHeight() / 2 + 22);

        switch(getCurrentRecipe())
        {
            case TREE_EXTRACTOR ->
            {
                showDataField(RESIN_FIELD);
                setDataField(RESIN_FIELD, leftPos + imageWidth / 4 * 3 - 12, topPos + imageHeight / 3 - 13, 55, 16, 25);
            }
            case SAWMILL, PULVERIZER, SMELTER, INSOLATOR ->
            {
                for(int i = 0; i < 4; i++)
                {
                    showDataField(i + CHANCES_FIELD);
                    setDataField(i + CHANCES_FIELD, leftPos + imageWidth / 4 * 3 - 12, topPos + 33 + i * 26, 40, 16, 1D);
                }
            }
        }
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        switch(getCurrentRecipe())
        {
            case TREE_EXTRACTOR ->
            {
                this.renderSlotTitle(0, new TextComponent("Trunk"), matrixStack);
                this.renderSlotTitle(1, new TextComponent("Leaves"), matrixStack);
                renderDataFieldTitle(RESIN_FIELD, References.getTranslate("screen.thermal_recipe_creator.field.resin_amount"), matrixStack);
            }
            case PULVERIZER, SAWMILL, SMELTER, INSOLATOR ->
                    renderDataFieldTitle(CHANCES_FIELD, References.getTranslate("screen.thermal_recipe_creator.field.chances"), matrixStack);
        }

        renderDataFieldTitle(ENERGY_FIELD, References.getTranslate("screen.thermal_recipe_creator.field.energy"), matrixStack);
        renderDataFieldTitle(EXPERIENCE_FIELD, References.getTranslate("screen.thermal_recipe_creator.field.experience"), matrixStack);

        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int pMouseX, int pMouseY)
    {
        super.renderLabels(matrixStack, pMouseX, pMouseY);

        // Render Labels
        MutableComponent inputLabel = References.getTranslate("screen.recipe_creator.label.input").copy().withStyle(ChatFormatting.UNDERLINE);
        Component ouputLabel = References.getTranslate("screen.recipe_creator.label.output");
        Screen.drawString(matrixStack, this.font, inputLabel, this.imageWidth / 4 - font.width(inputLabel) / 2, 8, 0xFFFFFFFF);
        Screen.drawString(matrixStack, this.font, ouputLabel, this.imageWidth / 4 * 3 - font.width(ouputLabel) / 2, 8, 0xFFFFFFFF);
    }

    @Override
    protected List<PositionnedSlot> getTaggeableSlots()
    {
        return SlotHelper.THERMAL_SLOTS;
    }

    @Override
    protected Item getRecipeIcon()
    {
        return switch(getCurrentRecipe())
        {
            case TREE_EXTRACTOR -> ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse("thermal:device_tree_extractor"));
            case PULVERIZER -> ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse("thermal:machine_pulverizer"));
            case SAWMILL -> ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse("thermal:machine_sawmill"));
            case SMELTER -> ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse("thermal:machine_smelter"));
            case INSOLATOR -> ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse("thermal:machine_insolator"));
            default -> Items.AIR;
        };
    }

    @Override
    protected PairValues<Integer, Integer> getIconPos()
    {
        return PairValues.create(this.leftPos + this.imageWidth / 2 - 8, this.topPos + this.imageHeight / 4 + 7);
    }
}
