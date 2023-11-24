package uex.aseegps.ga03.tuonce.model.api

data class RemoteResource(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)