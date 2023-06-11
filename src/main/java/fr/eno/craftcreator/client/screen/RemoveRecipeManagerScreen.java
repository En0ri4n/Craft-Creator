package fr.eno.craftcreator.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.client.utils.ClientUtils;
import fr.eno.craftcreator.utils.CommonUtils;
import fr.eno.craftcreator.client.utils.ScreenUtils;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.client.screen.widgets.SuggesterTextFieldWidget;
import fr.eno.craftcreator.client.screen.widgets.buttons.SimpleButton;
import fr.eno.craftcreator.client.screen.widgets.buttons.SimpleCheckBox;
import fr.eno.craftcreator.init.InitPackets;
import fr.eno.craftcreator.packets.RemoveRecipePacket;
import fr.eno.craftcreator.recipes.base.ModRecipeSerializer;
import fr.eno.craftcreator.recipes.kubejs.KubeJSModifiedRecipe;
import fr.eno.craftcreator.recipes.utils.EntryType;
import fr.eno.craftcreator.utils.EntryHelper;
import fr.eno.craftcreator.utils.Utils;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class RemoveRecipeManagerScreen extends ListScreen
{
    private static final ResourceLocation GUI_TEXTURE = References.getLoc("textures/gui/container/gui_background.png");
    private SimpleCheckBox hasInputItemBox;
    private SimpleCheckBox hasOutputItemBox;
    private SimpleCheckBox hasModIdBox;
    private SimpleCheckBox hasRecipeTypeBox;
    private SimpleCheckBox hasRecipeIdBox;

    private SuggesterTextFieldWidget itemInputField;
    private SuggesterTextFieldWidget itemOutputField;
    private SuggesterTextFieldWidget modIdField;
    private SuggesterTextFieldWidget recipeTypeField;
    private SuggesterTextFieldWidget recipeIdField;

    private final Map<ModRecipeSerializer.RecipeDescriptors, String> recipeDescriptors = new HashMap<>();

    public RemoveRecipeManagerScreen()
    {
        super(References.getTranslate("screen.remove_manager.title"));
    }

    @Override
    protected void init()
    {
        clearLists();

        int i = 0;
        int default_x = width / 8 + 20;
        int default_y = height / 9 + 35;
        int spaceY = height / 10;
        int spaceX = 87;
        int size = 10;
        int fieldWidth = width / 10 * 4;
        int fieldHeight = 15;
        int heightGap = 2;

        this.addButton(hasInputItemBox = new SimpleCheckBox(default_x, default_y + i++ * spaceY, size, size, References.getTranslate("screen.remove_manager.button.has_input_item"), true));
        this.addButton(hasOutputItemBox = new SimpleCheckBox(default_x, default_y + i++ * spaceY, size, size, References.getTranslate("screen.remove_manager.button.has_output_item"), false));
        this.addButton(hasModIdBox = new SimpleCheckBox(default_x, default_y + i++ * spaceY, size, size, References.getTranslate("screen.remove_manager.button.has_mod"), false));
        this.addButton(hasRecipeTypeBox = new SimpleCheckBox(default_x, default_y + i++ * spaceY, size, size, References.getTranslate("screen.remove_manager.button.has_type"), false));
        this.addButton(hasRecipeIdBox = new SimpleCheckBox(default_x, default_y + i * spaceY, size, size, References.getTranslate("screen.remove_manager.button.has_id"), false));

        i = 0;
        this.addList(itemInputField = new SuggesterTextFieldWidget( default_x + spaceX, default_y + i++ * spaceY - heightGap, fieldWidth, fieldHeight,  EntryHelper.getStringEntryListWith(EntryHelper.getItems(), EntryType.ITEM), entry -> recipeDescriptors.put(ModRecipeSerializer.RecipeDescriptors.INPUT_ITEM, entry.getEntryValue())));
        this.addList(itemOutputField = new SuggesterTextFieldWidget(default_x + spaceX, default_y + i++ * spaceY - heightGap, fieldWidth, fieldHeight, EntryHelper.getStringEntryListWith(EntryHelper.getItems(), EntryType.ITEM), entry -> recipeDescriptors.put(ModRecipeSerializer.RecipeDescriptors.OUTPUT_ITEM, entry.getEntryValue())));
        this.addList(modIdField = new SuggesterTextFieldWidget(default_x + spaceX, default_y + i++ * spaceY - heightGap, fieldWidth, fieldHeight, EntryHelper.getStringEntryList(EntryHelper.getMods()), entry -> recipeDescriptors.put(ModRecipeSerializer.RecipeDescriptors.MOD_ID, entry.getEntryValue())));
        this.addList(recipeTypeField = new SuggesterTextFieldWidget(default_x + spaceX, default_y + i++ * spaceY - heightGap, fieldWidth, fieldHeight, EntryHelper.getStringEntryListWith(EntryHelper.getRecipeTypes(), EntryType.OTHER), entry -> recipeDescriptors.put(ModRecipeSerializer.RecipeDescriptors.RECIPE_TYPE, entry.getEntryValue())));
        this.addList(recipeIdField = new SuggesterTextFieldWidget(default_x + spaceX, default_y + i * spaceY - heightGap, fieldWidth, fieldHeight, EntryHelper.getStringEntryListWith(EntryHelper.getRecipeIds(), EntryType.OTHER), entry -> recipeDescriptors.put(ModRecipeSerializer.RecipeDescriptors.RECIPE_ID, entry.getEntryValue())));

        this.addButton(new SimpleButton(References.getTranslate("screen.remove_manager.button.remove"), this.width / 2 - 80, this.height / 9 * 7, 160, 20, (button) -> sendRemovedRecipe()));
        this.addButton(new SimpleButton(References.getTranslate("screen.button.back"), this.width / 2 + 80 + 5, this.height / 9 * 7, 50, 20, (button) -> ClientUtils.openScreen(new RecipeManagerScreen())));

        checkBoxes();
    }

    private void sendRemovedRecipe()
    {
        if(!hasInputItemBox.selected()) this.recipeDescriptors.remove(ModRecipeSerializer.RecipeDescriptors.INPUT_ITEM);
        if(!hasOutputItemBox.selected()) this.recipeDescriptors.remove(ModRecipeSerializer.RecipeDescriptors.OUTPUT_ITEM);
        if(!hasModIdBox.selected()) this.recipeDescriptors.remove(ModRecipeSerializer.RecipeDescriptors.MOD_ID);
        if(!hasRecipeTypeBox.selected()) this.recipeDescriptors.remove(ModRecipeSerializer.RecipeDescriptors.RECIPE_TYPE);
        if(!hasRecipeIdBox.selected()) this.recipeDescriptors.remove(ModRecipeSerializer.RecipeDescriptors.RECIPE_ID);

        if(!this.recipeDescriptors.isEmpty())
        {
            SupportedMods mod = this.recipeDescriptors.values().stream().findFirst().filter(SupportedMods::isModLoaded).map(SupportedMods::getMod).orElse(SupportedMods.MINECRAFT);
            InitPackets.NetworkHelper.sendToServer(new RemoveRecipePacket(mod, new KubeJSModifiedRecipe(KubeJSModifiedRecipe.KubeJSModifiedRecipeType.REMOVED, recipeDescriptors), ModRecipeSerializer.SerializerType.KUBE_JS));
        }
        //ModRecipeCreatorDispatcher.getSeralizer(getModId()).addModifiedRecipe(new KubeJSModifiedRecipe(KubeJSModifiedRecipe.KubeJSModifiedRecipeType.REMOVED, recipeDescriptors));
    }

    private String getModId()
    {
        for(Map.Entry<ModRecipeSerializer.RecipeDescriptors, String> entry : this.recipeDescriptors.entrySet())
        {
            try
            {
                ResourceLocation location = CommonUtils.parse(entry.getValue());
                return Utils.notNull(location).getNamespace();
            }
            catch(Exception ignored)
            {
                return entry.getValue();
            }
        }

        return "";
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        renderBackground(matrixStack);
        ClientUtils.bindTexture(GUI_TEXTURE);
        int x = width / 8;
        int y = height / 9;
        ScreenUtils.renderSizedTexture(matrixStack, 4, x, y, width - 2 * x, height - 2 * y, 0, 0, 16, 16, 16);

        super.render(matrixStack, mouseX, mouseY, partialTicks);

        drawCenteredString(matrixStack, this.font, this.title.getString(), this.width / 2, y + 10, 0xB3b3af);
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
        this.itemInputField.setVisible(this.hasInputItemBox.selected());
        this.itemOutputField.setVisible(this.hasOutputItemBox.selected());
        this.modIdField.setVisible(this.hasModIdBox.selected());
        this.recipeTypeField.setVisible(this.hasRecipeTypeBox.selected());
        this.recipeIdField.setVisible(this.hasRecipeIdBox.selected());
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
