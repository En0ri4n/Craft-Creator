package fr.eno.craftcreator.screen.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.api.ScreenUtils;
import fr.eno.craftcreator.base.SupportedMods;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DropdownListWidget<T extends DropdownListWidget.Entry<?>> extends SimpleListWidget
{
    private static final ResourceLocation BACKGROUND = new ResourceLocation("textures/block/stone.png");

    private T dropdownSelected;

    private final int dropdownFieldX;
    private final int dropdownFieldY;
    private final int dropdownFieldWidth;
    private final int dropdownFieldHeight;

    private final Consumer<T> onSelected;
    private boolean focused;
    private boolean hovered;

    public DropdownListWidget(int x, int y, int width, int height, int itemHeight, ArrayList<T> entries, Consumer<T> onSelected)
    {
        super(x, y + height, width, Math.min(entries.size(), MAX_ITEMS_DISPLAYED) * itemHeight, itemHeight, 0, 4, new StringTextComponent(""), null, false);
        this.setEntries(entries);
        this.dropdownSelected = entries.get(0);
        this.dropdownFieldX = x;
        this.dropdownFieldY = y;
        this.dropdownFieldWidth = width;
        this.dropdownFieldHeight = height;
        this.onSelected = onSelected;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.hovered = ScreenUtils.isMouseHover(dropdownFieldX, dropdownFieldY, mouseX, mouseY, dropdownFieldWidth, dropdownFieldHeight);

        ClientUtils.color4f(1.0F, 1.0F, 1.0F, 1F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        Screen.fill(matrixStack, this.dropdownFieldX, this.dropdownFieldY, this.dropdownFieldX + this.dropdownFieldWidth, this.dropdownFieldY + this.dropdownFieldHeight, 0xf2c3a942);
        Screen.fill(matrixStack, this.dropdownFieldX + 1, this.dropdownFieldY + 1, this.dropdownFieldX + this.dropdownFieldWidth - 1, this.dropdownFieldY + this.dropdownFieldHeight - 1, Color.DARK_GRAY.getRGB());
        int color = 0xFFFFFFFF;
        drawCenteredString(matrixStack, ClientUtils.getFontRenderer(), this.getMessage().copy().append(" ▼"), this.dropdownFieldX + this.dropdownFieldWidth / 2, this.dropdownFieldY + dropdownFieldHeight / 2 - ClientUtils.getFontRenderer().lineHeight / 2, color);

        if(isFocused())
        {
            matrixStack.pushPose();
            matrixStack.translate(0, 0, 200);
            super.render(matrixStack, mouseX, mouseY, partialTicks);
            matrixStack.popPose();
        }
    }

    @Override
    protected ResourceLocation getBackgroundTile()
    {
        return BACKGROUND;
    }

    protected int getIndex(T entry)
    {
        return this.getEntries().indexOf(entry);
    }

    public List<T> getDropdownEntries()
    {
        return this.children().stream().map(e -> (T) e).collect(Collectors.toList());
    }

    public ITextComponent getMessage()
    {
        return this.getDropdownSelected().getDisplayName(getIndex(getDropdownSelected()));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if(hovered)
        {
            this.setFocused(true);
            return true;
        }

        if(isFocused())
        {
            super.mouseClicked(mouseX, mouseY, button);

            if(!ScreenUtils.isMouseHover(dropdownFieldX, dropdownFieldY + dropdownFieldHeight, (int) mouseX, (int) mouseY, dropdownFieldWidth, height))
            {
                this.setFocused(false);
                return true;
            }

            if(this.getEntryAtPosition(mouseX, mouseY) != null && this.getSelected() != null)
            {
                this.setDropdownSelected((T) this.getSelected());
                this.setFocused(false);
                return true;
            }
        }

        return false;
    }

    private void setFocused(boolean isFocused)
    {
        this.focused = isFocused;
    }

    @Override
    public boolean isFocused()
    {
        return focused;
    }

    public void setEntries(ArrayList<T> entries)
    {
        this.setEntries(entries, true);
        this.setDropdownSelected(entries.get(0));
    }

    public void setDropdownSelected(T entry)
    {
        this.setSelected(entry);
        this.dropdownSelected = entry;
        if(this.onSelected != null) this.onSelected.accept(entry);
    }

    public T getDropdownSelected()
    {
        return this.dropdownSelected;
    }

    public void insertEntryBefore(T entry, T before)
    {
        this.getEntries().add(this.getEntries().indexOf(before), entry);
    }

    public void removeEntry(T entry)
    {
        if(entry == this.dropdownSelected)
            setDropdownSelected(this.getDropdownEntries().get(0));
        this.getEntries().remove(entry);
    }

    public static class Entries
    {
        public static ArrayList<StringEntry> getRecipeTypes(String modid)
        {
            SupportedMods mod = SupportedMods.getMod(modid);
            return mod.getSupportedRecipeTypes().stream().map(recipeTypeLocation -> new StringEntry(recipeTypeLocation.toString(), new StringTextComponent(recipeTypeLocation.toString()))).collect(Collectors.toCollection(ArrayList::new));//Registry.RECIPE_TYPE.stream().filter(type -> CommonUtils.getRecipeTypeName(type).getNamespace().equals(modid)).map(recipeType -> new DropdownListWidget.StringEntry(CommonUtils.getRecipeTypeName(recipeType).toString(), new StringTextComponent(CommonUtils.getRecipeTypeName(recipeType).toString()))).collect(Collectors.toList());
        }

        public static ArrayList<StringEntry> getModIds()
        {
            return SupportedMods.getSupportedLoadedMods().stream().map(mod -> new StringEntry(mod.getModId(), new StringTextComponent(mod.getModId()))).collect(Collectors.toCollection(ArrayList::new));
        }
    }

    public static abstract class Entry<T> extends SimpleListWidget.Entry
    {
        protected Entry() {}

        public abstract T getValue();

        public abstract IFormattableTextComponent getDisplayName(int index);
    }

    public static class StringEntry extends DropdownListWidget.Entry<String>
    {
        private final String value;
        private final IFormattableTextComponent displayName;

        public StringEntry(String value, IFormattableTextComponent displayName)
        {
            this.value = value;
            this.displayName = displayName;
        }

        @Override
        public void render(MatrixStack matrixStack, int index, int top, int left, int width, int itemHeight, int mouseX, int mouseY, boolean hovered, float partialTicks)
        {
            int color = 0xFFFFFF;
            if(hovered)
            {
                color = 0xf2c3a942;
            }

            Screen.fill(matrixStack, left, top, left + width - 3, top + itemHeight, 0xFF000000);
            Screen.fill(matrixStack, left, top, left + width - 4, top + itemHeight, 0xFF585858);

            matrixStack.pushPose();
            matrixStack.translate(0, 0, 100);
            ClientUtils.getFontRenderer().draw(matrixStack, this.displayName.getString(), left + 2, top + Math.floorDiv(itemHeight - 8, 2), color);
            matrixStack.popPose();
        }

        @Override
        public String getValue()
        {
            return this.value;
        }

        @Override
        public IFormattableTextComponent getDisplayName(int index)
        {
            return this.displayName;
        }

        @Override
        public void renderTooltip(MatrixStack matrixStack, int mouseX, int mouseY)
        {

        }

        @Override
        public String getEntryValue()
        {
            return this.displayName.getString();
        }
    }
}
