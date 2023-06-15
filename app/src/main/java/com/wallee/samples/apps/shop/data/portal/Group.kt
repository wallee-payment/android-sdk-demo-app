package com.wallee.samples.apps.shop.data.portal

import com.google.gson.annotations.SerializedName

data class Group(
    @SerializedName("beginDate") var beginDate: String? = null,
    @SerializedName("customerId") var customerId: String? = null,
    @SerializedName("endDate") var endDate: String? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("linkedSpaceId") var linkedSpaceId: Int? = null,
    @SerializedName("plannedPurgeDate") var plannedPurgeDate: String? = null,
    @SerializedName("state") var state: String? = null,
    @SerializedName("version") var version: Int? = null

)
