package org.example

import java.net.URI
import java.net.http.HttpResponse.BodyHandlers
import java.net.http.{HttpClient, HttpRequest}
import java.time.{Duration, ZoneId}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

def fetchDaylight(lat: Float, lng: Float, timezone: ZoneId): Future[Daylight] = {
  Future {
    val request = HttpRequest.newBuilder()
      .uri(URI(s"https://api.sunrise-sunset.org/json?lat=$lat&lng=$lng&formatted=0"))
      .GET()
      .timeout(Duration.ofSeconds(3))
      .build()

    val response = HttpClient.newHttpClient().sendAsync(request, BodyHandlers.ofString()).get()
    Daylight(response.body(), timezone)
  }
}

def fetchLocaleData(ipAddress: String): Future[String] = {
  Future {
    val request = HttpRequest.newBuilder()
      .uri(URI(s"https://ipapi.co/$ipAddress/json/"))
      .GET()
      .timeout(Duration.ofSeconds(3))
      .build()

    HttpClient.newHttpClient().sendAsync(request, BodyHandlers.ofString()).get().body()
  }
}

def fetchIp(): Future[String] = {
  Future {
    val request = HttpRequest.newBuilder()
      .uri(URI("https://api64.ipify.org?format=json"))
      .GET()
      .timeout(Duration.ofSeconds(3))
      .build()

    val json = HttpClient.newHttpClient().sendAsync(request, BodyHandlers.ofString()).get().body()
    ujson.read(json)("ip").str
  }
}

def reverseGeocode(lat: Float, lng: Float): Future[String] = {
  Future {
    val request = HttpRequest.newBuilder()
      .uri(URI(s"https://geokeo.com/geocode/v1/reverse.php?lat=$lat&lng=$lng&api=YOUR_API_KEY"))
      .GET()
      .timeout(Duration.ofSeconds(3))
      .build()

    val json = HttpClient.newHttpClient().sendAsync(request, BodyHandlers.ofString()).get().body()
    ujson.read(json)("ip").str
  }
}