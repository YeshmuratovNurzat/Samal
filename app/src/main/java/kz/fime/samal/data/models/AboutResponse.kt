package kz.fime.samal.data.models

data class AboutResponse(
    val address: String,
    val coords: Any,
    val description: String,
    val email: String,
    val location: List<Double>,
    val name: String,
    val phone1: String,
    val phone2: String,
    val url_map: String
)