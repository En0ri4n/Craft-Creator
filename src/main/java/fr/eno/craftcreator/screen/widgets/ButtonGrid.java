package fr.eno.craftcreator.screen.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.utils.Callable;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.SoundEvents;

import java.util.List;

public class ButtonGrid<T extends Button>
{
    private final int x;
    private final int y;
    private final int buttonSize;
    private final int buttonSpacing;
    private final int buttonPerLine;
    private final List<T> buttons;
    private final Callable<T> onPress;

    private boolean isVisible;

    public ButtonGrid(int x, int y, int buttonSize, int buttonSpacing, int buttonPerLine,List<T> buttons, Callable<T> onPress)
    {
        this.x = x;
        this.y = y;
        this.buttonSize = buttonSize;
        this.buttonSpacing = buttonSpacing;
        this.buttonPerLine = buttonPerLine;
        this.buttons = buttons;
        this.onPress = onPress;
        this.isVisible = false;
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        if(!isVisible)
            return;

        // Render background
        Screen.fill(matrixStack, x, y, x + getWidth(), y + getHeight(), 0x88000000);

        // Render buttons
        for(int i = 0; i < getButtonCount(); i++)
        {
            int x = this.x + buttonSpacing + (i % this.buttonPerLine) * (buttonSize + buttonSpacing);
            int y = this.y + buttonSpacing + (i / this.buttonPerLine) * (this.buttonSize + this.buttonSpacing);
            getButton(i).x = x;
            getButton(i).y = y;
            getButton(i).render(matrixStack, mouseX, mouseY, partialTicks);
        }
    }

    protected int getWidth()
    {
        return buttonSpacing + Math.min(getButtonCount(), buttonPerLine) * (buttonSize + buttonSpacing);
    }

    protected int getHeight()
    {
        return buttonSpacing + (Math.floorDiv(getButtonCount() - 1, buttonPerLine) + 1) * (buttonSpacing + buttonSize);
    }

    public void onMouseClicked(double mouseX, double mouseY, int button)
    {
        if(isVisible)
        {
            for(T b : getButtons())
                if(b.isMouseOver(mouseX, mouseY))
                {
                    onPress.run(b);
                    ClientUtils.getMinecraft().getSoundManager().play(SimpleSound.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                }
        }
    }

    public boolean isMouseOver(double mouseX, double mouseY)
    {
        return isVisible && mouseX >= x && mouseX <= x + getWidth() && mouseY >= y && mouseY <= y + getHeight();
    }

    public T getButton(int index)
    {
        return this.buttons.get(index);
    }

    public List<T> getButtons()
    {
        return this.buttons;
    }

    public int getButtonCount()
    {
        return this.buttons.size();
    }

    public void setVisible(boolean isVisible)
    {
        this.isVisible = isVisible;
    }

    public boolean isVisible()
    {
        return this.isVisible;
    }
}
