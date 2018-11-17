package scheduler

import scheduler.entity._
import scheduler.genetic.Individual

/** Time Table
  * Timetable class to encapsulate all these objects into one single timetable object.
  * It is the most important class, as it’s the only class that understands how the different constraints are supposed to interact with one another.
  * The Timetable class also understands how to parse a chromosome and create a candidate Timetable to be evaluated and scored.
  *
  * Created by Ajay Gupta on 17/11/18.
  */
case class TimeTable(roomMap: Map[String, Room],
                     professorMap: Map[String, Professor],
                     moduleMap: Map[String, Module],
                     groupMap: Map[String, Group],
                     timeSlotMap: Map[String, TimeSlot]
                    ) {

  var courseList: List[Course] = List.empty

  /** Get Random Time Slot
    *
    * @return Random Time Slot
    */
  def getRandomTimeSlot: TimeSlot = {
    val timeSlotArray = timeSlotMap.values.toArray
    val timeSlot = timeSlotArray((timeSlotArray.length * Math.random).toInt)
    timeSlot
  }

  /** Get Random Room
    *
    * @return Random Room
    */
  def getRandomRoom: Room = {
    val roomsArray = roomMap.values.toArray
    val room = roomsArray((roomsArray.length * Math.random).toInt)
    room
  }

  /** Calculate the number of clashes between Classes generated by a chromosome.
    *
    * The most important method in this class; look at a candidate timetable and figure out how many constraints are violated.
    *
    * Running this method requires that createCourses has been run first.
    * The return value of this method is simply the number of constraint violations
    * (conflicting professors, timeslots, or rooms), and that return value is used by the GeneticAlgorithm.calculateFitness method.
    *
    * @return Calculate Clashes
    */
  def calculateClashes: Int = {
    // Calculating Clashes
    var clashes = 0
    for (courseA <- courseList) {

      // Check room capacity
      val courseARoom = roomMap(courseA.roomId.toString)
      val courseAGroup = groupMap(courseA.groupId.toString)
      val roomCapacity = courseARoom.capacity
      val groupSize = courseAGroup.groupSize

      if (roomCapacity < groupSize) {
        clashes += 1
      }

      // Check if room is available
      val roomTakenClashOption = courseList.find { courseB =>
        courseA.roomId == courseB.roomId && courseA.timeSlotId == courseB.timeSlotId && courseA.courseId != courseB.courseId
      }
      if (roomTakenClashOption.isDefined) {
        clashes += 1
      }

      // Check if professor is available
      val professorTakenClashOption = courseList.find { courseB =>
        courseA.professorId == courseB.professorId && courseA.timeSlotId == courseB.timeSlotId && courseA.courseId != courseB.courseId
      }
      if (professorTakenClashOption.isDefined) {
        clashes += 1
      }
    }
    clashes
  }

  /** Get number of Courses that need scheduling
    *
    * @return numCourses Number of Courses
    */
  def getNumCourses: Int = {
    if (courseList.nonEmpty) {
      // If we have count already we wont be needing to compute
      courseList.length
    } else {
      // If we do not have count we need to compute
      val groupModuleCountList = groupMap.values.map(x => x.moduleIdList.length)
      groupModuleCountList.sum
    }
  }

  /** Create Courses
    *
    * @param individual Individual
    */
  def createCourses(individual: Individual): Unit = {
    // Init classes
    val courseList = new Array[Course](getNumCourses)

    // Get individual's chromosome
    val chromosome = individual.chromosome
    var chromosomePos = 0
    var courseIndex = 0

    for (group <- groupMap.values.toList) {
      val moduleIdList = group.moduleIdList
      for (moduleId <- moduleIdList) {
        val timeSlotId = chromosome(chromosomePos)
        chromosomePos += 1
        val professorId = chromosome(chromosomePos)
        chromosomePos += 1
        val roomId = chromosome(chromosomePos)
        chromosomePos += 1
        // Adding to Course List
        courseList(courseIndex) = new Course(courseIndex, group.groupId, moduleId, professorId, timeSlotId, roomId)
        courseIndex += 1
      }
    }
    this.courseList = courseList.toList
  }
}

object TimeTable {
  def initialize: TimeTable = {
    //Room
    val roomMap = Map(
      "1" -> Room(1, "A1", 15),
      "2" -> Room(2, "B1", 30),
      "3" -> Room(4, "D1", 20),
      "4" -> Room(5, "F1", 25)
    )

    // Professor
    val professorMap = Map(
      "1" -> Professor(1, "Dr P Smith"),
      "2" -> Professor(2, "Mrs E Mitchell"),
      "3" -> Professor(3, "Dr R Williams"),
      "4" -> Professor(4, "Mr A Thompson")
    )

    // Module
    val moduleMap = Map(
      "1" -> Module(1, "cs1", "Computer Science", List(1, 2)),
      "2" -> Module(2, "en1", "English", List(1, 3)),
      "3" -> Module(3, "ma1", "Maths", List(1, 2)),
      "4" -> Module(4, "ph1", "Physics", List(3, 4)),
      "5" -> Module(5, "hi1", "History", List(4)),
      "6" -> Module(6, "dr1", "Drama", List(1, 4))
    )

    // Group
    val groupMap = Map(
      "1" -> Group(1, 10, List(1, 3, 4)),
      "2" -> Group(2, 30, List(2, 3, 5, 6)),
      "3" -> Group(3, 18, List(3, 4, 5)),
      "4" -> Group(4, 25, List(1, 4)),
      "5" -> Group(5, 20, List(2, 3, 5)),
      "6" -> Group(6, 22, List(1, 4, 5)),
      "7" -> Group(7, 16, List(1, 3)),
      "8" -> Group(8, 18, List(2, 6)),
      "9" -> Group(9, 24, List(1, 6)),
      "10" -> Group(10, 25, List(3, 4))
    )

    // Time Slot
    val timeSlotMap = Map(
      "1" -> TimeSlot(1, "Mon 9:00 - 11:00"),
      "2" -> TimeSlot(2, "Mon 11:00 - 13:00"),
      "3" -> TimeSlot(3, "Mon 13:00 - 15:00"),
      "4" -> TimeSlot(4, "Tue 9:00 - 11:00"),
      "5" -> TimeSlot(5, "Tue 11:00 - 13:00"),
      "6" -> TimeSlot(6, "Tue 13:00 - 15:00"),
      "7" -> TimeSlot(7, "Wed 9:00 - 11:00"),
      "8" -> TimeSlot(8, "Wed 11:00 - 13:00"),
      "9" -> TimeSlot(9, "Wed 13:00 - 15:00"),
      "10" -> TimeSlot(10, "Thu 9:00 - 11:00"),
      "11" -> TimeSlot(11, "Thu 11:00 - 13:00"),
      "12" -> TimeSlot(12, "Thu 13:00 - 15:00"),
      "13" -> TimeSlot(13, "Fri 9:00 - 11:00"),
      "14" -> TimeSlot(14, "Fri 11:00 - 13:00"),
      "15" -> TimeSlot(15, "Fri 13:00 - 15:00")
    )

    new TimeTable(roomMap, professorMap, moduleMap, groupMap, timeSlotMap)
  }
}
