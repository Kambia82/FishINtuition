package com.example.data.model

data class ColorRecommendation(
    val name: String,
    val colorHexes: List<Long>,
    val rationale: String
)

data class RiggingSpecs(
    val rod: String,
    val reel: String,
    val line: String,
    val hookOrTerminal: String
)

data class BeginnerDirectGuidance(
    val simpleHeadline: String,       // e.g. "Tie on a 5\" Green Pumpkin Senko with a #3/0 hook"
    val castWhereTip: String,         // e.g. "Cast into the shaded corner next to that dock or fallen tree."
    val howToWorkIt: String,          // e.g. "Cast out. Don't touch the reel for 10 seconds. Watch your line for a twitch. Slowly lift your rod tip."
    val beginnerConfidenceRating: String // "Easiest to catch fish with / 95% Confidence"
)

data class TacticalPlan(
    val planLetter: String, // "A", "B", "C"
    val title: String,
    val lureName: String,
    val lureType: String,
    val lureWeight: String,
    val retrieveCadence: String,
    val targetDepth: String,
    val conditionTrigger: String,
    val isOwnedInBox: Boolean = false,
    val ownedItemName: String? = null
)

data class FishingRecommendation(
    val targetSpecies: FishSpecies,
    val primaryLureName: String,
    val primaryLureType: String,
    val primaryLureWeight: String,
    val alternateLureName: String,
    val alternateLureType: String,
    val primaryColors: ColorRecommendation,
    val secondaryColors: ColorRecommendation,
    val retrieveCadence: String,
    val targetDepthRange: String,
    val keyStructures: List<String>,
    val scientificExplanation: String,
    val biologicalPredatorFact: String,
    val riggingSpecs: RiggingSpecs,
    val matchScorePercent: Int,
    val matchedTackleFromBox: List<TackleItem> = emptyList(),
    val beginnerGuidance: BeginnerDirectGuidance,
    val tacticalPlans: List<TacticalPlan> = emptyList(),
    val isPrimaryLureOwnedInTackleBox: Boolean = false,
    val ownedLureName: String? = null,
    val dataProvenance: String = "Multi-Variable Biological & Meteorological Matrix"
)

data class TacticalAdaptation(
    val id: String,
    val triggerProblem: String,
    val quickDiagnosis: String,
    val immediateAdjustment: String,
    val recommendedLureSwap: String,
    val rationale: String,
    val voiceInstruction: String
)
