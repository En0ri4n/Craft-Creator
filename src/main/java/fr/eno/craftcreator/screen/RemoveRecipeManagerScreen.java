package fr.eno.craftcreator.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.kubejs.jsserializers.ModRecipesJSSerializer;
import fr.eno.craftcreator.kubejs.utils.RecipeFileUtils;
import fr.eno.craftcreator.screen.buttons.SimpleButton;
import fr.eno.craftcreator.screen.buttons.SimpleCheckBox;
import fr.eno.craftcreator.screen.buttons.SimpleTextButton;
import fr.eno.craftcreator.screen.widgets.SimpleListWidget;
import fr.eno.craftcreator.screen.widgets.SimpleTextFieldWidget;
import fr.eno.craftcreator.utils.EntryHelper;
import fr.eno.craftcreator.utils.ModifiedRecipe;
import fr.eno.craftcreator.utils.Utilities;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemoveRecipeManagerScreen extends ChildrenScreen
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

    private final Map<ModRecipesJSSerializer.RecipeDescriptors, String> removeMap;

    public RemoveRecipeManagerScreen(Screen parentIn)
    {
        super(References.getTranslate("screen.remove_manager.title"), parentIn);
        this.removeMap = new HashMap<>();
    }

    @Override
    protected void init()
    {
        this.lists.clear();
        int i = 0;
        int default_y = 50;
        int default_x = 25;
        int space = 20;
        int size = 10;

        this.addButton(hasInputItemBox = new SimpleCheckBox(default_x, default_y + i++ * space, size, size, References.getTranslate("screen.remove_manager.button.has_input_item"), true));
        this.addButton(hasOutputItemBox = new SimpleCheckBox(default_x, default_y + i++ * space, size, size, References.getTranslate("screen.remove_manager.button.has_output_item"), false));
        this.addButton(hasModBox = new SimpleCheckBox(default_x, default_y + i++ * space, size, size, References.getTranslate("screen.remove_manager.button.has_mod"), false));
        this.addButton(hasTypeBox = new SimpleCheckBox(default_x, default_y + i++ * space, size, size, References.getTranslate("screen.remove_manager.button.has_type"), false));
        this.addButton(hasIdBox = new SimpleCheckBox(default_x, default_y + i * space, size, size, References.getTranslate("screen.remove_manager.button.has_id"), false));

        i = 0;
        this.addButton(itemInputButton = new SimpleTextFieldWidget(new StringTextComponent(""), this.font, default_x * 5, default_y + i++ * space, this.width / 2, 15, (simpleTextFieldWidget) ->
        {
            this.updateList(ModRecipesJSSerializer.RecipeDescriptors.INPUT_ITEM, simpleTextFieldWidget.x, simpleTextFieldWidget.y, simpleTextFieldWidget, EntryHelper.getStringEntryListWith(EntryHelper.getItems()));
        }));
        this.addButton(itemOutputButton = new SimpleTextFieldWidget(new StringTextComponent(""), this.font, default_x * 5, default_y + i++ * space, this.width / 2, 15, (simpleTextFieldWidget) ->
        {
            this.updateList(ModRecipesJSSerializer.RecipeDescriptors.OUTPUT_ITEM, simpleTextFieldWidget.x, simpleTextFieldWidget.y, simpleTextFieldWidget, EntryHelper.getStringEntryListWith(EntryHelper.getItems()));
        }));
        this.addButton(modButton = new SimpleTextFieldWidget(new StringTextComponent(""), this.font, default_x * 5, default_y + i++ * space, this.width / 2, 15, (simpleTextFieldWidget) ->
        {
            this.updateList(ModRecipesJSSerializer.RecipeDescriptors.MOD_ID, simpleTextFieldWidget.x, simpleTextFieldWidget.y, simpleTextFieldWidget, EntryHelper.getStringEntryList(EntryHelper.getMods()));
        }));
        this.addButton(typeButton = new SimpleTextFieldWidget(new StringTextComponent(""), this.font, default_x * 5, default_y + i++ * space, this.width / 2, 15, (simpleTextFieldWidget) ->
        {
            this.updateList(ModRecipesJSSerializer.RecipeDescriptors.RECIPE_TYPE, simpleTextFieldWidget.x, simpleTextFieldWidget.y, simpleTextFieldWidget, EntryHelper.getStringEntryListWith(EntryHelper.getRecipeTypes()));
        }));
        this.addButton(idButton = new SimpleTextFieldWidget(new StringTextComponent(""), this.font, default_x * 5, default_y + i * space, this.width / 2, 15, (simpleTextFieldWidget) ->
        {
            this.updateList(ModRecipesJSSerializer.RecipeDescriptors.RECIPE_ID, simpleTextFieldWidget.x, simpleTextFieldWidget.y, simpleTextFieldWidget, EntryHelper.getStringEntryListWith(EntryHelper.getRecipeIds()));
        }));

        this.addButton(new SimpleTextButton(References.getTranslate("screen.remove_manager.button.remove"), this.width / 2 - 80, this.height - 50, 160, 15, (button) -> sendRemovedRecipe()));
        this.addButton(new SimpleButton(References.getTranslate("screen.remove_manager.button.back"), this.width - 200, this.height - 25, 160, 15, (button) -> this.minecraft.displayGuiScreen(new ModSelectionScreen())));

        this.addList(new SimpleListWidget(minecraft, 200, 200, 100, 100, 15,  4, null));
        this.setEntries(0, Arrays.asList(new SimpleListWidget.StringEntry("test"), new SimpleListWidget.StringEntry("test2")));
        this.getList(0).setVisible(false);

        checkBoxes();
    }

    private void sendRemovedRecipe()
    {
        if(!hasInputItemBox.isChecked())
            this.removeMap.remove(ModRecipesJSSerializer.RecipeDescriptors.INPUT_ITEM);
        if(!hasOutputItemBox.isChecked())
            this.removeMap.remove(ModRecipesJSSerializer.RecipeDescriptors.OUTPUT_ITEM);
        if(!hasModBox.isChecked())
            this.removeMap.remove(ModRecipesJSSerializer.RecipeDescriptors.MOD_ID);
        if(!hasTypeBox.isChecked())
            this.removeMap.remove(ModRecipesJSSerializer.RecipeDescriptors.RECIPE_TYPE);
        if(!hasIdBox.isChecked())
            this.removeMap.remove(ModRecipesJSSerializer.RecipeDescriptors.RECIPE_ID);

        if(!this.removeMap.isEmpty())
            ModRecipesJSSerializer.getInstance(getModId()).removeRecipe(new ModifiedRecipe(RecipeFileUtils.ModifiedRecipeType.REMOVED, removeMap));
    }

    private String getModId()
    {
        for(Map.Entry<ModRecipesJSSerializer.RecipeDescriptors, String> entry : this.removeMap.entrySet())
        {
            try
            {
                ResourceLocation location = ResourceLocation.tryCreate(entry.getValue());
                return location.getNamespace();
            }
            catch(Exception ignored)
            {
                return entry.getValue();
            }
        }

        return "";
    }

    private void updateList(ModRecipesJSSerializer.RecipeDescriptors removeTag, int x, int y, SimpleTextFieldWidget textFieldWidget, List<SimpleListWidget.Entry> entries)
    {
        this.getList(0).setCoordinates(x, y + 15);
        this.getList(0).setSize(textFieldWidget.getWidth(), 100);
        this.getList(0).setVisible(true);
        this.getList(0).setOnSelectedChange((entry) ->
        {
            this.removeMap.put(removeTag, entry.toString());
            textFieldWidget.setText(entry.toString());
            this.getList(0).setVisible(false);
        });

        this.setEntries(0, Utilities.copyPartialMatches(textFieldWidget.getText(), entries));
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        renderBackground(matrixStack);
        minecraft.getTextureManager().bindTexture(GUI_TEXTURE);

        Screen.blit(matrixStack, 10, 10, this.width - 20, this.height - 20, 0, 0, 256, 256, 256, 256);

        super.render(matrixStack, mouseX, mouseY, partialTicks);

        Screen.drawCenteredString(matrixStack, this.font, this.title.getString(), this.width / 2, 20, Color.LIGHT_GRAY.getRGB());
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
        if(this.itemInputButton.visible != this.hasInputItemBox.isChecked() ||
                this.itemOutputButton.visible != this.hasOutputItemBox.isChecked() ||
                this.modButton.visible != this.hasModBox.isChecked() ||
                this.typeButton.visible != this.hasTypeBox.isChecked() ||
                this.idButton.visible != this.hasIdBox.isChecked())
        {
            this.getList(0).setVisible(false);
        }
        
        this.itemInputButton.visible = this.hasInputItemBox.isChecked();
        this.itemOutputButton.visible = this.hasOutputItemBox.isChecked();
        this.modButton.visible = this.hasModBox.isChecked();
        this.typeButton.visible = this.hasTypeBox.isChecked();
        this.idButton.visible = this.hasIdBox.isChecked();
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
