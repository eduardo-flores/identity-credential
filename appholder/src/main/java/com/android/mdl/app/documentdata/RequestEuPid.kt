package com.android.mdl.app.documentdata

import com.android.mdl.app.R

object RequestEuPid : RequestDocument() {

    override val docType = "eu.europa.ec.eudiw.pid.1"
    override val nameSpace = "eu.europa.ec.eudiw.pid.1"
    override val dataItems = DataItems.values().asList()

    enum class DataItems(
        override val identifier: String,
        override val stringResourceId: Int
    ) : RequestDataItem {
        FAMILY_NAME("family_name", R.string.eu_europa_ec_eudiw_pid_1_family_name),
        FAMILY_NAME_NATIONAL_CHARACTER("family_name_national_characters", R.string.eu_europa_ec_eudiw_pid_1_family_name_national_characters),
        GIVEN_NAMES("given_name", R.string.eu_europa_ec_eudiw_pid_1_given_name),
        GIVEN_NAME_NATIONAL_CHARACTER("given_name_national_characters", R.string.eu_europa_ec_eudiw_pid_1_given_name_national_characters),
        BIRTH_DATE("birth_date", R.string.eu_europa_ec_eudiw_pid_1_birth_date),
        UNIQUE_IDENTIFIER("issue_date", R.string.eu_europa_ec_eudiw_pid_1_unique_identifier),
        BIRTH_FAMILY_NAME("family_name_birth", R.string.eu_europa_ec_eudiw_pid_1_family_name_birth),
        BIRTH_FAMILY_NAME_NATIONAL_CHARACTERS("family_name_birth_national_characters", R.string.eu_europa_ec_eudiw_pid_1_family_name_birth_national_characters),
        BIRTH_FIRST_NAME("given_name_birth", R.string.eu_europa_ec_eudiw_pid_1_given_name_birth),
        BIRTH_FIRST_NAME_NATIONAL_CHARACTERS("given_name_birth_national_characters", R.string.eu_europa_ec_eudiw_pid_1_given_name_birth_national_characters),
        BIRTH_PLACE("birth_place", R.string.eu_europa_ec_eudiw_pid_1_birth_place),
        RESIDENT_ADDRESS("resident_address", R.string.eu_europa_ec_eudiw_pid_1_resident_address),
        RESIDENT_POSTAL_CODE("resident_postal_code", R.string.eu_europa_ec_eudiw_pid_1_resident_postal_code),
        RESIDENT_CITY("resident_city", R.string.eu_europa_ec_eudiw_pid_1_resident_city),
        RESIDENT_STATE("resident_state", R.string.eu_europa_ec_eudiw_pid_1_resident_state),
        RESIDENT_COUNTRY("resident_country", R.string.eu_europa_ec_eudiw_pid_1_resident_country),
        GENDER("gender", R.string.eu_europa_ec_eudiw_pid_1_gender),
        NATIONALITY("nationality", R.string.eu_europa_ec_eudiw_pid_1_nationality),
        PORTRAIT("portrait", R.string.eu_europa_ec_eudiw_pid_1_portrait),
        PORTRAIT_CAPTURE_DATE("portrait_capture_date", R.string.eu_europa_ec_eudiw_pid_1_portrait_capture_date),
        AGE_OVER_13("age_over_13", R.string.eu_europa_ec_eudiw_pid_1_age_over_13),
        AGE_OVER_16("age_over_16", R.string.eu_europa_ec_eudiw_pid_1_age_over_16),
        AGE_OVER_18("age_over_18", R.string.eu_europa_ec_eudiw_pid_1_age_over_18),
        AGE_OVER_21("age_over_21", R.string.eu_europa_ec_eudiw_pid_1_age_over_21),
        AGE_OVER_60("age_over_60", R.string.eu_europa_ec_eudiw_pid_1_age_over_60),
        AGE_OVER_65("age_over_65", R.string.eu_europa_ec_eudiw_pid_1_age_over_65),
        AGE_OVER_68("age_over_68", R.string.eu_europa_ec_eudiw_pid_1_age_over_68),
        AGE_IN_YEARS("age_in_years", R.string.eu_europa_ec_eudiw_pid_1_age_in_years),
        AGE_BIRTH_YEAR("age_birth_year", R.string.eu_europa_ec_eudiw_pid_1_age_birth_year),
    }
}