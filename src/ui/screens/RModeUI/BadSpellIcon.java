package ui.screens.RModeUI;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

import domain.objects.Spells.InfiniteVoid;
import domain.objects.Spells.Spell;
import domain.objects.Spells.YmirSpell3;
import network.Connectable;

public class BadSpellIcon extends JPanel implements MouseListener{
    private BufferedImage image;
    private boolean Onit = false;
    public Spell spell;
    private Connectable con;

    public BadSpellIcon(Spell spell, Connectable con) {
        this.con=con;
        this.spell = spell;

        image = spell.getImg();

        setPreferredSize(new Dimension(120, 120));
        setOpaque(false);

        addMouseListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (spell.getNum()>0) {
            // Create a circular clipping area
            Graphics2D g2d = (Graphics2D) g.create();
            int diameter = Math.min(getWidth(), getHeight());
            int smallerDiameter = diameter / 3 + 3;
            Ellipse2D.Double circle = new Ellipse2D.Double((getWidth() - diameter) / 2, (getHeight() - diameter) / 2, diameter, diameter);
            g2d.setColor(Color.white);
            if (Onit) {
                g2d.fill(circle);
            }
            // Draw the border (black line)
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2)); // Set the thickness of the border
            g2d.draw(circle);

            // Calculate the angle to draw the green arc
            double angle = (double) (spell.getSecondsElapsed()) / spell.getDuration() * 360;

            // Draw the green arc
            g2d.setColor(spell.getColor()); // Green color with some transparency
            if (spell.getSecondsElapsed() > 25) {
                g2d.setColor(new Color(255, 0, 0, 150));
            }
            g2d.fillArc((int) circle.getX(), (int) circle.getY(), diameter, diameter, 90, (int) angle);

            if (image != null) {
                g2d.drawImage(image, (getWidth() - diameter) / 2, (getHeight() - diameter) / 2, diameter, diameter, null);
            }
            // Draw the white circle
            Ellipse2D.Double whiteCircle = new Ellipse2D.Double(diameter / 30 - 2, diameter / 30 - 2, smallerDiameter,
                    smallerDiameter);
            g2d.setColor(Color.WHITE);

            g2d.fill(whiteCircle);

            // Draw the remaining time inside the small circle
            Font font = new Font("Roboto", Font.BOLD, 16); // Set font for the text
            g2d.setFont(font);
            String text;
            if (spell.getActive()) {
                g2d.setColor(Color.GREEN);
                text = String.valueOf((int)(spell.getDuration() - spell.getSecondsElapsed()));
            } else {
                g2d.setColor(Color.BLACK);
                text = String.valueOf(spell.getNum());
            }

            int textWidth = g2d.getFontMetrics().stringWidth(text);
            int textHeight = g2d.getFontMetrics().getHeight();
            int x = (int) (whiteCircle.getX() + (whiteCircle.getWidth() - textWidth) / 2);
            int y = (int) (whiteCircle.getY() + (whiteCircle.getHeight() + textHeight) / 2);
            g2d.drawString(text, x, y - 5);

            g2d.dispose();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Clicked");
        if (spell.getNum() > 0) {
            if(spell instanceof YmirSpell3){
                con.sendSpell1();
            }else if(spell instanceof InfiniteVoid){
                con.sendSpell1();
            }else{
                con.sendSpell2();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Onit = true;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        Onit = false;
    }
}
