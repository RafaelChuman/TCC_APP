package br.univesp.tcc.webclient.model

import br.univesp.tcc.database.model.User
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson


class UserResponse (
    val id: String?,
    val name: String?,
    val userName: String?,
    val password: String?,
    val imgPath: String?,
    val email: String?,
    val cellphone: String?,
    val telegram: String?,
    val isAdmin: Boolean?,
    val createdAt: String?,
    val deleted: Boolean?,
    val updated: String?,
){
    val user: User get() = User(
        id = id?: "",
        name = name ?: "",
        userName = userName?: "",
        password = password?: "",
        imgPath = imgPath?: "",
        email = email?: "",
        cellphone = cellphone?: "",
        telegram = telegram?: "",
        isAdmin = isAdmin?: false,
        createdAt = createdAt?: "",
        deleted = deleted?: false,
        updated = updated?:  ""
    )

}

class UserJsonAdapter {
    @FromJson
    fun userFromJson(userJson: UserResponse): User {
        return User(
            id = userJson.id ?: "",
            name = userJson.name ?: "",
            userName = userJson.userName?: "",
            password = userJson.password?: "",
            imgPath = userJson.imgPath?: "",
            email = userJson.email?: "",
            cellphone = userJson.cellphone?: "",
            telegram = userJson.telegram?: "",
            isAdmin = userJson.isAdmin?: false,
            createdAt = userJson.createdAt?: "",
            updated = userJson.updated?: "",
            deleted = userJson.deleted?: false,
        )
    }

    @ToJson
    fun userToJson(user: User): UserResponse {
        return UserResponse(
            id = user.id ?: "",
            name = user.name ?: "",
            userName = user.userName?: "",
            password = user.password?: "",
            imgPath = user.imgPath?: "",
            email = user.email?: "",
            cellphone = user.cellphone?: "",
            telegram = user.telegram?: "",
            isAdmin = user.isAdmin?: false,
            createdAt = user.createdAt?: "",
            updated = user.updated?: "",
            deleted = user.deleted?: false,
        )
    }
}