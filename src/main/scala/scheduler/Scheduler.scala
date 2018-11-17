package scheduler

import scheduler.genetic.GeneticAlgorithm

/** Scheduler
  *
  * Created by Ajay Gupta on 17/11/18.
  */
object Scheduler {

  def main(args: Array[String]): Unit = {
    // Initializing Problem Parameters
    val initializedTimeTable = TimeTable.initialize

    // Initializing Genetic Algorithm Parameters
    val populationSize = 100
    val mutationRate = 0.01
    val crossoverRate = 0.9
    val elitismCount = 2
    val tournamentSize = 5
    val geneticAlgorithm = new GeneticAlgorithm(populationSize, mutationRate, crossoverRate, elitismCount, tournamentSize)

    // Initialize population
    val initPopulation = geneticAlgorithm.initPopulation(initializedTimeTable)

    // Evaluate population
    geneticAlgorithm.evalPopulation(initPopulation, initializedTimeTable)

    var generation = 1
    while (!geneticAlgorithm.isTerminationConditionMet(generation, 1000) && !geneticAlgorithm.isTerminationConditionMet(initPopulation)) {
      println(s"Generation: $generation - Best fitness: ${initPopulation.getFittest(0).fitness}")

      //  Apply Crossover
      val crossoverPopulation = geneticAlgorithm.crossoverPopulation(initPopulation)

      // Apply Mutation
      val mutationPopulation = geneticAlgorithm.mutatePopulation(crossoverPopulation, initializedTimeTable)

      // Evaluate Population
      geneticAlgorithm.evalPopulation(mutationPopulation, initializedTimeTable)

      // Incrementing the generation
      generation += 1
    }

    // Print fitness
    initializedTimeTable.createCourses(initPopulation.getFittest(0))
    System.out.println()
    System.out.println("Solution found in " + generation + " generations")
    System.out.println("Final solution fitness: " + initPopulation.getFittest(0).fitness)
    System.out.println("Clashes: " + initializedTimeTable.calculateClashes)
  }
}
