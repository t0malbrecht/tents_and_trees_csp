package tentsAndTrees.constraints;

import csp.AbstractConstraint;
import tentsAndTrees.Cell;
import tentsAndTrees.Grid;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class rowConstraint extends AbstractConstraint {

    @Override
    public boolean isConsistent(Map<Cell, Integer> assignments, Grid grid) {
        ArrayList<Integer> checkedRows = new ArrayList<>();
        for (Cell key : assignments.keySet()) {
            if(checkedRows.contains(key.getRow()))
                continue; //if column was checked alrady, then skip this cell

            int tentsInRow = grid.getRowTents().get(key.getRow());
            int rowOfCell = key.getRow();
            checkedRows.add(rowOfCell);

            Map<Cell, Integer> filteredMap = assignments.entrySet().stream()
                    .filter(x -> x.getKey().getRow() == rowOfCell)
                    .filter(x -> assignments.get(x.getKey()) == 1) //1 means there is a tent
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            if(filteredMap.size() > tentsInRow){
                System.out.println("Row Is not Consistent: Row"+rowOfCell);
                return false;
            }

        }
        return true;
    }

}
