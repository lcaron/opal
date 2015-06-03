# Introduction #

![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/notifier.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/notifier.png)

# Usage #

This component is dedicated to notify users. When a notification appears, a window slides in the bottom right corner on the screen.

The user can close this window by pressing the red cross. After about 5 seconds, the notification window fades out.

3 colors set (called themes) are available : YELLOW (default), BLUE or GRAY.

The code is pretty simple :

```
Notifier.notify("New Mail message", "Laurent CARON (lcaron@...)<br/><br/>Test message ...");
Notifier.notify("New Mail message", "Laurent CARON (lcaron@...)<br/><br/>Test message with blue theme...", NotifierTheme.BLUE_THEME);
```

4 methods are available :
  * `public static void notify(final String title, final String text)`
  * `public static void notify(final Image image,final String title, final String text)`
  * `public static void notify(final String title, final String text, final NotifierTheme theme)`
  * `public static void notify(final Image image,final String title, final String text, final NotifierTheme theme)`

You can change  the image or the theme.

The text can contains some pseudo-HTML tags for formatting :
  * `<br/>` for adding a line break
  * `<i>`...`</i>` to render text in italic
  * `<u>`...`</u>` to render text in underline
  * `<b>`...`</b>` to render text in bold
  * `<size>`...`</size>` to increase/decrease text size. You can use the following syntaxes : <size=10> (10px), <size=+4>, <size=-4>
  * `<color>`...`</color>` to change foreground color. You can use the following syntaxes : `<color=#FFCCAA>` (HTML color code), `<color=9,255,10>` (RGB values) and `<color=aliceblue>` (HTML color code)
  * `<backgroundcolor>`...`</backgroundcolor>` to change background color. You can use the following syntaxes : `<backgroundcolor=#FFCCAA>` (HTML color code), `<backgroundcolor=9,255,10>` (RGB values) and `<backgroundcolor=aliceblue>` (HTML color code)


# Example #

An example called **NotifierSnippet** is available in the directory **tests/org/mihalis/opal/notify**

You can also have a look here : [http://code.google.com/a/eclipselabs.org/p/opal/source/browse/src/test/java/org/mihalis/opal/notify/NotifierSnippet.java](http://code.google.com/a/eclipselabs.org/p/opal/source/browse/src/test/java/org/mihalis/opal/notify/NotifierSnippet.java)