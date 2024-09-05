package br.univesp.tcc.webclient.model

import br.univesp.tcc.database.model.User

class DTOCreateCar (
    val brand: String,
    val model: String,
    val kind: String,
    val type: String,
    val plate: String,
    val yearOfFabrication: Int,
    val yearOfModel: Int,
    val color: String,
    val user: User,
)

class DTOUpdateCar (
    val id: String,
    val brand: String,
    val model: String,
    val kind: String,
    val type: String,
    val plate: String,
    val yearOfFabrication: Int,
    val yearOfModel: Int,
    val color: String,
    val user: User,
)

class DTOListCarById (
    val id: List<String>,
)

class DTOListCarByPlate (
    val id: List<String>,
)

class DTODeleteCar (
    val id: List<String>,
)
