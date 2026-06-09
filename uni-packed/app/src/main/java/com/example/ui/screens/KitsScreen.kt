package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Catalog
import com.example.data.model.Product
import com.example.data.local.CustomKitEntity
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun KitsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var activeTab by remember { mutableStateOf(0) } // 0 = Catalog Kits, 1 = Custom Builder Workspace, 2 = My Saved Kits

    val customKits by viewModel.customKits.collectAsState()
    val builderSelection by viewModel.customBuilderProducts.collectAsState()

    var selectedProductDialog by remember { mutableStateOf<Product?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // --- HEADER ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .statusBarsPadding()
                .padding(vertical = 16.dp, horizontal = 20.dp)
        ) {
            Column {
                Text(
                    "Arrival Settle Kits",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Centralized inventory • Direct campus room pre-staging",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }

        // --- SUB TABS MODULE ---
        TabRow(selectedTabIndex = activeTab) {
            Tab(selected = activeTab == 0, onClick = { activeTab = 0 }) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Inventory, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Pre-made Kits", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            Tab(selected = activeTab == 1, onClick = { activeTab = 1 }) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Construction, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Kit Builder", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            Tab(selected = activeTab == 2, onClick = { activeTab = 2 }) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.BookmarkBorder, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Custom Saved (${customKits.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // --- DYNAMIC CONTENT PANEL ---
        Box(modifier = Modifier.weight(1f)) {
            when (activeTab) {
                0 -> {
                    // --- PRE-MADE KITS CATALOG ---
                    val kitsList = remember {
                        Catalog.productsList.filter {
                            it.category in listOf("Starter", "Standard", "Premium", "University", "Country")
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(kitsList) { kit ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedProductDialog = kit }
                                    .border(1.dp, Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column {
                                    // Header Card Color Band
                                    val headerColor = when (kit.category) {
                                        "Starter" -> Brush.horizontalGradient(listOf(Color(0xFF81C784), Color(0xFF4CAF50)))
                                        "Standard" -> Brush.horizontalGradient(listOf(Color(0xFF64B5F6), Color(0xFF1E88E5)))
                                        "Premium" -> Brush.horizontalGradient(listOf(Color(0xFFBA68C8), Color(0xFF8E24AA)))
                                        else -> Brush.horizontalGradient(listOf(Color(0xFFFFB74D), Color(0xFFF57C00)))
                                    }

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp)
                                            .background(headerColor)
                                            .padding(horizontal = 16.dp),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                "${kit.category.uppercase()} SELECTION",
                                                color = Color.White,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                letterSpacing = 1.sp
                                            )
                                            HighlightBadge(
                                                text = "${kit.weightKg} kg",
                                                backgroundColor = Color.White
                                            )
                                        }
                                    }

                                    if (kit.imgUrl.isNotEmpty()) {
                                        Image(
                                            painter = getProductPainter(kit.imgUrl),
                                            contentDescription = kit.name,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(180.dp)
                                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                                .clip(RoundedCornerShape(12.dp)),
                                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                        )
                                    }

                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = kit.name,
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = kit.subCategory,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.secondary,
                                            fontWeight = FontWeight.Bold
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                            text = kit.description,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                            lineHeight = 18.sp
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        // Bundle inclusion chips preview
                                        Text(
                                            "INCLUDED COMPONENT PRESTAGE:",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.Gray,
                                            letterSpacing = 0.5.sp
                                        )
                                        FlowRow(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            kit.bundleProducts.forEach { item ->
                                                Box(
                                                    modifier = Modifier
                                                        .padding(vertical = 2.dp)
                                                        .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                                ) {
                                                    Text(item, fontSize = 10.sp, color = Color.DarkGray)
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))

                                        // Ratings & Price row
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                StarRatingBar(rating = kit.rating)
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text("(${kit.reviewsCount})", fontSize = 11.sp, color = Color.Gray)
                                            }

                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    "${String.format("%,d", kit.krwPrice)} ₩",
                                                    style = MaterialTheme.typography.headlineSmall,
                                                    fontWeight = FontWeight.Black,
                                                    color = MaterialTheme.colorScheme.secondary
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Button(
                                            onClick = {
                                                viewModel.addToCart(kit)
                                                Toast.makeText(context, "${kit.name} added to cart!", Toast.LENGTH_SHORT).show()
                                            },
                                            modifier = Modifier.fillMaxWidth().testTag("add_kit_to_cart_btn"),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Icon(Icons.Default.ShoppingBag, null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("Settle and Prestage this Kit")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                1 -> {
                    // --- CUSTOM KIT BUILDER ---
                    var saveKitNameInput by remember { mutableStateOf("") }
                    val singleProducts = remember {
                        listOf(
                            Product("bp_pillow", "Memory Comfort Pillow", "Ergonomic head and neck support.", 12.00, 15000, "Starter", weightKg = 0.8),
                            Product("bp_duvet", "Microfiber Single Duvet", "Hypoallergenic all-weather blanket.", 28.00, 36000, "Starter", weightKg = 1.8),
                            Product("bp_adapter", "EU/KR Local Plug Adapter", "Ground Type F adapter plug.", 4.50, 6000, "Starter", weightKg = 0.1),
                            Product("bp_slippers", "Dorm Bathroom Slippers", "Non-slip rapid dry comfort grip.", 6.00, 8000, "Starter", weightKg = 0.35),
                            Product("bp_towels", "Absorbent Hand Towels (2-Pack)", "100% organic Korean cotton.", 8.00, 10000, "Starter", weightKg = 0.4),
                            Product("bp_multitap", "Korean Multi-tap Power Strip", "3-plug extension cord with master switch.", 10.00, 13000, "Starter", weightKg = 0.5),
                            Product("bp_tmoney", "Pre-loaded T-Money Card", "Ready commuting card with 10k Won balance.", 12.50, 16000, "Standard", weightKg = 0.05)
                        )
                    }

                    Column(modifier = Modifier.fillMaxSize()) {
                        // Live totals bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFFFF3E0))
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        "LIVE ASSEMBLED TOTALS",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFFE65100)
                                    )
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Text(
                                            "${String.format("%,d", viewModel.getBuilderTotalPrice().toInt())} ₩",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color(0xFFE65100)
                                        )
                                        Text(
                                            " • ${viewModel.getBuilderTotalWeight()} kg",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.DarkGray
                                        )
                                    }
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    OutlinedButton(
                                        onClick = {
                                            val sharedDesc = "I crafted my Korean University Kit on Uni Packed! Total: ${String.format("%,d", viewModel.getBuilderTotalPrice().toInt())} ₩ (${viewModel.getBuilderTotalWeight()}kg). Get 10% off!"
                                            Toast.makeText(context, "Assembled kit configuration shared to clipboard!", Toast.LENGTH_SHORT).show()
                                        },
                                        shape = RoundedCornerShape(6.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp)
                                    ) {
                                        Icon(Icons.Default.Share, null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Share", fontSize = 10.sp)
                                    }
                                }
                            }
                        }

                        // Assembling List
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 16.dp),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            item {
                                Box(modifier = Modifier.padding(vertical = 8.dp)) {
                                    Text(
                                        "Pick and toggle individual items from central inventories to construct a bespoke bundle to pre-stage in your dorm room.",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                            }

                            items(singleProducts) { prod ->
                                val qty = builderSelection[prod.id] ?: 0
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.25f))
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1.3f)) {
                                            Text(prod.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                            Text(
                                                "${prod.description} (${prod.weightKg}kg)",
                                                fontSize = 11.sp,
                                                color = Color.Gray
                                            )
                                            Text(
                                                "${String.format("%,d", prod.krwPrice)} ₩",
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.secondary,
                                                fontSize = 12.sp
                                            )
                                        }

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            if (qty > 0) {
                                                IconButton(
                                                    onClick = { viewModel.subtractItemFromCustomBuilder(prod.id) },
                                                    modifier = Modifier.size(28.dp).background(Color.LightGray.copy(alpha = 0.4f), CircleShape)
                                                ) {
                                                    Icon(Icons.Default.Remove, "less", modifier = Modifier.size(16.dp))
                                                }

                                                Text(
                                                    "$qty",
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 14.sp,
                                                    modifier = Modifier.padding(horizontal = 8.dp)
                                                )
                                            }

                                            IconButton(
                                                onClick = { viewModel.addItemToCustomBuilder(prod.id) },
                                                modifier = Modifier.size(28.dp).background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f), CircleShape)
                                            ) {
                                                Icon(
                                                    Icons.Default.Add,
                                                    "add",
                                                    tint = MaterialTheme.colorScheme.secondary,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Bottom save panel
                        if (builderSelection.isNotEmpty()) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(6.dp),
                                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    OutlinedTextField(
                                        value = saveKitNameInput,
                                        onValueChange = { saveKitNameInput = it },
                                        placeholder = { Text("Name your Custom Bundle (e.g. My KNU Pack)") },
                                        modifier = Modifier.fillMaxWidth().testTag("custom_kit_name_field")
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Button(
                                        onClick = {
                                            viewModel.saveCustomKit(saveKitNameInput)
                                            saveKitNameInput = ""
                                            Toast.makeText(context, "Bespoke kit saved to Local Database!", Toast.LENGTH_SHORT).show()
                                            activeTab = 2 // Redirect to saved kits tab
                                        },
                                        modifier = Modifier.fillMaxWidth().testTag("save_custom_kit_btn"),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                                    ) {
                                        Icon(Icons.Default.Save, null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Save Custom Kit Configuration")
                                    }
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // --- MY SAVED KITS ---
                    if (customKits.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text("📦", fontSize = 56.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "No Custom Kits Saved",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Go to the 'Kit Builder' tab to select individual bedding items, cables, and SIM cards to assemble your personalized pack.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            contentPadding = PaddingValues(vertical = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(customKits) { kit ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                kit.kitName,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.secondary
                                            )
                                            IconButton(onClick = { viewModel.deleteCustomKit(kit) }) {
                                                Icon(Icons.Default.Delete, "delete", tint = MaterialTheme.colorScheme.error)
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Text(
                                            text = kit.itemsJsonContent,
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column {
                                                Text("Pricing: ${String.format("%,d", kit.totalPrice.toInt())} ₩", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                                Text("Estimated Weight: ${kit.totalWeight} kg", fontSize = 11.sp, color = Color.Gray)
                                            }

                                            Button(
                                                onClick = {
                                                    viewModel.addCustomKitToCart(kit)
                                                    Toast.makeText(context, "Added custom bundle to cart!", Toast.LENGTH_SHORT).show()
                                                },
                                                shape = RoundedCornerShape(8.dp)
                                            ) {
                                                Text("Add to Cart")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // --- PRODUCT DETAIL POPUP OVERLAY ---
    selectedProductDialog?.let { kit ->
        AlertDialog(
            onDismissRequest = { selectedProductDialog = null },
            title = {
                Text(
                    kit.name,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp
                )
            },
            text = {
                val reviews = remember {
                    viewModel.reviewsList.filter { it.productId == kit.id }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 380.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        kit.description,
                        fontSize = 13.sp,
                        color = Color.DarkGray
                    )

                    if (kit.imgUrl.isNotEmpty()) {
                        Image(
                            painter = getProductPainter(kit.imgUrl),
                            contentDescription = kit.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .padding(vertical = 8.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("📊 Specifications:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("• Weight: ${kit.weightKg} kg", fontSize = 12.sp, color = Color.Gray)
                    Text("• Estimated Delivery: ${kit.estimatedDeliveryDays} day(s) direct delivery", fontSize = 12.sp, color = Color.Gray)
                    Text("• Material Composition: ${kit.ingredients}", fontSize = 12.sp, color = Color.Gray)

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("📦 Complete Product Box List:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    kit.bundleProducts.forEach { item ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                            Icon(Icons.Default.Done, "check", tint = Color(0xFF2E7D32), modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(item, fontSize = 12.sp, color = Color.DarkGray)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("💬 Frequently Asked Questions:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("Q: Can I check-in at midnight and have my package active?", fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
                    Text("A: Yes, our partnerships with Gangwon dorm managers mean packages are pre-staged in your room before standard lockouts.", fontSize = 11.sp, color = Color.Gray)

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("⭐ Verified Buyer Reviews (${reviews.size}):", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    if (reviews.isEmpty()) {
                        Text("No reviews yet. Be the first to verify!", fontSize = 11.sp, color = Color.Gray)
                    } else {
                        reviews.forEach { r ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .background(Color.LightGray.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(r.userName, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    HighlightBadge("VERIFIED BUYER", Color(0xFF2E7D32))
                                }
                                StarRatingBar(rating = r.rating.toFloat())
                                Text(r.text, fontSize = 11.sp, color = Color.DarkGray)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.addToCart(kit)
                        selectedProductDialog = null
                        Toast.makeText(context, "${kit.name} added!", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text("Add Settle Kit (${String.format("%,d", kit.krwPrice)} ₩)")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedProductDialog = null }) {
                    Text("Dismiss")
                }
            }
        )
    }
}

// In Compose, FlowRow is experimental, so a simple helper for chip layouts:
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    content: @Composable () -> Unit
) {
    // Basic wrapper to safely structure chips
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                content()
            }
        }
    }
}
