/*
This code is adapted directly from the Udacity lesson material for Room (Architecture Components)
Specifically:
    Lesson09b-ToDo-List-AAC\T09b.02-Solution-SaveTaskInDatabaseFromAddTaskActivity\
        app\src\main\java\com\example\android\todolist\database\DateConverter.java

The only change is I am NOT using Android Architecture Components in this project
 */

package com.example.ericrybarczyk.popularmovies.data;

import java.util.Date;

public class DateConverter {

    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
