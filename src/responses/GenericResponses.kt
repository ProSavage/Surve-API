package us.surve.responses



fun getGenericErrorResponse(message: String): Map<String, String> {
    return mapOf("message" to message)
}