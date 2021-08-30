package fr.eno.craftcreator.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class ReflectUtils
{
    /**
     * Get value from the provided field
     *
     * @param field    The field to get value
     * @param instance The instance of the class
     * @param value    The type of the return value of the field
     * @return <T>
     */
    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Field field, Object instance, Class<T> value)
    {
        try
        {
            return (T) field.get(instance);
        }
        catch(IllegalArgumentException | IllegalAccessException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Invoke method with specified parameters
     *
     * @param method   The method to invoke
     * @param instance The instance of the class to invoke (null if static method)
     * @param params   The parameters of the method to invoke
     */
    public static void invokeMethod(Method method, Object instance, Object... params)
    {
        try
        {
            method.invoke(instance, params);
        }
        catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @param clazz   The class of the method
     * @param obfName The SRG Name of the method
     * @param params  The class of parameters of the method
     * @return The method
     */
    public static <T> Method getMethodAndSetAccessible(Class<? super T> clazz, String obfName, Class<?>... params)
    {
        Method method = ObfuscationReflectionHelper.findMethod(clazz, obfName, params);
        try
        {
            method.setAccessible(true);
            Field modifiersMethod = Method.class.getDeclaredField("modifiers");
            modifiersMethod.setAccessible(true);
            modifiersMethod.setInt(method, method.getModifiers() & ~Modifier.FINAL);
        }
        catch(IllegalAccessException | NoSuchFieldException e)
        {
            e.printStackTrace();
        }

        return method;
    }

    /**
     * Set current static field with a new value
     *
     * @param field    the field to set
     * @param newValue the value to set
     */
    public static void setStaticField(Field field, Object newValue)
    {
        try
        {
            field.setAccessible(true);

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

            field.set(null, newValue);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Set current field with a new value
     *
     */
    public static void setField(Field field, Object target, Object value)
    {
        try
        {
            field.setAccessible(true);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(target, value);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Get field with the provided SRG name in the provided class
     *
     * @param clazz   The class of the field
     * @param obfName The srg name of the field
     * @return The field
     */
    public static <T> Field getFieldAndSetAccessible(Class<? super T> clazz, String obfName)
    {
        Field field = ObfuscationReflectionHelper.findField(clazz, obfName);
        try
        {
            field.setAccessible(true);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        }
        catch(IllegalAccessException | NoSuchFieldException e)
        {
            e.printStackTrace();
        }

        return field;
    }
}