package com.tablereservation.features.tables

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.tablereservation.R
import com.tablereservation.core.exception.Failure
import com.tablereservation.core.extension.*
import com.tablereservation.core.platform.BaseFragment
import kotlinx.android.synthetic.main.fragment_table_selection.*
import javax.inject.Inject

class TableSelectionFragment : BaseFragment() {

    override fun layoutId(): Int {
        return R.layout.fragment_table_selection
    }

    private lateinit var tablesListViewModel: TablesListViewModel
    @Inject lateinit var tablesAdapter: TablesListAdapter

    override fun onCreate(savedInstanceState : Bundle?)  {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        tablesListViewModel = viewModel(viewModelFactory) {
            observe(tables, ::renderTablesList)
            failure(failure, ::handleFailure)
        }
        tablesListViewModel.customerId = TableSelectionFragmentArgs.fromBundle(arguments).customerId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tablesAdapter.notAvailableColor = ContextCompat.getColor(appContext, R.color.colorLightPink)
        tablesAdapter.availableColor = ContextCompat.getColor(appContext, R.color.colorLightGreen)
        tablesAdapter.takenColor = ContextCompat.getColor(appContext, R.color.colorLightBlue)

        tablesAdapter.onItemClick = { tablesListViewModel.selectTable(it) }
        tablesList.layoutManager = GridLayoutManager(activity, 4)
        tablesList.adapter = tablesAdapter
        tablesListViewModel.loadData()
        showProgress()
    }

    private fun renderTablesList(list: List<DisplayTable>?) {
        tablesAdapter.items = list.orEmpty()
        hideProgress()
    }

    private fun handleFailure(failure: Failure?) {
        hideProgress()
        when (failure) {
            is Failure.ExistingReservation -> {
               showSnackbar(R.string.existing_reservation)
            }
            is Failure.DeletedReservation -> {
                showSnackbar(R.string.reservation_deleted)
            }
            is Failure.MaxNumberReached -> {
                showSnackbar(R.string.max_number_of_reservations_reached)
            }
            is Failure.NetworkConnection -> {
                showSnackbar(R.string.network_error_message)
            }
            is Failure.ServerError -> {
                showSnackbar(R.string.server_error_message)
            }
        }
    }
}
