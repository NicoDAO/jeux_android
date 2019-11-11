package jeux;


import java.util.Random;

public class nuage extends Jeux_generique{

	public nuage() {
	}


	public boolean affiche_nuage = true;
	private int vitesse_au_pic = 0;
	public int dessus_dessous = 0;
	public boolean thread_lance = false;

	public void init_nuage() {
		Random randomGenerator = new Random();
		vitesse_au_pic = randomGenerator.nextInt(200);

		position_y = randomGenerator.nextInt(200);
		position_x_camion =randomGenerator.nextInt(500);
	}

	public void incremente_position() {
		position_x_camion += 1;//vitesse_au_pic;
		if (position_x_camion > 700) {
			position_x_camion = -300;
			Random randomGenerator = new Random();
			vitesse_au_pic = (randomGenerator.nextInt(6) +1) *100;

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
				Thread.sleep(500 + vitesse_au_pic);
			}
		} catch (InterruptedException e) {
			return;

		}

	}

}
