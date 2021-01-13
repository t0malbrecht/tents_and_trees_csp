package tentsAndTrees.constraints;

import csp.AbstractConstraint;
import tentsAndTrees.Cell;
import tentsAndTrees.Grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RowConstraint extends AbstractConstraint {

    @Override
    public boolean isConsistent(Map<Cell, Integer> assignments, Grid grid) {
        ArrayList<Integer> checkedRows = new ArrayList<>();

        for (Cell key : assignments.keySet()) {
            if(checkedRows.contains(key.getRow()))
                continue; //if column was checked alrady, then skip this cell

            int tentsInRow = grid.getRowTents().get(key.getRow());
            int rowOfCell = key.getRow();
            checkedRows.add(rowOfCell);

            Map<Cell, Integer> assignedCellsInRow = assignments.entrySet().stream()
                    .filter(x -> x.getKey().getRow() == rowOfCell)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            //Get Available Cells which could be set initially (after arc consistency was used f.e.)
            List<Cell> cellsInRowWhichWereAvailable = grid.getOpenCells().stream()
                    .filter(x -> x.getRow() == rowOfCell)
                    .collect(Collectors.toList());

            Map<Cell, Integer> assignedCellsInRowWhichAreTent = assignedCellsInRow.entrySet().stream()
                    .filter(x -> assignedCellsInRow.get(x.getKey()) == 1) //1 means there is a tent
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            //When all cells in column are set and the amount of tents is not the amount which it should be, then this constraint is also violated
            if(assignedCellsInRow.size() == cellsInRowWhichWereAvailable.size() && assignedCellsInRowWhichAreTent.size() != tentsInRow){
                return false;
            }

            //When the amount of set tents exceeds the amount of tents allowed then this constraint is also violated
            if(assignedCellsInRowWhichAreTent.size() > tentsInRow){
                return false;
            }

        }
        return true;
    }

}
