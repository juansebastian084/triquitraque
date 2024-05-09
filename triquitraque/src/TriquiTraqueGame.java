import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TriquiTraqueGame extends JFrame implements ActionListener {
    private JButton[][] board;
    private boolean xTurn = true;
    private boolean gameActive = false;
    private int size;
    private Player[] players;

    public TriquiTraqueGame() {
        setTitle("Tic Tac Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);

        initializeGame();

        setVisible(true);
    }

    private void initializeGame() {
        String[] sizes = {"3x3", "4x4", "5x5"};
        String sizeSelection = (String) JOptionPane.showInputDialog(null, "Seleccione el tamaño del tablero", "Tamaño del Tablero", JOptionPane.PLAIN_MESSAGE, null, sizes, sizes[0]);
        if (sizeSelection == null) {
            System.exit(0);
        }
        size = Integer.parseInt(sizeSelection.substring(0, 1));

        players = new Player[2];
        for (int i = 0; i < 2; i++) {
            String name = JOptionPane.showInputDialog("Ingrese el nombre del Jugador " + (i + 1) + ":");
            if (name == null || name.trim().isEmpty()) {
                name = "Jugador " + (i + 1);
            }
            String color = (String) JOptionPane.showInputDialog(null, "Seleccione el color de la ficha para " + name + ":", "Color de Ficha", JOptionPane.PLAIN_MESSAGE, null, new String[]{"Rojo", "Azul", "Amarillo", "Verde"}, "Rojo");
            players[i] = new Player(name, color, (i == 0) ? "X" : "O");
        }

        setLayout(new GridLayout(size, size));
        board = new JButton[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                board[row][col] = new JButton("");
                board[row][col].setFont(new Font("Arial", Font.PLAIN, 40));
                board[row][col].setFocusPainted(false);
                board[row][col].addActionListener(this);
                add(board[row][col]);
            }
        }

        gameActive = true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameActive) return;

        JButton button = (JButton) e.getSource();
        int row = -1, col = -1;
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == button) {
                    row = i;
                    col = j;
                    break;
                }
            }
        }
        if (row == -1 || col == -1) return; 

        if (button.getText().equals("")) {
            if (xTurn) {
                button.setForeground(players[0].getColor());
                button.setText(players[0].getPiece());
            } else {
                button.setForeground(players[1].getColor());
                button.setText(players[1].getPiece());
            }
            xTurn = !xTurn;

            if (checkForWin(row, col)) {
                JOptionPane.showMessageDialog(this, (xTurn ? players[1].getName() : players[0].getName()) + " wins!");
                handleEndOfGame();
            } else if (checkForDraw()) {
                JOptionPane.showMessageDialog(this, "¡Empate!");
                handleEndOfGame();
            }
        }
    }

    private boolean checkForWin(int row, int col) {
        String symbol = board[row][col].getText();

        
        boolean win = true;
        for (int i = 0; i < size; i++) {
            if (!board[row][i].getText().equals(symbol)) {
                win = false;
                break;
            }
        }
        if (win) return true;

        
        win = true;
        for (int i = 0; i < size; i++) {
            if (!board[i][col].getText().equals(symbol)) {
                win = false;
                break;
            }
        }
        if (win) return true;

        
        if (row == col || row + col == size - 1) {
            win = true;
            for (int i = 0; i < size; i++) {
                if (!board[i][i].getText().equals(symbol)) {
                    win = false;
                    break;
                }
            }
            if (win) return true;

            win = true;
            for (int i = 0; i < size; i++) {
                if (!board[i][size - 1 - i].getText().equals(symbol)) {
                    win = false;
                    break;
                }
            }
            return win;
        }

        return false;
    }

    private boolean checkForDraw() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (board[row][col].getText().isEmpty()) {
                    return false; 
                }
            }
        }
        return true; 
    }

    private void resetBoard() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                board[row][col].setText("");
            }
        }
        xTurn = true;
    }

    private void handleEndOfGame() {
        int choice = JOptionPane.showConfirmDialog(this, "¿Desea iniciar una nueva partida?", "Fin del Juego", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            resetBoard();
        } else {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TriquiTraqueGame::new);
    }
}

class Player {
    private String name;
    private Color color;
    private String piece;

    public Player(String name, String color, String piece) {
        this.name = name;
        switch (color) {
            case "Rojo":
                this.color = Color.RED;
                break;
            case "Azul":
                this.color = Color.BLUE;
                break;
            case "Amarillo":
                this.color = Color.YELLOW;
                break;
            case "Verde":
                this.color = Color.GREEN;
                break;
        }
        this.piece = piece;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public String getPiece() {
        return piece;
    }
}

