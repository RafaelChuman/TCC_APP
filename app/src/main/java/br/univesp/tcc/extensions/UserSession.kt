package br.univesp.tcc.extensions

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.stringPreferencesKey


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sessao_usuario")

val userIdLogged = stringPreferencesKey("userIdLogged")
val userProfileLogged = stringPreferencesKey("userProfileLogged")