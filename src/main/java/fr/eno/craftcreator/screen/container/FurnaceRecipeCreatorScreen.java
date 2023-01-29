package fr.eno.craftcreator.screen.container;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.container.FurnaceRecipeCreatorContainer;
import fr.eno.craftcreator.recipes.utils.RecipeInfos;
import fr.eno.craftcreator.screen.utils.ModRecipeCreator;
import fr.eno.craftcreator.utils.PairValues;
import fr.eno.craftcreator.utils.PositionnedSlot;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class FurnaceRecipeCreatorScreen extends MultiScreenModRecipeCreatorScreen<FurnaceRecipeCreatorContainer>
{
    private final List<ModRecipeCreator> FURNACES = Arrays.asList(ModRecipeCreator.FURNACE_BLASTING, ModRecipeCreator.FURNACE_SMELTING, ModRecipeCreator.FURNACE_SMOKING, ModRecipeCreator.CAMPFIRE_COOKING);

    public FurnaceRecipeCreatorScreen(FurnaceRecipeCreatorContainer screenContainer, Inventory inv, Component titleIn)
    {
        super(screenContainer, inv, titleIn, screenContainer.getTile().getBlockPos());
        isVanillaScreen = true;
    }

    @Override
    protected void init()
    {
        super.init();

        addNumberField(leftPos + 8, topPos + 30, 40, 0.1D, 1);
        addNumberField(leftPos + 8, topPos + 60, 40, 200, 1);
    }

    @Override
    protected void updateScreen()
    {
        super.updateScreen();

        setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 14, this.topPos + 33);
        executeButton.setWidth(35);
        showDataField(0, 1);
    }

    @Override
    protected List<ModRecipeCreator> getAvailableRecipesCreator()
    {
        return FURNACES;
    }

    @Override
    protected RecipeInfos getRecipeInfos()
    {
        super.getRecipeInfos();
        recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber(RecipeInfos.Parameters.EXPERIENCE, getNumberField(0).getDoubleValue()));
        recipeInfos.addParameter(new RecipeInfos.RecipeParameterNumber(RecipeInfos.Parameters.COOKING_TIME, getNumberField(1).getIntValue()));
        return recipeInfos;
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        renderDataFieldAndTitle(0, References.getTranslate("screen.furnace_recipe_creator_screen.experience"), matrixStack, mouseX, mouseY, partialTicks);
        renderDataFieldAndTitle(1, References.getTranslate("screen.furnace_recipe_creator_screen.cooking_time"), matrixStack, mouseX, mouseY, partialTicks);

        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float pPartialTick, int pMouseX, int pMouseY)
    {
        super.renderBg(matrixStack, pPartialTick, pMouseX, pMouseY);

        // Only for >> FLAMES <<
        RenderSystem.setShaderTexture(0, References.getLoc("textures/gui/container/minecraft/furnace_recipe_creator.png"));
        blit(matrixStack, this.leftPos + 57, this.topPos + 37, 176, 0, 14, 14, 256, 256);
    }

    @Override
    protected Item getRecipeIcon()
    {
        return switch(getCurrentRecipe())
                {
                    case FURNACE_BLASTING -> Items.BLAST_FURNACE;
                    case FURNACE_SMOKING -> Items.SMOKER;
                    case CAMPFIRE_COOKING -> Items.CAMPFIRE;
                    default -> Items.FURNACE;
                };
    }

    @Override
    protected PairValues<Integer, Integer> getIconPos()
    {
        return PairValues.create(this.leftPos + imageWidth - 18, topPos + 2);
    }

    @Override
    protected List<PositionnedSlot> getTaggeableSlots()
    {
        return SlotHelper.FURNACE_SLOTS.stream().findFirst().stream().toList();
    }
}