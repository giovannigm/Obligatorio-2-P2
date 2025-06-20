// Autores: Giovanni - 288127,  Nicolas - 258264

import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VentanaInformacionAutores extends JPanel implements ModoOscuroObserver {
    private boolean modoOscuro;
    private JPanel panelAutores;
    private List<JTextArea> textos; // Cambiado a lista

    public VentanaInformacionAutores(boolean modoOscuro, ControladorSistema controlador) {
        this.modoOscuro = modoOscuro;
        setLayout(new BorderLayout());
        panelAutores = new JPanel();
        panelAutores.setLayout(new BoxLayout(panelAutores, BoxLayout.Y_AXIS));
        textos = new ArrayList<>();

        String[][] autores = {
                { "Giovanni-288127.jpeg", "Nombre: Giovanni Garcia\nN°Estudiante: 288127" },
                { "Nicolas-258264.jpeg", "Nombre: Nicolás Machado\nN°Estudiante: 258264" }
        };

        for (String[] autor : autores) {
            JPanel panelAutor = new JPanel();
            panelAutor.setLayout(new BoxLayout(panelAutor, BoxLayout.X_AXIS));
            panelAutor.setAlignmentX(Component.LEFT_ALIGNMENT);
            try {
                BufferedImage img = ImageIO.read(new File("Imagen-Nosotros/" + autor[0]));
                Image imgEscalada = img.getScaledInstance(145, 150, Image.SCALE_SMOOTH);
                JLabel labelImg = new JLabel(new ImageIcon(imgEscalada));
                labelImg.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                panelAutor.add(labelImg);
            } catch (IOException e) {
                JLabel labelImg = new JLabel("No se pudo cargar: " + autor[0]);
                panelAutor.add(labelImg);
            }
            panelAutor.add(Box.createRigidArea(new Dimension(20, 0)));
            JTextArea texto = new JTextArea(autor[1]);
            texto.setEditable(false);
            texto.setOpaque(false);
            texto.setFont(new Font("Arial", Font.PLAIN, 14));
            textos.add(texto); // Agregar a la lista
            panelAutor.add(texto);
            panelAutores.add(panelAutor);
            panelAutores.add(Box.createRigidArea(new Dimension(0, 30)));
        }
        add(panelAutores, BorderLayout.CENTER);
        actualizarModoOscuro(modoOscuro);
    }

    @Override
    public void actualizarModoOscuro(boolean modoOscuro) {
        this.modoOscuro = modoOscuro;
        // Aplicar estilos usando la clase Estilos
        Estilos.aplicarEstilos(panelAutores, modoOscuro);
        for (JTextArea texto : textos) {
            texto.setForeground(modoOscuro ? Color.WHITE : Color.BLACK);
        }
        repaint();
    }
}
