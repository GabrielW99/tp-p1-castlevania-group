package juego;

import java.awt.Color;
import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {

	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	private Velociraptor[] raptors = new Velociraptor[4];
	private Piso[] pisos = new Piso[6];
	private Vikinga vikinga;

	private Image gameOver;
	private Image fondo;
	private Rayo rayo;
	private Commodore commodore;
	private int contador;

	private boolean vuelta;

	private int vidas = 3;
	private int puntaje;

	public Juego() {
		contador = 400;
		this.entorno = new Entorno(this, "Blanco_CarroAvila_Ledesma_Equipo3", 800, 600);

		vikinga = new Vikinga(20, 555);

		commodore = new Commodore(50, 55, 50);

		pisos[0] = new Piso(entorno.ancho() / 2, entorno.alto() - 10, 800);
		pisos[1] = new Piso(entorno.ancho() / 2 - 60, entorno.alto() - 110, 680);
		pisos[2] = new Piso(entorno.ancho() / 2 + 60, entorno.alto() - 210, 680);
		pisos[3] = new Piso(entorno.ancho() / 2 - 60, entorno.alto() - 310, 680);
		pisos[4] = new Piso(entorno.ancho() / 2 + 60, entorno.alto() - 410, 680);
		pisos[5] = new Piso(entorno.ancho() / 2 - 60, entorno.alto() - 510, 680);

		fondo = Herramientas.cargarImagen("fondo.png");
		gameOver = Herramientas.cargarImagen("gameoverphrase.jpg");

		vuelta = true;

//        int puntaje = 10;

//      inicia el juego
		this.entorno.iniciar();

	}

	public void tick() {

		entorno.dibujarImagen(fondo, entorno.ancho() / 2, entorno.alto() / 2, 0);

		for (int i = 0; i < pisos.length; i++) {
			pisos[i].dibujar(entorno);
		}

		entorno.cambiarFont("sans", 20, Color.white);
		entorno.escribirTexto("Vidas: " + vidas + " Puntos: " + puntaje, entorno.ancho() - 200, 22);

		commodore.dibujar(entorno);
// vikinga

		if (entorno.estaPresionada('w')) {
			if (vikinga.banderaDeSalto(pisos)) {
				vikinga.saltar(entorno);
			}
		}
		if (vikinga.banderaDeCaida(pisos)) {
			vikinga.caer(entorno);
		}
		vikinga.dibujar(entorno);

		if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA) || entorno.estaPresionada('a')) {
			vikinga.moverHaciaIzquierda(entorno);
		}
		if (entorno.estaPresionada(entorno.TECLA_DERECHA) || entorno.estaPresionada('d')) {
			vikinga.moverHaciaDerecha(entorno);
		}
		if (entorno.estaPresionada('e')) {
			vikinga.escudo(entorno);
		}

//Rayo        

		if (rayo != null) {
			rayo.dibujar(entorno);
			rayo.mover(entorno);
			if (rayo.getX() > entorno.ancho() || rayo.getX() < 0) {
				rayo = null;
			}
		}
		if (entorno.estaPresionada(entorno.TECLA_ESPACIO) && rayo == null) {
			rayo = new Rayo(vikinga.getX(), vikinga.getY(), vikinga.getdireccion());
		}

// Raptors        

		for (int e = 0; e < raptors.length; e++) {
			if (raptors[e] != null) {
				raptors[e].dibujar(entorno);
				raptors[e].mover();
				if (raptors[e].banderaDeCaida(pisos)) {
					raptors[e].caer(entorno);
				}
				if (raptors[e].getX() < 0 + raptors[e].getAncho() / 2
						|| raptors[e].getX() > 800 - raptors[e].getAncho() / 2) {
					if (vuelta) {
						raptors[e].cambiarDeDireccion();
						raptors[e].cambiarDeDireccionImg(vuelta);
						vuelta = false;
					} else {
						raptors[e].cambiarDeDireccion();
						raptors[e].cambiarDeDireccionImg(vuelta);
						vuelta = true;
					}
				}
				if (vikinga.ChoqueRaptor(raptors[e])) {
					vikinga.muerte();
					vidas -= 1;
				}
				if (rayo != null && raptors[e].choqueRayo(rayo)) {
					rayo = null;
					raptors[e] = null;
					puntaje += 80;
					contador = 350;
				}
			}
		}
		if (contador == 500) {
			for (int i = 0; i < 3; i++)
				if (raptors[i] == null) {
					raptors[i] = new Velociraptor(pisos[5 - i].getY() - pisos[5 - i].getAlto() / 2, 100, 3);
					contador = 0;
				}
		}

		contador += 1;
		System.out.println(contador);
		if (vidas <= 0) {
			entorno.dibujarImagen(gameOver, entorno.ancho() / 2, entorno.alto() / 2, 0);
		}
		
		if (commodore.recuperasteCompu(vikinga)) {
			entorno.dibujarImagen(gameOver, entorno.ancho() / 2, entorno.alto() / 2, 0);
		}
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}

}
