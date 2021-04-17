package au.edu.utas.chiush.assignment2

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import au.edu.utas.chiush.assignment2.databinding.ActivityMainBinding
import au.edu.utas.chiush.assignment2.databinding.AddStudentActivityBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddStudent: AppCompatActivity() {
    private lateinit var ui: AddStudentActivityBinding
    private lateinit var ui2: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = AddStudentActivityBinding.inflate(layoutInflater)
        setContentView(ui.root)

        ui2 = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ui.root)

        val db = Firebase.firestore
       // val studentID = intent.getIntExtra(STUDENT_INDEX, -1)
       // var studentObject = items[studentID]

        //database /*need to add data from this end*/
        //Add student
        ui.btnAddStudent.setOnClickListener {
            //Add data
            if (ui.txtStudentName.text.isNotEmpty() && ui.txtStudentID.text.isNotEmpty()) {
                val txtStudentName = ui.txtStudentName.text
                val txtStudentID = ui.txtStudentID. text

                val lotr = Student(
                        studentName = txtStudentName?.toString(),
                        studentID = txtStudentID?.toString()
                )

                var studentsCollection = db.collection("students")
                studentsCollection
                        .add(lotr)

                        .addOnSuccessListener {
                            Log.d(FIREBASE_TAG, "Document created with id ${it.id}")
                            lotr.id = it.id
                            (ui2.myList.adapter as StudentAdapter).notifyDataSetChanged()
                            finish()
                        }
                        .addOnFailureListener {
                            Log.e(FIREBASE_TAG, "Error writing document", it)
                        }
            }else{
                //Alert box
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

    inner class StudentAdapter(private val students: MutableList<Student>): RecyclerView.Adapter<MainActivity.StudentHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainActivity.StudentHolder {
            TODO("Not yet implemented")
        }

        override fun onBindViewHolder(holder: MainActivity.StudentHolder, position: Int) {
            TODO("Not yet implemented")
        }

        override fun getItemCount(): Int {
            TODO("Not yet implemented")
        }


    }
    override fun onResume(){
        super.onResume()
        ui2.myList.adapter?.notifyDataSetChanged()
    }

}
