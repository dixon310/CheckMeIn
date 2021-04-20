package au.edu.utas.chiush.assignment2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import au.edu.utas.chiush.assignment2.databinding.ActivityStudentDetailsBinding
import au.edu.utas.chiush.assignment2.databinding.StudentListBinding
import au.edu.utas.chiush.assignment2.databinding.WeekListBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class StudentDetail: AppCompatActivity() {
        private lateinit var ui: ActivityStudentDetailsBinding
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                ui = ActivityStudentDetailsBinding.inflate(layoutInflater)
                setContentView(ui.root)

                val db = Firebase.firestore
                var studentsCollection = db.collection("students")

                //get all students
                studentsCollection
                        .get()
                        .addOnSuccessListener { result ->
                                Log.d(FIREBASE_TAG, "----- all students -----")
                                for (document in result) {
                                        val student = document.toObject<Student>()
                                        student.id = document.id
                                        Log.d(FIREBASE_TAG, student.toString())

                                        items.add(student)

                                }
                        }

                val studentID = intent.getIntExtra(STUDENT_INDEX, -1)
                var studentObject = items[studentID]

                ui.txteditStudentName.setText(studentObject.studentName)
                ui.txteditStudentID.setText(studentObject.studentID)

                ui.btnSave.setOnClickListener {
                        studentObject.studentName = ui.txteditStudentName.text.toString()
                        studentObject.studentID = ui.txteditStudentID.text.toString()

                        val db = Firebase.firestore
                        var studentsCollection = db.collection("students")
                        studentsCollection.document(studentObject.id!!)
                                .set(studentObject)
                                .addOnSuccessListener {
                                        Log.d(FIREBASE_TAG, "Successfully updated student ${studentObject?.id}")
                                        Toast.makeText(applicationContext,"${studentObject?.studentID} has been updated !", Toast.LENGTH_SHORT).show()
                                        finish()
                                }

                }

                ui.btnRemove.setOnClickListener {
                        val db = Firebase.firestore
                        var studentsCollection = db.collection("students")
                        studentsCollection.document(studentObject.id!!)
                                .delete()
                                .addOnSuccessListener {
                                        Log.d(FIREBASE_TAG, "DocumentSnapshot successfully deleted!")
                                        Toast.makeText(applicationContext,"${studentObject?.studentID} has been Remove!", Toast.LENGTH_SHORT).show()
                                        finish()
                                }

                                .addOnFailureListener { e ->
                                        Log.w(FIREBASE_TAG, "Error deleting document", e)
                                }

                }


        }

}