package com.example.ngsi_image_uploader.Adapters

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.ngsi_image_uploader.R
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RvHistoryAdapter (context: Context,
                        mGridData: ArrayList<JSONArray>,
                        mArray1_fullname: ArrayList<String>,
                        mArray2_email: ArrayList<String>,
                        mArray3_mobileNo: ArrayList<String>,
                        mArray4_chequeNo: ArrayList<String>,
                        mArray5_chequeImg: ArrayList<String>,
                        mArray6_depositImg: ArrayList<String>,
                        mArray7_bankRefNo: ArrayList<String>,
                        mArray8_amount: ArrayList<String>,
                        mArray9_transactionDate: ArrayList<String>) :
    RecyclerView.Adapter<RvHistoryAdapter.MyViewHolder>() {
    private var mGridData: ArrayList<JSONArray> = ArrayList<JSONArray>()
    private var mArray1_fullname: ArrayList<String> = ArrayList<String>()
    private var mArray2_email: ArrayList<String> = ArrayList<String>()
    private var mArray3_mobileNo: ArrayList<String> = ArrayList<String>()
    private var mArray4_chequeNo: ArrayList<String> = ArrayList<String>()
    private var mArray5_chequeImg: ArrayList<String> = ArrayList<String>()
    private var mArray6_depositImg: ArrayList<String> = ArrayList<String>()
    private var mArray7_bankRefNo: ArrayList<String> = ArrayList<String>()
    private var mArray8_amount: ArrayList<String> = ArrayList<String>()
    private var mArray9_transactionDate: ArrayList<String> = ArrayList<String>()
    private val context: Context

    /**
     * Updates grid data and refresh grid items.
     *
     * @param mGridData
     */
    fun setGridData(mGridData: ArrayList<JSONArray>,
                    mArray1_fullname: ArrayList<String>,
                    mArray2_email: ArrayList<String>,
                    mArray3_mobileNo: ArrayList<String>,
                    mArray4_chequeNo: ArrayList<String>,
                    mArray5_chequeImg: ArrayList<String>,
                    mArray6_depositImg: ArrayList<String>,
                    mArray7_bankRefNo: ArrayList<String>,
                    mArray8_amount: ArrayList<String>,
                    mArray9_transactionDate: ArrayList<String>) {
        this.mGridData = mGridData
        this.mArray1_fullname = mArray1_fullname
        this.mArray2_email = mArray2_email
        this.mArray3_mobileNo = mArray3_mobileNo
        this.mArray4_chequeNo = mArray4_chequeNo
        this.mArray5_chequeImg = mArray5_chequeImg
        this.mArray6_depositImg = mArray6_depositImg
        this.mArray7_bankRefNo = mArray7_bankRefNo
        this.mArray8_amount = mArray8_amount
        this.mArray9_transactionDate = mArray9_transactionDate
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        // inflate the item Layout
        val v: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.rv_layout_historydetails, viewGroup, false)
        // set the view's size, margins, paddings and layout parameters
        return MyViewHolder(v) // pass the view to View Holder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // set the data in items
        //val item: JSONArray = mGridData[position]
        holder.txt_fullname.text = mArray1_fullname[position]
        holder.txt_email.text = mArray2_email[position]
        holder.txt_mobileNumber.text = mArray3_mobileNo[position]
        holder.txt_cheque_num.text = mArray4_chequeNo[position]
        holder.txt_bankRefNo.text = mArray7_bankRefNo[position]
        holder.txt_amount.text = mArray8_amount[position]
        holder.txt_transactionDate.text = getDateTime(mArray9_transactionDate[position])

    }

    override fun getItemCount(): Int {
        return mArray1_fullname.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt_fullname : TextView = itemView.findViewById(R.id.txt_fullname)
        var txt_email: TextView = itemView.findViewById(R.id.txt_email)
        var txt_mobileNumber: TextView = itemView.findViewById(R.id.txt_mobileNumber)
        var txt_cheque_num: TextView = itemView.findViewById(R.id.txt_cheque_num)
        var txt_bankRefNo: TextView = itemView.findViewById(R.id.txt_bankRefNo)
        var txt_amount: TextView = itemView.findViewById(R.id.txt_amount)
        var txt_transactionDate: TextView = itemView.findViewById(R.id.txt_transactionDate)
    }

    init {
        this.context = context
        this.mGridData = mGridData
        this.mArray1_fullname = mArray1_fullname
        this.mArray2_email = mArray2_email
        this.mArray3_mobileNo = mArray3_mobileNo
        this.mArray4_chequeNo = mArray4_chequeNo
        this.mArray5_chequeImg = mArray5_chequeImg
        this.mArray6_depositImg = mArray6_depositImg
        this.mArray7_bankRefNo = mArray7_bankRefNo
        this.mArray8_amount = mArray8_amount
        this.mArray9_transactionDate = mArray9_transactionDate
    }

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