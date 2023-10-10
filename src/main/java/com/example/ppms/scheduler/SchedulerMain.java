package com.example.ppms.scheduler;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SchedulerMain {
  private GeneticAlgorithm geneticAlgorithm = null;
  
  private SchedulingPenaltyEvaluator evaluator = null;
  
  //private DetailedEvaluator detailedEvaluator = null;
  
  private int[] solutionChromosome;

  private int populationSize = 8;

  private int numberGeneration = 1000000;

  private double crossoverProb = 0.9;

  private double mutationProb = 0.6;

  private int minCutLength = 30;

  private int maxCutLength = 290;

  public File run(List<File> files) throws IOException {

    if (files.size() == 7) {
      try {
        this.evaluator = new SchedulingPenaltyEvaluator(files);
        //this.detailedEvaluator = new DetailedEvaluator(directory);
        this.geneticAlgorithm = new GeneticAlgorithm(this.evaluator);
      } catch (IOException e) {
        throw e;
      } 
    }

    // Thread thread = new Thread(() -> {
    //   try {
    //     this.solutionChromosome = this.geneticAlgorithm.run(this.populationSize, this.numberGeneration, this.crossoverProb, this.mutationProb, this.minCutLength, this.maxCutLength);
    //     this.geneticAlgorithm.outputSolution(this.solutionChromosome);
    //     //int penalty = this.detailedEvaluator.evaluate(this.solutionChromosome);
    //   } catch (Exception e) {
    //     throw e;
    //   } 
    // });
    // thread.setDaemon(true);
    // thread.start();

    try {
      this.solutionChromosome = this.geneticAlgorithm.run(this.populationSize, this.numberGeneration, this.crossoverProb, this.mutationProb, this.minCutLength, this.maxCutLength);
      File solutionFile = this.geneticAlgorithm.outputSolution(this.solutionChromosome, files);
      return solutionFile;
    } catch (Exception e) {
      throw e;
    } 
  }
}
