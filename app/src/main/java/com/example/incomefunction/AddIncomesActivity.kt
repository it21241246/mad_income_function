package com.example.incomefunction

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class AddIncomesActivity : AppCompatActivity() {

    private lateinit var incomeNameEditText: EditText
    private lateinit var incomeAmountEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var timeEditText: EditText
    private lateinit var saveIncomeButton: Button

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_incomes)

        incomeNameEditText = findViewById(R.id.incomeNameInput)
        incomeAmountEditText = findViewById(R.id.incomeAmountInput)
        dateEditText = findViewById(R.id.incomeDateInput)
        timeEditText = findViewById(R.id.incomeTimeInput)
        saveIncomeButton = findViewById(R.id.saveIncomeButton)

        // Get the current date and time
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        dateEditText.setText(dateFormat.format(calendar.time))
        timeEditText.setText(timeFormat.format(calendar.time))

        // Set up click listeners for the date and time fields to allow the user to edit them
        dateEditText.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(this,
                { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    calendar.set(selectedYear, selectedMonth, selectedDayOfMonth)
                    dateEditText.setText(dateFormat.format(calendar.time))
                },
                year,
                month,
                dayOfMonth
            )
            datePickerDialog.show()
        }

        timeEditText.setOnClickListener {
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(this,
                { _, selectedHour, selectedMinute ->
                    calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                    calendar.set(Calendar.MINUTE, selectedMinute)
                    timeEditText.setText(timeFormat.format(calendar.time))
                },
                hour,
                minute,
                false
            )
            timePickerDialog.show()
        }

        saveIncomeButton.setOnClickListener {
            val incomeName = incomeNameEditText.text.toString().trim()
            val incomeAmount = incomeAmountEditText.text.toString().toDouble()
            val date = calendar.time


            val incomesCollectionRef = db.collection("incomes")
            val IncomeDocRef = incomesCollectionRef.document()
            val IncomeId = IncomeDocRef.id

            // Create a new income document with the data and date
            val income = hashMapOf(
                "id" to IncomeId,
                "name" to incomeName,
                "amount" to incomeAmount,
                "date" to date
            )

            // Add the income to the 'incomes' collection in Firestore
            IncomeDocRef.set(income)
                .addOnSuccessListener {
                    // Show a success message and finish the activity
                    Toast.makeText(this, "Expense added successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    // Show an error message if the expense couldn't be added
                    Toast.makeText(this, "Error adding expense: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

    }
}