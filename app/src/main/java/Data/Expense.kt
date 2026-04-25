package Data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Creates and marks the class as a Room database entity (table)
@Entity(tableName = "expenses_table")
data class Expense(
    //Primary key for the expenses is auto generated
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val category: String, // Stores the expense category
    val amount: Double, // Stores the expense category
    val date: String, // Stores the date of the expense
    val description: String, // Stores the description about the expense
    val photoUri: String? = null // Stores the URI/path of the photo
)