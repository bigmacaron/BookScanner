package kr.kro.fatcats.bookscanner.model


import com.google.gson.annotations.SerializedName

data class BookInfo(
    @SerializedName("docs")
    val docs: List<Doc>?,
    @SerializedName("PAGE_NO")
    val pAGENO: String?,
    @SerializedName("TOTAL_COUNT")
    val tOTALCOUNT: String?
)