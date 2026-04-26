package com.example.prog7313budgettrackerapp

import Data.database.AppDatabase
import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.util.Calendar

class ReportPage : AppCompatActivity() {

    // global declaration of variables
    private lateinit var startingDate : EditText // starting date
    private lateinit var endingDate : EditText // ending date
    private lateinit var filter_Expenses : Button // filter expenses
    private lateinit var textView9 : TextView // display total

    private lateinit var expenseListContainer : LinearLayout // container for expenses

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_report_page)

        db = AppDatabase.getDatabase(this)

        // Initialize views
        startingDate = findViewById(R.id.startingDate)
        endingDate = findViewById(R.id.endingDate)
        filter_Expenses = findViewById(R.id.filter_Expenses)
        textView9 = findViewById(R.id.textView9)
        expenseListContainer = findViewById(R.id.expenseListContainer)


        // Set click listeners
        startingDate.setOnClickListener {
            showStartDatePicker()
        }

        endingDate.setOnClickListener {
            showEndDatePicker()
        }

        filter_Expenses.setOnClickListener {
            filterExpenses()
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Function to display a DatePickerDialog for selecting the START date
    private fun showStartDatePicker(){

        // Get the current date from the calendar
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            {_, selectedYear, selectedMonth, selectedDay ->
                val formattedMonth = String.format("%02d", selectedMonth + 1)
                val formattedDay = String.format("%02d", selectedDay)   // Format month and day so it will always have 2 digits

                // Create date string in YYYY-MM-DD format
                val selectedDate = "$selectedYear-$formattedMonth-$formattedDay"
                startingDate.setText(selectedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show() // Show the date picker dialog
    }



    // Function to filter expenses between selected start and end dates
    private fun filterExpenses(){
        val startDate = startingDate.text.toString()
        val endDate = endingDate.text.toString()

        // validation to check if both dates are entered
        if(startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, "Please enter both start and end dates", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            // Retrieve the expenses between selected dates from Room database
            val filteredExpenses = db.expenseDao().getExpensesBetweenDates(startDate, endDate)

            runOnUiThread {
                // Clear previous results
                expenseListContainer.removeAllViews()

                // Check to see if no expenses were found
                if (filteredExpenses.isEmpty()) {
                    val noResultsText = TextView(this@ReportPage)
                    noResultsText.text = "No expenses found between the selected dates"
                    expenseListContainer.addView(noResultsText)
                    textView9.text = "Total: R0.00"
                    return@runOnUiThread
                }

                var totalAmount = 0.0

                // Loop through each expense record
                for (expense in filteredExpenses) {
                    // Create a container for each expense
                    val itemLayout = LinearLayout(this@ReportPage)
                    itemLayout.orientation = LinearLayout.VERTICAL
                    itemLayout.setPadding(0, 0, 0, 32)

                    // Text details
                    val detailsTextView = TextView(this@ReportPage)
                    detailsTextView.text = "Category: ${expense.category}\nAmount: R${expense.amount}\nDate: ${expense.date}\nDescription: ${expense.description}"
                    detailsTextView.textSize = 16f
                    itemLayout.addView(detailsTextView)

                    // Display image if available
                    if (!expense.photoUri.isNullOrEmpty()) {
                        val imageView = ImageView(this@ReportPage)
                        val layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            600 // Fixed height for preview
                        )
                        imageView.layoutParams = layoutParams
                        imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
                        try {
                            imageView.setImageURI(Uri.parse(expense.photoUri))
                            itemLayout.addView(imageView)
                        } catch (e: Exception) {
                            // Handle error (e.g., permission issues)
                        }
                    }

                    expenseListContainer.addView(itemLayout)
                    totalAmount += expense.amount
                }

                textView9.text = "Total: R%.2f".format(totalAmount)
            }
        }
    }

    // Function to display a DatePickerDialog for selecting the END date
    private fun showEndDatePicker(){
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            {_, selectedYear, selectedMonth, selectedDay ->
                val formattedMonth = String.format("%02d", selectedMonth + 1)
                val formattedDay = String.format("%02d", selectedDay) // Format month and day so it will always have 2 digits

                // Create date string in YYYY-MM-DD format
                val selectedDate = "$selectedYear-$formattedMonth-$formattedDay"
                endingDate.setText(selectedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show() // Show the date picker dialog
    }
}