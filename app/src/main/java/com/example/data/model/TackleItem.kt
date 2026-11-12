package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tackle_items")
data class TackleItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val category: String, // "Soft Plastics", "Crankbaits & Plugs", "Jigs & Skirts", "Topwater", "Wire & Spinners", "Spoons & Blades", "Flies & Micro", "Terminal Rigs"
    val brand: String,
    val color: String,
    val weightOz: String,
    val targetSpeciesSummary: String, // Comma-separated or list of species keywords
    val bestWaterClarity: String, // "Any", "Clear", "Stained", "Murky / Muddy"
    val optimalDepthRange: String, // e.g. "0-2 ft", "3-8 ft", "8-18 ft", "Deep Bottom"
    val presentationStyle: String, // "Reaction / Power Cast", "Slow Finesse Drag", "Topwater Twitch", "Bottom Jig Thump", "Drift / Suspension"
    val notes: String = "",
    val inBox: Boolean = true,
    val isFavorite: Boolean = false
)

object DefaultTackleSeed {
    val starterTackle: List<TackleItem> = listOf(
        TackleItem(
            name = "Senko 5\" Stick Worm",
            category = "Soft Plastics",
            brand = "Gary Yamamoto",
            color = "Green Pumpkin / Black Flake",
            weightOz = "Weightless / 3/8 oz Jig Head",
            targetSpeciesSummary = "Largemouth Bass, Smallmouth Bass, Spotted Bass",
            bestWaterClarity = "Clear & Stained",
            optimalDepthRange = "1 - 10 ft",
            presentationStyle = "Slow Finesse Drag",
            notes = "The ultimate failsafe bait on high-pressure post-frontal days. Wacky rig or Texas rig.",
            isFavorite = true
        ),
        TackleItem(
            name = "KVD 1.5 Squarebill Crankbait",
            category = "Crankbaits & Plugs",
            brand = "Strike King",
            color = "Sexy Shad",
            weightOz = "3/8 oz",
            targetSpeciesSummary = "Largemouth Bass, Smallmouth Bass, Striped Bass, Pike",
            bestWaterClarity = "Stained & Murky",
            optimalDepthRange = "3 - 6 ft",
            presentationStyle = "Reaction / Power Cast",
            notes = "Deflect off riprap, stumps, and dock pilings to trigger erratic reaction strikes.",
            isFavorite = true
        ),
        TackleItem(
            name = "Z-Man ChatterBait Elite EVO",
            category = "Wire & Spinners",
            brand = "Z-Man",
            color = "Black / Blue",
            weightOz = "1/2 oz",
            targetSpeciesSummary = "Largemouth Bass, Northern Pike, Striped Bass",
            bestWaterClarity = "Murky / Muddy",
            optimalDepthRange = "2 - 8 ft",
            presentationStyle = "Reaction / Power Cast",
            notes = "Massive blade vibration alerts lateral lines in dirty water or low light.",
            isFavorite = true
        ),
        TackleItem(
            name = "Ned Rig TRD with Finesse ShroomZ",
            category = "Soft Plastics",
            brand = "Z-Man",
            color = "The Deal (Smoky Shad)",
            weightOz = "1/10 oz",
            targetSpeciesSummary = "Smallmouth Bass, Largemouth Bass, Walleye, Yellow Perch",
            bestWaterClarity = "Clear & Stained",
            optimalDepthRange = "4 - 25 ft",
            presentationStyle = "Slow Finesse Drag",
            notes = "Buoyant ElaZtech stands straight up off bottom on pause. Unmatched for finicky smallmouth.",
            isFavorite = true
        ),
        TackleItem(
            name = "Rapala Shad Rap (SR07)",
            category = "Crankbaits & Plugs",
            brand = "Rapala",
            color = "Perch / Firetiger",
            weightOz = "5/16 oz",
            targetSpeciesSummary = "Walleye, Smallmouth Bass, Crappie, Northern Pike",
            bestWaterClarity = "Stained & Murky",
            optimalDepthRange = "5 - 11 ft",
            presentationStyle = "Reaction / Power Cast",
            notes = "Tight balsa wobble mimics stressed baitfish; works in cold water transitions.",
            isFavorite = true
        ),
        TackleItem(
            name = "Bobby's Perfect Frog",
            category = "Topwater",
            brand = "Snag Proof",
            color = "Matte Black",
            weightOz = "1/2 oz",
            targetSpeciesSummary = "Largemouth Bass, Northern Pike, Snook",
            bestWaterClarity = "Murky / Muddy",
            optimalDepthRange = "0 - 1 ft (Surface)",
            presentationStyle = "Topwater Twitch",
            notes = "Walk over thick matted vegetation, lily pads, and shaded docks.",
            isFavorite = false
        ),
        TackleItem(
            name = "Keitech Swing Impact Fat 3.8\"",
            category = "Soft Plastics",
            brand = "Keitech",
            color = "Pro Blue Red Pearl",
            weightOz = "1/4 oz Swimbait Head",
            targetSpeciesSummary = "Smallmouth Bass, Striped Bass, Walleye, Snook, Redfish",
            bestWaterClarity = "Clear & Stained",
            optimalDepthRange = "2 - 15 ft",
            presentationStyle = "Drift / Suspension",
            notes = "Ultra-soft ribbed body with high-frequency tail kick at slowest crawl speeds.",
            isFavorite = true
        ),
        TackleItem(
            name = "Rooster Tail Inline Spinner",
            category = "Wire & Spinners",
            brand = "Yakima Bait",
            color = "Brown Trout / Gold Blade",
            weightOz = "1/8 oz",
            targetSpeciesSummary = "Rainbow Trout, Brown Trout, Bluegill, Yellow Perch",
            bestWaterClarity = "Clear & Stained",
            optimalDepthRange = "1 - 5 ft",
            presentationStyle = "Reaction / Power Cast",
            notes = "Spinning blade flash plus pulsating hackle tail triggers instinctive stream strikes.",
            isFavorite = true
        ),
        TackleItem(
            name = "Micro Tube Jig",
            category = "Flies & Micro",
            brand = "Strike King Mr. Crappie",
            color = "Electric Chicken (Chartreuse/Pink)",
            weightOz = "1/16 oz",
            targetSpeciesSummary = "Crappie (Black & White), Bluegill, Yellow Perch",
            bestWaterClarity = "Stained & Murky",
            optimalDepthRange = "4 - 14 ft",
            presentationStyle = "Drift / Suspension",
            notes = "Vertical jig through submerged brush tops or tight to dock pilings.",
            isFavorite = true
        ),
        TackleItem(
            name = "Weedless Gold Spoon",
            category = "Spoons & Blades",
            brand = "Johnson Silver Minnow",
            color = "Reflective 24k Gold",
            weightOz = "1/2 oz",
            targetSpeciesSummary = "Redfish (Red Drum), Northern Pike, Largemouth Bass",
            bestWaterClarity = "Stained & Murky",
            optimalDepthRange = "1 - 4 ft",
            presentationStyle = "Reaction / Power Cast",
            notes = "Wobbles over oyster beds and shallow marsh grass without snagging.",
            isFavorite = true
        ),
        TackleItem(
            name = "Santee Cooper Rig with Circle Hook",
            category = "Terminal Rigs",
            brand = "Whisker Seeker",
            color = "High-Vis Float",
            weightOz = "2.0 oz Egg Sinker",
            targetSpeciesSummary = "Channel Catfish, Blue Catfish, Flathead Catfish",
            bestWaterClarity = "Any",
            optimalDepthRange = "Deep Bottom",
            presentationStyle = "Bottom Jig Thump",
            notes = "Suspends fresh cut shad 6 inches off muddy bottoms right in sensory strike zones.",
            isFavorite = true
        ),
        TackleItem(
            name = "Pheasant Tail Beadhead Nymph #16",
            category = "Flies & Micro",
            brand = "Umpqua",
            color = "Natural Copper / Pheasant",
            weightOz = "Beadhead (Tungsten)",
            targetSpeciesSummary = "Rainbow Trout, Brown Trout, Brook Trout",
            bestWaterClarity = "Clear",
            optimalDepthRange = "2 - 6 ft",
            presentationStyle = "Drift / Suspension",
            notes = "Dead drift under an indicator along current foam lines and boulder seams.",
            isFavorite = false
        )
    )
}
