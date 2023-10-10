package com.example.ppms.scheduler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.filechooser.FileSystemView;

public class GeneticAlgorithm {
  private static Random randomiser = new Random();
  
  private int[][] population;
  
  private int[] penaltyScores;
  
  private boolean[] feasibilities;
  
  private int bestChromosomeIndex;
  
  private int totalNumSlot;
  
  private int numPresentation;
  
  private SchedulingPenaltyEvaluator evaluator;
  
  public GeneticAlgorithm(SchedulingPenaltyEvaluator evaluator) {
    this.evaluator = evaluator;
    this.totalNumSlot = evaluator.getTotalNumSlot();
    this.numPresentation = evaluator.getNumPresentation();
  }
  
  public int[] generateRandomChromosome() {
    int[] chromosome = new int[this.totalNumSlot];
    List<Integer> slotIndices = new LinkedList<>();
    for (int slotIndex = 0; slotIndex < this.totalNumSlot; slotIndex++)
      slotIndices.add(Integer.valueOf(slotIndex)); 
    
    for (int presentationNumber = 1; presentationNumber <= this.numPresentation; presentationNumber++)
      chromosome[((Integer)slotIndices.remove(randomiser.nextInt(slotIndices.size()))).intValue()] = presentationNumber; 
    
    return chromosome;
  }
  
  private void generateInitialPopulation(int populationSize) {
    this.population = new int[populationSize][this.totalNumSlot];
    this.penaltyScores = new int[populationSize];
    this.feasibilities = new boolean[populationSize];
    this.bestChromosomeIndex = 0;
    
    for (int chromosomeIndex = 0; chromosomeIndex < populationSize; chromosomeIndex++) {
      this.population[chromosomeIndex] = generateRandomChromosome();
      this.penaltyScores[chromosomeIndex] = this.evaluator.evaluate(this.population[chromosomeIndex]);
      this.feasibilities[chromosomeIndex] = this.evaluator.getIsFeasible();
      
      if (this.penaltyScores[chromosomeIndex] < this.penaltyScores[this.bestChromosomeIndex] && (!this.feasibilities[this.bestChromosomeIndex] || this.feasibilities[chromosomeIndex]))
        this.bestChromosomeIndex = chromosomeIndex; 
    } 
  }
  
  private int[] selectParents() {
    int[] parentIndices = { this.bestChromosomeIndex, 0 };
    while (true) {
      parentIndices[1] = randomiser.nextInt(this.population.length);
      if (parentIndices[1] != parentIndices[0])
        return parentIndices; 
    } 
  }
  
  private int[][] orderCrossOver(int minCutLength, int maxCutLength, int[] parent1, int[] parent2) {
    int[][] children = new int[2][this.totalNumSlot];
    int cutLength = randomiser.nextInt(maxCutLength - minCutLength + 1) + minCutLength;
    int firstCutPoint = randomiser.nextInt(this.totalNumSlot - maxCutLength + 1);
    int secondCutPoint = firstCutPoint + cutLength;
    List<Integer> parentSeq1 = new LinkedList<>();
    List<Integer> parentSeq2 = new LinkedList<>();
    int geneIndex;
    for (geneIndex = secondCutPoint; geneIndex < this.totalNumSlot; geneIndex++) {
      parentSeq1.add(Integer.valueOf(parent1[geneIndex]));
      parentSeq2.add(Integer.valueOf(parent2[geneIndex]));
    } 
    for (geneIndex = 0; geneIndex < secondCutPoint; geneIndex++) {
      parentSeq1.add(Integer.valueOf(parent1[geneIndex]));
      parentSeq2.add(Integer.valueOf(parent2[geneIndex]));
    } 
    for (geneIndex = firstCutPoint; geneIndex < secondCutPoint; geneIndex++) {
      parentSeq2.remove(Integer.valueOf(parent1[geneIndex]));
      parentSeq1.remove(Integer.valueOf(parent2[geneIndex]));
    } 
    for (geneIndex = 0; geneIndex < firstCutPoint; geneIndex++) {
      children[0][geneIndex] = ((Integer)parentSeq2.get(geneIndex)).intValue();
      children[1][geneIndex] = ((Integer)parentSeq1.get(geneIndex)).intValue();
    } 
    for (geneIndex = firstCutPoint; geneIndex < secondCutPoint; geneIndex++) {
      children[0][geneIndex] = parent1[geneIndex];
      children[1][geneIndex] = parent2[geneIndex];
    } 
    for (geneIndex = secondCutPoint; geneIndex < this.totalNumSlot; geneIndex++) {
      children[0][geneIndex] = ((Integer)parentSeq2.get(geneIndex - cutLength)).intValue();
      children[1][geneIndex] = ((Integer)parentSeq1.get(geneIndex - cutLength)).intValue();
    } 
    return children;
  }
  
