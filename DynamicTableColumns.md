# Introduction #

![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/dynamicTableColumns.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/dynamicTableColumns.png)

This is an enhanced version of Table, which support percentage width and pixel width. Thus you columns fit perfectly !

# Usage #

This table works exactly like Table. The only difference is visible in the setWidth method :
```
tblcId = new DynamicTableColumn(tblDyn, SWT.NONE);
tblcId.setText("Id");
tblcId.setWidth("25px");

tblcFirstName = new DynamicTableColumn(tblDyn, SWT.NONE);
tblcFirstName.setText("First Name");
tblcFirstName.setWidth("50%", "100px");

tblcLastName = new DynamicTableColumn(tblDyn, SWT.NONE);
tblcLastName.setText("Last Name");
tblcLastName.setWidth("50%", "100px");

tblcAge = new DynamicTableColumn(tblDyn, SWT.NONE);
tblcAge.setText("Age");
tblcAge.setWidth("60px");
```

# Example #

An example called **DynamicTableColumnsSnippet** is available in the directory **src/test/java/org/mihalis/opal/dynamictablecolumns**.

This example is also available here : http://code.google.com/a/eclipselabs.org/p/opal/source/browse/src/test/java/org/mihalis/opal/dynamictablecolumns/DynamicTableColumnsSnippet.java