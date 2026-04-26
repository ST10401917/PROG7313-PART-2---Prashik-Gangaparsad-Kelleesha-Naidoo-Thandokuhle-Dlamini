package Data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Creates and marks the class as a Room database entity (table)
@Entity(tableName = "monthlyGoals_table")
data class MonthlyGoal(
    // Primary key for the monthly goals is auto generated
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,

    val minGoal: Double, // Stores the minimum goal amount
    val maxGoal: Double // Stores the maximum goal amount

)