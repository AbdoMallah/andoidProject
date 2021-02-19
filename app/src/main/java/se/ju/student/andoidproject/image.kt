package se.ju.student.andoidproject

import java.sql.Date

class image (
        val  id: Int,
        var title: String,
        var description: String,
        var location: String,
        var date: Date,

        ){
    override fun toString()= title



}