package com.example.waynote

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.waynote.ui.theme.AquaAccent
import com.example.waynote.ui.theme.PowderBlue
import com.example.waynote.ui.theme.SkyBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen(
    navController: NavHostController,
    favoritePostIds: List<Int>,
    onTogglePostFavorite: (CommunityPost) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = CommunityTab.values()
    var selectedTab by rememberSaveable { mutableStateOf(CommunityTab.Recommended) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var showTypePicker by rememberSaveable { mutableStateOf(false) }
    var showPublishSheet by rememberSaveable { mutableStateOf(false) }
    var selectedShareType by rememberSaveable { mutableStateOf(ShareType.PhotoText) }
    val typePickerState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val publishSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val activePosts = when (selectedTab) {
        CommunityTab.Recommended -> recommendedCommunityPosts
        CommunityTab.Following -> followingCommunityPosts
        CommunityTab.Challenges -> challengeCommunityPosts
    }
    val filteredPosts = activePosts.filter { it.matchesQuery(searchQuery) }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(localizedText("Community", "社区")) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = localizedText("Back", "返回")
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showTypePicker = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = localizedText("New post", "新帖子")
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { CommunityBanner() }
            item {
                CommunityTabs(
                    tabs = tabs,
                    selected = selectedTab,
                    onSelect = { selectedTab = it },
                    searchQuery = searchQuery,
                    onSearchChange = { searchQuery = it }
                )
            }
            if (filteredPosts.isEmpty()) {
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        tonalElevation = 1.dp,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.08f))
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = localizedText("No posts yet", "暂无帖子"),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = localizedText(
                                    "Try another tab or tweak your search.",
                                    "试试其他标签或调整搜索条件。",
                                ),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.66f)
                            )
                        }
                    }
                }
            } else {
                items(filteredPosts) { post ->
                    CommunityPostCard(
                        post = post,
                        isFavorited = favoritePostIds.contains(post.id),
                        onToggleFavorite = onTogglePostFavorite
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(72.dp)) }
        }
    }

    if (showTypePicker) {
        ModalBottomSheet(
            onDismissRequest = { showTypePicker = false },
            sheetState = typePickerState,
            dragHandle = null
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = localizedText("What do you want to share?", "你想发布什么内容？"),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = localizedText(
                        "Pick post or video so we can route it to the right people.",
                        "选择图文或视频，方便推荐给合适的用户。",
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                OutlinedButton(
                    onClick = {
                        selectedShareType = ShareType.PhotoText
                        showTypePicker = false
                        showPublishSheet = true
                    },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                localizedText("Post", "图文"),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = localizedText(
                                    "Travel notes, guides, inspiration sets",
                                    "旅行笔记、攻略、灵感合集"
                                ),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                        Icon(imageVector = Icons.Outlined.Map, contentDescription = null)
                    }
                }
                OutlinedButton(
                    onClick = {
                        selectedShareType = ShareType.Video
                        showTypePicker = false
                        showPublishSheet = true
                    },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                localizedText("Video", "视频"),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = localizedText(
                                    "Route vlogs, challenges, live scenery",
                                    "路线Vlog、挑战或实况风景"
                                ),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                        Icon(imageVector = Icons.Outlined.PlayCircle, contentDescription = null)
                    }
                }
                TextButton(
                    onClick = { showTypePicker = false },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(localizedText("Cancel", "取消"))
                }
            }
        }
    }

    if (showPublishSheet) {
        ModalBottomSheet(
            onDismissRequest = { showPublishSheet = false },
            sheetState = publishSheetState
        ) {
            SharePublishForm(
                shareType = selectedShareType,
                onPublish = { showPublishSheet = false },
                onClose = { showPublishSheet = false }
            )
        }
    }
}

