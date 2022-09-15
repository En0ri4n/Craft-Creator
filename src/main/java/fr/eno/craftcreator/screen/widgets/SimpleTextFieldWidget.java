package fr.eno.craftcreator.screen.widgets;

import fr.eno.craftcreator.utils.Callable;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.TextComponent;

public class SimpleTextFieldWidget extends EditBox
{
    private final Callable<SimpleTextFieldWidget> onTextChangeCallable;

    public SimpleTextFieldWidget(TextComponent text, Font font, int x, int y, int width, int height, Callable<SimpleTextFieldWidget> onTextChange)
    {
        super(font, x, y, width, height, text);
        this.onTextChangeCallable = onTextChange;
    }

    @Override
    public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_)
    {
        this.onTextChangeCallable.run(this);
        return super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
    }
}