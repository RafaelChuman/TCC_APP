package br.univesp.tcc.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.univesp.tcc.database.model.User
import br.univesp.tcc.databinding.RecyclerviewUserBinding

private const val TAG = "UserRecycleView"

class UserRecycleView(
    private val context: Context,
    var userOnClickEvent: (user: User) -> Unit = {},
    userList: List<User> = emptyList()
) : RecyclerView.Adapter<UserRecycleView.ViewHolder>() {

    private val users = userList.toMutableList()

    inner class ViewHolder(
        private val binding: RecyclerviewUserBinding,
        private val userOnClickEvent: (user: User) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var user: User

        init {
            itemView.setOnClickListener {
                if (::user.isInitialized) {
                    userOnClickEvent(user)
                }
            }
        }

        fun associateUser(usr: User) {
            Log.i(TAG, "associateUser - usr: $usr")

            this.user = usr

            val name = usr.name
            val userName = usr.userName
            val email = usr.email
            val imgPath = usr.imgPath
            val cellphone = usr.cellphone
            val telegram = usr.telegram


            binding.textViewName.text = name
            binding.textViewUserName.text = userName
            binding.textViewEmail.text = email
            binding.textViewImgPath.text = imgPath
            binding.textViewCellphone.text = cellphone
            binding.textViewTelegram.text = telegram
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder =
        ViewHolder(
            RecyclerviewUserBinding
                .inflate(
                    LayoutInflater.from(context)
                ),
            userOnClickEvent
        )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder - users[position]: ${users[position]}")
        holder.associateUser(users[position])
    }

    override fun getItemCount(): Int
    {
        Log.i(TAG, "getItemCount - users.size: ${users.size}")
        return users.size
    }

    fun update(newUser: List<User>) {
        Log.i(TAG, "update - this.user.size: ${this.users.size}")
        notifyItemRangeRemoved(0, this.users.size)
        this.users.clear()
        this.users.addAll(newUser)

        Log.i(TAG, "update - this.user.size: ${this.users.size}")
        notifyItemInserted(this.users.size)
    }

}
