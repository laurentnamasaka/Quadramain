package com.hello.quadra

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
import kotlin.math.sqrt


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val aEditText = findViewById<EditText>(R.id.aEditText)
        val bEditText = findViewById<EditText>(R.id.bEditText)
        val cEditText = findViewById<EditText>(R.id.cEditText)
        val solveButton = findViewById<Button>(R.id.solveButton)
        val solutionTextView = findViewById<TextView>(R.id.solutionTextView)

        solveButton.setOnClickListener {
            val a = aEditText.text.toString().toDoubleOrNull() ?: 0.0
            val b = bEditText.text.toString().toDoubleOrNull() ?: 0.0
            val c = bEditText.text.toString().toDoubleOrNull() ?: 0.0
            val solutions = solveQuadratic(a, b , c)
            solutionTextView.text = "x1 = ${solutions.first}, x2 = ${solutions.second}"
        }
//        setContent {
//            QuadraTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    Greeting("Android")
//                }
//            }
//        }
    }

    private fun solveQuadratic(a: Double, b: Double, c: Double): Pair<Double?, Double?> {
        val discriminant = b * b - 4 * a * c
        if(discriminant < 0) {
            return  Pair(null, null)
        }
        val x1 = (-b + sqrt(discriminant)) / (2 * a)
        val x2 = (-b - sqrt(discriminant)) / (2 * a)
        return Pair(x1, x2)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QuadraTheme {
        Greeting("Android")
    }
}