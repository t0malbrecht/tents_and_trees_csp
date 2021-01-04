package tentsAndTrees;

import csp.AbstractConstraint;
import tentsAndTrees.constraints.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Grid {
    private ArrayList<Integer> columnsTents;
    private ArrayList<Integer> rowTents;
    private Cell[][] cells;
    private ArrayList<Cell> cellsWithTrees = new ArrayList<>();
    private ArrayList<Cell> cellsWithoutTrees = new ArrayList<>();
    private ArrayList<Cell> openCells;

    private AbstractConstraint[] constraints;

    public Grid(Cell[][] cells, ArrayList<Integer> columnsTents, ArrayList<Integer> rowTents){
        this.cells = cells;
        this.columnsTents = columnsTents;
        this.rowTents = rowTents;
        setCellsWithTrees();
        setCellsWithoutTrees();
        //initiallySetDomains();
        prefilterSetDomains();
        setVhdNeighborsForCells();
        openCells = new ArrayList<>(cellsWithoutTrees);
        openCells.removeIf(obj -> obj.getDomain().size() == 1);; // reduce Size of Variables
        constraints = new AbstractConstraint[] {new columnConstraint(), new EveryTreeHasATentConstraint(), new rowConstraint(), new TentsCannotBePlacedNextToEachotherConstraint(), new EveryTentNeedsATreeConstraint()};
    }

    public void removeVariablesWithOnlyOneDomain(){
        ArrayList<Cell> filteredList = new ArrayList<>(openCells);
        filteredList.removeIf(obj -> obj.getDomain().size() == 1);
        for(Cell cell: filteredList){
                openCells.remove(cell);
        }
    }

    private void initiallySetDomains(){
        for(Cell cell: cellsWithTrees){
            ArrayList<Cell> tmp = new ArrayList<>();
            int row = cell.getRow();
            int col = cell.getCol();

            if(!(row-1 < 0))
                tmp.add(cells[row-1][col]);
            if(!(row+1 > cells.length-1))
                tmp.add(cells[row+1][col]);
            if(col > 0)
                tmp.add(cells[row][col-1]);
            if(col < cells[0].length-1)
                tmp.add(cells[row][col+1]);

            ArrayList<Cell> vdNeighborsWithoutTrees = new ArrayList<Cell>(tmp);
            vdNeighborsWithoutTrees.removeIf(Cell::isTree);
            cell.setHvNeighborsWithoutTrees(vdNeighborsWithoutTrees);
            tmp.removeIf(obj -> !obj.isTree());

            if(tmp.size() > 0){
                cell.setTrees(tmp);
            }else{
                cell.setTrees(null);
            }
            cell.initiallySetDomains(new int[] {0,1});
        }
    }


      private void prefilterSetDomains(){
              for(Cell cell: cellsWithTrees){
                  ArrayList<Cell> tmp = new ArrayList<>();
                  int row = cell.getRow();
                  int col = cell.getCol();

                  if(!(row-1 < 0))
                  tmp.add(cells[row-1][col]);
                  if(!(row+1 > cells.length-1))
                      tmp.add(cells[row+1][col]);
                  if(col > 0)
                      tmp.add(cells[row][col-1]);
                  if(col < cells[0].length-1)
                      tmp.add(cells[row][col+1]);

                  ArrayList<Cell> vdNeighborsWithoutTrees = new ArrayList<Cell>(tmp);
                  vdNeighborsWithoutTrees.removeIf(Cell::isTree);
                  cell.setHvNeighborsWithoutTrees(vdNeighborsWithoutTrees);
                  tmp.removeIf(obj -> !obj.isTree());

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
        for(Cell cell: cellsWithoutTrees){
            ArrayList<Cell> tmp = new ArrayList<>();
            int row = cell.getRow();
            int col = cell.getCol();

            if(!(row-1 < 0)){
                tmp.add(cells[row-1][col]);
                if(col > 0)
                    tmp.add(cells[row-1][col-1]);
                if(col < cells[0].length-1)
                    tmp.add(cells[row-1][col+1]);

            }
            if(!(row+1 > cells.length-1)){
                tmp.add(cells[row+1][col]);
                if(col > 0)
                    tmp.add(cells[row+1][col-1]);
                if(col < cells[0].length-1)
                    tmp.add(cells[row+1][col+1]);
            }
            if(col > 0)
                tmp.add(cells[row][col-1]);
            if(col < cells[0].length-1)
                tmp.add(cells[row][col+1]);

            tmp.removeIf(Cell::isTree);
            cell.setHvdNeighborsWithoutTrees(tmp);
        }
    }

    //Getter and Setter
    public ArrayList<Cell> getOpenCells() {
        return openCells;
    }

    public void setOpenCells(ArrayList<Cell> openCells) {
        this.openCells = openCells;
    }

    public ArrayList<Integer> getColumnsTents() {
        return columnsTents;
    }

    public ArrayList<Integer> getRowTents() {
        return rowTents;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public AbstractConstraint[] getConstraints() {
        return constraints;
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
