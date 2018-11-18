package scheduler.entity

import scheduler.RandomNumberGenerator

/** Module
  *
  * Created by Ajay Gupta on 17/11/18.
  */
case class Module(moduleId: Int, moduleCode: String, module: String, professorIdList: List[Int]) {

  /** Get Random Professor Id
    *
    * @return Random Professor Id
    */
  def getRandomProfessorId: Int = {
    val randomProfessorIndex = (professorIdList.length * RandomNumberGenerator.get).toInt
    professorIdList(randomProfessorIndex)
  }
}
