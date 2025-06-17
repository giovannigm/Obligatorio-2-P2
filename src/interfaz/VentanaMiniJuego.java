import javax.swing.*;
import java.awt.*;

// VentanaMiniJuego: minijuego de Piedra, Papel o Tijera en Swing
public class VentanaMiniJuego extends JFrame {
    // Etiqueta que muestra el estado de la jugada actual
    private JLabel lblEstado;
    // Etiqueta que muestra la cantidad de victorias
    private JLabel lblVictorias;
    // Botón para reiniciar el contador de victorias
    private JButton btnReiniciar;
    // Instancia de la clase de dominio que maneja la lógica del juego
    private MiniJuegoPPT miniJuego = new MiniJuegoPPT();
    // Contador de victorias del usuario
    private int victorias = 0;

    public VentanaMiniJuego() {
        setTitle("Piedra, Papel o Tijera");
        setSize(400, 280);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel con los botones de jugada
        JPanel panelBotones = new JPanel(new GridLayout(1, 3, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30)); // margen
        panelBotones.setPreferredSize(new Dimension(320, 50)); // más fino
        for (int i = 0; i < MiniJuegoPPT.OPCIONES.length; i++) {
            JButton btn = new JButton(MiniJuegoPPT.OPCIONES[i]);
            btn.setFocusPainted(false);
            btn.setFont(new Font("Arial", Font.BOLD, 16));
            btn.setBackground(new Color(230, 230, 250));
            btn.setBorder(BorderFactory.createLineBorder(new Color(120, 120, 180), 2, true));
            int eleccionUsuario = i;
            // Al hacer clic, se juega la ronda con la opción elegida (la máquina elige su opción aleatoriamente en jugar())
            btn.addActionListener(e -> jugar(eleccionUsuario));
            panelBotones.add(btn);
        }
        add(panelBotones, BorderLayout.CENTER);

        // Etiqueta superior con el estado
        lblEstado = new JLabel("Elige tu jugada", SwingConstants.CENTER);
        lblEstado.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblEstado, BorderLayout.NORTH);

        // Etiqueta de victorias y botón de reinicio en el panel inferior
        lblVictorias = new JLabel("Victorias: 0", SwingConstants.CENTER);
        lblVictorias.setFont(new Font("Arial", Font.BOLD, 14));
        
        btnReiniciar = new JButton("⟳"); // Botón pequeño para reiniciar
        btnReiniciar.setFont(new Font("Arial", Font.BOLD, 20));
        btnReiniciar.setMargin(new Insets(1, 3, 1, 3));
        btnReiniciar.setFocusable(false);
        btnReiniciar.setToolTipText("Reiniciar contador");
        btnReiniciar.addActionListener(e -> reiniciarJuego());

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 2));
        panelSur.add(lblVictorias);
        panelSur.add(btnReiniciar);
        panelSur.setOpaque(false);
        add(panelSur, BorderLayout.PAGE_END);
    }

    // Reinicia el contador de victorias y el estado
    private void reiniciarJuego() {
        victorias = 0;
        lblEstado.setText("Elige tu jugada");
        lblVictorias.setText("Victorias: 0");
    }

    // Lógica de una jugada: compara la elección del usuario y la PC, actualiza el estado y muestra el resultado
    private void jugar(int eleccionUsuario) {
        int eleccionPC = miniJuego.jugadaPC();
        String jugadaUsuario = MiniJuegoPPT.OPCIONES[eleccionUsuario];
        String jugadaPC = MiniJuegoPPT.OPCIONES[eleccionPC];
        int resultado = miniJuego.resultado(eleccionUsuario, eleccionPC);
        String mensaje;
        if (resultado == 0) {
            mensaje = "¡Empate!";
        } else if (resultado == 1) {
            victorias++;
            mensaje = "¡Ganaste!";
        } else {
            mensaje = "¡Perdiste!";
        }
        lblEstado.setText("Tú: " + jugadaUsuario + " | PC: " + jugadaPC);
        lblVictorias.setText("Victorias: " + victorias);
        JOptionPane.showMessageDialog(this, mensaje, "Resultado", JOptionPane.INFORMATION_MESSAGE);
    }
}
