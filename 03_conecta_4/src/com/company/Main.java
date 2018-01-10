package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        char[][] cuenca = new char[6][7];
        int turno = 0, columna;
        boolean abandonado = false;
        String input;

        inicializarTablero(cuenca);
        //char 9679 white;
        //char 9675 black;

        do {
            do {
                visualizarTablero(cuenca);

                System.out.println("Turno de Jugador " + turnoNombre(turno) + ".");
                System.out.print("Escriba el número de columna donde depositar ficha o FIN para abandonar: ");
                input = br.readLine();
                columna = obtenerColumna(input);

            } while (!input.equalsIgnoreCase("fin") && !movimientoPosible(cuenca, columna));

            if (!input.equalsIgnoreCase("fin")) {

                meterFicha(cuenca, columna, turno);

                if (!comprobarVictoria(cuenca, columna, turno)) {
                    turno++;
                    if (turno > 41) input = "fin";
                } else {
                    visualizarTablero(cuenca);
                    input = "fin";
                }

            } else abandonado = true;

        } while (!input.equalsIgnoreCase("fin"));

        System.out.println("\nFin de la partida.\n");
        if (turno < 42) {
            System.out.printf("Después de %d turnos", turno);
            if (abandonado) turno++;
            System.out.println(", Jugador " + turnoNombre(turno) + " ha ganado la partida.");
        } else {
            System.out.printf("Pasados %d turnos y habiendo sido el tablero completado, la partida termina en empate.", turno - 1);
        }
    }

    private static boolean comprobarVictoria(char[][] cuenca, int columna, int turno) {
        char ficha;
        if (turno(turno)) ficha = 9679;
        else ficha = 9675;
        int fila = sueloColumna(cuenca, columna) + 1;
        return comprobarVictoria(cuenca, columna, fila, ficha);
    }

    private static boolean comprobarVictoria(char[][] cuenca, int columna, int fila, char ficha) {
        return (comprobarAdjuntas(cuenca, columna, fila, ficha, 1, 0) ||
                comprobarAdjuntas(cuenca, columna, fila, ficha, 0, 1) ||
                comprobarAdjuntas(cuenca, columna, fila, ficha, -1, 1) ||
                comprobarAdjuntas(cuenca, columna, fila, ficha, 1, -1));
    }


    private static boolean comprobarAdjuntas(char[][] cuenca, int columna, int fila, char ficha, int x, int y) {
        //la función primero busca el extremo para después recorrerlo en la dirección contraria
        if (columna - y >= 0 && columna - y < 6 && fila - x >= 0 && fila - x < 7) {
            if (cuenca[columna - y][fila - x] == ficha) {
                return comprobarAdjuntas(cuenca, columna - y, fila - x, ficha, x, y);
            }
        }
        if (columna + y * 3 < 0 || columna + y * 3 > 5 || fila + x * 3 < 0 || fila + x * 3 > 6) return false;
        for (int i = 0; i < 3; i++) {
            if (cuenca[columna + y * i][fila + x * i] != ficha) return false;
        }
        return true;

    }

    private static void meterFicha(char[][] cuenca, int columna, int turno) {
        char ficha;
        if (turno(turno)) ficha = 9679;
        else ficha = 9675;
        cuenca[sueloColumna(cuenca, columna)][columna] = ficha;
    }

    private static int sueloColumna(char[][] cuenca, int columna) {
        for (int i = cuenca.length - 1; i >= 0; i--) {
            if (cuenca[i][columna] == '0') return i;
        }
        return cuenca.length - 1;
    }

    private static boolean movimientoPosible(char[][] cuenca, int columna) {
        return (columna >= 0 && columna < 7 && (cuenca[0][columna] == '0'));
    }

    private static int obtenerColumna(String input) {
        try {
            return Integer.parseInt(input) - 1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static String turnoNombre(int turno) {
        if (turno(turno)) return "Blancas";
        else return "Negras";
    }

    private static boolean turno(int turno) {
        //true jugador blancas, false jugador negras
        return (turno % 2 == 0);
    }

    private static void visualizarTablero(char[][] cuenca) {
        for (char[] aCuenca : cuenca) {
            for (char anACuenca : aCuenca) {
                if (anACuenca != '0') System.out.print(anACuenca + " ");
                else System.out.print("  ");
            }
            System.out.println();
        }
        System.out.println("1 2 3 4 5 6 7");

    }

    private static void inicializarTablero(char[][] cuenca) {
        for (char[] aCuenca : cuenca) {
            Arrays.fill(aCuenca, '0');
        }
    }
}
