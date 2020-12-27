package csp;

import java.util.ArrayList;

public class Grid {
    private ArrayList<Integer> columnsTents;
    private ArrayList<Integer> rowTents;
    private Cell[][] cells;

    public Grid(Cell[][] cells, ArrayList<Integer> columnsTents, ArrayList<Integer> rowTents){
        this.cells = cells;
        this.columnsTents = columnsTents;
        this.rowTents = rowTents;

    }

}
