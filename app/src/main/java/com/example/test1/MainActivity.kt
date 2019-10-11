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
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import android.util.TypedValue


class MainActivity : AppCompatActivity() {

    private val ERROR = "Error"
    private val DECIMAL_FORMAT = "#,###.#####"

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
    var isNumberEmpty = true
    var isFullClear = false
    var strForTVMain: String = ""
    var stSecondNumber: String = ""


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


        //TODO posmotret kak eto sdelano v drugih proektah
        val gestureDetector =
            GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent?): Boolean {
                    Log.d("myApp", "double tap")
                    return true
                }
            })
        view.setOnTouchListener { _, event -> gestureDetector.onTouchEvent(event) }

    }


    fun onNumber(view: View) {
        buttonAC.text = "C"
        if (isAfterEqual) {
            Log.d("ONNUMBER", "isAfterEqual")
            strForTVMain = ""
            isAfterEqual = false
        }
        if (!isLastOfAllNumeric) {
            Log.d("ONNUMBER", "isLastOfAllNumeric")
            tvMain.text = ""
        }
//        }
        if (checkForLastOperator()) {
            Log.d("ONNUMBER", "checkForLastOperator")
            tvMain.text = ""
        }
        isLastOperator = false
        isFullClear = false
        forTvMain(view)
        isLastOfAllNumeric = true
    }


//    private fun hueChange(c: Int, deg: Int): Int {
//        val hsv = FloatArray(3)       //array to store HSV values
//        Color.colorToHSV(c, hsv) //get original HSV values of pixel
//        hsv[0] = hsv[0] + deg                //add the shift to the HUE of HSV array
//        hsv[0] = hsv[0] % 360                //confines hue to values:[0,360]
//        return Color.HSVToColor(Color.alpha(c), hsv)
//    }


    private fun forTvMain(view: View) {
        isNumberEmpty = false
        val button = view as Button
        Log.d("HEEEEELP", "$strForTVMain")
        if (strForTVMain.length < 9) {
            strForTVMain += button.text.toString()
            Log.d("HEEEEELP", "$strForTVMain")
            if (strForTVMain.contains(".0")) {
                val helper = strForTVMain.replace(".", ",")
                Log.d("FORTVMAIN", "helper:$helper")
                tvMain.text = helper
                return
            }
            val beautyStr = strForTVMain.toDouble()
            Log.d("STM", "strForTVMain.toDouble: $beautyStr")
            tvMain.text = decimalHelper(beautyStr, DECIMAL_FORMAT)
            Log.d("STM", "strForTVMain: $strForTVMain")
        } else return
    }


    private fun decimalHelper(number: Double, formatString: String): String {
        val formatSymbols = DecimalFormatSymbols(Locale.ENGLISH)
        formatSymbols.decimalSeparator = ','
        formatSymbols.groupingSeparator = ' '
        val formatter = DecimalFormat(formatString, formatSymbols)
        return formatter.format(number)
    }


    private fun checkForLastOperator() = when {
        isLastOperator -> {
            Log.d("stm", "isNotEmpty is done")
            tryError = false
            saveNumber()
            Log.d("stm", "FN: $firstNumber or SN:$secondNumber")
            true
        }
        else -> false
    }


    fun onDecimal(view: View) {
        Log.d(
            "onDecimal",
            "isNumberEmpty :$isNumberEmpty and strFor: $strForTVMain, isLastOfAllNumeric: $isLastOfAllNumeric, isDPhere: $isDPhere"
        )
        if (isAfterEqual) {
            onClear(view)
        }
        if (isNumberEmpty) {
            tvMain.append((view as Button).text)
            strForTVMain = "0."
            isDPhere = true
            isNumberEmpty = false
        }
        // pri 0 + 0,75 = 0,75 = 75
        if (checkForLastOperator()) {
            Log.d("onDecimal", "checkForLastOperator, isNumberEmpty: $isNumberEmpty")
            tvMain.text = "0,"
            strForTVMain = "0."
            isLastOperator = false
            // postavil isFirstNumber = false i teper posle onCleat eta tema ne rabotaet ??? 10.10.19 - hz o chem eto bilo, vrode pofiksil

            isFirstNumber = false
            return
        }
        // isLastOfAllNumeric нужен, иначе крашится при ", ="
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
        if (strForTVMain.isEmpty()) {
            return
        }
        if (strForTVMain.contains(".")) {
            strForTVMain = (strForTVMain.toDouble() * -1).toString()
        } else {
            strForTVMain = (strForTVMain.toInt() * -1).toString()
        }
        Log.d("ONPLUSMINUS", "strForTVMAIN: $strForTVMain")
        tvMain.text = decimalHelper(strForTVMain.toDouble(), DECIMAL_FORMAT)
    }


    fun onClear(view: View) {
        secondNumber = 0.0
        tvMain.text = "0"
        isDPhere = false
        strForTVMain = ""
        buttonAC.text = "AC"
        isNumberEmpty = true
        isPercentage = false
        Log.d("onClear", "isFullClear: $isFullClear")
        Log.d("steelwsky", "***********SN IS CLEARED***********")
        if (isFirstNumber || isFullClear) {
            buttonAC.text = "AC"
            isFirstNumber = true
            firstNumber = 0.0
            isLastOfAllNumeric = false
            isLastOperator = false
            newOpr = ""
            tryError = true
            isAfterEqual = false
            tvMain.setTextSize(TypedValue.COMPLEX_UNIT_SP, 60f)
            Log.d("steelwsky", "**********************CLEARED**********************")
        }
        isFullClear = true
    }


    fun onEqual(view: View) {
        Log.d("STM", "onEqual INIT")
        if (isFullClear && isNumberEmpty) {
            strForTVMain = firstNumber.toString()
            Log.d("STM", "onEqual, isFullClear = true, strTVMain: $strForTVMain")
        }
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
            isFullClear = false
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
        Log.d("STM", "forDecimalHelper:$forDecimalHelper, ${forDecimalHelper.length}")
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

    @SuppressLint("ServiceCast")
    fun saveNumberToBuffer(view: View) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("number", tvMain.text)
        clipboard.primaryClip = clip

        Log.d("Clipboard", "we saved str: $strForTVMain")

    }

}

