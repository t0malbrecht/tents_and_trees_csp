package csp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class Grid {
    private ArrayList<Integer> columnsTents;
    private ArrayList<Integer> rowTents;
    private Cell[][] cells;
    private ArrayList<Cell> cellsWithTrees = new ArrayList<>();
    private ArrayList<Cell> cellsWithoutTrees = new ArrayList<>();
    private ArrayList<Cell> openCells;

    public Grid(Cell[][] cells, ArrayList<Integer> columnsTents, ArrayList<Integer> rowTents){
        this.cells = cells;
        this.columnsTents = columnsTents;
        this.rowTents = rowTents;
        setCellsWithTrees();
        setCellsWithoutTrees();
        initiallySetDomains();
        openCells = new ArrayList<>(cellsWithoutTrees);
        setVhdNeighborsForCells();
    }
    public void startSolvingMCV(){
        ArrayList<Cell> filteredList = new ArrayList<>(openCells);
        filteredList.removeIf(obj -> obj.getDomainSize() == 1);
        while(filteredList.size() > 0){
            for(Cell cell: filteredList){
                cell.setSet();
                openCells.remove(cell);
            }
            filteredList = new ArrayList<>(openCells);
            filteredList.removeIf(obj -> obj.getDomainSize() == 1);
        }
    }

    private void initiallySetDomains(){
        for(Cell cell: cellsWithTrees){
            ArrayList<Cell> tmp = new ArrayList<>();
            int row = cell.getRow();
            int col = cell.getCol();

            if(!(row-1 < 0))
                tmp.add(cells[row-1][col]);
            if(!(row+1 > rowTents.size()-1))
                tmp.add(cells[row+1][col]);
            if(col > 1)
                tmp.add(cells[row][col-1]);
            if(col < cells.length)
                tmp.add(cells[row][col+1]);

            ArrayList<Cell> vdNeighborsWithoutTrees = new ArrayList<Cell>(tmp);
            cell.setVdNeighborsWithoutTrees(vdNeighborsWithoutTrees);
            System.out.println(tmp.size());
            tmp.removeIf(obj -> !obj.isTree());
            System.out.println(tmp.size());
            if(tmp.size() > 0){
                cell.initiallySetDomains(new int[]{0, 1});
                cell.setTrees(tmp);
            }else{
                cell.initiallySetDomains(new int[]{0});
                cell.setTrees(null);
            }
        }
    }

    private void setCellsWithTrees(){
        for(Cell[] row: cells){
            cellsWithTrees.addAll(Arrays.asList(row));
        }
    }

    public void setCellsWithoutTrees(){
        for(Cell[] row: cells){
            for(Cell cell: row){
                if(!cell.isTree())
                    cellsWithoutTrees.add(cell);
            }
        }
    }

    private void setVhdNeighborsForCells(){
        for(Cell cell: openCells){
            ArrayList<Cell> tmp = new ArrayList<>();
            int row = cell.getRow();
            int col = cell.getCol();

            if(!(row-1 < 0)){
                tmp.add(cells[row-1][col]);
                if(col > 1)
                    tmp.add(cells[row-1][col-1]);
                if(col < cells.length)
                    tmp.add(cells[row-1][col+1]);

            }
            if(!(row+1 > rowTents.size()-1)){
                tmp.add(cells[row+1][col]);
                if(col > 1)
                    tmp.add(cells[row+1][col-1]);
                if(col < cells.length)
                    tmp.add(cells[row+1][col+1]);
            }
            if(col > 1)
                tmp.add(cells[row][col-1]);
            if(col < cells.length)
                tmp.add(cells[row][col+1]);

            tmp.removeIf(Cell::isTree);
            cell.setHvdNeighborsWithoutTrees(tmp);
        }
    }


    /**
     * Debug Help-Methods
     */
    public void printField(){
        System.out.print("   |");
        for(Integer column: columnsTents){
            if(Integer.toString(column).length() == 1)
                System.out.print(" "+column+" |");
            else
                System.out.print(column+" ");
        }
        System.out.println();
        int tmp = 0;
        for(Integer row : rowTents){
            if(Integer.toString(row).length() == 1)
                System.out.print(" "+row+" |");
            else
                System.out.print(row+" |");
            for(Cell cell: cells[tmp]){
                if(cell.isTree())
                    System.out.print(" T |");
                else if(cell.isTent())
                    System.out.print(" Z |");
                else
                    System.out.print("   |");
            }
            System.out.println();
            tmp++;
        }
        System.out.println("Offene Zellen: "+openCells.size());
        System.out.println();
    }

}
