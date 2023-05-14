package com.example.jobquest.activities.message

class MassageModel {
    var massage: String? = null
    var senderId: String? = null

    constructor(){}

    constructor(massage: String?, senderId: String?){
        this.massage = massage
        this.senderId = senderId
    }
}