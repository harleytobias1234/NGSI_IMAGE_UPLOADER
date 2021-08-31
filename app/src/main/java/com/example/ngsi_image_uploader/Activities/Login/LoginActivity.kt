package com.example.ngsi_image_uploader.Activities.Login

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ngsi_image_uploader.MainActivity
import com.example.ngsi_image_uploader.R
import com.example.ngsi_image_uploader.Responses.POST_Login
import com.example.ngsi_image_uploader.retrofit.adapter.Api_NGSI
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.Exception
import java.util.*
import android.content.SharedPreferences;
import com.example.ngsi_image_uploader.Component.SessionManager


class LoginActivity : AppCompatActivity() {

    var et_email: EditText? = null
    var et_password: EditText? = null
    var sharedPreferences: SharedPreferences? = null
    var loginPref: String? = "loginPref"

    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        et_email = findViewById(R.id.et_email)
        et_password = findViewById(R.id.et_password)
        sharedPreferences = getSharedPreferences(loginPref, Context.MODE_PRIVATE);


    }

    fun btnClickSignin(v: View) {
        checkerText(et_email?.text.toString(), et_password?.text.toString())
    }

    fun btnClickSignup(v: View) {
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun checkerText(email: String, pword: String) {
        //*****************************Do Checking if text is empty***********************
        if (TextUtils.isEmpty(email)) { et_email?.setError("Please fill this") }
        else if (TextUtils.isEmpty(pword)) { et_password?.setError("Please fill this") }
        else {loginAPI(email, pword)} //Run SignUp
        //********************************************************************************
    }

    private fun loginAPI(email: String, pword: String) {
        // display a progress dialog
        val progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false) // set cancelable to false
        progressDialog.setMessage("Please Wait") // set message
        progressDialog.show() // show progress dialog
        try {
            //****************Set params*********************************************
            val paramObject = JsonObject()
            paramObject.addProperty("email", email)
            paramObject.addProperty("password", pword)
            //***********************************************************************
            //Run API//
            Api_NGSI.postLoginAPI()?.loginApi(paramObject)
                ?.enqueue(object : Callback<POST_Login?> {
                    override fun onResponse(
                        call: Call<POST_Login?>,
                        response: Response<POST_Login?>
                    ) {
                        if (response.isSuccessful) {
                            val jsonObject = JSONObject(Gson().toJson(response.body()))
                            Log.d("login Status", "" + response.body().toString())
                            if (jsonObject.getString("message") == "Registration Successful") {
                                Toast.makeText(this@LoginActivity, "" + jsonObject.getString("message"), Toast.LENGTH_LONG ).show()
                                //Session
                                sessionManager = SessionManager(this@LoginActivity)
                                sessionManager.createLoginSession(email,pword)
                                ///

                                //Go to Main Activity
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                                progressDialog.dismiss()
                            }
                            else {
                                Toast.makeText(this@LoginActivity, "" + jsonObject.getString("message"), Toast.LENGTH_LONG ).show()
                                progressDialog.dismiss()
                            }
                        } else {
                            try {
                                assert(response.errorBody() != null)
                                response.errorBody()?.let { Log.e("ERROR2", it.string()) }
                                Toast.makeText(this@LoginActivity, "" + "Invalid email address or password", Toast.LENGTH_LONG ).show()
                                response.errorBody()?.let { Log.e("ERROR", it.string()) }
                                progressDialog.dismiss()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                    }

                    override fun onFailure(call: Call<POST_Login?>, t: Throwable) {
                        Log.e("Fail response", Arrays.toString(t.stackTrace))
                        progressDialog.dismiss()
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    //**************************************************
}