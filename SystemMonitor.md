# Introduction #

![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/systemmonitor.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/systemmonitor.png)

An image monitor.

# Usage #

You just have to create a SystemMonitor object :
```
SystemMonitor cpu = new SystemMonitor(shell, SWT.NONE, SampleIdentifier.CPU_USAGE);
```

The first argument is the parent widget, the second is the style (SWT.NONE) and the third is an enumeration element : **PHYSICAL\_MEMORY, HEAP\_MEMORY, THREADS, CPU\_USAGE**.

# Making my own sample #

The first thing to do is to create a class that will represents a sample. This class implements the interface `org.mihalis.opal.systemMonitor.sample` :
```
import java.util.Random;

import org.mihalis.opal.systemMonitor.Sample;

/**
 * A random sample
 */
public class RandomSample implements Sample {

	@Override
	public double getValue() {
		 return new Random().nextInt(100);
	}

	@Override
	public double getMaxValue() {
		return 99d;
	}

}
```

Then you instantiate your SystemMonitor and customize it :
```
final SystemMonitor custom = new SystemMonitor(shell, SWT.NONE);
custom.addSample("custom", new RandomSample());
custom.setCaption("custom", "Random value:");
custom.setColor("custom", new RGB(255, 255, 216));
custom.setFormatPattern("custom", "%{value},.0f / %{maxValue},.0f / %{percentValue}.0f%%");
custom.setLayoutData(createLayoutData());
```

# Example #

An example called **SystemMonitorSnippet.java** is located in the directory **src/test/java/org/mihalis/opal/systemMonitor**.

This example is also available here : http://code.google.com/a/eclipselabs.org/p/opal/source/browse/src/test/java/org/mihalis/opal/SystemMonitor/SystemMonitorSnippet.java