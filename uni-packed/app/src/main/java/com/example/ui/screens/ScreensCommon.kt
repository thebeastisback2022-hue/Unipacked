package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarHalf
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.UniPrimary
import com.example.ui.theme.UniSecondary
import com.example.ui.theme.UniTertiary

@Composable
fun AppLogoBranding(
    modifier: Modifier = Modifier,
    showTagline: Boolean = true,
    height: Dp = 80.dp
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(height)
                .background(Color.White, CircleShape)
                .shadow(elevation = 2.dp, shape = CircleShape)
                .clip(CircleShape)
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = com.example.R.drawable.unipacked_brand_logo),
                contentDescription = "Uni Packed Official Brand Logo",
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Title: "Uni Packed" in custom paired blue/orange
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "Uni",
                color = UniPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                " Packed",
                color = UniSecondary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )
        }

        if (showTagline) {
            Text(
                "Pack Smart. Arrive Easy. Settle Faster.",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontSize = 12.sp,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
fun StarRatingBar(
    rating: Float,
    modifier: Modifier = Modifier,
    onRatingChanged: ((Int) -> Unit)? = null
) {
    Row(modifier = modifier) {
        val filledStars = rating.toInt()
        val hasHalfStar = rating - filledStars >= 0.25f && rating - filledStars < 0.75f
        val filledHalfOrFull = rating - filledStars >= 0.75f

        for (i in 1..5) {
            val starIcon = when {
                i <= filledStars -> Icons.Filled.Star
                i == filledStars + 1 && hasHalfStar -> Icons.Outlined.StarHalf
                i == filledStars + 1 && filledHalfOrFull -> Icons.Filled.Star
                else -> Icons.Outlined.StarOutline
            }

            Icon(
                imageVector = starIcon,
                contentDescription = "Star $i",
                tint = if (i <= (rating + 0.25f)) Color(0xFFFFB300) else Color.LightGray,
                modifier = Modifier
                    .size(18.dp)
                    .clickable(enabled = onRatingChanged != null) {
                        onRatingChanged?.invoke(i)
                    }
            )
        }
    }
}

@Composable
fun SectionHeader(title: String, subtitle: String? = null) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        subtitle?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun HighlightBadge(
    text: String,
    backgroundColor: Color,
    textColor: Color = Color.White
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor.copy(alpha = 0.15f))
            .border(1.dp, backgroundColor.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = backgroundColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun getProductPainter(imgUrl: String): androidx.compose.ui.graphics.painter.Painter {
    val context = androidx.compose.ui.platform.LocalContext.current
    if (imgUrl.isNotEmpty()) {
        val cleanName = imgUrl.substringBeforeLast(".") // strip extension if any
        val resourceId = context.resources.getIdentifier(cleanName, "drawable", context.packageName)
        if (resourceId != 0) {
            return androidx.compose.ui.res.painterResource(id = resourceId)
        }
    }
    // Fallback brand logo
    return androidx.compose.ui.res.painterResource(id = com.example.R.drawable.unipacked_brand_logo)
}

