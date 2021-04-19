package au.edu.utas.chiush.assignment2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
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


                //ui of student numbers
                //ui.weekList.adapter = StudentAdapter(students = items)

                //vertical list
                ui.weekList.layoutManager = LinearLayoutManager(this)


                //database /*need to add data from this end*/
                val db = Firebase.firestore
                var studentsCollection = db.collection("students")
                studentsCollection
                        .get()
                        .addOnSuccessListener { result ->
                                Log.d(FIREBASE_TAG,"----- all Weeks -----")
                                for (document in result)
                                {
                                        val week = document.toObject<Student>()
                                        week.id = document.id
                                        Log.d(FIREBASE_TAG, week.toString())

                                        items.add(week)
                                        //(ui.weekList.adapter as WeekAdapter).notifyDataSetChanged()
                                }
                        }

        }

        inner class WeekHolder(var ui: WeekListBinding): RecyclerView.ViewHolder(ui.root) {}

        inner class WeekAdapter(private val weeks: MutableList<Week>): RecyclerView.Adapter<WeekHolder>() {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekHolder {
                        TODO("Not yet implemented")
                }

                override fun onBindViewHolder(holder: WeekHolder, position: Int) {
                        TODO("Not yet implemented")
                        val week = weeks[position]   //get the data at the requested position
                        holder.ui.lblWeek.text = week.weekNo

                        // add clicked on the listing item
                        holder.ui.root.setOnClickListener {
                                var i = Intent(holder.ui.root.context, StudentDetail::class.java)
                                i.putExtra(STUDENT_INDEX, position)
                                startActivity(i)
                        }
                }

                override fun getItemCount(): Int {
                        TODO("Not yet implemented")
                }
        }
}


