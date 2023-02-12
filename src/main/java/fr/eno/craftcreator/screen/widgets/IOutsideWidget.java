package fr.eno.craftcreator.screen.widgets;

import net.minecraft.client.renderer.Rectangle2d;

public interface IOutsideWidget
{
    void calculateArea();

    Rectangle2d getArea();
}
