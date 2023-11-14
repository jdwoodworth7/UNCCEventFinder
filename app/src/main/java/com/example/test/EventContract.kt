package com.example.test

import android.provider.BaseColumns

object EventContract {
    // Define table and column names
    object EventEntry : BaseColumns {
        const val TABLE_NAME = "events"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_DATE = "date"
        const val COLUMN_TIME = "time"
        const val COLUMN_BUILDING_NAME = "building_name"
        const val COLUMN_ADDRESS = "address"
        const val COLUMN_IMAGE_URL = "image_url"
        const val COLUMN_CATEGORY_ACADEMIC = "category_academic"
        const val COLUMN_CATEGORY_SOCIAL = "category_social"
        const val COLUMN_CATEGORY_SPORTS = "category_sports"
        const val COLUMN_CATEGORY_CLUBS_ORG = "category_clubs_org"
        const val COLUMN_CATEGORY_WORKSHOPS = "category_workshops"
        const val COLUMN_CATEGORY_VOLUNTEERING = "category_volunteering"
        const val COLUMN_CATEGORY_STUDENTS_ONLY = "category_students_only"
    }

    // Define SQL statements for creating and deleting tables
    const val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${EventEntry.TABLE_NAME} (" +
                "${EventEntry.COLUMN_TITLE} TEXT," +
                "${EventEntry.COLUMN_DESCRIPTION} TEXT," +
                "${EventEntry.COLUMN_DATE} TEXT," +
                "${EventEntry.COLUMN_TIME} TEXT," +
                "${EventEntry.COLUMN_BUILDING_NAME} TEXT," +
                "${EventEntry.COLUMN_ADDRESS} TEXT," +
                "${EventEntry.COLUMN_IMAGE_URL} TEXT," +
                "${EventEntry.COLUMN_CATEGORY_ACADEMIC} INTEGER," +
                "${EventEntry.COLUMN_CATEGORY_SOCIAL} INTEGER," +
                "${EventEntry.COLUMN_CATEGORY_SPORTS} INTEGER," +
                "${EventEntry.COLUMN_CATEGORY_CLUBS_ORG} INTEGER," +
                "${EventEntry.COLUMN_CATEGORY_WORKSHOPS} INTEGER," +
                "${EventEntry.COLUMN_CATEGORY_VOLUNTEERING} INTEGER," +
                "${EventEntry.COLUMN_CATEGORY_STUDENTS_ONLY} INTEGER" +
                ")"


    // Define SQL statement for deleting table
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${EventEntry.TABLE_NAME}"
}