package com.example.test1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*


class MainActivity : AppCompatActivity() {

    val ERROR = "Error"
    val DECIMAL_FORMAT = "#,###.#####"

    var isLastOfAllNumeric = false
    var isLastOperator = false
    var isLastDP = false
    var isFirstNumber = true
    var tryError = true
    var isAfterEqual = false
    var firstNumber: Double = 0.0
    var secondNumber: Double = 0.0

    var newOpr = ""
    var isNeededToCheck = false

    //firstNumber as a String
    var strForTVMain: String = ""
    var stSecondNumber: String = ""

    //TODO MUSTHAVE  в tvMain отправлять только готовый string... tvMain и FN/SN - разные вещи! --- DONE
    // Видимо для onNumber нужно отдельно записывать число как string для tvMain и как double для внутренних расчетов ---DONE
    //TODO запоминать лишь последний нажатый знак, например 8+-7 должно ровняться 1, так как "-" это последний знак. Сейчас ответ будет 15 ---DONE
    //TODO отрицательные числа считаются неверно - -2+7 = -9. Так только после нажатия на "=", ибо не сохраняется новый знак, а иначе считается верно ---DONE
    // https://stackoverflow.com/questions/19694279/calculator-allowing-negative-numbers-in-calculation ???
    //TODO Все нецелые числа, возможность расчета 63/8 с получением нецелого числа --- DONE
    //TODO сделать новый onOperator без вызова функции onEqual и без записи в firstNumber и secondNumber ---DONE
    //      ******************************************
    //TODO максимальное количество знаков в числах!
    //TODO BigDecimal? Внедрить поддержку больших вычислений, типа 885 312 * 943 042 = и показывать с e11. Не столь обязательная штука.
    //TODO сделать % проценты
    //TODO сделать +-
    //TODO сделать формат # ### в textView,... при вводе числа в tvMain
    //TODO копирование tvMain в буфер по двойному нажатию на textView
    //TODO AC -> C
    //TODO


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvMain.text = "0"

    }

    fun onNumber(view: View) {
        if(isAfterEqual) {
            strForTVMain =""
            isAfterEqual = false
        }
        if (!isLastOfAllNumeric) {
            tvMain.text = ""
        }
        if (checkForLastOperator()) {
            tvMain.text = ""
        }
        isLastOperator = false
        forTvMain(view)
        isLastOfAllNumeric = true
    }


    fun forTvMain(view: View) {
        val button = view as Button
        strForTVMain += button.text.toString()
        tvMain.append((button).text)
        Log.d("STM", "strForTVMain: $strForTVMain")
    }


    private fun checkForLastOperator() = when {
        isLastOperator -> {
            Log.d("stm", "isNotEmpty is done")
            isNeededToCheck = true
            tryError = false
            saveNumber()
            Log.d("stm", "FN: $firstNumber or SN:$secondNumber")
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
        newOpr = ""
        tryError = true
        strForTVMain = ""
        isAfterEqual = false
        Log.d("steelwsky", "***************CLEARED**********************")
    }


    private fun decimalHelper(number: Double, formatString: String): String {
        val formatSymbols = DecimalFormatSymbols(Locale.ENGLISH)
        formatSymbols.decimalSeparator = '.'
        formatSymbols.groupingSeparator = ' '
        val formatter = DecimalFormat(formatString, formatSymbols)
        return formatter.format(number)
    }

// TODO Нет отличия, когда onEqual срабатывает после 5-4+ и 5+4= ... Нужно сделать так, чтобы "=" работал по другому.
// "=" сбрасывает strTVMain, если после него нажать на число. Если же после "=" нажать на другой знак - strTVMain не сбрасывать.
// Надо следить, что вызвало onEqual и отталкиваться от этого. Если после onEqual нажали на число - стирать strTMMain, если на оператор - оставлять

    fun onEqual(view: View) {
        Log.d("STM", "onEqual INIT")
        if (!isFirstNumber) {
            secondNumber = strForTVMain.toDouble()
            Log.d("STM", "onEqual !isFirstNumber")
        }
        if (!tryError) {
            Log.d(
                "STM",
                "BEFORE MATHFUN newOpr: $newOpr  firstNumber: $firstNumber  secondNumber: $secondNumber"
            )
            tvMain.text = math(newOpr, firstNumber, secondNumber)
            newOpr = ""
            isLastOfAllNumeric = false
            isFirstNumber = true
            isAfterEqual = true

            Log.d("STM", "onEqual EXIT")
            return
//        } else return
        } else {
            secondNumber = 0.0
            tvMain.text = math(newOpr, firstNumber, secondNumber)
            Log.d("STM", "onEqual EXIT")
            return
        }
    }


    private fun math(operation: String, first: Double, second: Double): String {
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
                if (second != 0.0) {
                    firstNumber = (first / second)
                    Log.d("steelwsky", "WeAreInside  $first / $second = $firstNumber")
                } else
                    return ERROR
            }
        }
        isFirstNumber = false
        strForTVMain = firstNumber.toString()
        Log.d("steelwsky", "mathEND   $firstNumber")
        val forDecimalHelper = decimalHelper(firstNumber, DECIMAL_FORMAT)
        Log.d("STM", "forDecimalHelper: $forDecimalHelper")
        return forDecimalHelper
    }


    // функция получения и хранения оператора
    fun onOperator(view: View) {
        isLastOperator = true
        if (!isFirstNumber) {
            onEqual(view)
        }
        when (view) {
            fabPlus -> newOpr = "+"
            fabMinus -> newOpr = "-"
            fabMulti -> newOpr = "*"
            fabDivide -> newOpr = "/"
        }
        isAfterEqual = false
        Log.d("steelwsky", "HERE IS MY OPERATOR: $newOpr")
    }

    private fun saveNumber() {
        if (isFirstNumber) {
            firstNumber = strForTVMain.toDouble()
            Log.d("SAVENUMBER", "FN: $firstNumber")
            strForTVMain = ""
            isFirstNumber = false
        } else {
            secondNumber = stSecondNumber.toDouble()
            Log.d("SAVENUMBER", "SN: $secondNumber")
            isFirstNumber = true
        }
    }

}


//    private fun decimalHelper(number: Double): String {
//        val df = DecimalFormat("#,###.#####")
//        df.roundingMode = RoundingMode.CEILING
//        val formatSymbols = DecimalFormatSymbols(Locale.ENGLISH)
//        formatSymbols.decimalSeparator = '.'
//        formatSymbols.groupingSeparator = ' '
//        val formatter = DecimalFormat(df.toString(),formatSymbols)
//        return formatter.format(number)
//    }

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
//    private fun saveNumber() {
//        if (isFirstNumber) {
//            firstNumber = tvMain.text.toString().toDouble()
//            Log.d("SAVENUMBER", "FN: $firstNumber")
//            isFirstNumber = false
//        } else {
//            secondNumber = tvMain.text.toString().toDouble()
//            Log.d("SAVENUMBER", "SN: $secondNumber")
//            isFirstNumber = true
//        }