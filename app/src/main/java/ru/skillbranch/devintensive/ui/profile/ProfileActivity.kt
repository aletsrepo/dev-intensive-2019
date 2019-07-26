package ru.skillbranch.devintensive.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.Bender

class ProfileActivity : AppCompatActivity() {

//    lateinit var benderImage: ImageView
//    lateinit var textTxt: TextView
//    lateinit var messageEt: EditText
//    lateinit var sendBtn: ImageView
//
//    lateinit var benderObj: Bender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        Log.d("M_MainActivity", "onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("M_MainActivity","onDestroy")
    }

    override fun onStart() {
        super.onStart()
        Log.d("M_MainActivity","onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("M_MainActivity","onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.d("M_MainActivity","onStop")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("M_MainActivity","onRestart")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

//        outState.putString("STATUS", benderObj.status.name)
//        outState.putString("QUESTION", benderObj.question.name)
    }
}
