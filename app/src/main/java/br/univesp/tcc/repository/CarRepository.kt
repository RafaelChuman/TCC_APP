package br.univesp.tcc.repository

import android.content.Context
import android.util.Log
import androidx.room.ColumnInfo
import br.univesp.tcc.database.dao.CarDAO
import br.univesp.tcc.database.dao.UserDao
import br.univesp.tcc.database.model.Car
import br.univesp.tcc.database.model.User
import br.univesp.tcc.extensions.dataStore
import br.univesp.tcc.extensions.tokenDataStore
import br.univesp.tcc.webclient.CarWebClient
import br.univesp.tcc.webclient.UserWebClient
import br.univesp.tcc.webclient.model.DTOCreateCar
import br.univesp.tcc.webclient.model.DTODeleteCar
import br.univesp.tcc.webclient.model.DTODeleteUser
import br.univesp.tcc.webclient.model.DTOUpdateCar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.toList
import java.time.LocalDateTime
import java.util.Calendar
import java.util.UUID

private const val TAG = "CarRepository"

class CarRepository(
    private val carDAO: CarDAO,
    private val carWebClient: CarWebClient,
    private val ctx: Context
) {

    private suspend fun getToken(): String {
        val preferences = ctx.dataStore.data.firstOrNull()

        if (preferences == null) return ""

        val userToken = preferences[tokenDataStore]

        if (userToken.isNullOrEmpty()) return ""

        return userToken
    }

    suspend fun getCarById(id: String): List<Car>? {
        Log.i(TAG, "getCarById - id: $id")

        val car = carDAO.getById(listOf(id)).firstOrNull()

        Log.i(TAG, "getCarById - car: $car")

        return car
    }

    suspend fun getAll(): List<Car>? {

        syncCar()

        val car = carDAO.getAll().firstOrNull()

        Log.i(TAG, "getCarById - car: $car")

        return car
    }

    private suspend fun syncCar() {

        Log.i(TAG, "syncCar")

        val userToken = getToken()

        val carListWeb = carWebClient.listAll(userToken)?: listOf<Car>()
        val carsListSqlite = carDAO.getAll().firstOrNull() ?: listOf<Car>()
        val updList: MutableList<Car> = mutableListOf<Car>()
        val dltList: MutableList<String> = mutableListOf<String>()

        //if (carsListSqlite.isEmpty()) return
        //if (carWeb == null) return

        for (carWeb in carListWeb) {
            val updCar = carsListSqlite.find { car -> car.id == carWeb.id }
            if (updCar == null ||  updCar.updated < carWeb.updated) {
                updList.add(carWeb)
            }
        }

        //Log.i(TAG, "syncCar - updList: $updList")
        if (updList.isNotEmpty())  carDAO.save(updList)

        for (carSqlite in carsListSqlite) {
            val updCar = carListWeb.find { web -> web.id == carSqlite.id }
            Log.i(TAG, "syncCar - dltList  carWeb: ${updCar?.updated.toString()} == carSqlite: ${carSqlite.updated}")
            if (updCar == null){  // ||  updCar.updated < carSqlite.updated) {
                dltList.add(carSqlite.id)
            }
        }

        Log.i(TAG, "syncCar - dltList: $dltList")
        if (dltList.isNotEmpty())  carDAO.purge(dltList)
    }


    suspend fun insert(newCar: Car) {

        Log.i(TAG, "insert - newCar: $newCar")
        val userToken = getToken()

        val carSearched = carDAO.getByPlate(listOf(newCar.plate)).firstOrNull()

        if (!carSearched.isNullOrEmpty()) return

        carDAO.save(listOf(newCar))

        val carInserted = carWebClient.create(userToken, listOf(newCar))

        Log.i(TAG, "insert - carInserted: $carInserted")

    }

    suspend fun update(updCar: Car) {

        Log.i(TAG, "update - updCar: $updCar")
        val userToken = getToken()

        val carSearched = carDAO.getById(listOf(updCar.id)).firstOrNull()

        if (carSearched.isNullOrEmpty()) return

        carDAO.save(listOf(updCar))

        val carUpdated = carWebClient.update(userToken, listOf(updCar))

        Log.i(TAG, "update - carUpdated: $carUpdated")
    }

    suspend fun delete(idList: List<String>) {

        if (idList.isNotEmpty()) return
        val userToken = getToken()

        val carSearched = carDAO.getById(idList).firstOrNull()

        if (carSearched.isNullOrEmpty()) return

        carDAO.remove(idList)

        val data = DTODeleteCar(
            id = idList
        )

        carWebClient.delete(userToken, data)
    }
}