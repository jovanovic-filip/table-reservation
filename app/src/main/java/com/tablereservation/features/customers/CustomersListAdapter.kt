package com.tablereservation.features.customers

import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.tablereservation.R
import com.tablereservation.core.extension.inflate
import kotlinx.android.synthetic.main.item_customer.view.*
import javax.inject.Inject
import kotlin.properties.Delegates

class CustomersListAdapter @Inject constructor() : RecyclerView.Adapter<CustomersListAdapter.CustomerHolder>() {

    internal var items: List<Customer> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerHolder {
        val view = parent.inflate(R.layout.item_customer)
        return CustomerHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CustomerHolder, position: Int) {
        val customer = items[position]
        val displayNme = "${customer.customerFirstName} ${customer.customerLastName}"
        holder.itemView.customerName.text = displayNme
        holder.itemView.setOnClickListener {
            val action =
                CustomersListFragmentDirections.openTableSelection(customer.id)
            it.findNavController().navigate(action)
        }
    }

    class CustomerHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view)
}