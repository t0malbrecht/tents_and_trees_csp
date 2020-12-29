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
        boolean consistent = true;

        for (Cell key : assignments.keySet()) {
            if(assignments.get(key) != 1)
                continue;

            ArrayList<Cell> hvdNeighborsWithoutTrees = key.getHvdNeighborsWithoutTrees();
            for(Cell neighbor: hvdNeighborsWithoutTrees){
                if(assignments.containsKey(neighbor)){
                    if(assignments.get(neighbor) == 1){
                        consistent = false;
                    }
                }
            }
        }
        System.out.println("TentsCannot Is Consistent: "+consistent);
        return consistent;
    }
}
