package com.example.incomefunction

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewincomes: Button = findViewById(R.id.dashboardButton)
        viewincomes.setOnClickListener {
            val intent = Intent(this, DisplayIncome::class.java)
            startActivity(intent)
        }


//        val addExpenseButton: Button = findViewById(R.id.addExpenseButton)
//        addExpenseButton.setOnClickListener {
//            val intent = Intent(this, AddExpensesActivity::class.java)
//            startActivity(intent)
//        }

        val addIncomeButton: Button = findViewById(R.id.addIncomeButton)
        addIncomeButton.setOnClickListener {
            val intent = Intent(this, AddIncomesActivity::class.java)
            startActivity(intent)
        }
    }
}