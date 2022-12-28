package fr.eno.craftcreator.screen.container;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.container.ThermalRecipeCreatorContainer;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.Nonnull;

public class ThermalRecipeCreatorScreen extends MultiScreenModRecipeCreatorScreen<ThermalRecipeCreatorContainer>
{
    private static final int RESIN_AMOUNT_FIELD = 0;

    public ThermalRecipeCreatorScreen(ThermalRecipeCreatorContainer screenContainer, Inventory inv, Component titleIn)
    {
        super(screenContainer, inv, titleIn, screenContainer.getTile().getBlockPos());
    }

    @Override
    protected void init()
    {
        super.init();

        textFields.add(new EditBox(minecraft.font, leftPos + imageWidth - 44, topPos + imageHeight / 2, 35, 10, new TextComponent("")));
        textFields.get(RESIN_AMOUNT_FIELD).setValue(String.valueOf(100));

        updateScreen();
    }

    @Override
    protected void updateScreen()
    {
        this.updateSlots();

        switch(getCurrentRecipe())
        {
            case TREE_EXTRACTOR ->
            {
                setTextFieldPos(RESIN_AMOUNT_FIELD, leftPos + imageWidth - 44, topPos + imageHeight / 2 - 16);
                setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 20, this.topPos + 35);
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
                // this.renderTooltip(matrixStack, Component.nullToEmpty("Resin amount"), mouseX, mouseY);
                // renderTextfieldTitle(RESIN_AMOUNT_FIELD, "Resin amount", matrixStack);
            }
        }

        this.renderTooltip(matrixStack, mouseX, mouseY);
    }
}
