package kz.fime.samal.data.models

import kz.fime.samal.utils.extensions.Item

data class ProfileResponse(
    val data: Profile
) {

    data class Profile(
        val name: String,
        val phone: String,
        val email: String?,
        val photo: String?,
        val language: String,
        val created_at: String,
        val points: String,
        val sex: ProfileSex,
        val address: Item?,
        val address_id: ProfileAddress
    )

    data class ProfileSex(
        val id: String,
        val name: String,
        val slug: String
    )

    data class ProfileAddress(
        val city: City,
        val name: String,
        val slug: String,
        val house_number: String,
        val apartment: String
    )

}