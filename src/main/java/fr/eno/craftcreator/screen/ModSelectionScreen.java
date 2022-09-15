package fr.eno.craftcreator.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.kubejs.utils.DeserializerHelper;
import fr.eno.craftcreator.kubejs.utils.SupportedMods;
import fr.eno.craftcreator.screen.buttons.SimpleButton;
import fr.eno.craftcreator.screen.widgets.SimpleListWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class ModSelectionScreen extends ListScreen
{
    public ModSelectionScreen()
    {
        super(new TextComponent(""));
    }

    @Override
    protected void init()
    {
        this.clearLists();

        this.addList(new SimpleListWidget(minecraft, 10, 10, this.width / 4, this.height - 20, 20, 14, 5, References.getTranslate("screen.mod_selection.list.mod"), null));
        this.getList(0).setEntries(DeserializerHelper.getStringEntryList(SupportedMods.MODS_IDS));

        this.addList(new SimpleListWidget(minecraft, this.width / 4 + 20, this.height / 2, this.width - (this.width / 4 + 20) - 10, this.height / 2 - 10, 20, 14, 5, References.getTranslate("screen.mod_selection.list.recipe_type"), null));

        this.addRenderableWidget(new SimpleButton(References.getTranslate("screen.recipe_manager.button.remove_recipe"), this.width - 123 - 10, this.height - 23, 120, 20, button -> minecraft.setScreen(new RemoveRecipeManagerScreen(this))));
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);

        matrixStack.pushPose();
        float scale = 3F;
        matrixStack.scale(scale, scale, scale);
        Screen.drawCenteredString(matrixStack, minecraft.font, References.getTranslate("screen.mod_selection.infos.modId"), Math.floorDiv((this.width / 4 + 10) + (width - (this.width / 4 + 10)) / 2, (int) scale), (int) (10 / scale), 0xFFFFFF);
        matrixStack.popPose();
        if(this.getList(0).getSelected() != null)
        {
            matrixStack.pushPose();
            scale = scale / 2F;
            matrixStack.scale(scale, scale, scale);
            Screen.drawCenteredString(matrixStack, minecraft.font, References.getTranslate("screen.mod_selection.infos.recipeType"), (int) (((this.width / 4 + 10) + (width - (this.width / 4 + 10)) / 2) / scale), (int) ((this.height / 2D - font.lineHeight * scale - 3) / scale), 0xFFFFFF);
            matrixStack.popPose();
        }

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        boolean bool = super.mouseClicked(mouseX, mouseY, button);

        if(this.getList(0).getSelected() != null)
            this.getList(1).setEntries(DeserializerHelper.getRecipeTypes(this.getModId()));

        if(this.doubleClickCounter > 0 && this.getList(1).getSelected() != null)
            minecraft.setScreen(new RecipeManagerScreen(this.getModId(), this.getRecipeType()));

        return bool;
    }

    private String getModId()
    {
        return ((SimpleListWidget.StringEntry) this.getList(0).getSelected()).getResource();
    }

    private ResourceLocation getRecipeType()
    {
        return ResourceLocation.tryParse(((SimpleListWidget.StringEntry) this.getList(1).getSelected()).getResource());
    }
}
