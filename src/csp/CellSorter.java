package csp;

import tentsAndTrees.Cell;

import java.util.Comparator;

public class CellSorter implements Comparator<Cell> {
    @Override
    public int compare(Cell o1, Cell o2) {
        Integer x1 = o1.getCol();
        Integer x2 = o2.getCol();
        int sComp = x1.compareTo(x2);

        if (sComp != 0) {
            return sComp;
        }

        Integer y1 = o1.getRow();
        Integer y2 = o2.getRow();
        return y1.compareTo(y2);
    }
}
