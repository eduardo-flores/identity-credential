package com.android.mdl.app.documentdata

import com.android.mdl.app.R

object RequestMvr : RequestDocument() {

    override val docType = "nl.rdw.mekb.1"
    override val nameSpace = "nl.rdw.mekb.1"
    override val dataItems = DataItems.values().asList()

    enum class DataItems(
        override val identifier: String,
        override val stringResourceId: Int
    ) : RequestDataItem {
        REGISTRATION_INFO("registration_info", R.string.nl_rdw_mekb_1_registration_info),
        ISSUE_DATE("issue_date", R.string.nl_rdw_mekb_1_issue_date),
        REGISTRATION_HOLDER("registration_holder", R.string.nl_rdw_mekb_1_registration_holder),
        BASIC_VEHICLE_INFO("basic_vehicle_info", R.string.nl_rdw_mekb_1_basic_vehicle_info),
        VIN("vin", R.string.nl_rdw_mekb_1_vin)
    }
}