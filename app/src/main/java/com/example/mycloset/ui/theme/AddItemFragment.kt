package com.example.mycloset.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.mycloset.R
import com.example.mycloset.database.AppDatabase
import com.example.mycloset.database.ClothingColor
import com.example.mycloset.database.ClothingType
import com.example.mycloset.database.Item
import com.example.mycloset.database.Occasion
import kotlinx.coroutines.launch

class AddItemFragment : Fragment() {

    private var itemId: Int? = null
    private var existingItem: Item? = null

    private lateinit var nameInput: EditText
    private lateinit var typeSpinner: Spinner
    private lateinit var colorSpinner: Spinner
    private lateinit var occasionSpinner: Spinner
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var imageView: ImageView
    private lateinit var selectImageButton: Button

    private var selectedImageUri: Uri? = null

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            if (uri != null) {
                //ask android to keep this permission even after app restarts
                val contentResolver = requireContext().contentResolver
                val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                try {
                    contentResolver.takePersistableUriPermission(uri, takeFlags)
                } catch (e: SecurityException) {
                    //if we already hve it, this can throw, safe to ignore
                }
            selectedImageUri = uri
            imageView.setImageURI(uri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemId = arguments?.getInt(ARG_ITEM_ID)?.takeIf { it != 0 }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameInput = view.findViewById(R.id.inputName)
        typeSpinner = view.findViewById(R.id.spinnerType)
        colorSpinner = view.findViewById(R.id.spinnerColor)
        occasionSpinner = view.findViewById(R.id.spinnerOccasion)
        saveButton = view.findViewById(R.id.addSaveButton)
        cancelButton = view.findViewById(R.id.addCancelButton)
        imageView = view.findViewById(R.id.imageItem)
        selectImageButton = view.findViewById(R.id.buttonSelectImage)
        //default placeholder for image
        imageView.setImageResource(R.drawable.catinashirt)

        // Set up adapters using enums
        typeSpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            ClothingType.values()
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        colorSpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            ClothingColor.values()
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        occasionSpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            Occasion.values()
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        selectImageButton.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
        }

        val db = AppDatabase.getDatabase(requireContext())
        val dao = db.itemDao()

        // If we have an itemId, we're in EDIT mode → load the item and prefill UI
        val id = itemId
        if (id != null) {
            viewLifecycleOwner.lifecycleScope.launch {
                val item = dao.getItemById(id)
                if (item == null) {
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), "Item not found", Toast.LENGTH_SHORT)
                            .show()
                        navigateBack()
                    }
                } else {
                    existingItem = item
                    requireActivity().runOnUiThread {
                        populateFields(item)
                    }
                }
            }
        }

        saveButton.setOnClickListener {
            val name = nameInput.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedType = typeSpinner.selectedItem as ClothingType
            val selectedColor = colorSpinner.selectedItem as ClothingColor
            val selectedOccasion = occasionSpinner.selectedItem as Occasion
            val imageUriString = selectedImageUri?.toString()

            viewLifecycleOwner.lifecycleScope.launch {
                val existing = existingItem
                if (existing == null) {
                    // ADD mode
                    val newItem = Item(
                        name = name,
                        type = selectedType,
                        color = selectedColor,
                        occasion = selectedOccasion,
                        imageUri = imageUriString
                    )
                    dao.insertItem(newItem)
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), "Item added!", Toast.LENGTH_SHORT).show()
                        navigateBack()
                    }
                } else {
                    // EDIT mode
                    val updated = existing.copy(
                        name = name,
                        type = selectedType,
                        color = selectedColor,
                        occasion = selectedOccasion,
                        imageUri = imageUriString
                    )
                    dao.updateItem(updated)
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), "Item updated!", Toast.LENGTH_SHORT)
                            .show()
                        navigateBack()
                    }
                }
            }
        }

        cancelButton.setOnClickListener {
            navigateBack()
        }
    }

    private fun populateFields(item: Item) {
        nameInput.setText(item.name)

        // set spinner selections based on existing item
        typeSpinner.setSelection(ClothingType.values().indexOf(item.type))
        colorSpinner.setSelection(ClothingColor.values().indexOf(item.color))
        occasionSpinner.setSelection(Occasion.values().indexOf(item.occasion))

        val uriString = item.imageUri
        if (uriString != null) {
            val uri = Uri.parse(uriString)
            try {
                // Try to show the saved image
                selectedImageUri = uri
                imageView.setImageURI(uri)
            } catch (e: SecurityException) {
                // We don't have permission anymore (old picker URI, etc.)
                selectedImageUri = null
                imageView.setImageResource(R.drawable.catinashirt)
            } catch (e: Exception) {
                // Any other error decoding / loading -> fallback
                selectedImageUri = null
                imageView.setImageResource(R.drawable.catinashirt)
            }
        } else {
            // No image saved for this item
            selectedImageUri = null
            imageView.setImageResource(R.drawable.catinashirt)
        }
    }

    private fun navigateBack() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    companion object {
        private const val ARG_ITEM_ID = "item_id"

        fun newInstanceForEdit(itemId: Int): AddItemFragment {
            val fragment = AddItemFragment()
            fragment.arguments = Bundle().apply {
                putInt(ARG_ITEM_ID, itemId)
            }
            return fragment
        }
    }
}

