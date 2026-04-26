package com.example.prog7313budgettrackerapp

import Data.Expense
import Data.MonthlyGoal
import Data.database.AppDatabase
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.util.Calendar

class ExpensesPage : AppCompatActivity() {

    private lateinit var amountText: EditText
    private lateinit var dateText: EditText
    private lateinit var descipText: EditText
    private lateinit var addPhotobtn: Button
    private lateinit var savebtn: Button
    private lateinit var minGoal : EditText
    private lateinit var maxGoal: EditText
    private lateinit var savebtn2: Button
    private lateinit var categoryText: EditText


    private lateinit var db: AppDatabase

    private var selectedPhotoUri: String? = null
    private lateinit var getImage: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_expenses_page)

        db = AppDatabase.getDatabase(this)


        getImage = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            uri?.let {

                // Grant persistable permission so we can see the image in the report later
                contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                selectedPhotoUri = it.toString()
                Toast.makeText(this, "Photo selected", Toast.LENGTH_SHORT).show()
                addPhotobtn.text = "Photo Added"
            }
        }

        categoryText = findViewById(R.id.CategoryText)
        amountText = findViewById(R.id.AmountText)
        dateText = findViewById(R.id.DateText)
        descipText = findViewById(R.id.DescipText)
        savebtn2 = findViewById(R.id.Savebtn2)
        savebtn = findViewById(R.id.Savebtn)
        minGoal = findViewById(R.id.MinGoal)
        maxGoal = findViewById(R.id.MaxGoal)
        addPhotobtn = findViewById(R.id.AddPhotobtn)


        dateText.setOnClickListener {
            showDatePicker()
        }

        addPhotobtn.setOnClickListener {
            getImage.launch(arrayOf("image/*"))
        }

        savebtn.setOnClickListener {
            saveExpense()
        }

        savebtn2.setOnClickListener {
            lifecycleScope.launch {
                saveGoals()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }
    private fun saveExpense(){
        val category = categoryText.text.toString().trim()
        val amountStr = amountText.text.toString().trim()
        val date = dateText.text.toString().trim()
        val description = descipText.text.toString().trim()


        //validation checks
        if(category.isEmpty()|| amountStr.isEmpty() || date.isEmpty()|| description.isEmpty()){
            Toast.makeText(this, "Please fill in all the required fields", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountStr.toDoubleOrNull()

        if(amount == null){

            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            return
        }

        val expense = Expense(
            category = category,
            amount = amount,
            date = date,
            description = description,
            photoUri = selectedPhotoUri
        )

        lifecycleScope.launch {
            db.expenseDao().insertExpense(expense)

            runOnUiThread {
                Toast.makeText(this@ExpensesPage, "Expenses saved successfully", Toast.LENGTH_SHORT).show()


                categoryText.text.clear()
                amountText.text.clear()
                dateText.text.clear()
                descipText.text.clear()
                selectedPhotoUri = null
                addPhotobtn.text = "Add a Photo"
            }
        }

    }

    suspend fun saveGoals(){
        val minGoalStr = minGoal.text.toString().trim()
        val maxGoalStr = maxGoal.text.toString().trim()

        if (minGoalStr.isEmpty()|| maxGoalStr.isEmpty()){
            Toast.makeText(this, "Please enter the minimum and maximum goals", Toast.LENGTH_SHORT).show()
            return
        }

        val minGoalVal = minGoalStr.toDoubleOrNull()
        val maxGoalVal = maxGoalStr.toDoubleOrNull()

        if (minGoalVal == null || maxGoalVal == null){
            Toast.makeText(this, "Please enter valid numbers for goals", Toast.LENGTH_SHORT).show()
            return
        }

        if (minGoalVal >= maxGoalVal){
            Toast.makeText(this, "Minimum goal cannot be greater than or equal to maximum goal", Toast.LENGTH_SHORT).show()
            return
        }

        val existingGoals = db.monthlyDao().getGoal()

        if (existingGoals == null){
            val newGoal = MonthlyGoal(
                minGoal = minGoalVal,
                maxGoal = maxGoalVal
            )

            db.monthlyDao().insertGoal(newGoal)
        } else {
            val updatedGoal = existingGoals.copy(
                minGoal = minGoalVal,
                maxGoal = maxGoalVal
            )
            db.monthlyDao().updateGoal(updatedGoal)
        }

        runOnUiThread {
            Toast.makeText(this@ExpensesPage, "Goals saved successfully", Toast.LENGTH_SHORT).show()
            minGoal.text.clear()
            maxGoal.text.clear()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formatedMonth = String.format("%02d",selectedMonth + 1)
                val formatedDay = String.format("%02d",selectedDay)

                val selectedDate = "$selectedYear-$formatedMonth-$formatedDay"
                dateText.setText(selectedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }
}

