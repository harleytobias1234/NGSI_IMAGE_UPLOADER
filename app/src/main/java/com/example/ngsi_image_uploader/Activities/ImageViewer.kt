package com.example.ngsi_image_uploader.Activities

import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.ngsi_image_uploader.R
import com.squareup.picasso.Picasso
import kotlin.math.max
import kotlin.math.min

class ImageViewer : AppCompatActivity() {
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var scaleFactor = 1.0f

    var imageView: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_image_viewer)
        imageView = findViewById(R.id.imageView)
        val ImageURL:String = intent.getStringExtra("ImageURL").toString()
        Picasso.get()
            .load(ImageURL)
            .resize(1000, 1000)
            .into(imageView)
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
    }
    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(motionEvent)
        return true
    }
    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            scaleFactor *= scaleGestureDetector.scaleFactor
            scaleFactor = max(0.1f, min(scaleFactor, 10.0f))
            imageView?.scaleX = scaleFactor
            imageView?.scaleY = scaleFactor
            return true
        }
    }
}