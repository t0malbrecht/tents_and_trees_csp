package csp;

import javafx.util.Pair;
import tentsAndTrees.Cell;
import tentsAndTrees.Grid;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Backtracing{
    private ArrayList<Cell> initialOpenCells;
    private ArrayList<Cell> currentOpenCells;
    private Assignment initialAssignment;
    private int counter = 0;
    private int depth = -1;
    private Grid grid;


    public Backtracing(Grid grid){
        this.initialOpenCells = grid.getOpenCells();
        this.currentOpenCells = new ArrayList<>(initialOpenCells);
        initialAssignment = new Assignment(grid, new HashMap<>());
        this.grid = grid;
    }

    public void start(){
        Assignment result = chronologicalBacktracking(initialAssignment);
        if(result == null){
            System.out.println("No Solution found");
        }else{
            result.printField();
            System.out.println("WIN"+counter);
            //for (Cell key : result.getAssignments().keySet()) {
            //    System.out.println(key+" "+result.getAssignments().get(key));
            //}
        }
    }

    public Assignment chronologicalBacktracking(Assignment currentAssigment){
        depth++;
        counter++;
        System.out.println(" Zug!!!: "+counter);
        //System.out.println(" Tiefe!!!: "+depth);
        if(currentAssigment.isComplete(initialOpenCells)){
            return currentAssigment;
        }
        Cell chosenVariable = chooseNonAssignedVariable(0);
        ArrayList<Integer> domain = chosenVariable.getDomain();
        ArrayList<Pair<Cell, Integer>> savedDomains = new ArrayList<>();
        while(domain.size() > 0){
            ArrayList<Pair<Cell, Integer>> savedDomainsForwardChecking = new ArrayList<>();
            counter++;
            Integer chosenValue = chooseValue(domain, true);
            Assignment newAssignment = new Assignment(currentAssigment);
            newAssignment.setAssignments(chosenVariable, chosenValue);
            //System.out.println("Zug: "+counter);
            //System.out.println("Set: "+"["+chosenVariable.getRow()+";"+chosenVariable.getCol()+"]"+" Domain:"+chosenVariable.getDomain()+" Choosen:"+chosenValue);
            if(newAssignment.isConsistent() && forwardChecking(newAssignment, savedDomainsForwardChecking)){
                Assignment result = chronologicalBacktracking(newAssignment);
                if(result != null){
                    return result;
                }else{
                    if(savedDomainsForwardChecking.size() > 0){
                        for(Pair pair : savedDomainsForwardChecking){
                            Cell cell = (Cell) pair.getKey();
                            cell.addOptionToDomains( (Integer) pair.getValue());
                        }
                        savedDomainsForwardChecking.clear();
                    }
                    savedDomains.add(new Pair(chosenVariable, chosenValue));
                    chosenVariable.removeOptionFromDomains(chosenValue);
                }
            }else{
                if(savedDomainsForwardChecking.size() > 0){
                    for(Pair pair : savedDomainsForwardChecking){
                        Cell cell = (Cell) pair.getKey();
                        cell.addOptionToDomains( (Integer) pair.getValue());
                    }
                    savedDomainsForwardChecking.clear();
                }
                savedDomains.add(new Pair(chosenVariable, chosenValue));
                chosenVariable.removeOptionFromDomains(chosenValue);
            }
        }
        if(savedDomains.size() > 0){
            for(Pair pair : savedDomains){
                Cell cell = (Cell) pair.getKey();
                cell.addOptionToDomains( (Integer) pair.getValue());
            }
            savedDomains.clear();
        }
        currentOpenCells.add(chosenVariable);
        currentOpenCells.sort(new CellSorter());
        depth--;
        //System.out.println(" Tiefe: "+depth);
        return null;
    }

    public boolean forwardChecking(Assignment assignment, ArrayList<Pair<Cell, Integer>> savedDomains){
        ArrayList<Cell> checkedColumns = new ArrayList<>();
        ArrayList<Cell> checkedRows = new ArrayList<>();
        ArrayList<Cell> checkedTrees = new ArrayList<>();

        for(Cell openCell: currentOpenCells){
            //Check Column Constraint
            if(openCell.getDomain().contains(1)){ //When column doesnt contains Option to set Tree then this Constraint has no matter
                if(!checkedColumns.contains(openCell.getCol())){
                    checkedColumns.add(openCell);
                    int columnOfCell = openCell.getCol();
                    int tentsInColumn = grid.getColumnsTents().get(columnOfCell);
                    Map<Cell, Integer> filteredMap = assignment.getAssignments().entrySet().stream()
                            .filter(x -> x.getKey().getCol() == columnOfCell)
                            .filter(x -> assignment.getAssignments().get(x.getKey()) == 1) //1 means there is a tent
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                    if(tentsInColumn == filteredMap.size()){
                        List<Cell> openCellsInColumn = currentOpenCells.stream()
                                .filter(x -> x.getCol() == columnOfCell)
                                .collect(Collectors.toList());
                        for(Cell openCellInColumn: openCellsInColumn){
                            savedDomains.add(new Pair(openCellInColumn, 1));
                            openCellInColumn.removeOptionFromDomains(1);
                        }
                    }
                }
            }
            //Check Row Constraint
            if(openCell.getDomain().contains(1)){ //When column doesnt contains Option to set Tree then this Constraint has no matter
                if(!checkedRows.contains(openCell.getRow())){
                    checkedRows.add(openCell);
                    int rowOfCell = openCell.getRow();
                    int tentsInRow = grid.getRowTents().get(rowOfCell);
                    Map<Cell, Integer> filteredMap = assignment.getAssignments().entrySet().stream()
                            .filter(x -> x.getKey().getRow() == rowOfCell)
                            .filter(x -> assignment.getAssignments().get(x.getKey()) == 1) //1 means there is a tent
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                    if(tentsInRow == filteredMap.size()){
                        List<Cell> openCellsInRow = currentOpenCells.stream()
                                .filter(x -> x.getRow() == rowOfCell)
                                .collect(Collectors.toList());
                        for(Cell openCellInRow: openCellsInRow){
                            savedDomains.add(new Pair(openCellInRow, 1));
                            openCellInRow.removeOptionFromDomains(1);
                        }
                    }
                }
            }
            //Check EveryTentNeedsATree Constraint (Doesnt need to be checked because it is already checked when creating the grid, and the trees position doesnt change)

            //Check EveryTreeHasATent Constraint
            if(openCell.getTrees().size() > 0){//When column doesnt contains Tree as neighbors hen this Constraint has no matter
                for(Cell tree: openCell.getTrees()){
                    if(!checkedTrees.contains(tree)){
                        checkedTrees.add(tree);
                        Map<Cell, Integer> assignedHvNeighborsOfTree = assignment.getAssignments().entrySet().stream()
                                .filter(x -> x.getKey().getTrees().contains(tree))
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                        Map<Cell, Integer> assignedHvNeighborsOfTreeWhichAreATent = assignedHvNeighborsOfTree.entrySet().stream()
                                .filter(x -> assignedHvNeighborsOfTree.get(x.getKey()) == 1) //1 means there is a tent
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                        if(tree.getHvNeighborsWithoutTrees().size()-1 == assignedHvNeighborsOfTree.size() && assignedHvNeighborsOfTreeWhichAreATent.size() == 0){
                            savedDomains.add(new Pair(openCell, 0));
                            openCell.removeOptionFromDomains(0); // When Cell is the last open Cell next to a tree and no other cell is a tent, then this needs to be a tent
                            //TODO: (doesnt checks for one tent which could be for multiple trees)
                        }
                    }
                }
            }
            //Check TentsCannotBePlacedNextToEachOtherConstraint
            if(openCell.getDomain().contains(1)){//When column doesnt contains Option to set Tree then this Constraint has no matter
                Map<Cell, Integer> assignedHvdNeighborsWhichAreATent = assignment.getAssignments().entrySet().stream()
                        .filter(x -> x.getKey().getHvdNeighborsWithoutTrees().contains(openCell))
                        .filter(x -> assignment.getAssignments().get(x.getKey()) == 1) //1 means there is a tent
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                if(assignedHvdNeighborsWhichAreATent.size() > 0){
                    savedDomains.add(new Pair(openCell, 1));
                    openCell.removeOptionFromDomains(1); //when any hvd neighbor is a tent, then this Cell cant be a tent.
                }
            }

            if(openCell.getDomain().size() == 0){
                //System.out.println("Z:"+openCell.getRow()+" S:"+openCell.getCol()+" FWD-Fail");
                return false;
            }
        }
        return true;
    }

    public Cell chooseNonAssignedVariable(int heuristic){
        //0 = first open Value in Array, 1= random, 2= most constraining variable (Most constrained variable isnt viable cause at beginning are all equal)
        Cell chosenVariable = null;
        if(heuristic == 0){
            chosenVariable = currentOpenCells.get(0);
        }else if(heuristic == 1){
            chosenVariable = currentOpenCells.get(new Random().nextInt(currentOpenCells.size()));
        }else if(heuristic == 2){
            ArrayList<Cell> sortedCells = new ArrayList<>(currentOpenCells);
            sortedCells.sort(Comparator.comparing(Cell::getHvdNeighborsWithoutTreesSize)); //Sort list based on h,v,d neighbors
            chosenVariable = sortedCells.get(0);
        }
        currentOpenCells.remove(chosenVariable);
        return chosenVariable;
    }

    public Integer chooseValue(ArrayList<Integer> domain, boolean leastConstraining){
        int chosenIndex;
        if(leastConstraining){
            //Least constraining is 0, because a 0 in a Cell doesnt bother the neighbours, but with 2 variables also not really viable
            chosenIndex = domain.indexOf(0);
        }else{
            //Is 1 most constraining? Much better performance!
            chosenIndex = domain.indexOf(1);
            //chosenIndex = new Random().nextInt(domain.size());
            //chosenIndex = domain.size()-1;
            //chosenIndex = 0;
        }
        if(chosenIndex == -1)
            chosenIndex = 0;
        return domain.get(chosenIndex);
    }

}
