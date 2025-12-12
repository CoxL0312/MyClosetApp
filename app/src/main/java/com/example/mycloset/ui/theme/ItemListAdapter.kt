package com.example.mycloset.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mycloset.R
import com.example.mycloset.database.Item

class ItemListAdapter(
    private val onItemClick: (Item) -> Unit
) : RecyclerView.Adapter<ItemListAdapter.ItemViewHolder>() {

    private val items = mutableListOf<Item>()

    fun submitList(newItems: List<Item>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.textItemName)
        val metaText: TextView = itemView.findViewById(R.id.textItemMeta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.nameText.text = item.name
        holder.metaText.text =
            "${item.type.displayName} • ${item.color.displayName} • ${item.occasion.displayName}"

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }
}
