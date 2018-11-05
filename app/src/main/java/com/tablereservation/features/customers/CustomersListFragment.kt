package com.tablereservation.features.customers

import androidx.lifecycle.MutableLiveData
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tablereservation.R
import com.tablereservation.core.extension.observe
import com.tablereservation.core.extension.failure
import com.tablereservation.core.extension.viewModel
import com.tablereservation.core.platform.BaseFragment
import com.tablereservation.core.exception.Failure
import com.tablereservation.core.extension.showSnackbar
import kotlinx.android.synthetic.main.fragment_customer_list.*
import javax.inject.Inject

class CustomersListFragment : BaseFragment() {

    override fun layoutId(): Int {
        return R.layout.fragment_customer_list
    }

    private lateinit var customersViewModel: CustomersViewModel
    @Inject lateinit var customersAdapter: CustomersListAdapter

    override fun onCreate(savedInstanceState : Bundle?)  {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        customersViewModel = viewModel(viewModelFactory) {
            observe<List<Customer>, MutableLiveData<List<Customer>>>(customers, ::renderCustomersList)
            failure(failure, ::handleFailure)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customersList.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL,false)
        customersList.adapter = customersAdapter
        customersViewModel.loadCustomers()
        showProgress()
    }

    private fun renderCustomersList(list: List<Customer>?) {
        customersAdapter.items = list.orEmpty()
        hideProgress()
    }

    private fun handleFailure(failure: Failure?) {
        hideProgress()
        when (failure) {
            is Failure.NetworkConnection -> {
                showSnackbar(R.string.network_error_message)
            }
            is Failure.ServerError -> {
                showSnackbar(R.string.server_error_message)
            }
        }
    }
}