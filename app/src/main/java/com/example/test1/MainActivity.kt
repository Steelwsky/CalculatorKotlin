package com.example.test1

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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


    var isNumberEmpty = true
    var isFullClear = false
//    var isFirstPlusMinus = false



    var strForTVMain: String = ""
    var stSecondNumber: String = ""

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

    //      ***************************************************************************************
    //TODO максимальное количество знаков в числах! 22.1E11 ... BigDecimal? Внедрить поддержку больших вычислений, типа 885 312 * 943 042 = и показывать с e11.
    // Не столь обязательная штука. переделывать всё в BigDecimal??? Посмотреть чужие проекты

    //TODO анимацию нажатий на цифры. Для операторов сделать состяние pressed.
    //TODO Сделать возможным нажатие на +- в начальном состоянии. мб сделать как в onDecimal при начальном положении


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


//        val gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
//            override fun onDoubleTap(e: MotionEvent?): Boolean {
//                Log.d("myApp", "double tap")
//                return true
//            }
//        })
//        view.setOnTouchListener { _, event -> gestureDetector.onTouchEvent(event) }

    }


    @SuppressLint("ServiceCast")
    fun saveNumberToBuffer(view: View) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("number", tvMain.text)
        clipboard.primaryClip = clip

        Log.d("Clipboard", "we saved str: $strForTVMain")

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
        if (checkForLastOperator()) {
            Log.d("ONNUMBER", "checkForLastOperator")
            tvMain.text = ""
        }
        isLastOperator = false
        isFullClear = false
        forTvMain(view)
        isLastOfAllNumeric = true
    }


    private fun forTvMain(view: View) {
        isNumberEmpty = false
        val button = view as Button
        Log.d("HEEEEELP", "$strForTVMain")
        if (strForTVMain.length < 9) {
            strForTVMain += button.text.toString()
            Log.d("HEEEEELP", "$strForTVMain")
            val beautyStr = strForTVMain.toDouble()
            Log.d("STM", "strForTVMain.toDouble: $beautyStr")
            tvMain.text = decimalHelper(beautyStr, DECIMAL_FORMAT)
            Log.d("STM", "strForTVMain: $strForTVMain")
        } else return
    }


    private fun checkForLastOperator() = when {
        isLastOperator -> {
            Log.d("stm", "isNotEmpty is done")
//            isNumberEmpty = true
            tryError = false
            saveNumber()
            Log.d("stm", "FN: $firstNumber or SN:$secondNumber")
            true
        }
        else -> false
    }


    // TODO проверить смысл isLastOfAllNumeric. мб в этой функции првоерку на нее можно убрать...
    fun onDecimal(view: View) {
        Log.d(
            "onDecimal",
            "isNumberEmpty :$isNumberEmpty and strFor: $strForTVMain, isLastOfAllNumeric: $isLastOfAllNumeric, isDPhere: $isDPhere"
        )
//        if (isNumberEmpty && strForTVMain.isEmpty()) {
        if (isNumberEmpty) {
            tvMain.append((view as Button).text)
            strForTVMain = "0."
            isDPhere = true
            isNumberEmpty = false
        }
//        if(isLastOperator) {
//            tvMain.text ="0,"
//            strForTVMain ="0."
//            isDPhere = true
//            isNumberEmpty = false
//        }
        if (checkForLastOperator()) {
            Log.d("onDecimal", "checkForLastOperator, isNumberEmpty: $isNumberEmpty")
            tvMain.text = "0,"
            strForTVMain = "0."
            isLastOperator = false
            // postavil isFirstNumber = false i teper posle onCleat eta tema ne rabotaet
            isFirstNumber = false
            return
        }
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

    //не работает второй IF, если нажать сразу на АС. Тут неправильный алгоритм. Если посчитать и нажать на "=" и потом на "С", то сотрется лишь SN.
// Багов не нашел, но это все равно не круто. Баги есть. + если нажать на С после "=", то скинет только до SN. Видимо надо вернуть старую реализацию. ---DONE
    fun onClear(view: View) {
        secondNumber = 0.0
        tvMain.text = "0"
        isDPhere = false
        strForTVMain = ""
        buttonAC.text = "AC"
        isNumberEmpty = true
        isPercentage = false
        Log.d("onClear", "isFullClear: $isFullClear")
//        if (!isFullClear) isFullClear = true
        Log.d("steelwsky", "***********SN IS CLEARED***********")
        if (isFirstNumber || isFullClear) {
//            isFullClear = false
            buttonAC.text = "AC"
            isFirstNumber = true
            firstNumber = 0.0
            isLastOfAllNumeric = false
            isLastOperator = false
            newOpr = ""
            tryError = true
            isAfterEqual = false
//            tvMain.setTextSize(TypedValue.COMPLEX_UNIT_SP, 60f)
            Log.d("steelwsky", "**********************CLEARED**********************")
        }
        isFullClear = true
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
//            isNumberEmpty = true
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
        if (firstNumber > 999999999) {
            return "Too big"
        } else {
            isFirstNumber = false
            strForTVMain = firstNumber.toString()
//            if (strForTVMain.length > 9) {
//                tvMain.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f) // ne pomogaet 789654321 / 7.3 = vse sdvigaetsya, ebalrot
////                round(firstNumber.)
//            }
            Log.d("steelwsky", "mathEND   $firstNumber")
            val forDecimalHelper = decimalHelper(firstNumber, DECIMAL_FORMAT)
            Log.d("STM", "forDecimalHelper: $forDecimalHelper")
            return forDecimalHelper
        }
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


//fun onClear1(view: View) {
//        secondNumber = 0.0
//        tvMain.text = "0"
//        isDPhere = false
//        strForTVMain = ""
//        buttonAC.text = "AC"
//        isNumberEmpty = true
//        isFirstPlusMinus = false
//        isPercentage = false
//        Log.d("steelwsky", "***********SN IS CLEARED***********")
//        if (isAfterEqual && isFullClear) {
//            isFullClear = false
//            buttonAC.text = "AC"
//            isFirstNumber = true
//            firstNumber = 0.0
//            isLastOfAllNumeric = false
//            isLastOperator = false
//            newOpr = ""
//            tryError = true
//            isAfterEqual = false
////            tvMain.setTextSize(TypedValue.COMPLEX_UNIT_SP, 60f)
//            Log.d("steelwsky", "**********************CLEARED**********************")
//        }
//        isFullClear = true
//    }