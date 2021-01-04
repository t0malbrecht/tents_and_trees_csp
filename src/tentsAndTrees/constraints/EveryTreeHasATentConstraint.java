package tentsAndTrees.constraints;

import csp.AbstractConstraint;
import csp.Assignment;
import tentsAndTrees.Cell;
import tentsAndTrees.Grid;

import java.util.ArrayList;
import java.util.HashMap;
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
                //Get all Vertical and Horizontal neighbors of the tree which arent trees
                Map<Cell, Integer> assignedVhNeighborsWithoutTree = assignments.entrySet().stream()
                        .filter(x -> x.getKey().getTrees() != null)
                        .filter(x -> x.getKey().getTrees().contains(tree))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                //When the tree has neighbors which arent set yet, then skip
                if (tree.getHvNeighborsWithoutTrees().size() > assignedVhNeighborsWithoutTree.size())
                    continue;

                //Filter assigned neighbors to get all neigbhors which are a tent
                Map<Cell, Integer> assignedVdNeighborsWithoutTreeWhichHaveSetTent = assignedVhNeighborsWithoutTree.entrySet().stream()
                        .filter(x -> assignments.get(x.getKey()) == 1) //1 means there is a tent
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                if (assignedVdNeighborsWithoutTreeWhichHaveSetTent.size() < 1)
                    return false;

                //Filter assigned neighbors to get all neigbhors which are a tent and which havent any other trees as neighbors
                Map<Cell, Integer> assignedVdNeighborsWithoutTreeWhichHaveSetTentAndNoOtherTreeAsNeighbor = assignedVdNeighborsWithoutTreeWhichHaveSetTent.entrySet().stream()
                        .filter(x -> x.getKey().getTrees().size() == 1)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                //When there are any, then skip
                if (assignedVdNeighborsWithoutTreeWhichHaveSetTentAndNoOtherTreeAsNeighbor.size() > 0)
                    continue;

                //Otherwise get all assigned neighbors which are a tent and which have other trees as horizontal or vertical neighbors
                Map<Cell, Integer> assignedVdNeighborsWithoutTreeWhichHaveSetTentAndOtherTreeAsNeighbor = assignedVdNeighborsWithoutTreeWhichHaveSetTent.entrySet().stream()
                        .filter(x -> x.getKey().getTrees().size() > 1)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                //for each of them, check for every of their trees if they have any unassigned vertical of horizontal neighbors
                for (Cell cell : assignedVdNeighborsWithoutTreeWhichHaveSetTentAndOtherTreeAsNeighbor.keySet()) {
                    ArrayList<Cell> tempTrees = new ArrayList<>(cell.getTrees());
                    tempTrees.remove(tree); //remove tree as it is already checkedW

                    for (Cell treeTmp : tempTrees) {
                        Map<Cell, Integer> assignedVhNeighborsWithoutTree2 = assignments.entrySet().stream()
                                .filter(x -> x.getKey().getTrees() != null)
                                .filter(x -> x.getKey().getTrees().contains(treeTmp))
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                        if (treeTmp.getHvNeighborsWithoutTrees().size() > assignedVhNeighborsWithoutTree2.size())
                            continue;

                        //Check if they have any other tent as neighbor
                        //TODO: Eigentlich reicht das hier noch nicht, rekursiver aufruf wäre möglich, aber geht das noch einfacher?
                        Map<Cell, Integer> otherTentsAsNeighbors = assignedVhNeighborsWithoutTree2.entrySet().stream()
                                .filter(x -> assignments.get(x.getKey()) == 1) //1 means there is a tent
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                        if (otherTentsAsNeighbors.size() < 2) {
                            //System.out.println("EveryTree Is not Consistent: Z" + tree.getRow() + " S" + tree.getCol());
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
