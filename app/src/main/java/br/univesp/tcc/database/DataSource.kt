package br.univesp.tcc.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.univesp.tcc.database.dao.CarDAO
import br.univesp.tcc.database.dao.ItemDAO
import br.univesp.tcc.database.dao.OrderAndItemsDAO
import br.univesp.tcc.database.dao.OrdersDAO
import br.univesp.tcc.database.dao.UserDao
import br.univesp.tcc.database.dao.UserTokenDAO
import br.univesp.tcc.database.model.Car
import br.univesp.tcc.database.model.Item
import br.univesp.tcc.database.model.OrderAndItems
import br.univesp.tcc.database.model.Orders
import br.univesp.tcc.database.model.User
import br.univesp.tcc.database.model.UserToken


@Database(
    version = 9,
    entities = [User::class, Car::class, Item::class, Orders::class, OrderAndItems::class, UserToken::class],
    exportSchema = true
)
@TypeConverters(RoomConverterLocalDateTime::class) // Adiciona os conversores
abstract class AppDatabase : RoomDatabase() {

    abstract fun UserDao(): UserDao

    abstract fun CarDAO(): CarDAO

    abstract fun ItemDAO(): ItemDAO

    abstract fun OrdersDAO(): OrdersDAO

    abstract fun OrderAndItemsDAO(): OrderAndItemsDAO

    abstract fun UserTokenDAO(): UserTokenDAO
}

object DataSource {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        // Verificar se a instância já existe, se não, cria uma nova
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "tcc.db"
            )
                .fallbackToDestructiveMigration()  // Usado para recriar o banco de dados em caso de mudanças
                .build()

            INSTANCE = instance
            instance
        }
    }
}


//
//@Database(
//    version = 7,
//    entities = [User::class, Car::class, Item::class, Orders::class, OrderAndItems::class, UserToken::class],
//    exportSchema = true
//)
//@TypeConverters(Converters::class) // Adiciona os conversores
//abstract class DataSource : RoomDatabase() {
//
//    abstract fun UserDao(): UserDao
//
//    abstract fun CarDAO(): CarDAO
//
//    abstract fun ItemDAO(): ItemDAO
//
//    abstract fun OrdersDAO(): OrdersDAO
//
//    abstract fun OrderAndItemsDAO(): OrderAndItemsDAO
//
//    abstract fun UserTokenDAO(): UserTokenDAO
//
//
//    companion object {
//        @Volatile
//        private var db: DataSource? = null
//
//        fun instance(context: Context): DataSource {
//
//
//            return db ?: Room.databaseBuilder(
//                context,
//                DataSource::class.java,
//                "tcc.db"
//            ).fallbackToDestructiveMigration().build()
//        }
//    }
//}



