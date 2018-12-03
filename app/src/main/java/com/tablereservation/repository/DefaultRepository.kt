package com.tablereservation.repository

import androidx.lifecycle.LiveData
import com.tablereservation.core.exception.Failure
import com.tablereservation.core.functional.Either
import com.tablereservation.core.interactor.UseCase
import com.tablereservation.features.customers.Customer
import com.tablereservation.features.reservation.Reservation
import com.tablereservation.features.tables.TableEntity
import com.tablereservation.repository.database.ReservationsDao
import com.tablereservation.repository.network.TablesAndUsersService
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class DefaultRepository
@Inject constructor(private val reservationService: TablesAndUsersService, private val dao: ReservationsDao) : Repository {

    override fun getTable(tableId: Int): TableEntity {
        return dao.getTableByTableId(tableId)
    }

    override fun getReservations(customerId: Int): List<Reservation> {
        return dao.getReservationsByCustomer(customerId)
    }

    override fun deleteReservation(tableId: Int) {
        dao.deleteReservation(tableId)
    }

    override fun saveReservation(reservation: Reservation) {
        dao.insertReservation(reservation.tableId, reservation.customerId)
    }

    override fun clearReservations(): Either<Failure, UseCase.None> {
        dao.deleteAllReservations()
        return Either.Right(UseCase.None())
    }


    @Suppress("UNCHECKED_CAST")
    override fun customers(): Either<Failure, List<Customer>> {
        return webRequest(reservationService.customers(), { it }, emptyList()).either(
             {
                 val databaseCustomers : List<Customer>? = dao.customers
                 return@either if(!databaseCustomers.isNullOrEmpty()){
                     Either.Right(databaseCustomers)
                 } else {
                     Either.Left(it)
                 }
             },
             {
                 dao.insertAllCustomers(it)
                 return@either Either.Right(it)
             }
         ) as Either<Failure, List<Customer>>
    }

    @Suppress("UNCHECKED_CAST")
    override fun tables(): Either<Failure, LiveData<List<TableEntity>>> {
        return webRequest(reservationService.tables(), { it }, emptyList()).either(
            {
                return@either if(dao.tables.isNotEmpty()){
                    Either.Right(dao.liveTables)
                } else {
                    Either.Left(it)
                }
            },
            {
                val tablesToUpdate : List<TableEntity>
                val cachedTables = dao.tables
                tablesToUpdate = if( it.size!= cachedTables.size){
                    dao.deleteAllTables()
                    it.mapIndexed { index, value -> TableEntity(index,value) }
                } else {
                    getTablesToUpdate(dao.tables, it)
                }
                dao.insertOrReplaceTables(tablesToUpdate)
                return@either Either.Right(dao.liveTables)
            }
        ) as Either<Failure, LiveData<List<TableEntity>>>
    }

     fun getTablesToUpdate(cachedTables : List<TableEntity>, freshData : List<Boolean>) : List<TableEntity>{
         val result = ArrayList<TableEntity>()
         if(cachedTables.size != freshData.size){
            return result
        }
        for ( i in 0 until cachedTables.size){
            val table = cachedTables[i]
            val freshValue = freshData[table.tableId]
            if(table.available != freshValue){
                result.add(TableEntity(table.tableId,freshValue))
            }
        }
        return result
    }

    private fun <T, R> webRequest(call: Call<T>, transform: (T) -> R, default: T): Either<Failure, R> {
        return try {
            val response = call.execute()
            when (response.isSuccessful) {
                true -> Either.Right(transform((response.body() ?: default)))
                false -> Either.Left(Failure.ServerError())
            }
        } catch (exception: Throwable) {
            Either.Left(Failure.NetworkConnection())
        }
    }
}