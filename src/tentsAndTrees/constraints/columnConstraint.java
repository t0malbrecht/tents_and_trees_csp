package tentsAndTrees.constraints;

import csp.AbstractConstraint;
import tentsAndTrees.Cell;
import tentsAndTrees.Grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class columnConstraint extends AbstractConstraint {

    @Override
    public boolean isConsistent(Map<Cell, Integer> assignments, Grid grid) {
        ArrayList<Integer> checkedColumns = new ArrayList<>();
        for (Cell key : assignments.keySet()) {
            if(checkedColumns.contains(key.getCol()))
                continue; //if column was checked alrady, then skip this cell

            int tentsInColumn = grid.getColumnsTents().get(key.getCol());
            int columnOfCell = key.getCol();
            checkedColumns.add(columnOfCell);

            Map<Cell, Integer> assignedCellsInColumn = assignments.entrySet().stream()
                    .filter(x -> x.getKey().getCol() == columnOfCell)
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

            //Get Available Cells which could be set initially (after arc consistency was used f.e.)
            List<Cell> cellsInColumnWhichWereAvailable = grid.getOpenCells().stream()
                    .filter(x -> x.getCol() == columnOfCell)
                    .collect(Collectors.toList());

            Map<Cell, Integer> assignedCellsInColumnWhichAreTent = assignedCellsInColumn.entrySet().stream()
                    .filter(x -> assignments.get(x.getKey()) == 1) //1 means there is a tent
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

            //When all cells in column are set and the amount of tents is not the amount which it should be, then this constraint is also violated
            if(assignedCellsInColumn.size() == cellsInColumnWhichWereAvailable.size() && assignedCellsInColumnWhichAreTent.size() != tentsInColumn){
                return false;
            }

            //When the amount of set tents exceeds the amount of tents allowed then this constraint is also violated
            if(assignedCellsInColumnWhichAreTent.size() > tentsInColumn){
                return false;
            }

        }
        return true;
    }
}
