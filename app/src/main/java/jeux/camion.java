package jeux;

import android.content.Context;

import nicolas.console.pr.R;
import java.util.Random;

public class camion extends Jeux_generique{
	struct_ball_dans_remorque[] tab_balle_dans_remorque;
	final static int nombre_de_balles_max_ds_remorque = 30;

	public camion(Context C) {
        bonhomme1[0] = C.getResources().getDrawable(
                R.drawable.camion);
	}

	public void initialise_camion() {
		tab_balle_dans_remorque = new struct_ball_dans_remorque[nombre_de_balles_max_ds_remorque + 2];
		int b = 0;
		for (b = 0; b < nombre_de_balles_max_ds_remorque; b++) {
			tab_balle_dans_remorque[b] = new struct_ball_dans_remorque();

		}
		setPosition_y((int) (this.getHauteur_de_l_ecran()*0.8));
	}

	public void decremente_balle_dans_cammion(int num_balle) {

		if (getNombre_de_balle_dans_la_remorque() < 1)
			return;
		enleve_la_balle_de_l_emplacement(num_balle);
		setNombre_de_balle_dans_la_remorque(getNombre_de_balle_dans_la_remorque() - 1);
	}

	public void incremente_balle_dans_cammion(int num_balle) {
		if (getNombre_de_balle_dans_la_remorque() > nombre_de_balles_max_ds_remorque)
			return;
		setNombre_de_balle_dans_la_remorque(getNombre_de_balle_dans_la_remorque() + 1);

	}

	private void met_dans_un_emplacement_libre(int num_balle,
			int nombre_de_balle_dans_la_remorque) {

		short num_pos = 0;

		for (num_pos = 0; num_pos < nombre_de_balles_max_ds_remorque; num_pos++) {
			if (tab_balle_dans_remorque[nombre_de_balle_dans_la_remorque].numero_balle == 0) {
				// s'il n y a pas de balle dans cet emplacement
				tab_balle_dans_remorque[nombre_de_balle_dans_la_remorque].numero_balle = num_balle;
				return;

			}
		}

	}

	private void enleve_la_balle_de_l_emplacement(int num_balle) {
		short num_pos = 0;
		for (num_pos = 0; num_pos < nombre_de_balles_max_ds_remorque; num_pos++) {
			if (tab_balle_dans_remorque[getNombre_de_balle_dans_la_remorque()].numero_balle == num_balle) {
				// si la balle concernee est dans cet emplacement, on l enleve
				tab_balle_dans_remorque[getNombre_de_balle_dans_la_remorque()].numero_balle = 0;
				return;

			}
		}

	}

	public void calcul_coordonees_remorques() {
		position_remorque_B = position_x+ largeur_remorque;

	}

	public void calcul_coordonees_dela_balle_dans_la_remorque(int num_b) {
		if (getNombre_de_balle_dans_la_remorque() > 0) {
			if (getNombre_de_balle_dans_la_remorque() < nombre_de_balles_max_ds_remorque) {
				int b;
				for (b = 0; b < getNombre_de_balle_dans_la_remorque(); b++) {
					if (tab_balle_dans_remorque[b].numero_balle == num_b) {
						calcul_postion_balle_en_fonction_de_son_index(b);
						return;
					}
				}

			}
		}
	}

	private void calcul_postion_balle_en_fonction_de_son_index(int index) {

		int pos_x_au_pif = 0;
		int pos_y_au_pif = 0;
		Random randomGenerator = new Random();
		pos_x_au_pif = randomGenerator.nextInt(10);
		pos_y_au_pif = randomGenerator.nextInt(10);

		switch (index) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
			position_x_balle_dans_remorque = position_x + (index * 30)
					+ pos_x_au_pif;
			position_y_balle_dans_remorque = getPosition_y()+ pos_y_au_pif
					+ 40;
			// debug = String.format("1_index %x",index);
			// System.out.println(debug);
			break;
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
		case 10:
			index -= 5;
			position_x_balle_dans_remorque = position_x + (index * 30)
					+ pos_x_au_pif;
			position_y_balle_dans_remorque = getPosition_y() - 20
					+ pos_y_au_pif + 40;
			// debug = String.format("2_index %x",index);
			// System.out.println(debug);
			break;

		}
	}

	public int position_x_balle_dans_remorque = 0;
	public int position_y_balle_dans_remorque = 0;

//	public int position_x_camion;
	//public int position_y_camion;
	//public int position_remorque_A = 0;
	public int position_remorque_B = 0;
	private int nombre_de_balle_dans_la_remorque = 0;
	public int largeur_remorque = 200;
	public int hauteur_remorque = 100;

	public void met_a_jour_nombre_balle_camion(int nb_balle) {
		setNombre_de_balle_dans_la_remorque(nb_balle);
	}

	private void avance_camion() {

	}

	public int getNombre_de_balle_dans_la_remorque() {
		return nombre_de_balle_dans_la_remorque;
	}

	public void setNombre_de_balle_dans_la_remorque(int nombre_de_balle_dans_la_remorque) {
		this.nombre_de_balle_dans_la_remorque = nombre_de_balle_dans_la_remorque;
	}
}
