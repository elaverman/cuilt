/*
 *  @author Mihaela Verman
 *  
 *  Copyright 2016 University of Zurich
 *      
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 *         http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  
 */

package com.signalcollect.dcop.custom

import java.io.BufferedReader
import scala.util.Random
import com.signalcollect._
import com.signalcollect.dcop.modules._
import com.signalcollect.dcop.graph._

//TODO: Decouple the functions for building vertices/edges from the algorithm. Add pluggable function for action initialization.
class ScheduleGraphReader(
  eventAlgo: IntAlgorithm,
  slotAlgo: IntAlgorithm,
  eventsNumber: Int,
  timeSlots: Int,
  rooms: Int,
  commonPeopleFile: String,
  lectureSizeFile: String,
  roomsFile: String,
  minOccupationRate: Double = 0.0) extends GraphInstantiator {
  var util = 0L

  def build(graphBuilder: GraphBuilder[Any, Any] = GraphBuilder): Graph[Any, Any] = {

    val g = graphBuilder.build

    val allSlots = (for { ts <- (1 to timeSlots); r <- (1 to rooms) }
      yield getSlotId(r, ts)).toList
    val allEvents = (for (e <- (1 to eventsNumber))
      yield (e * 2)).toList

    val commonPeople = computeCommonPeopleForEvents(allEvents)
    // Changed from *2 id to actual id.
    val lectureSize = computeLectureSize()
    val roomSize = computeRoomSize()
    val (eventsToRooms, roomsToEvents) = computeEdges(allEvents, allSlots, roomSize, lectureSize)

    val allSlotsList = allSlots.toList
    val allEventsList = allEvents.toList

    for (eventId <- 1 to eventsNumber) {
      if (!eventsToRooms(eventId).isEmpty) {
        val domain = (for { ts <- (1 to timeSlots); r <- eventsToRooms(eventId) }
          yield getSlotId(r, ts)).toSet
        g.addVertex(eventAlgo.createVertex(eventId * 2, getRandomFromDomain(domain), domain, Some(commonPeople(eventId))))
      } else {
        g.addVertex(eventAlgo.createVertex(eventId * 2, 0, Set(0), Some(commonPeople(eventId))))
      }
    }
    for (slotId <- allSlots) {
      // Slots can also be non-allocated, so the domain includes 0.
      g.addVertex(slotAlgo.createVertex(slotId, 0, roomsToEvents(getRoomId(slotId)).map(_ * 2) + 0))
    }

    //Add edges
    for (eventId <- 1 to eventsNumber) {
      //event to event
      for ((ev2, (s, p)) <- commonPeople(eventId)) {
        g.addEdge(eventId * 2, eventAlgo.createEdge(targetId = ev2))
        util += s + p
      }
      val thisLectureSize = lectureSize(eventId)

      //event to slot and slot to event
      // It might be the case that there are no outgoing edges, e.g. classes with 0 students.
      for (roomId <- eventsToRooms(eventId)) {
        val thisRoomSize = roomSize(roomId)
        if ((thisLectureSize > thisRoomSize * minOccupationRate)
          && (thisLectureSize <= thisRoomSize)) {
          for (ts <- (1 to timeSlots)) {
            val slotId = getSlotId(roomId, ts)
            g.addEdge(eventId * 2, eventAlgo.createEdge(targetId = slotId))
            g.addEdge(slotId, slotAlgo.createEdge(targetId = eventId * 2))
          }
        }
      }
    }
    println("Reading is done")
    g
  }

  def size = eventsNumber //number of vertices

  def maxUtility = rooms * timeSlots * eventsNumber + util

  override def toString = "Events" + eventsNumber + "_timeSlots" + timeSlots + "_Rooms" + rooms

  /*
   * Returns the number of common professors and students for all pairs of events (lectures).
   */
  def computeCommonPeopleForEvents(allEvents: List[Int]): Array[scala.collection.mutable.Map[Int, (Int, Int)]] = {
    val bufferedSource = io.Source.fromFile(commonPeopleFile)

    var commonPeople = new Array[scala.collection.mutable.Map[Int, (Int, Int)]](eventsNumber + 1)

    for (eventId <- (1 to eventsNumber)) {
      commonPeople(eventId) = scala.collection.mutable.Map.empty[Int, (Int, Int)]
    }
    for (line <- bufferedSource.getLines) {
      val Array(ev1, ev2, stud, prof) = line.split(",").map(_.trim).map(_.toInt)
      commonPeople(ev1.toInt) += ((ev2 * 2, (stud, prof)))
    }
    bufferedSource.close

    commonPeople
  }

  /*
   * Returns the edges between events and slots.
   */
  def computeEdges(
    allEvents: List[Int],
    allSlots: List[Int],
    roomSize: Array[Int],
    lectureSize: Array[Int]): (Array[Set[Int]], Array[Set[Int]]) = {

    val eventToRoomEdges = Array.fill[Set[Int]](eventsNumber + 1)(Set.empty[Int])
    val roomToEventEdges = Array.fill[Set[Int]](rooms + 1)(Set.empty[Int])

    for (eventId <- 1 to eventsNumber) {
      for (roomId <- 1 to rooms) {
        val thisRoomSize = roomSize(roomId)
        val thisLectureSize = lectureSize(eventId)

        if ((thisLectureSize > thisRoomSize * minOccupationRate)
          && (thisLectureSize <= thisRoomSize)) {
          eventToRoomEdges(eventId) = eventToRoomEdges(eventId) + roomId
          roomToEventEdges(roomId) = roomToEventEdges(roomId) + eventId
        }
      }
    }

    (eventToRoomEdges, roomToEventEdges)
  }

  /*
   * Returns the number of students for each event (lecture).
   */
  def computeLectureSize(): Array[Int] = {
    val bufferedSource = io.Source.fromFile(lectureSizeFile)

    val lectureSize = new Array[Int](eventsNumber + 1)

    for (line <- bufferedSource.getLines) {
      val Array(ev1, stud, prof) = line.split(",").map(_.trim).map(_.toInt)
      lectureSize(ev1) = stud
    }
    bufferedSource.close

    lectureSize
  }

  /*
   * Returns the capacity for each room.
   */
  def computeRoomSize(): Array[Int] = {
    val bufferedSource = io.Source.fromFile(roomsFile)

    val roomSize = new Array[Int](rooms + 1)

    println("Room size")

    for (line <- bufferedSource.getLines) {
      val Array(room, size, roomType) = line.split(",").map(_.trim)
      roomSize(room.toInt) = size.toInt
    }
    bufferedSource.close

    roomSize
  }

  /*
   * The following three functions provide the transformations from slotId to roomId and timeSlotId.
   */
  def getSlotId(roomId: Int, timeSlotId: Int) = (roomId * 100 + timeSlotId) * 2 + 1

  def getRoomId(id: Int) = (id / 2) / 100

  def timeSlotId(id: Int) = (id / 2) % 100

  def getRandomFromDomain(domain: Set[Int]): Int = domain.toVector(Random.nextInt(domain.size))

}
