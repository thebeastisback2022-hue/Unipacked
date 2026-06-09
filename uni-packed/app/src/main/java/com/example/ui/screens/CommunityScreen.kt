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
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.horizontalScroll
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.ChatMessage
import com.example.ui.viewmodel.MainViewModel

@Composable
fun CommunityScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var communityTab by remember { mutableStateOf(0) } // 0 = Chat with Uni, 1 = Discussion Board

    val chatLogs = viewModel.chatMessages
    val isThinking by viewModel.isAiThinking.collectAsState()

    var chatInputFieldText by remember { mutableStateOf("") }
    var voiceModeActive by remember { mutableStateOf(false) }

    // Discussion boards mock states
    val localPosts = remember {
        mutableStateListOf(
            ForumPost("Anya Patel", "Chuncheon Asian Food Mart is stocked with fresh mutton and premium longbasmati rice this week! Near KNU backgate.", 18, 4),
            ForumPost("Muhammad Ali", "Heads up! Chuncheon Immigration ARC reservation slots are completely filled for August. Highly advise booking via Uni Packed fast-track services immediately.", 34, 12),
            ForumPost("Liam Tran", "Do dorms open during Chuseok holiday? Yes, but you must report cooking setups beforehand.", 9, 2)
        )
    }

    var newPostContent by remember { mutableStateOf("") }

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
                    "Student Community & AI Hub",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Ask Uni AI • Swap tips with peers in Gangwon province",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }

        // --- SUB TABS ---
        TabRow(selectedTabIndex = communityTab) {
            Tab(selected = communityTab == 0, onClick = { communityTab = 0 }) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.SmartToy, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Chat with Uni", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            Tab(selected = communityTab == 1, onClick = { communityTab = 1 }) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Forum, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Student Forums", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // --- SUB VIEWS ---
        Box(modifier = Modifier.weight(1f)) {
            when (communityTab) {
                0 -> {
                    // --- CHAT WITH UNI ENGINE ---
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Scrolling chat messages
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 16.dp),
                            contentPadding = PaddingValues(top = 12.dp, bottom = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(chatLogs) { msg ->
                                val isUser = msg.sender == "user"
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(
                                                RoundedCornerShape(
                                                    topStart = 12.dp,
                                                    topEnd = 12.dp,
                                                    bottomStart = if (isUser) 12.dp else 2.dp,
                                                    bottomEnd = if (isUser) 2.dp else 12.dp
                                                )
                                            )
                                            .background(
                                                if (isUser) MaterialTheme.colorScheme.primary
                                                else MaterialTheme.colorScheme.surface
                                            )
                                            .border(
                                                1.dp,
                                                if (isUser) Color.Transparent else Color.LightGray.copy(alpha = 0.4f),
                                                RoundedCornerShape(12.dp)
                                            )
                                            .padding(12.dp)
                                            .widthIn(max = 280.dp)
                                    ) {
                                        Text(
                                            text = msg.message,
                                            color = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface,
                                            fontSize = 13.sp,
                                            lineHeight = 18.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = if (isUser) "You • ${msg.timestamp}" else "Uni Assistant • ${msg.timestamp}",
                                        fontSize = 9.sp,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(horizontal = 4.dp)
                                    )
                                }
                            }

                            if (isThinking) {
                                item {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(12.dp)
                                    ) {
                                        CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text("Uni is searching campus knowledge base...", fontSize = 11.sp, color = Color.Gray)
                                    }
                                }
                            }
                        }

                        // Bottom writing field
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(8.dp),
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                // Dynamic chips recommendation questions
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    QuickChatChip("🚌 Airport Bus #8113 Chuncheon Help") { chatInputFieldText = "How do I take bus #8113 from Incheon to Chuncheon?" }
                                    QuickChatChip("📄 How do I get an ARC visa?") { chatInputFieldText = "What documentation do I submit to apply for Alien Registration Card?" }
                                    QuickChatChip("🍛 Do we have Halal food near KNU?") { chatInputFieldText = "Where can I find Halal groceries or restaurants near Kangwon campus?" }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Voice activation toggle
                                    IconButton(
                                        onClick = {
                                            voiceModeActive = !voiceModeActive
                                            if (voiceModeActive) {
                                                Toast.makeText(context, "Microphone enabled! Try talking: 'Hello Uni'", Toast.LENGTH_SHORT).show()
                                                chatInputFieldText = "Can you help me translate 'Hello shower room is locked' into conversational Korean?"
                                            } else {
                                                Toast.makeText(context, "Microphone closed", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(
                                                if (voiceModeActive) MaterialTheme.colorScheme.error.copy(alpha = 0.15f)
                                                else Color.LightGray.copy(alpha = 0.25f),
                                                CircleShape
                                            )
                                    ) {
                                        Icon(
                                            if (voiceModeActive) Icons.Default.MicOff else Icons.Default.Mic,
                                            "voice mode",
                                            tint = if (voiceModeActive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    OutlinedTextField(
                                        value = chatInputFieldText,
                                        onValueChange = { chatInputFieldText = it },
                                        placeholder = { Text("Ask Uni about visas, transport or kits...") },
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("chat_input_text")
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    // Send trigger button
                                    IconButton(
                                        onClick = {
                                            if (chatInputFieldText.isNotBlank()) {
                                                viewModel.sendChatMessageToUni(chatInputFieldText)
                                                chatInputFieldText = ""
                                                voiceModeActive = false
                                            }
                                        },
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                                            .testTag("chat_send_btn")
                                    ) {
                                        Icon(Icons.Default.Send, "send message", tint = Color.White, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                1 -> {
                    // --- DISCUSSION BOARD FORUM ---
                    Column(modifier = Modifier.fillMaxSize()) {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 16.dp),
                            contentPadding = PaddingValues(vertical = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(localPosts) { post ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.25f))
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(28.dp)
                                                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f), CircleShape),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(Icons.Default.Person, "user", tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(14.dp))
                                                }
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(post.author, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                            }

                                            HighlightBadge("GANGWON STUDENT", MaterialTheme.colorScheme.primary)
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Text(post.body, fontSize = 12.sp, lineHeight = 16.sp, color = MaterialTheme.colorScheme.onSurface)

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Divider()

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.clickable {
                                                    Toast.makeText(context, "Upvoted!", Toast.LENGTH_SHORT).show()
                                                }
                                            ) {
                                                Icon(Icons.Default.ThumbUp, null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("${post.upvotes}", fontSize = 11.sp, color = Color.Gray)
                                            }

                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.clickable {
                                                    Toast.makeText(context, "Replying closed in sandbox", Toast.LENGTH_SHORT).show()
                                                }
                                            ) {
                                                Icon(Icons.Default.Comment, null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("${post.commentsCount}", fontSize = 11.sp, color = Color.Gray)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Writing new announcement card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(6.dp),
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("✍️ Share a Tip anonymously", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = newPostContent,
                                    onValueChange = { newPostContent = it },
                                    placeholder = { Text("E.g. Is there any prayer space at KNU library? Or local bus routes for Halal groceries...") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Button(
                                    onClick = {
                                        if (newPostContent.isNotBlank()) {
                                            localPosts.add(0, ForumPost("You (International Student)", newPostContent, 1, 0))
                                            newPostContent = ""
                                            Toast.makeText(context, "Uploaded to discussion stream!", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Default.PostAdd, null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Broadcast Student Tip")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuickChatChip(title: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(title, fontSize = 10.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
    }
}

data class ForumPost(
    val author: String,
    val body: String,
    val upvotes: Int,
    val commentsCount: Int
)
