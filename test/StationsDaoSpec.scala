import org.specs2.mutable._

import concurrent.duration.Duration
import concurrent.{Await, Future}

import java.util.concurrent.TimeUnit

import models._
import mongo._

import TestHelpers._

class StationsDaoSpec extends Specification {

  "The StationsDao class" should {

    "save all stations" in new FakeApp {
      val stations = List(
        new Station(_id = 1, name = "Station 1", lng = 1, lat = 2),
        new Station(_id = 2, "Station 2", lng = 2, lat = 3)
      )
      StationsDao.save(stations)

      val futureStations: Future[List[Station]] = StationsDao.find()
      Await.ready(futureStations, Duration(5, TimeUnit.SECONDS))

      val savedStations = seq(futureStations)
      savedStations.size must be equalTo (2)
    }

    "save one station" in new FakeApp {
      val stationDetails = new StationDetails(stationId = 1, down = true, bikes = 10, attachs = 20)
      StationDetailsDao.save(stationDetails)

      val futureStations: Future[List[StationDetails]] = StationDetailsDao.find()
      Await.ready(futureStations, Duration(5, TimeUnit.SECONDS))

      val savedStations = seq(futureStations)
      savedStations.size must be equalTo (1)

      val savedStation = savedStations.head
      savedStation.stationId must be equalTo(1)
      savedStation.down must be equalTo(true)
      savedStation.bikes must be equalTo(10)
      savedStation.attachs must be equalTo(20)
    }

  }


}
