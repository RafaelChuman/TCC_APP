package br.univesp.tcc.extensions

import android.content.Context
import android.widget.Toast

fun Context.ToastMessage(mensagem: String) {
    Toast.makeText(
        this,
        mensagem,
        Toast.LENGTH_SHORT
    ).show()
}