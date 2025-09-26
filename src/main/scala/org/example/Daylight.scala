package org.example

import java.time.{ZoneId, ZonedDateTime}
import java.time.format.DateTimeFormatter.ISO_LOCAL_TIME
import java.time.temporal.ChronoUnit

class Daylight(private val jsonBody: String, val timeZone: ZoneId) {

  private val results = ujson.read(jsonBody)("results")

  private val sunrise = ZonedDateTime.parse(results("sunrise").str).withZoneSameInstant(timeZone)
  private val sunset = ZonedDateTime.parse(results("sunset").str).withZoneSameInstant(timeZone)

  val sunriseStr: String = sunrise.format(ISO_LOCAL_TIME)
  val sunsetStr: String = sunset.format(ISO_LOCAL_TIME)

  /** Representing half-hour (48 in day) of sunup */
  val sunUp: Int = (sunrise.getHour * 2) + extraHalfHour(sunrise.getMinute)
  /** Representing half-hour (48 in day) of sundown */
  val sunDown: Int = (sunset.getHour * 2) + extraHalfHour(sunset.getMinute)

  private val now = ZonedDateTime.now(timeZone).truncatedTo(ChronoUnit.SECONDS)
  val nowStr: String = now.format(ISO_LOCAL_TIME)
  val nowHalfHour: Int = (now.getHour * 2) + extraHalfHour(now.getMinute)


  private def extraHalfHour(min: Int) =
    if sunrise.getMinute >= 30 then 1 else 0
}
