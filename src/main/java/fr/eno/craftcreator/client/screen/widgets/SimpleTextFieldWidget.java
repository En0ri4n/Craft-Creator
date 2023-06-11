package fr.eno.craftcreator.client.screen.widgets;

import fr.eno.craftcreator.client.utils.ClientUtils;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class SimpleTextFieldWidget extends TextFieldWidget
{
    private final Consumer<SimpleTextFieldWidget> onTextChangeCallable;

    public SimpleTextFieldWidget(int x, int y, int width, int height, @Nullable Consumer<SimpleTextFieldWidget> onTextChange)
    {
        super(ClientUtils.getFontRenderer(), x, y, width, height, new StringTextComponent(""));
        this.onTextChangeCallable = onTextChange;
    }

    @Override
    public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_)
    {
        boolean flag = super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
        if(onTextChangeCallable != null) this.onTextChangeCallable.accept(this);
        return flag;
    }

    @Override
    public boolean charTyped(char p_231042_1_, int p_231042_2_)
    {
        boolean flag = super.charTyped(p_231042_1_, p_231042_2_);
        if(onTextChangeCallable != null) this.onTextChangeCallable.accept(this);
        return flag;
    }
}