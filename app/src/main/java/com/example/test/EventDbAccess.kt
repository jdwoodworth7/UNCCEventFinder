package com.example.test

import android.content.Context
import com.example.test.EventData
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

class EventDbAccess(private val context: Context) {
    // Function to fetch data from the events table
    fun getEventDataFromDatabase(): List<EventData> {

        val dbHelper = EventDbHelper(context)
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            EventContract.EventEntry.COLUMN_ID,
            EventContract.EventEntry.COLUMN_TITLE,
            EventContract.EventEntry.COLUMN_DESCRIPTION,
            EventContract.EventEntry.COLUMN_DATE,
            EventContract.EventEntry.COLUMN_TIME,
            EventContract.EventEntry.COLUMN_BUILDING_NAME,
            EventContract.EventEntry.COLUMN_ADDRESS,
            EventContract.EventEntry.COLUMN_IMAGE_URL,
            EventContract.EventEntry.COLUMN_CATEGORY_ACADEMIC,
            EventContract.EventEntry.COLUMN_CATEGORY_SOCIAL,
            EventContract.EventEntry.COLUMN_CATEGORY_SPORTS,
            EventContract.EventEntry.COLUMN_CATEGORY_CLUBS_ORG,
            EventContract.EventEntry.COLUMN_CATEGORY_WORKSHOPS,
            EventContract.EventEntry.COLUMN_CATEGORY_VOLUNTEERING,
            EventContract.EventEntry.COLUMN_CATEGORY_STUDENTS_ONLY
        )

        val sortOrder = "${EventContract.EventEntry.COLUMN_DATE} ASC, ${EventContract.EventEntry.COLUMN_TIME} ASC"

        val cursor = db.query(
            EventContract.EventEntry.TABLE_NAME,  // The table to query
            projection,                           // The columns to return
            null,                         // The columns for the WHERE clause
            null,                      // The values for the WHERE clause
            null,                         // Don't group the rows
            null,                          // Don't filter by row groups
            sortOrder                             // The sort order
        )

        val eventList = mutableListOf<EventData>()

        while (cursor.moveToNext()) {
            //retrieves UUID as a string
            val idString =
                cursor.getString(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_ID))
            // Check if idString is not null and not empty before creating UUID
            val id = if (!idString.isNullOrEmpty()) {
                try {
                    UUID.fromString(idString)
                } catch (e: IllegalArgumentException) {
                    // Handle the case where the UUID string is not valid
                    // For example, provide a default UUID
                    UUID.randomUUID()
                }
            } else {
                // If idString is null or empty, provide a default UUID
                UUID.randomUUID()
            }

