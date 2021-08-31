package com.example.ngsi_image_uploader

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ngsi_image_uploader.Adapters.RvHistoryAdapter
import com.example.ngsi_image_uploader.Responses.GET_History
import com.example.ngsi_image_uploader.retrofit.adapter.Api_NGSI
import com.example.ngsi_image_uploader.utilities.RecyclerViewItemClickListener
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class HistoryActivity : AppCompatActivity() {
    private var recycler_view: RecyclerView? = null
    private var txt_informNoData: TextView? = null
    private var linearLayout: LinearLayout? = null

    private var mArray1_fullname: ArrayList<String> = ArrayList<String>()
    private var mArray2_email: ArrayList<String> = ArrayList<String>()
    private var mArray3_mobileNo: ArrayList<String> = ArrayList<String>()
    private var mArray4_chequeNo: ArrayList<String> = ArrayList<String>()
    private var mArray5_chequeImg: ArrayList<String> = ArrayList<String>()
    private var mArray6_depositImg: ArrayList<String> = ArrayList<String>()
    private var mArray7_bankRefNo: ArrayList<String> = ArrayList<String>()
    private var mArray8_amount: ArrayList<String> = ArrayList<String>()
    private var mArray9_transactionDate: ArrayList<String> = ArrayList<String>()

    private var historyContainer: ArrayList<JSONArray> = ArrayList<JSONArray>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_history)
        //INIT
        //Bundle
        val Email:String = intent.getStringExtra("Email").toString()
        ///
        txt_informNoData = findViewById(R.id.txt_informNoData)
        linearLayout = findViewById(R.id.linearLayout)
        recycler_view = findViewById(R.id.recyclerView)
        recycler_view?.addOnItemTouchListener(
            RecyclerViewItemClickListener(this, recycler_view!!, object : AdapterView.OnItemClickListener,
                RecyclerViewItemClickListener.OnItemClickListener {
                override fun onItemClick(view: View?, position: Int) {
                    val intent = Intent(this@HistoryActivity, HistoryDetailsActivity::class.java)
                    intent.putExtra("BankRefNumber", mArray7_bankRefNo.get(position))
                    intent.putExtra("ChequeNo", mArray4_chequeNo.get(position))
                    intent.putExtra("Username", mArray1_fullname.get(position))
                    intent.putExtra("MobileNo", mArray3_mobileNo.get(position))
                    intent.putExtra("Email", Email)
                    intent.putExtra("Amount", mArray8_amount.get(position))
                    intent.putExtra("DateTime", mArray9_transactionDate.get(position))
                    startActivity(intent)
                }

                override fun onLongItemClick(view: View?, position: Int) {
                    // do whatever
                }

                override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    // Do Whatever
                }
            })
        )
        //

        getHistoryData(Email, "")
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
                            for (i in 0 until jsonObject.getJSONArray("data").length()) {
                                val string1: String = jsonObject.getJSONArray("data").getJSONObject(i).getString("full_name")
                                val string2: String = jsonObject.getJSONArray("data").getJSONObject(i).getString("email")
                                val string3: String = jsonObject.getJSONArray("data").getJSONObject(i).getString("mobile_num")
                                val string4: String = jsonObject.getJSONArray("data").getJSONObject(i).getString("cheque_num")
                                val string5: String = jsonObject.getJSONArray("data").getJSONObject(i).getString("cheque_image")
                                val string6: String = jsonObject.getJSONArray("data").getJSONObject(i).getString("deposit_image")
                                val string7: String = jsonObject.getJSONArray("data").getJSONObject(i).getString("bank_reference_number")
                                val string8: String = jsonObject.getJSONArray("data").getJSONObject(i).getString("amount")
                                val string9: String = jsonObject.getJSONArray("data").getJSONObject(i).getString("transaction_date")
                                mArray1_fullname.add(string1)
                                mArray2_email.add(string2)
                                mArray3_mobileNo.add(string3)
                                mArray4_chequeNo.add(string4)
                                mArray5_chequeImg.add(string5)
                                mArray6_depositImg.add(string6)
                                mArray7_bankRefNo.add(string7)
                                mArray8_amount.add(string8)
                                mArray9_transactionDate.add(string9)
                            }
                            recycler_view?.adapter = RvHistoryAdapter(this@HistoryActivity,
                                historyContainer,
                                mArray1_fullname,
                                mArray2_email,
                                mArray3_mobileNo,
                                mArray4_chequeNo,
                                mArray5_chequeImg,
                                mArray6_depositImg,
                                mArray7_bankRefNo,
                                mArray8_amount,
                                mArray9_transactionDate)
                            recycler_view?.layoutManager = LinearLayoutManager(this@HistoryActivity)
                            recycler_view?.setHasFixedSize(true)
                            if (mArray1_fullname.size == 0) {
                                linearLayout?.visibility = View.INVISIBLE
                                txt_informNoData?.visibility = View.VISIBLE
                            }
                            else {
                                linearLayout?.visibility = View.VISIBLE
                                txt_informNoData?.visibility = View.INVISIBLE
                            }
                            progressDialog.dismiss()
                        } else {
                            try {
                                assert(response.errorBody() != null)
                                response.errorBody()?.let { Log.e("ERROR2", it.string()) }
                                Toast.makeText(this@HistoryActivity, "Check your input, User does not exists", Toast.LENGTH_LONG).show()
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
}