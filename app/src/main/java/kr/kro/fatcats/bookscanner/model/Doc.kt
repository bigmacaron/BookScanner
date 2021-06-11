package kr.kro.fatcats.bookscanner.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

data class Doc(
    @SerializedName("AUTHOR")
    val author: String?,
    @SerializedName("BIB_YN")
    val bIBYN: String?,
    @SerializedName("BOOK_INTRODUCTION_URL")
    val bOOKINTRODUCTIONURL: String?,
    @SerializedName("BOOK_SIZE")
    val bOOKSIZE: String?,
    @SerializedName("BOOK_SUMMARY_URL")
    val bOOKSUMMARYURL: String?,
    @SerializedName("BOOK_TB_CNT_URL")
    val bOOKTBCNTURL: String?,
    @SerializedName("CIP_YN")
    val cIPYN: String?,
    @SerializedName("CONTROL_NO")
    val cONTROLNO: String?,
    @SerializedName("DDC")
    val dDC: String?,
    @SerializedName("DEPOSIT_YN")
    val dEPOSITYN: String?,
    @SerializedName("EA_ADD_CODE")
    val eAADDCODE: String?,
    @SerializedName("EA_ISBN")
    val eAISBN: String?,
    @SerializedName("EBOOK_YN")
    val eBOOKYN: String?,
    @SerializedName("EDITION_STMT")
    val eDITIONSTMT: String?,
    @SerializedName("FORM")
    val fORM: String?,
    @SerializedName("FORM_DETAIL")
    val fORMDETAIL: String?,
    @SerializedName("INPUT_DATE")
    val iNPUTDATE: String?,
    @SerializedName("KDC")
    val kDC: String?,
    @SerializedName("PAGE")
    val pAGE: String?,
    @SerializedName("PRE_PRICE")
    val pREPRICE: String?,
    @SerializedName("PUBLISHER")
    val publisher: String?,
    @SerializedName("PUBLISHER_URL")
    val pUBLISHERURL: String?,
    @SerializedName("PUBLISH_PREDATE")
    val pUBLISHPREDATE: String?,
    @SerializedName("REAL_PRICE")
    val rEALPRICE: String?,
    @SerializedName("REAL_PUBLISH_DATE")
    val rEALPUBLISHDATE: String?,
    @SerializedName("RELATED_ISBN")
    val rELATEDISBN: String?,
    @SerializedName("SERIES_NO")
    val sERIESNO: String?,
    @SerializedName("SERIES_TITLE")
    val sERIESTITLE: String?,
    @SerializedName("SET_ADD_CODE")
    val sETADDCODE: String?,
    @SerializedName("SET_EXPRESSION")
    val sETEXPRESSION: String?,
    @SerializedName("SET_ISBN")
    val sETISBN: String?,
    @SerializedName("SUBJECT")
    val sUBJECT: String?,
    @SerializedName("TITLE")
    val title: String?,
    @SerializedName("TITLE_URL")
    val url: String?,
    @SerializedName("UPDATE_DATE")
    val uPDATEDATE: String?,
    @SerializedName("VOL")
    val vOL: String?
)