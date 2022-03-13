package com.example.a11ycomposetextfieldtests

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
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
            text = "Try to use an external keyboard to shift focus from one text field to the other with Tab or to submit the form with Enter",
            modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
            style = TextStyle(fontSize = 14.sp),
        )
        // Username Field
        TextField(
            value = name.value,
            onValueChange = { name.value = it },
            modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
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
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )
        )

        // Password Field
        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Enter your password")
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    Toast.makeText(
                        context,
                        "Username: ${name.value}/Password: ${password.value} entered",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    A11yComposeTextFieldTestsTheme {
        NamePasswordPair()
    }
}