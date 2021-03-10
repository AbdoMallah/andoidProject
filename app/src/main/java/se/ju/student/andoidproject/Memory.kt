package se.ju.student.andoidproject

import android.net.Uri


data class Memory(
    var title :String,
    var description:String,
    var date:Int,
    val location : String,
    var img: Uri,
     var id: String
) {
    override fun toString() = title
}
