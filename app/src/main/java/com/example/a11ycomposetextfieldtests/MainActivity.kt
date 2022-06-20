package com.example.a11ycomposetextfieldtests

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a11ycomposetextfieldtests.ui.theme.A11yComposeTextFieldTestsTheme

/**
 * Attempts to solve the keyboard trap issue of Jetpack Compose TextField using the techniques from:
 * https://proandroiddev.com/androids-ime-actions-don-t-ignore-them-36554da892ac.
 *
 * These techniques don't resolve the problem that keyboard Tab and Enter characters are eaten by
 * TextFields as characters to be added to the field value.
 */
@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            A11yComposeTextFieldTestsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NamePasswordPair()
                }
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun NamePasswordPair() {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val name = remember { mutableStateOf("")}
    val password = remember { mutableStateOf("")}

    Column (
        Modifier.padding(all = 16.dp)
    ) {
        // Heading text
        Text(
            text = "Keyboard Trap Demonstration",
            modifier = Modifier.semantics { heading() },
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
        )
        Text(
            text = "Try to use an external keyboard to shift focus from one text field to the other with Tab or to submit the form with Enter.",
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            style = TextStyle(fontSize = 14.sp),
        )
        // Username Field
        TextField(
            value = name.value,
            onValueChange = { name.value = it },
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
                .onPreviewKeyEvent { keyEvent ->
                    // A forward tab from a prior non-TextField (either no initial focus or from
                    // the Submit button) or a back tab from the following TextField will cause this
                    // field to be skipped over. Enter causes onDone() to be called twice.
                    if (keyEvent.key == Key.Tab) {
                        if (keyEvent.nativeKeyEvent.isShiftPressed) {
                            focusManager.moveFocus(FocusDirection.Previous)
                        } else {
                            focusManager.moveFocus(FocusDirection.Next)
                        }
                        true
                    } else if (keyEvent.key == Key.Enter || keyEvent.key == Key.NumPadEnter) {
                        onDone(context, name.value, password.value)
                        true
                    } else {
                        false
                    }
                    // The following code is exactly as recommended -- and doesn't work either.
//                    if (keyEvent.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_TAB) {
//                        focusManager.moveFocus(FocusDirection.Next)
//                        true
//                    } else {
//                        false
//                    }
                },
            label = {
                Text(text = "Enter your username")
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    // Pressing Ime button would move the text indicator's focus to the bottom
                    // field, if it exists!
                    focusManager.moveFocus(FocusDirection.Next)
                }
            )
        )

        // Password Field
        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .onPreviewKeyEvent { keyEvent ->
                    // A a forward tab from the prior TextField will cause this field to be skipped
                    // over, as will a back tab from a later non-input field (the Submit button).
                    // Enter causes onDone() to be called twice.
                    if (keyEvent.key == Key.Tab) {
                        if (keyEvent.nativeKeyEvent.isShiftPressed) {
                            focusManager.moveFocus(FocusDirection.Previous)
                        } else {
                            focusManager.moveFocus(FocusDirection.Next)
                        }
                        true
                    } else if (keyEvent.key == Key.Enter || keyEvent.key == Key.NumPadEnter) {
                        onDone(context, name.value, password.value)
                        true
                    } else {
                        false
                    }
                    // The following code is exactly as recommended -- and doesn't work either.
//                    if (keyEvent.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_TAB) {
//                        focusManager.moveFocus(FocusDirection.Next)
//                        true
//                    } else {
//                        false
//                    }
                },
            label = {
                Text(text = "Enter your password")
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onDone(context, name.value, password.value)
                }
            )
        )

        Button(
            onClick = {
                onDone(context, name.value, password.value)
            }
        ) {
            Text(text = "Submit")
        }
    }
}

private fun onDone(
    context: Context,
    name: String,
    password: String
) {
    Toast.makeText(
        context,
        "Username: '${name}'; Password: '${password}' entered",
        Toast.LENGTH_SHORT
    ).show()
}

@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    A11yComposeTextFieldTestsTheme {
        NamePasswordPair()
    }
}