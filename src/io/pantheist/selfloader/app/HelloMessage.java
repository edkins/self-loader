package io.pantheist.selfloader.app;

import io.pantheist.selfloader.inject.Layer;

@Layer
public class HelloMessage
{
	public String message()
	{
		return "Hello";
	}
}
