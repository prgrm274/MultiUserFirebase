package com.programmer270487.loginsignuptypicode.data.repo

import android.content.Context
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.programmer270487.loginsignuptypicode.ui.login.LoginActivity
import com.programmer270487.loginsignuptypicode.utils.State

class AuthRepoImpl(
    val firebaseAuth: FirebaseAuth,
    val firestore: FirebaseFirestore,
): AuthRepo {
    override fun registerUser(
        uname: String,
        email: String,
        password: String,
        role: Boolean,
        result: (State<String>) -> Unit
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    val documentReference = firestore.collection("Users").document(user!!.uid)
                    val userInfo = HashMap<String, Any>()
                    userInfo["username"] = uname
                    userInfo["email"] = email
                    userInfo["adminRole"] = role
                    documentReference.set(userInfo)

                    result.invoke(State.Success("Register successful"))
                } else {
                    try {
                        throw it.exception ?: java.lang.Exception("Invalid authentication")
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        result.invoke(State.Failure("Authentication failed, Password should be at least 6 characters"))
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        result.invoke(State.Failure("Authentication failed, Invalid email entered"))
                    } catch (e: FirebaseAuthUserCollisionException) {
                        result.invoke(State.Failure("Authentication failed, Email already registered."))
                    } catch (e: Exception) {
                        result.invoke(State.Failure(e.message))
                    }
                }
            }
            .addOnFailureListener {
                result.invoke(State.Failure("Failure registration: ${it.localizedMessage}"))
            }
    }

    override fun loginUser(
        email: String,
        password: String,
        result: (State<String>) -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(State.Success("Login successful."))
                }
            }
            .addOnFailureListener {
                result.invoke(State.Failure("Authentication failed, check email."))
            }
    }

    override fun logout(context: Context, result: () -> Unit) {
        firebaseAuth.signOut()
        result() // Call the lambda function after logging out
        // Can start any activity here
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }

    override fun handleOnStart(context: Context, result: (Boolean, Boolean) -> Unit) {
        if (firebaseAuth.currentUser != null) {
            val documentReference = firestore.collection("Users")
                .document(firebaseAuth.currentUser!!.uid)
            documentReference.get()
                .addOnSuccessListener {
                    val isAdmin = it.getBoolean("adminRole") == true
                    result(true, isAdmin)
                }
                .addOnFailureListener {
                    firebaseAuth.signOut()
                    result(false, false)
                }
        } else {
            result(false, false)
        }
    }
}