package testing

object Resources {
    fun getResourceString(path: String) = javaClass.getResource(path)?.readText()
}