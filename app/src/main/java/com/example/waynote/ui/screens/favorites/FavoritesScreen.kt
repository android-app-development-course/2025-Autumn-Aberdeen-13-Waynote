package com.example.waynote

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    navController: NavHostController,
    favoriteDestinations: List<Destination>,
    favoritePosts: List<CommunityPost>,
    favoritePostIds: List<Int>,
    onDestinationClick: (Destination) -> Unit,
    onTogglePostFavorite: (CommunityPost) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by rememberSaveable { mutableStateOf(FavoriteTab.PopularPicks) }
    val tabs = FavoriteTab.values()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(localizedText("My Favorites", "我的收藏")) },
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
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(
                selectedTabIndex = tabs.indexOf(selectedTab),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[tabs.indexOf(selectedTab)]),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            ) {
                tabs.forEach { tab ->
                    Tab(
                        selected = tab == selectedTab,
                        onClick = { selectedTab = tab },
                        text = { Text(tab.localizedLabel(), fontWeight = FontWeight.SemiBold) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            when (selectedTab) {
                FavoriteTab.PopularPicks -> {
                    if (favoriteDestinations.isEmpty()) {
                        FavoritesEmptyState(
                            title = localizedText("No destinations yet", "还没有收藏的目的地"),
                            subtitle = localizedText(
                                "Save a destination from Popular Picks to see it here.",
                                "在热门精选中收藏目的地后会显示在这里。"
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(favoriteDestinations) { destination ->
                                DestinationCard(
                                    destination = destination,
                                    onClick = { onDestinationClick(destination) }
                                )
                            }
                        }
                    }
                }
                FavoriteTab.FavoritedPosts -> {
                    if (favoritePosts.isEmpty()) {
                        FavoritesEmptyState(
                            title = localizedText("No posts yet", "还没有收藏的帖子"),
                            subtitle = localizedText(
                                "Tap the bookmark on a community post to collect it.",
                                "在社区帖子上点击书签即可收藏。"
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            items(favoritePosts) { post ->
                                CommunityPostCard(
                                    post = post,
                                    isFavorited = favoritePostIds.contains(post.id),
                                    onToggleFavorite = onTogglePostFavorite
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
private fun FavoritesEmptyState(title: String, subtitle: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}
