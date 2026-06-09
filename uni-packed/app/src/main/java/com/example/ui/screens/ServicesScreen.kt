package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Catalog
import com.example.data.model.StudentService
import com.example.ui.viewmodel.MainViewModel

@Composable
fun ServicesScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedServiceForDialogue by remember { mutableStateOf<StudentService?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // --- PROMINENT HEADER ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .statusBarsPadding()
                .padding(vertical = 16.dp, horizontal = 20.dp)
        ) {
            Column {
                Text(
                    "Student Onboarding Services",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Official partnerships • Native translators • Fast processing",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Box(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(
                        "Review our official student integration programs. Securing these eliminates standard settling barriers, like bank translation bottlenecks and airport navigation stress.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }

            items(Catalog.servicesList) { service ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedServiceForDialogue = service }
                        .border(1.dp, Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val icon = when (service.category) {
                                        "SIM" -> Icons.Default.Call
                                        "Onboarding" -> Icons.Default.DirectionsTransit
                                        "Admin" -> Icons.Default.Info
                                        else -> Icons.Default.Face
                                    }
                                    Icon(icon, service.category, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Text(
                                    service.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.widthIn(max = 200.dp)
                                )
                            }

                            HighlightBadge(
                                text = service.estProcessingTime.uppercase(),
                                backgroundColor = MaterialTheme.colorScheme.secondary
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            service.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "${String.format("%,d", service.price.toInt())} ₩",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            )

                            Button(
                                onClick = {
                                    viewModel.addToCart(
                                        com.example.data.model.Product(
                                            id = service.id,
                                            name = service.title,
                                            description = service.description,
                                            price = service.price,
                                            krwPrice = service.price.toInt(),
                                            category = "Service",
                                            weightKg = 0.0
                                        )
                                    )
                                    Toast.makeText(context, "${service.title} added to cart!", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("buy_service_btn")
                            ) {
                                Text("Secure Service")
                            }
                        }
                    }
                }
            }
        }
    }

    // --- SERVICES DETAILED INSTRUCTIONS DIALOGUE ---
    selectedServiceForDialogue?.let { svc ->
        AlertDialog(
            onDismissRequest = { selectedServiceForDialogue = null },
            title = {
                Text(svc.title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 16.sp)
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 380.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        svc.description,
                        fontSize = 13.sp,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("📝 Key Features Covered:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    svc.features.forEach { ft ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                            Icon(Icons.Default.Check, "check", tint = Color(0xFF2E7D32), modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(ft, fontSize = 12.sp, color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("🎓 How It Works (Instructions):", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(
                        svc.detailGuide,
                        fontSize = 12.sp,
                        color = Color.DarkGray,
                        modifier = Modifier
                            .background(Color.LightGray.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("⏳ Estimated Process Speed:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(svc.estProcessingTime, fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.addToCart(
                            com.example.data.model.Product(
                                id = svc.id,
                                name = svc.title,
                                description = svc.description,
                                price = svc.price,
                                krwPrice = svc.price.toInt(),
                                category = "Service",
                                weightKg = 0.0
                            )
                        )
                        selectedServiceForDialogue = null
                        Toast.makeText(context, "${svc.title} added!", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text("Secure Setup Booking")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedServiceForDialogue = null }) {
                    Text("Dismiss")
                }
            }
        )
    }
}