//        if(strForTVMain.contains(".")) {
//            val afterDot: String = strForTVMain.substringAfter(".")
//            if (afterDot.length > 5) {
//                tvMain.setTextSize(TypedValue.COMPLEX_UNIT_SP, 60f)
//            }
//        } else {
//            if (strForTVMain.length > 9) {
//                tvMain.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40f)
//            }
//        }

//      ***************************************************************************************
//--- DONE MUSTHAVE  в tvMain отправлять только готовый string... tvMain и FN/SN - разные вещи!
//--- DONE Видимо для onNumber нужно отдельно записывать число как string для tvMain и как double для внутренних расчетов ---DONE
//--- DONE запоминать лишь последний нажатый знак, например 8+-7 должно ровняться 1, так как "-" это последний знак. Сейчас ответ будет 15 ---DONE
//--- DONE отрицательные числа считаются неверно - -2+7 = -9. Так только после нажатия на "=", ибо не сохраняется новый знак, а иначе считается верно ---DONE
//--- DONE https://stackoverflow.com/questions/19694279/calculator-allowing-negative-numbers-in-calculation ???
//--- DONE Все нецелые числа, возможность расчета 63/8 с получением нецелого числа --- DONE
//--- DONE сделать новый onOperator без вызова функции onEqual и без записи в firstNumber и secondNumber ---DONE
//--- DONE Нет отличия, когда onEqual срабатывает после 5-4+ и 5+4= ... Нужно сделать так, чтобы "=" работал по другому. --- DONE
//--- DONE "=" сбрасывает strTVMain, если после него нажать на число. Если же после "=" нажать на другой знак - strTVMain не сбрасывать.
//--- DONE Надо следить, что вызвало onEqual и отталкиваться от этого. Если после onEqual нажали на число - стирать strTMMain, если на оператор - оставлять ---DONE
//--- DONE сделать "," --- DONE
//--- DONE сделать "%" проценты ---DONE
//--- DONE сделать формат # ### в textView,... при вводе числа в tvMain ---DONE
//--- DONE сделать "+-" ---DONE
//--- DONE копирование tvMain в буфер по двойному нажатию на textView (сделал по лонгклику, хз, как делать по даблтапу) ---DONE
//--- DONE AC -> C  ---DONE
//--- DONE Сделать двухэтапный сброс. Если есть знак (точнее мы уже записываем второе число), то мы можем сбросить лишь второе число и записать новое. По нажатию на "=" - операция выполнится с новым вторым числом.  ---DONE
// надо обудмать, как в начальном положении нажать на "+-" и не крашнуться. Так не делают кста. Можно просто return ---DONE
//---DONE  8 + , -> 8,5 но должно быть 8 + 0,5  --- DONE
//---DONE 0.5 * 6 = 3 -> "," 5 -> 5 Если получить ответ нажатием на "=" и после нажать на ",", то ответ сотрется после нажатия новой цифры. ---DONE
//---DONE анимацию нажатий на цифры.
//---NOT DONE максимальное количество знаков в числах! 22.1E11 ... BigDecimal? Внедрить поддержку больших вычислений, типа 885 312 * 943 042 = и показывать с e11. SDELAL INACHE, prosto umenshil text ---NOT DONE
//---NOT DONE Для операторов сделать состяние pressed (это у меня fab`ы, надо гуглить, хотя у них самих вроде норма анимации). ---NOT DONE
//---DONE A net, case takoy: 5*6 = 30 i posle etogo najat na "," i na number (6) i ostanetsya prosto 6, hotya doljno bit 30,6.
// Тут надо лезть думать насчет isAfterEqual sho? NET, tak nigde ne sdelano, OTMENA. Navernoe sdelat  obichnuyu proverku isAfterEqual. Vozmozhno eto bilo sdelano, no ya polomal ---DONE
//---NOT DONE тут можно все же сделать при начальном положении поставить "-" в tvmain и вписать его в str, но в целом можно и не делать
//---DONE не пишется число 0,0003. Точнее пишется, но нули не отображаются до тех пор, пока не нажмешь на цифру отличную от 0. forTVMAin  и decimalHelper. разбирться надо в них ---DONE

