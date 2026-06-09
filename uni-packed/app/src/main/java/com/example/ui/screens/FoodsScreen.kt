package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Catalog
import com.example.data.model.Product
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FoodsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isHalalOnly by remember { mutableStateOf(true) } // true = Halal marketplace, false = Non-Halal sections
    var selectedCountryTab by remember { mutableStateOf("All") } // "All", "Nepal", "India", "Pakistan", "Bangladesh", "Southeast Asia"

    val countries = remember { listOf("All", "Nepal", "India", "Pakistan", "Bangladesh", "Southeast Asia") }

    var selectedFoodForDetails by remember { mutableStateOf<Product?>(null) }

    // Writing review state parameters
    var ratingSelected by remember { mutableStateOf(5) }
    var reviewTextInput by remember { mutableStateOf("") }
    var feedbackTypeSelected by remember { mutableStateOf("Product Experience") } // "Product Experience", "Delivery Experience", "Suggestion", "Complaint"
    val maxReviewLength = 200

    val foodsList = remember(isHalalOnly, selectedCountryTab) {
        Catalog.productsList.filter {
            val matchesHalal = if (isHalalOnly) it.category == "Halal-Food" else it.category == "Non-Halal-Food"
            val matchesCountry = if (selectedCountryTab == "All") true else it.subCategory == selectedCountryTab
            matchesHalal && matchesCountry
        }
    }

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
                    "Home Food Marketplace",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Central warehouse • Express delivery to Gangwon campuses",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }

        // --- HALAL VS STANDARD DIVISION SELECTOR ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
                .border(1.dp, Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                .padding(4.dp)
        ) {
            Button(
                onClick = { isHalalOnly = true },
                modifier = Modifier
                    .weight(1f)
                    .testTag("halal_toggle_btn"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isHalalOnly) Color(0xFF2E7D32) else Color.Transparent,
                    contentColor = if (isHalalOnly) Color.White else MaterialTheme.colorScheme.onBackground
                ),
                shape = RoundedCornerShape(8.dp),
                elevation = null
            ) {
                Icon(Icons.Default.Verified, "Halal", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Halal Foods Only", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { isHalalOnly = false },
                modifier = Modifier
                    .weight(1f)
                    .testTag("nonhalal_toggle_btn"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!isHalalOnly) MaterialTheme.colorScheme.secondary else Color.Transparent,
                    contentColor = if (!isHalalOnly) Color.White else MaterialTheme.colorScheme.onBackground
                ),
                shape = RoundedCornerShape(8.dp),
                elevation = null
            ) {
                Icon(Icons.Default.RestaurantMenu, "Standard", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Standard / K-Snacks", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        // --- DIVISION BY COUNTRY TABS ---
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(vertical = 8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(countries) { country ->
                val isSelected = country == selectedCountryTab
                AssistChip(
                    onClick = { selectedCountryTab = country },
                    label = { Text(country, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else Color.Transparent,
                        labelColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray.copy(alpha = 0.5f)
                    )
                )
            }
        }

        Divider()

        // --- FOOD LIST FIELD ---
        if (foodsList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("🍲", fontSize = 56.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Item Currently Out-of-stock", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text(
                    "Our central logistics pre-stages shipments weekly. If you have requests, consult 'Uni' or write a suggestion in our feedback hub.",
                    fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 4.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(foodsList) { rawFood ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedFoodForDetails = rawFood
                                // reset feedback inputs
                                ratingSelected = 5
                                reviewTextInput = ""
                                feedbackTypeSelected = "Product Experience"
                            }
                            .border(1.dp, Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Food indicator symbol
                            if (rawFood.imgUrl.isNotEmpty() && !isCountryFoodProduct(rawFood)) {
                                Image(
                                    painter = getProductPainter(rawFood.imgUrl),
                                    contentDescription = rawFood.name,
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(52.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (isHalalOnly) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        if (isHalalOnly) "🕌" else "🍿",
                                        fontSize = 24.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1.3f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        rawFood.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    if (rawFood.isHalalCertified) {
                                        HighlightBadge("HALAL", Color(0xFF2E7D32))
                                    }
                                }

                                Text(
                                    "Origin: ${rawFood.originCountry} • Exp: ${rawFood.expiryDays} Days",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    StarRatingBar(rating = rawFood.rating)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("(${rawFood.reviewsCount})", fontSize = 11.sp, color = Color.Gray)
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    "${String.format("%,d", rawFood.krwPrice)} ₩",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.secondary
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                IconButton(
                                    onClick = {
                                        viewModel.addToCart(rawFood)
                                        Toast.makeText(context, "${rawFood.name} added!", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                                        .testTag("add_food_btn")
                                ) {
                                    Icon(Icons.Default.Add, "add to cart", tint = Color.White, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // --- FOOD DETAILS & REVIEWS/FEEDBACK SHEET PANEL ---
    selectedFoodForDetails?.let { food ->
        AlertDialog(
            onDismissRequest = { selectedFoodForDetails = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(food.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    if (food.isHalalCertified) {
                        HighlightBadge("HALAL CERTIFIED", Color(0xFF2E7D32))
                    }
                }
            },
            text = {
                val reviews = viewModel.reviewsList.filter { it.productId == food.id }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text("ℹ️ Food Description:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(food.description, fontSize = 12.sp, color = Color.DarkGray)

                    if (food.imgUrl.isNotEmpty() && !isCountryFoodProduct(food)) {
                        Image(
                            painter = getProductPainter(food.imgUrl),
                            contentDescription = food.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .padding(vertical = 8.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("🌾 Ingredients List:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(food.ingredients, fontSize = 12.sp, color = Color.Gray)

                    Spacer(modifier = Modifier.height(6.dp))

                    Text("⚠️ Allergen Cautions:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(
                        if (food.allergies.isEmpty()) "None. Clean recipe." else food.allergies.joinToString(", "),
                        fontSize = 12.sp, color = if (food.allergies.isEmpty()) Color.Gray else Color(0xFFD32F2F)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text("⏳ Expiry & Origin Info:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("Manufactured in ${food.originCountry}. Best store before ${food.expiryDays} days.", fontSize = 12.sp, color = Color.Gray)

                    Spacer(modifier = Modifier.height(6.dp))

                    Text("💪 Nutrition Facts:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(food.nutritionInfo, fontSize = 12.sp, color = Color.Gray)

                    Spacer(modifier = Modifier.height(12.dp))

                    // Reviews listings
                    Text("⭐ Student Feedbacks & Ratings:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    if (reviews.isEmpty()) {
                        Text("No reviews posted yet. Be the first to advise classmates!", fontSize = 11.sp, color = Color.Gray, modifier = Modifier.padding(vertical = 4.dp))
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
                                    HighlightBadge(r.reviewType.uppercase(), MaterialTheme.colorScheme.primary)
                                }
                                StarRatingBar(rating = r.rating.toFloat())
                                Text(r.text, fontSize = 11.sp, color = Color.DarkGray)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(12.dp))

                    // CUSTOM VERIFIED USER FEEDBACK WRITING INTERFACE
                    Text("✍️ Post Your Experiences & Concerns", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                    Text("Your feedback aids automated Central Depot audits.", fontSize = 11.sp, color = Color.Gray)

                    Spacer(modifier = Modifier.height(8.dp))

                    // Star slider selector (1 - 5 stars)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Rating Score:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        StarRatingBar(rating = ratingSelected.toFloat(), onRatingChanged = { ratingSelected = it })
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("$ratingSelected / 5", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // FeedBack Category Selector
                    Text("Concern Type Category:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    FlowRow(
                        modifier = Modifier.padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Product Experience", "Delivery Experience", "Suggestion", "Complaint").forEach { type ->
                            val isSelected = type == feedbackTypeSelected
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.surface
                                    )
                                    .border(1.dp, if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray)
                                    .clickable { feedbackTypeSelected = type }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    type,
                                    fontSize = 10.sp,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Written feedback review text box (Controlled Character limit 200)
                    OutlinedTextField(
                        value = reviewTextInput,
                        onValueChange = {
                            if (it.length <= maxReviewLength) {
                                reviewTextInput = it
                            }
                        },
                        placeholder = { Text("Write about allergen packaging errors, late logistics, or recommendations...") },
                        modifier = Modifier.fillMaxWidth().testTag("write_feedback_box"),
                        textStyle = TextStyle(fontSize = 12.sp),
                        supportingText = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Moderated channel", fontSize = 9.sp, color = Color.Red.copy(alpha = 0.6f))
                                Text("${reviewTextInput.length} / $maxReviewLength Chars", fontSize = 10.sp)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            val msg = viewModel.submitUserReview(
                                food.id,
                                ratingSelected,
                                reviewTextInput,
                                feedbackTypeSelected
                            )
                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                            if (msg.startsWith("Feedback uploaded")) {
                                reviewTextInput = "" // Reset on success
                            }
                        },
                        modifier = Modifier.fillMaxWidth().testTag("submit_feedback_btn"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Post Verified Feedback")
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.addToCart(food)
                        selectedFoodForDetails = null
                        Toast.makeText(context, "${food.name} Added to Cart!", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text("Add to Cart (${String.format("%,d", food.krwPrice)} ₩)")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedFoodForDetails = null }) {
                    Text("Dismiss")
                }
            }
        )
    }
}

private fun isCountryFoodProduct(product: Product): Boolean {
    val countrySubcategories = setOf("Nepal", "India", "Pakistan", "Bangladesh", "Southeast Asia", "Vietnam")
    return countrySubcategories.contains(product.subCategory)
}
