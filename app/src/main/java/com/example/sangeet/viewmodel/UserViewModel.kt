package com.example.sangeet.viewmodel


import android.accounts.Account
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sangeet.model.UserModel
import com.example.sangeet.repository.UserRepository

class UserViewModel(private val repo: UserRepository) : ViewModel() {

    // LiveData to observe the current user
    private val _user = MutableLiveData<UserModel?>()
    val user: LiveData<UserModel?> get() = _user

    // Authentication

    fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        repo.login(email, password, callback)
    }

    fun register(
        fullName: String,
        email: String,
        password: String,
        callback: (Boolean, String, String) -> Unit
    ) {
        repo.register(fullName, email, password, callback)
    }

    fun forgetPassword(email: String, callback: (Boolean, String) -> Unit) {
        repo.forgetPassword(email, callback)
    }

    fun updatePassword(newPassword: String, callback: (Boolean, String) -> Unit) {
        repo.updatePassword(newPassword, callback)
    }

    fun logout(callback: () -> Unit) {
        repo.logout {
            _user.postValue(null)
            callback()
        }
    }

    fun getCurrentUser(callback: (UserModel?) -> Unit) {
        repo.getCurrentUser { user ->
            _user.postValue(user)
            callback(user)
        }
    }

    // 🗃️ Database Operations

    fun addUserToDatabase(userId: String, userModel: UserModel, callback: (Boolean, String) -> Unit) {
        repo.addUserToDatabase(userId, userModel, callback)
    }

    fun updateProfile(userId: String, data: Map<String, Any?>, callback: (Boolean, String) -> Unit) {
        repo.updateProfile(userId, data, callback)
    }

    fun getUserById(userId: String) {
        repo.getUserById(userId) { fetchedUser ->
            _user.postValue(fetchedUser)
        }
    }

    // 🧹 Utility

    fun clearUser() {
        _user.value = null
    }
}
// UserViewModel.kt
//fun isUserLoggedIn(onResult: (Boolean) -> Unit) {
    //viewModelScope.launch {
        //try {
            //val account = Account(userRepository.client)
            //val user = account.get()
            //onResult(true)
       // } catch (e: Exception) {
          //  onResult(false)
       // }
    //}
//}