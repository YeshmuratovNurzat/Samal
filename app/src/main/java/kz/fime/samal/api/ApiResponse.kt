package kz.fime.samal.api

data class ApiResponse<T>(
        val status: String?,
        val message: String?,
        val data: T?,
        val links: HashMap<String, Any>,
        val meta: HashMap<String, Any>
)