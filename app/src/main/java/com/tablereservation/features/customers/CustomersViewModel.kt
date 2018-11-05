package com.tablereservation.features.customers

import androidx.lifecycle.MutableLiveData
import com.tablereservation.core.interactor.UseCase
import com.tablereservation.core.platform.BaseViewModel
import kotlinx.coroutines.cancel
import javax.inject.Inject

class CustomersViewModel
@Inject constructor(private val getCustomers: GetCustomersUseCase) : BaseViewModel() {

    var customers: MutableLiveData<List<Customer>> = MutableLiveData()

    fun loadCustomers() = getCustomers(UseCase.None()) { it.either(::handleFailure, ::handleCustomersList) }

    private fun handleCustomersList(customers: List<Customer>) {
        this.customers.value = customers.map { it }
    }

    override fun onCleared() {
        super.onCleared()
        getCustomers.coroutineContext.cancel()
    }
}