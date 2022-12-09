package kz.fime.samal.data.models

data class CategoryModel(
    val data: List<Categories>
)

data class Categories(
    val category_slug: String,
    val name: String,
    val image: String,
    val sort: String
)