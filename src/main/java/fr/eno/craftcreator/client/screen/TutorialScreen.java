package fr.eno.craftcreator.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.client.utils.ClientUtils;
import fr.eno.craftcreator.client.screen.widgets.buttons.SimpleButton;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import java.util.Arrays;
import java.util.List;

public class TutorialScreen extends Screen
{
    private static final List<ResourceLocation> pages = Arrays.asList(References.getLoc("textures/gui/tutorial/tutorial_0.png"), References.getLoc("textures/gui/tutorial/tutorial_1.png"), References.getLoc("textures/gui/tutorial/tutorial_2.png"));

    private static final int textureWidth = 1600, textureHeight = 900; // 16:9

    private int currentTutorialPageIndex;

    private SimpleButton nextButton;
    private SimpleButton previousButton;


    public TutorialScreen()
    {
        super(new StringTextComponent("screen.tutorial.title"));
    }

    @Override
    protected void init()
    {
        super.init();

        int backWidth = 100;
        int buttonWidth = 20;
        int buttonHeight = 20;
        int gapX = 3;

        addButton(previousButton = new SimpleButton(References.getTranslate("screen.button.previous"), width / 2 - backWidth / 2 - buttonWidth - gapX, height - buttonHeight - 20, buttonWidth, buttonHeight, b -> previousPage()));
        addButton(nextButton = new SimpleButton(References.getTranslate("screen.button.next"), width / 2 + backWidth / 2 + gapX, height - buttonHeight - 20, buttonWidth, buttonHeight, b -> nextPage()));

        addButton(new SimpleButton(References.getTranslate("screen.button.back"), width / 2 - backWidth / 2, height - buttonHeight - 20, backWidth, buttonHeight, b -> ClientUtils.openScreen(null)));

        checkPage();
    }

    @Override
    public void render(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks)
    {
        ClientUtils.bindTexture(pages.get(currentTutorialPageIndex));
        Screen.blit(pMatrixStack, 0, 0, width, height, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);

        Screen.drawString(pMatrixStack, ClientUtils.getFontRenderer(), References.getTranslate("screen.tutorial.page", currentTutorialPageIndex + 1, pages.size()), 5, 5, 0xFFFFFF);

        super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
    }

    private void nextPage()
    {
        this.currentTutorialPageIndex++;
        checkPage();
    }

    private void previousPage()
    {
        this.currentTutorialPageIndex--;
        checkPage();
    }

    private void checkPage()
    {
        previousButton.visible = currentTutorialPageIndex > 0;
        nextButton.visible = currentTutorialPageIndex < pages.size() - 1;
    }
}
