package fr.eno.craftcreator.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.api.ScreenUtils;
import fr.eno.craftcreator.base.ModRecipeCreatorDispatcher;
import fr.eno.craftcreator.recipes.base.ModRecipeSerializer;
import fr.eno.craftcreator.recipes.kubejs.KubeJSModifiedRecipe;
import fr.eno.craftcreator.screen.widgets.SimpleListWidget;
import fr.eno.craftcreator.screen.widgets.SimpleTextFieldWidget;
import fr.eno.craftcreator.screen.widgets.buttons.SimpleButton;
import fr.eno.craftcreator.screen.widgets.buttons.SimpleCheckBox;
import fr.eno.craftcreator.utils.EntryHelper;
import fr.eno.craftcreator.utils.Utils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemoveRecipeManagerScreen extends ListScreen
{
    private static final ResourceLocation GUI_TEXTURE = References.getLoc("textures/gui/container/gui_background.png");
    private SimpleCheckBox hasInputItemBox;
    private SimpleCheckBox hasOutputItemBox;
    private SimpleCheckBox hasModBox;
    private SimpleCheckBox hasTypeBox;
    private SimpleCheckBox hasIdBox;

    private TextFieldWidget itemInputButton;
    private TextFieldWidget itemOutputButton;
    private TextFieldWidget modButton;
    private TextFieldWidget typeButton;
    private TextFieldWidget idButton;

    private final Map<ModRecipeSerializer.RecipeDescriptors, String> recipeDescriptors;

    public RemoveRecipeManagerScreen()
    {
        super(References.getTranslate("screen.remove_manager.title"));
        this.recipeDescriptors = new HashMap<>();
    }

    @Override
    protected void init()
    {
        this.lists.clear();
        int i = 0;
        int default_x = 110;
        int default_y = 90;
        int spaceY = 30;
        int spaceX = 87;
        int size = 10;
        int fieldWidth = 250;
        int fieldHeight = 15;
        int heightGap = 2;

        this.addButton(hasInputItemBox = new SimpleCheckBox(default_x, default_y + i++ * spaceY, size, size, References.getTranslate("screen.remove_manager.button.has_input_item"), true));
        this.addButton(hasOutputItemBox = new SimpleCheckBox(default_x, default_y + i++ * spaceY, size, size, References.getTranslate("screen.remove_manager.button.has_output_item"), false));
        this.addButton(hasModBox = new SimpleCheckBox(default_x, default_y + i++ * spaceY, size, size, References.getTranslate("screen.remove_manager.button.has_mod"), false));
        this.addButton(hasTypeBox = new SimpleCheckBox(default_x, default_y + i++ * spaceY, size, size, References.getTranslate("screen.remove_manager.button.has_type"), false));
        this.addButton(hasIdBox = new SimpleCheckBox(default_x, default_y + i * spaceY, size, size, References.getTranslate("screen.remove_manager.button.has_id"), false));

        i = 0;
        this.addButton(itemInputButton = new SimpleTextFieldWidget(new StringTextComponent(""), this.font, default_x + spaceX, default_y + i++ * spaceY - heightGap, fieldWidth, fieldHeight, (simpleEditBox) -> this.updateList(ModRecipeSerializer.RecipeDescriptors.INPUT_ITEM, simpleEditBox.x, simpleEditBox.y, simpleEditBox, EntryHelper.getStringEntryListWith(EntryHelper.getItems()))));
        this.addButton(itemOutputButton = new SimpleTextFieldWidget(new StringTextComponent(""), this.font, default_x + spaceX, default_y + i++ * spaceY - heightGap, fieldWidth, fieldHeight, (simpleEditBox) -> this.updateList(ModRecipeSerializer.RecipeDescriptors.OUTPUT_ITEM, simpleEditBox.x, simpleEditBox.y, simpleEditBox, EntryHelper.getStringEntryListWith(EntryHelper.getItems()))));
        this.addButton(modButton = new SimpleTextFieldWidget(new StringTextComponent(""), this.font, default_x + spaceX, default_y + i++ * spaceY - heightGap, fieldWidth, fieldHeight, (simpleEditBox) -> this.updateList(ModRecipeSerializer.RecipeDescriptors.MOD_ID, simpleEditBox.x, simpleEditBox.y, simpleEditBox, EntryHelper.getStringEntryList(EntryHelper.getMods()))));
        this.addButton(typeButton = new SimpleTextFieldWidget(new StringTextComponent(""), this.font, default_x + spaceX, default_y + i++ * spaceY - heightGap, fieldWidth, fieldHeight, (simpleEditBox) -> this.updateList(ModRecipeSerializer.RecipeDescriptors.RECIPE_TYPE, simpleEditBox.x, simpleEditBox.y, simpleEditBox, EntryHelper.getStringEntryListWith(EntryHelper.getRecipeTypes()))));
        this.addButton(idButton = new SimpleTextFieldWidget(new StringTextComponent(""), this.font, default_x + spaceX, default_y + i * spaceY - heightGap, fieldWidth, fieldHeight, (simpleEditBox) -> this.updateList(ModRecipeSerializer.RecipeDescriptors.RECIPE_ID, simpleEditBox.x, simpleEditBox.y, simpleEditBox, EntryHelper.getStringEntryListWith(EntryHelper.getRecipeIds()))));

        this.addButton(new SimpleButton(References.getTranslate("screen.remove_manager.button.remove"), this.width / 2 - 80, this.height - 85, 160, 20, (button) -> sendRemovedRecipe()));
        this.addButton(new SimpleButton(References.getTranslate("screen.remove_manager.button.back"), this.width - 97, this.height - 35, 80, 20, (button) -> ClientUtils.openScreen(new RecipeManagerScreen())));

        this.addList(new SimpleListWidget(200, 200, 100, 100, 15, 0, 6, new StringTextComponent(""), null, false));
        this.getList(0).setVisible(false);

        checkBoxes();
    }

    private void sendRemovedRecipe()
    {
        if(!hasInputItemBox.selected()) this.recipeDescriptors.remove(ModRecipeSerializer.RecipeDescriptors.INPUT_ITEM);
        if(!hasOutputItemBox.selected()) this.recipeDescriptors.remove(ModRecipeSerializer.RecipeDescriptors.OUTPUT_ITEM);
        if(!hasModBox.selected()) this.recipeDescriptors.remove(ModRecipeSerializer.RecipeDescriptors.MOD_ID);
        if(!hasTypeBox.selected()) this.recipeDescriptors.remove(ModRecipeSerializer.RecipeDescriptors.RECIPE_TYPE);
        if(!hasIdBox.selected()) this.recipeDescriptors.remove(ModRecipeSerializer.RecipeDescriptors.RECIPE_ID);

        if(!this.recipeDescriptors.isEmpty()) ModRecipeCreatorDispatcher.getSeralizer(getModId()).addModifiedRecipe(new KubeJSModifiedRecipe(KubeJSModifiedRecipe.KubeJSModifiedRecipeType.REMOVED, recipeDescriptors));
    }

    private String getModId()
    {
        for(Map.Entry<ModRecipeSerializer.RecipeDescriptors, String> entry : this.recipeDescriptors.entrySet())
        {
            try
            {
                ResourceLocation location = ClientUtils.parse(entry.getValue());
                return Utils.notNull(location).getNamespace();
            }
            catch(Exception ignored)
            {
                return entry.getValue();
            }
        }

        return "";
    }

    private void updateList(ModRecipeSerializer.RecipeDescriptors removeTag, int x, int y, SimpleTextFieldWidget textFieldWidget, List<SimpleListWidget.Entry> entries)
    {
        this.getList(0).setCoordinates(x, y + 15);
        this.getList(0).setSize(textFieldWidget.getWidth(), 100);
        this.getList(0).setVisible(true);
        this.getList(0).setOnSelectedChange((entry) ->
        {
            this.recipeDescriptors.put(removeTag, entry.toString());
            textFieldWidget.setValue(entry.toString());
            this.getList(0).setVisible(false);
        });

        this.setEntries(0, ClientUtils.copyPartialMatches(textFieldWidget.getValue(), entries), true);
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        renderBackground(matrixStack);
        ClientUtils.bindTexture(GUI_TEXTURE);
        ScreenUtils.renderSizedTexture(matrixStack, 4, 100, 50, width - 200, height - 100, 0, 0, 16, 16, 16);

        super.render(matrixStack, mouseX, mouseY, partialTicks);

        Screen.drawCenteredString(matrixStack, this.font, this.title.getString(), this.width / 2, 60, 0xB3b3af);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        super.mouseClicked(mouseX, mouseY, button);

        checkBoxes();

        return true;
    }

    private void checkBoxes()
    {
        if(this.itemInputButton.visible != this.hasInputItemBox.selected() || this.itemOutputButton.visible != this.hasOutputItemBox.selected() || this.modButton.visible != this.hasModBox.selected() || this.typeButton.visible != this.hasTypeBox.selected() || this.idButton.visible != this.hasIdBox.selected())
        {
            this.getList(0).setVisible(false);
        }

        this.itemInputButton.visible = this.hasInputItemBox.selected();
        this.itemOutputButton.visible = this.hasOutputItemBox.selected();
        this.modButton.visible = this.hasModBox.selected();
        this.typeButton.visible = this.hasTypeBox.selected();
        this.idButton.visible = this.hasIdBox.selected();
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY)
    {
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta)
    {
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
