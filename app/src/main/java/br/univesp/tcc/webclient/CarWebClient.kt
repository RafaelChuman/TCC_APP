package br.univesp.tcc.webclient

import android.util.Log
import br.univesp.tcc.database.model.Car
import br.univesp.tcc.database.model.DeleteResult
import br.univesp.tcc.database.model.InsertResult
import br.univesp.tcc.database.model.User
import br.univesp.tcc.webclient.model.DTOCreateCar
import br.univesp.tcc.webclient.model.DTODeleteCar
import br.univesp.tcc.webclient.model.DTOUpdateCar
import retrofit2.Response

private const val TAG = "CarWebClient"

class CarWebClient {

    private val carService = RetrofitInicializador().carService

    suspend fun create(userToken: String, data: List<Car>): Response<InsertResult>?
    {
        try {
            val listResp = carService.create(userToken, data)

            Log.i(TAG, "create - listResp: $listResp")
            return listResp
        } catch (e: Exception) {
            Log.e(TAG, "create - error", e)
        }
        return null
    }

    suspend fun update(userToken: String, data: List<Car>): Car?
    {
        try {
            val listResp = carService.update(userToken, data)

            Log.i(TAG, "update - id: $listResp")
            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "update - error", e)
        }
        return null
    }

    suspend fun delete(userToken: String, data: DTODeleteCar): Response<DeleteResult>?
    {
        try {
            val listResp = carService.delete(userToken, data)

            Log.i(TAG, "delete - id: $listResp")
            return listResp
        } catch (e: Exception) {
            Log.e(TAG, "delete - error", e)
        }
        return null
    }

    suspend fun listAll(userToken: String): List<Car>?
    {
        try {
            val listResp = carService.listAll(userToken)

            Log.i(TAG, "listAll - listResp: ${listResp.body()}")
            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "listAll - error: ", e)
        }
        return null
    }

    suspend fun listCarByUser(userToken: String, userId: String): List<Car>?
    {
         try {
            val listResp = carService.listCarByUser(userToken, userId)

             Log.i(TAG, "listCarByUser - listResp: $listResp")
             return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "listCarByUser - error", e)
        }
        return null
    }

    suspend fun listCarById(userToken: String, id: List<String>): Car?
    {
        try {
            val listResp = carService.listCarById(userToken, id)

            Log.i(TAG, "listCarById - listResp: $listResp")
            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "listCarById - error", e)
        }
        return null
    }

    suspend fun listCarByPlate(userToken: String, plate: List<String>): List<Car>?
    {
        try {
            val listResp = carService.listCarByPlate(userToken, plate)

            Log.i(TAG, "listCarByPlate - listResp: $listResp")
            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "listCarByPlate - error", e)
        }
        return null
    }
}