package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.theme.*

/**
 * FishINtuition On the Bank Screen (§4.3)
 *
 * Real-time fishing companion used while an angler is actively at the water.
 * Bridges pre-trip planning and active fishing:
 * - Location selection (GPS, catalog selector, recent/Recon waters).
 * - Real-time local conditions (Temp, Wind, Pressure, Sky).
 * - In-situ Angler Water Clarity Observation selector with explicit disclaimer.
 * - Active session status & instant "Fish on!" logger.
 * - Direct launcher for Hands-Free AI Assistant.
 * - Real-time tactical troubleshooting for tough bites.
 */
@Composable
fun OnTheBankScreen(
    waterBody: WaterBody,
    species: FishSpecies,
    conditions: EnvironmentalConditions,
    recommendation: FishingRecommendation?,
    tackleBoxCount: Int = 0,
    isSpeaking: Boolean,
    isListening: Boolean,
    activeCadenceCoach: String?,
    lastRecognizedQuery: String?,
    lastSpokenResponse: String?,
    adaptations: List<TacticalAdaptation>,
    onVoiceCommand: (String) -> Unit,
    onStartHandsFreeListening: () -> Unit,
    onStopSpeaking: () -> Unit,
    onNavigateToGamePlan: () -> Unit = {},
    onLogCatchClick: () -> Unit = {},
    onOpenHandsFreeAi: () -> Unit = {},
    allWaterBodies: List<WaterBody> = emptyList(),
    onSelectWaterBody: (WaterBody) -> Unit = {},
    onUpdateClarity: (WaterClarity) -> Unit = {},
    customTriggers: List<CustomVoiceTrigger> = DefaultVoiceTriggers.createDefaults(),
    onOpenVoiceSettings: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var expandedProblemId by remember { mutableStateOf<String?>(null) }
    var showWaterSelectorDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // =============================================================
        // 1. ACTIVE BANK SESSION & LOCATION SELECTOR CARD (§4.3)
        // =============================================================
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NcCard),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, NcLime.copy(alpha = 0.6f))
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
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(CatchGreen)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "ACTIVE FISHING SESSION",
                                color = CatchGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                        }

                        // Water selector action
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = NcCardElevated,
                            border = androidx.compose.foundation.BorderStroke(1.dp, NcBorderSubtle),
                            modifier = Modifier.clickable { showWaterSelectorDialog = true }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Place, contentDescription = null, tint = HydroCyan, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Switch Water",
                                    color = HydroCyan,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = waterBody.name,
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "${waterBody.location} • ${waterBody.category.label} • ${waterBody.accessStatus.label}",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = NcBorderSubtle)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Target Species & Tied Lure
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "TARGET SPECIES & TIED LURE",
                                color = TextMuted,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${species.commonName} • ${recommendation?.primaryLureName ?: "Squarebill Crankbait"}",
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // 1-Tap "Fish on!" Quick Logger
                        Button(
                            onClick = onLogCatchClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CatchGreen,
                                contentColor = MarineDeep
                            ),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Fish on!", fontSize = 12.sp, fontWeight = FontWeight.Black)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Strategy verification note
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (recommendation?.isPrimaryLureOwnedInTackleBox == true) "✓ Matching tackle in your box ready to throw"
                            else "Plan A presentation configured",
                            color = if (recommendation?.isPrimaryLureOwnedInTackleBox == true) CatchGreen else HydroCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        TextButton(
                            onClick = onNavigateToGamePlan,
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp)
                        ) {
                            Text("Full Plan", color = HydroCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(2.dp))
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = HydroCyan, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }
        }

        // =============================================================
        // 2. WEATHER & ENVIRONMENTAL OBSERVATIONS CARD (§4.3)
        // =============================================================
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NcCard),
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NcBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Cloud, contentDescription = null, tint = HydroCyan, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "LOCAL BANK WEATHER & TELEMETRY",
                                color = HydroCyan,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color.White.copy(alpha = 0.05f)
                        ) {
                            Text(
                                text = "Live Conditions",
                                color = TextMuted,
                                fontSize = 9.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = NcCardElevated,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("AIR & WATER TEMP", color = TextMuted, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("${conditions.waterTempF}°F water", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text("${conditions.airTempF}°F air", color = TextSecondary, fontSize = 10.sp)
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = NcCardElevated,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("WIND & SKY", color = TextMuted, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("${conditions.windSpeedMph} mph", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text(conditions.skyCover, color = TextSecondary, fontSize = 10.sp)
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = NcCardElevated,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("BAROMETER", color = TextMuted, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("${conditions.barometerInHg} inHg", color = StrikeAmber, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text(conditions.pressureTrend.label, color = StrikeAmber.copy(alpha = 0.8f), fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }

        // =============================================================
        // 3. ANGLER-ENTERED WATER CLARITY OBSERVATION CARD (§4.3)
        // Explicitly communicates that weather data cannot determine water clarity
        // =============================================================
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF071920)),
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, StrikeAmber.copy(alpha = 0.6f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Visibility, contentDescription = null, tint = StrikeAmber, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "WATER CLARITY OBSERVATION",
                            color = StrikeAmber,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Explicit clarity provenance disclaimer per specification
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = StrikeAmber.copy(alpha = 0.1f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StrikeAmber.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = StrikeAmber, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Water clarity is an angler-entered observation. Weather data cannot determine water clarity.",
                                color = StrikeAmber,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                lineHeight = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Current Bank Clarity: ${conditions.waterClarity.label}",
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Clarity Selectors: Clear, Stained, Muddy, Highly Turbid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            WaterClarity.CRYSTAL_CLEAR,
                            WaterClarity.STAINED,
                            WaterClarity.MURKY,
                            WaterClarity.MUDDY
                        ).forEach { clarityOption ->
                            val isSelected = conditions.waterClarity == clarityOption
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) StrikeAmber else NcCardElevated,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) StrikeAmber else NcBorder
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onUpdateClarity(clarityOption) }
                            ) {
                                Column(
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = when (clarityOption) {
                                            WaterClarity.CRYSTAL_CLEAR -> "Clear"
                                            WaterClarity.STAINED -> "Stained"
                                            WaterClarity.MURKY -> "Murky"
                                            WaterClarity.MUDDY -> "Muddy"
                                        },
                                        color = if (isSelected) NcBg else TextPrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = when (clarityOption) {
                                            WaterClarity.CRYSTAL_CLEAR -> "> 6 ft"
                                            WaterClarity.STAINED -> "2-4 ft"
                                            WaterClarity.MURKY -> "1-2 ft"
                                            WaterClarity.MUDDY -> "< 1 ft"
                                        },
                                        color = if (isSelected) NcBg.copy(alpha = 0.8f) else TextMuted,
                                        fontSize = 9.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // =============================================================
        // 4. PROMINENT HANDS-FREE AI ASSISTANT HERO CONNECTION (§6)
        // High-contrast outdoor launcher preserving full session context
        // =============================================================
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenHandsFreeAi() },
                colors = CardDefaults.cardColors(
                    containerColor = if (isListening) Color(0xFF08272F) else Color(0xFF0A1F26)
                ),
                shape = RoundedCornerShape(22.dp),
                border = androidx.compose.foundation.BorderStroke(
                    2.dp,
                    if (isListening) NcLime else if (isSpeaking) StrikeAmber else HydroCyan
                )
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Big wet-hand friendly mic circle
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isListening) NcLime.copy(alpha = 0.25f)
                                    else if (isSpeaking) StrikeAmber.copy(alpha = 0.25f)
                                    else HydroCyan.copy(alpha = 0.2f)
                                )
                                .border(
                                    2.dp,
                                    if (isListening) NcLime else if (isSpeaking) StrikeAmber else HydroCyan,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isListening) Icons.Default.GraphicEq
                                else if (isSpeaking) Icons.Default.VolumeUp
                                else Icons.Default.Mic,
                                contentDescription = "Hands-Free Voice AI",
                                tint = if (isListening) NcLime else if (isSpeaking) StrikeAmber else HydroCyan,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "HANDS-FREE AI ASSISTANT",
                                    color = if (isSpeaking) StrikeAmber else HydroCyan,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(if (isListening) NcLime else CatchGreen)
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Open Voice Command HUD",
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Wet-hands audio assistant • 7 canonical fishing intents",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = HydroCyan,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        // =============================================================
        // 5. ACTIVE RETRIEVE CADENCE COACH METRONOME (if active)
        // =============================================================
        if (activeCadenceCoach != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MarineSurfaceVariant),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, StrikeAmber)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.SportsScore,
                                    contentDescription = null,
                                    tint = StrikeAmber,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "AUDIO RETRIEVE COACH ACTIVE",
                                    color = StrikeAmber,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp
                                )
                            }
                            IconButton(
                                onClick = { onVoiceCommand("Change cadence") },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = "New cadence", tint = StrikeAmber, modifier = Modifier.size(18.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = activeCadenceCoach,
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(5) { index ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(
                                            if (index == 2) StrikeAmber else MarineOutline
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }

        // =============================================================
        // 6. REAL-TIME TACTICAL ADJUSTMENTS: "FISH AREN'T BITING?"
        // =============================================================
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = null,
                        tint = HydroCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "FISH AREN'T BITING? TACTICAL ADVISOR",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Observe something on the water? Tap to diagnose and adapt:",
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }
        }

        items(adaptations) { adaptation ->
            val isExpanded = expandedProblemId == adaptation.id

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        expandedProblemId = if (isExpanded) null else adaptation.id
                    },
                colors = CardDefaults.cardColors(containerColor = MarineSurface),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isExpanded) HydroCyan else MarineOutline
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.HelpOutline,
                                contentDescription = null,
                                tint = if (isExpanded) HydroCyan else StrikeAmber,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = adaptation.triggerProblem,
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    if (isExpanded) {
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = MarineOutlineSubtle)
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "DIAGNOSIS",
                            color = TextSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = adaptation.quickDiagnosis,
                            color = TextPrimary,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "IMMEDIATE ADJUSTMENT",
                            color = StrikeAmber,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = adaptation.immediateAdjustment,
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "RECOMMENDED LURE SWAP",
                            color = HydroCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = adaptation.recommendedLureSwap,
                            color = TextPrimary,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                onVoiceCommand(adaptation.triggerProblem)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MarineSurfaceVariant,
                                contentColor = HydroCyan
                            ),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, HydroCyanDim)
                        ) {
                            Icon(Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Hear Audio Coach for this problem", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // Water Body Switcher Modal Dialog
    if (showWaterSelectorDialog) {
        AlertDialog(
            onDismissRequest = { showWaterSelectorDialog = false },
            title = {
                Text("Select Active Water Body", color = TextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth().heightIn(max = 350.dp)
                ) {
                    items(allWaterBodies) { water ->
                        val isSelected = water.id == waterBody.id
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) NcCardElevated else Color(0xFF09161B),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) NcLime else NcBorderSubtle
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectWaterBody(water)
                                    showWaterSelectorDialog = false
                                }
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = water.name,
                                    color = if (isSelected) NcLime else TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "${water.location} • ${water.accessStatus.label}",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showWaterSelectorDialog = false }) {
                    Text("Close", color = HydroCyan)
                }
            },
            containerColor = NcCard,
            tonalElevation = 6.dp
        )
    }
}
