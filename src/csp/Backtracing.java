package csp;

import tentsAndTrees.Cell;
import tentsAndTrees.Grid;

import java.util.*;
import java.util.logging.Logger;

public class Backtracing{
    private ArrayList<Cell> initialOpenCells;
    private ArrayList<Cell> currentOpenCells;
    private Assignment initialAssignment;
    private int counter = 0;
    private int depth = -1;


    public Backtracing(Grid grid){
        this.initialOpenCells = grid.getOpenCells();
        this.currentOpenCells = new ArrayList<>(initialOpenCells);
        initialAssignment = new Assignment(grid, new HashMap<>());
    }

    public void start(){
        Assignment result = chronologicalBacktracking(initialAssignment);
        if(result == null){
            System.out.println("No Solution found");
        }else{
            result.printField();
            System.out.println("WIN");
            //for (Cell key : result.getAssignments().keySet()) {
            //    System.out.println(key+" "+result.getAssignments().get(key));
            //}
        }
    }

    public Assignment chronologicalBacktracking(Assignment currentAssigment){
        depth++;
        System.out.println(" Tiefe!!!: "+depth);
        if(currentAssigment.isComplete(initialOpenCells)){
            System.out.println("WIN"+counter);
            return currentAssigment;
        }
        Cell chosenVariable = chooseNonAssignedVariable();
        ArrayList<Integer> domain = chosenVariable.getDomain();
        ArrayList<Integer> savedDomains = new ArrayList<>();
        while(domain.size() > 0){
            counter++;
            Integer chosenValue = chooseValue(domain);
            Assignment newAssignment = new Assignment(currentAssigment);
            newAssignment.setAssignments(chosenVariable, chosenValue);
            System.out.println("Zug: "+counter);
            System.out.println("Set: "+"["+chosenVariable.getRow()+";"+chosenVariable.getCol()+"]"+" Domain:"+chosenVariable.getDomain()+" Choosen:"+chosenValue);
            if(newAssignment.isConsistent()){
                Assignment result = chronologicalBacktracking(newAssignment);
                if(result != null){
                    return result;
                }else{
                    savedDomains.add(chosenValue);
                    System.out.println("PUT IN"+savedDomains);
                    chosenVariable.removeOptionFromDomains(chosenValue);
                }
            }else{
                savedDomains.add(chosenValue);
                System.out.println("PUT IN"+savedDomains);
                chosenVariable.removeOptionFromDomains(chosenValue);
            }
        }
        if(savedDomains.size() > 0){
            for(Integer tmp : savedDomains){
                chosenVariable.addOptionToDomains(tmp);
            }
            savedDomains.clear();
        }
        currentOpenCells.add(chosenVariable);
        depth--;
        System.out.println(" Tiefe: "+depth);
        return null;
    }

    public Cell chooseNonAssignedVariable(){
        if(currentOpenCells.size() == 0){
            System.out.println("AHA");
            System.exit(0);
        }
        Cell chosenVariable = currentOpenCells.get(0);
        currentOpenCells.remove(chosenVariable);
        return chosenVariable;
    }

    public Integer chooseValue(ArrayList<Integer> domain){
        Integer chosenIndex = new Random().nextInt(domain.size());
        return domain.get(chosenIndex);
    }

}
