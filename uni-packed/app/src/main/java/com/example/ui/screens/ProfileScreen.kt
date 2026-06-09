package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.MainViewModel

@Composable
fun ProfileScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentProfile by viewModel.userProfile.collectAsState()
    val currentUniv by viewModel.currentUnivInfo.collectAsState()

    var activeProfileSubTab by remember { mutableStateOf(0) } // 0 = Profile Setup, 1 = KYC Files, 2 = Sessions / 2FA, 3 = Care & Feedback

    // Text inputs synced locally with profile update form
    var editName by remember { mutableStateOf(currentProfile.name) }
    var editAddress by remember { mutableStateOf(currentProfile.address) }
    var editNational by remember { mutableStateOf(currentProfile.nationality) }
    var editDate by remember { mutableStateOf(currentProfile.arrivalDate) }

    LaunchedEffect(currentProfile) {
        editName = currentProfile.name
        editAddress = currentProfile.address
        editNational = currentProfile.nationality
        editDate = currentProfile.arrivalDate
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // --- TOP USER CARD BANNER ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .statusBarsPadding()
                .padding(vertical = 16.dp, horizontal = 20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(MaterialTheme.colorScheme.secondary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        currentProfile.name.take(1).uppercase().ifBlank { "S" },
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        currentProfile.name.ifBlank { "International Student" },
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                    Text(
                        "${currentProfile.email} • ${currentProfile.nationality}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Text(
                        "Campus Destination: ${currentProfile.university}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // --- SUB OPTIONS SWITCH DIRECTORY ---
        TabRow(selectedTabIndex = activeProfileSubTab) {
            Tab(selected = activeProfileSubTab == 0, onClick = { activeProfileSubTab = 0 }) {
                Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ManageAccounts, null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(2.dp))
                    Text("Profile", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
            Tab(selected = activeProfileSubTab == 1, onClick = { activeProfileSubTab = 1 }) {
                Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Fingerprint, null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(2.dp))
                    Text("KYC", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
            Tab(selected = activeProfileSubTab == 2, onClick = { activeProfileSubTab = 2 }) {
                Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Security, null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(2.dp))
                    Text("Sessions/Logs", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
            Tab(selected = activeProfileSubTab == 3, onClick = { activeProfileSubTab = 3 }) {
                Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Feedback, null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(2.dp))
                    Text("Support", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // --- DYNAMIC SWITCH VIEWS ---
        Box(modifier = Modifier.weight(1f)) {
            when (activeProfileSubTab) {
                0 -> {
                    // --- PROFILE INFO SETUP ---
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        SectionHeader("Personal Student Credentials")

                        OutlinedTextField(
                            value = editName,
                            onValueChange = { editName = it },
                            label = { Text("Student Full Name") },
                            modifier = Modifier.fillMaxWidth().testTag("setup_name_field")
                        )

                        OutlinedTextField(
                            value = editNational,
                            onValueChange = { editNational = it },
                            label = { Text("Country of Origin / Nationality") },
                            modifier = Modifier.fillMaxWidth().testTag("setup_nationality_field")
                        )

                        OutlinedTextField(
                            value = editDate,
                            onValueChange = { editDate = it },
                            label = { Text("Arrival Date (e.g. 2026-08-25)") },
                            modifier = Modifier.fillMaxWidth().testTag("setup_arrival_date_field")
                        )

                        OutlinedTextField(
                            value = editAddress,
                            onValueChange = { editAddress = it },
                            label = { Text("Assigned Korean Housing Address") },
                            placeholder = { Text("1 Gangwondaehak-gil, Chuncheon-si, Gangwon-do") },
                            modifier = Modifier.fillMaxWidth().testTag("setup_address_field")
                        )

                        // Language Preferences Dropdown Selector Mock
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color.LightGray.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                                .clickable {
                                    val nextLang = when (currentProfile.languagePreference) {
                                        "English" -> "Hindi"
                                        "Hindi" -> "Nepali"
                                        else -> "English"
                                    }
                                    viewModel.updateProfile(
                                        name = currentProfile.name,
                                        university = currentProfile.university,
                                        nationality = currentProfile.nationality,
                                        languagePreference = nextLang,
                                        address = currentProfile.address,
                                        arrivalDate = currentProfile.arrivalDate
                                    )
                                    Toast.makeText(context, "Language preference changed to $nextLang", Toast.LENGTH_SHORT).show()
                                }
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Primary Language:", fontSize = 11.sp, color = Color.Gray)
                                    Text(currentProfile.languagePreference, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                                Icon(Icons.Default.Language, "lang", tint = MaterialTheme.colorScheme.primary)
                            }
                        }

                        // SAVE CHANGES BUTTON
                        Button(
                            onClick = {
                                viewModel.updateProfile(
                                    name = editName,
                                    university = currentProfile.university,
                                    nationality = editNational,
                                    languagePreference = currentProfile.languagePreference,
                                    address = editAddress,
                                    arrivalDate = editDate
                                )
                                Toast.makeText(context, "Profile database updated successfully!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.fillMaxWidth().testTag("save_profile_button"),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Check, null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Save Profile Changes")
                        }

                        // --- REFERRAL DISCOUNTS ENGINE PANEL ---
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.08f)),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Celebration, "rewards", tint = MaterialTheme.colorScheme.secondary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "Your Personal Referral Card",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("Referral Code:", fontSize = 11.sp, color = Color.Gray)
                                        Text(currentProfile.referralCode, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                                    }

                                    Button(
                                        onClick = {
                                            viewModel.applyCoupon("UNIPACK_STUDENT_BONUS") // triggers self simulated referral applying
                                            Toast.makeText(context, "Referral Code Copied & Shared!", Toast.LENGTH_SHORT).show()
                                        },
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text("Share / Simulate")
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Successful Referrals Registered:", fontSize = 12.sp, color = Color.DarkGray)
                                    Text("${currentProfile.referralCount} Friend(s)", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                                }
                                Text(
                                    "Unlocked Discount: ${currentProfile.unlockedDiscountPercent}% off! Coupon is linked instantly to your checkout cart.",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2E7D32)
                                )
                            }
                        }

                        // --- TRACK PERSISTED SHIPMENTS ENGINE DESK ---
                        val placedOrders by viewModel.placedOrders.collectAsState()
                        if (placedOrders.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            SectionHeader("Pre-staged Shipments & Logistics", "Real-time room-delivery staging status")
                            placedOrders.forEach { order ->
                                Card(
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
                                    border = BorderStroke(1.dp, Color(0xFF81C784).copy(alpha = 0.5f))
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.LocalShipping, "Order Status", tint = Color(0xFF2E7D32))
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(order.orderReferenceCode, fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))
                                            }
                                            HighlightBadge(order.status, Color(0xFF2E7D32))
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("Destination: ${order.university}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                        Text("Package Items: ${order.itemNamesSummary}", fontSize = 11.sp, color = Color.DarkGray)
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text("Total paid: ₩%,.0f".format(order.totalKrwPrice), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            Text("Settle Date: ${order.timestamp}", fontSize = 11.sp, color = Color.Gray)
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }

                1 -> {
                    // --- KYC PHOTO VERIFICATIONS FOR EXPAT SAFETY ---
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        SectionHeader("Official KYC Credentials Desk", "Korean security compliance checklist")

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFFF9C4))
                                .border(1.dp, Color(0xFFFFEB3B), RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Row {
                                Icon(Icons.Default.Warning, "alert", tint = Color(0xFFE65100))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "🚨 Verification Required! Securely upload passport scans and Alien Registration Cards " +
                                            "to authorize delivery of student SIM lines, bank account setup codes, and coordinate private taxi arrivals.",
                                    fontSize = 11.sp,
                                    color = Color(0xFFE65100)
                                )
                            }
                        }

                        // Determine status checklist indicators dynamically based on properties:
                        val passportStateStr = if (currentProfile.passportImageUri == null) "NOT_SUBMITTED" else currentProfile.kycStatus
                        val arcStateStr = if (currentProfile.arcImageUri == null) "NOT_SUBMITTED" else currentProfile.kycStatus
                        val studentIdStateStr = if (currentProfile.studentIdImageUri == null) "NOT_SUBMITTED" else currentProfile.kycStatus
                        val visaStateStr = if (currentProfile.visaImageUri == null) "NOT_SUBMITTED" else currentProfile.kycStatus

                        KycDocumentRow(
                            title = "Valid Passport Photos",
                            status = passportStateStr,
                            expiryStr = "Not Expired",
                            onUpload = {
                                viewModel.simulateDocUpload("Passport", "passport_scan_uri")
                                Toast.makeText(context, "Passport scan upload successful!", Toast.LENGTH_SHORT).show()
                            }
                        )

                        KycDocumentRow(
                            title = "Korean Alien Registration Card (ARC)",
                            status = arcStateStr,
                            expiryStr = "Alert: Expiry in 45 Days!",
                            onUpload = {
                                viewModel.simulateDocUpload("ARC", "arc_card_uri")
                                Toast.makeText(context, "ARC photo upload successful!", Toast.LENGTH_SHORT).show()
                            },
                            showWarning = true
                        )

                        KycDocumentRow(
                            title = "University Student ID Card",
                            status = studentIdStateStr,
                            expiryStr = "Active Enrollment",
                            onUpload = {
                                viewModel.simulateDocUpload("StudentID", "student_id_uri")
                                Toast.makeText(context, "Student ID mock upload success!", Toast.LENGTH_SHORT).show()
                            }
                        )

                        KycDocumentRow(
                            title = "Korean D-2 Entry Visa Paperwork",
                            status = visaStateStr,
                            expiryStr = "Valid D2-Entry clearance",
                            onUpload = {
                                viewModel.simulateDocUpload("Visa", "visa_letter_uri")
                                Toast.makeText(context, "Visa PDF mock upload success!", Toast.LENGTH_SHORT).show()
                            }
                        )

                        if (currentProfile.kycStatus == "PENDING") {
                            Button(
                                onClick = {
                                    viewModel.simulateAdminKycApprove()
                                    Toast.makeText(context, "Korean compliance system verified files successfully!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                            ) {
                                Text("Simulate Admin Compliance Approval")
                            }
                        }

                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }

                2 -> {
                    // --- MULTI-DEVICE SESSION LISTINGS & ACCOUNT CONTROLS ---
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        SectionHeader("Enhanced Session & Device Desk", "Live device security alerts")

                        // Active 2FA configuration toggle
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Double 2-Factor Authentication (2FA)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(
                                        if (currentProfile.hasTwoFactorEnabled) "Active OTP token check: Active" else "Inactive (Passwords only)",
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                }

                                Switch(
                                    checked = currentProfile.hasTwoFactorEnabled,
                                    onCheckedChange = {
                                        viewModel.toggleTwoFactor(it)
                                        Toast.makeText(context, "2-Factor authenticate security changed!", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.testTag("2fa_switch_toggle")
                                )
                            }
                        }

                        // Auto logout configurations
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Session Timeout (Inactivity)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text("Session cleanses automatically after 15 minutes of inactivity", fontSize = 11.sp, color = Color.Gray)
                                }

                                Button(
                                    onClick = {
                                        Toast.makeText(context, "Session auto-logout is active (15 Min default)", Toast.LENGTH_SHORT).show()
                                    },
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text("Configure", fontSize = 11.sp)
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Authenticated Sessions (${viewModel.loginHistory.size})",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            // LOGOUT ALL DEVICE TRIGGERS
                            TextButton(
                                onClick = {
                                    viewModel.loginHistory.clear()
                                    viewModel.loginHistory.add(
                                        com.example.ui.viewmodel.MapEntry("Jun 08, 2026 - 09:24", "Incheon Terminal 1 App Launch (Current Mobile)")
                                    )
                                    Toast.makeText(context, "Cleaned up other peripheral device sessions!", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                            ) {
                                Text("Sign Out Other Devices", fontSize = 11.sp)
                            }
                        }

                        // Sessions list loop
                        viewModel.loginHistory.forEach { session ->
                            val isCurrent = session.key.contains("09:24")
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.25f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        if (session.value.contains("Emulator") || session.value.contains("Mac")) Icons.Default.LaptopMac else Icons.Default.Smartphone,
                                        "device",
                                        tint = MaterialTheme.colorScheme.primary
                                    )

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(session.value, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                            if (isCurrent) {
                                                HighlightBadge("CURRENT DEVICE", Color(0xFF2E7D32))
                                            }
                                        }
                                        Text("Logon Time: ${session.key}", fontSize = 11.sp, color = Color.Gray)
                                        Text("Location Checked: Chuncheon, Gangwon-do", fontSize = 11.sp, color = Color.Gray)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // MAIN LOGOUT BUTTON FOR SINGLE ACCOUNT
                        Button(
                            onClick = {
                                viewModel.logout()
                                Toast.makeText(context, "Settle terminal session out. Safe travels!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.fillMaxWidth().testTag("profile_bottom_logout_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Logout, null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Secure Terminal Session Out")
                        }

                        Spacer(modifier = Modifier.height(60.dp))
                    }
                }
                3 -> {
                    // --- CUSTOMER CARE & FEEDBACK SUPPORT CENTER ---
                    var feedbackText by remember { mutableStateOf("") }
                    var feedbackCategory by remember { mutableStateOf("General Support") }
                    var ratingValue by remember { mutableStateOf(5f) }
                    var supportInquirySubmitted by remember { mutableStateOf(false) }
                    var ticketId by remember { mutableStateOf("") }
                    var isCategoryExpanded by remember { mutableStateOf(false) }
                    
                    val categories = listOf("General Support", "Arrival Kit Issue", "Food Market Suggestion", "Service Onboarding Complaint", "App Bug / Slow")

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        SectionHeader(
                            title = "Care & Support Desk",
                            subtitle = "Direct contact with Pack Smart agents"
                        )

                        // Care Support Card with Email
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = "Support Email",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            "Official Customer Support Email",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            "unipackedcustomercare@gmail.com",
                                            fontWeight = FontWeight.Black,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontSize = 15.sp,
                                            modifier = Modifier.testTag("support_email_text")
                                        )
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Send your onboarding complaints, address queries, or payment receipts directly to our registered care email.",
                                    fontSize = 12.sp,
                                    color = Color.DarkGray
                                )
                                
                                Spacer(modifier = Modifier.height(14.dp))
                                
                                val emailContext = LocalContext.current
                                Button(
                                    onClick = {
                                        try {
                                            val intent = android.content.Intent(android.content.Intent.ACTION_SENDTO).apply {
                                                data = android.net.Uri.parse("mailto:")
                                                putExtra(android.content.Intent.EXTRA_EMAIL, arrayOf("unipackedcustomercare@gmail.com"))
                                                putExtra(android.content.Intent.EXTRA_SUBJECT, "Pack Smart Customer Care Inquiry")
                                                putExtra(android.content.Intent.EXTRA_TEXT, "Hello Customer Support,\n\n[Explain your inquiry/issue here]\n\nRegards,\n${currentProfile.name}\n${currentProfile.email}")
                                            }
                                            emailContext.startActivity(intent)
                                        } catch (e: Exception) {
                                            Toast.makeText(emailContext, "No email client application found on this terminal device.", Toast.LENGTH_LONG).show()
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth().testTag("launch_email_app_btn"),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(Icons.Default.MailOutline, "Compose support mail")
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Compose Support Email Now")
                                }
                            }
                        }

                        // Feedback Submission Card
                        SectionHeader("Give Instant App Feedback", "Help us polish your settling experience")

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                if (supportInquirySubmitted) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFFE8F5E9))
                                            .border(1.dp, Color(0xFF81C784), RoundedCornerShape(8.dp))
                                            .padding(14.dp)
                                    ) {
                                        Column {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.CheckCircle, "success", tint = Color(0xFF2E7D32))
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    "Feedback Logged Successfully!",
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF1B5E20),
                                                    fontSize = 14.sp
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                "Thank you for logging your feedback! Ticket ID $ticketId has been processed and routed to unipackedcustomercare@gmail.com. We will follow up via your profile email within 24 hours.",
                                                color = Color(0xFF2E7D32),
                                                fontSize = 12.sp
                                            )
                                            Spacer(modifier = Modifier.height(12.dp))
                                            OutlinedButton(
                                                onClick = {
                                                    supportInquirySubmitted = false
                                                    feedbackText = ""
                                                },
                                                shape = RoundedCornerShape(8.dp)
                                            ) {
                                                Text("Submit Another Response", fontSize = 11.sp)
                                            }
                                        }
                                    }
                                } else {
                                    // Feedback Category Dropdown Selector
                                    Text("Category of Feedback", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Gray)
                                    
                                    Box {
                                        OutlinedButton(
                                            onClick = { isCategoryExpanded = !isCategoryExpanded },
                                            modifier = Modifier.fillMaxWidth().testTag("feedback_category_dropdown"),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(feedbackCategory, color = MaterialTheme.colorScheme.onSurface)
                                                Icon(Icons.Default.ArrowDropDown, "dropdown", tint = MaterialTheme.colorScheme.primary)
                                            }
                                        }
                                        
                                        DropdownMenu(
                                            expanded = isCategoryExpanded,
                                            onDismissRequest = { isCategoryExpanded = false }
                                        ) {
                                            categories.forEach { cat ->
                                                DropdownMenuItem(
                                                    text = { Text(cat) },
                                                    onClick = {
                                                        feedbackCategory = cat
                                                        isCategoryExpanded = false
                                                    }
                                                )
                                            }
                                        }
                                    }

                                    // Rating slider
                                    Text("Rate Pack Smart Experience: ${ratingValue.toInt()} / 5 Stars", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Gray)
                                    Slider(
                                        value = ratingValue,
                                        onValueChange = { ratingValue = it },
                                        valueRange = 1f..5f,
                                        steps = 3,
                                        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp).testTag("feedback_rating_slider")
                                    )

                                    // Feedback text
                                    OutlinedTextField(
                                        value = feedbackText,
                                        onValueChange = { feedbackText = it },
                                        label = { Text("Describe your feedback/issue in detail") },
                                        placeholder = { Text("e.g. My student SIM card package was awesome, or write standard queries regarding Incheon taxi shuttle transport.") },
                                        modifier = Modifier.fillMaxWidth().height(120.dp).testTag("feedback_text_input"),
                                        maxLines = 5
                                    )

                                    Button(
                                        onClick = {
                                            if (feedbackText.trim().length >= 5) {
                                                val generatedTicket = "TKT-" + (100000..999999).random()
                                                ticketId = generatedTicket
                                                supportInquirySubmitted = true
                                                
                                                // Seed into the reviewsList as a suggestion review to show it dynamically!
                                                viewModel.submitUserReview(
                                                    productId = "standard_kit",
                                                    rating = ratingValue.toInt(),
                                                    reviewText = "[$feedbackCategory] $feedbackText",
                                                    typeSelected = "Suggestion"
                                                )
                                                
                                                Toast.makeText(context, "Feedback registered! Support ticket $generatedTicket created.", Toast.LENGTH_LONG).show()
                                            } else {
                                                Toast.makeText(context, "Please enter at least 5 characters for feedback detail.", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth().testTag("submit_feedback_btn"),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Icon(Icons.Default.Send, "send feedback")
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Send App Feedback")
                                    }
                                }
                            }
                        }

                        // Help Line Card Details
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.25f))
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("Emergency & Campus Safety Lines", fontWeight = FontWeight.Black, fontSize = 13.sp)
                                Divider()
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("General Emergency Dispatcher", fontSize = 12.sp, color = Color.Gray)
                                    Text("Dial 119", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Korean Police Station Help", fontSize = 12.sp, color = Color.Gray)
                                    Text("Dial 112", fontWeight = FontWeight.Bold)
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Korea Medical Referral / Translation", fontSize = 12.sp, color = Color.Gray)
                                    Text("Dial 1339", fontWeight = FontWeight.Bold)
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Current Selected University Office", fontSize = 12.sp, color = Color.Gray)
                                    Text(currentUniv.emergencyContact, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 11.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(60.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun KycDocumentRow(
    title: String,
    status: String,
    expiryStr: String,
    onUpload: () -> Unit,
    showWarning: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray.copy(alpha = 0.25f), RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp)

                val (badgeTxt, badgeColor) = when (status) {
                    "VERIFIED" -> "VERIFIED" to Color(0xFF2E7D32)
                    "PENDING" -> "UNDER REVIEW" to Color(0xFF1E88E5)
                    "REJECTED" -> "FAILED" to Color(0xFFE53935)
                    else -> "MOCK NOT SUBMITTED" to Color(0xFF757575)
                }

                HighlightBadge(badgeTxt, badgeColor)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text("Compliance Info: $expiryStr", fontSize = 11.sp, color = if (showWarning) Color(0xFFE65100) else Color.Gray, fontWeight = if (showWarning) FontWeight.Bold else FontWeight.Normal)

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onUpload,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Icon(Icons.Default.CloudUpload, "upload", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Upload PhotoScan", fontSize = 10.sp)
                }

                if (status == "VERIFIED" || status == "PENDING") {
                    Button(
                        onClick = onUpload, // allows re-uploading
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text("Re-upload Scan", fontSize = 10.sp)
                    }
                }
            }
        }
    }
}
