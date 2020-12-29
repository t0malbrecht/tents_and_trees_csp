package tentsAndTrees.constraints;

import csp.AbstractConstraint;
import tentsAndTrees.Cell;
import tentsAndTrees.Grid;
import java.util.Map;

public class EveryTentNeedsATreeConstraint extends AbstractConstraint {
    @Override
    public boolean isConsistent(Map<Cell, Integer> assignments, Grid grid) {
        boolean consistent = true;

        for (Cell key : assignments.keySet()) {
            if(assignments.get(key) != 1)
                continue;

            if(key.getTrees() == null)
                consistent = false;
        }
        System.out.println("EveryTent Is Consistent: "+consistent);
        return consistent;
    }

}
