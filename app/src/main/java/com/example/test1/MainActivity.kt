package com.example.test1

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*


class MainActivity : AppCompatActivity() {

    val ERROR = "Error"
    val DECIMAL_FORMAT = "#,###.#####"

    var isLastOfAllNumeric = false
    var isLastOperator = false
    var isDPhere = false
    var isFirstNumber = true
    var tryError = true
    var isAfterEqual = false
    var firstNumber: Double = 0.0
    var secondNumber: Double = 0.0
    var isPercentage = false
    var newOpr = ""
    var isNoNumber = false

    //firstNumber as a String
    var strForTVMain: String = ""
    var stSecondNumber: String = ""

    //TODO MUSTHAVE  в tvMain отправлять только готовый string... tvMain и FN/SN - разные вещи! --- DONE
    // Видимо для onNumber нужно отдельно записывать число как string для tvMain и как double для внутренних расчетов ---DONE
    //TODO запоминать лишь последний нажатый знак, например 8+-7 должно ровняться 1, так как "-" это последний знак. Сейчас ответ будет 15 ---DONE
    //TODO отрицательные числа считаются неверно - -2+7 = -9. Так только после нажатия на "=", ибо не сохраняется новый знак, а иначе считается верно ---DONE
    //https://stackoverflow.com/questions/19694279/calculator-allowing-negative-numbers-in-calculation ???
    //TODO Все нецелые числа, возможность расчета 63/8 с получением нецелого числа --- DONE
    //TODO сделать новый onOperator без вызова функции onEqual и без записи в firstNumber и secondNumber ---DONE
    //TODO Нет отличия, когда onEqual срабатывает после 5-4+ и 5+4= ... Нужно сделать так, чтобы "=" работал по другому. --- DONE
    // "=" сбрасывает strTVMain, если после него нажать на число. Если же после "=" нажать на другой знак - strTVMain не сбрасывать.
    //TODO Надо следить, что вызвало onEqual и отталкиваться от этого. Если после onEqual нажали на число - стирать strTMMain, если на оператор - оставлять ---DONE
    //TODO сделать "," --- DONE
    //TODO сделать "%" проценты ---DONE
    //TODO сделать формат # ### в textView,... при вводе числа в tvMain ---DONE
    //TODO сделать "+-" ---DONE

    //      ***************************************************************************************
    //TODO максимальное количество знаков в числах! 22.1E11
    //TODO BigDecimal? Внедрить поддержку больших вычислений, типа 885 312 * 943 042 = и показывать с e11. Не столь обязательная штука.
    //TODO копирование tvMain в буфер по двойному нажатию на textView
    //TODO Сделать двухэтапный сброс. Если есть знак (точнее мы уже записываем второе число), то мы можем сбросить лишь второе число и записать новое. По нажатию на "=" - операция выполнится с новым вторым числом.
    //TODO AC -> C



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvMain.text = "0"

    }

    fun letMe(view: View) {
        view.setOnLongClickListener() {
            saveNumberToBuffer(view)
            Toast.makeText(this, "Copied", Toast.LENGTH_SHORT).show()
            true
        }

    }


    @SuppressLint("ServiceCast")
    fun saveNumberToBuffer(view: View) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("number", tvMain.text)
        clipboard.primaryClip = clip

        Log.d("Clipboard", "we saved str: $strForTVMain")

    }


    fun onNumber(view: View) {
        if (isAfterEqual) {
            strForTVMain = ""
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


    private fun forTvMain(view: View) {
        val button = view as Button
        Log.d("HEEEEELP", "$strForTVMain")
        strForTVMain += button.text.toString()
        Log.d("HEEEEELP", "$strForTVMain")
        val beautyStr = strForTVMain.toDouble()
        Log.d("STM", "strForTVMain.toDouble: $beautyStr")
        tvMain.text = decimalHelper(beautyStr, DECIMAL_FORMAT)
        Log.d("STM", "strForTVMain: $strForTVMain")
    }


    private fun checkForLastOperator() = when {
        isLastOperator -> {
            Log.d("stm", "isNotEmpty is done")
//            isNoNumber = true
            tryError = false
            saveNumber()
            Log.d("stm", "FN: $firstNumber or SN:$secondNumber")
            true
        }
        else -> false
    }

    fun onDecimal(view: View) {
        if (isLastOfAllNumeric && !isDPhere) {
            tvMain.append((view as Button).text)
            strForTVMain += "."
            Log.d("HEEEEELP", "$strForTVMain")
            isDPhere = true
        } else return
    }

    fun onPercentage(view: View) {
        isPercentage = true
        if (isFirstNumber) {
            isFirstNumber = false
            secondNumber = 0.01
            newOpr = "*"
            onEqual(view)
        } else {
            if (newOpr.equals("+") || newOpr.equals("-")) {
                secondNumber = firstNumber * strForTVMain.toDouble() * 0.01
                Log.d("STM", "onPercentage, else +++ -> SN: $secondNumber")
            } else {
                secondNumber = strForTVMain.toDouble() * 0.01
                Log.d("STM", "onPercentage, else *** -> SN: $secondNumber")
            }
            tvMain.text = decimalHelper(secondNumber, DECIMAL_FORMAT)
        }
    }

    fun onPlusMinus(view: View) {
        if (strForTVMain.contains(".")) {
            strForTVMain = (strForTVMain.toDouble() * -1).toString()
        } else {
            strForTVMain = (strForTVMain.toInt() * -1).toString()
        }
        Log.d("ONPLUSMINUS", "strForTVMAIN: $strForTVMain")
        tvMain.text = decimalHelper(strForTVMain.toDouble(), DECIMAL_FORMAT)
    }

    fun onClear(view: View) {
        tvMain.text = "0"
        isFirstNumber = true
        isDPhere = false
        firstNumber = 0.0
        secondNumber = 0.0
        isLastOfAllNumeric = false
        isLastOperator = false
        newOpr = ""
        tryError = true
        strForTVMain = ""
        isAfterEqual = false
        isPercentage = false
        Log.d("steelwsky", "***************CLEARED**********************")
    }


    private fun decimalHelper(number: Double, formatString: String): String {
        val formatSymbols = DecimalFormatSymbols(Locale.ENGLISH)
        formatSymbols.decimalSeparator = ','
        formatSymbols.groupingSeparator = ' '
        val formatter = DecimalFormat(formatString, formatSymbols)
        return formatter.format(number)
    }

    fun onEqual(view: View) {
        Log.d("STM", "onEqual INIT")
        if (!isFirstNumber && !isPercentage) {
            secondNumber = strForTVMain.toDouble()
            Log.d("STM", "onEqual !isFirstNumber")
        }
        if (strForTVMain.equals("")) {
            onClear(view)
            Log.d("STM", "strTVMain = null, onEqual EXIT")
            return
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
            isDPhere = false
            isPercentage = false
            Log.d("STM", "onEqual EXIT")
            tryError = true
            return
        } else {
            tryError = false
            firstNumber = strForTVMain.toDouble()
            isFirstNumber = false
            isLastOperator = false
            Log.d("STM", "tryError is now FALSE")
            onEqual(view)
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


    fun onOperator(view: View) {
        isDPhere = false
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
        Log.d(" BEFORE SAVENUMBER", "strMain: $strForTVMain and FN:$firstNumber, SN:$secondNumber")
        if (isFirstNumber) {
            if (strForTVMain.equals("")) strForTVMain = "0"
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
