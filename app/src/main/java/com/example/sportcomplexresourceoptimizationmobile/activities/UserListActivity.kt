package com.example.sportcomplexresourceoptimizationmobile.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sportcomplexresourceoptimizationmobile.ApiServiceImpl
import com.example.sportcomplexresourceoptimizationmobile.R
import com.example.sportcomplexresourceoptimizationmobile.models.RoleModel
import com.example.sportcomplexresourceoptimizationmobile.models.UserItemModel
import com.example.sportcomplexresourceoptimizationmobile.models.UserListModel
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Locale

class UserListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private val apiService = ApiServiceImpl()
    private var userList: List<UserItemModel> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        recyclerView = findViewById(R.id.recyclerViewUsers)
        recyclerView.layoutManager = LinearLayoutManager(this)

        userAdapter = UserAdapter(emptyList())
        recyclerView.adapter = userAdapter
        val navigationView: NavigationView = findViewById(R.id.navigationView)

        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Не використовується
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Не використовується
            }

            override fun afterTextChanged(s: Editable?) {
                // Оновіть список користувачів згідно зі строкою пошуку
                filterUsersByEmail(s.toString())
            }
        })

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_item1 -> {
                    val intent = Intent(this, UsersReservationsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_item2 -> {
                    val intent = Intent(this, UserListActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_item3 -> {
                    val intent = Intent(this, SportComplexActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        fetchUsers()
    }

    private fun fetchUsers() {
        apiService.getUsers(1, 30, object : ApiServiceImpl.UsersCallback {
            override fun onSuccess(result: UserListModel) {
                // Оновлюємо змінну userList
                userList = result.items
                userAdapter.updateData(userList)
            }

            override fun onError(error: String) {
                // Обробка помилок, якщо потрібно
            }
        })
    }

    fun openDrawer(view: View) {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        drawerLayout.openDrawer(GravityCompat.START)
    }

    private fun filterUsersByEmail(query: String) {
        val filteredList = userList.filter { user ->
            user.email.toLowerCase(Locale.getDefault()).contains(query.toLowerCase(Locale.getDefault()))
        }

        userAdapter.updateData(filteredList)
    }

    inner class UserAdapter(private var userList: List<UserItemModel>) :
        RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

        // Оновлення даних в адаптері
        fun updateData(newUsers: List<UserItemModel>) {
            userList = newUsers
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_user, parent, false)
            return UserViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
            val user = userList[position]

            holder.userName.text = user.email
            holder.userRoles.text = "Roles: ${user.roles.joinToString { it.name }}"
            holder.IsBan.text = "Ban: " + user.isDeleted.toString()

            // Оновлення тексту на кнопці в залежності від значення isDeleted
            val banButtonText = if (user.isDeleted) "Unban" else "Ban"
            holder.buttonBan.text = banButtonText

            // Додаємо слухача кліків для кнопки "Ban"
            holder.buttonBan.setOnClickListener {
                // Викликаємо метод banUser при кліку на кнопку "Ban"
                val userId = user.id // assuming you have a method to get the user ID
                if(banButtonText=="Ban"){
                    apiService.banUser(userId, object : ApiServiceImpl.ApiCallback {
                        override fun onSuccess(result: String) {
                            fetchUsers()
                        }

                        override fun onError(error: String) {
                        }
                    })
                }
                if(banButtonText=="Unban"){
                    apiService.unBanUser(userId, object : ApiServiceImpl.ApiCallback {
                        override fun onSuccess(result: String) {
                            println("UNBAN")
                            fetchUsers()
                        }

                        override fun onError(error: String) {
                        }
                    })
                }

            }
        }


        override fun getItemCount(): Int {
            return userList.size
        }

        inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val userName: TextView = itemView.findViewById(R.id.textViewEmail)
            val userRoles: TextView = itemView.findViewById(R.id.textViewUserRoles)
            val buttonBan: Button = itemView.findViewById(R.id.buttonBan)
            val IsBan: TextView = itemView.findViewById(R.id.textViewIsBan)
        }

    }
}
