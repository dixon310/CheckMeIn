package au.edu.utas.chiush.assignment2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import au.edu.utas.chiush.assignment2.databinding.ActivityStudentDetailsBinding
import com.google.firebase.firestore.core.View
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

const val REQUEST_IMAGE_CAPTURE = 1

class StudentDetail: AppCompatActivity() {
        private lateinit var ui: ActivityStudentDetailsBinding

        @RequiresApi(Build.VERSION_CODES.M)
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

            //Cam
            ui.btnCamera.setOnClickListener {
                requestToTakeAPicture()
            }
        }

    //step 4
    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestToTakeAPicture()
    {
        requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_IMAGE_CAPTURE
        )
    }

    //step 5
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode)
        {
            REQUEST_IMAGE_CAPTURE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted.
                    takeAPicture()
                } else {
                    Toast.makeText(this, "Cannot access camera, permission denied", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    //step 6
    private fun takeAPicture() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        //try {
        val photoFile: File = createImageFile()!!
        val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "au.edu.utas.chiush.assignment2",
                photoFile
        )
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        //} catch (e: Exception) {}

    }

    //step 6 part 2
    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    //step 7
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            setPic(ui.myImageView)
        }
    }

    //step 7 pt2
    private fun setPic(imageView: ImageView) {
        // Get the dimensions of the View
        val targetW: Int = imageView.measuredWidth
        val targetH: Int = imageView.measuredHeight

        val bmOptions = BitmapFactory.Options().apply {
            // Get the dimensions of the bitmap
            inJustDecodeBounds = true

            BitmapFactory.decodeFile(currentPhotoPath, this)

            val photoW: Int = outWidth
            val photoH: Int = outHeight

            // Determine how much to scale down the image
            val scaleFactor: Int = Math.max(1, Math.min(photoW / targetW, photoH / targetH))

            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
        }
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions)?.also { bitmap ->
            imageView.setImageBitmap(bitmap)
        }
    }


}


