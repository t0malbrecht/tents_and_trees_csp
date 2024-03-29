package tentsAndTrees;

import csp.Backtracking;
import services.CSVReader;

import java.io.IOException;
import java.util.ArrayList;

public class Runner {
    static String[] result = new String[10];
    public static void main(String[] args){
        Object[] results;
        try {

            for(int i=0; i<10; i++){
                CSVReader.setCsvFilePath(i);
                Object[] info = CSVReader.read();
                Grid grid = new Grid((Cell[][]) info[0], (ArrayList<Integer>) info[1], (ArrayList<Integer>) info[2]);

                Backtracking backtracking = new Backtracking(grid);
                String counter = backtracking.start();
                System.out.println("Rätsel: "+i+" Anzahl an Zügen: "+counter);
            }

            /**CSVReader.setCsvFilePath(13);
             Object[] info2 = CSVReader.read();
             Grid grid2 = new Grid((Cell[][]) info2[0], (ArrayList<Integer>) info2[1], (ArrayList<Integer>) info2[2]);
             ArrayList<Cell> cellsList = new ArrayList<>();
             List<Cell> cellsListFiltered = grid2.getOpenCells().stream()
             .filter(Cell::isTent)
             .collect(Collectors.toList());
             HashMap<Cell, Integer> assignments = new HashMap<>();
             for(Cell celltmp: cellsListFiltered){
             assignments.put(celltmp, 1);
             }
             for(Cell celltmp: grid2.getOpenCells()){
             if(!assignments.containsKey(celltmp)){
             assignments.put(celltmp, 0);
             }
             }
             Assignment assignment = new Assignment(grid2, assignments);
             assignment.printField();
             System.out.println(assignment.isConsistent());**/

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
