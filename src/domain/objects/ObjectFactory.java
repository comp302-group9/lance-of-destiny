package domain.objects;

import domain.models.RunningModeModel;
import domain.objects.Barrier.*;
import domain.objects.Spells.*;
import ui.screens.RModeUI.SpellIcon;

import java.util.ArrayList;
import java.util.List;

public class ObjectFactory {

    // Singleton instance
    private static ObjectFactory instance;

    // Private constructor to prevent instantiation
    private ObjectFactory() {}

    // Public method to provide access to the instance
    public static synchronized ObjectFactory getInstance() {
        if (instance == null) {
            instance = new ObjectFactory();
        }
        return instance;
    }

    // Methods to create game objects
    public Barrier createBarrier(String type, int x, int y) {
        switch (type) {
            case "SimpleBarrier":
                return new SimpleBarrier(x, y);
            case "ReinforcedBarrier":
                return new ReinforcedBarrier(x, y);
            case "ExplosiveBarrier":
                return new ExplosiveBarrier(x, y);
            case "RewardingBarrier":
                return new RewardingBarrier(x, y);
            default:
                throw new IllegalArgumentException("Unknown barrier type: " + type);
        }
    }

    public Spell createSpell(String type, Object context) {
        switch (type) {
            case "DoubleAccel":
                return new DoubleAccel((Fireball) context);
            case "Expension":
                return new Expension((Paddle) context);
            case "Felicis":
                return new Felicis((RunningModeModel) context);
            case "Hex":
                return new Hex((Paddle) context);
            case "InfiniteVoid":
                return new InfiniteVoid();
            case "Overwhelm":
                return new Overwhelm((Fireball) context);
            default:
                throw new IllegalArgumentException("Unknown spell type: " + type);
        }
    }

    public Paddle createPaddle(int x, int y, int width, int height) {
        return new Paddle(x, y, width, height);
    }

    public Fireball createFireball(int x, int y, int width, int height) {
        return new Fireball(x, y, width, height);
    }

    public List<SpellIcon> createSpellIcons(RunningModeModel model) {
        List<SpellIcon> spellIcons = new ArrayList<>();
        spellIcons.add(new SpellIcon(new Overwhelm(model.getFireball())));
        spellIcons.add(new SpellIcon(new Hex(model.getPaddle())));
        spellIcons.add(new SpellIcon(new Expension(model.getPaddle())));
        spellIcons.add(new SpellIcon(new Felicis(model)));
        spellIcons.add(new SpellIcon(new DoubleAccel(model.getFireball())));
        spellIcons.add(new SpellIcon(new InfiniteVoid()));
        return spellIcons;
    }

    public List<SpellIcon> createSpellIcons2(RunningModeModel model) {
        List<SpellIcon> spellIcons = new ArrayList<>();
        spellIcons.add(new SpellIcon(new Overwhelm(model.getFireball())));
        spellIcons.add(new SpellIcon(new Hex(model.getPaddle())));
        spellIcons.add(new SpellIcon(new Expension(model.getPaddle())));
        spellIcons.add(new SpellIcon(new Felicis(model)));
        return spellIcons;
    }

    public List<Spell> createSpellIcons3(RunningModeModel model) {
        List<Spell> spell = new ArrayList<>();
        spell.add(new DoubleAccel(model.getFireball()));
        spell.add(new InfiniteVoid());
        return spell;
    }
}
