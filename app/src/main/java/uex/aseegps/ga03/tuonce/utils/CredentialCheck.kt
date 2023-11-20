package uex.aseegps.ga03.tuonce.utils

import android.util.Log
import android.widget.Toast
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


fun hashPassword(password: String): String {
    try {
        val md = MessageDigest.getInstance("SHA-256")

        md.update(password.toByteArray())

        return String.format("%064x", BigInteger(1, md.digest()))

    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
        return ""
    }
}
class CredentialCheck private constructor() {

    var fail: Boolean = false
    var msg: String = ""
    var error: CredentialError = CredentialError.PasswordError

    companion object{

        private val TAG = CredentialCheck::class.java.canonicalName
        private val MINCHARS = 4

        private val checks = arrayOf(
            CredentialCheck().apply {
                fail = false
                msg = "Your credentials are OK"
                error = CredentialError.Success
            },
            CredentialCheck().apply {
                fail = true
                msg = "Invalid username"
                error = CredentialError.UsernameError
            },
            CredentialCheck().apply {
                fail = true
                msg = "Invalid password"
                error = CredentialError.PasswordError
            },
            CredentialCheck().apply {
                fail = true
                msg = "Passwords do not match"
                error = CredentialError.PasswordError
            }

        )

        fun login(username: String, password: String): CredentialCheck {
            return if (username.isBlank() || username.length < MINCHARS) checks[1]
            else if (password.isBlank() || password.length < MINCHARS) checks[2]
            else checks[0]
        }

        fun join(username: String, password: String, repassword: String): CredentialCheck {
            return if (username.isBlank() || username.length < MINCHARS) checks[1]
            else if (password.isBlank() || password.length < MINCHARS) checks[2]
            else if (password!=repassword) checks[3]
            else checks[0]
        }

        fun passwordOk(pw1: String, pw2: String): CredentialCheck {
            return if (hashPassword(pw1) != pw2) checks[2]
            else checks[0]
        }
    }

    enum class CredentialError {
        PasswordError, UsernameError, Success
    }
}