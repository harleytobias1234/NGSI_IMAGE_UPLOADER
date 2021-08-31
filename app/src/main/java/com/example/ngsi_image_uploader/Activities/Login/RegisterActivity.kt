package com.example.ngsi_image_uploader.Activities.Login

import android.app.ProgressDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ngsi_image_uploader.R
import com.example.ngsi_image_uploader.Responses.POST_Registration
import com.example.ngsi_image_uploader.retrofit.adapter.Api_NGSI
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.Exception
import java.util.*
import com.google.gson.Gson

import org.json.JSONObject




class RegisterActivity : AppCompatActivity() {

    var et_fullname : EditText? = null
    var et_email : EditText? = null
    var et_password : EditText? = null
    var et_password2 : EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        et_fullname = findViewById(R.id.et_fullname)
        et_email = findViewById(R.id.et_email)
        et_password = findViewById(R.id.et_password)
        et_password2 = findViewById(R.id.et_password2)
    }

    fun btnClickSignup(v : View) {
        checkerText(et_fullname?.text.toString(), et_email?.text.toString(), et_password?.text.toString(), et_password2?.text.toString())
    }


    private fun signUp(full_name: String, email: String, password: String) {
        // display a progress dialog
        val progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false) // set cancelable to false
        progressDialog.setMessage("Please Wait") // set message
        progressDialog.show() // show progress dialog
        try {
            val paramObject = JsonObject()
            paramObject.addProperty("full_name", full_name)
            paramObject.addProperty("email", email)
            paramObject.addProperty("password", password)
            Api_NGSI.postRegistration()?.registration(paramObject)
                ?.enqueue(object : Callback<POST_Registration?> {
                    override fun onResponse(
                        call: Call<POST_Registration?>,
                        response: Response<POST_Registration?>
                    ) {
                        if (response.isSuccessful) {
                            val jsonObject = JSONObject(Gson().toJson(response.body()))
                            Toast.makeText(this@RegisterActivity, "" + jsonObject.getString("message"), Toast.LENGTH_LONG).show()
                            Log.d("Signup Data", response.body().toString())
                            progressDialog.dismiss()
                            finish()
                        } else {
                            try {
                                assert(response.errorBody() != null)
                                response.errorBody()?.let { Log.e("ERROR2", it.string()) }
                                Toast.makeText(this@RegisterActivity, "Check your crendentials, User invalid registeration", Toast.LENGTH_LONG).show()
                                response.errorBody()?.let { Log.e("ERROR", it.string()) }
                                progressDialog.dismiss()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                    }

                    override fun onFailure(call: Call<POST_Registration?>, t: Throwable) {
                        Log.e("Fail response", Arrays.toString(t.stackTrace))
                        progressDialog.dismiss()
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkerText(name: String, email: String, pword: String, pword2: String) {
        //*****************************Do Checking if text is empty***********************
        if(TextUtils.isEmpty(name)
            || TextUtils.isEmpty(email)
            || TextUtils.isEmpty(pword)
            || TextUtils.isEmpty(pword2)
            || pword != pword2) {
            if (TextUtils.isEmpty(name)) { et_fullname?.setError("Please fill this") }
            if (TextUtils.isEmpty(email)) { et_email?.setError("Please fill this") }
            if (TextUtils.isEmpty(pword)) { et_password?.setError("Please fill this") }
            if (TextUtils.isEmpty(pword2)) { et_password2?.setError("Please fill this") }
            if (pword != pword2) { Toast.makeText(this@RegisterActivity, "password and confirm password mismatch", Toast.LENGTH_LONG).show() }
        } else { signUp(name, email, pword)}
        //********************************************************************************
    }

}