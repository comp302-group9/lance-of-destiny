package ui.screens;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import java.awt.event.MouseEvent;

public class SignUpView extends JPanel {
    public static final int WIDTH = 900;
    public static final int HEIGHT = 600;
    
    private JFrame frame;
    private JTextField usernameField, emailField;
    private JPasswordField passwordField, confirmPasswordField;
    private JButton signUpButton, backButton;
    private JLabel statusLabel;
    private Image backgroundImage;
    private Image smallBackgroundImage;

    public SignUpView() {
        this.setLayout(new GridBagLayout());
        initUI();
        frameSetup();
        backgroundImage = new ImageIcon("src/ui/gifs/back.gif").getImage();
        smallBackgroundImage = new ImageIcon("src/ui/gifs/small_back.gif").getImage();
    }

    private void initUI() {
        Color original = new Color(173, 206, 235);
        Color oncolor = new Color(173, 206, 255, 200);
        LineBorder offBord = new LineBorder(Color.black, 1);
        LineBorder onBord = new LineBorder(Color.black, 3);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Bauhaus 93", Font.PLAIN, 16));
        usernameLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        this.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Bauhaus 93", Font.ROMAN_BASELINE, 14));
        usernameField.setBorder(new LineBorder(Color.BLACK));
        usernameField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                usernameField.setBorder(new LineBorder(Color.BLACK, 2));
            }
            @Override
            public void keyReleased(KeyEvent e) {
                usernameField.setBorder(new LineBorder(Color.BLACK, 1));
            }
        });
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        this.add(usernameField, gbc);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Bauhaus 93", Font.PLAIN, 16));
        emailLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        this.add(emailLabel, gbc);

        emailField = new JTextField(20);
        emailField.setFont(new Font("Bauhaus 93", Font.ROMAN_BASELINE, 14));
        emailField.setBorder(new LineBorder(Color.BLACK));
        emailField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                emailField.setBorder(new LineBorder(Color.BLACK, 2));
            }
            @Override
            public void keyReleased(KeyEvent e) {
                emailField.setBorder(new LineBorder(Color.BLACK, 1));
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        this.add(emailField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Bauhaus 93", Font.PLAIN, 16));
        passwordLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        this.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Bauhaus 93", Font.PLAIN, 14));
        passwordField.setBorder(new LineBorder(Color.BLACK));
        passwordField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                passwordField.setBorder(new LineBorder(Color.BLACK, 2));
            }
            @Override
            public void keyReleased(KeyEvent e) {
                passwordField.setBorder(new LineBorder(Color.BLACK, 1));
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        this.add(passwordField, gbc);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setFont(new Font("Bauhaus 93", Font.PLAIN, 16));
        confirmPasswordLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        this.add(confirmPasswordLabel, gbc);

        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(new Font("Bauhaus 93", Font.PLAIN, 14));
        confirmPasswordField.setBorder(new LineBorder(Color.BLACK));
        confirmPasswordField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                confirmPasswordField.setBorder(new LineBorder(Color.BLACK, 2));
            }
            @Override
            public void keyReleased(KeyEvent e) {
                confirmPasswordField.setBorder(new LineBorder(Color.BLACK, 1));
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        this.add(confirmPasswordField, gbc);

        signUpButton = new JButton("Sign Up");
        signUpButton.setPreferredSize(new Dimension(120, 30));
        signUpButton.setBackground(original);
        signUpButton.setBorder(offBord);
        signUpButton.setForeground(Color.BLACK);
        signUpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                signUpButton.setBackground(oncolor);
                signUpButton.setBorder(onBord);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                signUpButton.setBackground(original);
                signUpButton.setBorder(offBord);
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        this.add(signUpButton, gbc);
        
        backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(120, 30));
        backButton.setBackground(original);
        backButton.setBorder(offBord);
        backButton.setForeground(Color.BLACK);
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setBackground(oncolor);
                backButton.setBorder(onBord);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setBackground(original);
                backButton.setBorder(offBord);
            }
        });
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        this.add(backButton, gbc);

        statusLabel = new JLabel();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(statusLabel, gbc);
    }
    
    private void frameSetup() {
        frame = new JFrame("Sign Up");
        frame.setContentPane(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(new Dimension(600 * 16 / 9, 600)); // Adjusted to match SignInView dimensions
        frame.setLocationRelativeTo(null); // Center the frame
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (frame.getWidth() < 500) {
                    backgroundImage = smallBackgroundImage;
                } else {
                    backgroundImage = new ImageIcon("src/ui/gifs/back.gif").getImage();
                }
                repaint();
            }
        });
    }

    public void display() {
        frame.setVisible(true); 
    }

    public void addSignUpButtonListener(ActionListener listener) {
        signUpButton.addActionListener(listener);
    }

    public void addBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }
    
    public JPanel getPanel() {
        return this;
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public String getConfirmPassword() {
        return new String(confirmPasswordField.getPassword());
    }

    public void setStatus(String message) {
        statusLabel.setText(message);
    }
    
    public void closeFrame() {
        if (frame != null) {
            frame.setVisible(false);
            frame.dispose();  // Close the frame when switching views
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
