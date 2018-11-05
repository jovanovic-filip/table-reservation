package com.tablereservation.features.customers

import com.tablereservation.core.exception.Failure
import com.tablereservation.core.functional.Either
import com.tablereservation.core.interactor.UseCase
import com.tablereservation.repository.Repository
import javax.inject.Inject

open class GetCustomersUseCase
@Inject constructor(private val repository: Repository) : UseCase<List<Customer>, UseCase.None>() {

    @Suppress("UNCHECKED_CAST")
    override suspend fun run(params: None) : Either<Failure, List<Customer>> {
        return repository.customers()
    }
}