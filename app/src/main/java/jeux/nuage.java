package jeux;


import java.util.Random;

public class nuage extends Jeux_generique{

	public nuage() {
	}


	public boolean affiche_nuage = true;
	private int vitesse_au_pic = 0;
	public int dessus_dessous = 0;
	public boolean thread_lance = false;
	private int vitesse_vent = 0;

	public void init_nuage() {
		Random randomGenerator = new Random();
		vitesse_au_pic = randomGenerator.nextInt(200);

		position_y = randomGenerator.nextInt(200);
		position_x_camion =randomGenerator.nextInt(500);
		}

	public void incremente_position() {
		position_x_camion += (vitesse_vent);//vitesse_au_pic;

		if (position_x_camion > largeur_ecran) {
			position_x_camion = -300;

			Random randomGenerator = new Random();

			position_y = randomGenerator.nextInt(500);
		}
		if (position_x_camion < -300) {
			position_x_camion = largeur_ecran+300;

			Random randomGenerator = new Random();

			position_y = randomGenerator.nextInt(500);
		}

	}
	@Override
	public void run() {

		try {
			// while (etat) {
			while (!Thread.currentThread().isInterrupted()) {
				incremente_position();
				//Thread.sleep(vitesse_au_pic);
				Thread.sleep( 100);
			}
		} catch (InterruptedException e) {
			return;

		}

	}

	public int getVitesse_vent() {
		return vitesse_vent;
	}

	public void setVitesse_vent(int vitesse_vent) {
		this.vitesse_vent = vitesse_vent;
	}
}
