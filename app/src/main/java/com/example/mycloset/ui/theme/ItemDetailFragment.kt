package com.example.mycloset.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.mycloset.R
import com.example.mycloset.database.AppDatabase
import com.example.mycloset.database.Item
import kotlinx.coroutines.launch

class ItemDetailFragment : Fragment() {

    private var itemId: Int = 0
    private var currentItem: Item? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemId = requireArguments().getInt(ARG_ITEM_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_item_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameText = view.findViewById<TextView>(R.id.textDetailName)
        val typeText = view.findViewById<TextView>(R.id.textDetailType)
        val colorText = view.findViewById<TextView>(R.id.textDetailColor)
        val occasionText = view.findViewById<TextView>(R.id.textDetailOccasion)
        val imageView = view.findViewById<ImageView>(R.id.imageDetailItem)

        val deleteButton = view.findViewById<Button>(R.id.buttonDelete)
        val editButton = view.findViewById<Button>(R.id.buttonEdit)
        val backButton = view.findViewById<Button>(R.id.buttonBack)

        val db = AppDatabase.getDatabase(requireContext())
        val dao = db.itemDao()



        // Load item
        viewLifecycleOwner.lifecycleScope.launch {
            val item = dao.getItemById(itemId)
            if (item == null) {
                Toast.makeText(requireContext(), "Item not found", Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressedDispatcher.onBackPressed()
            } else {
                currentItem = item
                nameText.text = item.name
                typeText.text = "Type: ${item.type.displayName}"
                colorText.text = "Color: ${item.color.displayName}"
                occasionText.text = "Occasion: ${item.occasion.displayName}"

                if (item.imageUri != null) {
                    val uri = Uri.parse(item.imageUri)
                    try {
                        imageView.setImageURI(uri)
                    } catch (e: SecurityException) {
                        imageView.setImageResource(R.drawable.catinashirt)
                    } catch (e: Exception) {
                        imageView.setImageResource(R.drawable.catinashirt)
                    }
                } else {
                    // No image URI at all
                    imageView.setImageResource(R.drawable.catinashirt)
                }
            }
        }

        deleteButton.setOnClickListener {
            val item = currentItem ?: return@setOnClickListener
            viewLifecycleOwner.lifecycleScope.launch {
                dao.deleteItem(item)
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Item deleted", Toast.LENGTH_SHORT).show()
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }

        editButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, AddItemFragment.newInstanceForEdit(itemId))
                .addToBackStack(null)
                .commit()
        }


        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    companion object {
        private const val ARG_ITEM_ID = "item_id"

        fun newInstance(itemId: Int): ItemDetailFragment {
            val fragment = ItemDetailFragment()
            fragment.arguments = Bundle().apply {
                putInt(ARG_ITEM_ID, itemId)
            }
            return fragment
        }
    }
}
