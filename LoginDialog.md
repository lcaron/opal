# Introduction #

![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/login1.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/login1.png)
![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/login2.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/login2.png)

This component is very simple and present a simple and flexible dialog box to authentify a user.
Inspired by SwingX.

# Usage #

The first thing to do is to create a **Verifier** by instancing a object that implements the interface `LoginDialogVerifier`.

This interface contains one method : `public void authenticate(final String login, final String password) throws Exception`

In the implementation of this method, you put the code to authenticate the user (LDAP, DB, ...). If the couple login/password is incorrect or if something goes wrong (Database lost for example), the method throws an Exception. The detail message contains the description of the error :

```
final LoginDialogVerifier verifier = new LoginDialogVerifier() {

	@Override
	public void authenticate(final String login, final String password) throws Exception {
		if ("".equals(login)) {
			throw new Exception("Please enter a login.");
		}

		if ("".equals(password)) {
			throw new Exception("Please enter a password.");
		}

		if (!login.equalsIgnoreCase("laurent")) {
			throw new Exception("Login unknown.");
		}

		if (!password.equalsIgnoreCase("laurent")) {
			throw new Exception("Authentication failed, please check your password.");
		}

	}
};
```

Then, you create a LoginDialog object, you inject the **verifier** and you call the open() methods that returns `true` if the couple login/password is correct, or `false` if the user pressed on the **cancel** button.

```
final LoginDialog dialog = new LoginDialog();
dialog.setVerifier(verifier);

final boolean result = dialog.open();
if (result) {
	System.out.println("Login confirmed : " + dialog.getLogin());
} else {
	System.out.println("User canceled !");
}
```

You can customize the dialog box by setting the following properties :
  * **image**. By default, the header image is a blue wave with a title
  * **description**. A text located between the header and the login text box
  * **login**, **password**
  * **autorizedLogin**, if you want to limit the login to a set of users. In this case, the login text box will be replaced by a combo.
  * **displayRememberPassword**. If true, a checkbox "Remember my password" is displayed. In this case, you can get/set the checkbox by manipulating the property **rememberPassword**.


# Example #
An example called **LoginDialogSnippet.java** is available in the directory **src/test/java/org/mihalis/opal/login**.

This example is also available here : http://code.google.com/a/eclipselabs.org/p/opal/source/browse/src/test/java/org/mihalis/opal/login/LoginDialogSnippet.java