            val title =
                cursor.getString(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_TITLE))
            val description =
                cursor.getString(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_DESCRIPTION))
            val date =
                LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_DATE)))
            val time =
                LocalTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_TIME)))
            val buildingName =
                cursor.getString(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_BUILDING_NAME))
            val address =
                cursor.getString(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_ADDRESS))
            val imageUrl =
                cursor.getString(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_IMAGE_URL))

            val academic =
                cursor.getInt(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_CATEGORY_ACADEMIC)) == 1
            val social =
                cursor.getInt(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_CATEGORY_SOCIAL)) == 1
            val sports =
                cursor.getInt(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_CATEGORY_SPORTS)) == 1
            val clubsOrg =
                cursor.getInt(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_CATEGORY_CLUBS_ORG)) == 1
            val workshops =
                cursor.getInt(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_CATEGORY_WORKSHOPS)) == 1
            val volunteering =
                cursor.getInt(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_CATEGORY_VOLUNTEERING)) == 1
            val studentsOnly =
                cursor.getInt(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_CATEGORY_STUDENTS_ONLY)) == 1

            val categories = mutableListOf<String>()
            if (academic) categories.add("Academic")
            if (social) categories.add("Social")
            if (sports) categories.add("Sports")
            if (clubsOrg) categories.add("Clubs/Organizations")
            if (workshops) categories.add("Workshops/Seminars")
            if (volunteering) categories.add("Volunteering")
            if (studentsOnly) categories.add("Students Only")

            val eventData = EventData(
                id,
                title,
                description,
                date.toString(),
                time.toString(),
                buildingName,
                address,
                imageUrl,
                categories
            )
            eventList.add(eventData)
        }
        cursor.close()

        return eventList
    }
    companion object{
        fun removeEventFromDatabase(context: Context, itemTitle: String){
            val dbHelper = EventDbHelper(context)
            val db = dbHelper.readableDatabase
            //val tableName = getTableName(context).toString()


            db.delete("events", "title=?", arrayOf(itemTitle))
            db.close()
        }

    }

    fun getEventById(eventId: UUID): List<EventData> {
        val dbHelper = EventDbHelper(context)
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            EventContract.EventEntry.COLUMN_ID,
            EventContract.EventEntry.COLUMN_TITLE,
            EventContract.EventEntry.COLUMN_DESCRIPTION,
            EventContract.EventEntry.COLUMN_DATE,
            EventContract.EventEntry.COLUMN_TIME,
            EventContract.EventEntry.COLUMN_BUILDING_NAME,
            EventContract.EventEntry.COLUMN_ADDRESS,
            EventContract.EventEntry.COLUMN_IMAGE_URL,
            EventContract.EventEntry.COLUMN_CATEGORY_ACADEMIC,
            EventContract.EventEntry.COLUMN_CATEGORY_SOCIAL,
            EventContract.EventEntry.COLUMN_CATEGORY_SPORTS,
            EventContract.EventEntry.COLUMN_CATEGORY_CLUBS_ORG,
            EventContract.EventEntry.COLUMN_CATEGORY_WORKSHOPS,
            EventContract.EventEntry.COLUMN_CATEGORY_VOLUNTEERING,
            EventContract.EventEntry.COLUMN_CATEGORY_STUDENTS_ONLY
        )

        val selection = "${EventContract.EventEntry.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(eventId.toString())

        val cursor = db.query(
            EventContract.EventEntry.TABLE_NAME,  // The table to query
            projection,                           // The columns to return
            selection,                            // The columns for the WHERE clause
            selectionArgs,                        // The values for the WHERE clause
            null,                                 // Don't group the rows
            null,                                 // Don't filter by row groups
            null                                  // No sort order needed for a single row
        )

        val eventData = mutableListOf<EventData>()

        while (cursor.moveToNext()) {
            //retrieves UUID as a string
            val idString =
                cursor.getString(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_ID))

        val id = if (!idString.isNullOrEmpty()) {
            try {
                UUID.fromString(idString)
            } catch (e: IllegalArgumentException) {
                // Handle the case where the UUID string is not valid
                // For example, provide a default UUID
                UUID.randomUUID()
            }
        } else {
            // If idString is null or empty, provide a default UUID
            UUID.randomUUID()
        }

        val title =
            cursor.getString(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_TITLE))
        val description =
            cursor.getString(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_DESCRIPTION))
        val date =
            LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_DATE)))
        val time =
            LocalTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_TIME)))
        val buildingName =
            cursor.getString(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_BUILDING_NAME))
        val address =
            cursor.getString(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_ADDRESS))
        val imageUrl =
            cursor.getString(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_IMAGE_URL))

        val academic =
            cursor.getInt(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_CATEGORY_ACADEMIC)) == 1
        val social =
            cursor.getInt(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_CATEGORY_SOCIAL)) == 1
        val sports =
            cursor.getInt(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_CATEGORY_SPORTS)) == 1
        val clubsOrg =
            cursor.getInt(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_CATEGORY_CLUBS_ORG)) == 1
        val workshops =
            cursor.getInt(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_CATEGORY_WORKSHOPS)) == 1
        val volunteering =
            cursor.getInt(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_CATEGORY_VOLUNTEERING)) == 1
        val studentsOnly =
            cursor.getInt(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_CATEGORY_STUDENTS_ONLY)) == 1

        val categories = mutableListOf<String>()
        if (academic) categories.add("Academic")
        if (social) categories.add("Social")
        if (sports) categories.add("Sports")
        if (clubsOrg) categories.add("Clubs/Organizations")
        if (workshops) categories.add("Workshops/Seminars")
        if (volunteering) categories.add("Volunteering")
        if (studentsOnly) categories.add("Students Only")

            // Create an EventData object for the retrieved row
            val eventData = EventData(
                id,
                title,
                description,
                date,
                time,
                buildingName,
                address,
                imageUrl,
                categories
            )
        }
        cursor.close()

        return eventData
    }

}