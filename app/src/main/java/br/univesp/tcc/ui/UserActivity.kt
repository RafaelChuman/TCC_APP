package br.univesp.tcc.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import br.univesp.tcc.database.DataSource
import br.univesp.tcc.databinding.ActivityUserBinding
import br.univesp.tcc.repository.UserRepository
import br.univesp.tcc.ui.activity.USER_ID
import br.univesp.tcc.webclient.UserWebClient
import kotlinx.coroutines.launch

private const val TAG = "UserActivity"

class UserActivity : Fragment() {

    private lateinit var binding: ActivityUserBinding

    private val recycleViewAdapter by lazy {
        UserRecycleView(requireContext())
    }

    private val userDao by lazy {
        DataSource.getDatabase(requireContext()).UserDao()
    }

    private val userWebClient by lazy {
        UserWebClient()
    }

    private val ctx by lazy {
        requireContext()
    }

    private val userRepository by lazy {

        UserRepository(
            userDao,
            userWebClient,
            ctx
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityUserBinding.inflate(inflater, container, false)

        configRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                getAllUser()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.fab.setOnClickListener {
            setFab()
        }
    }


    private suspend fun getAllUser() {
        val user = userRepository.getUsers()

        Log.i(TAG, "getAllUser")

        if (user.isNullOrEmpty()) {
            binding.textView.visibility = View.VISIBLE
            binding.recyclerview.visibility = View.GONE

            Log.i(TAG, "getAllUser - user: user.isNullOrEmpty()")
        } else {
            binding.textView.visibility = View.GONE
            binding.recyclerview.visibility = View.VISIBLE

            Log.i(TAG, "getAllUser - user: ${user.toString()}")
            recycleViewAdapter.update(user)
        }
    }

    private fun setFab() {
        val intent = Intent(requireActivity(), UserMgmtActivity::class.java)
        startActivity(intent)
    }

    private fun configRecyclerView() {
        binding.recyclerview.adapter = recycleViewAdapter
        recycleViewAdapter.userOnClickEvent = { user ->
            val intent = Intent(requireActivity(), UserMgmtActivity::class.java)

            intent.putExtra(USER_ID, user.userId)
            startActivity(intent)
        }
    }
}