# Introduction #

![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/calculator.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/calculator.png)
![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/calculatorCombo.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/calculatorCombo.png)

A simple calculator, displayed as a whole widget or inside a combo.

# Usage #

This is very simple : you instantiate a Calculator or a CalculatorCombo, then you attach a ModifyListener in order to be informed when the value changed :

```
final Calculator calc = new Calculator(shell, SWT.NONE);

calc.addModifyListener(new ModifyListener() {

     @Override
     public void modifyText(final ModifyEvent e) {
          System.out.println("New value is " + calc.getValue());
     }
});

```

And _voilà_ !

# Examples #

2 examples called **CalculatorSnippet.java** and **CalculatorComboSnippet.java** are located in the directory **src/test/java/org/mihalis/opal/calculator**.

These examples are also available here :
  * http://code.google.com/a/eclipselabs.org/p/opal/source/browse/src/test/java/org/mihalis/opal/calculator/CalculatorSnippet.java
  * http://code.google.com/a/eclipselabs.org/p/opal/source/browse/src/test/java/org/mihalis/opal/calculator/CalculatorComboSnippet.java