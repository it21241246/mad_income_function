package com.example.incomefunction

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class DisplayIncome : AppCompatActivity() {

    private lateinit var IncomeAdapter: IncomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_income)

        IncomeAdapter = IncomeAdapter(this)

        findViewById<RecyclerView>(R.id.expensesRecycler).apply {
            layoutManager = LinearLayoutManager(this@DisplayIncome)
            adapter = IncomeAdapter
        }

        loadIncome()
    }

    private fun loadIncome() {
        FirebaseFirestore.getInstance()
            .collection("income")
            .get()
            .addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
                val dsList = queryDocumentSnapshots.documents
                val incomeList = mutableListOf<Income>()
                for (ds in dsList) {
                    val expenses = ds.toObject(Income::class.java)
                    expenses?.let { incomeList.add(it) }
                }
                IncomeAdapter.setIncome(incomeList)
            }
    }
}

