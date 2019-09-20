package com.example.test1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var isLastNumeric = false
    var isLastDP = false
    var isNewState = true
    var isFirstNumber = true

    var opr = ""
    var firstNumber: Int = 0
    var secondNumber: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvMain.text = "0"

//        buttonPlus.setImageResource(R.drawable.ic_add_plus_button)
    }

    fun onNumber(view: View) {
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
        } else return
    }

    fun onClear(view: View) {
        tvMain.text = "0"
        isFirstNumber = true
        isLastDP = false
        firstNumber = 0
        secondNumber = 0
        isNewState = true
        isLastNumeric = false
        opr = ""
        Log.d("steelwsky", "***************CLEARED**********************")
    }


//    fun onOperator(view: View) {
//        val btn = view as Button
//        if (isLastNumeric) {
//            opr = btn.text.toString()
//            Log.d ("steelwsky", "$opr HERE")
//            isLastNumeric = false
//            if (isFirstNumber) {
//                firstNumber = tvMain.text.toString().toInt()
//                Log.d ("steelwsky", "TOOK FIRSTNUMBER")
//                isFirstNumber = false
//            } else {
//                secondNumber = tvMain.text.toString().toInt()
//                isFirstNumber = true
//                Log.d ("steelwsky", "TOOK SECONDNUMBER")
//            }
//                //проверка на заполненность первого и второго числа
//            if (firstNumber != 0 && secondNumber != 0) {
//                onEqual(view)
//            }
//        } else return
//    }


    fun onOperator(view: View) {
        Log.d("steelwsky", " before IF (SecondNumber !=0) opr is $opr")
        if (secondNumber != 0) {
            Log.d("steelwsky", "SecondNumber !=0 :$secondNumber, so opr is $opr")
            onEqual(view)
            return
        } else {
            opr = fabHelper(view)
            if (isLastNumeric) {
                Log.d("steelwsky", "$opr HERE")
                isLastNumeric = false
                if (isFirstNumber) {
                    firstNumber = tvMain.text.toString().toInt()
                    Log.d("steelwsky", "TOOK FIRSTNUMBER $firstNumber")
                    isFirstNumber = false
                } else {
                    secondNumber = tvMain.text.toString().toInt()
                    isFirstNumber = true
                    Log.d("steelwsky", "TOOK SECONDNUMBER $secondNumber")
                }
                //проверка на заполненность первого и второго числа// сделано херово, надо бы иначе
                if (firstNumber != 0 && secondNumber != 0) {
                    onEqual(view)
                }
            } else return
        }
    }

    fun onEqual(view: View) {
        if (!isFirstNumber) {
            secondNumber = tvMain.text.toString().toInt()
        }
        tvMain.text = math(opr, firstNumber, secondNumber)
        firstNumber = tvMain.text.toString().toInt()
        isFirstNumber = false
        isLastNumeric = false
        return
    }


    fun math(operation: String, first: Int, second: Int): String {
        var finalNumber = ""
        Log.d("steelwsky", "mathINIT")
        if (operation == "+") {
            finalNumber = (first + second).toString()
            Log.d("steelwsky", "WeAreInside  $first + $second = $finalNumber")
        } else if (operation == "-") {
            finalNumber = (first - second).toString()
            Log.d("steelwsky", "WeAreInside  $first - $second = $finalNumber")
        } else if (operation == "*") {
            finalNumber = (first * second).toString()
            Log.d("steelwsky", "WeAreInside  $first * $second = $finalNumber")
        }

        Log.d("steelwsky", "mathEND   $finalNumber")
        return finalNumber
    }


    fun fabHelper(v: View): String {
        var operator = ""
        when (v) {
            fabPlus -> operator = "+"
            fabMinus -> operator = "-"
            fabMulti -> operator = "*"
            fabDivide -> operator = "/"
        }
        Log.d("steelwsky", " HERE IS MY OPERATOR: $operator")
        return operator
    }

}







