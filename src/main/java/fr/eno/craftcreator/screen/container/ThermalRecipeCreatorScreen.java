package fr.eno.craftcreator.screen.container;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.container.ThermalRecipeCreatorContainer;
import fr.eno.craftcreator.kubejs.utils.RecipeInfos;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

public class ThermalRecipeCreatorScreen extends MultiScreenModRecipeCreatorScreen<ThermalRecipeCreatorContainer>
{
    public ThermalRecipeCreatorScreen(ThermalRecipeCreatorContainer screenContainer, Inventory inv, Component titleIn)
    {
        super(screenContainer, inv, titleIn, screenContainer.getTile().getBlockPos());
    }

    @Override
    protected void init()
    {
        super.init();

        addTextField( leftPos + imageWidth - 44, topPos + imageHeight / 2, 35, 10, -1);
        addTextField( leftPos + imageWidth - 44, topPos + imageHeight / 2, 35, 10, -1);
        addTextField( leftPos + imageWidth - 44, topPos + imageHeight / 2, 35, 10, -1);
        addTextField( leftPos + imageWidth - 44, topPos + imageHeight / 2, 35, 10, -1);
        addTextField( leftPos + imageWidth - 44, topPos + imageHeight / 2, 35, 10, -1);

        updateScreen();
    }

    @Override
    protected RecipeInfos getRecipeInfos()
    {
        switch(getCurrentRecipe())
        {
            case TREE_EXTRACTOR -> this.recipeInfos.addParameter(new RecipeInfos.RecipeParameterInteger("resin_amount", getTextField(0).getValue()));
            case PULVERIZER ->
            {
                this.recipeInfos.addParameter(new RecipeInfos.RecipeParameterDouble("experience", getTextField(0).getValue()));
                for(int i = 0; i < 4; i++)
                    this.recipeInfos.addParameter(new RecipeInfos.RecipeParameterDouble("chance_" + i, getTextField(i + 1).getValue()));
            }
            case SAWMILL ->
            {
                this.recipeInfos.addParameter(new RecipeInfos.RecipeParameterDouble("energy", getTextField(0).getValue()));
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

        switch(getCurrentRecipe())
        {
            case TREE_EXTRACTOR ->
            {
                showTextField(0);
                setTextFieldValue(1, 0);
                setTextFieldPos(0, leftPos + imageWidth - 42, topPos + imageHeight / 2 - 36);
                setExecuteButtonPos(this.leftPos + this.imageWidth - 45, this.topPos + this.imageHeight / 2 - 23);
            }
            case SAWMILL, PULVERIZER ->
            {
                showTextField(0, 1, 2, 3, 4);
                setTextFieldValue(1D, 0, 1, 2, 3, 4);
                setTextFieldPos(0, leftPos + 22, topPos + imageHeight / 2 - 16);
                setTextFieldPos(1, leftPos + imageWidth / 4 * 3 - 8, topPos + 12);
                setTextFieldPos(2, leftPos + imageWidth / 4 * 3 - 8, topPos + 12 + 19);
                setTextFieldPos(3, leftPos + imageWidth / 4 * 3 - 8, topPos + 12 + 19 + 19);
                setTextFieldPos(4, leftPos + imageWidth / 4 * 3 - 8, topPos + 12 + 19 + 19 + 19);
                setExecuteButtonPos(this.leftPos + this.imageWidth / 4 + 5, this.topPos + 33);
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

                Slot slot = this.getMenu().slots.get(SlotHelper.TREE_EXTRACTOR_SLOTS.get(SlotHelper.TREE_EXTRACTOR_SLOTS.size() - 1).getIndex());
                Screen.drawString(matrixStack, this.font, "Resin :", this.leftPos + slot.x, this.topPos + slot.y - font.lineHeight - 2, 0xFFFFFFFF);
            }
            case PULVERIZER ->
            {
                this.minecraft.getItemRenderer().renderAndDecorateFakeItem(new ItemStack(ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse("thermal:machine_pulverizer"))), this.leftPos + this.imageWidth / 2 - 27, this.topPos + 13);
                renderTextfieldTitle(0, "Exp.", matrixStack);
                renderTextfieldTitle(1, "Chances", matrixStack);
            }
            case SAWMILL ->
            {
                this.minecraft.getItemRenderer().renderAndDecorateFakeItem(new ItemStack(ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse("thermal:machine_sawmill"))), this.leftPos + this.imageWidth / 2 - 27, this.topPos + 13);
                renderTextfieldTitle(0, "Energy", matrixStack);
                renderTextfieldTitle(1, "Chances", matrixStack);
            }
        }

        this.renderTooltip(matrixStack, mouseX, mouseY);
    }
}
