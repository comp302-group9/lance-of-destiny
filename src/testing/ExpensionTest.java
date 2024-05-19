package testing;

import domain.DEFAULT;
import domain.objects.Spells.Expension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExpensionTest {
    private Expension spell;

    @BeforeEach
    void setUp() {
        spell = new Expension();
    }

    // Black-Box Tests
    @Test
    void testActivate() {
        // Requires: spell object to be initialized.
        // Modifies: the DEFAULT.paddleWidth field.
        // Effects: the DEFAULT.paddleWidth is set to screenWidth / 5.
        spell.Activate();
        assertEquals(DEFAULT.screenWidth / 5, DEFAULT.paddleWidth);
    }

    @Test
    void testDeactivate() {
        // Requires: spell object to be initialized and activated.
        // Modifies: the DEFAULT.paddleWidth field.
        // Effects: the DEFAULT.paddleWidth is set back to screenWidth / 10.
        spell.Activate();
        spell.deActivate();
        assertEquals(DEFAULT.screenWidth / 10, DEFAULT.paddleWidth);
    }
}
