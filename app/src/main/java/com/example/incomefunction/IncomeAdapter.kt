package com.example.incomefunction


import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.incomefunction.IncomeAdapter.MyViewHolder
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Attributes.Name

class IncomeAdapter(private val context: Context) : RecyclerView.Adapter<MyViewHolder>() {
    private val incomeList: MutableList<Income>


    init {
        incomeList = ArrayList()
    }

    fun add(income: Income) {
        incomeList.add(income)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.income_view, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val income = incomeList[position]
        holder.name.text = income.name
        holder.amount.text = income.amount.toString()

        // Convert Timestamp to Date
        val date = income.date?.toDate()

        // Format the Date to a String
        if (date != null) {
            holder.date.text = SimpleDateFormat("dd/MM/yyyy").format(date)
        } else {
            holder.date.text = ""
        }

        holder.deleteButton.setOnClickListener {
            val incomeId = income.id
            val db = FirebaseFirestore.getInstance()
            val incomeCollectionRef = db.collection("incomes")
            val incomeDocRef = incomeId?.let { it1 -> incomeCollectionRef.document(it1) }
            if (incomeDocRef != null) {
                incomeDocRef.delete()
                    .addOnSuccessListener {
                        // Show a success message if the expense is deleted
                        Toast.makeText(holder.itemView.context, "income deleted", Toast.LENGTH_SHORT)
                            .show()
                        incomeList.removeAt(holder.adapterPosition)
                        notifyItemRemoved(holder.adapterPosition)

                    }
                    .addOnFailureListener { e ->
                        // Show an error message if the expense couldn't be deleted
                        Toast.makeText(
                            holder.itemView.context,
                            "Error deleting income: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }

        holder.editButton.setOnClickListener {
            val income = incomeList[holder.adapterPosition]
            val builder = AlertDialog.Builder(holder.itemView.context)


            // Create the layout for the dialog
            val view = LayoutInflater.from(holder.itemView.context).inflate(R.layout.updateincome_popup, null)
            builder.setView(view)

            // Set the initial values for the input fields
            view.findViewById<EditText>(R.id.textname).setText(income.name)
            view.findViewById<EditText>(R.id.textamount).setText(income.amount.toString())


            // Set up the save button
            builder.setPositiveButton("Save") { _, _ ->
                // Get the updated values from the input fields
                val updatedName = view.findViewById<EditText>(R.id.textname).text.toString()
                val updatedAmountStr = view.findViewById<EditText>(R.id.textamount).text.toString()
                val updatedAmount = updatedAmountStr.toDouble()


                // Update the user's details in the database
                val incomeId = income.id
                val db = FirebaseFirestore.getInstance()
                val incomeCollectionRef = db.collection("incomes")
                val incomeDocRef = incomeId?.let { it1 -> incomeCollectionRef.document(it1) }
                if (incomeDocRef != null) {
                    incomeDocRef.update(mapOf(
                        "name" to updatedName,
                        "amount" to updatedAmount,

                        ))
                        .addOnSuccessListener {
                            // Show a success message if the user's details are updated
                            Toast.makeText(holder.itemView.context, "income details updated", Toast.LENGTH_SHORT).show()

                            // Update the user's details in the local list and refresh the adapter
                            income.name = updatedName
                            income.amount = updatedAmount

                            notifyItemChanged(holder.adapterPosition)
                        }
                        .addOnFailureListener { e ->
                            // Show an error message if the user's details couldn't be updated
                            Toast.makeText(holder.itemView.context, "Error updating income details: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }

            // Set up the cancel button
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

            // Show the dialog
            val dialog = builder.create()
            dialog.show()
        }

    }


    override fun getItemCount(): Int {
        return incomeList.size
    }

    fun setIncome(expensesList: List<Income>) {
        this.incomeList.clear()
        this.incomeList.addAll(expensesList)
        notifyDataSetChanged()
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val amount: TextView = itemView.findViewById(R.id.amount)
        val date: TextView = itemView.findViewById(R.id.date)
        val deleteButton: Button = itemView.findViewById(R.id.delete_button)
        val editButton: Button = itemView.findViewById(R.id.edit_button)

    }


}
