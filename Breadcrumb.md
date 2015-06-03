# Introduction #

![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/breadcrumb.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/breadcrumb.png)

A simple breadcrumb toolbar.

# Usage #

This is very simple : you instantiate a Breadcrumb object and then you create BreadcrumbItems :

```
final Breadcrumb bc = new Breadcrumb(shell, breadCrumbArgument);
final BreadcrumbItem item = new BreadcrumbItem(bc, SWT.PUSH);
item.setText("Text");
```

And _voilà_ !

The BreadcrumbItem can be labels (style SWT.NONE), buttons (style SWT.PUSH) or toggle buttons (style SWT.TOGGLE).

# Examples #

An example called **BreadcrumbSnippet.java** is located in the directory **src/test/java/org/mihalis/opal/breadcrumb**.

This example is also available here :
  * http://opal.eclipselabs.org.codespot.com/hg/src/test/java/org/mihalis/opal/breadcrumb/BreadcrumbSnippet.java