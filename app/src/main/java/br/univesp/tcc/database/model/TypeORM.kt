package br.univesp.tcc.database.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DeleteResult(
    @Json(name = "raw") val raw: List<Any>,
    @Json(name = "affected") val affected: Int
)

@JsonClass(generateAdapter = true)
data class Identifier(
    @Json(name = "id") val id: Int
)

@JsonClass(generateAdapter = true)
data class InsertResult(
    @Json(name = "identifiers") val identifiers: List<Identifier>,
    @Json(name = "generatedMaps") val generatedMaps: List<Any>,
    @Json(name = "raw") val raw: List<Any>
)