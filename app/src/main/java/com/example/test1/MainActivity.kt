package com.example.test1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var isLastNumeric = false
    var isLastDP = false
    var isNewState = true
    var isFirstNumber = true

    var firstNumber: Int = 0
    var SecondNumber = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        val buttonOne = findViewById<Button>(R.id.buttonOne)
//        fabDivide.setOnClickListener(new vi)
    }

    fun onNumber(view: View) {
        val button = view as Button
        val numberString = button.text
        if (isNewState) tvMain.setText("")
        isNewState = false
//        if (isFirstNumber)
        tvMain.append((button).text)
        isLastNumeric = true
    }

    fun onDecimal(view: View) {
        if (isLastNumeric && !isLastDP) {
            tvMain.append((view as Button).text)
            isLastDP = true
        }
    }

    fun onOperator(view: View) {
        tvMiniHelper.text = tvMain.text
        firstNumber = tvMain.text.toString().toInt()
        tvMain.text = ""
//        if () {
//
//        }
    }

    fun saveNumber () {
//123
    }


}


