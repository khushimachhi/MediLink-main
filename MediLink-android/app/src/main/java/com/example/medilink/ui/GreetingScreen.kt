package com.example.medilinkapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medilinkapp.R
import com.example.medilinkapp.viewmodel.GreetingViewModel
import kotlinx.coroutines.delay

/**
 * Composable function for the greeting screen
 * Displays a welcome message for 2 seconds before transitioning to main content
 *
 * @param viewModel ViewModel that provides the greeting message
 * @param onGreetingComplete Callback to be invoked after 2 seconds
 */
@Composable
fun GreetingScreen(
    viewModel: GreetingViewModel,
    onGreetingComplete: () -> Unit
) {
    // Collect the greeting message from ViewModel
    val greeting by viewModel.greeting.collectAsState()

    // Launch a coroutine to handle the 2-second delay
    LaunchedEffect(key1 = true) {
        delay(2000) // Show for 2 seconds
        onGreetingComplete() // Notify parent that greeting is complete
    }

    // Center the greeting message on screen
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.greeting_bg), // Your image name
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // To fill the screen nicely
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
                .padding(bottom = 30.dp)
        ) {
            // Circular Image
            Image(
                painter = painterResource(id = R.drawable.greeting_doc), // Replace with your image name
                contentDescription = null,
                modifier = Modifier
                    .size(180.dp) // Adjust size as needed
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            // Greeting Text
            Text(
                text = greeting,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight(600),
                color = Color(0xFF212121)
            )
        }
    }
}