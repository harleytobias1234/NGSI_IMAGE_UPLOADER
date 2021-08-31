package com.example.ngsi_image_uploader.data

class Registration(text: String, text1: String, text2: String, text3: String, text4: String) {
    var regTransactionNumber: String? = text
    var regName: String? = text1
    var regEmail: String? = text2
    var regBase64Img: String? = text3
    var regContactNo: String? = text4

    fun MyRegistration(
        regTransactionNumber: String?,
        regName: String?,
        regEmail: String?,
        regBase64Img: String?,
        regContactNo: String?
    ) {
        this.regTransactionNumber = regTransactionNumber
        this.regName = regName
        this.regEmail = regEmail
        this.regBase64Img = regBase64Img
        this.regContactNo = regContactNo
    }
}