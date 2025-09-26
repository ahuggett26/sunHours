import org.example.{DaylightPrinter, fetchDaylight, fetchLatLng}

import java.time.ZoneId
import scala.concurrent.ExecutionContext
import scala.io.StdIn.readChar

@main
def main(): Unit = {
  val latLng = fetchLatLng()

  println("Fetching daylight hours... \n")
  val daylight = fetchDaylight(latLng(0), latLng(1), latLng(2))

  given ExecutionContext = ExecutionContext.global

  daylight.onComplete(daylight => {
    if daylight.isFailure then println("Something went wrong.")
    else
      DaylightPrinter(daylight.get).print()
    println("\nEnter any key to exit")
  })

  readChar() // Read to avoid early exit
}

