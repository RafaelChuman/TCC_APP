package br.univesp.tcc.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.UUID

val MIGRATION_1_2 = object : Migration(1, 2) {

    override fun migrate(db: SupportSQLiteDatabase) {
        val tabelaNova = "Nota_nova"
        val tabelaAtual = "Nota"

        //criar tabela nova com todos os campos esperados
        db.execSQL(
            """CREATE TABLE IF NOT EXISTS $tabelaNova (
        `id` TEXT PRIMARY KEY NOT NULL, 
        `titulo` TEXT NOT NULL, 
        `descricao` TEXT NOT NULL, 
        `imagem` TEXT)"""
        )

        //copiar dados da tabela atual para a tabela nova
        db.execSQL(
            """INSERT INTO $tabelaNova (id, titulo, descricao, imagem) 
        SELECT id, titulo, descricao, imagem FROM $tabelaAtual
    """
        )

        //gerar ids diferentes e novos
        val cursor = db.query("SELECT * FROM $tabelaNova")
        while (cursor.moveToNext()) {
            val id = cursor.getString(
                cursor.getColumnIndex("id")
            )
            db.execSQL(
                """
        UPDATE $tabelaNova 
            SET id = '${UUID.randomUUID()}' 
            WHERE id = $id"""
            )
        }

        //apagar tabela atual
        db.execSQL("DROP TABLE $tabelaAtual")

        //renomear tabela nova com o nome da tabela atual
        db.execSQL("ALTER TABLE $tabelaNova RENAME TO $tabelaAtual")
    }

}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE Nota ADD sincronizada INTEGER NOT NULL DEFAULT 0")
    }

}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE Nota ADD desativada INTEGER NOT NULL DEFAULT 0")
    }
}

val MIGRATION_4_5 = object : Migration(4,5) {
    override fun migrate(db: SupportSQLiteDatabase) {

        val createTableName = "UserToken"

        //criar tabela nova com todos os campos esperados
        db.execSQL(
            """CREATE TABLE IF NOT EXISTS $createTableName (
            `userId` TEXT PRIMARY KEY NOT NULL,
            `isAdmin` INTEGER NOT NULL, 
            `userName` TEXT NOT NULL"""
        )

    }
}

val MIGRATION_5_6 = object : Migration(5,6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        val TableName = "UserToken"

        db.execSQL("""ALTER TABLE $TableName  ADD token `STRING` NOT NULL DEFAULT ``""")
    }

}