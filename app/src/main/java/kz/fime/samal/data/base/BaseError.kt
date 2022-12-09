package kz.fime.samal.data.base

open class BaseError(
    open val code: BaseErrorCode,
    open val underlying: Throwable? = null,
    open val devMessage: String? = null
) : Exception(devMessage, underlying) {

    open fun domain(): String = this.javaClass.simpleName
    open fun errorCode(): Int = code.code
    open fun errorCodeName(): String = code.toString()
    open fun shortName(): String = ""

}

interface BaseErrorCode {
    val code: Int
}