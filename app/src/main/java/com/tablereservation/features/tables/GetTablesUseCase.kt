package com.tablereservation.features.tables

import androidx.lifecycle.LiveData
import com.tablereservation.core.exception.Failure
import com.tablereservation.core.functional.Either
import com.tablereservation.core.interactor.UseCase
import com.tablereservation.repository.Repository
import javax.inject.Inject

class GetTablesUseCase
@Inject constructor(private val repository: Repository) : UseCase<LiveData<List<TableEntity>>, UseCase.None>() {

    @Suppress("UNCHECKED_CAST")
    override suspend fun run(params: None) : Either<Failure, LiveData<List<TableEntity>>> {
        return repository.tables()
    }
}