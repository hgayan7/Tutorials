package com.mercury.retrofitkotlintutorial.View

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mercury.retrofitkotlintutorial.Adapter.MarsAdapter
import com.mercury.retrofitkotlintutorial.R
import com.mercury.retrofitkotlintutorial.ViewModel.MarsViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var marsViewModel : MarsViewModel
    private lateinit var marsAdapter : MarsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        marsViewModel = ViewModelProviders.of(this).get(MarsViewModel::class.java)
        setUI()
    }

    private fun setUI() {
        setActionBar()
        getDataFromAPI()
    }

    private fun setActionBar(){
        val actionbar: ActionBar? = supportActionBar
        actionbar?.title = "Mars Photos"
        actionbar?.elevation = 4.toFloat()
        actionbar?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(this,
                    R.color.colorPrimary
                ))
        )
    }

    private fun getDataFromAPI() {
        marsViewModel.data.observe(this, Observer { data ->
            marsAdapter = MarsAdapter(photos = data.photos)
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerView.adapter = marsAdapter
        })
    }
}
