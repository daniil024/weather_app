package com.example.yandexweatherapp.utils

enum class Cities(val city: String, val lon: Double, val lat: Double, val timezone:String) {

    CHOOSE("Город", 0.0, 0.0, ""),
    NEW_YORK("Нью-Йорк", -74.006111, 40.712778, "America/New_York"),
    LONDON("Лондон", -0.1275, 51.507222, "Europe/London"),
    BARCELONA("Барселона", 2.183333, 41.383333, "Europe/Madrid")
}