package com.example.medilinkapp.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

// 🎨 Color Theme - Renamed to prevent conflicts
val ReportsBackgroundColor = Color(0xFFF6F9FC)
val ReportsCardPrimary = Color(0xFFEDF3FB)
val ReportsCardSecondary = Color(0xFFE0F2F1)
val ReportsAccentColor = Color(0xFF00695C)
val ReportsTextPrimary = Color(0xFF263238)
val ReportsTextSecondary = Color.Gray

data class UploadedFile(
    val uri: Uri,
    val name: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(paddingValues: PaddingValues, userName: String) {
    val reports = listOf(
        "Blood Test - 12th Jan 2025",
        "X-Ray - 25th Feb 2025",
        "MRI Scan - 10th March 2025"
    )

    val uploadedFiles = remember { mutableStateListOf<UploadedFile>() }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            val name = uri.lastPathSegment ?: "Uploaded Report"
            uploadedFiles.add(UploadedFile(uri = it, name = name))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ReportsBackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Hello $userName",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = ReportsTextPrimary
            )
            Text(
                text = "Here’s your health overview",
                fontSize = 16.sp,
                color = ReportsTextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatCard(
                    title = "Focus Time",
                    value = "2h 15m",
                    bgColor = ReportsCardPrimary,
                    icon = Icons.Default.Description
                )
                StatCard(
                    title = "Mood Level",
                    value = "7/10",
                    bgColor = ReportsCardPrimary,
                    icon = Icons.Default.Warning
                )
            }

            Spacer(modifier = Modifier.height(13.dp))

            SleepProgressCard(
                achieved = 6.5f,
                target = 8f
            )

            Spacer(modifier = Modifier.height(13.dp))

            Text(
                text = "Recent Reports",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = ReportsTextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    launcher.launch(arrayOf("application/pdf", "image/*"))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Upload Report")
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(reports) { report ->
                    ReportCard(title = report, fileUrl = "https://example.com")
                }

                items(uploadedFiles) { uploaded ->
                    UploadedReportCard(file = uploaded)
                }
            }

            Spacer(modifier = Modifier.height(13.dp))

            AnimatedVisibility(visible = true) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = ReportsCardSecondary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(20.dp))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = ReportsAccentColor,
                            modifier = Modifier
                                .size(28.dp)
                                .padding(end = 12.dp)
                        )
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Drink 2L water today",
                                    fontSize = 13.sp,
                                    color = ReportsTextPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.InvertColors,
                                    contentDescription = "Water Drop",
                                    tint = Color(0xFF29B6F6),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    bgColor: Color
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(100.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = ReportsAccentColor
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun UploadedReportCard(file: UploadedFile) {
    val context = LocalContext.current
    val mimeType = context.contentResolver.getType(file.uri)
    val isPdf = mimeType?.contains("pdf") == true

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ReportsCardPrimary),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(file.uri, mimeType ?: "*/*")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
                }
                context.startActivity(intent)
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            if (isPdf) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = ReportsAccentColor,
                    modifier = Modifier.size(28.dp)
                )
            } else {
                AsyncImage(
                    model = file.uri,
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .padding(end = 12.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = file.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
        }
    }
}

@Composable
fun ReportCard(title: String, fileUrl: String) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(fileUrl)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            },
        colors = CardDefaults.cardColors(containerColor = ReportsCardPrimary),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Description,
                contentDescription = null,
                tint = ReportsAccentColor,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text("Tap to view", fontSize = 12.sp, color = ReportsTextSecondary)
            }
        }
    }
}

@Composable
fun SleepProgressCard(achieved: Float, target: Float) {
    val progress = achieved / target
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ReportsCardSecondary),
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(
                progress = progress,
                strokeWidth = 8.dp,
                color = ReportsAccentColor,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Sleep Goal", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("$achieved h slept out of $target h", fontSize = 14.sp)
                Text("${target - achieved} h deficit", fontSize = 13.sp, color = Color.Red)
            }
        }
    }
}
