package com.example.ngsi_image_uploader

import android.app.ProgressDialog
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ngsi_image_uploader.Adapters.CustomRecyclerViewAdapter

import com.example.ngsi_image_uploader.data.Registration
import com.example.ngsi_image_uploader.Responses.GET_Transaction
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
import com.example.ngsi_image_uploader.Activities.Login.LoginActivity
import com.example.ngsi_image_uploader.Component.SessionManager

class MainActivity : AppCompatActivity() {
    //Counters
    var testCtr: Int? = 0
    //

    //INIT//
    var txt_noOfPending: TextView? = null
    var txt_noOfComplete: TextView? = null
    var txt_informNoData: TextView? = null

    var intPending: Int? = 0
    var intComplete: Int? = 0

    lateinit var sessionManager: SessionManager
    private lateinit var array1: ArrayList<String> //BANK REF #
    private lateinit var  array2: ArrayList<String> //Username
    private lateinit var  array3: ArrayList<String> //Mobile
    private lateinit var  array4: ArrayList<String> //Amount
    private lateinit var  array5: ArrayList<String> //Status
    private var userEmail: String? = null
    ///////////////////////

    private var recycler_view: RecyclerView? = null
    private lateinit var categoriesList: ArrayList<Registration>
    private lateinit var transactionList_container: ArrayList<JSONArray>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        txt_noOfPending = findViewById(R.id.txt_noOfPending)
        txt_noOfComplete = findViewById(R.id.txt_noOfComplete)
        txt_informNoData = findViewById(R.id.txt_informNoData)

