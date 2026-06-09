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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.CartItemEntity
import com.example.ui.viewmodel.MainViewModel

@Composable
fun CartScreen(
    viewModel: MainViewModel,
    onNavigateToTab: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val cartList by viewModel.cartItems.collectAsState()

    val subtotal by viewModel.rawSubtotalPrice.collectAsState()
    val totalWeight by viewModel.cartTotalWeightKg.collectAsState()
    val deliveryFee by viewModel.deliveryFee.collectAsState()
    val discountPercent by viewModel.discountPercentUnlocked.collectAsState()
    val promoDiscount by viewModel.promoDiscountAmount.collectAsState()
    val finalPrice by viewModel.finalTotalPrice.collectAsState()
    val currentProfile by viewModel.userProfile.collectAsState()

    var couponInputText by remember { mutableStateOf("") }
    var showCheckoutSuccessDialog by remember { mutableStateOf(false) }
    var orderRefCode by remember { mutableStateOf("UPK-208945") }

    // Split cartList between active items and save-for-later items
    val activeCartItems = remember(cartList) { cartList.filter { !it.isSavedForLater } }
    val savedForLaterItems = remember(cartList) { cartList.filter { it.isSavedForLater } }

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
                    "Student Checkout Cart",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Pre-staging lockers • Incheon logistical clearing",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }

        if (cartList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("🛒", fontSize = 64.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Your Cart is Empty",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Select Starter Bedding kits, custom food boxes, or airport transportation services to pre-stage your South Korea departure.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { onNavigateToTab("kits") },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Browse Onboarding Kits")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                // Active Cart Header
                item {
                    SectionHeader(
                        title = "Assembled Settle Items (${activeCartItems.size})",
                        subtitle = "Ready for logistical deployment"
                    )
                }

                if (activeCartItems.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f))
                        ) {
                            Text(
                                "No active items in cart. Move items back from 'Saved For Later' list or check kits.",
                                modifier = Modifier.padding(16.dp),
                                textAlign = TextAlign.Center,
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                } else {
                    items(activeCartItems) { item ->
                        CartItemRow(
                            item = item,
                            onQuantityChanged = { delta -> viewModel.changeCartQuantity(item, delta) },
                            onToggleSaved = { viewModel.saveForLater(item, true) },
                            onRemove = { viewModel.removeFromCart(item) }
                        )
                    }
                }

                // Promo Coupon & Referrer code support Section
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "🔒 Unlocked Promo & Referral Discount Check",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .border(1.dp, Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                "Enter a welcome coupon or friend's referral code to instantly deduct 10%-15% off your entire kit invoice.",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = couponInputText,
                                    onValueChange = { couponInputText = it },
                                    label = { Text("Coupon Code (e.g. UNIPACK_WELCOME)") },
                                    modifier = Modifier
                                        .weight(1.3f)
                                        .testTag("promo_code_input"),
                                    textStyle = TextStyle(fontSize = 12.sp)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Button(
                                    onClick = {
                                        if (couponInputText.isNotBlank()) {
                                            val feedbackStr = viewModel.applyCoupon(couponInputText)
                                            Toast.makeText(context, feedbackStr, Toast.LENGTH_LONG).show()
                                        }
                                    },
                                    modifier = Modifier.testTag("apply_promo_btn"),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Apply", fontSize = 12.sp)
                                }
                            }

                            if (discountPercent > 0) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFFE8F5E9))
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        "🎯 Success! Discount of $discountPercent% applied dynamically on checkout totals.",
                                        color = Color(0xFF2E7D32),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Pricing Summary calculations Card
                item {
                    SectionHeader("Deployments Estimate Breakdown", "Pricing tracked in KRW Won")

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            PricingRow(label = "Assembled Basket Weight", value = "${String.format("%.2f", totalWeight)} kg")
                            PricingRow(label = "Raw Subtotal", value = "${String.format("%,d", subtotal.toInt())} ₩")

                            if (discountPercent > 0) {
                                PricingRow(
                                    label = "Unlocked Promo Discount (-$discountPercent%)",
                                    value = "-${String.format("%,d", promoDiscount.toInt())} ₩",
                                    color = Color(0xFF2E7D32)
                                )
                            }

                            PricingRow(
                                label = "Estimated Central Delivery Fee",
                                value = if (deliveryFee == 0.0) "FREE (on orders > 100,000 ₩)" else "${String.format("%,d", deliveryFee.toInt())} ₩",
                                color = if (deliveryFee == 0.0) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurface
                            )

                            Divider(modifier = Modifier.padding(vertical = 10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("DEPARTURE ESTIMATE TOTAL", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        "${String.format("%,d", finalPrice.toInt())} ₩",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 20.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = {
                                    val randomRef = "UPK-${(100000..999999).random()}"
                                    orderRefCode = randomRef
                                    viewModel.checkoutAndRecordOrder(
                                        referenceCode = randomRef,
                                        university = currentProfile.university,
                                        totalKrwPrice = finalPrice
                                    )
                                    showCheckoutSuccessDialog = true
                                },
                                modifier = Modifier.fillMaxWidth().testTag("checkout_deploy_btn"),
                                shape = RoundedCornerShape(10.dp),
                                enabled = activeCartItems.isNotEmpty()
                            ) {
                                Icon(Icons.Default.FlightTakeoff, null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Pre-stage Logistics & Deploy Checkout")
                            }
                        }
                    }
                }

                // SAVED FOR LATER SECTIONS
                if (savedForLaterItems.isNotEmpty()) {
                    item {
                        SectionHeader(
                            title = "Saved For Later Shelf (${savedForLaterItems.size})",
                            subtitle = "Items saved for secondary ordering cycles"
                        )
                    }

                    items(savedForLaterItems) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .border(1.dp, Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1.3f)) {
                                    Text(item.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.Gray)
                                    Text("${String.format("%,d", item.price.toInt())} ₩", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    TextButton(onClick = { viewModel.saveForLater(item, false) }) {
                                        Text("Move To Cart", fontSize = 11.sp)
                                    }
                                    IconButton(onClick = { viewModel.removeFromCart(item) }) {
                                        Icon(Icons.Default.Delete, "Delete", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // --- CHECKOUT SUCCESSFUL DIALOG OVERLAY ---
    if (showCheckoutSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showCheckoutSuccessDialog = false
            },
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text("✈ Package Pre-staged!", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("📦 Delivery Location: ${currentProfile.university} Housing", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Your bedding sets, adapters, and home country foods have been successfully allocated and transferred to our staging lockers. " +
                                "Relax! On the morning of your arrival, our couriers will stage this box directly inside your assigned dormitory room.",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFE3F2FD))
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Order Reference Code: $orderRefCode",
                            color = Color(0xFF1E88E5),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showCheckoutSuccessDialog = false
                        onNavigateToTab("home") // Navigate to dashboard
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Settle and Track My Shipment")
                }
            }
        )
    }
}

