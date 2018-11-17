package scheduler.genetic

import java.util.Random

import scheduler.TimeTable

/** Population
  *
  * Created by Ajay on 17/11/18.
  */
case class Population(populationSize: Int) {
  val individualArray: Array[Individual] = new Array[Individual](populationSize)
  var populationFitness: Double = -1

  /** Find fittest individual in the population
    *
    * @param offset Offset
    * @return individual Fittest individual at offset
    */
  def getFittest(offset: Int): Individual = {
    // Order population by fitness
    val sortedIndividualList = individualArray.sortBy { individual =>
      individual.fitness
    }
    val reverseSortedIndividualList = sortedIndividualList.reverse
    reverseSortedIndividualList(offset)
  }

  /** Set Individual
    *
    * @param offset     Offset
    * @param individual Individual
    */
  def setIndividual(offset: Int, individual: Individual): Unit = {
    individualArray(offset) = individual
  }

  /** Get Individual at offset
    *
    * @param offset Offset
    * @return Individual
    */
  def getIndividual(offset: Int): Individual = {
    individualArray(offset)
  }

  /** Shuffles the population in-place
    *
    * @return void
    */
  def shuffle(): Unit = {
    val random = new Random
    var i = individualArray.length - 1
    while (i > 0) {
      val index = random.nextInt(i + 1)
      val individual = individualArray(index)
      individualArray(index) = individualArray(i)
      individualArray(i) = individual
      i -= 1
    }
  }
}

object Population {
  /** Create Population From Time Table
    *
    * @param populationSize Population Size
    * @param timeTable      Time Table
    * @return Population
    */
  def createPopulationFromTimeTable(populationSize: Int, timeTable: TimeTable): Population = {
    val newPopulation = new Population(populationSize)
    Range(0, populationSize).foreach { index =>
      val individual = Individual.getIndividualFromTimeTable(timeTable)
      newPopulation.setIndividual(index, individual)
    }
    newPopulation
  }
}