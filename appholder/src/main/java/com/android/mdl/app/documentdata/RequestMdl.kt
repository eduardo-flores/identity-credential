package com.android.mdl.app.documentdata

import com.android.mdl.app.R

object RequestMdl : RequestDocument() {

    override val docType = "org.iso.18013.5.1.mDL"
    override val nameSpace = "org.iso.18013.5.1"
    override val dataItems = DataItems.values().asList()

    enum class DataItems(
        override val identifier: String,
        override val stringResourceId: Int
    ) : RequestDataItem {
        FAMILY_NAME("family_name", R.string.org_iso_18013_5_1_family_name),
        GIVEN_NAMES("given_name", R.string.org_iso_18013_5_1_given_name),
        BIRTH_DATE("birth_date", R.string.org_iso_18013_5_1_birth_date),
        ISSUE_DATE("issue_date", R.string.org_iso_18013_5_1_issue_date),
        EXPIRY_DATE("expiry_date", R.string.org_iso_18013_5_1_expiry_date),
        ISSUING_COUNTRY("issuing_country", R.string.org_iso_18013_5_1_issuing_country),
        ISSUING_AUTHORITY("issuing_authority", R.string.org_iso_18013_5_1_issuing_authority),
        DOCUMENT_NUMBER("document_number", R.string.org_iso_18013_5_1_document_number),
        PORTRAIT("portrait", R.string.org_iso_18013_5_1_portrait),
        DRIVING_PRIVILEGES("driving_privileges", R.string.org_iso_18013_5_1_driving_privileges),
        UN_DISTINGUISHING_SIGN("un_distinguishing_sign", R.string.org_iso_18013_5_1_un_distinguishing_sign),
        ADMINISTRATIVE_NUMBER("administrative_number", R.string.org_iso_18013_5_1_administrative_number),
        HEIGHT("height", R.string.org_iso_18013_5_1_height),
        WEIGHT("weight", R.string.org_iso_18013_5_1_weight),
        EYE_COLOUR("eye_colour", R.string.org_iso_18013_5_1_eye_colour),
        HAIR_COLOUR("hair_colour", R.string.org_iso_18013_5_1_hair_colour),
        BIRTH_PLACE("birth_place", R.string.org_iso_18013_5_1_birth_place),
        RESIDENT_ADDRESS("resident_address", R.string.org_iso_18013_5_1_resident_address),
        PORTRAIT_CAPTURE_DATE("portrait_capture_date", R.string.org_iso_18013_5_1_portrait_capture_date),
        AGE_IN_YEARS("age_in_years", R.string.org_iso_18013_5_1_age_in_years),
        AGE_BIRTH_YEAR("age_birth_year", R.string.org_iso_18013_5_1_age_birth_year),
        AGE_OVER_18("age_over_18", R.string.org_iso_18013_5_1_age_over_18),
        AGE_OVER_21("age_over_21", R.string.org_iso_18013_5_1_age_over_21),
        ISSUING_JURISDICTION("issuing_jurisdiction", R.string.org_iso_18013_5_1_issuing_jurisdiction),
        NATIONALITY("nationality", R.string.org_iso_18013_5_1_nationality),
        RESIDENT_CITY("resident_city", R.string.org_iso_18013_5_1_resident_city),
        RESIDENT_STATE("resident_state", R.string.org_iso_18013_5_1_resident_state),
        RESIDENT_POSTAL_CODE("resident_postal_code", R.string.org_iso_18013_5_1_resident_postal_code),
        RESIDENT_COUNTRY("resident_country", R.string.org_iso_18013_5_1_resident_country),
        FAMILY_NAME_NATIONAL_CHARACTER("family_name_national_character", R.string.org_iso_18013_5_1_family_name_national_character),
        GIVEN_NAME_NATIONAL_CHARACTER("given_name_national_character", R.string.org_iso_18013_5_1_given_name_national_character),
        SIGNATURE_USUAL_MARK("signature_usual_mark", R.string.org_iso_18013_5_1_signature_usual_mark)
    }
}