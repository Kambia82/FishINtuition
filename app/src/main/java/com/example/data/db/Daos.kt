package com.example.data.db

import androidx.room.*
import com.example.data.model.CatchLog
import com.example.data.model.GamePlan
import com.example.data.model.TackleItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TackleDao {
    @Query("SELECT * FROM tackle_items ORDER BY isFavorite DESC, category ASC, name ASC")
    fun getAllTackle(): Flow<List<TackleItem>>

    @Query("SELECT * FROM tackle_items WHERE inBox = 1 ORDER BY isFavorite DESC, name ASC")
    fun getInBoxTackle(): Flow<List<TackleItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTackle(item: TackleItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<TackleItem>)

    @Update
    suspend fun updateTackle(item: TackleItem)

    @Delete
    suspend fun deleteTackle(item: TackleItem)

    @Query("SELECT COUNT(*) FROM tackle_items")
    suspend fun getCount(): Int
}

@Dao
interface CatchDao {
    @Query("SELECT * FROM catch_logs ORDER BY timestampMillis DESC")
    fun getAllCatches(): Flow<List<CatchLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCatch(catchLog: CatchLog): Long

    @Delete
    suspend fun deleteCatch(catchLog: CatchLog)
}

@Dao
interface GamePlanDao {
    @Query("SELECT * FROM game_plans ORDER BY createdAtMillis DESC")
    fun getAllGamePlans(): Flow<List<GamePlan>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlan(plan: GamePlan): Long

    @Delete
    suspend fun deletePlan(plan: GamePlan)
}
