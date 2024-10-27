package br.univesp.tcc.webclient

import br.univesp.tcc.database.MoshiConverterDateTime
import br.univesp.tcc.database.model.DeleteResult
import br.univesp.tcc.database.model.InsertResult
import br.univesp.tcc.webclient.services.AuthenticationService
import br.univesp.tcc.webclient.services.CarService
import br.univesp.tcc.webclient.services.ItemService
import br.univesp.tcc.webclient.services.OrderAndItemsService
import br.univesp.tcc.webclient.services.OrdersService
import br.univesp.tcc.webclient.services.UserService
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RetrofitInicializador {



    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(MoshiConverterDateTime())  // Adapter para LocalDateTime
        .build()

    val adapterDeleteResult = moshi.adapter(DeleteResult::class.java)
    val adapterInsertResult = moshi.adapter(InsertResult::class.java)

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.15.3:3333/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val userService: UserService = retrofit.create(UserService::class.java)

    val authenticationService = retrofit.create(AuthenticationService::class.java)

    val carService = retrofit.create(CarService::class.java)

    val itemService = retrofit.create(ItemService::class.java)

    val ordersService = retrofit.create(OrdersService::class.java)

    val orderAndItemsService = retrofit.create(OrderAndItemsService::class.java)
}