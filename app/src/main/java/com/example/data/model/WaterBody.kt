package com.example.data.model

enum class WaterCategory(val label: String) {
    URBAN_POND("Urban / Neighborhood Pond"),
    RESERVOIR("Reservoir / Impoundment"),
    NATURAL_LAKE("Natural Glacial Lake"),
    RIVER("River / Tailrace"),
    COASTAL_ESTUARY("Coastal Bay / Inshore Estuary"),
    MOUNTAIN_STREAM("Mountain Freestone Creek")
}

enum class DataSourceOrigin(val label: String, val badgeColorHex: Long) {
    VERIFIED_HYDROLOGY("USGS 3DHP Verified", 0xFF06B6D4),
    OBSERVED_WATERSHED("Public Watershed Recon", 0xFF22C55E),
    CALCULATED_WATERSHED("Calculated Hydro Model", 0xFFF59E0B),
    USER_CUSTOM_SPOT("Angler Private Pin", 0xFFA855F7)
}

enum class AccessStatus(val label: String, val badgeColorHex: Long) {
    VERIFIED_PUBLIC("Verified Public Access", 0xFF22C55E),
    RESTRICTED_OR_PRIVATE("Restricted / Private Access", 0xFFEF4444),
    UNKNOWN_ACCESS("Access Status Unknown", 0xFFF59E0B)
}

data class WaterBody(
    val id: String,
    val name: String,
    val location: String,
    val category: WaterCategory,
    val surfaceArea: String,
    val maxDepthFt: Int,
    val defaultTempF: Int,
    val defaultClarity: WaterClarity,
    val primarySpecies: List<String>,
    val structures: List<String>,
    val accessType: String,
    val description: String,
    val lat: Double,
    val lng: Double,
    val isLocalNeighborhood: Boolean = false,
    val dataOrigin: DataSourceOrigin = DataSourceOrigin.VERIFIED_HYDROLOGY,
    val accessStatus: AccessStatus = AccessStatus.VERIFIED_PUBLIC
)

