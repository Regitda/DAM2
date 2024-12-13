package com.project1.project1;

public class Carril {
    private static final int LONGITUD_CARRIL = 400_000;  // 400000 mm
    private final int idCarril;
    private final Corredor[] posiciones;
    private int corredoresFinalizados = 0;

    // Sincronization lock. Never really had much experience with threading. Normally internal APIs dont allow that.
    private final Object lock = new Object();

    public Carril(int idCarril) {
        this.idCarril = idCarril;
        this.posiciones = new Corredor[LONGITUD_CARRIL];
    }

    public boolean añadirCorredor(Corredor corredor, int posicion) {
        synchronized (lock) {
            if (posicion >= 0 && posicion < LONGITUD_CARRIL && posiciones[posicion] == null) {
                posiciones[posicion] = corredor;
                return true;
            }
            return false;
        }
    }

    public boolean avanzarCorredor(int corredorId) {
        synchronized (lock) {
            int posicionActual = obtenerPosicion(corredorId);
            if (posicionActual == -1) return false;  // doesnt exist on the carril
            if (posicionActual == LONGITUD_CARRIL - 1) return false; // finish line check

            int siguiente = posicionActual + 1;
            if (siguiente < LONGITUD_CARRIL && posiciones[siguiente] == null) { // Overflow check and advancement.
                Corredor c = posiciones[posicionActual];
                posiciones[posicionActual] = null;
                posiciones[siguiente] = c;
                return true;
            }
            return false;
        }
    }

    public Corredor puedePasarTestigo(int corredorId) {
        synchronized (lock) {
            int pos = obtenerPosicion(corredorId);
            if (pos == -1) return null; // Doesnt exist on field.
            int siguiente = pos + 1;
            if (siguiente < LONGITUD_CARRIL && posiciones[siguiente] != null) {
                return posiciones[siguiente];
            }
            return null;
        }
    }

    public int obtenerPosicion(int corredorId) {
        synchronized (lock) {
            for (int i = 0; i < LONGITUD_CARRIL; i++) {
                if (posiciones[i] != null && posiciones[i].getIdCorredor() == corredorId) {
                    return i;
                }
            }
            return -1;
        }
    }

    public synchronized void corredorFinaliza() {
        corredoresFinalizados++;
    }

    public synchronized boolean haFinalizadoCarril() {
        return corredoresFinalizados == 4;
    }

    public int getIdCarril() {
        return idCarril;
    }

    public synchronized void despertarCorredores() { // Scrapped the idea.
        notifyAll();
    }
}