@Composable
fun CartItemRow(
    item: CartItemEntity,
    onQuantityChanged: (Int) -> Unit,
    onToggleSaved: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .border(1.dp, Color.LightGray.copy(alpha = 0.25f), RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category symbol
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    when (item.category) {
                        "Food", "Halal-Food", "Non-Halal-Food" -> "🍲"
                        "Service" -> "📱"
                        else -> "📦"
                    },
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1.3f)) {
                Text(item.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(
                    "${String.format("%,d", item.price.toInt())} ₩ • ${item.weight} kg",
                    fontSize = 11.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row {
                    Text(
                        "Save for Later",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { onToggleSaved() }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Remove",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.clickable { onRemove() }
                    )
                }
            }

            // Quantity increments
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(
                    onClick = { onQuantityChanged(-1) },
                    modifier = Modifier
                        .size(26.dp)
                        .background(Color.LightGray.copy(alpha = 0.3f), CircleShape)
                ) {
                    Icon(Icons.Default.Remove, "less quantity", modifier = Modifier.size(14.dp))
                }

                Text(
                    "${item.quantity}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                IconButton(
                    onClick = { onQuantityChanged(1) },
                    modifier = Modifier
                        .size(26.dp)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(Icons.Default.Add, "more quantity", modifier = Modifier.size(14.dp))
                }
            }
        }
    }
}

@Composable
fun PricingRow(label: String, value: String, color: Color = MaterialTheme.colorScheme.onSurface) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 12.sp, color = Color.Gray)
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color)
    }
}
