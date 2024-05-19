package testing;

import domain.objects.Spells.Expension;
import domain.objects.Spells.Spell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.Timer;

import static org.junit.jupiter.api.Assertions.*;

class SpellTest {
    private Spell spell;

    @BeforeEach
    void setUp() {
        spell = new Expension();
    }

    // Black-Box Tests
    @Test
    void testStartStopTimer() {
        // Requires: spell object to be initialized.
        // Modifies: the active state of the spell.
        // Effects: the spell's active state is true after starting the timer, and false after stopping the timer.
        spell.startTimer();
        assertTrue(spell.getActive());

        spell.stopTimer();
        assertFalse(spell.getActive());
    }

    @Test
    void testResetTimer() {
        // Requires: spell object to be initialized and timer to be started.
        // Modifies: the secondsElapsed field of the spell.
        // Effects: the secondsElapsed field is reset to 0.
        spell.startTimer();
        spell.resetTimer();
        assertEquals(0, spell.getSecondsElapsed());
    }

    // White-Box Tests
    @Test
    void testTimerLogic() throws NoSuchFieldException, IllegalAccessException {
        // Requires: spell object to be initialized.
        // Modifies: the secondsElapsed and active state of the spell.
        // Effects: the secondsElapsed field reaches the duration, the active state becomes false, and the number of uses decreases.
        spell.startTimer();

        // Simulate timer action for duration + 1 seconds
        Timer timer = (Timer) spell.getTimer();
        for (int i = 0; i < 15; i++) {
            timer.getActionListeners()[0].actionPerformed(null);
        }

        assertEquals(15, spell.getSecondsElapsed());
        for (int i = 0; i < 16; i++) {
            timer.getActionListeners()[0].actionPerformed(null);
        }
        assertFalse(spell.getActive());
    }

    @Test
    void testSpellStateTransitions() {
        // Requires: spell object to be initialized.
        // Modifies: the active state of the spell.
        // Effects: the spell's active state becomes true after activation and false after deactivation.
        spell.setActive(true);
        assertTrue(spell.getActive());

        spell.deActivate();
        assertFalse(spell.getActive());
    }
}

