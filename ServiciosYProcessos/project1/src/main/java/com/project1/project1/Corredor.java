package com.project1.project1;

class Corredor implements Runnable {
    private final int idCorredor;
    private final Carril carril;
    private volatile boolean tieneTestigo;
    private boolean finalizado = false;
    private boolean yaAnuncieQueCorro = false; // Anti-Spam mesure. Debugging was pain.

    public Corredor(int idCorredor, Carril carril, boolean tieneTestigo) {
        this.idCorredor = idCorredor;
        this.carril = carril;
        this.tieneTestigo = tieneTestigo;
    }

    public int getIdCorredor() {
        return idCorredor;
    }

    public void setTieneTestigo(boolean tieneTestigo) {
        this.tieneTestigo = tieneTestigo;
        this.yaAnuncieQueCorro = false;
    }

    @Override
    public void run() {
        if (tieneTestigo) {
            System.out.println("Carril " + carril.getIdCarril() + " Corredor " + idCorredor
                    + ": Tengo el testigo y empiezo a correr");
            yaAnuncieQueCorro = true;
        } else {
            System.out.println("Carril " + carril.getIdCarril() + " Corredor " + idCorredor
                    + ": A la espera de recibir el testigo");
        }

        while (!finalizado) {
            synchronized (carril) {
                try {
                    if (!tieneTestigo && !finalizado) {
                        // Waiting for the thingy.
                        carril.wait();
                    }

                    if (tieneTestigo && !finalizado) {
                        // Awaken notice. With anti-log spam check.
                        if (!yaAnuncieQueCorro) {
                            System.out.println("Carril " + carril.getIdCarril() + " Corredor " + idCorredor
                                    + ": Me han despertado.Tengo el testigo");
                            System.out.println("Carril " + carril.getIdCarril() + " Corredor " + idCorredor
                                    + ": Tengo el testigo y empiezo a correr");
                            yaAnuncieQueCorro = true;
                        }

                        boolean avanzo = carril.avanzarCorredor(idCorredor); // Check for if finish line was crossed.
                        if (!avanzo) {
                            int posActual = carril.obtenerPosicion(idCorredor);
                            int limiteTramo = (idCorredor * 100_000) - 1; // Could also just make the runway 1m less. But same thing in logs.


                            // Check for if advanced to finish line
                            if (posActual >= limiteTramo) {
                                System.out.println("Carril " + carril.getIdCarril()
                                        + " Corredor " + idCorredor
                                        + ": He terminado de correr. Posición final: " + posActual);
                                System.out.println("Carril " + carril.getIdCarril()
                                        + " Corredor " + idCorredor
                                        + ": Pierdo el testigo");
                                tieneTestigo = false;
                                finalizado = true;

                                // Pass the thingy to next runner if it exists
                                Corredor siguiente = carril.puedePasarTestigo(idCorredor);
                                if (siguiente != null) {
                                    System.out.println("Carril " + carril.getIdCarril()
                                            + " Corredor " + siguiente.getIdCorredor()
                                            + ": Recibo el testigo");
                                    siguiente.setTieneTestigo(true);
                                }

                                carril.corredorFinaliza();
                                System.out.println("Carril " + carril.getIdCarril()
                                        + " Despierto a todos");
                                carril.notifyAll();
                            } else {
                                // Cant pass, awaiting.
                                carril.wait(2);
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}

