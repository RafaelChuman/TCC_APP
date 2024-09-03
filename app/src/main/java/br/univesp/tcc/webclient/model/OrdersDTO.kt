package br.univesp.tcc.webclient.model

import br.univesp.tcc.database.model.Car
import br.univesp.tcc.database.model.User

class DTOCreateOrders (
    km: Int,
    fuel: Int,
    statusExecution: String,
    statusOrder: Boolean,
    car: Car,
    user: User
)

class DTOUpdateOrders (
    id: String,
    km: Int,
    fuel: Int,
    statusExecution: String,
    statusOrder: Boolean,
    car: Car,
    user: User
)

class DTODeleteOrders (
    id: List<String>,
)
