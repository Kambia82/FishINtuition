package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.CatchLog
import com.example.data.model.DefaultTackleSeed
import com.example.data.model.GamePlan
import com.example.data.model.TackleItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [TackleItem::class, CatchLog::class, GamePlan::class],
    version = 1,
    exportSchema = false
)
abstract class NovaCastDatabase : RoomDatabase() {
    abstract fun tackleDao(): TackleDao
    abstract fun catchDao(): CatchDao
    abstract fun gamePlanDao(): GamePlanDao

    companion object {
        @Volatile
        private var INSTANCE: NovaCastDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): NovaCastDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NovaCastDatabase::class.java,
                    "novacast_intelligence.db"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    val tackleDao = database.tackleDao()
                    if (tackleDao.getCount() == 0) {
                        tackleDao.insertAll(DefaultTackleSeed.starterTackle)
                    }

                    val catchDao = database.catchDao()
                    catchDao.insertCatch(
                        CatchLog(
                            speciesName = "Largemouth Bass",
                            weightLbs = 4.8f,
                            lengthInches = 20.5f,
                            lureUsed = "Z-Man ChatterBait Elite EVO",
                            lureColor = "Black / Blue",
                            waterBodyName = "Lake Guntersville",
                            waterTempF = 69,
                            barometricPressureInHg = 29.84,
                            waterClarity = "Murky / Off-Color",
                            timeOfDay = "Morning (7 AM - 11 AM)",
                            notes = "Crushed it on the outside grass edge right during a 30-minute pre-frontal barometer plunge."
                        )
                    )
                    catchDao.insertCatch(
                        CatchLog(
                            speciesName = "Smallmouth Bass",
                            weightLbs = 3.6f,
                            lengthInches = 18.2f,
                            lureUsed = "Ned Rig TRD with Finesse ShroomZ",
                            lureColor = "The Deal (Smoky Shad)",
                            waterBodyName = "Mille Lacs Lake",
                            waterTempF = 61,
                            barometricPressureInHg = 30.12,
                            waterClarity = "Crystal Clear",
                            timeOfDay = "Midday (11 AM - 3 PM)",
                            notes = "Hit on an 8-second dead stick pause over isolated 14ft boulder transition."
                        )
                    )

                    val planDao = database.gamePlanDao()
                    planDao.insertPlan(
                        GamePlan(
                            title = "Spring Pre-Spawn Blitz",
                            waterBodyName = "Lake Lanier",
                            targetSpecies = "Largemouth Bass",
                            plannedDate = "Saturday Morning",
                            primaryPlanA = "Squarebill Crankbait deflecting off shallow secondary clay points and dock pilings.",
                            secondaryPlanB = "Weightless Senko wacky-rigged skipped under deep shaded walkways if high pressure hits.",
                            emergencyFailsafePlanC = "Drop-shotting 4.5\" straight tail worm on 15-20ft timber edge transitions.",
                            riggingChecklist = "7'0\" MH Casting (15lb fluoro), 7'0\" Medium Spinning (8lb braid to 6lb fluoro leader).",
                            feedingWindowTarget = "6:30 AM - 9:00 AM (Solunar Major Peak)",
                            notes = "Wind predicted from SW at 10mph; target wind-blown shorelines first."
                        )
                    )
                }
            }
        }
    }
}
