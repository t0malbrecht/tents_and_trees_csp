package tentsAndTrees.constraints;

import csp.AbstractConstraint;
import tentsAndTrees.Cell;
import tentsAndTrees.Grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class columnConstraint extends AbstractConstraint {

    @Override
    public boolean isConsistent(Map<Cell, Integer> assignments, Grid grid) {
        boolean consistent = true;
        ArrayList<Integer> checkedColumns = new ArrayList<>();
        for (Cell key : assignments.keySet()) {
            if(checkedColumns.contains(key.getCol()))
                continue; //if column was checked alrady, then skip this cell

            int tentsInColumn = grid.getColumnsTents().get(key.getRow());
            int columnOfCell = key.getCol();
            checkedColumns.add(columnOfCell);

            Map<Cell, Integer> filteredMap = assignments.entrySet().stream()
                    .filter(x -> x.getKey().getCol() == columnOfCell)
                    .filter(x -> assignments.get(x.getKey()) == 1) //1 means there is a tent
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

            if(filteredMap.size() > tentsInColumn)
                consistent = false;

        }
        System.out.println("Column Is Consistent: "+consistent);
        return consistent;
    }
}
