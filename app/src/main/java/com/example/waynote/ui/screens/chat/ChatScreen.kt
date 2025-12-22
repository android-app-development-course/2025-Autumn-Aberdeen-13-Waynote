package com.example.waynote

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(sender: String, navController: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val meAvatarFlow = remember {
        context.authDataStore.data.map { prefs -> prefs[AuthKeys.AvatarUri].orEmpty() }
    }
    val meAvatar by meAvatarFlow.collectAsState(initial = "")
    val partnerAvatar = remember(sender) { contactAvatarForSender(sender) }
    val chatHistoryFlow = remember {
        context.chatDataStore.data.map { prefs ->
            decodeChatHistory(prefs[ChatKeys.History].orEmpty())
        }
    }
    val chatHistory by chatHistoryFlow.collectAsState(initial = emptyMap())
    var messageText by rememberSaveable { mutableStateOf("") }
    val messages = remember(sender) { mutableStateListOf<ChatMessage>() }

    LaunchedEffect(sender, chatHistory) {
        val persisted = chatHistory[sender]
        messages.clear()
        if (persisted.isNullOrEmpty()) {
            messages.addAll(initialChatMessages(sender, meAvatar, partnerAvatar))
        } else {
            messages.addAll(persisted)
        }
    }

    LaunchedEffect(meAvatar) {
        if (meAvatar.isNotBlank()) {
            for (index in messages.indices) {
                val msg = messages[index]
                if (msg.isMe) {
                    messages[index] = msg.copy(avatarUrl = meAvatar)
                }
            }
            persistChatHistory(context, chatHistory.toMutableMap().apply {
                put(sender, messages.toList())
            })
        }
    }

    fun persistMessages() {
        val updated = chatHistory.toMutableMap()
        updated[sender] = messages.toList()
        scope.launch { persistChatHistory(context, updated) }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(sender.ifBlank { localizedText("Chat", "聊天") }) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = localizedText("Back", "返回")
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(messages) { chat ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (chat.isMe) Arrangement.End else Arrangement.Start,
                        verticalAlignment = Alignment.Top
                    ) {
                        if (!chat.isMe) {
                            ChatAvatar(
                                imageUrl = chat.avatarUrl.ifBlank { partnerAvatar },
                                fallback = sender
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (chat.isMe) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            }
                        ) {
                            Text(
                                text = chat.text,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (chat.isMe) {
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                        }
                        if (chat.isMe) {
                            Spacer(modifier = Modifier.width(8.dp))
                            ChatAvatar(
                                imageUrl = chat.avatarUrl.ifBlank { meAvatar },
                                fallback = localizedText("Me", "我")
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text(localizedText("Type a message...", "输入消息...")) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (messageText.isNotBlank()) {
                                messages.add(
                                    ChatMessage(
                                        text = messageText.trim(),
                                        isMe = true,
                                        avatarUrl = meAvatar
                                    )
                                )
                                messageText = ""
                                persistMessages()
                            }
                        }
                    )
                )
                Button(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            messages.add(
                                ChatMessage(
                                    text = messageText.trim(),
                                    isMe = true,
                                    avatarUrl = meAvatar
                                )
                            )
                            messageText = ""
                            persistMessages()
                        }
                    },
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(localizedText("Send", "发送"))
                }
            }
        }
    }
}

@Composable
private fun ChatAvatar(imageUrl: String, fallback: String) {
    val initial = fallback.trim().takeIf { it.isNotEmpty() }?.firstOrNull()?.uppercase() ?: "?"
    Surface(
        modifier = Modifier
            .size(34.dp)
            .clip(CircleShape),
        tonalElevation = 1.dp,
        shadowElevation = 2.dp
    ) {
        if (imageUrl.isBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initial,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Avatar",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

private fun initialChatMessages(sender: String, meAvatar: String, partnerAvatar: String): List<ChatMessage> {
    return when (sender) {
        "TripAdvisor" -> listOf(
            ChatMessage("Hey there, I found a sunrise hike that fits your schedule.", isMe = false, avatarUrl = partnerAvatar),
            ChatMessage("Looks great. Can you also check tea tasting after?", isMe = true, avatarUrl = meAvatar),
            ChatMessage("Done. Added both to your plan with buffer time.", isMe = false, avatarUrl = partnerAvatar)
        )
        "Mia - Waynote" -> listOf(
            ChatMessage("Locals suggest a sunset lookout above Fushimi Inari.", isMe = false, avatarUrl = partnerAvatar),
            ChatMessage("Pinned it already. Want me to add a cafe stop?", isMe = false, avatarUrl = partnerAvatar),
            ChatMessage("Yes please, somewhere quiet.", isMe = true, avatarUrl = meAvatar)
        )
        "Air China" -> listOf(
            ChatMessage("Gate updated to E12. Boarding starts 17:50.", isMe = false, avatarUrl = partnerAvatar),
            ChatMessage("Thanks. Any chance of delay?", isMe = true, avatarUrl = meAvatar),
            ChatMessage("Monitoring now. Will alert you if it slips.", isMe = false, avatarUrl = partnerAvatar)
        )
        "Alps Basecamp" -> listOf(
            ChatMessage("Trail map PDF is attached for tomorrow's hike.", isMe = false, avatarUrl = partnerAvatar),
            ChatMessage("Got it. Breakfast still included?", isMe = true, avatarUrl = meAvatar),
            ChatMessage("Yes, from 6:30 to 10:00.", isMe = false, avatarUrl = partnerAvatar)
        )
        else -> listOf(
            ChatMessage("Welcome back! How can I help with your trip?", isMe = false, avatarUrl = partnerAvatar)
        )
    }
}

private fun contactAvatarForSender(sender: String): String {
    return when (sender) {
        "TripAdvisor" -> "https://plus.unsplash.com/premium_photo-1681488053244-a2e42f6968fa?w=400&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTN8fFRyaXAlMjBhcHB8ZW58MHx8MHx8fDA%3D"
        "Mia - Waynote" -> "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=400&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OHx8cHJvZmlsZXxlbnwwfHwwfHx8MA%3D%3D"
        "Air China" -> "https://images.unsplash.com/photo-1739258471788-4a820de58362?w=400&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8QWlyJTIwQ2hpbmF8ZW58MHx8MHx8fDA%3D"
        "Alps Basecamp" -> "https://images.unsplash.com/photo-1528892952291-009c663ce843?auto=format&fit=crop&w=200&q=80"
        else -> ""
    }
}
