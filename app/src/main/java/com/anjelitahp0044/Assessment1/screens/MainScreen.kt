package com.anjelitahp0044.Assessment1.screens

import android.content.Intent
import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.anjelitahp0044.Assessment1.R
import com.anjelitahp0044.Assessment1.navigation.Screen
import com.anjelitahp0044.Assessment1.ui.theme.Assessment1
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, isDarkTheme: MutableState<Boolean>) {
    val input = rememberSaveable { mutableStateOf("") }
    val selectedUnit = rememberSaveable { mutableStateOf("Celsius") }
    val result = remember { mutableStateOf("") }
    val history = remember { mutableStateListOf<String>() }
    val showResult = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.temperature_icon),
                            contentDescription = "Thermometer Icon",
                            modifier = Modifier.size(50.dp).padding(end = 10.dp)
                        )
                        Text(text = stringResource(R.string.app_name))
                    }
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Text("🌞 / 🌙")
                        Switch(
                            checked = isDarkTheme.value,
                            onCheckedChange = { isDarkTheme.value = it }
                        )

                        var expanded by remember { mutableStateOf(false) }

                        IconButton(onClick = { expanded = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More Options"
                            )
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(id = R.string.about)) },
                                onClick = {
                                    expanded = false
                                    navController.navigate(Screen.About.route)
                                }
                            )
                        }
                    }
                }

            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                painter = painterResource(id = R.drawable.temp_color),
                contentDescription = "Banner Suhu",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .padding(vertical = 12.dp)
            )

            OutlinedTextField(
                value = input.value,
                onValueChange = { input.value = it },
                label = { Text(stringResource(R.string.input_label)) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(stringResource(R.string.select_unit))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("Celsius", "Fahrenheit", "Kelvin").forEach { unit ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedUnit.value == unit,
                            onClick = { selectedUnit.value = unit }
                        )
                        Text(unit)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val output = convertTemperature(input.value, selectedUnit.value)
                result.value = output
                if (output.isNotEmpty()) {
                    history.add(0, "${input.value} ${selectedUnit.value} = $output")
                }
                showResult.value = false
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
            Text(stringResource(R.string.convert))
        }

            LaunchedEffect(result.value) {
                if (result.value.isNotEmpty()) {
                    delay(100)
                    showResult.value = true
                }
            }

            AnimatedVisibility(
                visible = showResult.value,
                enter = fadeIn(animationSpec = tween(500)) + slideInVertically(),
                exit = fadeOut()
            ) {
                Column {
                    Text(
                        text = stringResource(id = R.string.result_title, result.value),
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(top = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val context = LocalContext.current
                    Button(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name))
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    context.getString(R.string.share_message, result.value)
                                )
                            }
                            context.startActivity(
                                Intent.createChooser(shareIntent, context.getString(R.string.share_via))
                            )
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text(text = stringResource(R.string.share_button))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(stringResource(R.string.conversion_history))
            LazyColumn {
                items(history) { item ->
                    Text("• $item", modifier = Modifier.padding(4.dp))
                }
            }
        }
    }
}

fun convertTemperature(input: String, unit: String): String {
    val value = input.toDoubleOrNull() ?: return ""
    return when (unit) {
        "Celsius" -> "F: ${value * 9 / 5 + 32}, K: ${value + 273.15}"
        "Fahrenheit" -> "C: ${(value - 32) * 5 / 9}, K: ${(value - 32) * 5 / 9 + 273.15}"
        "Kelvin" -> "C: ${value - 273.15}, F: ${(value - 273.15) * 9 / 5 + 32}"
        else -> ""
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    val isDark = rememberSaveable { mutableStateOf(false) }

    Assessment1(darkTheme = isDark.value) {
        MainScreen(navController = rememberNavController(), isDarkTheme = isDark)
    }
}

