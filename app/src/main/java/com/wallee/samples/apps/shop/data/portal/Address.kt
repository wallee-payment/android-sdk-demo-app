package com.wallee.samples.apps.shop.data.portal

import com.google.gson.annotations.SerializedName

data class Address(

    @SerializedName("city") var city: String? = null,
    @SerializedName("country") var country: String? = null,
    @SerializedName("emailAddress") var emailAddress: String? = null,
    @SerializedName("familyName") var familyName: String? = null,
    @SerializedName("givenName") var givenName: String? = null,
    @SerializedName("organisationName") var organisationName: String? = null,
    @SerializedName("postcode") var postcode: String? = null,
    @SerializedName("street") var street: String? = null,
    @SerializedName("socialSecurityNumber") var socialSecurityNumber: String? = null

)