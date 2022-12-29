package com.android.mdl.app.documentdata

import com.android.mdl.app.R

object RequestMicovAtt : RequestDocument() {

    override val docType = "org.micov.1"
    override val nameSpace = "org.micov.attestation.1"
    override val dataItems = DataItems.values().asList()

    enum class DataItems(
        override val identifier: String,
        override val stringResourceId: Int
    ) : RequestDataItem {
        INDICATION_OF_VACCINATION_YELLOW_FEVER("D47_vaccinated", R.string.org_micov_attestation_1_D47_vaccinated),
        INDICATION_OF_VACCINATION_COVID_19("RA01_vaccinated", R.string.org_micov_attestation_1_RA01_vaccinated),
        INDICATION_OF_TEST_EVENT_COVID_19("RA01_test", R.string.org_micov_attestation_1_RA01_test),
        SAFE_ENTRY_INDICATION("safeEntry_Leisure", R.string.org_micov_attestation_1_safeEntry_Leisure),
        FACIAL_IMAGE("fac", R.string.org_micov_attestation_1_fac),
        FAMILY_NAME_INITIAL("fni", R.string.org_micov_attestation_1_fni),
        GIVEN_AME_INITIAL("gni", R.string.org_micov_attestation_1_gni),
        BIRTH_YEAR("by", R.string.org_micov_attestation_1_by),
        BIRTH_MONTH("bm", R.string.org_micov_attestation_1_bm),
        BIRTH_DAY("bd", R.string.org_micov_attestation_1_bd)
    }
}