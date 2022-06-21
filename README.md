# A11yComposeTextFieldTests

Attempts to solve the keyboard trap issue of Jetpack Compose TextField using IMEActions, etc., based on techniques from 
[Android’s IME Actions: Don’t ignore them](https://proandroiddev.com/androids-ime-actions-don-t-ignore-them-36554da892ac).
Unfortunately, these methods don't eliminate the problem that TextFields eat Tabs and Enter characters,
adding them to the TextField's value, as demonstrated by this project.


![Screenshot_20220313-104816 A11yComposeTextFieldTests 3](https://user-images.githubusercontent.com/85311885/158065264-0f489e61-055d-4f2b-ae42-828715e4cdea.png)

Adopting the Modifier.onPreviewKeyEvent() technique suggested by 
[What is the simplest way to set the focus order in Jetpack Compose](https://stackoverflow.com/questions/66817856/what-is-the-simplest-way-to-set-the-focus-order-in-jetpack-compose)
can fix the problem with the TextField taking Tab and Enter characters as part of the text.
However, that technique results in other problems: e.g., Tabs will skip past the next following TextField, 
landing focus on the next focusable field (or no on-screen focus), 
presumably because the preview processing doesn't consume the Tab.
The sample was expanded with a Submit button and a third TextField to better explore this behavior.

Unlike the StackOverflow article on focus order, this code handles Shift-tabbing to move backwards,
as well as the using the Enter key to submit the form. However, Enter processing also exhibits 
strange behavior, triggering the onDone() method twice.

Using Modifier.onKeyEvent() may handle focus control better, 
but would result in the Tab or Enter being absorbed by the TextField in which it occurs, 
in addition to triggering focus change.

More research is required, as is testing with newer versions of Compose.

![Screenshot_20220621-134430 A11yComposeTextFieldTests 4](https://user-images.githubusercontent.com/85311885/174866717-17375de3-afaf-4a5e-8256-e9befd1d25a0.png)
