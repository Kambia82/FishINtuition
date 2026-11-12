package com.example.data.model

data class KnotGuide(
    val id: String,
    val name: String,
    val difficulty: String,
    val bestFor: String,
    val steps: List<KnotStep>,
    val proTip: String
)

data class KnotStep(
    val stepNumber: Int,
    val instruction: String,
    val why: String
)

data class LiveBaitRig(
    val id: String,
    val name: String,
    val baitType: String,
    val hookSize: String,
    val targetSpecies: String,
    val rigSummary: String,
    val hookingMethod: String,
    val bestDepth: String
)

data class ReelGuide(
    val id: String,
    val name: String,
    val experienceLevel: String,
    val pros: List<String>,
    val cons: List<String>,
    val bestFor: String,
    val backlashRecoverySteps: List<String>? = null
)

data class RetailerItem(
    val id: String,
    val name: String,
    val category: String, // "Hooks & Weights", "Soft Plastics", "Hard Baits", "Terminal & Line"
    val typicalPrice: String,
    val whyNeeded: String,
    val walmartAisleTip: String,
    val recommendedBrand: String
)

object MentorCatalog {
    val knots: List<KnotGuide> = listOf(
        KnotGuide(
            id = "palomar",
            name = "Palomar Knot",
            difficulty = "Easiest & Strongest (95%+ strength)",
            bestFor = "Braided line, terminal tackle, hooks, and swivels",
            steps = listOf(
                KnotStep(1, "Double 6 inches of line and pass through the eye of the hook.", "Doubling the line cushions the eyelet and prevents friction slippage."),
                KnotStep(2, "Tie a loose overhand knot with the doubled line.", "Keep the hook suspended in the center loop."),
                KnotStep(3, "Pass the loop completely over the hook, lure, or weight.", "Locks the knot around the entire bait structure."),
                KnotStep(4, "CRITICAL: Wet knot thoroughly with water or saliva before pulling tight.", "Water eliminates heat friction that permanently weakens fluorocarbon and monofilament."),
                KnotStep(5, "Pull both ends evenly and trim tag end to 1/8 inch.", "Leaves a clean profile that won't catch floating pond weeds.")
            ),
            proTip = "The #1 knot for beginners and pros alike. Never ties backwards."
        ),
        KnotGuide(
            id = "improved_clinch",
            name = "Improved Clinch Knot",
            difficulty = "Beginner (Classic)",
            bestFor = "Monofilament and light fluorocarbon (under 15 lb)",
            steps = listOf(
                KnotStep(1, "Pass line through hook eye, leave 4 inches of tag.", "Gives ample line for twists."),
                KnotStep(2, "Wrap tag end 5 to 7 times around the standing line.", "Creates the gripping friction spirals."),
                KnotStep(3, "Thread tag through first loop near eye, then back through big loop.", "The 'improved' lock prevents the coils from unspooling."),
                KnotStep(4, "Moisten line generously and slide coils neatly down to the eye.", "Stacked coils maintain line rating."),
                KnotStep(5, "Cinch tight and trim tag end.", "Inspect for any overlapping crossed loops.")
            ),
            proTip = "Avoid using on slick braided line, as braid will slip out of a clinch."
        )
    )

    val liveBaitRigs: List<LiveBaitRig> = listOf(
        LiveBaitRig(
            id = "nightcrawler_splitshot",
            name = "Nightcrawler on Split-Shot Rig",
            baitType = "Live Canadian Nightcrawler",
            hookSize = "#6 or #8 Baitholder Hook",
            targetSpecies = "Bluegill, Bass, Catfish, Trout",
            rigSummary = "Thread half a worm onto a baitholder hook with barbs on the shank. Place one round split-shot sinker 12 to 18 inches above the hook.",
            hookingMethod = "Thread hook point through the broken tip and push down the shank, leaving 1 inch of dangling tail for natural wiggling action.",
            bestDepth = "Bottom substrate or slowly dragged along weedline drops"
        ),
        LiveBaitRig(
            id = "live_minnow_slipbobber",
            name = "Live Minnow on Slip Bobber",
            baitType = "Fathead Minnow or Emerald Shiner",
            hookSize = "#4 or #6 Light Wire Aberdeen Hook",
            targetSpecies = "Crappie, Walleye, Smallmouth Bass",
            rigSummary = "Slip bobber knot on mainline, small plastic bead, slip float, split shot, and light wire hook. Allows casting deep while suspending bait at exact depth.",
            hookingMethod = "Hook through the upper lip and out a nostril (for active swimming) or through the back just behind the dorsal fin without hitting the spine (for vertical hovering).",
            bestDepth = "3 to 12 feet suspended over brush piles or rock reefs"
        ),
        LiveBaitRig(
            id = "chicken_liver_bottom",
            name = "Chicken Liver on Bottom Sinker Rig",
            baitType = "Fresh Chicken Liver (Grocery Meat Dept)",
            hookSize = "2/0 or 3/0 Circle Hook",
            targetSpecies = "Channel Catfish, Bullhead",
            rigSummary = "Egg sinker (1/2 to 1 oz) free-sliding on mainline, swivel, 12-inch 20lb mono leader to circle hook. Fish hook themselves against the rod holder.",
            hookingMethod = "Wrap liver in sewing thread or pantyhose mesh scraps to prevent it flying off during long casts into river currents.",
            bestDepth = "Deep river holes, creek confluences, and reservoir channels"
        )
    )

