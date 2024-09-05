package br.univesp.tcc.webclient

import android.util.Log
import br.univesp.tcc.database.model.Car
import br.univesp.tcc.database.model.User
import br.univesp.tcc.webclient.model.DTOCreateCar
import br.univesp.tcc.webclient.model.DTODeleteCar
import br.univesp.tcc.webclient.model.DTOUpdateCar

private const val TAG = "CarWebClient"

class CarWebClient {

    private val carService = RetrofitInicializador().carService

    suspend fun listAll(): List<Car>?
    {
        try {
            val listResp = carService.listAll()

            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "listCarByUser", e)

        }
        return null
    }

    suspend fun listCarByUser(userId: String): List<Car>?
    {
         try {
            val listResp = carService.listCarByUser(userId)

             return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "listCarByUser", e)

        }
        return null
    }

    suspend fun listCarById(id: List<String>): Car?
    {
        try {
            val listResp = carService.listCarById(id)

            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "listCarById", e)

        }
        return null
    }

    suspend fun listCarByPlate(plate: List<String>): List<Car>?
    {
        try {
            val listResp = carService.listCarByPlate( plate)

            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "listCarByPlate", e)
        }
        return null
    }

    suspend fun create(data: DTOCreateCar): Car?
    {
        try {
            val listResp = carService.create(data)

            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "create", e)

        }
        return null
    }

    suspend fun update(data: DTOUpdateCar): Car?
    {
        try {
            val listResp = carService.update(data)

            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "update", e)

        }
        return null
    }

    suspend fun delete(id: List<String>): Car?
    {
        try {
            val listResp = carService.delete(id)

            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "delete", e)

        }
        return null
    }
}