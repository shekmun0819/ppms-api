package com.example.ppms.scheduler;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DetailedEvaluator extends SchedulingPenaltyEvaluator {
  private StringBuilder log;
  
  public DetailedEvaluator(List<File> files) throws IOException {
    super(files);
  }
  
  public int evaluate(int[] chromosome) {
    this.log = new StringBuilder();
    return super.evaluate(chromosome);
  }
  
  protected void violateHardConstraint(int constraintNumber, int slotIndex, int presentationIndex, String description) {
    this.penalty += this.PENALTY_HC[constraintNumber - 1];
    this.isFeasible = false;
    this.log.append("+").append(this.PENALTY_HC[constraintNumber - 1]).append(" pts: HC0").append(constraintNumber)
      .append(": ");
    if (slotIndex >= 0)
      this.log.append("Slot ").append(slotIndex + 1); 
    this.log.append("P").append(presentationIndex + 1).append(": ").append(description).append("\n");
  }
  
  protected void evaluateSoftConstraint1(int numConsecutive, int staffIndex, int slotIndex, int presentationIndex) {
    int penaltyIncrement = this.PENALTY_SC[0] * Math.abs(numConsecutive - this.staffConsecPresentPref[staffIndex]);
    this.penalty += penaltyIncrement;
    if (penaltyIncrement > 0)
      this.log.append("+").append(penaltyIncrement).append(" pts: SC01: Staff S").append(staffIndex + 1)
        .append(" Slot ").append(slotIndex + 1).append(" P").append(presentationIndex + 1).append(": ")
        .append(numConsecutive).append(" consecutive presentations\n"); 
  }
  
  protected void evaluateSoftConstraint2(int numDay, int staffIndex) {
    if (numDay > this.staffNumDayPref[staffIndex]) {
      this.penalty += this.PENALTY_SC[1] * (numDay - this.staffNumDayPref[staffIndex]);
      this.log.append("+").append(this.PENALTY_SC[1] * (numDay - this.staffNumDayPref[staffIndex])).append(" pts: SC02: Staff S")
        .append(staffIndex + 1).append(": ").append(numDay).append(" days of presentations\n");
    } 
  }
  
  protected void violateSoftConstraint3(int staffIndex, int slotIndex, int presentationIndex, int lastSlotIndex, int lastPresentationNumber) {
    this.penalty += this.PENALTY_SC[2];
    this.log.append("+").append(this.PENALTY_SC[2]).append(" pts: SC03: Staff S").append(staffIndex + 1).append(" Slot ")
      .append(slotIndex + 1).append(" (P").append(presentationIndex + 1).append(") with Slot ")
      .append(lastSlotIndex + 1).append(" (P").append(lastPresentationNumber).append(") different venue\n");
  }
  
  public String printChromosome(int[] chromosome) {
    StringBuilder result = new StringBuilder();
    result.append("-");
    for (int i = 0; i < 15; i++)
      result.append("-----------"); 
    result.append("\n");
    for (int rowIndex = 0; rowIndex < 5 * this.numRoom; rowIndex++) {
      StringBuilder slotRow = new StringBuilder();
      StringBuilder presentationRow = new StringBuilder();
      StringBuilder staff1Row = new StringBuilder();
      StringBuilder staff2Row = new StringBuilder();
      StringBuilder staff3Row = new StringBuilder();
      slotRow.append("|");
      presentationRow.append("|");
      staff1Row.append("|");
      staff2Row.append("|");
      staff3Row.append("|");
      for (int columnIndex = 0; columnIndex < 15; columnIndex++) {
        int slotIndex = rowIndex * 15 + columnIndex;
        int presentationIndex = chromosome[slotIndex] - 1;
        slotRow.append(String.format(" Slot %-3s |", new Object[] { Integer.valueOf(slotIndex + 1) }));
        if (presentationIndex >= 0) {
          presentationRow.append(String.format("   P%-3s   |", new Object[] { Integer.valueOf(chromosome[slotIndex]) }));
          staff1Row.append(String.format("   S%-3s   |", new Object[] { Integer.valueOf(this.audience[presentationIndex][0]) }));
          staff2Row.append(String.format("   S%-3s   |", new Object[] { Integer.valueOf(this.audience[presentationIndex][1]) }));
          staff3Row.append(String.format("   S%-3s   |", new Object[] { Integer.valueOf(this.audience[presentationIndex][2]) }));
        } else {
          presentationRow.append("          |");
          staff1Row.append("          |");
          staff2Row.append("          |");
          staff3Row.append("          |");
        } 
      } 
      result.append(slotRow.toString()).append("\n");
      result.append(presentationRow.toString()).append("\n");
      result.append(staff1Row.toString()).append("\n");
      result.append(staff2Row.toString()).append("\n");
      result.append(staff3Row.toString()).append("\n");
      result.append("-");
      for (int j = 0; j < 15; j++)
        result.append("-----------"); 
      result.append("\n");
    } 
    return result.toString();
  }
  
  public String getLogText() {
    return this.log.toString();
  }
}
