package com.example.test1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*


class MainActivity : AppCompatActivity() {

    var isLastNumeric = false
    var isLastDP = false
    var isNewState = true
    var isFirstNumber = true

    // firstNumber - последнее вводимое в tvMain
    // secondNumber - результат последних вычислений в tvMiniHelper
    var firstNumber: Int = 0
    var secondNumber: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvMain.text = "0"
//        val buttonOne = findViewById<Button>(R.id.buttonOne)
//        fabDivide.setOnClickListener(new vi)
    }

    fun onNumber(view: View) {

//        if (isFirstNumber) {
//        val numberString = button.text
//        if (isNewState) tvMain.setText("")
//        isNewState = false
//        if (isFirstNumber)
        val button = view as Button
        if (!isLastNumeric) {
            tvMain.text = ""
        }
        tvMain.append((button).text)
        isLastNumeric = true
    }

    fun onDecimal(view: View) {
        if (isLastNumeric && !isLastDP) {
            tvMain.append((view as Button).text)
            isLastDP = true
            isLastNumeric = false
        }
    }

    fun onClear(view: View) {
        tvMain.text = "0"
//        tvMiniHelper.text = ""
        isFirstNumber = true
        isLastDP = false
//        isNewState = true
        firstNumber = 0
        secondNumber = 0
    }

    //   TODO: отделить условие when другой функцией и добавить ее выполнение здесь
    fun onOperator(view: View) {
//        val button = view as Button
        var operator = ""
        if (isLastNumeric) {
            isLastNumeric = false
            if (isFirstNumber) {
//                tvMiniHelper.text = tvMain.text
                firstNumber = tvMain.text.toString().toInt()
                isFirstNumber = false
            } else {
                secondNumber = tvMain.text.toString().toInt()
                whenHelper(view, secondNumber)

                // tut fun onEqual
//                operator = button.text.toString()
            }
        } else return

    }


    fun saveNumber() {

    }

    fun onEqual(view: View) {

    }

    fun whenHelper(v: View, sec: Int) {
        var operator = ""
        when (v) {
            fabPlus -> operator = "+"
            fabEquals -> operator = "="

        }
        if (operator.equals("+")) {
            firstNumber += sec
            tvMain.text = firstNumber.toString()
        }

    }

}


//fun onOperator(view: View) {
//    var operator = view
//        when (view) {
//            fabPlus -> operator = "+"
//            fabDivide -> operator = "/"
//
//    if (isLastNumeric) {
//        tvMiniHelper.text = tvMain.text
//        if (isFirstNumber) {
//            firstNumber = tvMain.text.toString().toInt()
//            isFirstNumber = false
//        } else {
//
//        }
//        if (operator.equals("+")) secondNumber += firstNumber
//        if (operator.equals("/")) secondNumber /= firstNumber
//
//        isFirstNumber = false
//        tvMain.text = ""
//        isLastNumeric = false
//
//    } else {
//        return
//
//    }
//}