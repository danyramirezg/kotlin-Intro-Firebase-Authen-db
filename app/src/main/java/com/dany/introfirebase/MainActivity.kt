package com.dany.introfirebase

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import java.text.FieldPosition


class MainActivity : AppCompatActivity() {

    // Authentication variables:
    private var myAuth: FirebaseAuth? = null
    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // This is the 4 step setting up the Firebase database:
        var firebaseDatabase = FirebaseDatabase.getInstance()
        var databaseRef = firebaseDatabase.getReference("Data class employee")
            .push() // push to add more info to the db


        myAuth = FirebaseAuth.getInstance()

        createActId.setOnClickListener {

            // Create a new user
            var email = emailId.text.toString().trim()
            var passw = passwordId.text.toString().trim()

            myAuth?.createUserWithEmailAndPassword(email, passw)
                ?.addOnCompleteListener(this) { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        var user: FirebaseUser? = myAuth?.currentUser

                        Log.d("===>User", "The user ${user?.email} was created successfully!")
                        Toast.makeText(this, "The user ${user?.email} was created successfully!", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        // Not successful
                        Log.d("===>Unsuccessful", "${task.exception}")
                        Toast.makeText(this, "${task.exception}", Toast.LENGTH_LONG)
                            .show()
                    }
                }
        }


        /*
        The code below is to debugging, it could be the first thing I can do (keep it in mind
        the onStart fun that is below as well)
         */

        // Sign existing user in
//        var user = "dany@me.com"
//        var password = "password"
//
//        myAuth?.signInWithEmailAndPassword(user, password)
//             ?.addOnCompleteListener { task: Task<AuthResult> ->
//                if (task.isSuccessful) {
//                    // Sign in was successful
//                    Log.d("===>Successful", "The signed in $user is successful")
//                    Toast.makeText(this, "The signed in $user is successful", Toast.LENGTH_LONG)
//                        .show()
//                } else {
//                    // Not successful
//                    Log.d("===>Unsuccessful", "The signed in is unsuccessful")
//                    Toast.makeText(this, "The signed in is unsuccessful", Toast.LENGTH_LONG).show()
//                }
//
//            }


        var employee = Employee("Dany Ramirez", "Kotlin developer", "Medellin", 28)

        //databaseRef.setValue(employee)


        // Read from the database
        databaseRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // Casting value as HashMap
//                val value = dataSnapshot.value as HashMap<String, Any>
//                Log.d("===>Value is: ", value.get("name").toString())
//
//                for (name in value.entries){
//                    Log.d("Names: ", name.toString())
//                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Failed to read value.", error.toException())
            }
        })
    }

    override fun onStart() {
        super.onStart()
        currentUser = myAuth?.currentUser

        // Call a function to update the userInterface with current user

        if (currentUser != null) {
            Toast.makeText(this, "On start fun: User is Logged in", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "On start fun: User is Logged out", Toast.LENGTH_LONG).show()
        }
    }

}

data class Employee(
    val name: String, var position: String, var homeAddress: String,
    var age: Int) {

}