package com.company;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Random r = new Random();
        int[] array = new int[10];
        for (int i = 0; i < 10; i++) {
            array[i] = r.nextInt(100);
        }
        visualizarArray(array);
        visualizarArray(ordenarArray(array));
    }

    private static int[] ordenarArray(int[] array) {
        int[] orden = new int[10];
        int[] arrayordenado = new int[10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (array[i] > array[j]) orden[i]++;
            }
        }

        for (int i = 0; i < 10; i++) {
            arrayordenado[orden[i]] = array[i];
        }

        // Necesario en caso de que se repita un número en el array. No es la manera más deseable pero sirve para este caso
        for (int i = 0; i < 9; i++) {
            if (arrayordenado[i] > 0 && arrayordenado[i+1] == 0) arrayordenado[i+1] = arrayordenado[i];
        }
        return arrayordenado;
    }

    private static void visualizarArray(int[] array) {
        for (int i = 0; i < 10; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println();
    }
}
