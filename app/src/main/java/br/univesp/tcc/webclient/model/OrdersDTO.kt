package br.univesp.tcc.webclient.model

import br.univesp.tcc.database.model.Car
import br.univesp.tcc.database.model.User

class DTOCreateOrders (
    val km: Int,
    val fuel: Int,
    val statusExecution: String,
    val statusOrder: Boolean,
    val car: Car,
    val user: User
)

class DTOUpdateOrders (
    val id: String,
    val km: Int,
    val fuel: Int,
    val statusExecution: String,
    val statusOrder: Boolean,
    val car: Car,
    val user: User
)

class DTODeleteOrders (
    val id: List<String>,
)
