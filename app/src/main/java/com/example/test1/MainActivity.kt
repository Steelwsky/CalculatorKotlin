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
import android.util.TypedValue

private const val MATHERROR = "Error"


class MainActivity : AppCompatActivity() {

//    private var calculator = Calculator(
//        false, false, false, true,
//        true, false, 0.0, 0.0,
//        false, "", true, false, "")

    private val calculator = Calculator()

    // можно сделать immutable и вообще тут нет конструктора

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvMain.text = "0"


        val gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent?): Boolean {
                saveNumberToBuffer()
                Toast.makeText(this@MainActivity, "Copied", Toast.LENGTH_SHORT).show()
                Log.d("tvMain touch", "double tap applied")
                return true
            }
        })
        tvMain.setOnTouchListener { _, event -> gestureDetector.onTouchEvent(event) }

        tvMain.setOnLongClickListener {
            saveNumberToBuffer()
            Toast.makeText(this@MainActivity, "Copied", Toast.LENGTH_SHORT).show()
            Log.d("tvMain touch", "long click applied")
            true
        }

    }


    fun onNumber(view: View) {
        buttonAC.text = "C"
        if (calculator.isAfterEqual) {
            Log.d("ONNUMBER", "isAfterEqual")
            calculator.strForTVMain = ""
            calculator.isAfterEqual = false
        }
        if (!calculator.isLastOfAllNumeric) {
            Log.d("ONNUMBER", "isLastOfAllNumeric")
            tvMain.text = ""
        }
//        }
        if (checkForLastOperator()) {
            Log.d("ONNUMBER", "checkForLastOperator")
            tvMain.text = ""
        }
        calculator.isLastOperator = false
        calculator.isFullClear = false
        forTvMain(view)
        calculator.isLastOfAllNumeric = true
    }


    private fun forTvMain(view: View) {
        calculator.isNumberEmpty = false
        val button = view as Button
        Log.d("HEEEEELP", "strForTVMain: $calculator.strForTVMain")
        if (calculator.strForTVMain.length < 9) {
            calculator.strForTVMain += button.text.toString()
            Log.d("HEEEEELP", "strForTVMain: $calculator.strForTVMain")
            if (calculator.strForTVMain.contains(".")) {
                val helper = calculator.strForTVMain.replace(".", ",")
                Log.d("FORTVMAIN", "helper:$helper")
                tvMain.text = helper
                return
            }
            val beautyStr = calculator.strForTVMain.toDouble()
            Log.d("STM", "strForTVMain.toDouble: $beautyStr")
            tvMain.text = decimalHelper(beautyStr)
            Log.d("STM", "strForTVMain: $calculator.strForTVMain")
        } else return
    }

    private fun decimalHelper(number: Double): String {
        val dFormat = "#,###.#####"
        val formatSymbols = DecimalFormatSymbols(Locale.ENGLISH)
        formatSymbols.decimalSeparator = ','
        formatSymbols.groupingSeparator = ' '
        val formatter = DecimalFormat(dFormat, formatSymbols)
        return formatter.format(number)
    }

    private fun checkForLastOperator() = when {
        calculator.isLastOperator -> {
            Log.d("stm", "isNotEmpty is done")
            calculator.tryError = false
            saveNumber()
            Log.d("stm", "FN: $calculator.firstNumber or SN:$calculator.secondNumber")
            true
        }
        else -> false
    }

    fun onDecimal(view: View) {
        Log.d(
            "onDecimal",
            "isNumberEmpty :$calculator.isNumberEmpty and strFor: $calculator.strForTVMain, isLastOfAllNumeric: $calculator.isLastOfAllNumeric, isDPhere: $calculator.isDPhere"
        )
        if (calculator.isAfterEqual) {
            onClear(view)
        }
        if (calculator.isNumberEmpty) {
            tvMain.append((view as Button).text)
            calculator.strForTVMain = "0."
            calculator.isDPhere = true
            calculator.isNumberEmpty = false
        }
        // pri 0 + 0,75 = 0,75 = 75
        if (checkForLastOperator()) {
            Log.d("onDecimal", "checkForLastOperator, isNumberEmpty: $calculator.isNumberEmpty")
            tvMain.text = "0,"
            calculator.strForTVMain = "0."
            calculator.isLastOperator = false
            // postavil isFirstNumber = false i teper posle onCleat eta tema ne rabotaet ??? 10.10.19 - hz o chem eto bilo, vrode pofiksil
            calculator.isFirstNumber = false
            return
        }
        // isLastOfAllNumeric нужен, иначе крашится при ", ="
        if (calculator.isLastOfAllNumeric && !calculator.isDPhere) {
            tvMain.append((view as Button).text)
            calculator.strForTVMain += "."
            Log.d("HEEEEELP", "strForTVMain: $calculator.strForTVMain")
            calculator.isDPhere = true
        } else return
    }

    fun onPercentage(view: View) {
        calculator.isPercentage = true
        if (calculator.isFirstNumber) {
            calculator.isFirstNumber = false
            calculator.secondNumber = 0.01
            calculator.newOpr = "*"
            onEqual(view)
        } else {
            if (calculator.newOpr.equals("+") || calculator.newOpr.equals("-")) {
                calculator.secondNumber = calculator.firstNumber * calculator.strForTVMain.toDouble() * 0.01
                Log.d("STM", "onPercentage, else +++ -> SN: $calculator.secondNumber")
            } else {
                calculator.secondNumber = calculator.strForTVMain.toDouble() * 0.01
                Log.d("STM", "onPercentage, else *** -> SN: $calculator.secondNumber")
            }
            tvMain.text = decimalHelper(calculator.secondNumber)
        }
    }


    fun onPlusMinus(view: View) {
        if (calculator.strForTVMain.isEmpty()) {
            return
        }
        calculator.strForTVMain = if (calculator.strForTVMain.contains(".")) {
            (calculator.strForTVMain.toDouble() * -1).toString()
        } else {
            (calculator.strForTVMain.toInt() * -1).toString()
        }
        Log.d("ONPLUSMINUS", "strForTVMAIN: $calculator.strForTVMain")
        tvMain.text = decimalHelper(calculator.strForTVMain.toDouble())
    }


    fun onClear(view: View) {
        calculator.secondNumber = 0.0
        tvMain.text = "0"
        calculator.isDPhere = false
        calculator.strForTVMain = ""
        buttonAC.text = getString(R.string.ac)
        calculator.isNumberEmpty = true
        calculator.isPercentage = false
        Log.d("onClear", "isFullClear: $calculator.isFullClear")
        Log.d("steelwsky", "***********SN IS CLEARED***********")
        if (calculator.isFirstNumber || calculator.isFullClear) {
            calculator.isFirstNumber = true
            calculator.firstNumber = 0.0
            calculator.isLastOfAllNumeric = false
            calculator.isLastOperator = false
            calculator.newOpr = ""
            calculator.tryError = true
            calculator.isAfterEqual = false
            tvMain.setTextSize(TypedValue.COMPLEX_UNIT_SP, 60f)
            Log.d("steelwsky", "**********************CLEARED**********************")
        }
        calculator.isFullClear = true
    }


    fun onEqual(view: View) {
        Log.d("STM", "onEqual INIT")
        if (calculator.isFullClear && calculator.isNumberEmpty) {
            calculator.strForTVMain = calculator.firstNumber.toString()
            Log.d("STM", "onEqual, isFullClear = true, strTVMain: $calculator.strForTVMain")
        }
        if (!calculator.isFirstNumber && !calculator.isPercentage) {
            calculator.secondNumber = calculator.strForTVMain.toDouble()
            Log.d("STM", "onEqual !isFirstNumber")
        }
        if (calculator.strForTVMain.equals("")) {
            onClear(view)
            Log.d("STM", "strTVMain = null, onEqual EXIT")
            return
        }

        if (!calculator.tryError) {
            Log.d(
                "STM",
                "BEFORE MATHFUN newOpr: $calculator.newOpr  firstNumber: $calculator.firstNumber  secondNumber: $calculator.secondNumber"
            )
            tvMain.text = math(calculator.newOpr, calculator.firstNumber, calculator.secondNumber)
            calculator.newOpr = ""
            calculator.isLastOfAllNumeric = false
            calculator.isFirstNumber = true
            calculator.isAfterEqual = true
            calculator.isDPhere = false
            calculator.isPercentage = false
            Log.d("STM", "onEqual EXIT")
            calculator.tryError = true
            calculator.isFullClear = false
            return
        } else {
            calculator.tryError = false
            calculator.firstNumber = calculator.strForTVMain.toDouble()
            calculator.isFirstNumber = false
            calculator.isLastOperator = false
            Log.d("STM", "tryError is now FALSE")
            onEqual(view)
            return
        }
    }

    private fun math(operation: String, first: Double, second: Double): String {
        Log.d("steelwsky", "mathINIT, newOpr is: $calculator.newOpr")
        when (operation) {
            "+" -> {
                calculator.firstNumber = (first + second)
                Log.d("steelwsky", "WeAreInside  $first + $second = $calculator.firstNumber")
            }
            "-" -> {
                calculator.firstNumber = (first - second)
                Log.d("steelwsky", "WeAreInside  $first - $second = $calculator.firstNumber")
            }
            "*" -> {
                calculator.firstNumber = (first * second)
                Log.d("steelwsky", "WeAreInside  $first * $second = $calculator.firstNumber")
            }
            "/" -> {
                if (second != 0.0) {
                    calculator.firstNumber = (first / second)
                    Log.d("steelwsky", "WeAreInside  $first / $second = $calculator.firstNumber")
                } else
                    return MATHERROR
            }
        }
        calculator.isFirstNumber = false
        calculator.strForTVMain = calculator.firstNumber.toString()
        Log.d("steelwsky", "mathEND   $calculator.firstNumber")
        val forDecimalHelper = decimalHelper(calculator.firstNumber)
        Log.d("STM", "forDecimalHelper:$forDecimalHelper, ${forDecimalHelper.length}")
        return forDecimalHelper
    }

    fun onOperator(view: View) {
        calculator.isDPhere = false
        calculator.isLastOperator = true
        if (!calculator.isFirstNumber) {
            onEqual(view)
        }
        when (view) {
            fabPlus -> calculator.newOpr = "+"
            fabMinus -> calculator.newOpr = "-"
            fabMulti -> calculator.newOpr = "*"
            fabDivide -> calculator.newOpr = "/"
        }
        calculator.isAfterEqual = false
        Log.d("steelwsky", "HERE IS MY OPERATOR: $calculator.newOpr")
    }

    private fun saveNumber() {
        Log.d(" BEFORE SAVENUMBER", "strMain: $calculator.strForTVMain and FN:$calculator.firstNumber, SN:$calculator.secondNumber")
        if (calculator.isFirstNumber) {
            if (calculator.strForTVMain.equals("")) calculator.strForTVMain = "0"
            calculator.firstNumber = calculator.strForTVMain.toDouble()
            Log.d("SAVENUMBER", "FN: $calculator.firstNumber")
            calculator.strForTVMain = ""
            calculator.isFirstNumber = false
        }
    }


    @SuppressLint("ServiceCast")
    fun saveNumberToBuffer() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("number", tvMain.text)
        clipboard.primaryClip = clip

        Log.d("Clipboard", "we saved str: $calculator.strForTVMain")

    }

}



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
//---DONE posmotret kak eto sdelano v drugih proektah (gestureDetector onDoubleTap ---DONE

