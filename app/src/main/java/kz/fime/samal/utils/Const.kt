package kz.fime.samal.utils

import kz.fime.samal.utils.decoration.GridSpacingItemDecoration
import kz.fime.samal.utils.extensions.dpToPx

//const val BASE_URL = "https://api.samal.market"
const val BASE_URL = "https://api-dev.samalsauda.kz"
const val API = "$BASE_URL/api/v1/"

const val SHARED_PREFERENCES_NAME = "Samal"

val gridItemDecorator = GridSpacingItemDecoration(2, 16.dpToPx(), true)