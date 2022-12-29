package com.android.mdl.app.documentdata

import com.android.mdl.app.R

object RequestMicovVtr : RequestDocument() {

    override val docType = "org.micov.1"
    override val nameSpace = "org.micov.vtr.1"
    override val dataItems = DataItems.values().asList()

    enum class DataItems(
        override val identifier: String,
        override val stringResourceId: Int
    ) : RequestDataItem {
        FAMILY_NAME("fn", R.string.org_micov_vtr_1_fn),
        GIVEN_NAME("gn", R.string.org_micov_vtr_1_gn),
        DATE_OF_BIRTH("dob", R.string.org_micov_vtr_1_dob),
        SEX("sex", R.string.org_micov_vtr_1_sex),
        FIRST_VACCINATION_AGAINST_RA01("v_RA01_1", R.string.org_micov_vtr_1_v_RA01_1),
        SECOND_VACCINATION_AGAINST_RA01("v_RA01_2", R.string.org_micov_vtr_1_v_RA01_2),
        ID_WITH_PASPORT_NUMBER("pid_PPN", R.string.org_micov_vtr_1_pid_PPN),
        ID_WITH_DRIVERS_LICENSE_NUMBER("pid_DL", R.string.org_micov_vtr_1_pid_DL)
    }
}