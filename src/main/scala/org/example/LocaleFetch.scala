package org.example

import java.time.ZoneId
import scala.concurrent.Await
import scala.concurrent.duration.*
import scala.io.StdIn.{readInt, readLine}

def fetchLatLng(): (Float, Float, ZoneId) = {
  println("Would you like to fetch daylight hours from (1): this IP or (2): a custom lat/lng?")
  val in = readInt
  if in == 1 then ipLatLng()
  else if in == 2 then customLatLng()
  else {
    println("Sorry, that input was not recognized. Falling back to custom lat/lng.")
    customLatLng()
  }
}

def customLatLng(): (Float, Float, ZoneId) = {
  println("Enter in a lat/lng: (e.g 51.501580,-0.141253)")
  val in = readLine().split(',')

  println("Please enter the timezone that you want to display daylight hours in: (e.g +05:00)")
  val input = readLine()

  (in(0).toFloat, in(1).toFloat, ZoneId.of(input))
}

def ipLatLng(): (Float, Float, ZoneId) = {
  val ip = Await.result(fetchIp(), 3.seconds)

  val locale = Await.result(fetchLocaleData(ip), 3.seconds)
  val json = ujson.read(locale)
  val lat = json("latitude").num.toFloat
  val lng = json("longitude").num.toFloat
  println(s"Using found location: ${json("city")} in ${json("country_name")} at ($lat, $lng)")

  // Assume when using IP address, you want to display time zone of that IP
  val zoneId = ZoneId.of(json("utc_offset").str)
  println(s"Using time zone: UTC$zoneId")

  (lat, lng, zoneId)
}