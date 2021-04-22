package au.edu.utas.chiush.assignment2

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import au.edu.utas.chiush.assignment2.databinding.ActivityMainBinding
import au.edu.utas.chiush.assignment2.databinding.AddStudentActivityBinding
import au.edu.utas.chiush.assignment2.databinding.StudentListBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class AddStudent: AppCompatActivity() {
    private lateinit var ui: AddStudentActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = AddStudentActivityBinding.inflate(layoutInflater)
        setContentView(ui.root)

        val db = Firebase.firestore
        val studentID = intent.getIntExtra(STUDENT_INDEX, -1)
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


                val studentsCollection = db.collection("students")
                studentsCollection
                        .add(lotr)
                        .addOnSuccessListener {
                            Log.d(FIREBASE_TAG, "Document created with id ${it.id}")
                            lotr.id = it.id
                            Toast.makeText(applicationContext, "Successfully create student ${lotr.studentID}", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Log.e(FIREBASE_TAG, "Error writing document", it)
                        }
                finish()
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
    inner class StudentHolder(var ui: StudentListBinding): RecyclerView.ViewHolder(ui.root) {}

    inner class StudentAdapter(private val students: MutableList<Student>): RecyclerView.Adapter<StudentHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddStudent.StudentHolder {
            TODO("Not yet implemented")
            val ui = StudentListBinding.inflate(layoutInflater, parent, false)
            return StudentHolder(ui)
        }
        override fun onBindViewHolder(holder: AddStudent.StudentHolder, position: Int) {
            TODO("Not yet implemented")

        }


        override fun getItemCount(): Int {
            TODO("Not yet implemented")
        }




    }

    override fun onResume(){
        super.onResume()
        val db = Firebase.firestore
        var studentsCollection = db.collection("students")

        //get all students
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


                    }
  //                  (ui.MainActivity.myList.adapter as MainActivity.StudentAdapter).notifyDataSetChanged()
                }

    }


}
