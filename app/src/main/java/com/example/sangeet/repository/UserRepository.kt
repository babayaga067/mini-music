package com.example.sangeet.repository

import com.example.sangeet.model.UserModel
import com.google.firebase.auth.FirebaseUser



interface UserRepository {
    //login
    //registration
    //forgetPassword
    //update profile
    //getCurrentUser
    //addUserToDatabase
    //logout

    fun login(email: String, password: String,callback: (Boolean, String) -> Unit)

    //authentication function
    fun register( userid : String,
                  email : String,
                  password: String,
                  firstname : String,
                  lastname : String,
                  gender : String,
                  address : String,
                  callback:(Boolean, String, String) -> Unit )


    //database function
    fun addUserToDatabase(
        userid: String,
        userModel: UserModel,
        callback: (Boolean, String)-> Unit)


    fun forgetPassword(
        email: String,
        callback: (Boolean, String) -> Unit )


    fun getUserById(userid: String,callback: (UserModel?) -> Unit)


    fun updateProfile(userId: String, data : MutableMap<String, Any?>,
                      callback: (Boolean, String) -> Unit)




    fun getCurrentUser() : FirebaseUser?


    fun logout( userId: String, callback: (UserModel?) -> Unit)

    fun updatePassword(userId: String,oldPassword: String, callback: (Boolean, String) -> Unit)
}