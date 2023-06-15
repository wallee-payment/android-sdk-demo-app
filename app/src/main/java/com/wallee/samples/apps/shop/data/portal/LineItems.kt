package com.wallee.samples.apps.shop.data.portal

import com.google.gson.annotations.SerializedName

data class LineItems(
    @SerializedName("aggregatedTaxRate") var aggregatedTaxRate: Int? = null,
    @SerializedName("amountExcludingTax") var amountExcludingTax: Int? = null,
    @SerializedName("amountIncludingTax") var amountIncludingTax: Int? = null,
    @SerializedName("attributes") var attributes: Attributes? = Attributes(),
    @SerializedName("currency") var currency: String? = null,
    @SerializedName("discountExcludingTax") var discountExcludingTax: Int? = null,
    @SerializedName("discountIncludingTax") var discountIncludingTax: Int? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("quantity") var quantity: Int? = null,
    @SerializedName("shippingRequired") var shippingRequired: Boolean? = null,
    @SerializedName("sku") var sku: String? = null,
    @SerializedName("taxAmount") var taxAmount: Int? = null,
    @SerializedName("taxAmountPerUnit") var taxAmountPerUnit: Int? = null,
    @SerializedName("taxes") var taxes: ArrayList<String> = arrayListOf(),
    @SerializedName("type") var type: String? = null,
    @SerializedName("undiscountedAmountExcludingTax") var undiscountedAmountExcludingTax: Int? = null,
    @SerializedName("undiscountedAmountIncludingTax") var undiscountedAmountIncludingTax: Int? = null,
    @SerializedName("undiscountedUnitPriceExcludingTax") var undiscountedUnitPriceExcludingTax: Int? = null,
    @SerializedName("undiscountedUnitPriceIncludingTax") var undiscountedUnitPriceIncludingTax: Int? = null,
    @SerializedName("uniqueId") var uniqueId: String? = null,
    @SerializedName("unitPriceExcludingTax") var unitPriceExcludingTax: Int? = null,
    @SerializedName("unitPriceIncludingTax") var unitPriceIncludingTax: Int? = null,
    @SerializedName("version") var version: Int? = null

)
