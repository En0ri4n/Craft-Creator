package fr.eno.craftcreatorapi.utils;

public abstract class ComponentHelperBase<C>
{
    public abstract C getComponent(String content);

    public abstract C createComponentUrlOpener(C content, String url);
}
