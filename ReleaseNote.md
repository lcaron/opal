# version 1.0.1 (2014-09-25) #

This release fixes the following issues :
  * [issue 37](https://code.google.com/p/opal/issues/detail?id=37) : Propose Opal as OSGI Bundle
  * [issue 38](https://code.google.com/p/opal/issues/detail?id=38) : Not displaying "display name" (thanks to vetamames)

# version 1.0.0 (2014-07-16) #

This release fixes the following issues :
  * [issue 29](https://code.google.com/p/opal/issues/detail?id=29) : In a Dialog widget, the size of the button is correct, so the label is not truncated
  * [issue 30](https://code.google.com/p/opal/issues/detail?id=30) : DLItem getLastAction() method is now public
  * [issue 31](https://code.google.com/p/opal/issues/detail?id=31) : In dualList, add method that do not trigger any selection or SelectionChange event,
  * [issue 32](https://code.google.com/p/opal/issues/detail?id=32) : Duallist.add() method did not refresh the widget
  * [issue 33](https://code.google.com/p/opal/issues/detail?id=33) : Duallist resizes correctly
  * [issue 34](https://code.google.com/p/opal/issues/detail?id=34) : PropertyTable PTIntegerEditor can handle empty inputs
  * [issue 35](https://code.google.com/p/opal/issues/detail?id=35) : Fix a problem with PreferenceWindow constructor

# version 0.9.9e (2014-04-17) #

This release fixes the following issues :
  * [issue 28](https://code.google.com/p/opal/issues/detail?id=28) : add a new Listener [SelectionChangeListener](SelectionChangeListener.md) (thanks to Benjamin Klatt for his help), which is fired when a selection/de selection is done
  * I discovered that in widgets in which SWT.CR is used, I forgot SWT.KEYPAD\_CR. It's fixed.
  * In OpalDialog, the icon for the convenient method ask was an exclamation mark instead of an interrogation mark !

# version 0.9.9c (2014-04-05) #

This release fixes the following issues :
  * [issue 25](https://code.google.com/p/opal/issues/detail?id=25) : memory leak in OButton
  * [issue 26](https://code.google.com/p/opal/issues/detail?id=26), [issue 27](https://code.google.com/p/opal/issues/detail?id=27), [issue 28](https://code.google.com/p/opal/issues/detail?id=28) : fix numerous problems in DualList (thanks to Benjamin Klatt for his help)


# version 0.9.9c (2014-04-05) #

This release fixes the following issues :
  * [issue 24](https://code.google.com/p/opal/issues/detail?id=24) : if you use BreadCrumb with images, the rendering was incorrect
  * unreferenced issue : in various widgets, dispose() method is more rigorous

# version 0.9.9b (2014-03-17) #

This release fixes the following issues :
  * [issue 22](https://code.google.com/p/opal/issues/detail?id=22) : in the Multichoice Widget, if there is a lot of data, the elements in the popup window are displayed correctly
  * [issue 23](https://code.google.com/p/opal/issues/detail?id=23) : problem when using Dialog widget when you have 2 screens
  * unreferenced issue : problem when we want to initialize the PTComboEditor

# version 0.9.9 (2014-02-28) #

This release do not contain any new feature.
The code has been cleaned, the comments and images have been modyfied in order to be ready for Nebula !

# version 0.9.8 (2013-12-18) #

**_New features_**
  * HTML rendering accepts new tags : size, color, backgroundcolor.

**_Bug fixes_**
  * [Issue 20](https://code.google.com/p/opal/issues/detail?id=20) : Few bugs in bread crumb and OButton
  * [Issue 21](https://code.google.com/p/opal/issues/detail?id=21) : MultiChoice NullPointerException

# version 0.9.7 (2013-11-17) #

**_New features_**
  * In MultiChoice combo, you can now provide a label provider.
  * In MultiChoice combo, one can type data

**_Bug fixes_**
  * In OButton, GridData constraints are used, and you use traverse the widget by pressing "TAB"

# version 0.9.6 (2013-10-24) #

**_New features_**
  * In CheckboxGroup, when we enable the group the previous "enabled" state is used (before, the widgets were enabled event if they have been disabled)

**_Bug fixes_**
  * Better display of Dialogs under MacOSX
  * When a dialog has a progress bar, one can change the text when the value of the progress bar is udpated
  * When you have 2 monitors, the Dialog is now centered on the screen that contains the parent Shell
  * In ImageSelector, you can give a path to an image
  * TitledSeparator : the SWT.CENTER style is working

# version 0.9.5.2 (2013-01-27) #

**_New features_**
  * Added OBusyIndicator
  * Adding gridsize and refresh time options for SystemMonitor
  * Support for changing color of rounded toolbar

**_Bug fixes_**
  * Fix for bounds issue in ColumnBrowser
  * Fix for Widget disposed error in Switch button

# version 0.9.5 (2012-09-07) #

**_Bug fixes_**
  * Fix a problem in the dispose() method of BreadCrumItem (thanks to XiaoLibang)
  * Fix problems with TextAssist (thanks to Timm Baumeister)

# version 0.9.4 (2012-09-04) #

**_New features_**
  * Polish translations, thanks to Michal Niewrzal

**_Bug fixes_**
  * Fix a problem with HorizontalSpinner and floating values (thanks to Michal Niewrzal)

# version 0.9.3 (2012-08-27) #

**_New widgets_**
  * StarRating, a star rating component
  * Breadcrumb, a nice-looking breadcrumb
  * OButton, a customizable button widget

# version 0.9.2 (2012-08-11) #

**_New widget_**

  * RoundedToolbar, a widget that is equivalent to the Toolbar one, but into a rounded box.

**_Bug fixes_**
  * The parser used in dialog boxes is better
  * You can  open the dialog box in the middle of the screen or in the middle of the window
  * In the ask dialog, when we enter a value and press enter key, the dialog closes
  * You can select a tab in the preference window

# version 0.9.1 (2012-05-09) #

**_New widget_**

  * Calculator and Calculator combo, a widget that displays a calculator or a calculator in a combo box


# version 0.9 (2012-03-31) #

**_New widget_**

  * SystemMonitor, a widget that displays system ressources (CPU, Threads...)

# version 0.8 (2012-03-04) #

**_New widget_**

  * DynamicColumnTable, an advanced widget that allow you to set width using percentages (by Luis Carlos Moreira da Costa)

**_New feature_**

  * Dialogs can now be resized. You can also put large text in it, and set the displaying of the vertical scrollbar and the preferred height of the text area.

**_Bug fixes_**
  * Notifier can now run event if there is no active shell
  * CheckBoxGroup can now be mixed with the Gradient Composite (thanks to Marnix van Bochove)

# version 0.7 (2012-01-06) #

**_New widget_**

  * PropertyTable, a widget to edit properties

**_New feature_**

  * Horizontal Spinner accepts 2 new styles : SWT.LEFT and SWT.RIGHT. With these styles, the buttons are located on the left part or on the right part of the widget.

Because I've almost reach my objectives (only a few widgets are missing), I decided to increased the version number to 0.7.

# version 0.12 (2011-11-13) #

**_New feature_**

  * PromptSupport, an utility class to add a support to your Text and Combo widgets.


# version 0.11 (2011-11-12) #

**_New widget_**

  * Rangle Slider, a range slider to select a upper value and a lower value

**_Translations_**

  * New Spanish translations by Vicente Rico

# version 0.10.1 (2011-11-07) #

**_Fixes_**

  * Fix a problem with enable/disable conditions

# version 0.10 (2011-11-05) #

**_New widget_**

  * PreferenceWindow, a window to set up easily preferences

**_Fixes_**

  * In the checkbox group, the button is protected (asked by Marcos López Barbeito)
  * In the duallist, the actions called when the user clicks on button are now protected (asked by Marcos López Barbeito)

# version 0.09.1 (2011-10-26) #

In this version, the code structure has been modified to comply with Maven standard. Many thanks to Marnix van Bochove for his help !

**_Fixes_**

  * Add a selection listener to the checkbox group widget

# version 0.09 (2011-10-24) #

**_New widget_**

  * Notifier

# version 0.08 (2011-10-20) #

**_New widget_**

  * Tip of the day

**_Fixes_**

  * Fix some stange behaviour on column widget
  * Fix some displaying problems on Windows XP
  * The description part of the header was selectable, it's no longer the case

# version 0.07 (2011-10-19) #

**_New widget_**

  * Login Dialog Box

**_Fixes_**

  * Dialog : MessageArea and footer area are visible
  * Fix a bug with resources in the jar file

# version 0.06.1 (2011-10-16) #

  * Finish the documentation on the wiki
  * Rename packages to use lowercase
  * Code cleaning

# version 0.06 (2011-10-07) #

**_New widgets_**

  * Header
  * Titled Separator

**_Fixes_**

  * Columns : avoid a memory leak
  * Angle : avoid flickering
  * Fix comments
  * SWTGraphicUtil is no longer a singleton

# version 0.05 (2011-08-09) #

**_New widgets_**

  * Checkbox group
  * Heap Manager
  * Column browser
  * Launcher

**_Fixes_**

  * Improve performances
  * Avoid flickering

# version 0.04 (2011-06-10) #

**_New widgets_**

  * Horizonal Spinner
  * DarkPanel and BlurredPanel


# version 0.03 (2011-06-02) #

First official release of Opal, including the following widgets :
  * Multichoice
  * Text assist
  * Angle Slider
  * Brushed Metal Composite
  * Dual list selector
  * Infinite progress panel
  * Switch button
  * Gradient Composite
  * Fancy image selector
  * Fancy dialog box