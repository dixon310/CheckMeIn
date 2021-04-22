package au.edu.utas.chiush.assignment2

import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import au.edu.utas.chiush.assignment2.databinding.ActivityStudentDetailsBinding
import com.google.firebase.firestore.core.View
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
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
//                ui.spnWeek.setOnItemSelectedListener(object : OnItemSelectedListener {
//                        override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
//                                TODO("Not yet implemented")
//                                // Object item = parentView.getItemAtPosition(position);
//
//                                // Depend on first spinner value set adapter to 2nd spinner
//                                if (position === 1 || position === 3 ||position === 5) {
//                                        ui.spnMark.setAdapter(adapter2)
//                                } else {
//                                        spinner2.setAdapter(adapter3)
//                                }
//                        }
//
//                        override fun onNothingSelected(arg0: AdapterView<*>?) { // do nothing
//                        }
//                })

                val db = Firebase.firestore
                var studentsCollection = db.collection("students")
                var weeksCollection = db.collection("weeks")


                val studentID = intent.getIntExtra(STUDENT_INDEX, -1)
                var studentObject = items[studentID]


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
                        studentObject.grades[week] = newWeekGrade

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
                                        Toast.makeText(applicationContext, "${studentObject?.studentID} has been Remove!", Toast.LENGTH_SHORT).show()
                                        finish()
                                }

                                .addOnFailureListener { e ->
                                        Log.w(FIREBASE_TAG, "Error deleting document", e)
                                }

                }

                studentsCollection
                        .document(studentObject.id!!)
                        .get()
                        .addOnSuccessListener { result ->
                                items.clear()
                                var student = result.toObject<Student>()
                                student!!.id = result.id
                                if(student.grades["Week 1"]?.Mark?.isNotBlank() == true)
                                { ui.lblDisplayMark.text = student.grades["Week 1"]!!.Mark.toString()}

                                if(student.grades["Week 2"]?.Mark?.isNotBlank() == true)
                                { ui.lblDisplayMark2.text = student.grades["Week 2"]!!.Mark.toString()}

                                if(student.grades["Week 3"]?.Mark?.isNotBlank() == true)
                                { ui.lblDisplayMark3.text = student.grades["Week 3"]!!.Mark.toString()}

                                if(student.grades["Week 4"]?.Mark?.isNotBlank() == true)
                                { ui.lblDisplayMark4.text = student.grades["Week 4"]!!.Mark.toString()}

                                if(student.grades["Week 5"]?.Mark?.isNotBlank() == true)
                                { ui.lblDisplayMark5.text = student.grades["Week 5"]!!.Mark.toString()}

                                if(student.grades["Week 6"]?.Mark?.isNotBlank() == true)
                                { ui.lblDisplayMark6.text = student.grades["Week 6"]!!.Mark.toString()}

                                if(student.grades["Week 7"]?.Mark?.isNotBlank() == true)
                                { ui.lblDisplayMark7.text = student.grades["Week 7"]!!.Mark.toString()}

                                if(student.grades["Week 8"]?.Mark?.isNotBlank() == true)
                                { ui.lblDisplayMark8.text = student.grades["Week 8"]!!.Mark.toString()}

                                if(student.grades["Week 9"]?.Mark?.isNotBlank() == true)
                                { ui.lblDisplayMark9.text = student.grades["Week 9"]!!.Mark.toString()}

                                if(student.grades["Week 10"]?.Mark?.isNotBlank() == true)
                                { ui.lblDisplayMark10.text = student.grades["Week 10"]!!.Mark.toString()}

                                if(student.grades["Week 11"]?.Mark?.isNotBlank() == true)
                                { ui.lblDisplayMark11.text = student.grades["Week 11"]!!.Mark.toString()}

                                if(student.grades["Week 12"]?.Mark?.isNotBlank() == true)
                                { ui.lblDisplayMark12.text = student.grades["Week 12"]!!.Mark.toString()}

                                if(student.grades["Week 13"]?.Mark?.isNotBlank() == true)
                                { ui.lblDisplayMark13.text = student.grades["Week 13"]!!.Mark.toString()}

                        }





        }

}