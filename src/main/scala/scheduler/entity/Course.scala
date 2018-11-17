package scheduler.entity

/** Course
  * Container for course, group, module, professor, time slot, and room IDs
  *
  * Created by Ajay Gupta on 17/11/18.
  */
case class Course(courseId: Int, groupId: Int, moduleId: Int, professorId: Int, timeSlotId: Int, roomId: Int)