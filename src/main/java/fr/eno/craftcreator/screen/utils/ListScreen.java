package fr.eno.craftcreator.screen.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.screen.buttons.SimpleButton;
import fr.eno.craftcreator.screen.widgets.SimpleListWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ListScreen extends Screen
{
    protected final List<SimpleListWidget> lists;
    protected int doubleClickCounter;

    protected ListScreen(ITextComponent titleIn)
    {
        super(titleIn);
        this.lists = new ArrayList<>();
    }

    public List<SimpleListWidget> getLists()
    {
        return this.lists;
    }

    protected void addList(SimpleListWidget list)
    {
        this.lists.add(list);
    }

    protected SimpleListWidget getList(int index)
    {
        return this.lists.get(index);
    }

    protected void setEntries(int index, List<? extends SimpleListWidget.Entry> entries)
    {
        this.getList(index).setEntries(entries);
    }

    protected void clearLists()
    {
        this.lists.clear();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        boolean flag = super.mouseClicked(mouseX, mouseY, button);
        this.lists.forEach(list -> list.mouseClicked(mouseX, mouseY, button));
        this.doubleClickCounter = 20;
        return flag;
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        if(this.doubleClickCounter >= 0) this.doubleClickCounter--;

        super.render(matrixStack, mouseX, mouseY, partialTicks);

        this.lists.forEach(list -> list.render(matrixStack, mouseX, mouseY, partialTicks));
        this.lists.forEach(list -> list.renderTooltip(matrixStack, mouseX, mouseY));

        this.buttons.stream().filter(widget -> widget instanceof SimpleButton).forEach(widget -> widget.renderToolTip(matrixStack, mouseX, mouseY));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta)
    {
        this.lists.forEach(list -> list.mouseScrolled(mouseX, mouseY, delta));
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY)
    {
        this.lists.forEach(list -> list.mouseDragged(mouseX, mouseY, button, dragX, dragY));
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }


    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button)
    {
        this.lists.forEach(list -> list.mouseReleased(mouseX, mouseY, button));
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        this.lists.forEach(list -> list.keyPressed(keyCode, scanCode, modifiers));
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}
