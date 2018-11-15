package com.tablereservation.features.reservation

import com.tablereservation.MAX_NUMBER_OF_TABLES_PER_CUSTOMER
import com.tablereservation.UnitTest
import com.tablereservation.core.exception.Failure
import com.tablereservation.core.functional.Either
import com.tablereservation.features.tables.TableEntity
import com.tablereservation.repository.Repository
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import com.tablereservation.core.interactor.TestUseCaseContextProvider
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class UpdateReservationUseCaseTest : UnitTest() {

    private lateinit var createReservationUseCase : UpdateReservationUseCase
    @Mock
    private lateinit var repository: Repository

    private val CUSTOMER_ID = 1
    private val OTHER_CUSTOMER_ID = 2
    private val TABLE_ID = 1
    private val tableEntity = TableEntity(TABLE_ID,true,-1)
    private val otherCustomerTable = TableEntity(TABLE_ID,true,OTHER_CUSTOMER_ID)
    private val takenTable = TableEntity(TABLE_ID,false,-1)
    private val ownReservation = Reservation(TABLE_ID, CUSTOMER_ID)

    @Before
    fun setUp(){
        createReservationUseCase = UpdateReservationUseCase(repository)
        createReservationUseCase.contextProvider = TestUseCaseContextProvider()
    }

    @Test
    fun `new reservation is being saved`(){
        given {repository.getReservations(CUSTOMER_ID)}.willReturn(emptyList())
        given {repository.getTable(TABLE_ID)}.willReturn(tableEntity)
        runBlocking { createReservationUseCase.run(
            UpdateReservationUseCase.Params(TABLE_ID, CUSTOMER_ID))
        }
        verify(repository).saveReservation(ownReservation)
    }

    @Test
    fun `existing reservation is being deleted`(){
        given {repository.getReservations(CUSTOMER_ID)}.willReturn(listOf(ownReservation))
        given {repository.getTable(TABLE_ID)}.willReturn(tableEntity)
        val result = runBlocking { createReservationUseCase.run(
            UpdateReservationUseCase.Params(TABLE_ID, CUSTOMER_ID))
        }
        verify(repository).deleteReservation(TABLE_ID)
        result shouldBeInstanceOf Either::class.java
        result.isLeft shouldEqual true
        result.either({ failure -> failure shouldBeInstanceOf Failure.DeletedReservation::class.java }, {})
    }

    @Test
    fun `when reservation that belongs to other customer is changed, failure occurs`(){
        given {repository.getReservations(CUSTOMER_ID)}.willReturn(emptyList())
        given {repository.getTable(TABLE_ID)}.willReturn(otherCustomerTable)
        val result = runBlocking { createReservationUseCase.run(
            UpdateReservationUseCase.Params(TABLE_ID, CUSTOMER_ID))
        }
        result shouldBeInstanceOf Either::class.java
        result.isLeft shouldEqual true
        result.either({ failure -> failure shouldBeInstanceOf Failure.ExistingReservation::class.java }, {})
    }

    @Test
    fun `when table marked as not available is reserved, failure occurs`(){
        given {repository.getReservations(CUSTOMER_ID)}.willReturn(emptyList())
        given {repository.getTable(TABLE_ID)}.willReturn(takenTable)
        val result = runBlocking { createReservationUseCase.run(
            UpdateReservationUseCase.Params(TABLE_ID, CUSTOMER_ID))
        }
        result shouldBeInstanceOf Either::class.java
        result.isLeft shouldEqual true
        result.either({ failure -> failure shouldBeInstanceOf Failure.ExistingReservation::class.java }, {})
    }

    @Test
    fun `when limit for a maximum number of reservation is reached failure occurs`(){
        val listOfReservations = ArrayList<Reservation>()
        repeat(MAX_NUMBER_OF_TABLES_PER_CUSTOMER){
            listOfReservations.add(ownReservation)
        }
        given {repository.getReservations(CUSTOMER_ID)}.willReturn(listOfReservations)
        val result = runBlocking { createReservationUseCase.run(
            UpdateReservationUseCase.Params(TABLE_ID, CUSTOMER_ID))
        }
        result shouldBeInstanceOf Either::class.java
        result.isLeft shouldEqual true
        result.either({ failure -> failure shouldBeInstanceOf Failure.MaxNumberReached::class.java }, {})
    }
}
