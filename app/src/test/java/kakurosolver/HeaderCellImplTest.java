package kakurosolver;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HeaderCellImplTest {
    private HeaderCellImpl headerCell;

    @Before
    public void setup() {
        headerCell = new HeaderCellImpl();
    }

    @Test
    public void getRowTotal() {
        assertTrue(headerCell.getRowTotal().isEmpty());

        headerCell.setRowTotal(16);

        assertEquals(16, headerCell.getRowTotal().get().intValue());
    }

    @Test
    public void getColumnTotal() {
        assertTrue(headerCell.getColumnTotal().isEmpty());

        headerCell.setColumnTotal(23);

        assertEquals(23, headerCell.getColumnTotal().get().intValue());
    }
}