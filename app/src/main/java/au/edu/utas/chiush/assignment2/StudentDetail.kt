package au.edu.utas.chiush.assignment2

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import au.edu.utas.chiush.assignment2.databinding.ActivityStudentDetailsBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class StudentDetail: AppCompatActivity() {
        private lateinit var ui: ActivityStudentDetailsBinding
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                ui = ActivityStudentDetailsBinding.inflate(layoutInflater)
                setContentView(ui.root)

                val spinnerWeek: Spinner = findViewById(R.id.spnWeek)
                val spinnerMark: Spinner = findViewById(R.id.spnMark)
                // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter.createFromResource(
                        this,
                        R.array.weeks_array,
                        android.R.layout.simple_spinner_item
                ).also { adapter ->
                        // Specify the layout to use when the list of choices appears
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        // Apply the adapter to the spinner
                        spinnerWeek.adapter = adapter

                }
                ArrayAdapter.createFromResource(
                        this,
                        R.array.Marks_array,
                        android.R.layout.simple_spinner_item
                ).also { adapter ->
                        // Specify the layout to use when the list of choices appears
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        // Apply the adapter to the spinner
                        spinnerMark.adapter = adapter
                }
                

                val db = Firebase.firestore
                var studentsCollection = db.collection("students")
                var weeksCollection = db.collection("weeks")


                val studentID = intent.getIntExtra(STUDENT_INDEX, -1)
//                val weekID = intent.getIntExtra(WEEK_INDEX, -1)
                var studentObject = items[studentID]
//                var weekObject = weekitems[weekID]


                //Show student name from database
                ui.txteditStudentName.setText(studentObject.studentName)
                ui.txteditStudentID.setText(studentObject.studentID)

                // Update button
                ui.btnSave.setOnClickListener {
                        //Storing the student in the studentCollection(Firebase)
                        studentObject.studentName = ui.txteditStudentName.text.toString()
                        studentObject.studentID = ui.txteditStudentID.text.toString()
                        val week = ui.spnWeek.selectedItem.toString()
                        val mark = ui.spnMark.selectedItem.toString()
                        //Actually using the Week.kt (Class) <--why ??
                        var newWeekGrade = Week(
                                Mark = mark
                        )
                        // store in grades (Week)
                        studentObject.grades.set(week , newWeekGrade)

                        val db = Firebase.firestore
                        var studentsCollection = db.collection("students")
                        studentsCollection.document(studentObject.id!!)
                                .set(studentObject)
                                .addOnSuccessListener {
                                        Log.d(FIREBASE_TAG, "Successfully updated student ${studentObject?.id}")
                                        Toast.makeText(applicationContext,
                                                "${studentObject?.studentID} has been updated !",
                                                Toast.LENGTH_SHORT).show()
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