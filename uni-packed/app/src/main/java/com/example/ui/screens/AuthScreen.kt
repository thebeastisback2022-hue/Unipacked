package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.AuthState
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AuthScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val authState by viewModel.authState.collectAsState()
    val userEmail by viewModel.currentUserEmail.collectAsState()

    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var otpInput by remember { mutableStateOf("") }
    var authModeIsSignUp by remember { mutableStateOf(false) } // true = Sign Up, false = Login
    var showForgotPassword by remember { mutableStateOf(false) }

    var forgotPasswordEmail by remember { mutableStateOf("") }
    var showResetSuccess by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AppLogoBranding(showTagline = true)

            Spacer(modifier = Modifier.height(24.dp))

            AnimatedContent(
                targetState = authState,
                transitionSpec = { fadeIn() with fadeOut() }
            ) { state ->
                when (state) {
                    is AuthState.LoggedOut -> {
                        if (showForgotPassword) {
                            // --- FORGOT PASSWORD PANEL ---
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), RoundedCornerShape(16.dp)),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(onClick = { showForgotPassword = false }) {
                                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                                        }
                                        Text(
                                            "Reset Password",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        "Enter your registered student email address. We will transmit an automatic recovery security link.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    OutlinedTextField(
                                        value = forgotPasswordEmail,
                                        onValueChange = { forgotPasswordEmail = it },
                                        label = { Text("Student Email (e.g., student@kangwon.ac.kr)") },
                                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                                        modifier = Modifier.fillMaxWidth().testTag("forgot_email_input")
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Button(
                                        onClick = {
                                            if (forgotPasswordEmail.isNotBlank()) {
                                                showResetSuccess = true
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth().testTag("reset_password_btn"),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Transferred Recovery Link")
                                    }

                                    if (showResetSuccess) {
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color(0xFFE8F5E9))
                                                .border(1.dp, Color(0xFF81C784), RoundedCornerShape(8.dp))
                                                .padding(12.dp)
                                        ) {
                                            Row {
                                                Icon(Icons.Default.CheckCircle, "success", tint = Color(0xFF2E7D32))
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    "Reset voucher transferred successfully to $forgotPasswordEmail! Please inspect your student inbox.",
                                                    color = Color(0xFF1B5E20),
                                                    fontSize = 12.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            // --- REGULAR EMAIL LOGIN / SIGNUP ---
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), RoundedCornerShape(16.dp)),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Text(
                                        text = if (authModeIsSignUp) "Create Student Account" else "International Student Access",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Text(
                                        text = if (authModeIsSignUp) "Sign up to configure kits and arrival checklists" else "Securely access kits, halal marketplace, and AI support",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                        modifier = Modifier.padding(bottom = 16.dp)
                                    )

                                    // Email Address
                                    OutlinedTextField(
                                        value = emailInput,
                                        onValueChange = { emailInput = it },
                                        label = { Text("University / Student Email") },
                                        placeholder = { Text("yourname@univ.ac.kr") },
                                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                                        modifier = Modifier.fillMaxWidth().testTag("email_input"),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // Password
                                    OutlinedTextField(
                                        value = passwordInput,
                                        onValueChange = { passwordInput = it },
                                        label = { Text("Password") },
                                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                                        visualTransformation = PasswordVisualTransformation(),
                                        modifier = Modifier.fillMaxWidth().testTag("password_input"),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                                    )

                                    if (!authModeIsSignUp) {
                                        TextButton(
                                            onClick = { showForgotPassword = true },
                                            modifier = Modifier.align(Alignment.End)
                                        ) {
                                            Text("Forgot Password?", fontSize = 12.sp)
                                        }
                                    } else {
                                        Spacer(modifier = Modifier.height(12.dp))
                                    }

                                    Button(
                                        onClick = {
                                            if (emailInput.isNotBlank()) {
                                                if (authModeIsSignUp) {
                                                    viewModel.signUpWithEmail(emailInput)
                                                } else {
                                                    viewModel.loginWithEmail(emailInput)
                                                }
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth().testTag("login_btn"),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(if (authModeIsSignUp) "Proceed with Sign Up" else "Secure Login")
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    TextButton(
                                        onClick = { authModeIsSignUp = !authModeIsSignUp },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            if (authModeIsSignUp) "Already registered? Standard Sign In"
                                            else "First time here? Register as International Student",
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    is AuthState.OtpVerification -> {
                        // --- OTP DYNAMIC DIALOG PANEL ---
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), RoundedCornerShape(16.dp)),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.LockClock,
                                    contentDescription = "OTP",
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(48.dp)
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    "Enter Verification Code",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    "A 4-digit security code was transferred to ${state.email}. Please verify.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                    textAlign = TextAlign.Center
                                )

                                // Real simulation showing what the code is so the user doesn't struggle to remember
                                Box(
                                    modifier = Modifier
                                        .padding(vertical = 12.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f))
                                        .padding(horizontal = 16.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        "Simulated Verification Code: ${state.otpCode}",
                                        color = MaterialTheme.colorScheme.secondary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }

                                OutlinedTextField(
                                    value = otpInput,
                                    onValueChange = { otpInput = it },
                                    label = { Text("4-Digit OTP Code") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.width(180.dp).testTag("otp_input"),
                                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = { viewModel.logout() },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Cancel")
                                    }
                                    Button(
                                        onClick = {
                                            viewModel.verifyOtp(otpInput)
                                            otpInput = ""
                                        },
                                        modifier = Modifier.weight(1f).testTag("otp_verify_btn")
                                    ) {
                                        Text("Verify")
                                    }
                                }
                            }
                        }
                    }

                    is AuthState.TwoFactorChallenge -> {
                        // --- 2FA SECURITY CHALLENGE ---
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), RoundedCornerShape(16.dp)),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.VerifiedUser,
                                    contentDescription = "2FA Active",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(48.dp)
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    "2-Factor Authentication Required",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    "This profile has enhanced 2FA layer. Injected OTP token key: ${state.code}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )

                                OutlinedTextField(
                                    value = otpInput,
                                    onValueChange = { otpInput = it },
                                    label = { Text("Enter 2FA Key") },
                                    modifier = Modifier.fillMaxWidth().testTag("2fa_input"),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        viewModel.verifyTwoFactor(otpInput)
                                        otpInput = ""
                                    },
                                    modifier = Modifier.fillMaxWidth().testTag("2fa_verify_btn")
                                ) {
                                    Text("Approve Account")
                                }
                            }
                        }
                    }

                    is AuthState.LoggedIn -> {
                        // --- ALREADY LOGGED IN PREVIEW ---
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), RoundedCornerShape(16.dp)),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = "Active",
                                    tint = Color(0xFF2E7D32),
                                    modifier = Modifier.size(56.dp)
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    "You are Authenticated",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    "Logged account: $userEmail",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                    modifier = Modifier.padding(vertical = 6.dp)
                                )

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFE3F2FD))
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        "🔒 Double authentication is integrated to guard your passport & student document uploads. You may toggle 2FA configurations securely in your profile dashboard.",
                                        color = Color(0xFF0D47A1),
                                        fontSize = 11.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                OutlinedButton(
                                    onClick = { viewModel.logout() },
                                    modifier = Modifier.fillMaxWidth().testTag("main_logout_btn"),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                                ) {
                                    Text("Change Profile / Sign Out")
                                }
                            }
                        }
                    }
                    AuthState.SignUpForm -> {
                        // Empty signup branch to assert when exhaustiveness
                    }
                }
            }
        }
    }
}
