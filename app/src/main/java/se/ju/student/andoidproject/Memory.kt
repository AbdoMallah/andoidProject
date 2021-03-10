package se.ju.student.andoidproject

import android.net.Uri


data class Memory(
    var title :String,
    var description:String,
    var date:String,
    val location : String,
    var img: Uri,
     var id: String
) {
    lateinit var Img: Any

    override fun toString() = title
}
