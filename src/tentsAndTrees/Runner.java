package tentsAndTrees;

import csp.Backtracing;
import services.CSVReader;
import tentsAndTrees.Cell;
import tentsAndTrees.Grid;

import java.io.IOException;
import java.util.ArrayList;

public class Runner {
    public static void main(String[] args){
        Object[] results;
        try {
            CSVReader.setCsvFilePath(10);
            Object[] info = CSVReader.read();
            Grid grid = new Grid((Cell[][]) info[0], (ArrayList<Integer>) info[1], (ArrayList<Integer>) info[2]);
            Backtracing backtracing = new Backtracing(grid);
            backtracing.start();

            //grid.printField();
            //grid.startSolvingMCV();
            //grid.printField();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
