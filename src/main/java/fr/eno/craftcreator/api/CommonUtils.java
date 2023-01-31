package fr.eno.craftcreator.api;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;

import java.util.function.Predicate;

public class CommonUtils
{
    public static final Predicate<RenderType> DEFAULT_ITEMBLOCK_RENDER = (r) -> r == RenderType.getCutout();

    public static void setItemBlockRender(Block block, Predicate<RenderType> render)
    {
        RenderTypeLookup.setRenderLayer(block, render);
    }

    public static void setDefaultItemblockRender(Block block)
    {
        setItemBlockRender(block, DEFAULT_ITEMBLOCK_RENDER);
    }
}
