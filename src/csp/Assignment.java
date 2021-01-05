package csp;

import tentsAndTrees.Cell;
import tentsAndTrees.Grid;
import tentsAndTrees.constraints.EveryTreeHasATentConstraint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Assignment {
    private HashMap<Cell, Integer> assignments;
    private Grid grid;
    private ArrayList<AbstractConstraint> constraints;

        public Assignment(Grid grid, HashMap<Cell, Integer> assignments){
            this.assignments = assignments;
            this.grid = grid;
        }

        public Assignment(Assignment assignments){
            this.assignments = new HashMap<>(assignments.assignments);
            this.grid = assignments.grid;
            this.constraints = assignments.constraints;
        }

        public boolean isConsistent() {
           //printField();
           for(AbstractConstraint constraint: grid.getConstraints()){
               if(!constraint.isConsistent(assignments, grid)){
                   if(constraint.getClass() == EveryTreeHasATentConstraint.class){
                       printField();
                   }
                   return false;
               }
           }
           return true;
        }

        public boolean isComplete(ArrayList<Cell> initialOpenCells){
            return initialOpenCells.size() == assignments.size();
        }

        public void setAssignments(Cell cell, Integer value){
            this.assignments.put(cell, value);
        }

        public void removeAssignments(Cell cell){
            this.assignments.remove(cell);
        }

        public HashMap<Cell, Integer> getAssignments(){
            return assignments;
        }

    public void printField(){

        System.out.print("   |");
        for(Integer column: grid.getColumnsTents()){
            if(Integer.toString(column).length() == 1)
                System.out.print(" "+column+" |");
            else
                System.out.print(column+" ");
        }
        System.out.println();
        int tmp = 0;
        for(Integer row : grid.getRowTents()){
            if(Integer.toString(row).length() == 1)
                System.out.print(" "+row+" |");
            else
                System.out.print(row+" |");
            for(Cell cell: grid.getCells()[tmp]){
                if(cell.isTree())
                    System.out.print(" T |");
                else if(assignments.containsKey(cell) && assignments.get(cell)==  1)
                    System.out.print(" Z |");
                else if(assignments.containsKey(cell) && assignments.get(cell) == 0)
                    System.out.print(" N |");
                else
                    System.out.print("   |");
            }
            System.out.println();
            tmp++;
        }
        System.out.println();
    }

}
