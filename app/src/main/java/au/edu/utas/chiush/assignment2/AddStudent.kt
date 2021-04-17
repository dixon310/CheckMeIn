package au.edu.utas.chiush.assignment2

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import au.edu.utas.chiush.assignment2.databinding.AddStudentActivityBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddStudent: AppCompatActivity() {
    private lateinit var ui: AddStudentActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = AddStudentActivityBinding.inflate(layoutInflater)
        setContentView(ui.root)

        val studentID = intent.getIntExtra(STUDENT_INDEX, -1)
        var studentObject = items[studentID]

        //database /*need to add data from this end*/


        //Add student
        ui.btnAddStudent.setOnClickListener {
            //Add data
            if (ui.txtStudentName.text.isNotEmpty() && ui.txtStudentID.text.isNotEmpty()) {
                var txtStudentName = ui.txtStudentName.text
                var txtStudentID = ui.txtStudentID. text

                val lotr = Student(
                    studentName = txtStudentName.toString(),
                    studentID = txtStudentID.toString()
                )
                val db = Firebase.firestore
                var studentsCollection = db.collection("students")
                studentsCollection.document()
                    .add(lotr)
                    .addOnSuccessListener {
                        Log.d(FIREBASE_TAG, "Document created with id ${it.id}")
                        lotr.id = it.id
                        finish()
                    }
                    .addOnFailureListener {
                        Log.e(FIREBASE_TAG, "Error writing document", it)
                    }
            }else{
                val builder = AlertDialog.Builder(this)
                with(builder){
                    setTitle("Invalid input")
                    setMessage("Please enter the Detail")
                    setPositiveButton("OK", null)
                    show()
                }
            }

        }
    }


}