package uex.aseegps.ga03.tuonce.model

import java.io.Serializable

data class User(
    val name: String = "",
    val password: String = ""
) : Serializable
