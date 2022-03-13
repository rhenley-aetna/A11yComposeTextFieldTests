# A11yComposeTextFieldTests

Attempts to solve the keyboard trap issue of Jetpack Compose TextField using IMEActions, etc., based on techniques from 
[Android’s IME Actions: Don’t ignore them](https://proandroiddev.com/androids-ime-actions-don-t-ignore-them-36554da892ac).
Unfortunately, these methods don't eliminate the problem that TextFields eat Tabs and Enter characters,
adding them the TextField value, as demonstrated by this project.

![Screenshot_20220313-104816 A11yComposeTextFieldTests 3](https://user-images.githubusercontent.com/85311885/158065264-0f489e61-055d-4f2b-ae42-828715e4cdea.png)
