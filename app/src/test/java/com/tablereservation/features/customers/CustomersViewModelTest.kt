package com.tablereservation.features.customers

import androidx.lifecycle.Observer
import com.tablereservation.core.functional.Either
import com.tablereservation.core.interactor.UseCase
import com.tablereservation.repository.Repository
import com.nhaarman.mockitokotlin2.given
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import java.util.Arrays.asList
import com.nhaarman.mockitokotlin2.verify
import com.tablereservation.UnitTest
import com.tablereservation.core.interactor.TestUseCaseContextProvider
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule
import org.junit.rules.TestRule


class CustomersViewModelTest : UnitTest() {

    @Rule @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var customersViewModel: CustomersViewModel
    private lateinit var getCustomersUseCase: GetCustomersUseCase

    @Mock private lateinit var repository: Repository
    @Mock private lateinit var customersObserver: Observer<List<Customer>>

    private val demoCustomer = Customer(0,"Tom","Sawyer")

    @Before
    fun setUp() {
        getCustomersUseCase = GetCustomersUseCase(repository)
        getCustomersUseCase.contextProvider = TestUseCaseContextProvider()
        customersViewModel = CustomersViewModel(getCustomersUseCase)
        given { runBlocking { getCustomersUseCase.run(UseCase.None()) } }.willReturn(Either.Right(asList(demoCustomer)))
        customersViewModel.customers.observeForever(customersObserver)
    }

    @Test
    fun `loadCustomers call should update live data`() {
        runBlocking { customersViewModel.loadCustomers() }
        verify(customersObserver).onChanged(asList(demoCustomer))
    }
}