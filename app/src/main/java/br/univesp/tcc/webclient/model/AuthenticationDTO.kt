package br.univesp.tcc.webclient.model

data class AuthenticateDTO(
    val userName: String,
    val password: String,
)

//class UserJsonAdapter {
//    @FromJson
//    fun userFromJson(userJson: UserResponse): User {
//        return User(
//            id = userJson.id ?: "",
//            name = userJson.name ?: "",
//            userName = userJson.userName?: "",
//            password = userJson.password?: "",
//            imgPath = userJson.imgPath?: "",
//            email = userJson.email?: "",
//            cellphone = userJson.cellphone?: "",
//            telegram = userJson.telegram?: "",
//            isAdmin = userJson.isAdmin?: false,
//            createdAt = userJson.createdAt?: "",
//            updated = userJson.updated?: "",
//            deleted = userJson.deleted?: false,
//        )
//    }
//
//    @ToJson
//    fun userToJson(user: User): UserResponse {
//        return UserResponse(
//            id = user.id ?: "",
//            name = user.name ?: "",
//            userName = user.userName?: "",
//            password = user.password?: "",
//            imgPath = user.imgPath?: "",
//            email = user.email?: "",
//            cellphone = user.cellphone?: "",
//            telegram = user.telegram?: "",
//            isAdmin = user.isAdmin?: false,
//            createdAt = user.createdAt?: "",
//            updated = user.updated?: "",
//            deleted = user.deleted?: false,
//        )
//    }
//}