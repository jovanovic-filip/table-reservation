package com.tablereservation.repository

import androidx.lifecycle.MutableLiveData
import com.tablereservation.UnitTest
import com.tablereservation.core.exception.Failure
import com.tablereservation.core.functional.Either
import com.tablereservation.features.tables.TableEntity
import com.tablereservation.repository.database.ReservationsDao
import com.tablereservation.repository.network.TablesAndUsersService
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import retrofit2.Call
import retrofit2.Response
import java.util.Arrays.asList

class DefaultRepositoryTest: UnitTest() {

    private lateinit var repository: DefaultRepository
    @Mock
    private lateinit var service: TablesAndUsersService
    @Mock
    private lateinit var dao: ReservationsDao

    @Mock private lateinit var tablesCall: Call<List<Boolean>>
    @Mock private lateinit var tablesResponse: Response<List<Boolean>>
    @Mock private lateinit var liveTablesResponse: MutableLiveData<List<TableEntity>>

    @Before
    fun setUp() {
        repository = DefaultRepository(service,dao)
    }

    @Test
    fun `should return live data tables from service`() {
        given { tablesResponse.body() }.willReturn(null)
        given { tablesResponse.isSuccessful }.willReturn(true)
        given { tablesCall.execute() }.willReturn(tablesResponse)
        given { service.tables() }.willReturn(tablesCall)
        given { dao.liveTables }.willReturn(liveTablesResponse)

        val tables = repository.tables()

        tables shouldEqual Either.Right(liveTablesResponse)
        verify(dao).insertOrReplaceTables(emptyList())
        verify(service).tables()
    }

    @Test fun `when unsuccessful response, tables service should return server error`() {
        given { tablesResponse.isSuccessful }.willReturn(false)
        given { tablesCall.execute() }.willReturn(tablesResponse)
        given { service.tables() }.willReturn(tablesCall)

        val tables = repository.tables()

        tables shouldBeInstanceOf Either::class.java
        tables.isLeft shouldEqual true
        tables.either({ failure -> failure shouldBeInstanceOf Failure.ServerError::class.java }, {})
    }

    private val dummyFreshData : List<Boolean> = asList(
        true,
        false,
        false,
        false,
        true
    )

    private val dummyCachedTables : List<TableEntity> =
        asList(
            TableEntity(0,true, 13),
            TableEntity(1,false, -1),
            TableEntity(2,false, -1),
            TableEntity(3,true, 15),
            TableEntity(4,false, -1)
        )

    private val dummyFreshDataUnchanged : List<Boolean> = asList(
        true,
        false,
        false,
        true,
        false
    )

    private val dummyResultingTables : MutableList<TableEntity> =
        asList(
            TableEntity(3,false, -1),
            TableEntity(4,true, -1)
        )

    @Test
    fun `should update database tables from web`() {
        repeat(10){
            dummyResultingTables.shuffle()
            dummyResultingTables shouldContainSame repository.getTablesToUpdate(dummyCachedTables,dummyFreshData)
        }
        repository.getTablesToUpdate(dummyCachedTables,dummyFreshDataUnchanged).shouldBeEmpty()
        repository.getTablesToUpdate(emptyList(),dummyFreshData).shouldBeEmpty()
        repository.getTablesToUpdate(dummyCachedTables,emptyList()).shouldBeEmpty()
    }
}