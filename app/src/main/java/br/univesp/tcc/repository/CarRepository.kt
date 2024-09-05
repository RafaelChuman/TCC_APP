package br.univesp.tcc.repository

import androidx.room.ColumnInfo
import br.univesp.tcc.database.dao.CarDAO
import br.univesp.tcc.database.dao.UserDao
import br.univesp.tcc.database.model.Car
import br.univesp.tcc.database.model.User
import br.univesp.tcc.webclient.CarWebClient
import br.univesp.tcc.webclient.UserWebClient
import br.univesp.tcc.webclient.model.DTOCreateCar
import br.univesp.tcc.webclient.model.DTODeleteCar
import br.univesp.tcc.webclient.model.DTOUpdateCar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.toList
import java.time.LocalDateTime
import java.util.Calendar
import java.util.UUID

class CarRepository(
    private val carDAO: CarDAO,
    private val carWebClient: CarWebClient
) {

    suspend fun getCar(userId: String): List<Car>? {
        val cars = carDAO.getAll()?.single()
        val carWeb = carWebClient.listAll()
        val updList : MutableList<Car> = mutableListOf<Car>()

        if(cars == null) return null
        if(carWeb == null) return null

        for(car in  cars)
        {
            val updCar = carWeb.find { web -> web.id == car.id }
            if(updCar != null && updCar.updated < car.updated )
            {
                updList.add(updCar)
            }
        }

        carDAO.save(updList)

        return carWeb
    }


    suspend fun insert(userId: String, newCar: DTOCreateCar) {

        val createdAt = Calendar.getInstance().time.toString()
        val car: Car = Car(
            id = UUID.randomUUID().toString(),
            brand = newCar.brand,
            model = newCar.model,
            kind = newCar.kind,
            type = newCar.type,
            plate = newCar.plate,
            yearOfFabrication = newCar.yearOfFabrication,
            yearOfModel = newCar.yearOfModel,
            color = newCar.color,
            createdAt = createdAt,
            userId = newCar.user.id,
            deleted = false,
            updated =  LocalDateTime.now(),
        )

        carDAO.save( listOf(car) )

        val data: List<String> = listOf(newCar.plate)
        val carSearched = carWebClient.listCarByPlate(data)

        if (carSearched?.isEmpty() == true) {
            carWebClient.create(newCar)
        }
    }

    suspend fun update(userId: String, updCar: DTOUpdateCar) {

        val updated = LocalDateTime.now()
        val data: List<String> = listOf(updCar.id)

        val car = carDAO.getById(data)

        if (car == null) return

        car.first().brand = updCar.brand
        car.first().model = updCar.model
        car.first().kind = updCar.kind
        car.first().type = updCar.type
        car.first().plate = updCar.plate
        car.first().yearOfFabrication = updCar.yearOfFabrication
        car.first().yearOfModel = updCar.yearOfModel
        car.first().color = updCar.color
        car.first().userId = updCar.user.id
        car.first().updated = updated

        carDAO.save(car)

        val carSearched = carWebClient.listCarById(data)

        if (carSearched !== null) {
            carWebClient.update(updCar)
        }
    }

    suspend fun delete(id: List<String>) {

        val car = carDAO.getById(id)

        if (car == null) return

        carDAO.remove(id)

        val carSearched = carWebClient.listCarById(id)

        if (carSearched !== null) {
            carWebClient.delete(id)
        }
    }
}