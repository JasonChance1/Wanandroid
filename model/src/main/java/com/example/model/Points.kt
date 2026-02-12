package com.example.model

/**
 * @author wandervogel
 * @date 2026-02-11  星期三
 * @description 积分
 */

private val deltaRegex = Regex("""([+-]\s*\d+)\s*$""")

data class Points(
    val id: Int,
    val coinCount: Int,
    val date: Long,
    val desc: String?,// 示例：2026-01-14 15:46:49 签到 , 积分：10 + 2
    val reason: String,
    val userId: Int,
    val userName: String
) {

    val displayDate: String
        get() {
            val formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss")
            return java.time.Instant.ofEpochMilli(date)
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime()
                .format(formatter)
        }

    fun getDisplayReason(): String {
        val delta = deltaRegex.find(desc ?: "")?.groupValues?.get(1)
            ?.replace(" ", "")
        return if (delta != null) "${reason}($delta)" else reason
    }
}

data class PointsList(
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int,
    val datas: List<Points>,
    val curPage: Int
)

/**
 * coinCount":192620,"level":1927,"nickname":"goweii","rank":"1","userId":20382,"username":"g**eii
 */
data class PointsRank(
    val coinCount: Int,
    val level: Int,
    val nickname: String,
    val rank: Int,
    val userId: Int,
    val username: String
)