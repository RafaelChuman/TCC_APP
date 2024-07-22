package br.univesp.pji610.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.univesp.pji610.database.DataSource
import br.univesp.pji610.database.model.User
import br.univesp.pji610.databinding.ActivityInsertUserBinding
import br.univesp.pji610.extensions.ToastMessage
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID.randomUUID

class InsertUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInsertUserBinding

    private val userDao by lazy {
        DataSource.instance(this).userDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInsertUserBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    fun insertOnClick(view: View)  {

        val newUser = creatUser()


        lifecycleScope.launch {
            try {
                userDao.save(newUser)
                finish()
            } catch (e: Exception) {
                Log.e("CadastroUsuario", "configuraBotaoCadastrar: ", e)
                ToastMessage("Falha ao cadastrar usuário")
            }
        }
    }

    private fun creatUser(): User {
        val userName = binding.activityInsertUserTextInputEditTextUser.text.toString()
        val name = binding.activityInsertUserTextInputEditTextName.text.toString()
        val password = binding.activityInsertUserTextInputEditTextPassword.text.toString()
        val imgPath = binding.activityInsertUserTextInputEditTextPassword.text.toString()
        val email = binding.activityInsertUserTextInputEditTextPassword.text.toString()
        val celular = 12997200179.0
        val telegram = binding.activityInsertUserTextInputEditTextPassword.text.toString()
        val isAdmin = false


        return User(
            id = randomUUID().toString(),
            userName = userName,
            name = name,
            password = password,
            imgPath = imgPath,
            email = email,
            celular = celular,
            telegram = telegram,
            isAdmin = isAdmin,
            createdAt = Calendar.getInstance().time.toString(),
        )
    }



}