package consola;

import uniandes.dpoo.taller4.modelo.Tablero;
import uniandes.dpoo.taller4.modelo.RegistroTop10;
import uniandes.dpoo.taller4.modelo.Top10;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Interfaz extends JFrame implements ActionListener {

    private JButton[][] buttons;
    private JPanel panel;
    private Tablero tablero;
    private Top10 registro;
    public Interfaz(int rows, int cols) {
        setTitle("Lights Out");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.registro = new Top10();

        panel = new JPanel();
        panel.setLayout(new GridLayout(rows, cols));
        buttons = new JButton[rows][cols];
        tablero = new Tablero(rows);
        
     // Crear la etiqueta JLabel y agregarla al panel
        ImageIcon imagen = new ImageIcon("data/luz.png");

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                JButton button = new JButton(imagen);
                button.setBackground(Color.LIGHT_GRAY);
                button.setActionCommand(r + "," + c);
                button.addActionListener(this);
                panel.add(button);
                buttons[r][c] = button;
            }
        }

        // Agregar JComboBox para seleccionar el tamaño del tablero
        String[] opciones = {"5x5", "6x6", "7x7", "8x8", "9x9", "10x10"};
        JComboBox<String> comboBox = new JComboBox<>(opciones);
        comboBox.setSelectedIndex(rows - 5); // seleccionar la opción correspondiente al tamaño actual del tablero
        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String opcion = (String) comboBox.getSelectedItem();
                int newRows = Integer.parseInt(opcion.substring(0,1));
                int newCols = Integer.parseInt(opcion.substring(2,3));
                dispose(); // cerrar la ventana actual
                new Interfaz(newRows, newCols); // crear una nueva ventana con el tamaño seleccionado
            }
        });

        JPanel comboBoxPanel = new JPanel();
        comboBoxPanel.add(comboBox);
        add(comboBoxPanel, "North");
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1)); // se utiliza un GridLayout para acomodar los botones verticalmente
        JButton nuevoJuegoButton = new JButton("Nuevo Juego");
        JButton guardarButton = new JButton("Guardar");
        JButton cargarButton = new JButton("Cargar");
        JButton top10Button = new JButton("Top 10");
        
        buttonPanel.add(nuevoJuegoButton);
        buttonPanel.add(guardarButton);
        buttonPanel.add(cargarButton);
        buttonPanel.add(top10Button);
        
        add(buttonPanel, "East"); 
        nuevoJuegoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tablero.reiniciar();
                for (int r = 0; r < buttons.length; r++) {
                    for (int c = 0; c < buttons[0].length; c++) {
                        if (tablero.darTablero()[r][c]) {
                            buttons[r][c].setBackground(Color.LIGHT_GRAY);
                        } else {
                            buttons[r][c].setBackground(Color.YELLOW);
                        }
                    }
                }
            }
        });

        add(panel);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String[] rc = e.getActionCommand().split(",");
        int row = Integer.parseInt(rc[0]);
        int col = Integer.parseInt(rc[1]);

        // Llamar al método de Tablero para cambiar el estado de la casilla y sus vecinas
        tablero.jugar(row, col);

        // Actualizar el color de los botones
        for (int r = 0; r < buttons.length; r++) {
            for (int c = 0; c < buttons[0].length; c++) {
                if (tablero.darTablero()[r][c]) {
                    buttons[r][c].setBackground(Color.LIGHT_GRAY);
                } else {
                    buttons[r][c].setBackground(Color.YELLOW);
                }
            }
        }

        // Verificar si todas las luces están apagadas
        boolean todasApagadas = true;
        for (int r = 0; r < tablero.darTablero().length; r++) {
            for (int c = 0; c < tablero.darTablero()[0].length; c++) {
                if (tablero.darTablero()[r][c]) {
                    todasApagadas = false;
                    break;
                }
            }
        }
        if (todasApagadas) {
            JOptionPane.showMessageDialog(this, "¡Ganaste en "+ tablero.darJugadas() + " jugadas!");
            String nombre = JOptionPane.showInputDialog(this,"Ingresa tu nombre: ");
            int jugadas = tablero.darJugadas();
            registro.agregarRegistro(nombre, jugadas);

        }
    }
    public static void main(String[] args) {
        new Interfaz(5, 5);
    }
       
}
