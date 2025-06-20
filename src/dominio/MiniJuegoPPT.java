// Autores: Giovanni - 288127,  Nicolas - 258264

import java.util.Random;

public class MiniJuegoPPT {
    public static final int PIEDRA = 0;
    public static final int PAPEL = 1;
    public static final int TIJERA = 2;
    public static final String[] OPCIONES = {"Piedra", "Papel", "Tijera"};

    private Random random;

    public MiniJuegoPPT() {
        this.random = new Random();
    }

    public int jugadaPC() {
        return random.nextInt(3);
    }

    /**
     * Determina el resultado de la jugada.
     * @param eleccionUsuario 0: Piedra, 1: Papel, 2: Tijera
     * @param eleccionPC 0: Piedra, 1: Papel, 2: Tijera
     * @return 0: Empate, 1: Gana usuario, -1: Gana PC
     */
    public int resultado(int eleccionUsuario, int eleccionPC) {
        if (eleccionUsuario == eleccionPC) return 0;
        if ((eleccionUsuario == PIEDRA && eleccionPC == TIJERA) ||
            (eleccionUsuario == PAPEL && eleccionPC == PIEDRA) ||
            (eleccionUsuario == TIJERA && eleccionPC == PAPEL)) {
            return 1;
        }
        return -1;
    }
}
