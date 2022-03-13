# A11yComposeTextFieldTests

Attempts to solve the keyboard trap issue of Jetpack Compose TextField using IMEActions, etc., based on techniques from 
[Android’s IME Actions: Don’t ignore them.](https://proandroiddev.com/androids-ime-actions-don-t-ignore-them-36554da892ac).
Unfortunately, these methods don't eliminate the problem that TextFields eat Tabs and Enter characters,
adding them the TextField value, as demonstrated by this project.

