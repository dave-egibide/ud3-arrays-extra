//TODO(Dave): Añadir enroque
//TODO(Dave): Hay algún problema a la hora de visualizar el desplazamiento diagonal de las figuras

package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        char[][] tablero = nuevoTablero();
        visualizarTablero(tablero);
        int turno = 1;
        String x = "", y;

        do {
            try {

                if (turno % 2 == 0) System.out.println("Turno Negras.");
                else System.out.println("Turno Blancas.");

                if (esJaque(tablero, turno))
                    System.out.println("Jaque. Escriba la coordenada de la figura que desea mover (ej.: 4c) o \"mate\" para conceder la victoria: ");
                else
                    System.out.print("Escriba la coordenada de la figura que desea mover (ej.: 4c) o \"tablas\" para terminar: ");

                x = br.readLine().toLowerCase();
                System.out.print(crdy(x));
                System.out.println(crdx(x));

                if (coordenadaLegal(tablero, turno, crdy(x), crdx(x), true)) {
                    System.out.print("Seleccionado " + figuraTablero(tablero[crdy(x)][crdx(x)]) +
                            ". Escriba la coordenada a la que desea mover la figura (ej.: 4c): ");
                    y = br.readLine();
                    System.out.print(crdy(y));
                    System.out.println(crdx(y));

                    if (coordenadaLegal(tablero, turno, crdy(y), crdx(y), false)) {

                        if (movimientoLegal(tablero, turno, crdy(x), crdx(x), crdy(y), crdx(y))) {
                            tablero[crdy(y)][crdx(y)] = tablero[crdy(x)][crdx(x)];
                            tablero[crdy(x)][crdx(x)] = 0;
                            tablero[crdy(y)][crdx(y)] = (peonCoronado(tablero, crdy(y), crdx(x)));
                            turno++;
                            visualizarTablero(tablero);

                        } else System.out.println("Movimiento inválido.");
                    } else System.out.println("Coordenada ilegal.");
                } else System.out.println("Coordenada ilegal.");

            } catch (NumberFormatException e) {
                System.out.println("Coordenada incorrecta.");
            }
        } while (!x.equals("tablas") && !x.equals("mate"));
        if (x.equals("tablas")) System.out.printf("Empate. Fin de la partida después de %d turnos.", turno);
        if (x.equals("mate")) {
            if (turno % 2 == 0)
                System.out.printf("Victoria para jugador Blancas por Jaque Mate trás %d turnos.", turno);
            else System.out.printf("Victoria para jugador Negras por Jaque Mate trás %d turnos.", turno);
        }

    }

    private static char peonCoronado(char[][] tablero, int ydest, int xdest) {
        if (ydest == 0 || ydest == 7) {
            if (tablero[ydest][xdest] == 9817) tablero[ydest][xdest] = 9813;
            if (tablero[ydest][xdest] == 9823) tablero[ydest][xdest] = 9819;
        }
        return tablero[ydest][xdest];
    }

    private static boolean esJaque(char[][] tablero, int turno) {
        int yrey = yRey(tablero, turno);
        int xrey = xRey(tablero, turno);
        //peon
        if (esJaquePeon(tablero, turno, yrey, xrey)) return true;
        //caballo
        if (esJaqueCaballo(tablero, turno, yrey, xrey)) return true;
        // diagonales (alfil & reina)
        if (esJaqueDiagonales(tablero, turno, yrey, xrey)) return true;
        // cruz (torre & reina)
        if (esJaqueCruz(tablero, turno, yrey, xrey)) return true;
        // rey
        return (esJaqueRey(tablero, turno, yrey, xrey));
    }

    private static boolean esJaqueRey(char[][] tablero, int turno, int yrey, int xrey) {
        int rey;
        if (turno % 2 == 0) rey = 9818;
        else rey = 9812;
        if (yrey > 0) {
            if (casillaColindante(tablero, yrey, xrey, "N", rey)) return true;
            if (xrey > 0) if (casillaColindante(tablero, yrey, xrey, "NW", rey)) return true;
            if (xrey < 7) if (casillaColindante(tablero, yrey, xrey, "NE", rey)) return true;
        }
        if (yrey < 7) {
            if (casillaColindante(tablero, yrey, xrey, "S", rey)) return true;
            if (xrey > 0) if (casillaColindante(tablero, yrey, xrey, "SW", rey)) return true;
            if (xrey < 7) if (casillaColindante(tablero, yrey, xrey, "SE", rey)) return true;
        }
        if (xrey > 0) if (casillaColindante(tablero, yrey, xrey, "W", rey)) return true;
        if (xrey < 7) if (casillaColindante(tablero, yrey, xrey, "E", rey)) return true;
        return false;
    }

    private static int casillaColindante(char[][] tablero, int ycord, int xcord, String cardinal) {
        switch (cardinal.toUpperCase()) {
            case "N":
                return tablero[ycord - 1][xcord];
            case "NE":
                return tablero[ycord - 1][xcord + 1];
            case "E":
                return tablero[ycord][xcord + 1];
            case "SE":
                return tablero[ycord + 1][xcord + 1];
            case "S":
                return tablero[ycord + 1][xcord];
            case "SW":
                return tablero[ycord + 1][xcord - 1];
            case "W":
                return tablero[ycord][xcord - 1];
            case "NW":
                return tablero[ycord - 1][xcord - 1];
        }
        return 0;
    }

    private static boolean casillaColindante(char[][] tablero, int ycord, int xcord, String cardinal, int contenido) {
        return casillaColindante(tablero, ycord, xcord, cardinal) == contenido;
    }

    private static boolean esJaqueCruz(char[][] tablero, int turno, int yrey, int xrey) {
        int torre, reina;
        if (turno % 2 == 0) {
            torre = 9820;
            reina = 9819;
        } else {
            torre = 9814;
            reina = 9813;
        }
        for (int i = yrey - 1; i >= 0; i--) { //S
            if (tablero[i][xrey] == torre || tablero[i][xrey] == reina) return true;
            if (tablero[i][xrey] > 0) break;
        }
        for (int i = yrey + 1; i < 8; i++) { //N
            if (tablero[i][xrey] == torre || tablero[i][xrey] == reina) return true;
            if (tablero[i][xrey] > 0) break;
        }
        for (int i = xrey - 1; i >= 0; i--) { //W
            if (tablero[yrey][i] == torre || tablero[yrey][i] == reina) return true;
            if (tablero[yrey][i] > 0) break;
        }
        for (int i = xrey + 1; i < 8; i++) { //E
            if (tablero[yrey][i] == torre || tablero[yrey][i] == reina) return true;
            if (tablero[yrey][i] > 0) break;
        }
        return false;
    }

    private static boolean esJaqueDiagonales(char[][] tablero, int turno, int yrey, int xrey) {
        int alfil, reina;
        if (turno % 2 == 0) {
            alfil = 9821;
            reina = 9819;
        } else {
            alfil = 9815;
            reina = 9813;
        }
        for (int i = yrey - 1, j = xrey - 1; i >= 0 && j >= 0; i--, j--) {
            if (tablero[i][j] == alfil || tablero[i][j] == reina)
                return true;
            if (tablero[i][j] > 0) break;
        }
        if (xrey < 7) { // NE
            for (int i = yrey - 1, j = xrey + 1; i >= 0 && j < 8; i--, j++) {
                if (tablero[i][j] == alfil || tablero[i][j] == reina)
                    return true;
                if (tablero[i][j] > 0) break;
            }
        }
        for (int i = yrey + 1, j = xrey - 1; i < 8 && j >= 0; i++, j--) {
            if (tablero[i][j] == alfil || tablero[i][j] == reina)
                return true;
            if (tablero[i][j] > 0) break;
        }
        if (xrey < 7) { // SE
            for (int i = yrey + 1, j = xrey + 1; i < 8 && j < 8; i++, j++) {
                if (tablero[i][j] == alfil || tablero[i][j] == reina)
                    return true;
                if (tablero[i][j] > 0) break;
            }
        }
        return false;
    }

    private static boolean esJaqueCaballo(char[][] tablero, int turno, int yrey, int xrey) {
        int caballo;
        if (turno % 2 == 0) caballo = 9822;
        else caballo = 9816;
        if (yrey < 6 && xrey > 0) if (tablero[yrey + 2][xrey - 1] == caballo) return true;
        if (yrey < 6 && xrey < 7) if (tablero[yrey + 2][xrey + 1] == caballo) return true;
        if (yrey > 1 && xrey > 0) if (tablero[yrey - 2][xrey - 1] == caballo) return true;
        if (yrey > 1 && xrey < 7) if (tablero[yrey - 2][xrey + 1] == caballo) return true;
        if (yrey < 7 && xrey < 6) if (tablero[yrey + 1][xrey + 2] == caballo) return true;
        if (yrey < 7 && xrey > 1) if (tablero[yrey + 1][xrey - 2] == caballo) return true;
        if (yrey > 0 && xrey < 6) if (tablero[yrey - 1][xrey + 2] == caballo) return true;
        if (yrey > 0 && xrey > 1) if (tablero[yrey - 1][xrey - 2] == caballo) return true;
        return false;
    }

    private static boolean esJaquePeon(char[][] tablero, int turno, int yrey, int xrey) {
        if (yrey != 0 && turno % 2 == 0)
            if (tablero[yrey - 1][xrey + 1] == 9823 || tablero[yrey - 1][xrey - 1] == 9823) return true;
            else if (yrey != 7 && turno % 2 != 0)
                if (tablero[yrey + 1][xrey + 1] == 9817 || tablero[yrey + 1][xrey - 1] == 9817) return true;
        return false;
    }


    private static int xRey(char[][] tablero, int turno) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((tablero[i][j] == 9812 && turno % 2 == 0) || (tablero[i][j] == 9818 && turno % 2 != 0))
                    return j;
            }
        }
        return 0;
    }

    private static int yRey(char[][] tablero, int turno) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((tablero[i][j] == 9812 && turno % 2 == 0) || (tablero[i][j] == 9818 && turno % 2 != 0))
                    return i;
            }
        }
        return 0;
    }

    private static boolean coordenadaLegal(char[][] tablero, int turno, int y, int x, boolean origen) {
        int rey;
        if (turno % 2 == 0 ^ origen) rey = 9818;
        else rey = 9812;
        return y >= 0 && x >= 0 && x < 8 && y < 8 &&
                (tablero[y][x] >= rey && tablero[y][x] < rey + 6 || ((tablero[y][x] == 0) && !origen));
    }

    private static boolean movimientoLegal(char[][] tablero, int turno, int yorig, int xorig, int ydest, int xdest) {
        if (yorig == ydest && xorig == xdest) return false;
        switch (tablero[yorig][xorig]) {
            case 9812:
            case 9818:
                //return "Rey";
                if (movimientoLegalRey(yorig, xorig, ydest, xdest))
                    return !esJaque(tablero, turno, yorig, xorig, ydest, xdest);
                break;
            case 9814:
            case 9820:
                //return "Torre";
            case 9813:
            case 9819:
                //return "Reina" cruz
                if (movimientoLegalCruz(tablero, yorig, xorig, ydest, xdest))
                    return !esJaque(tablero, turno, yorig, xorig, ydest, xdest);
                if (tablero[yorig][xorig] == 9814 || tablero[yorig][xorig] == 9820) break;
            case 9815:
            case 9821:
                //return "Alfil"; Reina diagonal
                if (movimientoLegalDiagonal(tablero, yorig, xorig, ydest, xdest))
                    return !esJaque(tablero, turno, yorig, xorig, ydest, xdest);
                break;
            case 9816:
            case 9822:
                //return "Caballo";
                if (movimientoLegalCaballo(yorig, xorig, ydest, xdest))
                    return !esJaque(tablero, turno, yorig, xorig, ydest, xdest);
                break;
            case 9817:
            case 9823:
                //return "Peon";
                if (movimientoLegalPeon(tablero, turno, yorig, xorig, ydest, xdest))
                    return !esJaque(tablero, turno, yorig, xorig, ydest, xdest);
        }
        return false;
    }

    private static boolean movimientoLegalDiagonal(char[][] tablero, int yorig, int xorig, int ydest, int xdest) {
        if (yorig > ydest) { // N
            if (xorig > xdest) { // NW
                for (int i = yorig - 1, j = xorig - 1; i > ydest; i--, j--) {
                    if (tablero[i][j] > 0) return false;
                }
                return true;
            } else { // NE
                for (int i = yorig - 1, j = xorig + 1; i > ydest; i--, j++) {
                    if (tablero[i][j] > 0) return false;
                }
                return true;
            }
        } else { // S
            if (xorig > xdest) { // SW
                for (int i = yorig + 1, j = xorig - 1; i < ydest; i++, j--) {
                    if (tablero[i][j] > 0) return false;
                }
                return true;
            } else { // SE
                for (int i = yorig + 1, j = xorig + 1; i < ydest; i++, j++) {
                    if (tablero[i][j] > 0) return false;
                }
                return true;
            }
        }

    }


    private static boolean movimientoLegalCruz(char[][] tablero, int yorig, int xorig, int ydest, int xdest) {
        if (yorig == ydest) { //horizontal
            if (xorig > xdest) { // W
                for (int i = xorig - 1; i > xdest; i--) {
                    if (tablero[yorig][i] > 0) return false;
                }
                return true;
            } else { // E
                for (int i = xorig + 1; i < xdest; i++) {
                    if (tablero[yorig][i] > 0) return false;
                }
                return true;
            }
        } else { //vertical
            if (yorig > ydest) { // N
                for (int i = yorig - 1; i > ydest; i--) {
                    if (tablero[i][xorig] > 0) return false;
                }
                return true;
            } else { // S
                for (int i = yorig + 1; i < ydest; i++) {
                    if (tablero[i][xorig] > 0) return false;
                }
                return true;
            }
        }
    }

    private static boolean movimientoLegalPeon(char[][] tablero, int turno, int yorig, int xorig, int ydest, int xdest) {
        int vertical, aliados;
        if (turno % 2 == 0) {
            vertical = -1;
            aliados = 9812;
        } else {
            vertical = 1;
            aliados = 9818;
        }
        if (ydest == yorig + 2 * vertical && (yorig == 1 || yorig == 6)) {
            return (tablero[yorig + vertical][xorig] == 0);
        }
        if (ydest == yorig + vertical && (xdest == xorig + 1 || xdest == xorig - 1)) {
            return !(tablero[ydest][xdest] >= aliados && tablero[ydest][xdest] < aliados + 6);
        }
        return (ydest == yorig + vertical);
    }

    private static boolean esJaque(char[][] tablero, int turno, int yorig, int xorig, int ydest, int xdest) {
        char[][] tablero2 = new char[8][8];
        for (int i = 0; i < tablero2.length; i++) {
            System.arraycopy(tablero[i], 0, tablero2[i], 0, tablero2[0].length);
        }
        tablero2[ydest][xdest] = tablero2[yorig][xorig];
        tablero2[yorig][xorig] = 0;
        return esJaque(tablero2, turno);
    }

    private static boolean movimientoLegalCaballo(int yorig, int xorig, int ydest, int xdest) {
        return ((Math.abs(yorig - ydest) == 2 && Math.abs(xorig - xdest) == 1) ||
                (Math.abs(yorig - ydest) == 1 && Math.abs(xorig - xdest) == 2));
    }

    private static boolean movimientoLegalRey(int yorig, int xorig, int ydest, int xdest) {
        return (Math.abs(yorig - ydest) < 2 && Math.abs(xorig - xdest) < 2);
    }

    private static int crdx(String coordenada) {
        return "abcdefgh".indexOf(coordenada.charAt(1));
    }

    private static int crdy(String coordenada) {
        return Integer.parseInt(coordenada.substring(0, 1)) - 1;
    }

    private static String figuraTablero(char coordenada) {
        switch (coordenada) {
            case 9812:
            case 9818:
                return "Rey";
            case 9813:
            case 9819:
                return "Reina";
            case 9814:
            case 9820:
                return "Torre";
            case 9815:
            case 9821:
                return "Alfil";
            case 9816:
            case 9822:
                return "Caballo";
            case 9817:
            case 9823:
                return "Peón";
            default:
                return "";
        }
    }

    private static void visualizarTablero(char[][] tablero) {
        for (int i = 0; i < 8; i++) {
            System.out.print(i + 1 + " ");
            for (int j = 0; j < 8; j++) {
                if (tablero[i][j] != 0) {
                    System.out.print(tablero[i][j]);
                } else System.out.print(" ");
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.print("  ");
        for (int i = 0; i < 8; i++) {
            System.out.print("abcdefgh".charAt(i) + "  ");
        }
        System.out.println();
    }

    private static char[][] nuevoTablero() {
        char[][] tablero = new char[8][8];
        //peones
        for (int i = 0; i < 8; i++) {
            tablero[1][i] = 9823;
            tablero[6][i] = 9817;
        }
        //rey
        tablero[0][4] = 9818;
        tablero[7][4] = 9812;
        //reina
        tablero[0][3] = 9819;
        tablero[7][3] = 9813;
        //torre
        tablero[0][0] = tablero[0][7] = 9820;
        tablero[7][0] = tablero[7][7] = 9814;
        //alfil
        tablero[0][2] = tablero[0][5] = 9821;
        tablero[7][2] = tablero[7][5] = 9815;
        //caballo
        tablero[0][1] = tablero[0][6] = 9822;
        tablero[7][1] = tablero[7][6] = 9816;

        return tablero;
    }
}