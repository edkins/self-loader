package io.pantheist.selfloader.inject;

public class InjectionException extends Exception
{

	private static final long serialVersionUID = 2561718155328505026L;

	public InjectionException(final String message)
	{
		super(message);
	}

	public InjectionException(final Exception e)
	{
		super(e);
	}

}
