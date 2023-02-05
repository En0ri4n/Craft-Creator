package fr.eno.craftcreator.api;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;

import java.util.function.Predicate;

public class CommonUtils
{
    public static final Predicate<RenderType> DEFAULT_BLOCK_RENDER = (r) -> r == RenderType.cutoutMipped();

    public static void setBlockRender(Block block, Predicate<RenderType> render)
    {
        RenderTypeLookup.setRenderLayer(block, render);
    }

    public static void setDefaultBlockRender(Block block)
    {
        setBlockRender(block, DEFAULT_BLOCK_RENDER);
    }
}
