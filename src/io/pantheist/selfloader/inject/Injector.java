package io.pantheist.selfloader.inject;

import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Injector
{
	private Map<Class<?>, Object> objects;

	public static void main(final String[] args) throws InjectionException
	{
		new Injector().run("io.pantheist.selfloader.app.App");
	}

	private void run(final String className) throws InjectionException
	{
		objects = new HashMap<>();

		final Class<?> rootClass = loadClass(className);
		objects.put(rootClass, makeTopLayer(rootClass));

		boolean anyCreated = false;
		do
		{
			anyCreated = false;
			for (final Class<?> clazz : objects.keySet())
			{
				if (sortOutClass(clazz))
				{
					anyCreated = true;
				}
			}
		} while (anyCreated);

		go(objects.get(rootClass));
	}

	private void go(final Object object) throws InjectionException
	{
		Method goMethod = null;
		for (final Method method : object.getClass().getMethods())
		{
			if (method.getAnnotationsByType(Go.class).length != 0)
			{
				if (goMethod != null)
				{
					throw new InjectionException("Multiple Go methods");
				}
				goMethod = method;
			}
		}
		if (goMethod == null)
		{
			throw new InjectionException("No Go method");
		}
		try
		{
			goMethod.invoke(object);
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			throw new InjectionException(e);
		}
	}

	private boolean sortOutClass(final Class<?> clazz) throws InjectionException
	{
		final Object obj = objects.get(clazz);
		boolean anyCreated = false;
		try
		{
			for (final Field field : clazz.getFields())
			{
				if (field.get(obj) == null)
				{
					Object value;
					final Class<?> fieldType = field.getType();
					if (field.getAnnotationsByType(Std.class).length != 0)
					{
						value = getStdObj(fieldType);
					}
					else if (objects.get(fieldType) == null)
					{
						value = makeLayer(fieldType);
						objects.put(fieldType, value);
					}
					else
					{
						value = objects.get(fieldType);
					}
					anyCreated = true;
					field.set(obj, value);
				}
			}
			return anyCreated;
		}
		catch (final IllegalAccessException e)
		{
			throw new InjectionException(e);
		}
	}

	private Class<?> loadClass(final String className) throws InjectionException
	{
		try
		{
			return classLoader().loadClass(className);
		}
		catch (final ClassNotFoundException e)
		{
			throw new InjectionException(e);
		}
	}

	private Object makeTopLayer(final Class<?> type) throws InjectionException
	{
		if (type.getAnnotationsByType(TopLayer.class).length == 0)
		{
			throw new InjectionException("Not a top layer class: " + type);
		}
		try
		{
			return type.newInstance();
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			throw new InjectionException(e);
		}
	}

	private Object makeLayer(final Class<?> type) throws InjectionException
	{
		if (type.getAnnotationsByType(Layer.class).length == 0)
		{
			throw new InjectionException("Not a layer class: " + type);
		}
		try
		{
			return type.newInstance();
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			throw new InjectionException(e);
		}
	}

	private Object getStdObj(final Class<?> type) throws InjectionException
	{
		if (type.equals(InputStream.class))
		{
			return System.in;
		}
		else if (type.equals(PrintStream.class))
		{
			return System.out;
		}
		else
		{
			throw new InjectionException("No standard object for class " + type);
		}
	}

	private ClassLoader classLoader()
	{
		return ClassLoader.getSystemClassLoader();
	}
}
