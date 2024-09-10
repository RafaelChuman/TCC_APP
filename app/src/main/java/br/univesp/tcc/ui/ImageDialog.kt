package br.univesp.tcc.ui

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import br.univesp.tcc.databinding.DialogImagemBinding
import br.univesp.tcc.extensions.loadImageFromPath

class ImageDialog(private val context: Context) {

    fun show(
        urlDefault: String? = null,
        setImageOnLoadEvent: (imagePath: String) -> Unit
    ) {
        DialogImagemBinding.inflate(LayoutInflater.from(context)).apply {

            urlDefault?.let {
                dialogImageImageView.loadImageFromPath(it)
                dialogImageTextInputEditTextUrl.setText(it)
            }

            dialogImageButtonLoad.setOnClickListener {
                val url = dialogImageTextInputEditTextUrl.text.toString()
                dialogImageImageView.loadImageFromPath(url)
            }

            AlertDialog.Builder(context)
                .setView(root)
                .setPositiveButton("Confirmar") { _, _ ->
                    val url = dialogImageTextInputEditTextUrl.text.toString()
                    setImageOnLoadEvent(url)
                }
                .setNegativeButton("Cancelar") { _, _ ->

                }
                .show()
        }
    }
}