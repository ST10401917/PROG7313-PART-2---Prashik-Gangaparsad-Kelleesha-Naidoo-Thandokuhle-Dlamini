package Data.dao

import Data.Expense
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExpenseDao {
    @Insert
    suspend fun insertExpense(expense: Expense) // Insert a new expense into the database

    // Query that can retrieve all the expenses from the database
    @Query("SELECT * FROM expenses_table")
    suspend fun getAllExpenses(): List<Expense>

    // Query that can retrieve all the expenses between two dates from the database
    @Query("SELECT * FROM expenses_table WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getExpensesBetweenDates(startDate: String, endDate: String): List<Expense>
}