package br.univesp.tcc.webclient.model

import br.univesp.tcc.database.model.Car
import br.univesp.tcc.database.model.Item
import br.univesp.tcc.database.model.Orders
import br.univesp.tcc.database.model.User
import java.time.LocalDateTime
import java.util.Date

class DTOCreateOrderAndItems (
    val quantity: Int,
    val price: Int,
    val discount: Int,
    val item: Item,
    val orders: Orders,
)
class DTOListOrderAndItemsByOrder (
    val orderId: Orders,
    val dateBegin: LocalDateTime,
    val dateEnd: LocalDateTime,
)

class DTOListOrderAndItemsByUser (
    val userId: String,
    val dateBegin: LocalDateTime,
    val dateEnd: LocalDateTime,
)


class DTODeleteOrderAndItems (
    val id: List<String>,
)