@Composable
private fun CommunityBanner() {
    val gradient = Brush.linearGradient(
        colors = listOf(SkyBlue, AquaAccent.copy(alpha = 0.88f), PowderBlue)
    )
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 2.dp,
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f))
    ) {
        Box(
            modifier = Modifier
                .background(gradient)
                .padding(18.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Surface(
                    color = Color.White.copy(alpha = 0.16f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.24f))
                ) {
                    Text(
                        text = localizedText("Waynote Community", "Waynote 社区"),
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(
                    text = localizedText("Inspiration · Routes · Live challenges", "灵感 · 路线 · 实时挑战"),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = localizedText(
                        "Real trips meet smart recommendations. Fresh editor picks and challenges every day.",
                        "真实行程与智能推荐相遇。每天都有新编辑精选与挑战。",
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
private fun CommunityTabs(
    tabs: Array<CommunityTab>,
    selected: CommunityTab,
    onSelect: (CommunityTab) -> Unit,
    searchQuery: String,
    onSearchChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        TabRow(
            selectedTabIndex = tabs.indexOf(selected),
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[tabs.indexOf(selected)])
                        .height(3.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        ) {
            tabs.forEach { tab ->
                Tab(
                    selected = tab == selected,
                    onClick = { onSelect(tab) },
                    text = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = tab.localizedLabel(), fontWeight = FontWeight.SemiBold)
                            Text(
                                text = tab.localizedSubLabel(),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                )
            }
        }
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Search, contentDescription = localizedText("Search", "搜索"))
            },
            placeholder = { Text(localizedText("Search title / summary / tags", "搜索标题 / 摘要 / 标签")) },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun CommunityPostCard(
    post: CommunityPost,
    isFavorited: Boolean,
    onToggleFavorite: (CommunityPost) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        tonalElevation = 2.dp,
        shadowElevation = 4.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = post.author.avatarUrl,
                        contentDescription = post.author.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                    )
                    Column {
                        Text(
                            text = post.author.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (post.highlight.isNotBlank()) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.16f)
                            ) {
                                Text(
                                    text = post.highlight,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
                IconButton(onClick = { onToggleFavorite(post) }) {
                    Icon(
                        imageVector = if (isFavorited) Icons.Outlined.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = if (isFavorited) {
                            localizedText("Remove from favorites", "取消收藏")
                        } else {
                            localizedText("Save", "收藏")
                        },
                        tint = if (isFavorited) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = post.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = post.summary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.74f),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (post.media.isNotEmpty()) {
                MediaPreviewRow(media = post.media)
            }
            TagRow(tags = post.tags)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatPill(icon = Icons.Outlined.FavoriteBorder, value = post.likes, modifier = Modifier.weight(1f))
                StatPill(icon = Icons.Outlined.ChatBubbleOutline, value = post.comments, modifier = Modifier.weight(1f))
                StatPill(icon = Icons.Outlined.BookmarkBorder, value = post.saves, modifier = Modifier.weight(1f))
            }
            Text(
                text = "${post.location} · ${post.device}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun MediaPreviewRow(media: List<CommunityMedia>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        media.take(3).forEach { item ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(14.dp))
            ) {
                AsyncImage(
                    model = item.thumbnailUrl,
                    contentDescription = item.description,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(14.dp))
                )
                if (item.type == MediaType.Video) {
                    Surface(
                        modifier = Modifier
                            .padding(10.dp)
                            .align(Alignment.BottomStart),
                        shape = RoundedCornerShape(10.dp),
                        color = Color.Black.copy(alpha = 0.5f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.PlayCircle,
                                contentDescription = null,
                                tint = Color.White
                            )
                            if (item.durationLabel != null) {
                                Text(
                                    text = item.durationLabel,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TagRow(tags: List<String>) {
    if (tags.isEmpty()) return
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tags.forEach { tag ->
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFFFF3C4),
                border = BorderStroke(1.dp, Color(0xFFFFD27F))
            ) {
                Text(
                    text = "#$tag",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF8A6B00)
                )
            }
        }
    }
}

@Composable
private fun StatPill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 0.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun SharePublishForm(
    shareType: ShareType,
    onPublish: () -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var title by rememberSaveable { mutableStateOf("") }
    var summary by rememberSaveable { mutableStateOf("") }
    var tags by rememberSaveable { mutableStateOf("") }
    var location by rememberSaveable { mutableStateOf("") }
    var isAnonymous by rememberSaveable { mutableStateOf(false) }
    val language = LocalAppLanguage.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (shareType == ShareType.PhotoText) {
                    localizedText("Publish post", "发布图文")
                } else {
                    localizedText("Publish video", "发布视频")
                },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = onClose) {
                Text(localizedText("Close", "关闭"))
            }
        }
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text(localizedText("Title", "标题")) },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = summary,
            onValueChange = { summary = it },
            label = { Text(localizedText("Summary / description", "摘要 / 描述")) },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 90.dp)
        )
        OutlinedTextField(
            value = tags,
            onValueChange = { tags = it },
            label = { Text(localizedText("Tags (space separated)", "标签（空格分隔）")) },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text(localizedText("Location", "位置")) },
            leadingIcon = { Icon(imageVector = Icons.Outlined.Place, contentDescription = null) },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = localizedText("Post anonymously", "匿名发布"),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = localizedText(
                        "Hide your avatar and nickname for this post.",
                        "隐藏头像和昵称，匿名展示本条内容。"
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            Switch(checked = isAnonymous, onCheckedChange = { isAnonymous = it })
        }
        Button(
            onClick = {
                Toast.makeText(
                    context,
                    localizedText(
                        "Submitted. We’ll review and publish soon.",
                        "已提交，我们会尽快审核发布。",
                        language
                    ),
                    Toast.LENGTH_SHORT
                ).show()
                onPublish()
            },
            enabled = title.isNotBlank(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(localizedText("Publish", "发布"))
        }
    }
}

private fun CommunityPost.matchesQuery(query: String): Boolean {
    if (query.isBlank()) return true
    val lower = query.lowercase()
    return title.lowercase().contains(lower) ||
        summary.lowercase().contains(lower) ||
        tags.any { it.lowercase().contains(lower) }
}
