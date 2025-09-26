package org.example

import java.time.format.TextStyle
import java.util.Locale

class DaylightPrinter(private val daylight: Daylight) {

  def print(): Unit = {
    val timezone = daylight.timeZone.getDisplayName(TextStyle.SHORT, Locale.getDefault)
    println(s"      The sun will rise at ${daylight.sunriseStr} and set at ${daylight.sunsetStr}, at time zone: $timezone")
    println(s"      The current time is ${daylight.nowStr}, at time zone: $timezone")
    println(s"   |  01:00   03:00   05:00   07:00   09:00   11:00   13:00   15:00   17:00   19:00   21:00   23:00  |")

    printHalfHourChart(chooseEmoji)
    printHalfHourChart(halfHour => if daylight.nowHalfHour == halfHour then "⬆️" else "  ")
  }

  private def printHalfHourChart(consumer: Int => String): Unit = {
    val statusStr = StringBuilder("   | ")
    for halfHour <- 0 until 48 do statusStr ++= consumer(halfHour)
    statusStr ++= "|"
    println(statusStr)
  }

  private def chooseEmoji(halfHour: Int): String = {
    val day = "🏙️"
    val night = "🌃"
    val sunrise = "🌇"
    val sunset = "🌆"

    if halfHour == daylight.sunUp then sunrise
    else if halfHour == daylight.sunDown then sunset
    else {
      if daylight.sunUp < daylight.sunDown then
        if halfHour < daylight.sunUp || halfHour > daylight.sunDown then night
        else day
      else if halfHour > daylight.sunDown && halfHour < daylight.sunUp then night
      else day
    }
  }
}
