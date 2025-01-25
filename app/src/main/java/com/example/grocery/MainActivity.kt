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
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private lateinit var groceryViewModel: GroceryViewModel

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
            addItem(it)
        }

        val myApp = application as MyApp
        groceryViewModel = ViewModelProvider(this, GroceryViewModelFactory(myApp.repository))
            .get(GroceryViewModel::class.java)

        groceryViewModel.allItems.observe(this) { groceryItems -> }
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

        private fun addItem(view: View) {
        val itemName = editTextItem.text.toString()
        val star = editstar.text.toString().toDoubleOrNull() ?: 0.0
        val dmart = editdmart.text.toString().toDoubleOrNull() ?: 0.0
        val flipkart = editflipkart.text.toString().toDoubleOrNull() ?: 0.0
        val bb = editbb.text.toString().toDoubleOrNull() ?: 0.0
        val itemQty = editTextQty.text.toString().toDoubleOrNull() ?: 0.0

        if (itemName.isBlank() || star == null || dmart == null || flipkart == null || bb == null || itemQty == null) {
            showSnackbar("Please enter all details")
            return
        }

        if (grocerySet.any { it.name.equals(itemName, ignoreCase = true) }) {
            showSnackbar("Item '$itemName' already exists")
            return
        }

        // Create a new GroceryItem
        val newItem = GroceryItem(1,itemName, star, dmart, flipkart, bb, itemQty)

        // Create a new GroceryItemEntity
        val newItemEntity = GroceryItemEntity(0, itemName, star, dmart, flipkart, bb, itemQty)

        // Insert the new item into the database
        groceryViewModel.insert(newItemEntity)

        // Add the new item to the grocerySet
        grocerySet.add(newItem)

        // Call the display function to refresh the UI
        displayGroceryList()

        // Hide the soft keyboard
        hideKeyboard()

        // Clear the input fields
        clearInputFields()

        // Display a success message using Snackbar
        showSnackbar("Item added successfully")
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    private fun clearInputFields() {
        editTextItem.text.clear()
        editstar.text.clear()
        editdmart.text.clear()
        editflipkart.text.clear()
        editbb.text.clear()
        editTextQty.text.clear()
    }

    private fun displayGroceryList() {
        tableLayout.removeAllViews()

        // Header row
        val headerRow = TableRow(this)
        headerRow.addView(createTextView("Item", true, true))
        headerRow.addView(createTextView("Star", false, true))
        headerRow.addView(createTextView("DMart", false, true))
        headerRow.addView(createTextView("Flipkart", false, true))
        headerRow.addView(createTextView("BB", false, true))
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

            // Set OnClickListener for row selection
            dataRow.setOnClickListener {
                // Toggle the selection state
                item.isSelected = !item.isSelected
                // Update UI based on the selection state (you can change row color, etc.)
                updateRowSelection(dataRow, item.isSelected)
            }
            tableLayout.addView(dataRow)
        }
    }

    private fun updateRowSelection(row: TableRow, isSelected: Boolean) {
        // Update UI based on the selection state (you can change row color, etc.)
        if (isSelected) {
            row.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray))
        } else {
            row.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white))
        }
    }

    private fun clearSelectedRows() {
        // Create a copy of the set to avoid concurrent modification issues
        val selectedItems = grocerySet.filter { it.isSelected }.toSet()

        // Remove the selected items from the set
        grocerySet.removeAll(selectedItems)

        // Call the display function to refresh the UI
        displayGroceryList()
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
            textView.gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL
        }
        container.addView(textView)
        return container
    }

    fun reset(view: View) {
        if (grocerySet.isNotEmpty()) {
            grocerySet.clear()
            displayGroceryList()
            clearInputFields()
            showSnackbar("List reset successfully")
        }
        else {
            showSnackbar("No data available to reset")
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(
            findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }
}

data class GroceryItem(
    val id: Int,
    val name: String,
    val star: Double,
    val dmart: Double,
    val flipkart: Double?,
    val bb: Double?,
    val Qty: Double,
    var isSelected: Boolean = false
)