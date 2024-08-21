package br.univesp.tcc.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
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
    version = 4,
    entities = [User::class, Car::class, Item::class, Orders::class, OrderAndItems::class, UserToken::class],
    exportSchema = true
)
abstract class DataSource : RoomDatabase() {

    abstract fun UserDao(): UserDao

    abstract fun CarDAO(): CarDAO

    abstract fun ItemDAO(): ItemDAO

    abstract fun OrdersDAO(): OrdersDAO

    abstract fun OrderAndItemsDAO(): OrderAndItemsDAO

    abstract fun UserTokenDAO(): UserTokenDAO

    companion object {
        @Volatile
        private var db: DataSource? = null

        fun instance(context: Context): DataSource {
            return db ?: Room.databaseBuilder(
                context,
                DataSource::class.java,
                "tcc.db"
            ).fallbackToDestructiveMigration().build()
        }
    }
}


//addMigrations(
//MIGRATION_1_2,
//MIGRATION_2_3,
//MIGRATION_3_4,
//MIGRATION_4_5,
//MIGRATION_5_6).build()


