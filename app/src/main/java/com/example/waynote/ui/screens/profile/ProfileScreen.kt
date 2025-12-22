package com.example.waynote

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceholderScreen(title: String, modifier: Modifier = Modifier) {
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text(title) }) }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(text = title, style = MaterialTheme.typography.headlineSmall)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    currentUser: String,
    avatarUri: String,
    onAvatarChange: (String) -> Unit,
    favoriteDestinationCount: Int,
    favoritePostCount: Int,
    flightOrders: List<FlightOrder>,
    onOrdersClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onLogout: () -> Unit,
    language: AppLanguage = AppLanguage.English,
    onLanguageChange: (AppLanguage) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var localAvatarUri by rememberSaveable { mutableStateOf(avatarUri) }
    LaunchedEffect(avatarUri) {
        localAvatarUri = avatarUri
    }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            val selected = uri?.toString().orEmpty()
            if (selected.isNotBlank()) {
                localAvatarUri = selected
                uri?.let {
                    runCatching {
                        context.contentResolver.takePersistableUriPermission(
                            it,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                    }
                }
                onAvatarChange(selected)
            }
        }
    )
    val favoritesSummary = if (favoriteDestinationCount + favoritePostCount == 0) {
        localizedText("Browse and save places or posts.", "浏览并收藏目的地或帖子。", language)
    } else {
        val destinationsLabel = localizedText(
            "$favoriteDestinationCount destinations",
            "${favoriteDestinationCount}个目的地",
            language
        )
        val postsLabel = localizedText(
            "$favoritePostCount posts",
            "${favoritePostCount}篇帖子",
            language
        )
        listOf(destinationsLabel, postsLabel).joinToString(" · ")
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(localizedText("Profile", "个人资料", language)) })
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .size(108.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { galleryLauncher.launch(arrayOf("image/*")) },
                    tonalElevation = 2.dp,
                    shadowElevation = 4.dp
                ) {
                    if (localAvatarUri.isBlank()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentUser.takeIf { it.isNotBlank() }?.firstOrNull()?.uppercase()
                                    ?: localizedText("U", "我", language),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        AsyncImage(
                            model = localAvatarUri,
                            contentDescription = localizedText("Avatar", "头像", language),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                OutlinedButton(
                    onClick = { galleryLauncher.launch(arrayOf("image/*")) },
                    shape = RoundedCornerShape(50)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = localizedText("Choose avatar", "选择头像", language)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(localizedText("Choose from gallery", "从相册选择", language))
                }
            }
            Text(
                text = if (currentUser.isNotBlank()) {
                    localizedText("Logged in as $currentUser", "当前登录：$currentUser", language)
                } else {
                    localizedText("Logged in", "已登录", language)
                },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = localizedText("Your session stays signed in on this device.", "此设备将保持登录状态。", language),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 2.dp,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = localizedText("Language", "界面语言", language),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = localizedText(
                                "Switch between English and Chinese.",
                                "切换英文或中文界面。",
                                language
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                    Switch(
                        checked = language == AppLanguage.Chinese,
                        onCheckedChange = { isChecked ->
                            onLanguageChange(if (isChecked) AppLanguage.Chinese else AppLanguage.English)
                        }
                    )
                }
            }
            FlightOrdersCard(
                orders = flightOrders,
                onClick = onOrdersClick,
                language = language
            )

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 2.dp,
                shadowElevation = 2.dp,
                onClick = onFavoritesClick
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = localizedText("My Favorites", "我的收藏", language),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = favoritesSummary,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = localizedText("My Favorites", "我的收藏", language),
                        modifier = Modifier.rotate(180f)
                    )
                }
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onLogout,
                shape = RoundedCornerShape(18.dp)
            ) {
                Text(localizedText("LOG OUT", "退出登录", language))
            }
        }
    }
}
