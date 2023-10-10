package com.example.ppms.scheduler;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class SchedulingPenaltyEvaluator {
  protected final int SLOTS_PER_DAY = 15;
  
  protected final int NUM_DAY = 5;
  
  protected final int NUM_AUDIENCE_PER_PRESENTATION = 3;
  
  protected final int DISCONTINOUS_TIME_SLOT_INDEX = 8;
  
  protected final int[] PENALTY_HC = new int[] { 1000, 1000, 1000, 1000, 1000 };
  
  protected final int[] PENALTY_SC = new int[] { 10, 10, 10 };
  
  private List<File> files;
  
  protected int numPresentation;
  
  protected int numStaff;
  
  protected int numRoom;
  
  protected int totalNumSlot;
  
  protected int[][] audience;
  
  private boolean[] isRoomAvailable;
  
  private boolean[][] isStaffAvailable;
  
  private boolean[][] isStudentAvailable;
  
  protected int[] staffConsecPresentPref;
  
  protected int[] staffNumDayPref;
  
  private boolean[] staffRoomChangePref;
  
  private boolean[] isPresentationScheduled;
  
  private int[] staffLastSlotIndex;
  
  private int[] staffNumConsecutive;
  
  private int[] staffLastDayNumber;
  
  private int[] staffNumDay;
  
  protected int penalty;
  
  protected boolean isFeasible;
  
  public SchedulingPenaltyEvaluator(List<File> files) throws IOException {
    this.files = files;
    readFiles(files);
  }
  
  private void readFiles(List<File> files) throws IOException {
    try (Scanner scanner = (new Scanner(files.get(6))).useDelimiter("\\s+|P|,S|=")) {
      scanner.next();
      this.numPresentation = scanner.nextInt();
      this.audience = new int[this.numPresentation][3];
      scanner.next();
      scanner.nextLine();
      while (true) {
        int presentationNumber = scanner.nextInt();
        for (int roleIndex = 0; roleIndex < 3; roleIndex++)
          this.audience[presentationNumber - 1][roleIndex] = scanner.nextInt(); 
        if (scanner.hasNext()) {
          scanner.next();
          continue;
        } 
        break;
      } 
    } catch (IOException e) {
      throw e;
    }

    try (Scanner scanner = (new Scanner(files.get(0))).useDelimiter("\\s+|R(oom)?|,|=")) {
      scanner.next();
      this.numRoom = scanner.nextInt();
      this.totalNumSlot = 75 * this.numRoom;
      this.isRoomAvailable = new boolean[this.totalNumSlot];
      for (int slotIndex = 0; slotIndex < this.isRoomAvailable.length; slotIndex++)
        this.isRoomAvailable[slotIndex] = true; 
      scanner.next();
      scanner.nextLine();
      while (scanner.hasNext()) {
        scanner.nextInt();
        String token;
        while (scanner.hasNext() && !(token = scanner.next()).equals(""))
          this.isRoomAvailable[Integer.parseInt(token) - 1] = false; 
      } 
    } catch (IOException e) {
      throw e;
    }

    try (Scanner scanner = (new Scanner(files.get(1))).useDelimiter("\\s+|S|,|=")) {
      scanner.next();
      this.numStaff = scanner.nextInt();
      this.isStaffAvailable = new boolean[this.numStaff][this.totalNumSlot];
      int staffIndex;
      for (staffIndex = 0; staffIndex < this.numStaff; staffIndex++) {
        for (int slotIndex = 0; slotIndex < this.totalNumSlot; slotIndex++)
          this.isStaffAvailable[staffIndex][slotIndex] = true; 
      } 
      scanner.next();
      scanner.nextLine();
      while (scanner.hasNext()) {
        staffIndex = scanner.nextInt() - 1;
        String token;
        while (scanner.hasNext() && !(token = scanner.next()).equals(""))
          this.isStaffAvailable[staffIndex][Integer.parseInt(token) - 1] = false; 
      } 
    } catch (IOException e) {
      throw e;
    }

    try (Scanner scanner = (new Scanner(files.get(2))).useDelimiter("\\s+|P|,|=")) {
      scanner.nextLine();
      scanner.nextLine();
      this.isStudentAvailable = new boolean[this.numPresentation][this.totalNumSlot];
      int studentIndex;
      for (studentIndex = 0; studentIndex < this.numPresentation; studentIndex++) {
        for (int slotIndex = 0; slotIndex < this.totalNumSlot; slotIndex++)
          this.isStudentAvailable[studentIndex][slotIndex] = true; 
      } 
      while (scanner.hasNext()) {
        studentIndex = scanner.nextInt() - 1;
        String token;
        while (scanner.hasNext() && !(token = scanner.next()).equals(""))
          this.isStudentAvailable[studentIndex][Integer.parseInt(token) - 1] = false; 
      } 
    } catch (IOException e) {
      throw e;
    }

    try (Scanner scanner = (new Scanner(files.get(3))).useDelimiter("\\s+|S|,|=")) {
      scanner.nextLine();
      scanner.nextLine();
      this.staffConsecPresentPref = new int[this.numStaff];
      while (true) {
        int staffIndex = scanner.nextInt() - 1;
        this.staffConsecPresentPref[staffIndex] = scanner.nextInt();
        if (scanner.hasNext()) {
          scanner.next();
          continue;
        } 
        break;
      } 
    } catch (IOException e) {
      throw e;
    }

    try (Scanner scanner = (new Scanner(files.get(4))).useDelimiter("\\s+|S|,|=")) {
      scanner.nextLine();
      scanner.nextLine();
      this.staffNumDayPref = new int[this.numStaff];
      while (true) {
        int staffIndex = scanner.nextInt() - 1;
        this.staffNumDayPref[staffIndex] = scanner.nextInt();
        if (scanner.hasNext()) {
          scanner.next();
          continue;
        } 
        break;
      } 
    } catch (IOException e) {
      throw e;
    }

    try (Scanner scanner = (new Scanner(files.get(5))).useDelimiter("\\s+|S|,|=")) {
      scanner.nextLine();
      scanner.nextLine();
      this.staffRoomChangePref = new boolean[this.numStaff];
      while (true) {
        int staffIndex = scanner.nextInt() - 1;
        this.staffRoomChangePref[staffIndex] = (Character.toLowerCase(scanner.next().charAt(0)) == 'y');
        if (scanner.hasNext()) {
          scanner.next();
          continue;
        } 
        break;
      } 
    } catch (IOException e) {
      throw e;
    } 
  }
  
  public int evaluate(int[] chromosome) {
    this.penalty = 0;
    this.isFeasible = true;
    this.isPresentationScheduled = new boolean[this.numPresentation];

    for (int presentationIndex = 0; presentationIndex < this.numPresentation; presentationIndex++)
      this.isPresentationScheduled[presentationIndex] = false;
    
    this.staffLastSlotIndex = new int[this.numStaff];
    this.staffNumConsecutive = new int[this.numStaff];
    this.staffLastDayNumber = new int[this.numStaff];
    this.staffNumDay = new int[this.numStaff];

    for (int staffIndex = 0; staffIndex < this.numStaff; staffIndex++) {
      this.staffLastSlotIndex[staffIndex] = -2;
      this.staffNumConsecutive[staffIndex] = 0;
      this.staffLastDayNumber[staffIndex] = -1;
      this.staffNumDay[staffIndex] = 0;
    }

    int dayNumber = 1;
    int dayTimeSlotIndex = 0;
    while (dayTimeSlotIndex < this.totalNumSlot) {
      for (int roomIndex = 0; roomIndex < this.numRoom; roomIndex++) {
        int slotIndex = dayTimeSlotIndex + roomIndex * 15;
        int k = chromosome[slotIndex] - 1;
        if (k >= 0) {
          if (this.isPresentationScheduled[k]) {
            violateHardConstraint(1, slotIndex, k, "Duplicate presentation");
          } else {
            this.isPresentationScheduled[k] = true;
          } 
          if (!this.isRoomAvailable[slotIndex])
            violateHardConstraint(3, slotIndex, k, "Room unavailable"); 
          if (!this.isStudentAvailable[k][slotIndex])
            violateHardConstraint(5, slotIndex, k, "Student P" + (k + 1) + " unavailable"); 
          for (int staffNumber : this.audience[k]) {
            int m = staffNumber - 1;
            int staffLastDayTimeSlotIndex = this.staffLastSlotIndex[m] % 15 + (dayNumber - 1) * 15 * this.numRoom;
            if (!this.isStaffAvailable[staffNumber - 1][slotIndex])
              violateHardConstraint(4, slotIndex, k, "Staff S" + staffNumber + " unavailable"); 
            if (staffLastDayTimeSlotIndex == dayTimeSlotIndex) {
              violateHardConstraint(2, slotIndex, k, "Staff S" + staffNumber + " concurrent presentation with Slot " + (this.staffLastSlotIndex[m] + 1) + " (P" + chromosome[this.staffLastSlotIndex[m]] + ")");
            } else if (dayTimeSlotIndex - staffLastDayTimeSlotIndex == 1 && dayTimeSlotIndex % 15 != 8) {
              this.staffNumConsecutive[m] = this.staffNumConsecutive[m] + 1;
              if (this.staffLastSlotIndex[m] / 15 % this.numRoom != slotIndex / 15 % this.numRoom && this.staffRoomChangePref[m])
                violateSoftConstraint3(m, slotIndex, k, this.staffLastSlotIndex[m], chromosome[this.staffLastSlotIndex[m]]); 
            } else {
              if (this.staffNumConsecutive[m] > 0)
                evaluateSoftConstraint1(this.staffNumConsecutive[m], m, slotIndex, k); 
              this.staffNumConsecutive[m] = 1;
            } 
            if (dayNumber > this.staffLastDayNumber[m]) {
              this.staffNumDay[m] = this.staffNumDay[m] + 1;
              this.staffLastDayNumber[m] = dayNumber;
            } 
            this.staffLastSlotIndex[m] = slotIndex;
          } 
        } 
      }

      dayTimeSlotIndex++;
      if (dayTimeSlotIndex % 15 == 0) {
        dayNumber++;
        dayTimeSlotIndex += 15 * (this.numRoom - 1);
      } 
    }

    for (int j = 0; j < this.numPresentation; j++) {
      if (!this.isPresentationScheduled[j])
        violateHardConstraint(1, -1, j, "Presentation is not scheduled"); 
    }

    for (int i = 0; i < this.numStaff; i++) {
      if (this.staffNumConsecutive[i] > 0)
        evaluateSoftConstraint1(this.staffNumConsecutive[i], i, this.staffLastSlotIndex[i], chromosome[this.staffLastSlotIndex[i]] - 1); 
      evaluateSoftConstraint2(this.staffNumDay[i], i);
    }
    
    return this.penalty;
  }
  
  protected void violateHardConstraint(int constraintNumber, int slotIndex, int presentationIndex, String description) {
    this.penalty += this.PENALTY_HC[constraintNumber - 1];
    this.isFeasible = false;
  }
  
  protected void evaluateSoftConstraint1(int numConsecutive, int staffIndex, int slotIndex, int presentationIndex) {
    this.penalty += this.PENALTY_SC[0] * Math.abs(numConsecutive - this.staffConsecPresentPref[staffIndex]);
  }
  
  protected void evaluateSoftConstraint2(int numDay, int staffIndex) {
    if (numDay > this.staffNumDayPref[staffIndex])
      this.penalty += this.PENALTY_SC[1] * (numDay - this.staffNumDayPref[staffIndex]); 
  }
  
  protected void violateSoftConstraint3(int staffIndex, int slotIndex, int presentationIndex, int lastSlotIndex, int lastPresentationNumber) {
    this.penalty += this.PENALTY_SC[2];
  }
  
  public List<File> getFile() {
    return this.files;
  }
  
  public int getNumPresentation() {
    return this.numPresentation;
  }
  
  public int getTotalNumSlot() {
    return this.totalNumSlot;
  }
  
  public boolean getIsFeasible() {
    return this.isFeasible;
  }
}
