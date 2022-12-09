package kz.fime.samal.data.models

data class AddCardResponse(
    val data: AddCard
) {
    class AddCard(
        val url: String
    )
}