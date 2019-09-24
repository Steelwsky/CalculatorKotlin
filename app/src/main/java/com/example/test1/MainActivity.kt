package com.example.test1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var isLastOfAllNumeric = false
    var isLastOperator = false
    var isLastDP = false
    var isFirstNumber = true
    var tryError = true
    var isAfterEqual = false
    var opr = ""
    var firstNumber: Double = 0.0
    var secondNumber: Double = 0.0

    var newOpr = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvMain.text = "0"

    }

    fun onNumber(view: View) {
        val button = view as Button
        if (!isLastOfAllNumeric) {
            tvMain.text = ""
        }
        tvMain.append((button).text)
        isLastOfAllNumeric = true
    }

    fun onDecimal(view: View) {
        if (isLastOfAllNumeric && !isLastDP) {
            tvMain.append((view as Button).text)
            isLastDP = true
            isLastOfAllNumeric = false
        } else return
    }

    fun onClear(view: View) {
        tvMain.text = "0"
        isFirstNumber = true
        isLastDP = false
        firstNumber = 0.0
        secondNumber = 0.0
        isLastOfAllNumeric = false
        isLastOperator = false
        opr = ""
        tryError = true
        Log.d("steelwsky", "***************CLEARED**********************")
    }


    fun decimalHelper(number: Double) {

    }

    //TODO запоминать лишь последний нажатый знак, например 8+-7 должно ровняться 1, так как "-" это последний знак. Сейчас ответ будет 15
    //TODO отрицательные числа считаются неверно - -2+7 = -9. Так только после нажатия на "=", ибо не сохраняется новый знак, а иначе считается верно
    // https://stackoverflow.com/questions/19694279/calculator-allowing-negative-numbers-in-calculation ???
    //TODO Все нецелые числа, возможность расчета 63/8 с получением нецелого числа
    //TODO сделать % проценты
    //TODO сделать +-
    //TODO сделать формат # ###,...

    //TODO сделать новый onOperator без вызова функции onEqual и без записи в firstNumber и secondNumber

    fun onOperator(view: View) {
        if (isAfterEqual) {
            isAfterEqual = false
            opr = fabHelper(view)
        }
        Log.d("steelwsky", "Operator is pressed")
        if (isLastOfAllNumeric) {
            isLastOfAllNumeric = false
            if (isFirstNumber) {
                tryError = false
                firstNumber = tvMain.text.toString().toDouble()
                isFirstNumber = false
                Log.d("steelwsky", "FirstNumber: $firstNumber")
            } else {
                secondNumber = tvMain.text.toString().toDouble()
                Log.d("steelwsky", "SecondNumber: $secondNumber")
                onEqual(view)
            }
            opr = fabHelper(view)
        } else return
    }


    fun onEqual(view: View) {
        if (!tryError) {
            if (!isFirstNumber) {
                secondNumber = tvMain.text.toString().toDouble()
            }
            tvMain.text = math(opr, firstNumber, secondNumber)
            firstNumber = tvMain.text.toString().toDouble()
            isFirstNumber = false
            isLastOfAllNumeric = false
            isAfterEqual = true
//            opr = "="
            return
        } else return
    }


    fun math(operation: String, first: Double, second: Double): String {
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

    // функция получения и хранения оператора
//    fun onOperatorNew(view: View) {
//        when (view) {
//            fabPlus -> newOpr = "+"
//            fabMinus -> newOpr = "-"
//            fabMulti -> newOpr = "*"
//            fabDivide -> newOpr = "/"
//        }
//        Log.d("steelwsky", " HERE IS MY OPERATOR: $newOpr")
//    }
//}


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


//    fun onOperator1(view: View) {
//        Log.d("steelwsky", " before IF (SecondNumber !=0) opr is $opr")
//        if (secondNumber != 0.0) {
//            Log.d("steelwsky", "SecondNumber !=0 :$secondNumber, so opr is $opr")
//            onEqual(view)
//            return
//        } else {
//            opr = fabHelper(view)
//            if (isLastOfAllNumeric) {
//                Log.d("steelwsky", "$opr HERE")
//                isLastOfAllNumeric = false
//                if (isFirstNumber) {
//                    firstNumber = tvMain.text.toString().toDouble()
//                    Log.d("steelwsky", "TOOK FIRSTNUMBER $firstNumber")
//                    isFirstNumber = false
//                } else {
//                    secondNumber = tvMain.text.toString().toDouble()
//                    isFirstNumber = true
//                    Log.d("steelwsky", "TOOK SECONDNUMBER $secondNumber")
//                }
//                //проверка на заполненность первого и второго числа// сделано херово, надо бы иначе
//                if (firstNumber != 0.0 && secondNumber != 0.0) {
//                    onEqual(view)
//                }
//            } else return
//        }
//    }



