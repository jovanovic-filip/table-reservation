package com.tablereservation.features.reservation

import com.tablereservation.core.exception.Failure
import com.tablereservation.core.functional.Either
import com.tablereservation.core.interactor.UseCase
import com.tablereservation.repository.Repository
import javax.inject.Inject

class ClearReservationsUseCase
@Inject constructor(private val repository: Repository) : UseCase<UseCase.None, UseCase.None>() {

    override suspend fun run(params: None): Either<Failure, UseCase.None> {
        return repository.clearReservations()
    }
}