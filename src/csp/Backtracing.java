package csp;

import com.sun.org.apache.xpath.internal.operations.Bool;
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
    public static int counter = 0;
    private int depth = -1;
    private Grid grid;


    public Backtracing(Grid grid){
        this.initialOpenCells = grid.getOpenCells();
        this.currentOpenCells = new ArrayList<>(initialOpenCells);
        initialAssignment = new Assignment(grid, new HashMap<>());
        this.grid = grid;
    }

    public String start(){
        counter = 0;
        Assignment result = chronologicalBacktracking(initialAssignment);
        if(result == null){
            return "No Solution found";
        }else{
            //result.printField();
            return "WIN"+counter;
            //for (Cell key : result.getAssignments().keySet()) {
            //    System.out.println(key+" "+result.getAssignments().get(key));
            //}
        }
    }

    public Assignment chronologicalBacktracking(Assignment currentAssigment){
        depth++;
        //System.out.println(" Zug!!!: "+counter);
        //System.out.println(" Zug!!!: "+counter);
        //System.out.println(" Tiefe!!!: "+depth);
        if(currentAssigment.isComplete(initialOpenCells)){
            return currentAssigment;
        }
        Cell chosenVariable = chooseNonAssignedVariable(0);
        ArrayList<Integer> domain = chosenVariable.getDomain();
        ArrayList<Pair<Cell, Integer>> savedDomains = new ArrayList<>();
        while(domain.size() > 0){
            counter++;
            Integer chosenValue = chooseValue(domain, true);
            Assignment newAssignment = new Assignment(currentAssigment);
            newAssignment.setAssignments(chosenVariable, chosenValue);
            ArrayList<Pair<Cell, Integer>> savedDomainsForwardChecking = new ArrayList<>();
            if(newAssignment.isConsistent()){
                Assignment result = chronologicalBacktracking(newAssignment);
                if(result != null){
                    return result;
                }else{
                    if(savedDomainsForwardChecking.size() > 0){
                        for(Pair pair : savedDomainsForwardChecking){
                            Cell cell = (Cell) pair.getKey();
                            if(!cell.getDomain().contains((Integer) pair.getValue()))
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
                        if(!cell.getDomain().contains((Integer) pair.getValue()))
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
        depth--;
        //System.out.println(" Tiefe: "+depth);
        return null;
    }

    public Boolean forwardChecking(Assignment assignment, ArrayList<Pair<Cell, Integer>> savedDomainsForwardChecking){
        if(currentOpenCells.size() * 2 < currentOpenCells.stream().mapToInt(Cell::getDomainSize).sum())
            System.out.println("Size: "+currentOpenCells.size()+" Domain: "+currentOpenCells.stream().mapToInt(Cell::getDomainSize).sum());

        ArrayList<Cell> currentOpenCellsCopy = new ArrayList<>();
        for(Cell cell: currentOpenCells){
            Cell newCell = new Cell(cell);
            currentOpenCellsCopy.add(newCell);
        }

        reduceDomainsFromNothing(currentOpenCellsCopy, assignment, savedDomainsForwardChecking);
        reduceDomainsFromTents(currentOpenCellsCopy, assignment, savedDomainsForwardChecking);

        for(Cell cell: currentOpenCells){
            Cell cellToCheck = currentOpenCellsCopy.stream().filter(x -> x.getRow() == cell.getRow()).filter(x -> x.getCol() == cell.getCol()).findFirst().orElse(null);
            assert cellToCheck != null;
            if(cellToCheck.getDomain().size() == 0){
                savedDomainsForwardChecking.clear();
                //System.out.println("Z:"+openCell.getRow()+" S:"+openCell.getCol()+" FWD-Fail");
                return false;
            }
        }
        for(Pair pair: savedDomainsForwardChecking){
            Cell cell = (Cell) pair.getKey();
            cell.removeOptionFromDomains((Integer) pair.getValue());
        }
        return true;
    }

    public void reduceDomainsFromNothing(ArrayList<Cell> currentOpenCellsCopy, Assignment assignment, ArrayList<Pair<Cell, Integer>> savedDomainsForwardChecking) {
        ArrayList<Cell> checkedTrees = new ArrayList<>();
        ArrayList<Integer> checkedColumns = new ArrayList<>();
        ArrayList<Integer> checkedRows = new ArrayList<>();

        for (Cell openCell : currentOpenCells) {
            //Check Column Constraint (Set as many tents in columns as they should)
            if (openCell.getDomain().contains(0)) { //When cell doesnt contains Option to set Nothing then this Constraint has no matter
                if (!checkedColumns.contains(openCell.getCol())) {
                    checkedColumns.add(openCell.getCol());

                    int columnOfCell = openCell.getCol();
                    int tentsInColumn = grid.getColumnsTents().get(columnOfCell);

                    Map<Cell, Integer> assignedCellsInColumn = assignment.getAssignments().entrySet().stream()
                            .filter(x -> x.getKey().getCol() == columnOfCell)
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                    //Get Available Cells which could be set initially (after arc consistency was used f.e.)
                    List<Cell> cellsInColumnWhichWereAvailable = grid.getOpenCells().stream()
                            .filter(x -> x.getCol() == columnOfCell)
                            .collect(Collectors.toList());

                    Map<Cell, Integer> assignedCellsInColumnWhichAreTent = assignedCellsInColumn.entrySet().stream()
                            .filter(x -> assignedCellsInColumn.get(x.getKey()) == 1) //1 means there is a tent
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                    List<Cell> openCellsInColumnWhichCanBeTent = currentOpenCells.stream()
                            .filter(x -> x.getCol() == columnOfCell)
                            .filter(x -> x.getDomain().contains(1))
                            .collect(Collectors.toList());

                    int amountOfTentsWhichStillneedToBePlaced = tentsInColumn - assignedCellsInColumnWhichAreTent.size();


                    //When the amount of tentswhichneedtobeplaced equals the amount of cells which can be placed a tent, then this all need to be set as tent
                    if (amountOfTentsWhichStillneedToBePlaced == openCellsInColumnWhichCanBeTent.size()) {
                        for (Cell openCellInColumnWhichCanBeTent : openCellsInColumnWhichCanBeTent) {
                            if (!openCellInColumnWhichCanBeTent.getDomain().contains(0))
                                continue;
                            savedDomainsForwardChecking.add(new Pair(openCellInColumnWhichCanBeTent, 0));
                            Cell cellToEdit = currentOpenCellsCopy.stream().filter(x -> x.getRow() == openCellInColumnWhichCanBeTent.getRow()).filter(x -> x.getCol() == openCellInColumnWhichCanBeTent.getCol()).findFirst().orElse(null);
                            assert cellToEdit != null;
                            cellToEdit.removeOptionFromDomains(0);
                        }
                    }
                }
            }

                //Check Row Constraint (set as many tents in row as they should)
                if (openCell.getDomain().contains(0)) { //When cell doesnt contains Option to set Nothing then this Constraint has no matter
                    if (!checkedRows.contains(openCell.getRow())) {
                        checkedRows.add(openCell.getRow());

                        int rowOfCell = openCell.getRow();
                        int tentsInRow = grid.getRowTents().get(rowOfCell);

                        Map<Cell, Integer> assignedCellsInRow = assignment.getAssignments().entrySet().stream()
                                .filter(x -> x.getKey().getRow() == rowOfCell)
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                        //Get Available Cells which could be set initially (after arc consistency was used f.e.)
                        List<Cell> cellsInRowWhichWereAvailable = grid.getOpenCells().stream()
                                .filter(x -> x.getRow() == rowOfCell)
                                .collect(Collectors.toList());

                        Map<Cell, Integer> assignedCellsInRowWhichAreTent = assignedCellsInRow.entrySet().stream()
                                .filter(x -> assignedCellsInRow.get(x.getKey()) == 1) //1 means there is a tent
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                        List<Cell> openCellsInRowWhichCanBeTent = currentOpenCells.stream()
                                .filter(x -> x.getRow() == rowOfCell)
                                .filter(x -> x.getDomain().contains(1))
                                .collect(Collectors.toList());

                        int amountOfTentsWhichStillneedToBePlaced = tentsInRow - assignedCellsInRowWhichAreTent.size();


                        //When the amount of tentswhichneedtobeplaced equals the amount of cells which can be placed a tent, then this all need to be set as tent
                        if (amountOfTentsWhichStillneedToBePlaced == openCellsInRowWhichCanBeTent.size()) {
                            for (Cell openCellInRowWhichCanBeTent : openCellsInRowWhichCanBeTent) {
                                if (!openCellInRowWhichCanBeTent.getDomain().contains(0))
                                    continue;
                                savedDomainsForwardChecking.add(new Pair(openCellInRowWhichCanBeTent, 0));
                                Cell cellToEdit = currentOpenCellsCopy.stream().filter(x -> x.getRow() == openCellInRowWhichCanBeTent.getRow()).filter(x -> x.getCol() == openCellInRowWhichCanBeTent.getCol()).findFirst().orElse(null);
                                assert cellToEdit != null;
                                cellToEdit.removeOptionFromDomains(0);
                            }
                        }
                    }
                }

                    //Check EveryTreeHasATent Constraint
                    if (openCell.getTrees().size() > 0) {//When cell doesnt contains Tree as neighbors then this Constraint has no matter
                        for (Cell tree : openCell.getTrees()) {
                            if (!checkedTrees.contains(tree)) {
                                checkedTrees.add(tree);
                                //get assignedNeighbors
                                Map<Cell, Integer> assignedHvNeighborsOfTree = assignment.getAssignments().entrySet().stream()
                                        .filter(x -> x.getKey().getTrees().contains(tree))
                                        .filter(x -> x.getKey().getDomain().contains(1))
                                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                                //get assignedNeighbors which are a tent
                                Map<Cell, Integer> assignedHvNeighborsOfTreeWhichAreATent = assignedHvNeighborsOfTree.entrySet().stream()
                                        .filter(x -> assignedHvNeighborsOfTree.get(x.getKey()) == 1) //1 means there is a tent
                                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                                //get unassignedNeighbors which can be trees
                                List<Cell> unassignedNeighbors = currentOpenCellsCopy.stream()
                                        .filter(x -> x.getTrees().contains(tree))
                                        .collect(Collectors.toList());

                                List<Cell> unassignedNeighborsWhichCanBeTrees = unassignedNeighbors.stream()
                                        .filter(x -> x.getDomain().contains(1))
                                        .collect(Collectors.toList());

                                //when there is only one unassignedNeighbor left and currently is no tent set, then this neighbor must be a tent
                                if (unassignedNeighbors.size() == 1 && assignedHvNeighborsOfTreeWhichAreATent.size() == 0) {
                                    if (!unassignedNeighbors.get(0).getDomain().contains(0))
                                        continue;
                                    savedDomainsForwardChecking.add(new Pair(unassignedNeighbors.get(0), 0));
                                    Cell cellToEdit = currentOpenCellsCopy.stream().filter(x -> x.getRow() == unassignedNeighbors.get(0).getRow()).filter(x -> x.getCol() == unassignedNeighbors.get(0).getCol()).findFirst().orElse(null);
                                    assert cellToEdit != null;
                                    cellToEdit.removeOptionFromDomains(0);
                                }

                                //when there is only one unassignedNeighbor who can be a tent left and currently is no tent set, then this neighbor must be a tent
                                if (unassignedNeighborsWhichCanBeTrees.size() == 1 && assignedHvNeighborsOfTreeWhichAreATent.size() == 0) {
                                    if (!unassignedNeighbors.get(0).getDomain().contains(0))
                                        continue;
                                    savedDomainsForwardChecking.add(new Pair(unassignedNeighbors.get(0), 0));
                                    Cell cellToEdit = currentOpenCellsCopy.stream().filter(x -> x.getRow() == unassignedNeighborsWhichCanBeTrees.get(0).getRow()).filter(x -> x.getCol() == unassignedNeighborsWhichCanBeTrees.get(0).getCol()).findFirst().orElse(null);
                                    assert cellToEdit != null;
                                    cellToEdit.removeOptionFromDomains(0); // When Cell is the last open Cell next to a tree and no other cell is a tent, then this needs to be a tent
                                }
                            }
                        }
                    }
                }
            }

    public void reduceDomainsFromTents(ArrayList<Cell> currentOpenCellsCopy, Assignment assignment, ArrayList<Pair<Cell, Integer>> savedDomainsForwardChecking) {
        ArrayList<Integer> checkedColumns = new ArrayList<>();
        ArrayList<Integer> checkedRows = new ArrayList<>();
        for (Cell openCell : currentOpenCells) {
            //Check Column Constraint
            if (openCell.getDomain().contains(1)) { //When cell doesnt contains Option to set Tree then this Constraint has no matter
                if (!checkedColumns.contains(openCell.getCol())) {
                    checkedColumns.add(openCell.getCol());
                    int columnOfCell = openCell.getCol();
                    int tentsInColumn = grid.getColumnsTents().get(columnOfCell);

                    Map<Cell, Integer> assignedCellsInColumn = assignment.getAssignments().entrySet().stream()
                            .filter(x -> x.getKey().getCol() == columnOfCell)
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                    Map<Cell, Integer> assignedCellsInColumnWhichAreTent = assignedCellsInColumn.entrySet().stream()
                            .filter(x -> assignedCellsInColumn.get(x.getKey()) == 1) //1 means there is a tent
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


                    //When the amount of set tents is similar the amount of tents allowed then the other tents in this column only can be tents
                    if (tentsInColumn == assignedCellsInColumnWhichAreTent.size()) {
                        List<Cell> openCellsInColumn = currentOpenCells.stream()
                                .filter(x -> x.getCol() == columnOfCell)
                                .collect(Collectors.toList());
                        for (Cell openCellInColumn : openCellsInColumn) {
                            if(!openCellInColumn.getDomain().contains(1))
                                continue;
                            savedDomainsForwardChecking.add(new Pair(openCellInColumn, 1));
                            Cell cellToEdit = currentOpenCellsCopy.stream().filter(x -> x.getRow() == openCellInColumn.getRow()).filter(x -> x.getCol() == openCellInColumn.getCol()).findFirst().orElse(null);
                            assert cellToEdit != null;
                            cellToEdit.removeOptionFromDomains(1);
                        }
                    }
                }
            }
            //Check Row Constraint
            if (openCell.getDomain().contains(1)) { //When cell doesnt contains Option to set Tree then this Constraint has no matter
                if (!checkedRows.contains(openCell.getRow())) {
                    checkedRows.add(openCell.getRow());
                    int rowOfCell = openCell.getRow();
                    int tentsInRow = grid.getRowTents().get(rowOfCell);
                    Map<Cell, Integer> filteredMap = assignment.getAssignments().entrySet().stream()
                            .filter(x -> x.getKey().getRow() == rowOfCell)
                            .filter(x -> assignment.getAssignments().get(x.getKey()) == 1) //1 means there is a tent
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                    if (tentsInRow == filteredMap.size()) {
                        List<Cell> openCellsInRow = currentOpenCells.stream()
                                .filter(x -> x.getRow() == rowOfCell)
                                .collect(Collectors.toList());
                        for (Cell openCellInRow : openCellsInRow) {
                            if(!openCellInRow.getDomain().contains(1))
                                continue;
                            savedDomainsForwardChecking.add(new Pair(openCellInRow, 1));
                            Cell cellToEdit = currentOpenCellsCopy.stream().filter(x -> x.getRow() == openCellInRow.getRow()).filter(x -> x.getCol() == openCellInRow.getCol()).findFirst().orElse(null);
                            assert cellToEdit != null;
                            cellToEdit.removeOptionFromDomains(1);
                        }
                    }
                }
            }
            //Check EveryTentNeedsATree Constraint (Doesnt need to be checked because it is already checked when creating the grid, and the trees position doesnt change)

            //Check TentsCannotBePlacedNextToEachOtherConstraint
            if (openCell.getDomain().contains(1)) {//When cell doesnt contains Option to set Tree then this Constraint has no matter
                Map<Cell, Integer> assignedHvdNeighborsWhichAreATent = assignment.getAssignments().entrySet().stream()
                        .filter(x -> x.getKey().getHvdNeighborsWithoutTrees().contains(openCell))
                        .filter(x -> assignment.getAssignments().get(x.getKey()) == 1) //1 means there is a tent
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                if (assignedHvdNeighborsWhichAreATent.size() > 0) {
                    savedDomainsForwardChecking.add(new Pair(openCell, 1));
                    Cell cellToEdit = currentOpenCellsCopy.stream().filter(x -> x.getRow() == openCell.getRow()).filter(x -> x.getCol() == openCell.getCol()).findFirst().orElse(null);
                    assert cellToEdit != null;
                    cellToEdit.removeOptionFromDomains(1); //when any hvd neighbor is a tent, then this Cell cant be a tent.
                }
            }
        }
    }

    public Cell chooseNonAssignedVariable(int heuristic){
        //0 = first open Value in Array, 1= random, 2= most constraining variable (Most constrained variable isnt viable cause at beginning are all equal)
        Cell chosenVariable = null;
        if(heuristic == 0){
            currentOpenCells.sort(new CellSorter());
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
