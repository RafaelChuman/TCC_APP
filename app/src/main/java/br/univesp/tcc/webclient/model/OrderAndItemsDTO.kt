package br.univesp.tcc.webclient.model

import br.univesp.tcc.database.model.Car
import br.univesp.tcc.database.model.Item
import br.univesp.tcc.database.model.Orders
import br.univesp.tcc.database.model.User
import java.util.Date

class DTOCreateOrderAndItems (
    quantity: Int,
    price: Int,
    discount: Int,
    item: Item,
    orders: Orders,
)
class DTOListOrderAndItemsByOrder (
    orderId: Orders,
    dateBegin: Date,
    dateEnd: Date,
)

class DTOListOrderAndItemsByUser (
    userId: String,
    dateBegin: Date,
    dateEnd: Date,
)


class DTODeleteOrderAndItems (
    id: List<String>,
)
