package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.NavigationDestination
import com.example.ui.theme.*

/**
 * NOVACAST 2-COLUMN, 6-TILE HOME DASHBOARD
 * Neutral entry portal — does NOT force a lake on the user.
 * Lets the angler declare their intent first: Discover water, build a game plan,
 * check in from the bank, explore their tacklebox, learn techniques, or run to Walmart.
 */
@Composable
fun HomeDashboardScreen(
    onNavigate: (NavigationDestination) -> Unit,
    onOpenFeedback: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NcBg)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Welcome & Intent Header
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "FISHINTUITION",
                    color = NcLime,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp,
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = NcLime.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NcLime.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "Fish Psychology",
                        color = NcLime,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = "What's the plan today?",
                color = TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Find water, build a strategy before heading out, or get live guidance while you fish.",
                color = TextSecondary,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Hands-Free AI Audio HUD Quick Banner (§6)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigate(NavigationDestination.HANDS_FREE_AI) },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF081C22)),
            border = androidx.compose.foundation.BorderStroke(1.dp, HydroCyan.copy(alpha = 0.6f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(HydroCyan.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = "Hands-Free Voice AI",
                            tint = HydroCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "HANDS-FREE AI ASSISTANT",
                                color = HydroCyan,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(CatchGreen)
                            )
                        }
                        Text(
                            text = "Wet-hands audio coach • Instant tactical diagnosis",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = HydroCyan, modifier = Modifier.size(18.dp))
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 2-Column, 6-Tile Master Grid (§6 of Bible v2.0)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Tile 1: RECON (Water Discovery)
            item {
                HomeDashboardTile(
                    title = "RECON",
                    subtitle = "Find Water Near You",
                    description = "Discover lakes, rivers, and neighborhood ponds on an interactive map.",
                    icon = Icons.Default.Explore,
                    badgeText = "DISCOVERY",
                    accentColor = HydroCyan,
                    onClick = { onNavigate(NavigationDestination.RECON) }
                )
            }

            // Tile 2: GAME PLAN (Strategy Builder)
            item {
                HomeDashboardTile(
                    title = "GAME PLAN",
                    subtitle = "Build a Strategy",
                    description = "Choose your water and species to formulate your approach before heading out.",
                    icon = Icons.Default.Assignment,
                    badgeText = "STRATEGY",
                    accentColor = NcLime,
                    onClick = { onNavigate(NavigationDestination.GAME_PLAN) }
                )
            }

            // Tile 3: ON THE BANK (Active In-Field Companion)
            item {
                HomeDashboardTile(
                    title = "ON THE BANK",
                    subtitle = "Already at the Water?",
                    description = "Auto-detect location, troubleshoot slow bites, and use hands-free voice coach.",
                    icon = Icons.Default.RecordVoiceOver,
                    badgeText = "IN THE FIELD",
                    accentColor = StrikeAmber,
                    onClick = { onNavigate(NavigationDestination.ON_THE_BANK) }
                )
            }

            // Tile 4: TACKLEBOX (Inventory Personalization)
            item {
                HomeDashboardTile(
                    title = "TACKLEBOX",
                    subtitle = "My Gear & Lures",
                    description = "Manage your inventory so recommendations highlight gear you actually own.",
                    icon = Icons.Default.Inventory2,
                    badgeText = "INVENTORY",
                    accentColor = CatchGreen,
                    onClick = { onNavigate(NavigationDestination.TACKLEBOX) }
                )
            }

            // Tile 5: LEARN & FUN (Field Guide & Mentorship)
            item {
                HomeDashboardTile(
                    title = "LEARN & FUN",
                    subtitle = "Field Guide",
                    description = "Knot tying steps, how to hook live bait, and 60-second backlash fixes.",
                    icon = Icons.Default.School,
                    badgeText = "MENTOR",
                    accentColor = Color(0xFFA855F7), // Purple accent
                    onClick = { onNavigate(NavigationDestination.LEARN_AND_FUN) }
                )
            }

            // Tile 6: WALMART RUN (Gear Shopping Utility)
            item {
                HomeDashboardTile(
                    title = "WALMART RUN",
                    subtitle = "Gear Shopping List",
                    description = "Realistic starting gear checklist, prices, and Walmart sporting goods aisle tips.",
                    icon = Icons.Default.ShoppingCart,
                    badgeText = "SHOPPING",
                    accentColor = Color(0xFFF97316), // Orange accent
                    onClick = { onNavigate(NavigationDestination.WALMART_RUN) }
                )
            }

            // Footer Item: Feedback & Contact Founder
            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 16.dp)
                        .clickable { onOpenFeedback() },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = NcCardElevated),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NcLime.copy(alpha = 0.35f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(NcLimeGlow),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ChatBubbleOutline,
                                    contentDescription = null,
                                    tint = NcLime,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Angler Feedback & Suggestions",
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Suggest secret spots, request lures, or submit feature ideas",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = NcLime,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeDashboardTile(
    title: String,
    subtitle: String,
    description: String,
    icon: ImageVector,
    badgeText: String,
    accentColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = NcCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, NcBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Row: Icon & Category Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = NcCardElevated,
                    border = androidx.compose.foundation.BorderStroke(0.8.dp, accentColor.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = badgeText,
                        color = accentColor,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Bottom Text Details
            Column {
                Text(
                    text = title,
                    color = TextPrimary,
                    fontSize = 18.sp,
                    letterSpacing = 1.sp,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = subtitle,
                    color = accentColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    color = TextSecondary,
                    fontSize = 10.sp,
                    lineHeight = 14.sp,
                    maxLines = 3
                )
            }
        }
    }
}
