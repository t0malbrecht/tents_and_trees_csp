package csp;

public class ConstraintPropagation {

    /**
     *  private void updateDomainsForNeighborsAfterSetTent(){
     *         for(Cell neighbor: this.hvdNeighborsWithoutTrees){
     *             neighbor.removeOptionFromDomains(1);
     *         }
     *     }
     *
     *     private void updateDomainsForNeighborsAfterSetNothing(){
     *         if(this.trees == null)
     *             return;
     *         for(Cell tree: this.trees){
     *             tree.updateDomainForNeighborsAsTree();
     *         }
     *     }
     */

    /**
     * public void updateDomainForNeighborsAsTree(){
     *         if(!this.isTree)
     *             return;
     *         ArrayList<Cell> cellsWhichCanBeTent = new ArrayList<>(hvNeighborsWithoutTrees);
     *         cellsWhichCanBeTent.removeIf(obj -> obj.isSet);
     *         cellsWhichCanBeTent.removeIf(obj -> !obj.domains.contains(1));
     *         if(cellsWhichCanBeTent.size() == 1)
     *             cellsWhichCanBeTent.get(0).removeOptionFromDomains(0);
     *     }
     */

    /**
     * private void initiallySetDomains(){
     *         for(Cell cell: cellsWithTrees){
     *             ArrayList<Cell> tmp = new ArrayList<>();
     *             int row = cell.getRow();
     *             int col = cell.getCol();
     *
     *             if(!(row-1 < 0))
     *                 tmp.add(cells[row-1][col]);
     *             if(!(row+1 > rowTents.size()-1))
     *                 tmp.add(cells[row+1][col]);
     *             if(col > 1)
     *                 tmp.add(cells[row][col-1]);
     *             if(col < cells.length)
     *                 tmp.add(cells[row][col+1]);
     *
     *             ArrayList<Cell> vdNeighborsWithoutTrees = new ArrayList<Cell>(tmp);
     *             cell.setHvNeighborsWithoutTrees(vdNeighborsWithoutTrees);
     *             System.out.println(tmp.size());
     *             tmp.removeIf(obj -> !obj.isTree());
     *             System.out.println(tmp.size());
     *             if(tmp.size() > 0){
     *                 cell.initiallySetDomains(new int[]{0, 1});
     *                 cell.setTrees(tmp);
     *             }else{
     *                 cell.initiallySetDomains(new int[]{0});
     *                 cell.setTrees(null);
     *             }
     *         }
     *     }
     */


    /**
     *    Cell[][] cells = Arrays.stream(grid.getCells()).map(Cell[]::clone).toArray(Cell[][]::new);
     *         for (Cell[] row: cells) {
     *             for(Cell cell: row){
     *                 if(assignments.containsKey(cell)){
     *                     if(assignments.get(cell) == 1)
     *                         cell.isTree();
     *                 }
     *             }
     *
     *         }
     */
}
