package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.theme.*

/**
 * FishINtuition Dedicated Hands-Free AI Audio HUD Screen
 *
 * Designed specifically for anglers with wet hands, handling fish, or holding a rod.
 * Provides high-contrast glanceability, active context awareness, large tactile voice
 * controls, active cadence coaching, and direct access to the 7 canonical fishing voice intents.
 */
@Composable
fun HandsFreeAiScreen(
    waterBody: WaterBody,
    species: FishSpecies,
    conditions: EnvironmentalConditions,
    recommendation: FishingRecommendation?,
    tackleBoxCount: Int,
    isSpeaking: Boolean,
    isListening: Boolean,
    activeCadenceCoach: String?,
    lastRecognizedQuery: String?,
    lastSpokenResponse: String?,
    onVoiceCommand: (String) -> Unit,
    onStartHandsFreeListening: () -> Unit,
    onStopSpeaking: () -> Unit,
    onLogCatchClick: () -> Unit,
    onNavigateToBank: () -> Unit,
    customTriggers: List<CustomVoiceTrigger> = DefaultVoiceTriggers.createDefaults(),
    onOpenVoiceSettings: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    // Pulse animation for active voice listening & audio speaking
    val infiniteTransition = rememberInfiniteTransition(label = "VoicePulse")
    val micPulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isListening) 1.14f else if (isSpeaking) 1.08f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 750, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "micPulse"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NcBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. ACTIVE SESSION CONTEXT HUD
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NcCard),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, HydroCyan.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(HydroCyan)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "HANDS-FREE AI • AUDIO HUD",
                                color = HydroCyan,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = NcCardElevated,
                            border = androidx.compose.foundation.BorderStroke(1.dp, NcBorderSubtle)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(if (isListening) NcLime else if (isSpeaking) StrikeAmber else TextMuted)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isListening) "LISTENING" else if (isSpeaking) "SPEAKING" else "READY",
                                    color = if (isListening) NcLime else if (isSpeaking) StrikeAmber else TextMuted,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = waterBody.name,
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Context telemetry chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = NcCardElevated,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = "TARGET SPECIES",
                                    color = TextMuted,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = species.commonName,
                                    color = NcLime,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = NcCardElevated,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = "WATER CLARITY (ANGLER)",
                                    color = TextMuted,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = conditions.waterClarity.label,
                                    color = StrikeAmber,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = NcCardElevated,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = "PRESSURE / TREND",
                                    color = TextMuted,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${conditions.barometerInHg} inHg",
                                    color = HydroCyan,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Tied gear confirmation
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF071B20),
                        border = androidx.compose.foundation.BorderStroke(1.dp, HydroCyan.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = CatchGreen, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "TIED LURE (PLAN A)",
                                        color = TextMuted,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = recommendation?.primaryLureName ?: "Squarebill Crankbait",
                                        color = TextPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            TextButton(
                                onClick = onNavigateToBank,
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                            ) {
                                Text("Session View", color = HydroCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(2.dp))
                                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = HydroCyan, modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                }
            }
        }

        // 2. HERO GLOVE-FRIENDLY VOICE TRIGGER (High Contrast, Big Target)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (isListening) Color(0xFF082229) else Color(0xFF09161B)
                ),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(
                    width = 2.dp,
                    color = if (isListening) NcLime else if (isSpeaking) StrikeAmber else HydroCyan.copy(alpha = 0.6f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isListening) "LISTENING TO YOUR VOICE..."
                        else if (isSpeaking) "AUDIO COACH ADVISING..."
                        else "TAP TO SPEAK OR SHOUT A TRIGGER",
                        color = if (isListening) NcLime else if (isSpeaking) StrikeAmber else HydroCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Massive 104dp Wet-Hand Touch Target
                    Box(
                        modifier = Modifier
                            .size(104.dp)
                            .clip(CircleShape)
                            .background(
                                if (isListening) NcLime.copy(alpha = 0.25f)
                                else if (isSpeaking) StrikeAmber.copy(alpha = 0.25f)
                                else HydroCyan.copy(alpha = 0.15f)
                            )
                            .border(
                                width = 3.dp,
                                color = if (isListening) NcLime else if (isSpeaking) StrikeAmber else HydroCyan,
                                shape = CircleShape
                            )
                            .clickable {
                                if (isSpeaking) {
                                    onStopSpeaking()
                                } else {
                                    onStartHandsFreeListening()
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isListening) Icons.Default.GraphicEq
                            else if (isSpeaking) Icons.Default.VolumeUp
                            else Icons.Default.Mic,
                            contentDescription = "Voice Input",
                            tint = if (isListening) NcLime else if (isSpeaking) StrikeAmber else HydroCyan,
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = if (isListening) "Speak your fishing question now..."
                        else if (isSpeaking) "Audio instruction playing. Tap above to mute."
                        else "Hands wet or holding rod? Tap anywhere on this tile to activate speech recognition.",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 17.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    // Spoken or Recognized Feedback Banner
                    if (!lastRecognizedQuery.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = NcCardElevated,
                            border = androidx.compose.foundation.BorderStroke(1.dp, NcBorder)
                        ) {
                            Text(
                                text = "Heard: \"$lastRecognizedQuery\"",
                                color = HydroCyan,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    if (isSpeaking) {
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedButton(
                            onClick = onStopSpeaking,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = WarningRed),
                            border = androidx.compose.foundation.BorderStroke(1.dp, WarningRed),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Stop, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Silence Audio Coach", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 3. RETRIEVE CADENCE COACH (Metronome / Pulse Guidance)
        if (!activeCadenceCoach.isNullOrBlank()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF142416)),
                    shape = RoundedCornerShape(18.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, CatchGreen)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Speed, contentDescription = null, tint = CatchGreen, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "ACTIVE CADENCE COACH",
                                    color = CatchGreen,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp
                                )
                            }
                            Text(
                                text = "RHYTHM PACER",
                                color = CatchGreen.copy(alpha = 0.8f),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = activeCadenceCoach,
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Maintain this rhythm for 10-15 casts before switching presentations.",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // 4. THE 7 CANONICAL FISHING VOICE INTENTS (One-Tap or Spoken)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "BUILT-IN VOICE COMMANDS",
                        color = NcLime,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Say these aloud or tap to hear tactical advice",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }

                if (onOpenVoiceSettings != null) {
                    IconButton(
                        onClick = onOpenVoiceSettings,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Tune, contentDescription = "Voice Settings", tint = TextSecondary, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }

        // Intent 1: "What are they biting?"
        item {
            CanonicalIntentCard(
                phrase = "\"What are they biting?\"",
                intentTitle = "Lure & Presentation Summary",
                description = "Summarizes the top recommended lure and depth for current weather & clarity.",
                accentColor = NcLime,
                onClick = { onVoiceCommand("What are they biting?") }
            )
        }

        // Intent 2: "Nothing is biting"
        item {
            CanonicalIntentCard(
                phrase = "\"Nothing is biting\"",
                intentTitle = "Tactical Bite Troubleshooter",
                description = "Diagnoses tough conditions: triggers cadence adjustments, downsized profile, or dead-sticking.",
                accentColor = WarningRed,
                onClick = { onVoiceCommand("Nothing is biting") }
            )
        }

        // Intent 3: "Change cadence"
        item {
            CanonicalIntentCard(
                phrase = "\"Change cadence\"",
                intentTitle = "Retrieve Speed & Rhythm",
                description = "Activates retrieve rhythm guidance: pause timing, twitch patterns, and stop-and-go speed.",
                accentColor = StrikeAmber,
                onClick = { onVoiceCommand("Change cadence") }
            )
        }

        // Intent 4: "Fish on!"
        item {
            CanonicalIntentCard(
                phrase = "\"Fish on!\"",
                intentTitle = "Instant Catch Logger",
                description = "Instantly records a catch with current water, species, barometer, and temperature pre-filled.",
                accentColor = CatchGreen,
                onClick = {
                    onVoiceCommand("Fish on!")
                    onLogCatchClick()
                }
            )
        }

        // Intent 5: "Check conditions"
        item {
            CanonicalIntentCard(
                phrase = "\"Check conditions\"",
                intentTitle = "Environmental & Solunar Briefing",
                description = "Reads out current temperature, wind, barometric trend, and active solunar feeding windows.",
                accentColor = HydroCyan,
                onClick = { onVoiceCommand("Check conditions") }
            )
        }

        // Intent 6: "What's in my tackle?"
        item {
            CanonicalIntentCard(
                phrase = "\"What's in my tackle?\"",
                intentTitle = "Personal Tackle Box Matcher",
                description = "Scans your inventory ($tackleBoxCount items) and announces your best matching owned gear.",
                accentColor = Color(0xFFA855F7),
                onClick = { onVoiceCommand("What's in my tackle?") }
            )
        }

        // Intent 7: "What's the backup?"
        item {
            CanonicalIntentCard(
                phrase = "\"What's the backup?\"",
                intentTitle = "Plan B / Plan C Alternative",
                description = "Delivers fallback finesse presentation if the primary aggressive bite has shut down.",
                accentColor = Color(0xFFF97316),
                onClick = { onVoiceCommand("What's the backup?") }
            )
        }
    }
}

@Composable
private fun CanonicalIntentCard(
    phrase: String,
    intentTitle: String,
    description: String,
    accentColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NcCardElevated),
        border = androidx.compose.foundation.BorderStroke(1.dp, NcBorder)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = phrase,
                        color = accentColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = accentColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "TRIGGER",
                            color = accentColor,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = intentTitle,
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Surface(
                shape = CircleShape,
                color = accentColor.copy(alpha = 0.2f),
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Speak Intent",
                        tint = accentColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
