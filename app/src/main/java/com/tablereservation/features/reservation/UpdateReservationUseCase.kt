package com.tablereservation.features.reservation

import com.tablereservation.MAX_NUMBER_OF_TABLES_PER_CUSTOMER
import com.tablereservation.core.exception.Failure
import com.tablereservation.core.functional.Either
import com.tablereservation.core.interactor.UseCase
import com.tablereservation.repository.Repository
import javax.inject.Inject

class UpdateReservationUseCase
@Inject constructor(private val repository: Repository) : UseCase<Unit, UpdateReservationUseCase.Params>() {

    override suspend fun run(params: Params) : Either<Failure, Unit>{
        val customerReservations= repository.getReservations(params.customerId)
        val selectedTable = repository.getTable(params.tableId)

        if(customerReservations.size>=MAX_NUMBER_OF_TABLES_PER_CUSTOMER){
            return Either.Left(Failure.MaxNumberReached())
        }
        customerReservations.forEach {
            if(it.customerId == params.customerId && it.tableId == params.tableId){
                repository.deleteReservation(selectedTable.tableId)
                return Either.Left(Failure.DeletedReservation())
            }
        }
        return if(selectedTable.reservedForCustomerId >= 0 || !selectedTable.available) {
            Either.Left(Failure.ExistingReservation())
        } else {
            repository.saveReservation(Reservation(params.tableId, params.customerId))
            Either.Right(Unit)
        }
    }

    data class Params(val tableId: Int, val customerId: Int)
}