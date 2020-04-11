package com.mercury.barcodescanner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.otaliastudios.cameraview.frame.Frame
import kotlinx.android.synthetic.main.activity_barcode_scanner.*

class BarcodeScannerActivity : AppCompatActivity() {

    private var isQR = false
    private lateinit var barcodeDetectorOptions : FirebaseVisionBarcodeDetectorOptions
    private lateinit var barcodeDetector : FirebaseVisionBarcodeDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode_scanner)
        setActionBar()
        requestPermission()
    }
    private fun setActionBar(){
        supportActionBar?.setHomeButtonEnabled(true)
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply { setDisplayHomeAsUpEnabled(true) }
        actionbar?.title = "Barcode Scanner"
        actionbar?.elevation = 4.toFloat()
        actionbar?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(this,
                    R.color.colorPrimaryDark
                ))
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun requestPermission(){
        val cameraPermission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.CAMERA)
        val recordAudioPermission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.RECORD_AUDIO)
        if (cameraPermission != PackageManager.PERMISSION_GRANTED || recordAudioPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO),
                100)
        } else {
            showCamera()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            100 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,"Permission not granted",Toast.LENGTH_LONG).show()
                } else {
                    showCamera()
                }
            }
        }
    }

    private fun showCamera() {
        barcodeDetectorOptions = FirebaseVisionBarcodeDetectorOptions.Builder()
            .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE)
            .build()
        barcodeDetector = FirebaseVision.getInstance().getVisionBarcodeDetector(barcodeDetectorOptions)
        cameraView.setLifecycleOwner(this)
        cameraView.addFrameProcessor { frame -> processImage(getFirebaseVisionImage(frame)) }
    }

    private fun processImage(vision: FirebaseVisionImage) {
        if(!isQR){
            barcodeDetector.detectInImage(vision)
                .addOnFailureListener{e -> Log.d("Data",e.toString())}
                .addOnSuccessListener { qrInfo -> process(qrInfo) }
        }
    }

    private fun process(barCodes: List<FirebaseVisionBarcode>) {
        if(barCodes.isNotEmpty()) {
            isQR = true
            when (barCodes.first().valueType){
                FirebaseVisionBarcode.TYPE_CONTACT_INFO -> {
                    val intent = Intent(this,MainActivity::class.java)
                    intent.putExtra("fromScannerActivity",true)
                    intent.putExtra("name",barCodes.firstOrNull()?.contactInfo?.name?.formattedName ?: "NA")
                    intent.putExtra("email",barCodes.firstOrNull()?.contactInfo?.emails?.firstOrNull()?.address ?: "NA")
                    intent.putExtra("url",barCodes.firstOrNull()?.contactInfo?.urls?.firstOrNull() ?: "NA")
                    intent.putExtra("organization",barCodes.firstOrNull()?.contactInfo?.organization ?: "NA")
                    intent.putExtra("phoneNo",barCodes.firstOrNull()?.contactInfo?.phones?.firstOrNull()?.number ?: "NA")
                    startActivity(intent)
                }
            }
        }
    }

    private fun getFirebaseVisionImage(frame : Frame) : FirebaseVisionImage {
        val metadata = FirebaseVisionImageMetadata.Builder()
            .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
            .setHeight(frame.size.height)
            .setWidth(frame.size.width)
            .build()
        val data: ByteArray = frame.getData()
        return FirebaseVisionImage.fromByteArray(data,metadata)
    }
}
