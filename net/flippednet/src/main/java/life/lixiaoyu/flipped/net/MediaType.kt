package life.lixiaoyu.flipped.net

import java.lang.IllegalArgumentException
import java.nio.charset.Charset
import java.util.Locale
import java.util.regex.Pattern

class MediaType private constructor(
    private val mediaType: String,

    val type: String,

    val subtype: String,

    private val parameterNamesAndValues: Array<String>
) {
    fun charset(defaultValue: Charset? = null): Charset? {
        val charset = parameter("charset") ?: return defaultValue
        return try {
            Charset.forName(charset)
        } catch (_: IllegalArgumentException) {
            defaultValue
        }
    }

    fun parameter(name: String): String? {
        for(i in parameterNamesAndValues.indices step 2) {
            if (parameterNamesAndValues[i].equals(name, ignoreCase = true)) {
                return parameterNamesAndValues[i + 1]
            }
        }
        return null
    }

    override fun toString(): String = mediaType

    override fun equals(other: Any?): Boolean = other is MediaType && other.mediaType == mediaType

    override fun hashCode(): Int = mediaType.hashCode()

    companion object {
        private const val TOKEN = "([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)"
        private const val QUOTED = "\"([^\"]*)\""
        private val TYPE_SUBTYPE = Pattern.compile("$TOKEN/$TOKEN")
        private val PARAMETER = Pattern.compile(";\\s*(?:$TOKEN=(?:$TOKEN|$QUOTED))?")

        fun String.toMediaType(): MediaType {
            val typeSubtype = TYPE_SUBTYPE.matcher(this)
            require(typeSubtype.lookingAt()) { "No subtype found for: \"$this\"" }
            val type = typeSubtype.group(1).lowercase(Locale.US)
            val subtype = typeSubtype.group(2).lowercase(Locale.US)

            val parameterNamesAndValues = mutableListOf<String>()
            val parameter = PARAMETER.matcher(this)
            var s = typeSubtype.end()
            while (s < length) {
                parameter.region(s, length)
                require(parameter.lookingAt()) {
                    "Parameter is not formatted correctly: \"${substring(s)}\" for: \"$this\""
                }

                val name = parameter.group(1)
                if (name == null) {
                    s = parameter.end()
                    continue
                }

                val token = parameter.group(2)
                val value = when {
                    token == null -> {
                        // Value is "double-quoted". That's valid and our regex group already strips the quotes.
                        parameter.group(3)
                    }
                    token.startsWith("'") && token.endsWith("'") && token.length > 2 -> {
                        // If the token is 'single-quoted' it's invalid! But we're lenient and strip the quotes.
                        token.substring(1, token.length - 1)
                    }
                    else -> token
                }

                parameterNamesAndValues += name
                parameterNamesAndValues += value
                s = parameter.end()
            }

            return MediaType(this, type, subtype, parameterNamesAndValues.toTypedArray())
        }

        fun String.toMediaTypeOrNull(): MediaType? {
            return try {
                toMediaType()
            } catch (_: IllegalArgumentException) {
                null
            }
        }
    }
}