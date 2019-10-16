package com.example.test1


data class Calculator(
    var isLastOfAllNumeric: Boolean = false,
    var isLastOperator: Boolean = false,
    var isDPhere: Boolean = false,
    var isFirstNumber: Boolean = true,
    var tryError: Boolean = true,
    var isAfterEqual: Boolean = false,
    var firstNumber: Double = 0.0,
    var secondNumber: Double = 0.0,
    var isPercentage: Boolean = false,
    var newOpr: String = "",
    var isNumberEmpty: Boolean = true,
    var isFullClear: Boolean = false,
    var strForTVMain: String = ""
)


//data class Calculator (val isLastOfAllNumeric: Boolean = false, val isLastOperator: Boolean = false, val isDPhere: Boolean = false, val isFirstNumber: Boolean = true,
//                                 val tryError: Boolean = true, val isAfterEqual: Boolean = false, val firstNumber: Double = 0.0, val secondNumber: Double = 0.0, val isPercentage: Boolean = false,
//                                 val newOpr: String = "", val isNumberEmpty: Boolean = true, val isFullClear: Boolean = false, val strForTVMain: String = "") {


//class Calculator(
//    isLastOfAllNumeric: Boolean,
//    isLastOperator: Boolean,
//    isDPhere: Boolean,
//    isFirstNumber: Boolean,
//    tryError: Boolean,
//    isAfterEqual: Boolean,
//    firstNumber: Double,
//    secondNumber: Double,
//    isPercentage: Boolean,
//    newOpr: String,
//    isNumberEmpty: Boolean,
//    isFullClear: Boolean,
//    strForTVMain: String
//) {
//
//    var isLastOfAllNumeric = false
//    var isLastOperator = false
//    var isDPhere = false
//    var isFirstNumber = true
//    var tryError = true
//    var isAfterEqual = false
//    var firstNumber: Double = 0.0
//    var secondNumber: Double = 0.0
//    var isPercentage = false
//    var newOpr = ""
//    var isNumberEmpty = true
//    var isFullClear = false
//    var strForTVMain: String = ""
//
//    init {
//        this.isLastOfAllNumeric = isLastOfAllNumeric
//        this.isLastOperator = isLastOperator
//        this.isDPhere = isDPhere
//        this.isFirstNumber = isFirstNumber
//        this.tryError = tryError
//        this.isAfterEqual = isAfterEqual
//        this.firstNumber = firstNumber
//        this.secondNumber = secondNumber
//        this.isPercentage = isPercentage
//        this.newOpr = newOpr
//        this.isNumberEmpty = isNumberEmpty
//        this.isFullClear = isFullClear
//        this.strForTVMain = strForTVMain
//    }
//}
