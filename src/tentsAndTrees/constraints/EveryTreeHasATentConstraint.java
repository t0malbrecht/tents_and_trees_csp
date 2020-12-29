package tentsAndTrees.constraints;

import csp.AbstractConstraint;
import tentsAndTrees.Cell;
import tentsAndTrees.Grid;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class EveryTreeHasATentConstraint extends AbstractConstraint {

    @Override
    public boolean isConsistent(Map<Cell, Integer> assignments, Grid grid) {
        boolean consistent = true;
        ArrayList<Cell> checkedTrees = new ArrayList<>();

        for (Cell key : assignments.keySet()) {
            if(key.getTrees() == null)
                continue;
            for (Cell tree : key.getTrees()) {
                if (checkedTrees.contains(tree))
                    continue; //if tree was checked alrady, then skip this cell

                checkedTrees.add(tree);
                Map<Cell, Integer> assignedVdNeighborsWithoutTree = assignments.entrySet().stream()
                        .filter(x -> x.getKey().getTrees() != null)
                        .filter(x -> x.getKey().getTrees().contains(tree))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                if (tree.getHvNeighborsWithoutTrees().size() > assignedVdNeighborsWithoutTree.size())
                    continue;
                else {
                    Map<Cell, Integer> assignedVdNeighborsWithoutTreeWhichHaveSetTent = assignments.entrySet().stream()
                            .filter(x -> assignments.get(x.getKey()) == 1) //1 means there is a tent
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                    if (assignedVdNeighborsWithoutTreeWhichHaveSetTent.size() > 0)
                        continue;
                    else
                        consistent = false;
                }

            }
        }
        System.out.println("EveryTent Is Consistent: "+consistent);
        return consistent;
    }
}
