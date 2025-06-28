// Enhanced ProfileScreen.kt
package com.example.medilinkapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medilinkapp.network.models.UserRole
import com.example.medilinkapp.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

// Updated Theme
val BackgroundColor = Color(0xFFF4FCFC)
val CardColor = Color(0xFFFFFFFF)
val BorderColor = Color(0xFFBCE3E3)
val PrimaryAccent = Color(0xFF009688)
val SecondaryAccent = Color(0xFFB2DFDB)
val TextPrimary = Color(0xFF1B1B1B)
val TextSecondary = Color(0xFF5F6368)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(paddingValues: PaddingValues) {
    val viewModel: AuthViewModel = viewModel()
    val showSignup by viewModel.showSignup.collectAsState()
    val selectedRole by viewModel.selectedRole.collectAsState()
    val error by viewModel.error.collectAsState()
    val authResult by viewModel.authResult.collectAsState()
    val hasToken by viewModel.hasToken.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(authResult) {
        if (authResult != null && !showSignup) {
            scope.launch {
                snackbarHostState.showSnackbar("Logged in successfully!")
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = BackgroundColor,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "👤 Profile",
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        color = TextPrimary
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = CardColor
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                tonalElevation = 4.dp,
                color = CardColor,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    error?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    authResult?.let {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(bottom = 24.dp)
                        ) {
                            Text(
                                text = "Welcome, ${it.name}!",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Text(text = "📧 ${it.email}", style = MaterialTheme.typography.bodyLarge, color = TextSecondary)
                            Text(text = "🔰 ${it.role}", style = MaterialTheme.typography.bodyLarge, color = TextSecondary)
                            if (!it.verifiedEmail) {
                                Text(
                                    text = "Please verify your email",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }

                    if (hasToken) {
                        Button(
                            onClick = { viewModel.logout() },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryAccent),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        ) {
                            Text("Logout")
                        }
                    } else {
                        Text(
                            text = "Select Role:",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.align(Alignment.Start),
                            color = TextPrimary
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            UserRole.values().forEach { role ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = selectedRole == role,
                                        onClick = { viewModel.updateRole(role) },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = PrimaryAccent
                                        )
                                    )
                                    Text(
                                        role.value.replaceFirstChar { it.uppercase() },
                                        color = TextSecondary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryAccent,
                                unfocusedBorderColor = BorderColor,
                                cursorColor = PrimaryAccent,
                                focusedLabelColor = PrimaryAccent,
                                unfocusedLabelColor = TextSecondary,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryAccent,
                                unfocusedBorderColor = BorderColor,
                                cursorColor = PrimaryAccent,
                                focusedLabelColor = PrimaryAccent,
                                unfocusedLabelColor = TextSecondary,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )

                        if (showSignup) {
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("Name") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryAccent,
                                    unfocusedBorderColor = BorderColor,
                                    cursorColor = PrimaryAccent,
                                    focusedLabelColor = PrimaryAccent,
                                    unfocusedLabelColor = TextSecondary,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                if (showSignup) {
                                    viewModel.signup(email, password, name)
                                } else {
                                    viewModel.login(email, password)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryAccent)
                        ) {
                            Text(if (showSignup) "Sign Up" else "Login")
                        }

                        TextButton(
                            onClick = { viewModel.toggleSignup() },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text(
                                if (showSignup) "Already have an account? Login"
                                else "New user? Sign up",
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}
