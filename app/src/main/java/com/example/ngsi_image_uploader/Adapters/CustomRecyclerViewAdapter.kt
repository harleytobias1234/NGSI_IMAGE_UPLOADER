package com.example.ngsi_image_uploader.Adapters

import android.content.Context
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.ngsi_image_uploader.R
import org.json.JSONArray

class CustomRecyclerViewAdapter(context: Context,
                                mGridData: ArrayList<JSONArray>,
                                mArray1: ArrayList<String>,
                                mArray2: ArrayList<String>,
                                mArray3: ArrayList<String>,
                                mArray4: ArrayList<String>,
                                mArray5: ArrayList<String>) :
    RecyclerView.Adapter<CustomRecyclerViewAdapter.MyViewHolder>() {
    private var mGridData: ArrayList<JSONArray> = ArrayList<JSONArray>()
    private var mArray1: ArrayList<String> = ArrayList<String>()
    private var mArray2: ArrayList<String> = ArrayList<String>()
    private var mArray3: ArrayList<String> = ArrayList<String>()
    private var mArray4: ArrayList<String> = ArrayList<String>()
    private var mArray5: ArrayList<String> = ArrayList<String>()
    private val context: Context

    /**
     * Updates grid data and refresh grid items.
     *
     * @param mGridData
     */
    fun setGridData(mGridData: ArrayList<JSONArray>,
                    mArray1: ArrayList<String>,
                    mArray2: ArrayList<String>,
                    mArray3: ArrayList<String>,
                    mArray4: ArrayList<String>,
                    mArray5: ArrayList<String>) {
        this.mGridData = mGridData
        this.mArray1 = mArray1
        this.mArray2 = mArray2
        this.mArray3 = mArray3
        this.mArray4 = mArray4
        this.mArray5 = mArray5
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        // inflate the item Layout
        val v: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.custom_recyclerview_layout, viewGroup, false)
        // set the view's size, margins, paddings and layout parameters
        return MyViewHolder(v) // pass the view to View Holder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // set the data in items
        //val item: JSONArray = mGridData[position]
        holder.txt_bankRefNo.text = mArray1[position]
        holder.txt_username.text = mArray2[position]
        holder.txt_mobileNumber.text = mArray3[position]
        holder.txt_amount.text = mArray4[position]
        if(mArray5[position] == "completed") {
            holder.txt_status.setTextColor(Color.parseColor("#00A300"))
        }
        else {
            holder.txt_status.setTextColor(Color.parseColor("#077E8C"))
        }
        holder.txt_status.text = mArray5[position]

//        holder.txt_bankRefNo.text = item.getJSONObject(position).getString("bank_reference_number")
//        holder.txt_username.text = item.getJSONObject(position).getString("username")
//        holder.txt_mobileNumber.text = item.getJSONObject(position).getString("mobile_number")
//        holder.txt_amount.text = item.getJSONObject(position).getString("amount")
//        holder.txt_status.text = item.getJSONObject(position).getString("status")

    }

    override fun getItemCount(): Int {
        return mArray1.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt_bankRefNo : TextView = itemView.findViewById(R.id.txt_bankRefNo)
        var txt_username: TextView = itemView.findViewById(R.id.txt_username)
        var txt_mobileNumber: TextView = itemView.findViewById(R.id.txt_mobileNumber)
        var txt_amount: TextView = itemView.findViewById(R.id.txt_amount)
        var txt_status: TextView = itemView.findViewById(R.id.txt_status)
    }

    init {
        this.context = context
        this.mGridData = mGridData
        this.mArray1 = mArray1
        this.mArray2 = mArray2
        this.mArray3 = mArray3
        this.mArray4 = mArray4
        this.mArray5 = mArray5
    }
}


