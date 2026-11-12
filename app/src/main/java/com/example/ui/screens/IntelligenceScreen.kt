package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
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
import com.example.ui.components.SolunarFeedingCard
import com.example.ui.theme.*

enum class IntelligenceMode {
    ALL_ANGLERS,
    BEGINNER_QUICK_START,
    TACTICAL_SCIENCE
}

@Composable
fun IntelligenceScreen(
    species: FishSpecies,
    conditions: EnvironmentalConditions,
    recommendation: FishingRecommendation?,
    onSpeciesClick: () -> Unit,
    onConditionsClick: () -> Unit,
    onAskAudioCoach: (String) -> Unit,
    onNavigateToTackleBox: () -> Unit,
    onSelectSpecies: ((FishSpecies) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var selectedMode by remember { mutableStateOf(IntelligenceMode.ALL_ANGLERS) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Quick Target Species Switcher
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MarineSurface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MarineOutline)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "TARGET SPECIES",
                                color = TextMuted,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = species.commonName,
                                    color = TextPrimary,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = HydroCyan.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = species.category,
                                        color = HydroCyan,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        OutlinedButton(
                            onClick = onSpeciesClick,
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = HydroCyan),
                            border = androidx.compose.foundation.BorderStroke(1.dp, HydroCyan.copy(alpha = 0.5f))
                        ) {
                            Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("All Species", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Horizontal Quick-Pill Species Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SpeciesCatalog.allSpecies.forEach { sp ->
                            val isSelected = sp.id == species.id
                            FilterChip(
                                selected = isSelected,
                                onClick = { onSelectSpecies?.invoke(sp) ?: onSpeciesClick() },
                                label = {
                                    Text(
                                        text = sp.commonName,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = HydroCyan,
                                    selectedLabelColor = MarineDeep,
                                    containerColor = MarineSurfaceVariant,
                                    labelColor = TextSecondary
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isSelected,
                                    borderColor = if (isSelected) HydroCyan else MarineOutlineSubtle,
                                    borderWidth = 1.dp
                                )
                            )
                        }
                    }
                }
            }
        }

        // Environmental Conditions & Solunar Snapshot
        item {
            SolunarFeedingCard(conditions = conditions)
        }

        // Quick Mode Switcher: All Anglers / Beginner Quick Start / Tactical Science
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MarineSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, MarineOutlineSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    ModeTabItem(
                        title = "All Guidance",
                        icon = Icons.Default.CheckCircle,
                        selected = selectedMode == IntelligenceMode.ALL_ANGLERS,
                        onClick = { selectedMode = IntelligenceMode.ALL_ANGLERS },
                        modifier = Modifier.weight(1f)
                    )
                    ModeTabItem(
                        title = "Beginner Quick",
                        icon = Icons.Default.Lightbulb,
                        selected = selectedMode == IntelligenceMode.BEGINNER_QUICK_START,
                        onClick = { selectedMode = IntelligenceMode.BEGINNER_QUICK_START },
                        modifier = Modifier.weight(1.1f)
                    )
                    ModeTabItem(
                        title = "Tactical Science",
                        icon = Icons.Default.Biotech,
                        selected = selectedMode == IntelligenceMode.TACTICAL_SCIENCE,
                        onClick = { selectedMode = IntelligenceMode.TACTICAL_SCIENCE },
                        modifier = Modifier.weight(1.1f)
                    )
                }
            }
        }

        if (recommendation != null) {
            // -------------------------------------------------------------
            // 1. BEGINNER DIRECT GUIDANCE: "JUST TELL ME WHAT TO THROW"
            // Visible in ALL_ANGLERS and BEGINNER_QUICK_START modes
            // -------------------------------------------------------------
            if (selectedMode == IntelligenceMode.ALL_ANGLERS || selectedMode == IntelligenceMode.BEGINNER_QUICK_START) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MarineSurface),
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(2.dp, CatchGreen)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            // Header Badge
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = CatchGreen.copy(alpha = 0.18f),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, CatchGreen)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Lightbulb,
                                            contentDescription = null,
                                            tint = CatchGreen,
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "BEGINNER QUICK START: WHAT TO THROW",
                                            color = CatchGreen,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Black,
                                            letterSpacing = 0.5.sp
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = StrikeAmber.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "High Confidence",
                                        color = StrikeAmber,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Simple Plain-English Headline
                            Text(
                                text = recommendation.beginnerGuidance.simpleHeadline,
                                color = TextPrimary,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                lineHeight = 26.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Confidence Rating Pill
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MarineSurfaceVariant,
                                border = androidx.compose.foundation.BorderStroke(1.dp, MarineOutline)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = CatchGreen,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = recommendation.beginnerGuidance.beginnerConfidenceRating,
                                        color = TextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = MarineOutlineSubtle)
                            Spacer(modifier = Modifier.height(14.dp))

                            // Where to Cast
                            Row(verticalAlignment = Alignment.Top) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(HydroCyan.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.NearMe,
                                        contentDescription = null,
                                        tint = HydroCyan,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "WHERE TO CAST",
                                        color = HydroCyan,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 1.sp
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = recommendation.beginnerGuidance.castWhereTip,
                                        color = TextPrimary,
                                        fontSize = 13.sp,
                                        lineHeight = 19.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // How to Work It
                            Row(verticalAlignment = Alignment.Top) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(StrikeAmber.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Timeline,
                                        contentDescription = null,
                                        tint = StrikeAmber,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "HOW TO WORK IT (STEP-BY-STEP)",
                                        color = StrikeAmber,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 1.sp
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = recommendation.beginnerGuidance.howToWorkIt,
                                        color = TextPrimary,
                                        fontSize = 13.sp,
                                        lineHeight = 20.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Hands-free Voice Coach Readout Button
                            Button(
                                onClick = { onAskAudioCoach("What are they biting?") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CatchGreen,
                                    contentColor = MarineDeep
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Read Beginner Steps Aloud",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }

            // -------------------------------------------------------------
            // 2. PINPOINT LURE RECOMMENDATION & SPECS
            // -------------------------------------------------------------
            if (selectedMode == IntelligenceMode.ALL_ANGLERS || selectedMode == IntelligenceMode.TACTICAL_SCIENCE) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MarineSurface),
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, HydroCyan)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = HydroCyan.copy(alpha = 0.15f),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, HydroCyan.copy(alpha = 0.6f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.GpsFixed,
                                            contentDescription = null,
                                            tint = HydroCyan,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "PINPOINT MATCH",
                                            color = HydroCyan,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Black,
                                            letterSpacing = 1.sp
                                        )
                                    }
                                }

                                Text(
                                    text = "${recommendation.matchScorePercent}% MATCH",
                                    color = CatchGreen,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            if (recommendation.isPrimaryLureOwnedInTackleBox) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = CatchGreen.copy(alpha = 0.18f),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, CatchGreen)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = CatchGreen,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "OWNED IN YOUR TACKLEBOX • READY TO TIE ON",
                                            color = CatchGreen,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Black,
                                            letterSpacing = 0.5.sp
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            Text(
                                text = recommendation.primaryLureName,
                                color = TextPrimary,
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Black,
                                lineHeight = 27.sp
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MarineSurfaceVariant
                                ) {
                                    Text(
                                        text = recommendation.primaryLureType,
                                        color = TextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MarineSurfaceVariant
                                ) {
                                    Text(
                                        text = "Weight: ${recommendation.primaryLureWeight}",
                                        color = TextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            HorizontalDivider(color = MarineOutlineSubtle)
                            Spacer(modifier = Modifier.height(14.dp))

                            // Retrieval Cadence
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(
                                    imageVector = Icons.Default.Timeline,
                                    contentDescription = null,
                                    tint = StrikeAmber,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .padding(top = 2.dp)
                                    )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "RETRIEVAL CADENCE & SPEED",
                                        color = StrikeAmber,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = recommendation.retrieveCadence,
                                        color = TextPrimary,
                                        fontSize = 13.sp,
                                        lineHeight = 19.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Depth & Structure
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(
                                    imageVector = Icons.Default.Waves,
                                    contentDescription = null,
                                    tint = DeepWaterBlue,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .padding(top = 2.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "TARGET DEPTH & HABITAT",
                                        color = DeepWaterBlue,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = recommendation.targetDepthRange,
                                        color = TextPrimary,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Key Structures: " + recommendation.keyStructures.joinToString(", "),
                                        color = TextSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Plan B / Alternate Lure
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MarineSurfaceVariant,
                                border = androidx.compose.foundation.BorderStroke(1.dp, MarineOutlineSubtle),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.SwapHoriz,
                                        contentDescription = null,
                                        tint = StrikeAmber,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "BACKUP PLAN B (IF THEY WON'T BITE):",
                                            color = StrikeAmber,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 0.5.sp
                                        )
                                        Text(
                                            text = "${recommendation.alternateLureName} (${recommendation.alternateLureType})",
                                            color = TextPrimary,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // -------------------------------------------------------------
            // STRATEGIC FORMATIONS: PLAN A, PLAN B, PLAN C (§4 of Bible v2.1)
            // -------------------------------------------------------------
            if (recommendation.tacticalPlans.isNotEmpty() && (selectedMode == IntelligenceMode.ALL_ANGLERS || selectedMode == IntelligenceMode.TACTICAL_SCIENCE)) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MarineSurface),
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
                                    Icon(
                                        imageVector = Icons.Default.ViewAgenda,
                                        contentDescription = null,
                                        tint = StrikeAmber,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "STRATEGIC FORMATIONS",
                                            color = StrikeAmber,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Black,
                                            letterSpacing = 1.sp
                                        )
                                        Text(
                                            text = "Plan A, Plan B & Emergency Plan C",
                                            color = TextPrimary,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MarineSurfaceVariant
                                ) {
                                    Text(
                                        text = "3-Tier Adaptive",
                                        color = TextSecondary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            recommendation.tacticalPlans.forEach { plan ->
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MarineSurfaceVariant,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (plan.planLetter == "A") NcLime.copy(alpha = 0.5f)
                                        else if (plan.planLetter == "B") HydroCyan.copy(alpha = 0.5f)
                                        else StrikeAmber.copy(alpha = 0.5f)
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(24.dp)
                                                        .clip(CircleShape)
                                                        .background(
                                                            if (plan.planLetter == "A") NcLime
                                                            else if (plan.planLetter == "B") HydroCyan
                                                            else StrikeAmber
                                                        ),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = plan.planLetter,
                                                        color = NcBg,
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Black
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = plan.title,
                                                    color = TextPrimary,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }

                                            if (plan.isOwnedInBox) {
                                                Surface(
                                                    shape = RoundedCornerShape(4.dp),
                                                    color = CatchGreen.copy(alpha = 0.15f)
                                                ) {
                                                    Text(
                                                        text = "IN BOX",
                                                        color = CatchGreen,
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Black,
                                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = "Lure: ${plan.lureName} (${plan.lureType})",
                                            color = TextPrimary,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )

                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(
                                            text = "When: ${plan.conditionTrigger}",
                                            color = TextSecondary,
                                            fontSize = 11.sp,
                                            lineHeight = 15.sp
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Cadence: ${plan.retrieveCadence}",
                                            color = TextMuted,
                                            fontSize = 11.sp,
                                            lineHeight = 15.sp
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }

            // -------------------------------------------------------------
            // 3. MATCHED FROM DIGITAL TACKLE BOX
            // -------------------------------------------------------------
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MarineSurface),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CatchGreen.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.WorkOutline,
                                    contentDescription = null,
                                    tint = CatchGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "FROM YOUR DIGITAL TACKLE BOX",
                                    color = CatchGreen,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            }
                            TextButton(onClick = onNavigateToTackleBox) {
                                Text(
                                    text = "View Box (${recommendation.matchedTackleFromBox.size} Matches)",
                                    color = HydroCyan,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (recommendation.matchedTackleFromBox.isNotEmpty()) {
                            recommendation.matchedTackleFromBox.take(3).forEach { item ->
                                TackleMatchItemRow(item = item)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        } else {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MarineSurfaceVariant,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "No exact gear match currently logged in your tackle box.",
                                        color = TextSecondary,
                                        fontSize = 12.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Add '${recommendation.primaryLureName}' or '${recommendation.alternateLureName}' to your box so NovaCast can match your inventory in the field.",
                                        color = HydroCyan,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // -------------------------------------------------------------
            // 4. COLOR SELECTION & OPTICAL RATIONALE
            // -------------------------------------------------------------
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MarineSurface),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MarineOutline)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Palette,
                                contentDescription = null,
                                tint = HydroCyan,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "COLOR SELECTION & OPTICAL RATIONALE",
                                color = TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Primary Color
                        Text(
                            text = "Primary Pattern: ${recommendation.primaryColors.name}",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            recommendation.primaryColors.colorHexes.forEach { hex ->
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(Color(hex))
                                        .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = recommendation.primaryColors.rationale,
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = MarineOutlineSubtle)
                        Spacer(modifier = Modifier.height(14.dp))

                        // Secondary Color
                        Text(
                            text = "Alternate Pattern: ${recommendation.secondaryColors.name}",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            recommendation.secondaryColors.colorHexes.forEach { hex ->
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(Color(hex))
                                        .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = recommendation.secondaryColors.rationale,
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    }
                }
            }

            // -------------------------------------------------------------
            // 5. THE SCIENCE: WHY THIS WORKS (PREDATORY BIOLOGY & PHYSICS)
            // -------------------------------------------------------------
            if (selectedMode == IntelligenceMode.ALL_ANGLERS || selectedMode == IntelligenceMode.TACTICAL_SCIENCE) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MarineSurface),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StrikeAmber.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Biotech,
                                    contentDescription = null,
                                    tint = StrikeAmber,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "THE SCIENCE: WHY THIS WORKS",
                                    color = StrikeAmber,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = recommendation.scientificExplanation,
                                color = TextPrimary,
                                fontSize = 13.sp,
                                lineHeight = 19.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MarineSurfaceVariant,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = HydroCyan,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "${species.commonName} Biology Fact",
                                            color = HydroCyan,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = recommendation.biologicalPredatorFact,
                                            color = TextSecondary,
                                            fontSize = 11.sp,
                                            lineHeight = 16.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // -------------------------------------------------------------
            // 6. RIGGING & GEAR SPECS
            // -------------------------------------------------------------
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MarineSurface),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MarineOutline)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Build,
                                contentDescription = null,
                                tint = TextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "RECOMMENDED RIGGING & TACKLE SETUP",
                                color = TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        RiggingRow("Rod", recommendation.riggingSpecs.rod)
                        RiggingRow("Reel", recommendation.riggingSpecs.reel)
                        RiggingRow("Line", recommendation.riggingSpecs.line)
                        RiggingRow("Hook/Terminal", recommendation.riggingSpecs.hookOrTerminal)
                    }
                }
            }
        }
    }
}

@Composable
private fun ModeTabItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (selected) HydroCyan else Color.Transparent,
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) MarineDeep else TextSecondary,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = title,
                color = if (selected) MarineDeep else TextSecondary,
                fontSize = 11.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun TackleMatchItemRow(item: TackleItem) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MarineSurfaceVariant,
        border = androidx.compose.foundation.BorderStroke(1.dp, MarineOutline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(CatchGreen.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = CatchGreen,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = item.name,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = item.weightOz,
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${item.brand} • ${item.color} • ${item.category}",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun RiggingRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "$label: ",
            color = HydroCyan,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(100.dp)
        )
        Text(
            text = value,
            color = TextPrimary,
            fontSize = 12.sp,
            lineHeight = 16.sp
        )
    }
}
