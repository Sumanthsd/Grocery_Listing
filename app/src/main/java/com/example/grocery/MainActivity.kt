package com.example.grocery

import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    private val grocerySet = mutableSetOf<GroceryItem>()

    private lateinit var tableLayout: TableLayout
    private lateinit var editTextItem: EditText
    private lateinit var editstar: EditText
    private lateinit var editdmart: EditText
    private lateinit var editflipkart: EditText
    private lateinit var editbb: EditText
    private lateinit var editTextQty: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tableLayout = findViewById(R.id.tableLayout)
        editTextItem = findViewById(R.id.editTextItem)
        editstar = findViewById(R.id.editstar)
        editdmart = findViewById(R.id.editdmart)
        editflipkart = findViewById(R.id.editflipkart)
        editbb = findViewById(R.id.editbb)
        editTextQty = findViewById(R.id.editTextQty)

        editTextItem.setOnEditorActionListener(editorActionListener)
        editstar.setOnEditorActionListener(editorActionListener)
        editdmart.setOnEditorActionListener(editorActionListener)
        editflipkart.setOnEditorActionListener(editorActionListener)
        editbb.setOnEditorActionListener(editorActionListener)
        editTextQty.setOnEditorActionListener(editorActionListener)

        val btnAddItem: Button = findViewById(R.id.btnAddItem)
        btnAddItem.setOnClickListener {
            addItem()
        }
    }

    private val editorActionListener = TextView.OnEditorActionListener { _, actionId, event ->
        when {
            actionId == EditorInfo.IME_ACTION_NEXT || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) -> {
                // Move focus to the next EditText field
                when {
                    editTextItem.isFocused -> editstar.requestFocus()
                    editstar.isFocused -> editdmart.requestFocus()
                    editdmart.isFocused -> editflipkart.requestFocus()
                    editflipkart.isFocused -> editbb.requestFocus()
                    editbb.isFocused -> editTextQty.requestFocus()
                    editTextQty.isFocused -> {
                        addItem(editTextQty)
                        return@OnEditorActionListener true
                    }
                }
                return@OnEditorActionListener true
            }
            else -> false
        }
    }

        private fun addItem(view: android.view.View) {
        val itemName = editTextItem.text.toString()
        val star = editstar.text.toString().toDoubleOrNull() ?: 0.0
        val dmart = editdmart.text.toString().toDoubleOrNull() ?: 0.0
        val flipkart = editflipkart.text.toString().toDoubleOrNull() ?: 0.0
        val bb = editbb.text.toString().toDoubleOrNull() ?: 0.0
        val itemQty = editTextQty.text.toString().toDoubleOrNull() ?: 0.0

        if (itemName.isBlank() || star == null || dmart == null || itemQty == null) {
            // Display an error message using Snackbar
            showSnackbar("Please enter all details")
            return
        }
        // Create a new GroceryItem
        val newItem = GroceryItem(itemName, star, dmart, flipkart, bb, itemQty)

        // Add the new item to the grocerySet
        grocerySet.add(newItem)

        // Call the display function to refresh the UI
        displayGroceryList(showDetails = true)
    }

    fun addItem() {
        val itemName = editTextItem.text.toString()
        val star = editstar.text.toString().toDoubleOrNull()
        val dmart = editdmart.text.toString().toDoubleOrNull()
        val flipkart = editflipkart.text.toString().toDoubleOrNull()
        val bb = editbb.text.toString().toDoubleOrNull()
        val Qty = editTextQty.text.toString().toDoubleOrNull()

        if (itemName.isBlank() || star == null || dmart == null || Qty == null) {
            // Display an error message using Snackbar
            showSnackbar("Please enter all details")
            return
        }

        // Create a new GroceryItem
        val newItem = GroceryItem(itemName, star, dmart, flipkart, bb, Qty)

        grocerySet.add(newItem)

        // Call the display function to refresh the UI
        displayGroceryList(showDetails = true)

        // Clear the input fields
        clearInputFields()

        // Display a success message using Snackbar
        showSnackbar("Item added successfully")

        if (!itemName.isBlank() && star != null && dmart != null && Qty != null) {
            val groceryItem = GroceryItem(itemName, star, dmart, flipkart, bb, Qty)
            grocerySet.add(groceryItem)
            displayGroceryList(showDetails = true)
        }
    }

    private fun clearInputFields() {
        // Clear the input fields
        editTextItem.text.clear()
        editstar.text.clear()
        editdmart.text.clear()
        editflipkart.text.clear()
        editbb.text.clear()
        editTextQty.text.clear()
    }

    private fun displayGroceryList(showDetails: Boolean) {
        tableLayout.removeAllViews()

        // Header row
        val headerRow = TableRow(this)
        headerRow.addView(createTextView("Item", true, true))
        headerRow.addView(createTextView("star", false, true))
        headerRow.addView(createTextView("dmart", false, true))
        headerRow.addView(createTextView("flipkart", false, true))
        headerRow.addView(createTextView("bb", false, true))
        headerRow.addView(createTextView("Qty", false, true))

        // Set border background for headerRow
        headerRow.background = ContextCompat.getDrawable(this, R.drawable.borders)

        tableLayout.addView(headerRow)

        for (item in grocerySet) {
            val dataRow = TableRow(this)

            // Set border background for dataRow
            dataRow.background = ContextCompat.getDrawable(this, R.drawable.borders)

            val itemNameTextView = createTextView(item.name, true, true)
            val starTextView = createTextView(item.star.toString(), false, true)
            val dmartTextView = createTextView(item.dmart.toString(), false, true)
            val flipkartTextView = createTextView(item.flipkart.toString(), false, true)
            val bbTextView = createTextView(item.bb.toString(), false, true)
            val QtyTextView = createTextView(item.Qty.toString(), false, true)

            dataRow.addView(itemNameTextView)
            dataRow.addView(starTextView)
            dataRow.addView(dmartTextView)
            dataRow.addView(flipkartTextView)
            dataRow.addView(bbTextView)
            dataRow.addView(QtyTextView)

            tableLayout.addView(dataRow)
        }
    }


    private fun createTextView(text: String,  isCheckBoxColumn: Boolean, isHeader: Boolean): View {
        val container = LinearLayout(this)
        container.orientation = LinearLayout.HORIZONTAL

        if (isCheckBoxColumn) {
            val checkBox = CheckBox(this)
            checkBox.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            container.addView(checkBox)
        }

        val textView = TextView(this)
        textView.text = text
        textView.layoutParams = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            if (isCheckBoxColumn) 1f else 0.75f
        )

        // Adjust the dimension resource as needed
        textView.width = resources.getDimensionPixelSize(R.dimen.column_width)

        if (isHeader) {
            textView.gravity = Gravity.CENTER
        } else {
            textView.gravity = Gravity.START or Gravity.CENTER_VERTICAL
        }

        val tableRow = TableRow(this)
        tableRow.layoutParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.WRAP_CONTENT
        )
        textView.gravity = Gravity.START or Gravity.CENTER_VERTICAL

        container.addView(textView)
        return container
    }

    fun reset(view: View) {
        // Clear the grocerySet and update the UI
        grocerySet.clear()
        displayGroceryList(showDetails = true)

        // Clear the input fields
        clearInputFields()

        // Display a message using Snackbar
        showSnackbar("List reset successfully")
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(
            findViewById(android.R.id.content),  // Use the root view of the activity
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }
}

data class GroceryItem(
    val name: String,
    val star: Double,
    val dmart: Double,
    val flipkart: Double?,
    val bb: Double?,
    val Qty: Double,
)