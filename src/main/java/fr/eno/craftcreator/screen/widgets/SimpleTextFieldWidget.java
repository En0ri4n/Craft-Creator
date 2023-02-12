package fr.eno.craftcreator.screen.widgets;

import fr.eno.craftcreator.utils.Callable;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.ITextComponent;

public class SimpleTextFieldWidget extends TextFieldWidget
{
    private final Callable<SimpleTextFieldWidget> onTextChangeCallable;

    public SimpleTextFieldWidget(ITextComponent text, FontRenderer font, int x, int y, int width, int height, Callable<SimpleTextFieldWidget> onTextChange)
    {
        super(font, x, y, width, height, text);
        this.onTextChangeCallable = onTextChange;
    }

    @Override
    public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_)
    {
        boolean flag = super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
        this.onTextChangeCallable.run(this);
        return flag;
    }
    
    @Override
    public boolean charTyped(char p_231042_1_, int p_231042_2_)
    {
        boolean flag = super.charTyped(p_231042_1_, p_231042_2_);
        this.onTextChangeCallable.run(this);
        return flag;
    }
}