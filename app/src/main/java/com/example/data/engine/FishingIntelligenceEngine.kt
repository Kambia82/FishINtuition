package com.example.data.engine

import com.example.data.model.*
import java.util.Locale

class FishingIntelligenceEngine {

    fun generateRecommendation(
        species: FishSpecies,
        waterBody: WaterBody,
        conditions: EnvironmentalConditions,
        userTackleBox: List<TackleItem>
    ): FishingRecommendation {
        val waterTemp = conditions.waterTempF
        val isOptimalTemp = waterTemp in species.minOptimalTempF..species.maxOptimalTempF
        val isColdWater = waterTemp < species.minOptimalTempF
        val isHotWater = waterTemp > species.maxOptimalTempF

        val isFallingPressure = conditions.pressureTrend == PressureTrend.FALLING_PREFRONTAL
        val isRisingPressure = conditions.pressureTrend == PressureTrend.RISING_POSTFRONTAL

        // Determine primary presentation based on species feeding habit and environmental variables
        val (primaryLure, altLure, primaryType, altType, weight) = when (species.id) {
            "largemouth_bass" -> when {
                isFallingPressure && conditions.waterClarity != WaterClarity.CRYSTAL_CLEAR ->
                    Tuple5("ChatterBait Bladed Jig with Trailer", "Squarebill Crankbait", "Bladed Swim Jig", "Shallow Crankbait", "3/8 oz")
                isRisingPressure || isColdWater ->
                    Tuple5("Finesse Jig with Craw Trailer", "Texas-Rigged Stick Worm (Senko)", "Substrate Jig", "Finesse Worm", "1/4 oz - 3/8 oz")
                conditions.timeOfDay == TimeOfDay.DAWN || conditions.timeOfDay == TimeOfDay.DUSK ->
                    Tuple5("Hollow Body Walking Frog", "Buzzbait with Clacker", "Topwater Surface", "Surface Wire", "1/2 oz")
                conditions.waterClarity == WaterClarity.CRYSTAL_CLEAR ->
                    Tuple5("Drop Shot Rig with Straight Tail Worm", "Deep Suspending Jerkbait", "Finesse Plastic", "Minnow Plug", "3/16 oz")
                else ->
                    Tuple5("Texas Rigged Ribbon Tail Worm", "Strike King Squarebill 1.5", "Soft Plastic", "Deflection Crank", "5/16 oz")
            }
            "smallmouth_bass" -> when {
                isRisingPressure || isColdWater ->
                    Tuple5("Ned Rig with Finesse TRD", "Compact Hair Jig", "Standup Mushroom Rig", "Marabou Jig", "1/10 oz")
                isFallingPressure ->
                    Tuple5("Megabass Vision Oneten Jerkbait", "Compact Spinnerbait with Double Willow", "Suspending Jerkbait", "Wire Blade", "3/8 oz")
                else ->
                    Tuple5("Keitech Fat Swing Impact 3.8\" on Jighead", "Tube Jig dragged across rocks", "Swimbait", "Tube Craw", "1/4 oz")
            }
            "walleye" -> when {
                conditions.waterClarity == WaterClarity.CRYSTAL_CLEAR && conditions.timeOfDay == TimeOfDay.MIDDAY ->
                    Tuple5("Rapala Jigging Rap in Deep Structure", "Slip Bobber with Live Leech", "Vertical Heavy Blade", "Finesse Bait", "5/8 oz")
                isFallingPressure || conditions.timeOfDay == TimeOfDay.DUSK || conditions.timeOfDay == TimeOfDay.NIGHT ->
                    Tuple5("Deep Diving Suspending Jerkbait (Ripstop)", "Paddle Tail on Swimbait Jig", "Minnow Plug", "Reaction Swimbait", "3/8 oz")
                else ->
                    Tuple5("Round Ball Jig tipped with Live Minnow", "Bottom Bouncer with Colorado Blade Harness", "Live Bait Jig", "Trolling Spinner", "1/4 oz")
            }
            "rainbow_trout", "brown_trout", "brook_trout" -> when {
                conditions.waterClarity == WaterClarity.CRYSTAL_CLEAR ->
                    Tuple5("Pheasant Tail Beadhead Nymph (Drift)", "Panther Martin Gold Blade Inline Spinner", "Micro Fly / Nymph", "Inline Spinner", "1/16 oz")
                conditions.waterClarity == WaterClarity.MURKY || isFallingPressure ->
                    Tuple5("Countdown Rapala Minnow (Brown Trout Pattern)", "Black Woolly Bugger Streamer", "Micro Crank", "Weighted Streamer", "1/8 oz")
                else ->
                    Tuple5("Rooster Tail Spinner with Hackle", "Small Kastmaster Spoon", "Inline Blade", "Reflective Spoon", "1/8 oz")
            }
            "northern_pike", "muskie" -> when {
                isFallingPressure ->
                    Tuple5("Double-Cowgirl Giant Bucktail Spinner", "Heavy Glider Jerkbait (Phantom / Suick)", "Heavy Inline Spinner", "Subsurface Glider", "1.5 oz - 2.5 oz")
                else ->
                    Tuple5("Daredevle Red & White Casting Spoon", "Large Paddle Tail Swimbait (6\")", "Flutter Spoon", "Heavy Soft Plastic", "1.0 oz")
            }
            "black_crappie" -> when {
                conditions.waterClarity == WaterClarity.MURKY ->
                    Tuple5("Micro Tube Jig (Electric Chicken)", "Road Runner Underspin with Willow Blade", "Micro Tube", "Finesse Underspin", "1/16 oz")
                else ->
                    Tuple5("Marabou Hair Jig (Black & Chartreuse)", "Live Minnow on Slip Float", "Feather Jig", "Live Bait Float", "1/32 oz")
            }
            "channel_catfish", "blue_catfish" -> {
                Tuple5("Santee Cooper Rig with Fresh Cut Shad", "Slip Sinker Carolina Rig with Scented Dough", "Bottom Drift Rig", "Substrate Scent Rig", "1.5 oz - 2.5 oz Egg Sinker")
            }
            "redfish" -> when {
                conditions.waterClarity == WaterClarity.MURKY || conditions.waterClarity == WaterClarity.STAINED ->
                    Tuple5("Johnson Weedless Gold Spoon", "Popping Cork with Live Shrimp", "Weedless Spoon", "Surface Sound Rig", "1/2 oz")
                else ->
                    Tuple5("Z-Man DieZel MinnowZ on Trout Eye Jig", "Topwater Walk-the-Dog Plug", "Inshore Swimbait", "Surface Walker", "1/4 oz")
            }
            "snook" -> when {
                conditions.timeOfDay == TimeOfDay.NIGHT || conditions.timeOfDay == TimeOfDay.DUSK ->
                    Tuple5("Flair Hawk Big Eye Snook Jig", "MirrOlure Heavy Suspending Twitchbait", "Lead Head Snook Jig", "Twitchbait", "1/2 oz - 1.0 oz")
                else ->
                    Tuple5("White Paddle Tail Swimbait 4\"", "Topdog Surface Spook", "Inshore Swimbait", "Walk-the-Dog", "3/8 oz")
            }
            "striped_bass" -> when {
                isFallingPressure ->
                    Tuple5("White Bucktail Jig with Curly Grub", "5-Wire Umbrella A-Rig with Keitechs", "Heavy Hair Jig", "Schooling Umbrella", "3/4 oz")
                else ->
                    Tuple5("Deep Diving Cotton Cordell Red Fin", "Fluke Soft Jerkbait on Weighted Hook", "Wake Plug", "Weightless Soft Plastic", "1/2 oz")
            }
            else -> {
                // Generic universal predator adaptation
                Tuple5("Search Bait Paddle Tail Swimbait", "Weedless Finesse Jig", "Soft Swimbait", "Bottom Contact Jig", "3/8 oz")
            }
        }

        // Color intelligence selection based on water clarity and cloud cover
        val (primaryColor, secColor) = when (conditions.waterClarity) {
            WaterClarity.CRYSTAL_CLEAR -> {
                Pair(
                    ColorRecommendation(
                        name = "Translucent Ghost & Natural Minnow",
                        colorHexes = listOf(0xFFB0BEC5, 0xFFECEFF1, 0xFF90A4AE),
                        rationale = "Water visibility exceeds 5 feet. High transparency renders unnatural opaque paints suspicious. Translucent silver and smoky ghost bellies blend with ambient light."
                    ),
                    ColorRecommendation(
                        name = "Green Pumpkin & Watermelon Seed",
                        colorHexes = listOf(0xFF334B35, 0xFF283618, 0xFF4A5D23),
                        rationale = "Natural olive camouflage mimics crawfish and bottom darters without alerting hyper-vigilant sight feeders."
                    )
                )
            }
            WaterClarity.STAINED -> {
                Pair(
                    ColorRecommendation(
                        name = "Sexy Shad & Pearl White",
                        colorHexes = listOf(0xFFE2E8F0, 0xFF38BDF8, 0xFFEAB308),
                        rationale = "Stained green water filters out red spectra at 4 feet. Pearlescent white flanks with blue spine and yellow strike stripe flash maximum light displacement."
                    ),
                    ColorRecommendation(
                        name = "Firetiger / Chartreuse Perch",
                        colorHexes = listOf(0xFF84CC16, 0xFFF59E0B, 0xFFEF4444),
                        rationale = "High-contrast fluorescent stripes stimulate aggressive territorial strikes when ambient light dims."
                    )
                )
            }
            WaterClarity.MURKY -> {
                Pair(
                    ColorRecommendation(
                        name = "Black & Blue Contrast Silhouette",
                        colorHexes = listOf(0xFF0F172A, 0xFF1E3A8A, 0xFF0284C7),
                        rationale = "In murky water, light scattering prevents color identification beyond 18 inches. Solid black with royal blue flakes casts the sharpest silhouette against filtered sky."
                    ),
                    ColorRecommendation(
                        name = "Solid Chartreuse & White Glow",
                        colorHexes = listOf(0xFFA3E635, 0xFFF8FAFC, 0xFFFACC15),
                        rationale = "Fluorescent pigment wavelength penetrates turbid particulate matter best, giving trailing fish an unmistakable target."
                    )
                )
            }
            WaterClarity.MUDDY -> {
                Pair(
                    ColorRecommendation(
                        name = "Midnight Black & Dark Junebug",
                        colorHexes = listOf(0xFF020617, 0xFF3B0764, 0xFF1E1B4B),
                        rationale = "Zero ambient transparency. Solid black blocks all background light to create a hard edge. Fish lock on via lateral line sound and bite the black mass."
                    ),
                    ColorRecommendation(
                        name = "High-Visibility Fluorescent Orange / Chartreuse",
                        colorHexes = listOf(0xFFEA580C, 0xFFCA8A04, 0xFFE11D48),
                        rationale = "Warm spectrum flash visible at point-blank range (under 6 inches) when lure bumps into cover."
                    )
                )
            }
        }

        // Retrieval cadence intelligence
        val cadence = when {
            isFallingPressure ->
                "High-Tempo Reaction Burn & Pause: Cast past ambush points. Retrieve aggressively with 2 sharp rod twitches every 4 turns, followed by a momentary half-second stall to induce reflexive reaction strikes."
            isRisingPressure ->
                "Ultra-Slow Finesse Crawl & Dead-Stick: Bass and predators are glued to cover with pressurized swim bladders. Maintain bottom contact. Crawl 6 inches, then leave completely motionless for 5 to 8 full seconds. Most bites occur on the dead-stick."
            isColdWater ->
                "Subtle Gliding Twitch with Extended 10-Second Pauses: Metabolism is depressed. Fish will not expend calories chasing fast baits. Suspend bait directly in their strike zone."
            conditions.timeOfDay == TimeOfDay.DAWN || conditions.timeOfDay == TimeOfDay.DUSK ->
                "Steady Rhythmic Cadence along Weed Edges: Maintain a walking rhythmic cadence with steady surface commotion. Pause when crossing current seams or dock shadows."
            else ->
                "Medium Varied Cadence with Bottom Bumps: Deflect lure erratically off rocks, wood, or weed stalks. The sudden change in trajectory triggers instinctual predatory snaps."
        }

        // Targeted depth calculation
        val depthRange = when {
            conditions.timeOfDay == TimeOfDay.DAWN || conditions.timeOfDay == TimeOfDay.DUSK -> "1 to 4 feet (Shallow feeding flats & shoreline margins)"
            isRisingPressure && conditions.timeOfDay == TimeOfDay.MIDDAY -> "12 to 24 feet (Deep drop-offs, shaded bridge pilings, or punchable heavy canopy mats)"
            isFallingPressure -> "4 to 10 feet (Active transition zones, secondary points, and outside weedlines)"
            isColdWater -> "15 to 30 feet (Wintering basins, steep bluff walls, and deep structure humps)"
            else -> "5 to 12 feet (Primary structure contours, brush tops, and channel bends)"
        }

        // Scientific Explanation
        val scienceExplanation = buildString {
            append("Predatory Biology Analysis for ${species.commonName}: ")
            if (isFallingPressure) {
                append("The approaching barometric dip (${conditions.barometerInHg} inHg) decreases hydrostatic pressure on the fish's swim bladder, stimulating stomach enzymes and triggering an instinctive feeding binge before turbulent weather. ")
            } else if (isRisingPressure) {
                append("Post-frontal barometric high pressure (${conditions.barometerInHg} inHg) compresses internal swim bladders, creating physiological discomfort that drives ${species.commonName} deep into high-density shade cover, requiring tight-to-the-nose finesse presentations. ")
            } else {
                append("Stable barometric conditions (${conditions.barometerInHg} inHg) foster predictable territorial hunting patterns. ")
            }
            append("In ${conditions.waterClarity.label.lowercase()} at ${conditions.waterTempF}°F water temperature, ")
            append(species.clarityBehavior)
        }

        val biologicalFact = species.biologicalFact

        // Rigging Specs
        val rigging = when (species.waterType) {
            WaterType.COLDWATER_STREAM ->
                RiggingSpecs(
                    rod = "6'6\" Light to Ultra-Light Fast Action",
                    reel = "1000 - 2000 Spinning Reel (5.2:1 ratio)",
                    line = "4 lb - 6 lb 100% Fluorocarbon",
                    hookOrTerminal = "Micro snap or direct Palomar knot to 1/16 oz jig"
                )
            WaterType.SALTWATER, WaterType.BRACKISH ->
                RiggingSpecs(
                    rod = "7'2\" Medium-Heavy Inshore Fast Action",
                    reel = "3000 - 4000 Saltwater Sealed Spinning (6.2:1)",
                    line = "20 lb Braided Mainline with 25 lb Fluorocarbon Leader (FG Knot)",
                    hookOrTerminal = "3/0 Mustad UltraPoint Weedless Jig Head"
                )
            else ->
                RiggingSpecs(
                    rod = "7'0\" Medium-Heavy Fast Action Casting Rod",
                    reel = "7.3:1 High-Speed Baitcaster",
                    line = "15 lb - 17 lb Fluorocarbon (or 50 lb Braid for heavy grass)",
                    hookOrTerminal = "Owner 4/0 EWG Hook with 3/8 oz Tungsten Sinker"
                )
        }

        // Cross-reference user's tackle box and prioritize owned gear
        val matchedTackle = findMatchingTackle(species, conditions, userTackleBox, primaryType, altType)
        val ownedTopLure = matchedTackle.firstOrNull()
        val isOwned = ownedTopLure != null

        val matchScore = if (isOwned) 98 else 88

        // Plan A, Plan B, Plan C Strategy Formations (§4 of NovaCast Bible v2.1)
        val planA = TacticalPlan(
            planLetter = "A",
            title = "Primary High-Percentage Pattern",
            lureName = if (isOwned) "${ownedTopLure?.name} (${ownedTopLure?.brand})" else primaryLure,
            lureType = primaryType,
            lureWeight = weight,
            retrieveCadence = cadence,
            targetDepth = depthRange,
            conditionTrigger = "Deploy during active feeding windows and stable/falling pressure (${conditions.barometerInHg} inHg)",
            isOwnedInBox = isOwned,
            ownedItemName = ownedTopLure?.name
        )

        val planB = TacticalPlan(
            planLetter = "B",
            title = "Alternate Presentation (Finesse Shift)",
            lureName = altLure,
            lureType = altType,
            lureWeight = weight,
            retrieveCadence = "Slow cadence with 4-second bottom pauses along weed edges and structure transitions.",
            targetDepth = depthRange,
            conditionTrigger = "Switch when fish show hesitation, tail-nip, or refuse reaction baits",
            isOwnedInBox = matchedTackle.size > 1,
            ownedItemName = matchedTackle.getOrNull(1)?.name
        )

        val planC = TacticalPlan(
            planLetter = "C",
            title = "Post-Frontal Lockjaw Safety Pattern",
            lureName = "1/10 oz Ned Rig or Weightless Texas Senko",
            lureType = "Dead-Stick Substrate Finesse",
            lureWeight = "1/10 oz - 1/8 oz",
            retrieveCadence = "Dead-stick bottom crawl: 6-inch sweep, then 8 to 10 full seconds of complete stillness.",
            targetDepth = "Deepest shaded structure / base of drop-offs (12 to 24 ft)",
            conditionTrigger = "Mandatory emergency pivot if no strikes occur within 45 minutes or under bluebird skies",
            isOwnedInBox = matchedTackle.any { it.category.contains("Plastic", ignoreCase = true) || it.category.contains("Jig", ignoreCase = true) },
            ownedItemName = matchedTackle.find { it.category.contains("Plastic", ignoreCase = true) || it.category.contains("Jig", ignoreCase = true) }?.name
        )

        val tacticalPlans = listOf(planA, planB, planC)

        // Compute plain-English Beginner Direct Guidance
        val beginnerGuide = when (species.id) {
            "largemouth_bass" -> BeginnerDirectGuidance(
                simpleHeadline = if (isOwned) "Tie on your ${ownedTopLure?.name}" else "Tie on a 5\" Green Pumpkin Senko or Texas-Rig Worm",
                castWhereTip = "Aim at shaded corners: right along dock walkways, lily pad edges, or downed trees. Bass never sit in bare open water under direct sunlight.",
                howToWorkIt = "1. Cast out. 2. Don't touch the reel—count to 8 while it sinks. 3. Watch your line for a subtle jump or sideways twitch. 4. Slowly lift your rod tip, reel down, and repeat.",
                beginnerConfidenceRating = if (isOwned) "Owned In Tackle Box / Highest Confidence" else "Highest Confidence / Catches bass anywhere in America"
            )
            "smallmouth_bass" -> BeginnerDirectGuidance(
                simpleHeadline = if (isOwned) "Tie on your ${ownedTopLure?.name}" else "Tie on a 1/10 oz Ned Rig with mushroom head",
                castWhereTip = "Cast onto rocky points, gravel banks, or current breaks behind large boulders.",
                howToWorkIt = "Drag the jig 6 inches across the bottom rocks, then PAUSE for 5 full seconds. The buoyant tail stands straight up off bottom. Smallies inhale it while it's sitting still.",
                beginnerConfidenceRating = "Easiest smallmouth bite / Fish hook themselves"
            )
            "black_crappie", "bluegill" -> BeginnerDirectGuidance(
                simpleHeadline = "Live Cricket or Worm under a small clip-on bobber (set 3 ft deep)",
                castWhereTip = "Cast 2 feet away from dock pilings, brush piles, or flooded willows.",
                howToWorkIt = "Let the bobber settle. If it doesn't plunge under in 30 seconds, reel 2 turns and pause. When the bobber disappears completely, lift your rod smoothly.",
                beginnerConfidenceRating = "Instant action / Guaranteed strikes for kids and beginners"
            )
            "channel_catfish" -> BeginnerDirectGuidance(
                simpleHeadline = "Fresh Chicken Liver on a #2 Circle Hook with 1/2 oz egg sinker",
                castWhereTip = "Cast into the deepest hole you can reach from the bank, especially where a creek feeds in.",
                howToWorkIt = "Cast out, reel in the slack, and set the rod in a bank holder or fork stick. Catfish bite by scent. When the rod bends double, DON'T hookset hard—just reel steadily.",
                beginnerConfidenceRating = "Zero casting effort / Proven bank producer"
            )
            "rainbow_trout", "brown_trout" -> BeginnerDirectGuidance(
                simpleHeadline = "Rooster Tail Inline Spinner (1/8 oz, Brown or Gold)",
                castWhereTip = "Cast 45 degrees upstream and let the current sweep the spinner past boulder shadows.",
                howToWorkIt = "Start reeling immediately at a medium-slow pace just fast enough to feel the blade thump in your rod handle. Trout strike out of pure reflex.",
                beginnerConfidenceRating = "Cast-and-reel simplicity / High hookup rate"
            )
            else -> BeginnerDirectGuidance(
                simpleHeadline = if (isOwned) "Tie on your ${ownedTopLure?.name}" else "Tie on a 3\" Paddle Tail Swimbait on a 1/4 oz jighead",
                castWhereTip = "Fan cast along the edge where dark water transitions to green shallows.",
                howToWorkIt = "Simple steady retrieve. Count 3 seconds after it hits the water, then turn the handle at a calm walking pace.",
                beginnerConfidenceRating = "Universal search bait / Beginner friendly"
            )
        }

        return FishingRecommendation(
            targetSpecies = species,
            primaryLureName = if (isOwned) "${ownedTopLure?.name} (${ownedTopLure?.brand})" else primaryLure,
            primaryLureType = primaryType,
            primaryLureWeight = weight,
            alternateLureName = altLure,
            alternateLureType = altType,
            primaryColors = primaryColor,
            secondaryColors = secColor,
            retrieveCadence = cadence,
            targetDepthRange = depthRange,
            keyStructures = species.bestStructureTypes.take(4),
            scientificExplanation = scienceExplanation,
            biologicalPredatorFact = biologicalFact,
            riggingSpecs = rigging,
            matchScorePercent = matchScore,
            matchedTackleFromBox = matchedTackle,
            beginnerGuidance = beginnerGuide,
            tacticalPlans = tacticalPlans,
            isPrimaryLureOwnedInTackleBox = isOwned,
            ownedLureName = ownedTopLure?.name,
            dataProvenance = "Multi-Variable Biological & Meteorological Matrix"
        )
    }

