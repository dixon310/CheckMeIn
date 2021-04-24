package au.edu.utas.chiush.assignment2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import au.edu.utas.chiush.assignment2.databinding.ActivityMainBinding
import au.edu.utas.chiush.assignment2.databinding.StudentListBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

val items = mutableListOf<Student>()
const val FIREBASE_TAG = "FirebaseLogging"
const val STUDENT_INDEX = "Student_Index"

class MainActivity : AppCompatActivity() {

    private lateinit var ui : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ui.root)

        //ui of student numbers
        ui.lblStudentCount.text = "${items.size} student"
        ui.myList.adapter = StudentAdapter(students = items)

        //vertical list
        ui.myList.layoutManager = LinearLayoutManager(this)


        //database /*need to add data from this end*/
        val db = Firebase.firestore
        var studentsCollection = db.collection("students")

        //get all students
        ui.lblStudentCount.text = "Loading..."
        studentsCollection
            .get()
            .addOnSuccessListener { result ->
                items.clear()
                Log.d(FIREBASE_TAG,"----- all students -----")
                for (document in result)
                {
                    val student = document.toObject<Student>()
                    student.id = document.id
                    Log.d(FIREBASE_TAG, student.toString())

                    items.add(student)
                    (ui.myList.adapter as StudentAdapter).notifyDataSetChanged()
                }
            }

        //Add button function
        ui.btnToAddStudent.setOnClickListener {
            val i = Intent(this, AddStudent::class.java)
            startActivity(i)
        }
    }


    inner class StudentHolder(var ui: StudentListBinding): RecyclerView.ViewHolder(ui.root) {}

    inner class StudentAdapter(val students: MutableList<Student>): RecyclerView.Adapter<StudentHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentHolder {
                val ui = StudentListBinding.inflate(layoutInflater, parent, false)
                return StudentHolder(ui)
            }

            override fun onBindViewHolder(holder: StudentHolder, position: Int) {

                val student = students[position]   //get the data at the requested position
                holder.ui.txtStName.text = student.studentName
                holder.ui.txtStID.text = student.studentID

                // add clicked on the listing item
                holder.ui.root.setOnClickListener {
                    var i = Intent(holder.ui.root.context, StudentDetail::class.java)
                    i.putExtra(STUDENT_INDEX, position)
                    startActivity(i)
                    }

            }

            override fun getItemCount(): Int {
                ui.lblStudentCount.text = "${students.size} Student(s)"
                return students.size
            }

        }
//Refresh page****
    override fun onResume(){
        super.onResume()
        val db = Firebase.firestore
        var studentsCollection = db.collection("students")
        studentsCollection
                .get()
                .addOnSuccessListener { result ->
                    Log.d(FIREBASE_TAG,"----- all students -----")
                    items.clear()
                    for (document in result)
                    {
                        val student = document.toObject<Student>()
                        student.id = document.id
                        Log.d(FIREBASE_TAG, student.toString())


                        items.add(student)
                        (ui.myList.adapter as StudentAdapter).notifyDataSetChanged()
                    }
                }
    }
}