object WaterCatalog {
    val presetWaters: List<WaterBody> = listOf(
        // Authentic Regional & Local Gems (from NovaCast web app)
        WaterBody(
            id = "busch_wildlife_lake33",
            name = "August A. Busch Lake 33",
            location = "St. Charles County, MO",
            category = WaterCategory.URBAN_POND,
            surfaceArea = "85 Acres",
            maxDepthFt = 18,
            defaultTempF = 68,
            defaultClarity = WaterClarity.STAINED,
            primarySpecies = listOf("largemouth_bass", "black_crappie", "bluegill", "channel_catfish"),
            structures = listOf("Brush Piles along Dam", "Cattail Shorelines", "Fishing Jetties", "Submerged Wood"),
            accessType = "100% Walkable Bank Fishing, ADA Pier & Boat Rental",
            description = "The premier public fishing complex in the region. Lake 33 is stocked heavily and boasts multiple rock casting jetties and timbered coves.",
            lat = 38.7180,
            lng = -90.7380,
            isLocalNeighborhood = true
        ),
        WaterBody(
            id = "creve_coeur_lake",
            name = "Creve Coeur Lake",
            location = "Maryland Heights, MO",
            category = WaterCategory.NATURAL_LAKE,
            surfaceArea = "320 Acres",
            maxDepthFt = 25,
            defaultTempF = 70,
            defaultClarity = WaterClarity.MURKY,
            primarySpecies = listOf("largemouth_bass", "channel_catfish", "black_crappie", "bluegill", "common_carp"),
            structures = listOf("Riprap Causeway", "Dripping Springs Outflow", "Sandy Shoreline Flats", "Sailboat Harbor Docks"),
            accessType = "Full Shoreline Perimeter Trail, Bank Fishing & Kayak Launches",
            description = "Historic oxbow lake with high bank accessibility. Excellent catfish after summer rain and bass along the riprap shores.",
            lat = 38.7144,
            lng = -90.4950,
            isLocalNeighborhood = true
        ),
        WaterBody(
            id = "simpson_lake",
            name = "Simpson Park Lake",
            location = "Valley Park / Fenton, MO",
            category = WaterCategory.URBAN_POND,
            surfaceArea = "72 Acres",
            maxDepthFt = 14,
            defaultTempF = 69,
            defaultClarity = WaterClarity.MURKY,
            primarySpecies = listOf("largemouth_bass", "channel_catfish", "bluegill"),
            structures = listOf("Meramec River Overflow Chute", "Cattail Pockets", "Pier Overhangs", "Lily Pad Corners"),
            accessType = "Paved Bank Perimeter & Concrete Fishing Docks",
            description = "Gentle neighborhood lake nestled along the Meramec River valley. High bank accessibility perfect for quick evening sessions with a spinnerbait or Senko.",
            lat = 38.5492,
            lng = -90.4851,
            isLocalNeighborhood = true
        ),
        WaterBody(
            id = "forest_park_lakes",
            name = "Forest Park Urban Waterway (Jefferson Lake)",
            location = "St. Louis City, MO",
            category = WaterCategory.URBAN_POND,
            surfaceArea = "12 Acres",
            maxDepthFt = 10,
            defaultTempF = 72,
            defaultClarity = WaterClarity.STAINED,
            primarySpecies = listOf("largemouth_bass", "channel_catfish", "rainbow_trout", "bluegill"),
            structures = listOf("Stone Bridges", "Fountain Aerators", "Concrete Wall Edges", "Overhanging Willow Trees"),
            accessType = "100% Paved Urban Bank Access",
            description = "Historic inner-city gem with dedicated trout stocking in winter and hungry bass/panfish in the spring.",
            lat = 38.6369,
            lng = -90.2870,
            isLocalNeighborhood = true
        ),
        // Major Renowned Waters
        WaterBody(
            id = "lake_lanier",
            name = "Lake Lanier",
            location = "Buford, Georgia",
            category = WaterCategory.RESERVOIR,
            surfaceArea = "37,000 Acres",
            maxDepthFt = 160,
            defaultTempF = 67,
            defaultClarity = WaterClarity.CRYSTAL_CLEAR,
            primarySpecies = listOf("smallmouth_bass", "largemouth_bass", "striped_bass", "black_crappie"),
            structures = listOf("Deep Standing Timber", "Clay Points", "Brush Piles", "Floating Marina Docks", "Bridge Pilings"),
            accessType = "Boat Ramps & Extensive Bank Parks",
            description = "Famed highland reservoir renowned for nomadic spotted and striped bass chasing herring over deep timber.",
            lat = 34.2144,
            lng = -83.9922
        ),
        WaterBody(
            id = "guntersville_lake",
            name = "Lake Guntersville",
            location = "Guntersville, Alabama",
            category = WaterCategory.RESERVOIR,
            surfaceArea = "69,000 Acres",
            maxDepthFt = 60,
            defaultTempF = 71,
            defaultClarity = WaterClarity.STAINED,
            primarySpecies = listOf("largemouth_bass", "black_crappie", "channel_catfish", "bluegill"),
            structures = listOf("Hydrilla & Milfoil Mats", "Creek Channel Ledges", "Bridge Riprap", "Causeway Cuts"),
            accessType = "Public Marinas & State Park Bank Access",
            description = "Tennessee River powerhouse celebrated for heavy hydrilla mats, ledge fishing, and massive trophy bass.",
            lat = 34.3582,
            lng = -86.2947
        ),
        WaterBody(
            id = "mille_lacs",
            name = "Mille Lacs Lake",
            location = "Garrison, Minnesota",
            category = WaterCategory.NATURAL_LAKE,
            surfaceArea = "128,000 Acres",
            maxDepthFt = 42,
            defaultTempF = 58,
            defaultClarity = WaterClarity.CRYSTAL_CLEAR,
            primarySpecies = listOf("smallmouth_bass", "walleye", "northern_pike", "muskie", "yellow_perch"),
            structures = listOf("Gravel Humps", "Rock Reefs", "Sand Flats", "Tule Reeds"),
            accessType = "Public Boat Launches & Shoreline Piers",
            description = "Premier northern glacial lake famous for giant smallmouth bass and world-class walleye fishing over isolated rock reefs.",
            lat = 46.2575,
            lng = -93.6558
        ),
        WaterBody(
            id = "tampa_bay",
            name = "Tampa Bay Inshore",
            location = "St. Petersburg, Florida",
            category = WaterCategory.COASTAL_ESTUARY,
            surfaceArea = "250,000 Acres",
            maxDepthFt = 43,
            defaultTempF = 77,
            defaultClarity = WaterClarity.STAINED,
            primarySpecies = listOf("snook", "redfish", "spotted_seatrout", "tarpon"),
            structures = listOf("Mangrove Shorelines", "Oyster Shell Bars", "Seagrass Potholes", "Tidal Creek Mouths", "Bridge Shadow Lines"),
            accessType = "Kayak Launches, Pier & Wading Access",
            description = "Vibrant sub-tropical estuary offering premier inshore flats stalking and nighttime dock ambush opportunities.",
            lat = 27.7676,
            lng = -82.6403
        )
    )
}
