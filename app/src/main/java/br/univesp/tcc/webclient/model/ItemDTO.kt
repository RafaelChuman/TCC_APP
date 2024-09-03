package br.univesp.tcc.webclient.model

import br.univesp.tcc.database.model.User

class DTOCreateItem (
    type: String,
    name: String,
)

class DTOUpdateItem (
    id: String,
    type: String,
    name: String,
)

class DTODeleteItem (
    id: List<String>,
)
