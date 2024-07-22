package br.univesp.pji610.extensions

import android.widget.ImageView
import br.univesp.pji610.R
import coil.load


fun ImageView.loadImageFromPath(
    url: String? = null,
    fallback: Int = R.drawable.imagem_padrao
) {
    load(url) {
        placeholder(R.drawable.placeholder)
        error(R.drawable.erro)
        fallback(fallback)
    }
}