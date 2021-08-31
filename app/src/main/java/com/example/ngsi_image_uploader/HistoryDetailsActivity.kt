package com.example.ngsi_image_uploader

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ngsi_image_uploader.Activities.ImageViewer
import com.example.ngsi_image_uploader.Responses.GET_History
import com.example.ngsi_image_uploader.retrofit.adapter.Api_NGSI
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max
import kotlin.math.min

class HistoryDetailsActivity : AppCompatActivity() {

    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var scaleFactor = 1.0f

    //INIT//
    private var string5: String? = null //Cheque Image URL
    private var string6: String? = null //Deposit Image URL
    ///
    private var img_cheque: ImageView? = null
    private var img_depositslip: ImageView? = null
    var et_bank_number: EditText? = null
    var et_transaction_number: EditText? = null
    var et_name: EditText? = null
    var et_email: EditText? = null
    var et_contact: EditText? = null
    var et_amount: EditText? = null
    var et_datetime: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_historydetails)
        //INIT
        img_cheque = findViewById(R.id.img_cheque)
        img_cheque?.setOnClickListener{
            val intent = Intent(this@HistoryDetailsActivity, ImageViewer::class.java)
            intent.putExtra("ImageURL", string5)
            startActivity(intent)
        }
        img_depositslip = findViewById(R.id.img_depositslip)
        img_depositslip?.setOnClickListener{
            val intent = Intent(this@HistoryDetailsActivity, ImageViewer::class.java)
            intent.putExtra("ImageURL", string6)
            startActivity(intent)
        }
        et_bank_number = findViewById(R.id.et_bank_number)
        et_transaction_number = findViewById(R.id.et_transaction_number)
        et_name = findViewById(R.id.et_name)
        et_email = findViewById(R.id.et_email)
        et_contact = findViewById(R.id.et_contact)
        et_amount = findViewById(R.id.et_amount)
        et_datetime = findViewById(R.id.et_datetime)
        //
        //Bundle
        val BankRefNumber:String = intent.getStringExtra("BankRefNumber").toString()
        val ChequeNo:String = intent.getStringExtra("ChequeNo").toString()
        val Username:String = intent.getStringExtra("Username").toString()

        val MobileNo:String = intent.getStringExtra("MobileNo").toString()
        val Amount:String = intent.getStringExtra("Amount").toString()
        val Status:String = intent.getStringExtra("Status").toString()
        val Email:String = intent.getStringExtra("Email").toString()
        val DateTime: String = intent.getStringExtra("DateTime").toString()

        et_bank_number?.setText(BankRefNumber)
        et_bank_number?.isEnabled = false

        et_transaction_number?.setText(ChequeNo)
        et_transaction_number?.isEnabled = false

        et_name?.setText(Username)
        et_name?.isEnabled = false

        et_email?.setText(Email)
        et_email?.isEnabled = false

        et_contact?.setText(MobileNo)
        et_contact?.isEnabled = false

        et_amount?.setText(Amount)
        et_amount?.isEnabled = false

        et_datetime?.setText(getDateTime(DateTime))
        et_datetime?.isEnabled = false
        ///
        getHistoryData(Email, BankRefNumber)
    }

    //*********************************************************************************************************************
    // API FUNCTION
    private fun getHistoryData(email: String, bank_reference_number: String) {
        // display a progress dialog
        val progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false) // set cancelable to false
        progressDialog.setMessage("Please Wait") // set message
        progressDialog.show() // show progress dialog
        try {
            Api_NGSI.getHistoryAPI()?.getHistoryData(email, bank_reference_number)
                ?.enqueue(object : Callback<GET_History?> {
                    override fun onResponse(
                        call: Call<GET_History?>,
                        response: Response<GET_History?>
                    ) {
                        if (response.isSuccessful) {
                            val jsonObject = JSONObject(Gson().toJson(response.body()))
                            Log.d("login Status", "" + response.body().toString())
                            val string1: String = jsonObject.getJSONArray("data").getJSONObject(0).getString("full_name")
                            val string2: String = jsonObject.getJSONArray("data").getJSONObject(0).getString("email")
                            val string3: String = jsonObject.getJSONArray("data").getJSONObject(0).getString("mobile_num")
                            val string4: String = jsonObject.getJSONArray("data").getJSONObject(0).getString("cheque_num")
                            string5 = jsonObject.getJSONArray("data").getJSONObject(0).getString("cheque_image")
                            string6 = jsonObject.getJSONArray("data").getJSONObject(0).getString("deposit_image")
                            val string7: String = jsonObject.getJSONArray("data").getJSONObject(0).getString("bank_reference_number")
                            Log.d("string5", "" + string5)
                            Log.d("string6", "" + string6)
                            Picasso.get()
                                .load(string5)
                                .resize(1000, 1000)
                                .into(img_cheque)
                            Picasso.get()
                                .load(string6)
                                .resize(1000, 1000)
                                .into(img_depositslip)
                            progressDialog.dismiss()
                        } else {
                            try {
                                assert(response.errorBody() != null)
                                response.errorBody()?.let { Log.e("ERROR2", it.string()) }
                                Toast.makeText(this@HistoryDetailsActivity, "Check your input, User does not exists", Toast.LENGTH_LONG).show()
                                response.errorBody()?.let { Log.e("ERROR", it.string()) }
                                progressDialog.dismiss()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                    }

                    override fun onFailure(call: Call<GET_History?>, t: Throwable) {
                        Log.d("response", Arrays.toString(t.stackTrace))
                        progressDialog.dismiss()
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    //*************************************************************************************************************

    private fun getDateTime(s: String): String? {
        return try {
            val sdf = SimpleDateFormat("MM/dd/yyyy")
            //Change to double first because of the value
            val d: Double = s.trim().toDouble()
            //Now convert to long
            val l = d.toLong()
            //Do Conversion
            val netDate = Date(l * 1000)
            Log.d("epochTime", "" + sdf.format(netDate).toString())
            sdf.format(netDate)
        } catch (e: Exception) {
            Log.d("epochTimeEror", "" + e.toString())
            e.toString()
        }
    }
}