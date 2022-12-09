package kz.fime.samal.data.models

data class CardsResponse(
    val data: List<UserCards>
) {
    class UserCards(
        val card_id: String,
        val card_hash: String
    )
}