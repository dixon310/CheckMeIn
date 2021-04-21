package au.edu.utas.chiush.assignment2

class Student (
        var id: String? =  null,

        var studentName: String? = null,
        var studentID: String? = null,
        var grades: MutableMap<String,String>? = mutableMapOf(),
        var imageURL: String? = null,
        var checkBol: Boolean? = null
)