    private fun findMatchingTackle(
        species: FishSpecies,
        conditions: EnvironmentalConditions,
        box: List<TackleItem>,
        primaryClass: String,
        altClass: String
    ): List<TackleItem> {
        val speciesKeyword = species.commonName.lowercase(Locale.ROOT)

        return box.filter { item ->
            val nameAndCategory = "${item.name} ${item.category} ${item.presentationStyle} ${item.targetSpeciesSummary}".lowercase(Locale.ROOT)
            val matchesSpecies = nameAndCategory.contains(speciesKeyword) ||
                    item.targetSpeciesSummary.contains(species.commonName, ignoreCase = true) ||
                    (species.category.contains("Bass") && nameAndCategory.contains("bass")) ||
                    (species.category.contains("Trout") && nameAndCategory.contains("trout")) ||
                    (species.category.contains("Catfish") && nameAndCategory.contains("catfish"))

            val matchesClarity = item.bestWaterClarity.contains("Any", ignoreCase = true) ||
                    (conditions.waterClarity == WaterClarity.CRYSTAL_CLEAR && item.bestWaterClarity.contains("Clear", ignoreCase = true)) ||
                    (conditions.waterClarity == WaterClarity.STAINED && (item.bestWaterClarity.contains("Stained", ignoreCase = true) || item.bestWaterClarity.contains("Clear", ignoreCase = true))) ||
                    (conditions.waterClarity == WaterClarity.MURKY && (item.bestWaterClarity.contains("Murky", ignoreCase = true) || item.bestWaterClarity.contains("Stained", ignoreCase = true))) ||
                    (conditions.waterClarity == WaterClarity.MUDDY && item.bestWaterClarity.contains("Muddy", ignoreCase = true))

            matchesSpecies && matchesClarity
        }.sortedByDescending { it.isFavorite }
    }

