package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EnvironmentalConditions
import com.example.data.model.FishSpecies
import com.example.data.model.WaterBody
import com.example.ui.NavigationDestination
import com.example.ui.theme.*

@Composable
fun TopTacticalBar(
    currentDestination: NavigationDestination,
    waterBody: WaterBody,
    species: FishSpecies,
    conditions: EnvironmentalConditions,
    isSpeaking: Boolean,
    isListening: Boolean,
    onHomeClick: () -> Unit,
    onWaterClick: () -> Unit,
    onSpeciesClick: () -> Unit,
    onConditionsClick: () -> Unit,
    onVoiceClick: () -> Unit,
    onStopSpeaking: () -> Unit,
    onFeedbackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = NcCard,
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Branding & Header
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (currentDestination != NavigationDestination.HOME_DASHBOARD) {
                        IconButton(
                            onClick = onHomeClick,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(NcLimeGlow)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back to Home",
                                tint = NcLime,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    } else {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(NcLimeGlow),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhonelinkRing,
                                contentDescription = null,
                                tint = NcLime,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                    }

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(NcLime)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "FISHINTUITION",
                                color = NcLime,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.5.sp,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = when (currentDestination) {
                                NavigationDestination.HOME_DASHBOARD -> "Fish Psychology"
                                NavigationDestination.RECON -> "Recon • Water Discovery"
                                NavigationDestination.GAME_PLAN -> "${waterBody.name} • Strategy"
                                NavigationDestination.ON_THE_BANK -> "${waterBody.name} • On the Bank"
                                NavigationDestination.HANDS_FREE_AI -> "Hands-Free AI • Audio HUD"
                                NavigationDestination.TACKLEBOX -> "Digital Tacklebox"
                                NavigationDestination.LEARN_AND_FUN -> "Learn & Fun • Field Guide"
                                NavigationDestination.WALMART_RUN -> "Walmart Run • Checklist"
                                NavigationDestination.CATCH_LOG -> "Catch Log & Analytics"
                            },
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }

                // Voice Assistant Button (Hands-Free)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isSpeaking) {
                        IconButton(
                            onClick = onStopSpeaking,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(WarningRed.copy(alpha = 0.2f))
                                .border(1.dp, WarningRed, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeOff,
                                contentDescription = "Mute Voice",
                                tint = WarningRed,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    IconButton(
                        onClick = onFeedbackClick,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(NcCardElevated)
                            .border(1.dp, NcBorder, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChatBubbleOutline,
                            contentDescription = "Angler Feedback",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))

                    FilledTonalIconButton(
                        onClick = onVoiceClick,
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = if (isListening) NcLime else if (isSpeaking) StrikeAmber else NcCardElevated,
                            contentColor = if (isListening) NcBg else if (isSpeaking) NcBg else NcLime
                        ),
                        modifier = Modifier
                            .size(40.dp)
                            .border(
                                width = 1.dp,
                                color = if (isListening) NcLime else NcBorder,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = if (isListening) Icons.Default.Mic else if (isSpeaking) Icons.Default.VolumeUp else Icons.Default.MicNone,
                            contentDescription = "Hands-Free Voice",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Only show contextual species & condition chips when the user has entered an active strategy or bank session!
            if (currentDestination == NavigationDestination.GAME_PLAN || currentDestination == NavigationDestination.ON_THE_BANK) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Target Species Chip
                    Surface(
                        onClick = onSpeciesClick,
                        shape = RoundedCornerShape(10.dp),
                        color = NcCardElevated,
                        border = androidx.compose.foundation.BorderStroke(1.dp, NcLime.copy(alpha = 0.4f)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Pets,
                                contentDescription = null,
                                tint = NcLime,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = "TARGET SPECIES",
                                    color = TextMuted,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = species.commonName,
                                    color = TextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    // Live Conditions Pill
                    Surface(
                        onClick = onConditionsClick,
                        shape = RoundedCornerShape(10.dp),
                        color = NcCardElevated,
                        border = androidx.compose.foundation.BorderStroke(1.dp, StrikeAmber.copy(alpha = 0.4f)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = null,
                                tint = StrikeAmber,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = "${conditions.waterTempF}°F • ${conditions.barometerInHg} inHg",
                                    color = TextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1
                                )
                                Text(
                                    text = conditions.waterClarity.label,
                                    color = TextSecondary,
                                    fontSize = 10.sp,
                                    maxLines = 1
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
fun AudibleVoiceResponseBanner(
    text: String?,
    isSpeaking: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = !text.isNullOrBlank(),
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        if (text != null) {
            Surface(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                color = NcCardElevated,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, if (isSpeaking) NcLime else NcBorder)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(if (isSpeaking) NcLimeGlow else NcCard),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isSpeaking) Icons.Default.GraphicEq else Icons.Default.RecordVoiceOver,
                            contentDescription = "Voice",
                            tint = if (isSpeaking) NcLime else StrikeAmber,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (isSpeaking) "AUDIO COACH SPEAKING" else "TACTICAL VOICE RESPONSE",
                                color = if (isSpeaking) NcLime else StrikeAmber,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                            IconButton(
                                onClick = onDismiss,
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Dismiss",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = text,
                            color = TextPrimary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SolunarFeedingCard(
    conditions: EnvironmentalConditions,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = NcCard),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, NcBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = conditions.moonPhase.iconSymbol,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "SOLUNAR & LUNAR FEEDING CYCLES",
                            color = StrikeAmber,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "${conditions.moonPhase.label} • ${conditions.moonPhase.illuminationPercent}% Illumination",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = StrikeAmber.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StrikeAmber.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "SOLUNAR RATING: ${conditions.solunarRating}/10",
                        color = StrikeAmber,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Major & Minor Feeding Windows
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Major Window Card
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = NcCardElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, NcLime.copy(alpha = 0.4f)),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(NcLime)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "MAJOR WINDOW",
                                color = NcLime,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = conditions.majorWindows.firstOrNull()?.timeRange ?: "7:15 AM - 9:30 AM",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Moon Overhead Transit",
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                    }
                }

                // Minor Window Card
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = NcCardElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, HydroCyan.copy(alpha = 0.4f)),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(HydroCyan)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "MINOR WINDOW",
                                color = HydroCyan,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = conditions.minorWindows.firstOrNull()?.timeRange ?: "1:50 PM - 2:50 PM",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Moonset Alignment",
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Barometric & Provenance Status Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pressure: ${conditions.barometerInHg} inHg (${conditions.pressureTrend.label})",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color.White.copy(alpha = 0.05f)
                ) {
                    Text(
                        text = "Calculated Astro Model",
                        color = TextMuted,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
