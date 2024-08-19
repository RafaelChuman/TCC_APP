package br.univesp.tcc.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.univesp.tcc.database.DataSource
import br.univesp.tcc.database.model.User
import br.univesp.tcc.databinding.ActivityInsertUserBinding
import br.univesp.tcc.extensions.ToastMessage
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID.randomUUID

private const val TAG = "InsertUserActivity"

class InsertUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInsertUserBinding

    private val userDao by lazy {
        DataSource.instance(this).UserDao()
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
                Log.e(TAG, "insertOnClick: ", e)
                ToastMessage("Falha ao cadastrar usuário: $e")
            }
        }
    }

    private fun creatUser(): User {
        val userName = binding.activityInsertUserTextInputEditTextUser.text.toString()
        val name = binding.activityInsertUserTextInputEditTextName.text.toString()
        val password = binding.activityInsertUserTextInputEditTextPassword.text.toString()
        val imgPath = binding.activityInsertUserTextInputEditTextPassword.text.toString()
        val email = binding.activityInsertUserTextInputEditTextPassword.text.toString()
        val cellphone = "+5512997200179"
        val telegram = binding.activityInsertUserTextInputEditTextPassword.text.toString()
        val isAdmin = false


        return User(
            id = randomUUID().toString(),
            userName = userName,
            name = name,
            password = password,
            imgPath = imgPath,
            email = email,
            cellphone = cellphone,
            telegram = telegram,
            isAdmin = isAdmin,
            createdAt = Calendar.getInstance().time.toString(),
            deleted = false,
            updated = ""
        )
    }
}