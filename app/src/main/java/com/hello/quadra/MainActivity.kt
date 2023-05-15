package com.hello.quadra

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
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
import android.widget.LinearLayout
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.github.mikephil.charting.charts.LineChart
import kotlin.math.sqrt
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.delay


class MainActivity : ComponentActivity() {

    private lateinit var aTextField: EditText
    private lateinit var bTextField: EditText
    private lateinit var cTextField: EditText
    private lateinit var calculateButton: Button
    private lateinit var solutionTextView: TextView

    private lateinit var chart: LineChart

    private  lateinit var clear_button: Button
    private lateinit var graph_button: Button

    @SuppressLint("SetTextI18n", "UnrememberedMutableState",
        "UnusedMaterial3ScaffoldPaddingParameter"
    )
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        com.github.mikephil.charting.utils.Utils.init(this)
        setContentView(R.layout.activity_main)

        val chart = findViewById<LineChart>(R.id.chart)
        // Graph and clear buttons
        val clearButton = findViewById<Button>(R.id.clear_button)
        val graphButton = findViewById<Button>(R.id.graph_button)

        // Set the visibility of the chart to "visible"
        chart.visibility = View.VISIBLE

        setContent {
            QuadraTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Quadra") },
                            Modifier.background(color = MaterialTheme.colorScheme.onPrimary).shadow(elevation = 8.dp),
//                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                ) {
//                    Spacer(modifier = Modifier.height(50.dp))

                    Surface(
                        modifier = Modifier.fillMaxSize().padding(top = 50.dp),
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
                            val errorMessage = remember {
                                mutableStateOf("")
                            }

//                        val buttons = LinearLayout(
//                            modifier = Modifier.fillMaxWidth(),
//                            orientation = LinearLayout.Orientation.HORIZONTAL
//                        )

                            // Keepp track of user activity in order to show clear button
                            val firstAttemptMade = remember { mutableStateOf(false) }

                            clearButton.visibility = View.GONE
                            graphButton.visibility = View.GONE

                            clearButton.setOnClickListener {
                                aTextField.setText("")
                                bTextField.setText("")
                                cTextField.setText("")
                            }

                            graphButton.setOnClickListener {
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

                                    val inputValues = "a: $aVal, b: $bVal, c: $cVal"
//                                val result =
//                                    "x1: ${solutions.value.first}, x2: ${solutions.value.second}"
//                                history.add(Pair(inputValues, result))

                                    // Graph the quadratic equation
//                                val chart = findViewById<LineChart>(R.id.chart)
                                    val entries = mutableListOf<Entry>()

                                    for (x in -10..10) {
                                        val y = aVal * x * x + bVal * x + cVal
                                        entries.add(Entry(x.toFloat(), y.toFloat()))
                                    }

                                    val dataset = LineDataSet(entries, "Quadratic Equation")
                                    dataset.color = Color.Black.toArgb()
                                    dataset.valueTextColor = Color.Black.toArgb()

                                    val lineData = LineData(dataset)
                                    chart.data = lineData
                                    chart.invalidate()
                                }
                            }

                            // Button activity

                            var clicked = remember {
                                mutableStateOf(false)
                            }

                            var isCalculating by remember { mutableStateOf(false) }

                            val buttonColor by animateColorAsState(
                                if (clicked.value) Color.Green else MaterialTheme.colorScheme.primary
                            )

                            // MutableList to display calculations history
                            val history = remember {
                                mutableStateListOf<Pair<String, String>>()
                            }


                            OutlinedTextField(
                                value = a.value,
                                onValueChange = { a.value = it },
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
                                onValueChange = { b.value = it },
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
                                    isCalculating = true;

                                    firstAttemptMade.value = true

                                    clicked = clicked
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

                                        val inputValues = "a: $aVal, b: $bVal, c: $cVal"
                                        val result =
                                            "x1: ${solutions.value.first}, x2: ${solutions.value.second}"
                                        history.add(Pair(inputValues, result))


                                        // Graph
                                        val entries = mutableListOf<Entry>()

                                        fun getNumericValue(textFieldValue: TextFieldValue): Double {
                                            return try {
                                                textFieldValue.text.toDouble()
                                            } catch (e: NumberFormatException) {
                                                // Handle the exception here
                                                0.0 // Default value in case of an exception
                                            }
                                        }

                                        val aNumeric = getNumericValue(a.value)
                                        val bNumeric = getNumericValue(b.value)
                                        val cNumeric = getNumericValue(c.value)

                                        for (x in -10..10) {
                                            val y = aNumeric * x * x + bNumeric * x + cNumeric
                                            entries.add(Entry(x.toFloat(), y.toFloat()))
                                        }

                                        val dataset = LineDataSet(entries, "Quadratic Equation")
                                        dataset.color = Color.Black.toArgb()
                                        dataset.valueTextColor = Color.Black.toArgb()

                                        val lineData = LineData(dataset)
                                        chart.data = lineData
                                        chart.invalidate()

                                        // Show the buttons only after the user has solved their first question
                                        clearButton.visibility = View.VISIBLE
                                        graphButton.visibility = View.VISIBLE
                                    }
                                },
                                modifier = Modifier
                                    .padding(vertical = 16.dp, horizontal = 20.dp)
                                    .fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                            ) {
                                if (isCalculating) {
                                    CircularProgressIndicator(
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    LaunchedEffect(Unit) {
                                        delay(1000)
                                        isCalculating = false;
                                    }
                                } else {
                                    Text("Solve")
                                }
                            }

//                        Button(
//                            onClick = {
//                                // Graph the equation
//                                val aVal = a.value.text.toDoubleOrNull() ?: 0.0
//                                val bVal = b.value.text.toDoubleOrNull() ?: 0.0
//                                val cVal = c.value.text.toDoubleOrNull() ?: 0.0
//
//                                if (aVal == null || bVal == null || cVal == null) {
//                                    errorMessage.value =
//                                        "Please enter valid values for a, b, and c"
//                                    solutions.value = null to null
//                                } else if (aVal == 0.0) {
//                                    errorMessage.value =
//                                        "The value of a must be non-zero"
//                                    solutions.value = null to null
//                                } else {
//                                    errorMessage.value = ""
//                                    solutions.value =
//                                        solveQuadratic(aVal, bVal, cVal)
//
//                                    // Set the visibility of the chart to "visible"
//                                    chart.visibility = View.VISIBLE
//
//                                    // Add the data to the chart
//                                    val entries = mutableListOf<Entry>()
//
//                                    for (x in -10..10) {
//                                        val y = aVal * x * x + bVal * x + cVal
//                                        entries.add(Entry(x.toFloat(), y.toFloat()))
//                                    }
//
//                                    val dataset = LineDataSet(entries, "Quadratic Equation")
//                                    dataset.color = Color.Black.toArgb()
//                                    dataset.valueTextColor = Color.Black.toArgb()
//
//                                    val lineData = LineData(dataset)
//                                    chart.data = lineData
//                                    chart.invalidate()
//
//                                    // Show the buttons only after the user has solved their first question
//                                    clearButton.visibility = View.VISIBLE
//                                    graphButton.visibility = View.VISIBLE
//                                }
//                            },
//                            modifier = Modifier
//                                .padding(vertical = 8.dp, horizontal = 16.dp)
//                                .fillMaxWidth(),
//                            colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
//                        ) {
//                            Text("Graph")
//                        }

                            if(firstAttemptMade.value) {
                                Button(
                                    onClick = {
                                        // Clear the input fields
                                        a.value = TextFieldValue()
                                        a.value = TextFieldValue()
                                        a.value = TextFieldValue()
                                    },
                                    modifier = Modifier
                                        .padding(vertical = 8.dp, horizontal = 16.dp)
                                        .fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                                ) {
                                    Text("Clear")
                                }
                            }

                            Text(
                                text = "${a.value.text}x² + ${b.value.text}x + ${c.value.text} = 0",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .padding(vertical = 16.dp, horizontal = 20.dp)
                                    .fillMaxWidth()
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            if (errorMessage.value.isNotEmpty()) {
                                Text(
                                    text = errorMessage.value,
                                    color = Color.Red,
                                    modifier =
                                    Modifier
                                        .padding(horizontal = 16.dp)
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
                                    modifier = Modifier
                                        .padding(vertical = 16.dp, horizontal = 20.dp)
                                        .fillMaxWidth()
                                        .wrapContentWidth(
                                            Alignment.CenterHorizontally
                                        )
                                )
                            }

                            LazyColumn {
                                items(history.size) { index ->
                                    val item = history[index]
                                    Text(text = "Input: ${item.first}")
                                    Text(text = "Input: ${item.second}")
                                }
                            }
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