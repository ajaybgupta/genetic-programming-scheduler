package scheduler.genetic

import scheduler.TimeTable

case class Individual(chromosomeLength: Int) {
  val chromosome: Array[Int] = new Array[Int](chromosomeLength)
  var fitness: Double = -1

  def this(chromosome: Array[Int]) = {
    this(chromosome.length)
    Array.copy(chromosome, 0, this.chromosome, 0, chromosome.length)
  }

  /** Set gene at offset
    *
    * @param gene   Gene
    * @param offset Offset
    */
  def setGene(offset: Int, gene: Int): Unit = {
    chromosome(offset) = gene
  }

  /** Get gene at offset
    *
    * @param offset Offset
    * @return gene Gene
    */
  def getGene(offset: Int): Int = {
    chromosome(offset)
  }
}

object Individual {
  /** Get Individual from Time Table
    *
    * @param timeTable Time Table
    * @return Individual
    */
  def getIndividualFromTimeTable(timeTable: TimeTable): Individual = {
    // Loop through groups
    val newChromosomeList = timeTable.groupMap.values.toList.flatMap { group =>
      // Loop through modules
      group.moduleIdList.flatMap { moduleId =>
        // Add random time
        val timeSlotId = timeTable.getRandomTimeSlot.timeSlotId

        // Add random room
        val roomId = timeTable.getRandomRoom.roomId

        // Add random professor
        val moduleMap = timeTable.moduleMap
        val moduleOption = moduleMap.get(moduleId.toString)
        val module = moduleOption.get
        val professorId = module.getRandomProfessorId
        Array(timeSlotId, roomId, professorId)
      }
    }

    // Merge Chromosome as per the requirement
    new Individual(newChromosomeList.toArray)
  }

}
