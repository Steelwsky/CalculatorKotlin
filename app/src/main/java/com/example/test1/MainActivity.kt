package com.example.test1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import java.math.RoundingMode
import java.text.DecimalFormat


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
    var isNeededToCheck = false


    //TODO запоминать лишь последний нажатый знак, например 8+-7 должно ровняться 1, так как "-" это последний знак. Сейчас ответ будет 15 ---DONE
    //TODO отрицательные числа считаются неверно - -2+7 = -9. Так только после нажатия на "=", ибо не сохраняется новый знак, а иначе считается верно ---DONE
    // https://stackoverflow.com/questions/19694279/calculator-allowing-negative-numbers-in-calculation ???
    //TODO Все нецелые числа, возможность расчета 63/8 с получением нецелого числа
    //TODO сделать % проценты
    //TODO сделать +-
    //TODO сделать формат # ###,...
    //TODO копирование tvMain в буфер по двойному нажатию

    //TODO сделать новый onOperator без вызова функции onEqual и без записи в firstNumber и secondNumber ---DONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvMain.text = "0"

    }

    // даблять, не записывается больше 1значного числа после нажатия на оператор: 7+ 89 = 15 -> 7+8 (9)
    fun onNumber(view: View) {
        if (checkForLastOperator()) {
            tvMain.text = ""
        }
        val button = view as Button
        if (!isLastOfAllNumeric) {
            Log.d("@@@@@@", "isLastNumeric")
            tvMain.text = ""
        }
        tvMain.append((button).text)
        isLastOfAllNumeric = true
    }


    private fun checkForLastOperator() = when {
        newOpr.isNotEmpty() -> {
            Log.d("stm", "isNotEmpty is done")
            isNeededToCheck = true
            tryError = false
            saveNumber()
            Log.d("stm", "FN: $firstNumber or SN:$secondNumber")
//            tvMain.text = ""
            //Проблема тут, надо как то сохранить старый знак для счета, но при этом не обнулять, когда пытаешься написать больше одного знака для SN
            newOpr = ""
            true
        }
        else -> false
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
        newOpr = ""
        tryError = true
        Log.d("steelwsky", "***************CLEARED**********************")
    }

    private fun decimalHelper(number: Double): Double {
        val df = DecimalFormat("#,###.#####")
        df.roundingMode = RoundingMode.CEILING
        val after = df.format(number).toDouble()
        Log.d("STM", "decimalHelper: $after")
        return after
    }


    fun onEqual(view: View) {
        if (!tryError) {
            if (!isFirstNumber) {
                secondNumber = tvMain.text.toString().toDouble()
            }
            Log.d(
                "STM",
                "BEFORE MATHFUN newOpr: $newOpr  firstNumber: $firstNumber  secondNumber: $secondNumber"
            )
            tvMain.text = math(newOpr, firstNumber, secondNumber).toString()
            newOpr = ""
//            isFirstNumber = false
            isLastOfAllNumeric = false
            isFirstNumber = true
            isAfterEqual = true
            return
        } else return
    }


    private fun math(operation: String, first: Double, second: Double): Double {
        Log.d("steelwsky", "mathINIT, newOpr is: $newOpr")
        when (operation) {
            "+" -> {
                firstNumber = (first + second)
                Log.d("steelwsky", "WeAreInside  $first + $second = $firstNumber")
            }
            "-" -> {
                firstNumber = (first - second)
                Log.d("steelwsky", "WeAreInside  $first - $second = $firstNumber")
            }
            "*" -> {
                firstNumber = (first * second)
                Log.d("steelwsky", "WeAreInside  $first * $second = $firstNumber")
            }
            "/" -> {
                firstNumber = (first / second)
                Log.d("steelwsky", "WeAreInside  $first / $second = $firstNumber")
            }
        }
        isFirstNumber = false
        Log.d("steelwsky", "mathEND   $firstNumber")
        val forDecimalHelper = decimalHelper(firstNumber)
        Log.d("STM", "forDecimalHelper: $forDecimalHelper")
        return forDecimalHelper
    }


    // функция получения и хранения оператора
    fun onOperator(view: View) {
        if (!isFirstNumber) {
            onEqual(view)
        }
        when (view) {
            fabPlus -> newOpr = "+"
            fabMinus -> newOpr = "-"
            fabMulti -> newOpr = "*"
            fabDivide -> newOpr = "/"
        }
        Log.d("steelwsky", "HERE IS MY OPERATOR: $newOpr")
    }

    private fun saveNumber() {
        if (isFirstNumber) {
            firstNumber = tvMain.text.toString().toDouble()
            isFirstNumber = false
        } else {
            secondNumber = tvMain.text.toString().toDouble()
            isFirstNumber = true
        }
    }


}


//    fun math(operation: String, first: Double, second: Double): String {
//        var finalNumber = ""
//        Log.d("steelwsky", "mathINIT")
//        if (operation == "+") {
//            finalNumber = (first + second).toString()
//            Log.d("steelwsky", "WeAreInside  $first + $second = $finalNumber")
//        } else if (operation == "-") {
//            finalNumber = (first - second).toString()
//            Log.d("steelwsky", "WeAreInside  $first - $second = $finalNumber")
//        } else if (operation == "*") {
//            finalNumber = (first * second).toString()
//            Log.d("steelwsky", "WeAreInside  $first * $second = $finalNumber")
//        }
//
//        Log.d("steelwsky", "mathEND   $finalNumber")
//        return finalNumber
//    }

//    fun onOperator1(view: View) {
//        if (isAfterEqual) {
//            isAfterEqual = false
//            opr = fabHelper(view)
//        }
//        Log.d("steelwsky", "Operator is pressed")
//        if (isLastOfAllNumeric) {
//            isLastOfAllNumeric = false
//            if (isFirstNumber) {
//                tryError = false
//                firstNumber = tvMain.text.toString().toDouble()
//                isFirstNumber = false
//                Log.d("steelwsky", "FirstNumber: $firstNumber")
//            } else {
//                secondNumber = tvMain.text.toString().toDouble()
//                Log.d("steelwsky", "SecondNumber: $secondNumber")
//                onEqual(view)
//            }
//            opr = fabHelper(view)
//        } else return
//    }
