package com.example.mycloset.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycloset.R
import com.example.mycloset.database.AppDatabase
import com.example.mycloset.database.ClothingColor
import com.example.mycloset.database.ClothingType
import com.example.mycloset.database.Item
import com.example.mycloset.database.Occasion
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ItemListFragment : Fragment() {

    private lateinit var adapter: ItemListAdapter
    private lateinit var typeFilterSpinner: Spinner
    private lateinit var colorFilterSpinner: Spinner
    private lateinit var occasionFilterSpinner: Spinner
    private lateinit var emptyText: TextView
    private lateinit var recycler: RecyclerView
    private lateinit var searchInput: EditText

    private var allItems: List<Item> = emptyList()
    private var searchQuery: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_item_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addButton = view.findViewById<Button>(R.id.buttonAddItem)
        recycler = view.findViewById(R.id.recyclerItems)
        emptyText = view.findViewById(R.id.textEmpty)
        searchInput = view.findViewById(R.id.editSearchName)

        typeFilterSpinner = view.findViewById(R.id.spinnerFilterType)
        colorFilterSpinner = view.findViewById(R.id.spinnerFilterColor)
        occasionFilterSpinner = view.findViewById(R.id.spinnerFilterOccasion)

        //RecyclerView + adapter
        adapter = ItemListAdapter { item ->
            // On click → detail
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ItemDetailFragment.newInstance(item.id))
                .addToBackStack(null)
                .commit()
        }

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        setupFilterSpinners()
        setupSearchInput()

        val db = AppDatabase.getDatabase(requireContext())
        val itemDao = db.itemDao()

        // Collect items from DB
        viewLifecycleOwner.lifecycleScope.launch {
            itemDao.getAllItems().collectLatest { items ->
                allItems = items
                applyFiltersAndUpdate()
            }
        }

        addButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, AddItemFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupSearchInput() {
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int ) {
                //no op
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //no op
            }
            override fun afterTextChanged(s: Editable?) {
                searchQuery = s?.toString() ?: ""
                applyFiltersAndUpdate()
            }
        })
    }

    private fun setupFilterSpinners() {
        // Type: "All" + each ClothingType
        val typeOptions = listOf("All Types") + ClothingType.values().map { it.displayName }
        typeFilterSpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            typeOptions
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        // Color: "All" + each ClothingColor
        val colorOptions = listOf("All Colors") + ClothingColor.values().map { it.displayName }
        colorFilterSpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            colorOptions
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        // Occasion: "All" + each Occasion
        val occasionOptions = listOf("All Occasions") + Occasion.values().map { it.displayName }
        occasionFilterSpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            occasionOptions
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                applyFiltersAndUpdate()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                applyFiltersAndUpdate()
            }
        }

        typeFilterSpinner.onItemSelectedListener = listener
        colorFilterSpinner.onItemSelectedListener = listener
        occasionFilterSpinner.onItemSelectedListener = listener
    }

    private fun getSelectedTypeFilter(): ClothingType? {
        val pos = typeFilterSpinner.selectedItemPosition
        return if (pos <= 0) null else ClothingType.values()[pos - 1]
    }

    private fun getSelectedColorFilter(): ClothingColor? {
        val pos = colorFilterSpinner.selectedItemPosition
        return if (pos <= 0) null else ClothingColor.values()[pos - 1]
    }

    private fun getSelectedOccasionFilter(): Occasion? {
        val pos = occasionFilterSpinner.selectedItemPosition
        return if (pos <= 0) null else Occasion.values()[pos - 1]
    }

    private fun applyFiltersAndUpdate() {
        val typeFilter = getSelectedTypeFilter()
        val colorFilter = getSelectedColorFilter()
        val occasionFilter = getSelectedOccasionFilter()
        val query = searchQuery.trim().lowercase()

        val filtered = allItems.filter { item ->
            (typeFilter == null || item.type == typeFilter) &&
            (colorFilter == null || item.color == colorFilter) &&
            (occasionFilter == null || item.occasion == occasionFilter) &&
            (query.isEmpty() || item.name.lowercase().contains(query))
        }

        if (filtered.isEmpty()) {
            recycler.visibility = View.GONE
            emptyText.visibility = View.VISIBLE
            emptyText.text = "No items match these filters."
        } else {
            recycler.visibility = View.VISIBLE
            emptyText.visibility = View.GONE
        }

        adapter.submitList(filtered)
    }
}


