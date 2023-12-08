package GamePanel;

import java.awt.*;

public class FieldCell{
    public static final int VOID = 0;
    public static final int EMPTY_ROAD = 1;
    public static final int POINT_ROAD = 2;
    public static final int BUFF_ROAD = 3;
    public static final int BRIDGE_ROAD = 4;
    public static final int WALL = 5;
    public static final int EXTRA_WALL = 6;

    public FieldCell west;
    public FieldCell north;
    public FieldCell east;
    public FieldCell south;

    private int cell_type;
    private String cell_style = "";
    private final Point location;

    public FieldCell(int row, int column) {
        this.location = new Point(column, row);
    }

    public void setCellType(int cell_type) {
        if(cell_type >= 0 && cell_type <= 6) {
            this.cell_type = cell_type;
            initCell();
        }
        else
            throw new RuntimeException("Wrong cell type");
    }

    public void initCell(){
        cell_style = "";

        if(cell_type == WALL){
            if(west != null && west.cell_type != WALL)
                cell_style += "l";
            if(north != null && north.cell_type != WALL)
                cell_style += "t";
            if(east != null && east.cell_type != WALL)
                cell_style += "r";
            if(south != null && south.cell_type != WALL)
                cell_style += "b";
        } else if(cell_type == EXTRA_WALL)
            cell_style = "x";
        else if(cell_type == POINT_ROAD)
            cell_style = "PR";
        else if(cell_type == BUFF_ROAD)
            cell_style = "BR";
    }

    public int getNearWallsCount(){
        int count = 0;
        if(west != null && west.cell_type == WALL) count++;
        if(north != null && north.cell_type == WALL) count++;
        if(east != null && east.cell_type == WALL) count++;
        if(south != null && south.cell_type == WALL) count++;

        return count;
    }

    public boolean canBeWall(){
        if(cell_type != EMPTY_ROAD) return false;
        if(validateCell(west)) return false;
        if(validateCell(north)) return false;
        if(validateCell(east)) return false;
        return !validateCell(south);
    }

    private boolean validateCell(FieldCell cell){
        return cell != null && (cell.cell_type == BRIDGE_ROAD || cell.cell_type == EXTRA_WALL || (cell.isRoad() && cell.cell_type != FieldCell.EMPTY_ROAD && cell.getNearWallsCount() >= 2));
    }

    public int getCellType() {
        return cell_type;
    }

    public String getCellStyle() {
        return cell_style;
    }

    public boolean isRoad(){
        return cell_type == POINT_ROAD || cell_type == BUFF_ROAD || cell_type == EMPTY_ROAD;
    }

    public boolean canBeReplaced(){
        if(west == null || west.getCellType() == VOID) return false;
        if(east == null || east.getCellType() == VOID) return false;
        if(north == null || north.getCellType() == VOID) return false;
        if(south == null || south.getCellType() == VOID) return false;
        return true;
    }

    public FieldCell[] getNeighbours(){
        return new FieldCell[]{west, north, east, south};
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof FieldCell))
            return false;

        return ((FieldCell) obj).getLocation().equals(location);
    }

    public Point getLocation() {
        return location;
    }

    public boolean isEmpty(){
        return cell_type == VOID || cell_type == EMPTY_ROAD;
    }

    @Override
    public String toString() {
        //return "X:" + location.y + " Y:" + location.x;
        //return (cell_type == BRIDGE_ROAD) ? "BRIDGE_ROAD" : (cell_type == WALL) ? "WALL" : (cell_type == EXTRA_WALL) ? "EXTRA_WALL" : (cell_type == EMPTY_ROAD || cell_type == VOID) ? "VOID" : (cell_type == POINT_ROAD) ? "POINT_ROAD" : "BUFF_ROAD";
        return (cell_type == BRIDGE_ROAD) ? "=" : (cell_type == WALL) ? "#" : (cell_type == EXTRA_WALL) ? "-" : cell_type == EMPTY_ROAD ? " " : cell_type == VOID ? "O" : (cell_type == POINT_ROAD) ? "." : "0";
    }
}
