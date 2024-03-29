package fr.eno.craftcreator.client.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.References;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;

public class ScreenUtils
{
    private static final ResourceLocation BUTTON_TEXTURE = References.getLoc("textures/gui/buttons/basic_button.png");

    /**
     * Render a sized button (not stretched)<br>
     * Calls {@link ScreenUtils#renderSizedTexture(MatrixStack, int, int, int, int, int, int, int, int, int, int)} with a cut size of 4<br>
     *
     * @param matrixStack the matrix stack
     * @param x           the x position
     * @param y           the y position
     * @param width       the width
     * @param height      the height
     * @param isActive    if the button is active
     * @param isHovered   if the button is hovered
     */
    public static void renderSizedButton(MatrixStack matrixStack, int x, int y, int width, int height, boolean isActive, boolean isHovered)
    {
        ClientUtils.bindTexture(BUTTON_TEXTURE);

        int buttonTextureHeight = 20;
        int textureWidth = 100;
        int textureHeight = 60;
        int xTexture = 0;
        int yTexture = !isActive ? 0 : isHovered ? 20 : 40;

        renderSizedTexture(matrixStack, 4, x, y, width, height, xTexture, yTexture, textureWidth, textureHeight, buttonTextureHeight);
    }

    /**
     * Render a sized texture (not stretched)
     *
     * @param matrixStack         the matrix stack
     * @param cutSize             the size of the cut (e.g. 4 for a 4x4 px texture for corners and sides)
     * @param x                   the x position
     * @param y                   the y position
     * @param width               the width
     * @param height              the height
     * @param xTexture            the x position in the texture
     * @param yTexture            the y position in the texture
     * @param textureWidth        the texture width
     * @param textureHeight       the texture height
     * @param buttonTextureHeight the button texture height (for multiple states like active, hovered, etc.) same as textureHeight if not a button
     */
    public static void renderSizedTexture(MatrixStack matrixStack, int cutSize, int x, int y, int width, int height, int xTexture, int yTexture, int textureWidth, int textureHeight, int buttonTextureHeight)
    {
        // Top left corner
        Screen.blit(matrixStack, x, y, cutSize, cutSize, xTexture, yTexture, cutSize, cutSize, textureWidth, textureHeight);
        // Left side
        Screen.blit(matrixStack, x, y + cutSize, cutSize, height - cutSize * 2, xTexture, yTexture + cutSize, cutSize, cutSize, textureWidth, textureHeight);
        // Bottom left corner
        Screen.blit(matrixStack, x, y + height - cutSize, cutSize, cutSize, xTexture, yTexture + buttonTextureHeight - cutSize, cutSize, cutSize, textureWidth, textureHeight);
        // Top side
        Screen.blit(matrixStack, x + cutSize, y, width - cutSize * 2, cutSize, cutSize, yTexture, textureWidth - cutSize * 2, cutSize, textureWidth, textureHeight);
        // Top right corner
        Screen.blit(matrixStack, x + width - cutSize, y, cutSize, cutSize, textureWidth - cutSize, yTexture, cutSize, cutSize, textureWidth, textureHeight);
        // Right side
        Screen.blit(matrixStack, x + width - cutSize, y + cutSize, cutSize, height - cutSize * 2, textureWidth - cutSize, yTexture + cutSize, cutSize, cutSize, textureWidth, textureHeight);
        // Bottom right corner
        Screen.blit(matrixStack, x + width - cutSize, y + height - cutSize, cutSize, cutSize, textureWidth - cutSize, yTexture + buttonTextureHeight - cutSize, cutSize, cutSize, textureWidth, textureHeight);
        // Bottom side
        Screen.blit(matrixStack, x + cutSize, y + height - cutSize, width - cutSize * 2, cutSize, cutSize, yTexture + buttonTextureHeight - cutSize, textureWidth - cutSize * 2, cutSize, textureWidth, textureHeight);
        // Middle
        Screen.blit(matrixStack, x + cutSize, y + cutSize, width - cutSize * 2, height - cutSize * 2, cutSize, yTexture + cutSize, textureWidth - cutSize * 2, buttonTextureHeight - cutSize * 2, textureWidth, textureHeight);
    }

    /**
     * Check if the mouse is in the specified area
     *
     * @return True if the mouse is in the specified area
     */
    public static boolean isMouseHover(int x, int y, int mouseX, int mouseY, int width, int height)
    {
        return mouseX > x && mouseX < (x + width) && mouseY > y && mouseY < (y + height);
    }

    /**
     * Truncate a string to fit in a specified width (with "..." at the end)
     *
     * @param width the width
     * @param displayStr the string to truncate
     * @return the truncated string
     */
    public static String truncateString(int width, String displayStr)
    {
        int stringWidth = ClientUtils.width(displayStr);

        if(stringWidth > width - (16 + 5))
        {
            int letters = displayStr.toCharArray().length;
            int letterWidth = stringWidth / letters;
            int def_width = width - (16 + 5);
            int width_much = stringWidth - def_width;
            int lettersToRemove = width_much / letterWidth;
            displayStr = displayStr.substring(0, displayStr.length() - lettersToRemove - 3) + "...";
        }
        return displayStr;
    }
}