    fun getTacticalAdaptations(): List<TacticalAdaptation> {
        return listOf(
            TacticalAdaptation(
                id = "no_bites_45min",
                triggerProblem = "Nothing is biting (Fished 45 min with zero strikes)",
                quickDiagnosis = "Fish are either suspended off structure due to a frontal shift, or your retrieve speed is mismatched with current water temperature metabolism.",
                immediateAdjustment = "Slow your retrieve speed by 50% immediately. Let your lure sit completely dead on the bottom for an 8-count before touching it.",
                recommendedLureSwap = "Downsize to a 1/10 oz Ned Rig or Weightless Wacky-Rigged Senko.",
                rationale = "When predators refuse reaction baits, a motionless finesse profile falling vertically right into their primary cone of vision triggers involuntary suction feeding.",
                voiceInstruction = "Retrieve coach: Nothing is biting. Slow your retrieve cadence by half immediately. Count four seconds between rod twitches and let the bait dead stick on the bottom. If still quiet, downsize to a Ned Rig or Senko from your box."
            ),
            TacticalAdaptation(
                id = "short_strikes",
                triggerProblem = "Getting short strikes / nibbles without hookups",
                quickDiagnosis = "Fish are curious and tail-nipping, but lack the aggression to engulf the entire lure profile.",
                immediateAdjustment = "Trim 1 inch off your soft plastic bait body to move the hook closer to the tail, or add a trailer hook to your spinnerbait/chatterbait.",
                recommendedLureSwap = "Switch to a smaller 3\" profile or add an unscented fluorocarbon stinger hook.",
                rationale = "Condensing the bait mass ensures that when the fish vacuums the tail end, the hook point penetrates the corner of the jaw.",
                voiceInstruction = "Short strikes detected. Trim one inch off your trailer body or downsize your lure profile by one size. Also pause for half a second before setting the hook to let them turn."
            ),
            TacticalAdaptation(
                id = "water_turned_muddy",
                triggerProblem = "Water turned muddy / Runoff inflow",
                quickDiagnosis = "Incoming storm runoff or boat churn dropped visibility under 1 foot. Sight feeding is completely shut down.",
                immediateAdjustment = "Move right up against hard shoreline banks or culvert current seams. Swap out natural translucent colors immediately.",
                recommendedLureSwap = "Solid Black/Blue ChatterBait with heavy thumping paddle tail, or a loud rattle-chamber squarebill.",
                rationale = "Lateral-line neuromast cells are now the predator's only tracking mechanism. High-amplitude vibration and pitch-black silhouettes are mandatory.",
                voiceInstruction = "Water has gotten muddy. Put away translucent colors. Tie on your black and blue chatterbait or squarebill with heavy vibration so the fish can find it through their lateral line."
            ),
            TacticalAdaptation(
                id = "bright_sun_lockjaw",
                triggerProblem = "Sun came out bright, bite died abruptly",
                quickDiagnosis = "Bright high-sun penetration forces gamefish into dark shaded pockets or tight against deep structure stems.",
                immediateAdjustment = "Target the darkest 5% of available cover: skip underneath covered boat docks, thick overhanging willows, or heavy weed mats.",
                recommendedLureSwap = "Compact heavy tungsten Texas Rig or Punch Jig (1/2 to 1 oz) with small craw.",
                rationale = "Fish lack eyelids; direct bluebird sunlight causes pupil dilation stress. They position directly with their backs touching shade ceilings.",
                voiceInstruction = "Bright sun has locked the fish into heavy shade. Look for boat slips, overhanging trees, or dense weed mats. Punch tight into the thickest shadows with a compact jig."
            ),
            TacticalAdaptation(
                id = "wind_picked_up",
                triggerProblem = "Wind picked up heavily (15+ mph gusts)",
                quickDiagnosis = "Wind pushes plankton and baitfish directly into wind-blown points and shoreline banks.",
                immediateAdjustment = "Move your boat or position directly onto the wind-blown bank facing into the wind. Throw with the wind at your back.",
                recommendedLureSwap = "Double Willow Spinnerbait or Lipless Crankbait burned across the wind-churned surf.",
                rationale = "Surface wave chop breaks light penetration, making predators bolder and disorienting forage against the bank.",
                voiceInstruction = "Wind has increased. Head straight to the shoreline where the wind is blowing water onto the bank. Throw a spinnerbait or lipless crankbait right into the churned wave breaks."
            )
        )
    }

    private data class Tuple5<A, B, C, D, E>(val a: A, val b: B, val c: C, val d: D, val e: E)
}