  private void mutate(int[] chromosome) {
    int index1 = randomiser.nextInt(chromosome.length);
    int index2 = randomiser.nextInt(chromosome.length);
    while (index2 == index1)
      index2 = randomiser.nextInt(chromosome.length); 
    int temp = chromosome[index1];
    chromosome[index1] = chromosome[index2];
    chromosome[index2] = temp;
  }
  
  private void replacePredecessor(int[] childChromosome) {
    int childPenaltyScore = this.evaluator.evaluate(childChromosome);
    boolean isChildFeasible = this.evaluator.getIsFeasible();
    int worstChromosomeIndex = 0;
    for (int chromosomeIndex = 0; chromosomeIndex < this.population.length; chromosomeIndex++) {
      if (this.penaltyScores[chromosomeIndex] == childPenaltyScore && 
        Arrays.equals(childChromosome, this.population[chromosomeIndex]))
        return; 
      if (this.penaltyScores[chromosomeIndex] > this.penaltyScores[worstChromosomeIndex] && (!this.feasibilities[chromosomeIndex] || this.feasibilities[worstChromosomeIndex]))
        worstChromosomeIndex = chromosomeIndex; 
    } 
    if (childPenaltyScore < this.penaltyScores[worstChromosomeIndex] && (!this.feasibilities[worstChromosomeIndex] || isChildFeasible)) {
      this.population[worstChromosomeIndex] = childChromosome;
      this.penaltyScores[worstChromosomeIndex] = childPenaltyScore;
      this.feasibilities[worstChromosomeIndex] = isChildFeasible;
    } 
  }
  
  public int[] run(int populationSize, int generationLimit, double crossoverProb, double mutationProb, int crossoverMinCutLength, int crossoverMaxCutLength) {
    int time = 0;
    generateInitialPopulation(populationSize);
    while (time < generationLimit && this.penaltyScores[this.bestChromosomeIndex] > 0) {
      int children[][], parentIndices[] = selectParents();
      if (randomiser.nextDouble() < crossoverProb) {
        children = orderCrossOver(crossoverMinCutLength, crossoverMaxCutLength, this.population[parentIndices[0]], this.population[parentIndices[1]]);
        while (randomiser.nextDouble() < mutationProb)
          mutate(children[0]); 
        while (randomiser.nextDouble() < mutationProb)
          mutate(children[1]); 
      } else {
        children = new int[2][this.totalNumSlot];
        for (int slotIndex = 0; slotIndex < this.totalNumSlot; slotIndex++) {
          children[0][slotIndex] = this.population[parentIndices[0]][slotIndex];
          children[1][slotIndex] = this.population[parentIndices[1]][slotIndex];
        } 
        do {
          mutate(children[0]);
        } while (randomiser.nextDouble() < mutationProb);
        do {
          mutate(children[1]);
        } while (randomiser.nextDouble() < mutationProb);
      } 
      replacePredecessor(children[0]);
      replacePredecessor(children[1]);
      time++;
    } 
    return this.population[this.bestChromosomeIndex];
  }
  
  public File outputSolution(int[] chromosome, List<File> files) throws IOException {
    File solutionFile = new File(files.get(0).getParent() + "\\solution.csv");
		FileOutputStream solutionFos = new FileOutputStream(solutionFile);
		BufferedWriter solutionBw = new BufferedWriter(new OutputStreamWriter(solutionFos));

    if (chromosome[0] > 0) {
        solutionBw.write(Integer.toString(chromosome[0]));
      } 
    else {
      solutionBw.write("null");
    } 
      
    for (int slotIndex = 1; slotIndex < this.totalNumSlot; slotIndex++) {
      if (chromosome[slotIndex] > 0) {
        solutionBw.write("," + chromosome[slotIndex]);
      } else {
        solutionBw.write(",null");
      } 
    }

    solutionBw.close();
		solutionBw.close();

    return solutionFile;
  }
}
