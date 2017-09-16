package io.pantheist.selfloader.app;

import java.io.InputStream;
import java.io.PrintStream;

import io.pantheist.selfloader.inject.Go;
import io.pantheist.selfloader.inject.Std;
import io.pantheist.selfloader.inject.TopLayer;

@TopLayer
public class App
{
	@Std
	public InputStream in;

	@Std
	public PrintStream out;

	public HelloMessage helloMessage;

	@Go
	public void run()
	{
		out.println(helloMessage.message());
	}
}
