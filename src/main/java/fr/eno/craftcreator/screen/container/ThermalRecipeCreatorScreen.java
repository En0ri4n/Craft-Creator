package fr.eno.craftcreator.screen.container;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.container.ThermalRecipeCreatorContainer;
import fr.eno.craftcreator.kubejs.utils.RecipeInfos;
import fr.eno.craftcreator.utils.PairValue;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

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

        addTextField( leftPos + imageWidth - 44, topPos + imageHeight / 2, 35, 10, -1, 6);

        updateScreen();
    }

    @Override
    public int getArrowXPos(boolean right)
    {
        return right ? super.getArrowXPos(right) - 60 : super.getArrowXPos(right) + 60;
    }

    @Override
    protected RecipeInfos getRecipeInfos()
    {
        this.recipeInfos.addParameter(new RecipeInfos.RecipeParameterDouble("experience", getTextField(EXPERIENCE_FIELD).getValue()));
        this.recipeInfos.addParameter(new RecipeInfos.RecipeParameterDouble("energy", getTextField(ENERGY_FIELD).getValue()));

        switch(getCurrentRecipe())
        {
            case TREE_EXTRACTOR -> this.recipeInfos.addParameter(new RecipeInfos.RecipeParameterInteger("resin_amount", getTextField(0).getValue()));
            case PULVERIZER, SAWMILL ->
            {
                for(int i = 0; i < 4; i++)
                    this.recipeInfos.addParameter(new RecipeInfos.RecipeParameterDouble("chance_" + i, getTextField(i + 1).getValue()));
            }
        }

        return this.recipeInfos;
    }

    @Override
    protected void updateScreen()
    {
        super.updateScreen();

        showTextField(ENERGY_FIELD, EXPERIENCE_FIELD);
        setTextField(ENERGY_FIELD, this.leftPos + 8, this.topPos + this.imageHeight / 2 + 23, 73, 16, 100);
        setTextField(EXPERIENCE_FIELD, this.leftPos + this.imageWidth - 73 - 8, this.topPos + this.imageHeight / 2 + 23, 73, 16, 0.1D);
        setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - this.executeButton.getWidth() / 2, this.topPos + this.imageHeight / 2 - this.executeButton.getHeight() / 2 + 22);

        switch(getCurrentRecipe())
        {
            case TREE_EXTRACTOR ->
            {
                showTextField(RESIN_FIELD);
                setTextField(RESIN_FIELD, leftPos + imageWidth / 4 * 3 - 12, topPos + imageHeight / 3 - 13, 55, 16, 25);
            }
            case SAWMILL, PULVERIZER, SMELTER ->
            {
                for(int i = 0; i < 4; i++)
                {
                    showTextField(i + CHANCES_FIELD);
                    setTextField(i + CHANCES_FIELD, leftPos + imageWidth / 4 * 3 - 12, topPos + 33 + i * 26, 40, 16, 1D);
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
                ItemStack trunkItem = this.getMenu().slots.get(SlotHelper.TREE_EXTRACTOR_SLOTS.get(0).getIndex()).hasItem() ? this.getMenu().slots.get(SlotHelper.TREE_EXTRACTOR_SLOTS.get(0).getIndex()).getItem() : ItemStack.EMPTY;
                ItemStack leavesItem = this.getMenu().slots.get(SlotHelper.TREE_EXTRACTOR_SLOTS.get(1).getIndex()).hasItem() ? this.getMenu().slots.get(SlotHelper.TREE_EXTRACTOR_SLOTS.get(1).getIndex()).getItem() : ItemStack.EMPTY;

                for(int i = 0; i < 2; i++)
                    this.minecraft.getItemRenderer().renderAndDecorateFakeItem(trunkItem, this.leftPos + this.imageWidth / 2 - 9, this.topPos + 40 + i * 18);

                for(int line = 0; line < 2; line++)
                    for(int row = 0; row < 3; row ++)
                    {
                        if(line == 1 && row == 1)
                            continue;

                        this.minecraft.getItemRenderer().renderAndDecorateFakeItem(leavesItem, this.leftPos + this.imageWidth / 2 - 9 - 18 + row * 18, this.topPos + 22 + line * 18);
                    }

                // Slot slot = this.getMenu().slots.get(SlotHelper.TREE_EXTRACTOR_SLOTS.get(SlotHelper.TREE_EXTRACTOR_SLOTS.size() - 1).getIndex());
                // Screen.drawString(matrixStack, this.font, "Resin :", this.leftPos + slot.x, this.topPos + slot.y - font.lineHeight - 2, 0xFFFFFFFF);

                renderTextFieldTitle(RESIN_FIELD, References.getTranslate("screen.thermal_recipe_creator.field.resin_amount"), matrixStack);
            }
            case PULVERIZER, SAWMILL, SMELTER ->
            {
                renderTextFieldTitle(CHANCES_FIELD, References.getTranslate("screen.thermal_recipe_creator.field.chances"), matrixStack);
            }
        }

        renderTextFieldTitle(ENERGY_FIELD, References.getTranslate("screen.thermal_recipe_creator.field.energy"), matrixStack);
        renderTextFieldTitle(EXPERIENCE_FIELD, References.getTranslate("screen.thermal_recipe_creator.field.experience"), matrixStack);

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
    protected Item getRecipeIcon()
    {
        return switch(getCurrentRecipe())
        {
            case TREE_EXTRACTOR -> ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse("thermal:device_tree_extractor"));
            case PULVERIZER -> ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse("thermal:machine_pulverizer"));
            case SAWMILL -> ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse("thermal:machine_sawmill"));
            default -> Items.AIR;
        };
    }

    @Override
    protected PairValue<Integer, Integer> getIconPos()
    {
        return PairValue.create(this.leftPos + this.imageWidth / 2 - 8, this.topPos + this.imageHeight / 4 + 7);
    }
}
