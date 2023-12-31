package uex.aseegps.ga03.tuonce.utils



class CredentialCheck() {

    var fail: Boolean = false
    var msg: String = ""
    var error: CredentialError = CredentialError.PasswordError

    companion object{

        private val TAG = CredentialCheck::class.java.canonicalName
        private val MINCHARS = 4

        private val checks = arrayOf(
            CredentialCheck().apply {
                fail = false
                msg = "Iniciando sesión..."
                error = CredentialError.Success
            },
            CredentialCheck().apply {
                fail = true
                msg = "Nombre de usuario incorrecto"
                error = CredentialError.UsernameError
            },
            CredentialCheck().apply {
                fail = true
                msg = "Contraseña incorrecta"
                error = CredentialError.PasswordError
            },
            CredentialCheck().apply {
                fail = true
                msg = "Las contraseñas no coinciden"
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
            return if (pw1 != pw2) checks[2]
            else checks[0]
        }
    }

    enum class CredentialError {
        PasswordError, UsernameError, Success
    }
}