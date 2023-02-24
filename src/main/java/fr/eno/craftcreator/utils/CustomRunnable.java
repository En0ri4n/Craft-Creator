package fr.eno.craftcreator.utils;

import net.minecraftforge.fml.DistExecutor;

public class CustomRunnable implements DistExecutor.SafeRunnable
{
    private final Runnable runnable;

    private CustomRunnable(Runnable runnable)
    {
        this.runnable = runnable;
    }

    @Override
    public void run()
    {
        runnable.run();
    }

    public static CustomRunnable of(Runnable runnable)
    {
        return new CustomRunnable(runnable);
    }
}