package fr.eno.craftcreator.screen;


import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.screen.widgets.SimpleListWidget;
import fr.eno.craftcreator.screen.widgets.buttons.SimpleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ListScreen extends Screen
{
    protected final List<SimpleListWidget> lists;
    protected int doubleClickCounter;

    protected ListScreen(Component titleIn)
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

    protected void setEntries(int index, List<? extends SimpleListWidget.Entry> entries, boolean resetScroll)
    {
        this.getList(index).setEntries(entries, resetScroll);
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
    public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        if(this.doubleClickCounter >= 0) this.doubleClickCounter--;

        super.render(matrixStack, mouseX, mouseY, partialTicks);

        this.lists.forEach(list -> list.render(matrixStack, mouseX, mouseY, partialTicks));
        this.lists.forEach(list -> list.renderTooltip(matrixStack, mouseX, mouseY));

        this.renderables.stream().filter(widget -> widget instanceof SimpleButton).forEach(widget -> ((SimpleButton) widget).renderToolTip(matrixStack, mouseX, mouseY));
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
        // if(keyCode == GLFW.GLFW_KEY_E) ClientUtils.openScreen(null); // Don't know if it's a good idea to do that, because of text fields
        this.lists.forEach(list -> list.keyPressed(keyCode, scanCode, modifiers));
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char pCodePoint, int pModifiers)
    {
        this.lists.forEach(list -> list.charTyped(pCodePoint, pModifiers));
        return super.charTyped(pCodePoint, pModifiers);
    }

    @Override
    public void tick()
    {
        this.lists.forEach(SimpleListWidget::tick);
        super.tick();
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}
