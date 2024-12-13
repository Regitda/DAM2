package com.project1.project1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final int NUM_CARRILES = 4;
    private static final int CORREDORES_POR_CARRIL = 4;
    private static final int[] POSICIONES_INICIALES = {0, 100_000, 200_000, 300_000}; // Java 7 benefit, _ makes more readable. Does nothing to code.

    private static final AtomicInteger posicionPodium = new AtomicInteger(1); // From what I found this is a way to avoid thread issues when updating an int.

    public static void main(String[] args) {
        Thread[] hilosCarril = new Thread[NUM_CARRILES]; // Makes 4 Carriles

        for (int i = 0; i < NUM_CARRILES; i++) {
            int idCarril = i;

            hilosCarril[i] = new Thread(() -> {
                Carril carril = new Carril(idCarril);
                try (ExecutorService executor = Executors.newFixedThreadPool(CORREDORES_POR_CARRIL)) {

                    for (int c = 0; c < CORREDORES_POR_CARRIL; c++) { // Adding 4 corredores for each carril
                        int corredorId = c + 1;  // 1..4
                        boolean tieneTestigo = (c == 0); // Only first one has the thingy.

                        Corredor corredor = new Corredor(corredorId, carril, tieneTestigo);
                        carril.añadirCorredor(corredor, POSICIONES_INICIALES[c]);
                        executor.execute(corredor);
                    }

                    executor.shutdown();
                }

                // Awaiting fin of execution.
                while (!carril.haFinalizadoCarril()) {
                    try { // Bad implementation, but works.
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                // Notificamos al final
                System.out.println("Carril " + idCarril + " ha finalizado la carrera. Notificamos a hilo principal.");
                int puesto = posicionPodium.getAndIncrement();
                System.out.println("PODIUM: Posicion " + puesto + "º para el equipo de carril " + idCarril);
            });

            hilosCarril[i].start();
        }

        // Awaiting when all threads finish and joining them to main.
        for (int c = 0; c < NUM_CARRILES; c++) {
            try {
                hilosCarril[c].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Todas las carreras han finalizado.");
    }
}
