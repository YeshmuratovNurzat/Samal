package kz.fime.samal.data.models

class FaqItem(
    val question: String,
    val answer: String,
    var isExpanded: Boolean = false
)