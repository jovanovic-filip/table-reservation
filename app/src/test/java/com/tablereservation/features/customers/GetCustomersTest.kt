package com.tablereservation.features.customers

import com.tablereservation.UnitTest
import com.tablereservation.core.functional.Either
import com.tablereservation.core.interactor.UseCase
import com.tablereservation.repository.Repository
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class GetCustomersTest: UnitTest() {

    private lateinit var getCustomersUseCase: GetCustomersUseCase

    @Mock private lateinit var repository: Repository

    @Before
    fun setUp() {
        getCustomersUseCase = GetCustomersUseCase(repository)
        getCustomersUseCase.setupForUnitTests()
        given { repository.customers() }.willReturn(Either.Right(ArrayList()))
    }

    @Test
    fun `GetCustomersUseCase should get customers from repository`(){
        runBlocking { getCustomersUseCase.run(UseCase.None()) }
        verify(repository).customers()
        verifyNoMoreInteractions(repository)
    }

}