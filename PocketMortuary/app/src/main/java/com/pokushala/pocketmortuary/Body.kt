package com.pokushala.pocketmortuary

class Body{
    var body_id:Int?=null
    var body_surname:String?=null
    //change
    var body_name:String?=null
    var body_sex:String?=null
    var body_birth:String?=null
    var body_arrived:String?=null
    var body_death:String?=null

    constructor(body_id:Int, body_surname:String, body_name:String, body_sex:String,
                body_arrived:String, body_birth:String, body_death:String){
        this.body_id = body_id
        this.body_surname = body_surname
        this.body_name = body_name
        this.body_sex = body_sex
        this.body_birth = body_birth
        this.body_death = body_death
        this.body_arrived = body_arrived
    }
}