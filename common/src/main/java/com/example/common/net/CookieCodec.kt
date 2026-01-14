package com.example.common.net

import okhttp3.Cookie

object CookieCodec {

    // 用 | 分隔，注意 value 里可能含特殊字符，所以做简单转义
    fun encode(c: Cookie): String {
        fun esc(s: String) = s.replace("\\", "\\\\").replace("|", "\\|")
        return listOf(
            esc(c.name),
            esc(c.value),
            esc(c.domain),
            esc(c.path),
            c.expiresAt.toString(),
            c.secure.toString(),
            c.httpOnly.toString(),
            c.hostOnly.toString(),
            c.persistent.toString()
        ).joinToString("|")
    }

    fun decode(s: String): Cookie? {
        return runCatching {
            val parts = splitEscaped(s)
            if (parts.size < 9) return null

            val name = parts[0]
            val value = parts[1]
            val domain = parts[2]
            val path = parts[3]
            val expiresAt = parts[4].toLong()
            val secure = parts[5].toBoolean()
            val httpOnly = parts[6].toBoolean()
            val hostOnly = parts[7].toBoolean()
            val persistent = parts[8].toBoolean()

            val b = Cookie.Builder()
                .name(name)
                .value(value)
                .path(path)
                .expiresAt(expiresAt)

            if (hostOnly) b.hostOnlyDomain(domain) else b.domain(domain)
            if (secure) b.secure()
            if (httpOnly) b.httpOnly()

            b.build()
        }.getOrNull()
    }

    private fun splitEscaped(input: String): List<String> {
        val out = ArrayList<String>()
        val sb = StringBuilder()
        var escaping = false
        for (ch in input) {
            when {
                escaping -> { sb.append(ch); escaping = false }
                ch == '\\' -> escaping = true
                ch == '|' -> { out.add(sb.toString()); sb.setLength(0) }
                else -> sb.append(ch)
            }
        }
        out.add(sb.toString())
        return out
    }
}