    val reels: List<ReelGuide> = listOf(
        ReelGuide(
            id = "spincast",
            name = "Spincast Reel (Closed-Face / Zebco)",
            experienceLevel = "Beginner / Kids / Casual",
            pros = listOf("Virtually tangle-free closed cone", "Simple push-button casting mechanism", "Affordable and durable ($15 - $35)"),
            cons = listOf("Limited casting distance", "Slower line retrieve ratio", "Cannot handle heavy 20+ lb line"),
            bestFor = "First-time anglers, bank fishing with bobbers, pond panfish."
        ),
        ReelGuide(
            id = "spinning",
            name = "Spinning Reel (Open-Bail)",
            experienceLevel = "All Anglers (Most Versatile)",
            pros = listOf("Exceptional casting distance with light lures", "Zero backlashes/birdnests", "Great drag system for fighting hard fish"),
            cons = listOf("Can twist line if reeled while fish is taking drag", "Requires finger line management"),
            bestFor = "Finesse worms, live bait, ned rigs, trout spinners, and clear water."
        ),
        ReelGuide(
            id = "baitcaster",
            name = "Baitcaster Reel (Revolving Spool)",
            experienceLevel = "Intermediate / Experienced",
            pros = listOf("Pinpoint casting accuracy to targets", "Maximum winch power and high-speed retrieve", "Throws heavy lures and punches thick mats"),
            cons = listOf("Prone to backlashes (birds-nests) if thumb pressure slips", "Requires practice and tension knob calibration"),
            bestFor = "Heavy jigs, crankbaits, frogs, chatterbaits, and flipping docks.",
            backlashRecoverySteps = listOf(
                "Stop pulling line immediately as soon as a tangle appears. Pulling hard tightens buried loops.",
                "Tighten the drag star, press your thumb firmly onto the spool over the bird's-nest, and crank the reel handle 2 full turns.",
                "Pull line forward gently from in front of the reel. If it snags, locate the cross-loop and flick it forward with your thumb.",
                "Pro Tip: Calibrate your spool tension knob whenever you change lure weight until the lure falls slowly when you hit the thumb bar without overrunning when it hits the ground."
            )
        )
    )

    val walmartChecklist: List<RetailerItem> = listOf(
        RetailerItem(
            id = "wm_senko",
            name = "Gary Yamamoto 5\" Senko / YUM Dinger",
            category = "Soft Plastics",
            typicalPrice = "$5.98 - $7.48",
            whyNeeded = "The #1 confidence bait on Earth. Works in any water in America. Cast out, let it sink without touching it.",
            walmartAisleTip = "Find in the Soft Plastics wall; look for Green Pumpkin or Black with Blue Flake.",
            recommendedBrand = "Gary Yamamoto or YUM Dinger"
        ),
        RetailerItem(
            id = "wm_hooks",
            name = "Offset Round Bend / EWG Hooks 3/0",
            category = "Hooks & Weights",
            typicalPrice = "$3.96 (Pack of 5)",
            whyNeeded = "Allows you to rig worms weedless so they bounce off logs and grass without snagging.",
            walmartAisleTip = "Terminal tackle carousel near the sinkers. Eagle Claw or Gamakatsu.",
            recommendedBrand = "Gamakatsu or Eagle Claw Lazer Sharp"
        ),
        RetailerItem(
            id = "wm_roostertail",
            name = "Yakima Bait Rooster Tail 1/8 oz",
            category = "Hard Baits",
            typicalPrice = "$4.24 each",
            whyNeeded = "Spinning blade and hackle tail triggers panfish, bass, and trout in any stream or pond.",
            walmartAisleTip = "Inline spinners peg hook. Brown Trout, White, or Chartreuse.",
            recommendedBrand = "Yakima Bait"
        ),
        RetailerItem(
            id = "wm_bulletweights",
            name = "Bullet Weights 3/16 oz (Lead or Tungsten)",
            category = "Hooks & Weights",
            typicalPrice = "$3.48",
            whyNeeded = "Slides onto your line ahead of the hook for Texas rigging.",
            walmartAisleTip = "Sinker wall next to split shot dials.",
            recommendedBrand = "Bullet Weights"
        ),
        RetailerItem(
            id = "wm_mono",
            name = "Trilene XL Monofilament 8 lb or 10 lb",
            category = "Terminal & Line",
            typicalPrice = "$7.98 (330 yds)",
            whyNeeded = "Forgiving, easy to tie, floats well for topwater and bobber fishing.",
            walmartAisleTip = "Spool racks at the end of the aisle. Clear or Low-Vis Green.",
            recommendedBrand = "Berkley Trilene XL"
        ),
        RetailerItem(
            id = "wm_chatterbait",
            name = "Z-Man Original ChatterBait 3/8 oz",
            category = "Hard Baits",
            typicalPrice = "$5.48",
            whyNeeded = "Hex-blade thumps hard in murky water or wind; easy cast-and-reel retrieve.",
            walmartAisleTip = "Bladed jigs section. White/Chartreuse or Black/Blue.",
            recommendedBrand = "Z-Man Fishing"
        )
    )
}
