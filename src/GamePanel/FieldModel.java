package GamePanel;

import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class FieldModel extends AbstractTableModel {
    private final FieldCell[][] cells;
    private final int columns;
    private final int rows;
    private ArrayList<FieldCell> roads;
    private final FieldCell[] teleports = new FieldCell[2];

    private final Point spawner_location;
    private final int[][] spawner = {
            {FieldCell.WALL, FieldCell.WALL, FieldCell.EXTRA_WALL, FieldCell.EXTRA_WALL, FieldCell.WALL, FieldCell.WALL},
            {FieldCell.WALL, FieldCell.VOID, FieldCell.VOID, FieldCell.VOID, FieldCell.VOID, FieldCell.WALL},
            {FieldCell.WALL, FieldCell.VOID, FieldCell.VOID, FieldCell.VOID, FieldCell.VOID, FieldCell.WALL},
            {FieldCell.WALL, FieldCell.WALL, FieldCell.WALL, FieldCell.WALL, FieldCell.WALL, FieldCell.WALL}
    };


    FieldModel(int columns, int rows){
        this.columns = columns + 2;
        this.rows = rows + 2;
        this.cells = new FieldCell[this.rows][this.columns];
        this.spawner_location = new Point(
                (int) (Math.random() * (this.columns - spawner.length - 2)) + 1,
                (int) (Math.random() * (this.rows - spawner[0].length - 2)) + 2
        );
        this.roads = new ArrayList<>();

        createField();
        initTeleport();
        setGhostSpawner();
        initWalls();
        checkWays();
        initRoads();
        initBuffPoints();
        initCells();
    }

    @Override
    public int getRowCount() {
        return rows;
    }

    @Override
    public int getColumnCount() {
        return columns;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return cells[rowIndex][columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(aValue instanceof FieldCell) {
            cells[rowIndex][columnIndex] = (FieldCell) aValue;
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return FieldCell.class;
    }

    private void createField(){
        for(int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++) {
                FieldCell cell = new FieldCell(j, i);
                cell.setCellType(FieldCell.EMPTY_ROAD);
                cells[i][j] = cell;
            }
        }

        for(int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {

                if (i > 0) {
                    cells[i][j].north = cells[i - 1][j];
                    cells[i-1][j].south = cells[i][j];
                }
                else
                    cells[i][j].setCellType(FieldCell.WALL);

                if (j > 0) {
                    cells[i][j].west = cells[i][j - 1];
                    cells[i][j-1].east = cells[i][j];
                }
                else
                    cells[i][j].setCellType(FieldCell.WALL);


                if (i >= rows - 1)
                    cells[i][j].setCellType(FieldCell.WALL);

                if (j >= columns - 1)
                    cells[i][j].setCellType(FieldCell.WALL);
            }
        }
    }

    private void initTeleport(){
        if(Math.random() > 0.5){
            int y1 = (int)(Math.random() * (this.rows -2)) + 1;
            int y2 = (int)(Math.random() * (this.rows -2)) + 1;

            cells[y1][0].setCellType(FieldCell.BRIDGE_ROAD);
            cells[y2][columns -1].setCellType(FieldCell.BRIDGE_ROAD);

            teleports[0] = cells[y1][0];
            teleports[1] = cells[y2][columns -1];
        }
        else {
            int x1 = (int)(Math.random() * (this.columns -2)) + 1;
            int x2 = (int)(Math.random() * (this.columns -2)) + 1;

            cells[0][x1].setCellType(FieldCell.BRIDGE_ROAD);
            cells[rows -1][x2].setCellType(FieldCell.BRIDGE_ROAD);

            teleports[0] = cells[0][x1];
            teleports[1] = cells[rows -1][x2];
        }
    }

    private void setGhostSpawner(){
        for(int i = 0; i < spawner.length; i++)
            for (int j = 0; j < spawner[0].length; j++)
                cells[i + spawner_location.y][j + spawner_location.x].setCellType(spawner[i][j]);
    }

    private void initWalls(){
        for(int i = 1; i < rows -1; i++){
            for (int j = 1; j < columns -1; j++){
                FieldCell cell = cells[i][j];
                if(cell.isEmpty() && cell.canBeWall() && Math.random() > 0.5) {
                    cell.setCellType(FieldCell.WALL);
                }
            }
        }
    }

    public void initRoads() {
        roads = new ArrayList<>();

        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < columns - 1; j++) {
                FieldCell cell = cells[i][j];

                if (cell.getCellType() == FieldCell.EMPTY_ROAD) {
                    cell.setCellType(FieldCell.POINT_ROAD);
                    setValueAt(cell, i, j);
                    roads.add(cell);
                } else if(!roads.contains(cell) && cell.getCellType() == FieldCell.POINT_ROAD)
                    roads.add(cell);
            }
        }
    }

    public void initBuffPoints(){
        for(int i = 1; i < getRowCount()-1; i += 2){
            FieldCell cell = getRandomRoad(i);
            cell.setCellType(FieldCell.BUFF_ROAD);
            setValueAt(cell, cell.getLocation().x, cell.getLocation().y);
        }
    }

    private void checkWays(){
        HashSet<FieldCell> roads = findRoads();
        HashSet<FieldCell> closed = findClosed(new HashSet<>(), roads.iterator().next());
        while (closed.size() < roads.size()) {
            makePath(closed, roads);
        }
        findClosed(new HashSet<>(), closed.iterator().next());
    }

    private HashSet<FieldCell> findRoads(){
        HashSet<FieldCell> roads = new HashSet<>();
        for(FieldCell[] list : cells)
            for(FieldCell elem : list)
                if(elem.isRoad())
                    roads.add(elem);
        return roads;
    }

    private HashSet<FieldCell> findClosed(HashSet<FieldCell> visited, FieldCell current){
        visited.add(current);
        for (FieldCell cell : current.getNeighbours()) {
            if(cell != null && !visited.contains(cell) && cell.isRoad()) {
                visited = findClosed(visited, cell);
            }
        }

        return visited;
    }

    private void makePath(HashSet<FieldCell> closed_cells, HashSet<FieldCell> roads) {
        ArrayList<ArrayList<FieldCell>> nearest = detectNearestRoad(closed_cells);
        closed_cells.addAll(findClosed(new HashSet<>(), nearest.get(0).get(1)));
        FieldCell start = null;

        Point firstLoc = nearest.get(0).get(0).getLocation();
        Point secondLoc = nearest.get(0).get(1).getLocation();

        if(firstLoc.x == secondLoc.x && nearest.get(0).get(0).canBeReplaced())
            start = nearest.get(0).get(0).east;
        else {
            if(firstLoc.x > secondLoc.x && nearest.get(0).get(0).north.canBeReplaced())
                start = nearest.get(0).get(0).north;
            else if(nearest.get(0).get(0).south.canBeReplaced())
                start = nearest.get(0).get(0).south;
        }

        if(start != null) {

            ArrayList<FieldCell> path = new ArrayList<>();

            path = getPath(path, start, nearest.get(0).get(1));

            closed_cells.add(start);
            for (FieldCell cell : path) {
                cell.setCellType(FieldCell.EMPTY_ROAD);
                roads.add(cell);
                closed_cells.add(cell);
            }
        }
    }

    public ArrayList<FieldCell> getPath(ArrayList<FieldCell> array, FieldCell current, FieldCell end){
        array.add(current);
        if(!current.equals(end)){
            double[] vectors = new double[]{
                    getVectorLength(current.north.getLocation(), end.getLocation()),
                    getVectorLength(current.east.getLocation(), end.getLocation()),
                    getVectorLength(current.south.getLocation(), end.getLocation())
            };

            if(current.north.canBeReplaced() && vectors[0] <= vectors[1] && vectors[0] <= vectors[2])
                array = getPath(array, current.north, end);
            else if(current.east.canBeReplaced() && vectors[1] <= vectors[0] && vectors[1] <= vectors[2])
                array = getPath(array, current.east, end);
            else if(current.south.canBeReplaced() && vectors[2] <= vectors[1] && vectors[2] <= vectors[0])
                array = getPath(array, current.south, end);
        }
        return array;
    }

    public double getVectorLength(Point p1, Point p2){
        return Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
    }

    private ArrayList<ArrayList<FieldCell>> detectNearestRoad(HashSet<FieldCell> closed) {
        ArrayList<ArrayList<FieldCell>> fieldCells = new ArrayList<>();
        boolean found = false;

        for(int i = 1; i < Math.max(columns, rows) && !found; i++){
            for(Iterator<FieldCell> j = closed.iterator(); j.hasNext() && !found; ){
                FieldCell cell;
                if((cell = j.next()).getNearWallsCount() > 0) {
                    FieldCell nearest = findRoadInRadius(cell, i, closed);
                    if (nearest != null) {
                        found = true;
                        fieldCells.add(new ArrayList<>());
                        fieldCells.get(0).add(cell.getLocation().y <= nearest.getLocation().y ? cell : nearest);
                        fieldCells.get(0).add(fieldCells.get(0).get(0).equals(nearest) ? cell : nearest);
                    }
                }
            }
        }

        fieldCells.add(new ArrayList<>());
        int direction = fieldCells.get(0).get(0).getLocation().x <= fieldCells.get(0).get(1).getLocation().x ? 1 : -1;

        for(int i = fieldCells.get(0).get(0).getLocation().x, i2 = fieldCells.get(0).get(1).getLocation().x; (direction == 1) ? i <= i2 : i >= i2; i += direction){
            for(int j = fieldCells.get(0).get(0).getLocation().y, j2 = fieldCells.get(0).get(1).getLocation().y; j < j2; j++){
                if(cells[i][j].getCellType() == FieldCell.WALL)
                    fieldCells.get(1).add(cells[i][j]);
            }
        }

        return fieldCells;
    }

    private FieldCell findRoadInRadius(FieldCell current, int radius, HashSet<FieldCell> ignored){
        int x1 = current.getLocation().y - radius;
        int x2 = current.getLocation().y + radius;
        int y1 = current.getLocation().x - radius;
        int y2 = current.getLocation().x + radius;
        if(x1 <= 0) x1 = 1;
        if(y1 <= 0) y1 = 1;
        if(x2 >= columns) x2 = columns - 1;
        if(y2 >= rows) y2 = rows - 2;


        FieldCell cell;
        for(int i = x1; i <= x2; i++){
            cell = cells[y1][i];
            if(cell.isRoad() && !cell.equals(current) && !ignored.contains(cell))
                return cell;
        }

        for(int i = x1; i <= x2; i++){
            cell = cells[y2][i];
            if(cell.isRoad() && !cell.equals(current) && !ignored.contains(cell))
                return cell;
        }

        for(int i = y1+1; i < y2; i++){
            cell = cells[i][x1];
            if(cell.isRoad() && !cell.equals(current) && !ignored.contains(cell))
                return cell;
        }
        for(int i = y1+1; i < y2; i++){
            cell = cells[i][x2];
            if(cell.isRoad() && !cell.equals(current) && !ignored.contains(cell))
                return cell;
        }

        return null;
    }

    private void initCells(){
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < columns; j++)
                cells[i][j].initCell();
    }

    public FieldCell getRandomRoad(){
        return roads.get((int)(Math.random() * roads.size()));
    }

    public FieldCell getRandomRoad(int rowIndex){
        ArrayList<FieldCell> roads = new ArrayList<>();
        for(FieldCell cell : cells[rowIndex]) {
            if(cell.isRoad())
                roads.add(cell);
        }
        return roads.get((int)(Math.random() * roads.size()));
    }

    public FieldCell[] getTeleports() {
        return teleports;
    }

    public ArrayList<FieldCell> getPointRoads() {
        return new ArrayList<>(roads.stream().filter(cell -> cell.getCellType() == FieldCell.POINT_ROAD || cell.getCellType() == FieldCell.BUFF_ROAD).toList());
    }

    public Rectangle getSpawnerRectangle() {
        return new Rectangle(spawner_location, new Dimension(spawner[0].length, spawner.length));
    }

    public void showFieldInConsole(){
        for(int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++){
                System.out.print(cells[i][j] + " ");
            }
            System.out.println();
        }
    }

}
