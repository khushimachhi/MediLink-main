// Enhanced AppointmentsScreen.kt
package com.example.medilinkapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

// Color Palette for Appointments Screen
val AppointmentsBackgroundColor = Color(0xFFF4FCFC)
val AppointmentsCardColor = Color(0xFFFFFFFF)
val AppointmentsAccentColor = Color(0xFF009688)
val AppointmentsTextPrimary = Color(0xFF1B1B1B)
val AppointmentsTextSecondary = Color(0xFF5F6368)

// Data class
data class Appointment(
    val id: Int,
    val title: String,
    val date: String,
    val isUpcoming: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen(paddingValues: PaddingValues) {
    var appointments by remember {
        mutableStateOf(
            listOf(
                Appointment(1, "Dental Checkup", "2025-06-15", true),
                Appointment(2, "Eye Specialist Visit", "2025-06-20", true),
                Appointment(3, "General Physician", "2025-05-30", false),
                Appointment(4, "Orthopedic Follow-up", "2025-04-15", false)
            )
        )
    }

    var showForm by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
            ) {
                Text(
                    text = "📅 Appointments",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppointmentsTextPrimary
                )
                Text(
                    text = "Manage your health schedule",
                    fontSize = 16.sp,
                    color = AppointmentsTextSecondary
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showForm = true },
                containerColor = AppointmentsAccentColor,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Appointment")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = AppointmentsBackgroundColor
    ) { innerPadding ->

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .padding(bottom = 80.dp) // Extra padding for FAB
                    .fillMaxSize()
            ) {
                Text("Upcoming Appointments", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = AppointmentsTextPrimary)
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    appointments.filter { it.isUpcoming }.forEach { appointment ->
                        AppointmentCard(appointment) {
                            appointments = appointments.filter { it.id != appointment.id }
                            scope.launch { snackbarHostState.showSnackbar("Appointment deleted") }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text("Past Appointments", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = AppointmentsTextPrimary)
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    appointments.filter { !it.isUpcoming }.forEach { appointment ->
                        AppointmentCard(appointment) {
                            appointments = appointments.filter { it.id != appointment.id }
                            scope.launch { snackbarHostState.showSnackbar("Appointment deleted") }
                        }
                    }
                }
            }

            if (showForm) {
                AddAppointmentDialog(
                    onAdd = { title, date, isUpcoming ->
                        val newId = appointments.maxOfOrNull { it.id }?.plus(1) ?: 1
                        appointments = appointments + Appointment(newId, title, date, isUpcoming)
                        showForm = false
                        scope.launch { snackbarHostState.showSnackbar("Appointment added") }
                    },
                    onDismiss = { showForm = false }
                )
            }
        }
    }
}

@Composable
fun AppointmentCard(appointment: Appointment, onDelete: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppointmentsCardColor),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = appointment.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AppointmentsTextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "\uD83D\uDD59 ${appointment.date}",
                    fontSize = 14.sp,
                    color = AppointmentsTextSecondary
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
            }
        }
    }
}

@Composable
fun AddAppointmentDialog(onAdd: (String, String, Boolean) -> Unit, onDismiss: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var isUpcoming by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Appointment", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Date (YYYY-MM-DD)") })
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isUpcoming, onCheckedChange = { isUpcoming = it })
                    Text("Upcoming Appointment")
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (title.isNotBlank() && date.isNotBlank()) onAdd(title, date, isUpcoming)
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}
