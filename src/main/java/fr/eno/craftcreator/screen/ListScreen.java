package fr.eno.craftcreator.screen;

import com.mojang.blaze3d.matrix.*;
import fr.eno.craftcreator.screen.buttons.SimpleButton;
import fr.eno.craftcreator.screen.widgets.*;
import net.minecraft.client.gui.screen.*;
import net.minecraft.util.text.*;

import javax.annotation.*;
import java.util.*;

public class ListScreen extends Screen
{
    private final List<SimpleListWidget> lists;
    protected int doubleClickCounter;

    protected ListScreen(ITextComponent titleIn)
    {
        super(titleIn);
        this.lists = new ArrayList<>();
    }

    protected List<SimpleListWidget> getLists()
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

    protected void setEntries(int index, List<SimpleListWidget.Entry> entries)
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
        this.lists.forEach(list -> list.mouseClicked(mouseX, mouseY, button));
        this.doubleClickCounter = 20;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        if(this.doubleClickCounter >= 0) this.doubleClickCounter--;

        super.render(matrixStack, mouseX, mouseY, partialTicks);

        this.lists.forEach(list -> list.render(matrixStack, mouseX, mouseY, partialTicks));
        this.lists.forEach(list -> list.renderTooltip(matrixStack, mouseX, mouseY));

        this.buttons.stream().filter(widget -> widget instanceof SimpleButton).forEach(widget -> ((SimpleButton) widget).renderTooltips(matrixStack, mouseX, mouseY));
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
    public boolean isPauseScreen()
    {
        return false;
    }
}
