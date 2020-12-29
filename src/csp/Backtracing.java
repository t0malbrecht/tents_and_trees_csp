package csp;

import tentsAndTrees.Cell;
import tentsAndTrees.Grid;

import java.util.*;

public class Backtracing extends AbstractCSP{
    private ArrayList<Cell> initialOpenCells;
    private ArrayList<Cell> currentOpenCells;
    private Assignment initialAssignment;
    int counter = 0;

    public Backtracing(Grid grid){
        this.initialOpenCells = grid.getOpenCells();
        this.currentOpenCells = initialOpenCells;
        initialAssignment = new Assignment(grid, new HashMap<>());
    }

    public void start(){
        Assignment result = chronologicalBacktracking(initialAssignment);
        if(result == null){
            System.out.println("No Solution found");
        }else{
            System.out.println("Hmmm");
            //for (Cell key : result.getAssignments().keySet()) {
            //    System.out.println(key+" "+result.getAssignments().get(key));
            //}
        }
    }

    public Assignment chronologicalBacktracking(Assignment currentAssigment){
        if(counter == 10)
            return null;
        counter++;
        System.out.println(counter + "Method Called");
        System.out.println(currentAssigment);
        HashMap<Cell, Integer> savedDomains = new HashMap<>();
        if(currentAssigment.isComplete(initialOpenCells))
            return currentAssigment;
        Cell chosenVariable = chooseNonAssignedVariable();
        ArrayList<Integer> domain = chosenVariable.getDomain();
        while(domain.size() > 0){
            Integer chosenValue = chooseValue(domain);
            Assignment newAssignment = new Assignment(currentAssigment);
            newAssignment.setAssignments(chosenVariable, chosenValue);
            if(newAssignment.isConsistent()){
                Assignment result = chronologicalBacktracking(newAssignment);
                if(result != null)
                    return result;
            }else{
                savedDomains.put(chosenVariable, chosenValue);
                chosenVariable.removeOptionFromDomains(chosenValue);
            }
        }
        if(savedDomains.size() > 0){
            for(Cell key : savedDomains.keySet()){
                key.addOptionToDomains(savedDomains.get(key));
            }
            savedDomains.clear();
        }
        System.out.println("Return null");
        return null;
    }

    public Cell chooseNonAssignedVariable(){
        Cell chosenVariable = currentOpenCells.get(0);
        currentOpenCells.remove(chosenVariable);
        return chosenVariable;
    }

    public Integer chooseValue(ArrayList<Integer> domain){
        Integer chosenIndex = new Random().nextInt(domain.size());
        return domain.get(chosenIndex);
    }

}
