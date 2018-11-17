package scheduler.genetic

import scheduler.TimeTable

class GeneticAlgorithm(populationSize: Int,
                       mutationRate: Double,
                       crossoverRate: Double,
                       elitismCount: Int,
                       tournamentSize: Int) {
  /** Initialize population
    *
    * @param timeTable Time Table
    * @return population Generate Initial Population
    */
  def initPopulation(timeTable: TimeTable): Population = {
    Population.createPopulationFromTimeTable(populationSize, timeTable)
  }

  /** Evaluate Population
    *
    * @param population Population
    * @param timeTable  Time Table
    */
  def evalPopulation(population: Population, timeTable: TimeTable): Unit = {
    val populationFitnessList = population.individualArray.map(individual =>
      GeneticAlgorithm.calculateFitness(individual, timeTable)
    )
    val totalPopulationFitness = populationFitnessList.sum
    population.populationFitness = totalPopulationFitness
  }

  /** Check if population has met termination condition
    *
    * @param generationsCount Number of generations passed
    * @param maxGenerations   Number of generations to terminate after
    * @return boolean True if termination condition met, otherwise, false
    */
  def isTerminationConditionMet(generationsCount: Int, maxGenerations: Int): Boolean = {
    generationsCount > maxGenerations
  }

  /** Check if population has met termination condition
    *
    * @param population Population
    * @return boolean True if termination condition met, otherwise, false
    */
  def isTerminationConditionMet(population: Population): Boolean = {
    population.getFittest(0).fitness == 1.0
  }


  /** Selects parent for crossover using tournament selection
    *
    * Tournament selection works by choosing N random individuals, and then choosing the best of those.
    *
    * @param population Population
    * @return The individual selected as a parent
    */
  def selectParent(population: Population): Individual = {
    // Create tournament
    val tournament = new Population(tournamentSize)
    // Add random individuals to the tournament
    population.shuffle()

    var i = 0
    while (i < tournamentSize) {
      val tournamentIndividual = population.getIndividual(i)
      tournament.setIndividual(i, tournamentIndividual)
      i += 1
    }
    // Return the best
    tournament.getFittest(0)
  }


  /** Apply crossover to population
    *
    * @param population Population to apply crossover to
    * @return New Crossover Population
    */
  def crossoverPopulation(population: Population): Population = {
    // Create new population
    val newPopulation = new Population(population.individualArray.length)
    // Loop over current population by fitness
    var populationIndex = 0
    while (populationIndex < population.individualArray.length) {
      val parent1 = population.getFittest(populationIndex)
      // Apply crossover to this individual
      if (crossoverRate > Math.random && populationIndex >= elitismCount) {
        // Initialize offspring
        val offspring = new Individual(parent1.chromosome.length)

        // Find second parent
        val parent2 = selectParent(population)

        // Loop over genome
        var geneIndex = 0
        while (geneIndex < parent1.chromosome.length) {
          // Use half of parent1's genes and half of parent2's genes
          if (0.5 > Math.random) {
            offspring.setGene(geneIndex, parent1.getGene(geneIndex))
          }
          else {
            offspring.setGene(geneIndex, parent2.getGene(geneIndex))
          }
          geneIndex += 1
        }
        // Add offspring to new population
        newPopulation.setIndividual(populationIndex, offspring)
      }
      else {
        // Add individual to new population without applying crossover
        newPopulation.setIndividual(populationIndex, parent1)
      }
      populationIndex += 1
    }
    newPopulation
  }

  /** Apply mutation to population
    *
    * @param population Population
    * @param timeTable  timeTable
    * @return Mutated Population
    */
  def mutatePopulation(population: Population, timeTable: TimeTable): Population = {
    // Initialize new population
    val newPopulation = new Population(this.populationSize)
    // Loop over current population by fitness
    var populationIndex = 0
    while (populationIndex < population.individualArray.length) {
      val individual = population.getFittest(populationIndex)
      // Create random individual to swap genes with
      val randomIndividual = Individual.getIndividualFromTimeTable(timeTable)
      // Loop over individual's genes
      var geneIndex = 0
      while (geneIndex < individual.chromosomeLength) {
        // Skip mutation if this is an elite individual
        if (populationIndex > this.elitismCount) {
          // Does this gene need mutation?
          if (this.mutationRate > Math.random) {
            // Swap for new gene
            individual.setGene(geneIndex, randomIndividual.getGene(geneIndex))
          }
        }
        geneIndex += 1
      }
      // Add individual to population
      newPopulation.setIndividual(populationIndex, individual)
      populationIndex += 1
    }
    // Return mutated population
    newPopulation
  }
}

object GeneticAlgorithm {

  /** Calculate individual's fitness value
    *
    * @param individual Individual
    * @param timetable  Time Table
    * @return fitness Fitness
    */
  def calculateFitness(individual: Individual, timetable: TimeTable): Double = {
    // Create new timetable object to use
    // Cloned from an existing timetable
    val threadTimetable = timetable.copy()
    threadTimetable.createCourses(individual)

    // Calculate Fitness
    val clashes = threadTimetable.calculateClashes
    val fitness = 1 / (clashes + 1).toDouble
    individual.fitness = fitness
    fitness
  }
}