package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Catalog
import com.example.ui.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToTab: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.userProfile.collectAsState()
    val currentUniv by viewModel.currentUnivInfo.collectAsState()
    val scrollState = rememberScrollState()

    // Dynamically calculate countdown to arrival date (defaults to 2026-08-25)
    val countdownDays = remember(profile.arrivalDate) {
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val arrival = sdf.parse(profile.arrivalDate) ?: Date()
            val today = sdf.parse("2026-06-08") ?: Date() // System's fixed context date
            val diffMs = arrival.time - today.time
            val days = diffMs / (1000 * 60 * 60 * 24)
            days.coerceAtLeast(0)
        } catch (e: Exception) {
            78L // default fallback
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC)) // Brand clean slate background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // --- PROFESSIONAL POLISH HEADER ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "UNIVERSITY PARTNER",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2563EB), // Tailwind blue-600
                        letterSpacing = 1.2.sp
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = currentUniv.EnglishName,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = Color(0xFF1C1B1F) // Theme light body text color
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Select Partner",
                            tint = Color(0xFF2563EB),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Header user initials badge
                val initials = if (profile.name.isNotBlank()) {
                    val parts = profile.name.trim().split("\\s+".toRegex())
                    if (parts.size >= 2) {
                        "${parts[0].take(1)}${parts[1].take(1)}".uppercase()
                    } else {
                        profile.name.take(2).uppercase()
                    }
                } else "AL"

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFDBEAFE), CircleShape) // blue-100
                        .border(2.dp, Color.White, CircleShape)
                        .shadow(1.dp, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials,
                        color = Color(0xFF1D4ED8), // blue-700
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    )
                }
            }

            // --- ARRIVAL COUNTDOWN WIDGET ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .shadow(elevation = 1.dp, shape = RoundedCornerShape(24.dp), clip = false),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Days to Arrival",
                            fontSize = 13.sp,
                            color = Color(0xFF64748B),
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "$countdownDays Days",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A),
                            letterSpacing = (-0.5).sp
                        )
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(Color(0xFF2563EB), CircleShape)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Flight KE-129 • Delayed",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2563EB)
                            )
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Chuncheon, KR",
                                fontSize = 11.sp,
                                color = Color(0xFF94A3B8)
                            )
                            Text(
                                text = "18°C • Sunny",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .background(Color(0xFFF1F5F9), RoundedCornerShape(100.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Dorm D-02",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF475569)
                            )
                        }
                    }
                }
            }

            // --- AI ASSISTANT PILL ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable { onNavigateToTab("community") },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E7FF))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color.White, RoundedCornerShape(16.dp))
                                .shadow(1.dp, RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SmartToy,
                                contentDescription = "Uni AI",
                                tint = Color(0xFF2563EB),
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = "Ask Uni",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF1E1B4B)
                            )
                            Text(
                                text = "\"How do I get my ARC at Kangwon?\"",
                                fontSize = 12.sp,
                                color = Color(0xFF4338CA)
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Navigate to Chat",
                        tint = Color(0xFF818CF8),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // --- MARKETPLACE CATEGORY GRID ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CategoryGridCard(
                        title = "Arrival Kits",
                        desc = "Bundled Essentials",
                        emoji = "📦",
                        bgColor = Color(0xFFE7F5E8),
                        borderColor = Color(0xFFCDE5D0),
                        textColor = Color(0xFF1B4332),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToTab("kits") }
                    )
                    CategoryGridCard(
                        title = "Food Market",
                        desc = "Halal & Global Brands",
                        emoji = "🍜",
                        bgColor = Color(0xFFFFF4E5),
                        borderColor = Color(0xFFFFE7CC),
                        textColor = Color(0xFF9A3412),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToTab("foods") }
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CategoryGridCard(
                        title = "Services",
                        desc = "SIM, Banking & KYC",
                        emoji = "📑",
                        bgColor = Color(0xFFF2EFFF),
                        borderColor = Color(0xFFE4DCFF),
                        textColor = Color(0xFF4338CA),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToTab("services") }
                    )
                    CategoryGridCard(
                        title = "Community",
                        desc = "Gangwon Student Hub",
                        emoji = "🌏",
                        bgColor = Color(0xFFE5F7FF),
                        borderColor = Color(0xFFD0EFFF),
                        textColor = Color(0xFF0E7490),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToTab("community") }
                    )
                }
            }

            // --- IN-APP BODY SECTIONS ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // --- UNIVERSITY SPECIFIC ONBOARDING MODULES ---
                SectionHeader("Universities of Gangwon Province", "Onboarding guides & orientation timelines")

                // Clickable Row of Universities logo blocks
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(Catalog.universitiesList) { univ ->
                        val isSelected = univ.id == currentUniv.id
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.surface
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray.copy(alpha = 0.5f),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { viewModel.selectUniversity(univ.id) }
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            Text(
                                univ.EnglishName.replace(" University", " U."),
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Highlighted University Details Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.LightGray.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.School, "Academic", tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                currentUniv.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Small checklist
                        Text("📍 Dormitory Destination:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text(currentUniv.dormitoryAddress, fontSize = 12.sp, color = Color.Gray)

                        Spacer(modifier = Modifier.height(6.dp))

                        Text("📅 Orientation Schedule:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text(currentUniv.orientationDates, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)

                        Spacer(modifier = Modifier.height(6.dp))

                        Text("📞 Emergency Helpline Representative:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text(currentUniv.emergencyContact, fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)

                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))
                                .padding(10.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Lightbulb, "pointer", tint = Color(0xFFFFB300), modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Pre-arrival Pointers", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                                }
                                currentUniv.usefulPointers.forEach { pt ->
                                    Text("• $pt", fontSize = 11.sp, color = Color.DarkGray, modifier = Modifier.padding(top = 2.dp))
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- PROMOTIONS & DISCOUNTS BANNERS ---
                SectionHeader("Promotions & Referral Discount Benefits")

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(Color(0xFF2563EB), Color(0xFF1E3A8A))
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "REFER & SAVE",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "Get 10,000 KRW",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = (-0.5).sp
                                )
                                Text(
                                    text = "Invite friends to Pack Smart.",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }

                            Button(
                                onClick = { onNavigateToTab("profile") },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = Color(0xFF2563EB)
                                ),
                                shape = RoundedCornerShape(16.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "INVITE",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 11.sp,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // --- RECOMMENDED KITS ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Recommended Onboarding Kits",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = { onNavigateToTab("kits") }) {
                        Text("View all", fontSize = 12.sp)
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Catalog.productsList.take(2).forEach { prod ->
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onNavigateToTab("kits") }
                                .border(1.dp, Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    prod.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    prod.subCategory,
                                    fontSize = 10.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )

                                Divider()

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "${'$'}${prod.price}",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(26.dp)
                                            .background(MaterialTheme.colorScheme.primary, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Add, "add", tint = Color.White, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                // Recent Order History
                SectionHeader("Logistics Central Operations Orders")
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 80.dp)
                        .border(1.dp, Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.LocalShipping, "shipping", tint = MaterialTheme.colorScheme.primary)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Order #UPK-208945", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                HighlightBadge("CENTRAL DISPATCH", Color(0xFF1E88E5))
                            }
                            Text("Arrived kit pre-staged in Chuncheon Campus Office.", fontSize = 11.sp, color = Color.Gray)
                            Text("Consignment: Transit Standard Bedding Package", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        // --- FLOATING AI SHORTCUT "UNI" ---
        FloatingActionButton(
            onClick = { onNavigateToTab("community") }, // Direct trigger to AI chatbot community section
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 16.dp)
                .testTag("floating_uni_fab"),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.SmartToy, "Uni")
                Spacer(modifier = Modifier.width(6.dp))
                Text("Ask Uni", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ShortcutButton(title: String, emoji: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .shadow(1.dp, CircleShape)
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(emoji, fontSize = 24.sp)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(title, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
    }
}

@Composable
fun CategoryGridCard(
    title: String,
    desc: String,
    emoji: String,
    bgColor: Color,
    borderColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .border(1.dp, borderColor, RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .shadow(1.dp, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .shadow(1.dp, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(emoji, fontSize = 18.sp)
            }
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = textColor
                )
                Text(
                    text = desc,
                    fontSize = 10.sp,
                    color = textColor.copy(alpha = 0.7f)
                )
            }
        }
    }
}
