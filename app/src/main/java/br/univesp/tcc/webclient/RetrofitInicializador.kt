package br.univesp.tcc.webclient

import br.univesp.tcc.webclient.model.UserJsonAdapter
import br.univesp.tcc.webclient.services.AuthenticationService
import br.univesp.tcc.webclient.services.UserService
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RetrofitInicializador {

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()


    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.15.56:3333/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val userService = retrofit.create(UserService::class.java)

    val authenticationService = retrofit.create(AuthenticationService::class.java)

}