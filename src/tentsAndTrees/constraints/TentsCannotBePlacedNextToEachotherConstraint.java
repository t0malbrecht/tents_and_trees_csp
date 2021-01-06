package tentsAndTrees.constraints;

import csp.AbstractConstraint;
import tentsAndTrees.Cell;
import tentsAndTrees.Grid;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class TentsCannotBePlacedNextToEachotherConstraint extends AbstractConstraint {

    @Override
    public boolean isConsistent(Map<Cell, Integer> assignments, Grid grid) {

        for (Cell key : assignments.keySet()) {
            if(assignments.get(key) != 1)
                continue;

            ArrayList<Cell> hvdNeighborsWithoutTrees = key.getHvdNeighborsWithoutTrees();
            for(Cell neighbor: hvdNeighborsWithoutTrees){
                if(assignments.containsKey(neighbor)) {
                    if (assignments.get(neighbor) == 1) {
                        //System.out.println("TentsCannot Is not Consistent: Z"+key.getRow()+" S"+key.getCol());
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
