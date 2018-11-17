package scheduler.entity

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
    val randomProfessorIndex = (professorIdList.length * Math.random).toInt
    professorIdList(randomProfessorIndex)
  }
}
