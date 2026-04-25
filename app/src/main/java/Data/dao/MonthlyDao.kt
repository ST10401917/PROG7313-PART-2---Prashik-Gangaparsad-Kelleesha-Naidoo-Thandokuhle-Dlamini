package Data.dao

import Data.MonthlyGoal
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MonthlyDao {
    @Insert
    suspend fun insertGoal(goal: MonthlyGoal) // Insert a new monthly goal into the database

    // Query that can retrieve the monthly goal from the database
    @Update
    suspend fun updateGoal(goal: MonthlyGoal)

    // Query that can retrieve the monthly goal from the database
    @Query("SELECT * FROM monthlyGoals_table LIMIT 1")
    suspend fun getGoal(): MonthlyGoal?
}