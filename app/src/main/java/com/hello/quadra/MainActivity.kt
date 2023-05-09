package com.hello.quadra

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hello.quadra.ui.theme.QuadraTheme
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.widget.EditText
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlin.math.sqrt


class MainActivity : ComponentActivity() {

    private lateinit var aTextField: EditText
    private lateinit var bTextField: EditText
    private lateinit var cTextField: EditText
    private lateinit var calculateButton: Button
    private lateinit var solutionTextView: TextView

    @SuppressLint("SetTextI18n")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            QuadraTheme {
//                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {

                        val a = remember {
                            mutableStateOf(TextFieldValue())
                        }
                        val b = remember {
                            mutableStateOf(TextFieldValue())
                        }
                        val c = remember {
                            mutableStateOf(TextFieldValue())
                        }
                        val solutions = remember {
                            mutableStateOf<Pair<String?, String?>>(null to null)
                        }
                        val  errorMessage = remember {
                            mutableStateOf("")
                        }
                        OutlinedTextField(
                            value = a.value,
                            onValueChange = { a.value = it},
                            label = { Text("a") },
                            isError = a.value.text.toDoubleOrNull() == null && a.value.text.isNotEmpty(),
                            modifier = Modifier
                                .padding(vertical = 8.dp, horizontal = 16.dp)
                                .fillMaxWidth(),
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // EditText for input of b
                        OutlinedTextField(
                            value = b.value,
                            onValueChange = { b.value = it},
                            label = { Text("b") },
                            isError = b.value.text.toDoubleOrNull() == null && b.value.text.isNotEmpty(),
                            modifier = Modifier
                                .padding(vertical = 8.dp, horizontal = 16.dp)
                                .fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // EditText for input of c
                        OutlinedTextField(
                            value = c.value,
                            onValueChange = { c.value = it },
                            label = { Text("c") },
                            isError = c.value.text.toDoubleOrNull() == null && c.value.text.isNotEmpty(),
                            modifier = Modifier
                                .padding(vertical = 8.dp, horizontal = 16.dp)
                                .fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Button to solve the quadratic equation
                        Button(
                            onClick = {
                                val aVal = a.value.text.toDoubleOrNull() ?: 0.0
                                val bVal = b.value.text.toDoubleOrNull() ?: 0.0
                                val cVal = c.value.text.toDoubleOrNull() ?: 0.0

                                if (aVal == null || bVal == null || cVal == null) {
                                    errorMessage.value =
                                        "Please enter valid values for a, b, and c"
                                    solutions.value = null to null
                                } else if (aVal == 0.0) {
                                    errorMessage.value =
                                        "The value of a must be non-zero"
                                    solutions.value = null to null
                                } else {
                                    errorMessage.value = ""
                                    solutions.value =
                                        solveQuadratic(aVal, bVal, cVal)
                                }
                            },
                            modifier = Modifier
                                .padding(vertical = 16.dp, horizontal = 20.dp)
                                .fillMaxWidth()
                        ) {
                            Text("Solve")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (errorMessage.value.isNotEmpty()) {
                            Text(
                                text = errorMessage.value,
                                color = Color.Red,
                                modifier =
                                Modifier.padding(horizontal=16.dp)
                                    .fillMaxWidth()
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                            )
                        }

                        // TextView to display the solution
                        if (solutions.value.first != null && solutions.value.second != null) {
                            Text(
                                text =
                                if (solutions.value.first != null && solutions.value.second != null) {
                                    "x1=${solutions.value.first}, x2=${solutions.value.second}"
                                } else {
                                    "No real solutions"
                                },
                                modifier = Modifier.padding(vertical = 16.dp, horizontal = 20.dp)
                                    .fillMaxWidth().wrapContentWidth(
                                    Alignment.CenterHorizontally
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun solveQuadratic(a: Double, b: Double, c: Double): Pair<String?, String?> {
        val discriminant = b * b - 4 * a * c
        if(discriminant < 0) {
            val realPart = (-b / (2 * a)).toString()
            val imaginaryPart = (sqrt(-discriminant) / (2 * a)).toString()
            return Pair(
                "$realPart + $imaginaryPart i",
                "$realPart - $imaginaryPart i"
            )
        }
        val x1 = (-b + sqrt(discriminant)) / (2 * a)
        val x2 = (-b - sqrt(discriminant)) / (2 * a)
        return Pair(x1.toString(), x2.toString())
    }
}