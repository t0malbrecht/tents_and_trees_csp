package tentsAndTrees.constraints;

import csp.AbstractConstraint;
import tentsAndTrees.Cell;
import tentsAndTrees.Grid;
import java.util.Map;

public class EveryTentNeedsATreeConstraint extends AbstractConstraint {
    @Override
    public boolean isConsistent(Map<Cell, Integer> assignments, Grid grid) {

        for (Cell key : assignments.keySet()) {
            if(assignments.get(key) != 1)
                continue;

            if(key.getTrees() == null){
                System.out.println("EveryTent Is not Consistent: Z"+key.getRow()+" S"+key.getCol());
                return false;
            }
        }
        return true;
    }

}
