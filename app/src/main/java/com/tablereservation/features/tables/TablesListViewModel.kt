package com.tablereservation.features.tables

import androidx.lifecycle.*
import com.tablereservation.core.interactor.UseCase
import com.tablereservation.core.platform.BaseViewModel
import com.tablereservation.features.customers.Customer
import com.tablereservation.features.customers.GetCustomersUseCase
import com.tablereservation.features.reservation.UpdateReservationUseCase
import kotlinx.coroutines.cancel
import javax.inject.Inject

class TablesListViewModel
@Inject constructor(
    private val getTables: GetTablesUseCase,
    private val getCustomers: GetCustomersUseCase,
    private val createReservation : UpdateReservationUseCase
) : BaseViewModel() {

    private lateinit var customers: List<Customer>
    var tables = MediatorLiveData<List<DisplayTable>>()
    var customerId: Int = -1

    fun loadData() {
        getCustomers(UseCase.None()) { it.either(::handleFailure, ::handleCustomersList) }
    }

    fun selectTable(tableId : Int) = createReservation(UpdateReservationUseCase.Params(tableId,customerId))
    { it.either(::handleFailure) {} }

    private fun handleCustomersList(customers: List<Customer>) {
        this.customers = customers
        getTables(UseCase.None()) { it.either(::handleFailure, ::handleTablesList) }
    }

    private fun handleTablesList(liveTables: LiveData<List<TableEntity>>) {
        tables.addSource(liveTables) {
           tables.postValue(it.map { table -> transformToDisplayTable(table,customers) })
        }
    }

    private fun extractFirstLetters(words : List<String?>) : String {
        var result = ""
        for(word in words){
            if(word!=null && word.isNotEmpty()){
                result += word[0].toUpperCase()
                if(words.lastIndexOf(word)<words.size-1){
                    result += " "
                }
            }
        }
        return result
    }

    private fun transformToDisplayTable(table : TableEntity, customers : List<Customer>?) : DisplayTable {
        val customer = customers?.find { it.id == table.reservedForCustomerId}
        return DisplayTable(
            table.tableId,
            table.available && table.reservedForCustomerId<0,
            extractFirstLetters(listOf(customer?.customerFirstName,customer?.customerLastName))
        )
    }

    override fun onCleared() {
        super.onCleared()
        getCustomers.cancel()
        getTables.cancel()
        createReservation.cancel()
    }
}