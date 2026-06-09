package com.example

import android.os.Bundle
import kotlin.getValue
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.local.AppDatabase
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AuthState
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val db by lazy { AppDatabase.getDatabase(this) }

    private val viewModel: MainViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(
                    db.profileDao(),
                    db.cartDao(),
                    db.customKitDao(),
                    db.placedOrderDao()
                ) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContainer(viewModel)
            }
        }
    }
}

@Composable
fun MainAppContainer(viewModel: MainViewModel) {
    val authState by viewModel.authState.collectAsState()
    var selectedTab by remember { mutableStateOf("home") }

    if (authState !is AuthState.LoggedIn) {
        Scaffold { innerPadding ->
            AuthScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
    } else {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    modifier = Modifier.testTag("main_bottom_nav_bar"),
                    containerColor = Color.White,
                    tonalElevation = 0.dp
                ) {
                    val tabs = listOf(
                        TabItem("home", "Home", Icons.Default.Home),
                        TabItem("kits", "Kits", Icons.Default.ShoppingBag),
                        TabItem("foods", "Foods", Icons.Default.Restaurant),
                        TabItem("services", "Services", Icons.Default.Build),
                        TabItem("cart", "Cart", Icons.Default.ShoppingCart),
                        TabItem("community", "AI Forums", Icons.Default.Forum),
                        TabItem("profile", "Profile", Icons.Default.Person)
                    )

                    tabs.forEach { tab ->
                        val isSelected = selectedTab == tab.id
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { selectedTab = tab.id },
                            label = {
                                Text(
                                    text = tab.label,
                                    fontSize = 9.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    maxLines = 1
                                )
                            },
                            alwaysShowLabel = false, // High-density UX hides passive labels for elegance
                            icon = {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.label,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            modifier = Modifier.testTag("nav_${tab.id}")
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (selectedTab) {
                    "home" -> HomeScreen(
                        viewModel = viewModel,
                        onNavigateToTab = { selectedTab = it }
                    )
                    "kits" -> KitsScreen(viewModel = viewModel)
                    "foods" -> FoodsScreen(viewModel = viewModel)
                    "services" -> ServicesScreen(viewModel = viewModel)
                    "cart" -> CartScreen(
                        viewModel = viewModel,
                        onNavigateToTab = { selectedTab = it }
                    )
                    "community" -> CommunityScreen(viewModel = viewModel)
                    "profile" -> ProfileScreen(viewModel = viewModel)
                }
            }
        }
    }
}

data class TabItem(
    val id: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
