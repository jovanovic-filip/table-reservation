package com.tablereservation.features.tables

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tablereservation.R
import com.tablereservation.core.extension.inflate
import kotlinx.android.synthetic.main.item_table.view.*
import javax.inject.Inject
import kotlin.properties.Delegates

class TablesListAdapter @Inject constructor() : RecyclerView.Adapter<TablesListAdapter.TableHolder>() {

    var availableColor : Int = 0
    var notAvailableColor : Int = 0
    var takenColor : Int = 0

    internal var items: List<DisplayTable> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }
    lateinit var onItemClick: (tableId : Int) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableHolder {
        val view = parent.inflate(R.layout.item_table)
        return TableHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TableHolder, position: Int) {
        val table = items[position]

        if(table.customerInitials.isBlank()){
            val displayText = "No ${table.tableId}"
            holder.itemView.tableName.text = displayText
            if(table.available){
                holder.itemView.setBackgroundColor(availableColor)
            } else {
                holder.itemView.setBackgroundColor(notAvailableColor)
            }
        } else {
            holder.itemView.tableName.text = table.customerInitials
            holder.itemView.setBackgroundColor(takenColor)
        }

        holder.itemView.setOnClickListener {
            onItemClick(table.tableId)
        }
    }
    class TableHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view)
}