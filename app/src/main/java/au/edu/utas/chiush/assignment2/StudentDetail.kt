package au.edu.utas.chiush.assignment2

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import au.edu.utas.chiush.assignment2.databinding.ActivityStudentDetailsBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class StudentDetail: AppCompatActivity() {
        private lateinit var ui: ActivityStudentDetailsBinding
        override fun onCreate(savedInstanceState: Bundle?){
                super.onCreate(savedInstanceState)
                ui = ActivityStudentDetailsBinding.inflate(layoutInflater)
                setContentView(ui.root)

                val studentID = intent.getIntExtra(STUDENT_INDEX, -1)
                var studentObject = items[studentID]

                ui.txteditStudentName.setText(studentObject.studentName)
                ui.txteditStudentID.setText(studentObject.studentID)

                ui.btnSave.setOnClickListener{
                        studentObject.studentName = ui.txteditStudentName.text.toString()
                        studentObject.studentID = ui.txteditStudentID.text.toString()

                        val db = Firebase.firestore
                        var studentsCollection = db.collection("students")
                        studentsCollection.document(studentObject.id!!)
                                .set(studentObject)
                                .addOnSuccessListener {
                                        Log.d(FIREBASE_TAG, "Successfully updated student ${studentObject?.id}")
                                        finish()
                                }

                }

                ui.btnRemove.setOnClickListener{
                        val db = Firebase.firestore
                        var studentsCollection = db.collection("students")
                        studentsCollection.document(studentObject.id!!)
                                .delete()
                                .addOnSuccessListener {
                                        Log.d(FIREBASE_TAG, "DocumentSnapshot successfully deleted!")
                                        finish()
                                }

                                .addOnFailureListener { e ->
                                        Log.w(FIREBASE_TAG, "Error deleting document", e)
                                }

                }


        }
}


