package pl.pandziak.ticketbookingapp.domain;

public enum Room {

    ROOM_A(7, 10),
    ROOM_B(5, 8),
    ROOM_C(5, 10);

    private final int columns;
    private final int rows;

    Room(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
    }

    public int getColumns() {
        return this.columns;
    }

    public int getRows() {
        return this.rows;
    }

}
