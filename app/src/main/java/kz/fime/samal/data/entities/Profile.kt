package kz.fime.samal.data.entities

data class Profile(
        val name: String,
        val phone: String,
        val email: String?,
        val photo: String?,
        val language: String?,
        val created_at: String?,
        val points: String?,
        val address: Address?
)

data class Address(
        val address_id: String,
        val city: City,
        val name: String,
        val slug: String,
        val street: String?,
        val house_number: String?,
        val apartment: String?
)

data class City(
        val city_id: Int,
        val name: String
)