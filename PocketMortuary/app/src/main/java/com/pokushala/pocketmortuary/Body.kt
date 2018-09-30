package com.pokushala.pocketmortuary

class Body{
    var body_id:Int?=null
    var body_name:String?=null
    var body_des:String?=null

    constructor(body_id:Int, body_name:String, body_des:String){
        this.body_id = body_id
        this.body_name = body_name
        this.body_des = body_des
    }
}