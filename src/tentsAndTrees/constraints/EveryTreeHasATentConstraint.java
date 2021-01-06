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
    static ArrayList<Cell> checkedCells = new ArrayList<>();

    @Override
    public boolean isConsistent(Map<Cell, Integer> assignments, Grid grid) {
        boolean consistent = true;
        ArrayList<Cell> checkedTrees = new ArrayList<>();

        for (Cell key : assignments.keySet()) {
            if (key.getTrees() == null)
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
                if (assignedVdNeighborsWithoutTreeWhichHaveSetTent.size() < 1) {
                    //System.out.println("EveryTree Is not Consistent: Z" + tree.getRow() + " S" + tree.getCol());
                    return false;
                }

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

                if(assignedVdNeighborsWithoutTreeWhichHaveSetTentAndOtherTreeAsNeighbor.size() == 1){
                    for(Cell onlyTentOfTreeWhichHasMoreTrees : assignedVdNeighborsWithoutTreeWhichHaveSetTentAndOtherTreeAsNeighbor.keySet()){
                        ArrayList<Cell> treesToCheck = new ArrayList<>(onlyTentOfTreeWhichHasMoreTrees.getTrees());
                        treesToCheck.remove(tree);
                        for(Cell treesOfOnlyTentOfTreeWhichHasMoreTrees: treesToCheck){
                            Map<Cell, Integer> assignedVhNeighborOfTree2 = assignments.entrySet().stream()
                                    .filter(x -> x.getKey().getTrees() != null)
                                    .filter(x -> x.getKey().getTrees().contains(treesOfOnlyTentOfTreeWhichHasMoreTrees))
                                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                            if(treesOfOnlyTentOfTreeWhichHasMoreTrees.getHvNeighborsWithoutTrees().size() > assignedVhNeighborOfTree2.size())
                                continue;

                            Map<Cell, Integer> assignedVhNeighborOfTreeWhichAreTents2 = assignedVhNeighborOfTree2.entrySet().stream()
                                    .filter(x -> assignments.get(x.getKey()) == 1) //1 means there is a tent
                                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                            if(assignedVhNeighborOfTreeWhichAreTents2.size() < 2){
                                return false; //When tree has only one tent as neighbor, then this tent needs to have at lest two tents as neighbors when each of its neighbors are assigned
                            }
                        }
                    }
                }

                //for each of them, check for every of their tents if their trees or the trees of the tents of them have tent for itself (recursive)
                Boolean workedForOneTent = false;
                for (Cell cell : assignedVdNeighborsWithoutTreeWhichHaveSetTentAndOtherTreeAsNeighbor.keySet()) {
                    workedForOneTent = recursiveAction(cell, tree, assignments);
                }
                if (!workedForOneTent) {
                    //System.out.println("EveryTree Is not Consistent: Z" + tree.getRow() + " S" + tree.getCol());
                    return false;
                }
            }
        }
        return true;
    }


    private boolean recursiveAction(Cell tent, Cell tree, Map<Cell, Integer> assignments) {
            ArrayList<Cell> treesOfTent = new ArrayList<>(tent.getTrees());
            treesOfTent.remove(tree); //remove tree as it is already checked
            for (Cell treeTmp : treesOfTent) {
                Map<Cell, Integer> assignedVhNeighborOfTree = assignments.entrySet().stream()
                        .filter(x -> x.getKey().getTrees() != null)
                        .filter(x -> x.getKey().getTrees().contains(treeTmp))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                if (treeTmp.getHvNeighborsWithoutTrees().size() > assignedVhNeighborOfTree.size()) {
                     //just means that it could be true in future so no constraint violation yet
                    return true;
                }
                Map<Cell, Integer> assignedVhNeighborOfTreeWhichAreTents = assignedVhNeighborOfTree.entrySet().stream()
                        .filter(x -> assignments.get(x.getKey()) == 1) //1 means there is a tent
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


                //Filter assigned neighbors to get all neigbhors which are a tent and which havent any other trees as neighbors
                Map<Cell, Integer> assignedVdNeighborsWithoutTreeWhichHaveSetTentAndNoOtherTreeAsNeighbor = assignedVhNeighborOfTreeWhichAreTents.entrySet().stream()
                        .filter(x -> x.getKey().getTrees().size() == 1)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                //When there are any, then true, otherwise recursive check the other neighbors
                if (assignedVdNeighborsWithoutTreeWhichHaveSetTentAndNoOtherTreeAsNeighbor.size() > 0) {
                    return true;
                } else {
                    Map<Cell, Integer> assignedVhNeighborOfTreeWhichAreTentsAndHaveOtherNeighbors = assignedVhNeighborOfTreeWhichAreTents.entrySet().stream()
                            .filter(x -> x.getKey().getTrees().size() > 1)
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                    Boolean tmp = false;
                    ArrayList<Cell> addedCells = new ArrayList<>();
                    for (Cell tent2 : assignedVhNeighborOfTreeWhichAreTentsAndHaveOtherNeighbors.keySet()) {
                        if(checkedCells.contains(tent2))
                            continue;
                        checkedCells.add(tent2);
                        addedCells.add(tent2);
                        if(recursiveAction(tent2, treeTmp, assignments)){
                            tmp = true;
                        }
                    }
                    checkedCells.removeAll(addedCells);
                    if(tmp)
                        return true;
                }
            }
        return false;
    }
}