        sessionManager = SessionManager(this@MainActivity)
        recycler_view = findViewById(R.id.recyclerView)
        recycler_view?.addOnItemTouchListener(
            RecyclerViewItemClickListener(this, recycler_view!!, object : AdapterView.OnItemClickListener,
                RecyclerViewItemClickListener.OnItemClickListener {
                override fun onItemClick(view: View?, position: Int) {

                    //IF pending
                    if(array5.get(position) == "pending") {
                        val intent = Intent(this@MainActivity, ChequeActivity::class.java)
                        intent.putExtra("BankRefNumber", array1.get(position))
                        intent.putExtra("Email", userEmail)
                        intent.putExtra("Amount", array4.get(position))
                        startActivity(intent)
                    }
                    else if(array5.get(position) == "completed") {
//                        val intent = Intent(this@MainActivity, HistoryDetailsActivity::class.java)
//                        intent.putExtra("BankRefNumber", array1.get(position))
//                        intent.putExtra("Username", array2.get(position))
//                        intent.putExtra("MobileNo", array3.get(position))
//                        intent.putExtra("Amount", array4.get(position))
//                        intent.putExtra("Status", array5.get(position))
//                        intent.putExtra("Email", userEmail)
//                        startActivity(intent)
                        Toast.makeText(this@MainActivity, "Please click MyHistory for Completed Statuses", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onLongItemClick(view: View?, position: Int) {
                    // do whatever
                }

                override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    //Do Whatever
                }
            })
        )
        /////////////////////////
        try{
            getTransactionData()
        } catch (e:Exception){
            e.printStackTrace()
        }
        ////////////////////////
        //***Session Store***************************************************
        sessionManager.checkLogin()
        for(key in sessionManager.userDetails.keys){
            if(key == "email") {
                userEmail = sessionManager.userDetails[key].toString()
            }
            Log.d("Key ", "" + key + sessionManager.userDetails[key].toString())
        }
        //*****************END**********************************************
    }

//    fun btn_transaction_click(v: View){
//        val intent = Intent(this@MainActivity, TransactionActivity::class.java)
//        startActivity(intent)
//    }
    fun btn_cheque_click(v: View){
        getTransactionData()
        Toast.makeText(this, "Refresh list", Toast.LENGTH_LONG).show()
    }
    fun imageview_refresher(v: View) {
        testCtr = testCtr?.inc()
        if (testCtr == 10) {
            testCtr = 0
            val intent = Intent(this@MainActivity, ImageQualityTester::class.java)
            startActivity(intent)
        }
//        getTransactionData()
//        Toast.makeText(this, "Refresh tables", Toast.LENGTH_LONG).show()
    }
    fun btnClickLogOut(v: View){
        sessionManager.logoutUser()
        finish()
    }
    fun btnClickHistory(v: View){
        val intent = Intent(this@MainActivity, HistoryActivity::class.java)
        intent.putExtra("Email", userEmail)
        startActivity(intent)
    }
    fun listCategories(){
        // DB
        var db = dbHandler(this, "imageUploader.db", null, 1, null)

        // Fetch categories
        var categoriesCursor: Cursor? = db.getListContents()
        if (categoriesCursor != null) {
            categoriesCursor.count
            Log.e("listCategories()", "categoriesSize=" + categoriesCursor.count)
        }
        var categoriesSize: Int = categoriesCursor!!.count
        Log.d("listCategories()", "categoriesSize=" + categoriesSize)

        // Add a list of categories
        categoriesList = ArrayList<Registration>()
        while (categoriesCursor.moveToNext()) {
            var categoryTransactionNumber = categoriesCursor.getString(1)
            var categoryName = categoriesCursor.getString(2)
            var categoryEmail = categoriesCursor.getString(3)
            var categoryBase64Img = categoriesCursor.getString(4)
            var categoryContact = categoriesCursor.getString(5)
            Log.d("listCategories()", "categoryId=" + categoryTransactionNumber
                    + " categoryName=" + categoryName)
            categoriesList.add(Registration(categoryTransactionNumber,
            categoryName,
            categoryEmail,
            categoryBase64Img,
            categoryContact))

        }
    }

    //*********************************************************************************************************************
    // API FUNCTION
    private fun getTransactionData() {
        //Refresh Counters
        intComplete = 0
        intPending = 0
        //
        // display a progress dialog
//        val progressDialog = ProgressDialog(this)
//        progressDialog.setCancelable(false) // set cancelable to false
//        progressDialog.setMessage("Please Wait") // set message
//        progressDialog.show() // show progress dialog
        try {
            Api_NGSI.getTransactionAPI()?.transactionAPI()
                ?.enqueue(object : Callback<GET_Transaction?> {
                    override fun onResponse(
                        call: Call<GET_Transaction?>,
                        response: Response<GET_Transaction?>
                    ) {
                        if (response.isSuccessful) {
//                            progressDialog.dismiss()
                            try {
                                val jsonObject = JSONObject(Gson().toJson(response.body()))
                                Log.d("login Status", "" + response.body().toString())
                                //Toast.makeText(this@MainActivity, "" + jsonObject.getString("message"), Toast.LENGTH_LONG ).show()
                                transactionList_container = ArrayList<JSONArray>()

                                ///
                                array1 = ArrayList<String>()
                                array2 = ArrayList<String>()
                                array3 = ArrayList<String>()
                                array4 = ArrayList<String>()
                                array5 = ArrayList<String>()
                                ///
                                Log.d("jsonLength", "" + jsonObject.getJSONArray("data").length())
                                for (i in 0 until jsonObject.getJSONArray("data").length()) {
                                    val string1: String = jsonObject.getJSONArray("data").getJSONObject(i).getString("bank_reference_number")
                                    val string2: String = jsonObject.getJSONArray("data").getJSONObject(i).getString("username")
                                    val string3: String = jsonObject.getJSONArray("data").getJSONObject(i).getString("mobile_number")
                                    val string4: String = jsonObject.getJSONArray("data").getJSONObject(i).getString("amount")
                                    val string5: String = jsonObject.getJSONArray("data").getJSONObject(i).getString("status")
                                    if(string5 == "completed") {
                                        intComplete = intComplete?.inc()
                                        Log.d("intCOmplete", "" + intComplete.toString())
                                    }
                                    else if(string5 == "pending") {
                                        intPending = intPending?.inc()
                                    }
                                    array1.add(string1)
                                    array2.add(string2)
                                    array3.add(string3)
                                    array4.add(string4)
                                    array5.add(string5)
                                }
                                recycler_view?.adapter = CustomRecyclerViewAdapter(this@MainActivity,
                                    transactionList_container, array1, array2, array3, array4, array5)
                                recycler_view?.layoutManager = LinearLayoutManager(this@MainActivity)
                                recycler_view?.setHasFixedSize(true)
                                txt_noOfComplete?.setText(intComplete?.toString())
                                txt_noOfPending?.setText(intPending?.toString())
                                if(array1.size == 0) {
                                    txt_informNoData?.visibility = View.VISIBLE
                                    recycler_view?.visibility = View.GONE
                                    Log.d("Visible", "NoData")
                                } else {
                                    Log.d("Visible", "HasData")
                                    txt_informNoData?.visibility = View.GONE
                                    recycler_view?.visibility = View.VISIBLE
                                }
                            } catch(e: IOException) {
                                e.printStackTrace()
                            }
                        } else {
                            try {
//                                progressDialog.dismiss()
                                assert(response.errorBody() != null)
                                response.errorBody()?.let { Log.e("ERROR2", it.string()) }
                                Toast.makeText(this@MainActivity, "Check your input, User does not exists", Toast.LENGTH_LONG).show()
                                response.errorBody()?.let { Log.e("ERROR", it.string()) }

                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                    }

                    override fun onFailure(call: Call<GET_Transaction?>, t: Throwable) {
//                        progressDialog.dismiss()
                        Log.d("response", Arrays.toString(t.stackTrace))

                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    //*************************************************************************************************************

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}