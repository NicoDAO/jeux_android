package jeux;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;

import java.util.Random;

import nicolas.console.pr.R;

public class balle extends Jeux_generique {

	public balle(Context C) {
		//image_avion = new Drawable[1];
       this.context = C;

	}

	public boolean thread_lance = false;

	//public int position_x = 0;
	//public int position_y;
	public boolean balle_affichee;
	public boolean balle_attrapee = false;
	public boolean balle_echappee = false;
	public boolean balle_echappee_info = false;
	public boolean balle_dans_camion_info = false;

	public boolean sens;
	public int vitesse = 2;
	public int couleur_au_pif = 0;
	public int son_au_pif = 0;
	int compteur_reaparition = 0;
	public boolean son1_a_relancer = false;
	public boolean son2_a_relancer = false;
	public boolean son3_a_relancer = false;

	public int compteur_clignote;
	private int quel_avion = 0;
	public boolean avion_vert;
	public boolean mutex_son = false;

	public static int coordonnees_remorques_X;
	public static int coordonnees_remorques_Y;
	public boolean balle_dans_la_remorque = false;
	public boolean balle_dans_le_camion_etat_avant = false;
	public int statut_balle = 0;
	private static final int init_la_balle = 1;
	public static final int balle_en_l_air = 2;
	public static final int balle_est_attrapee = 3;
	public static final int balle_dans_le_camion = 4;
	static final int balle_est_echappee = 5;
	static final int balle_tombe = 6;
	public static final int balle_perdu = 7;
	private static byte num_balle_lance = 0;
	private int compteur_au_pif = 0;
	private short duree_mutex = 0;
	public static int largeur_ecran;
	public static int hauteur_ecran;
	private static int coordonnees_X_de_retour;
	private int pos_au_pif_dans_camion_X;
	private int pos_au_pif_dans_camion_Y;

	private int limite_Basse = 0;

	private int[] carto_vitesse = { 9, 7, 4, 3, 2, 1, 2, 3, 4, 7, 9 };
	private int[] carto_vitesse_nrmalle = { 10, 25, 30, 35, 35, 30, 25, 20, 1,
			1 };
	// private int[] carto_vitesse_chute = { 10, 9, 8, 7, 6, 5, 3, 3, 2, 2, 2 };
	private int[] carto_vitesse_chute = { 8, 7, 6, 6, 6, 5, 4, 3, 2, 1, 1, 1, 1 };
	private int[] carto_angle = { 100, 96, 96, 95, 93, 90, 70, 40, 30, 10 };
	public static byte nombre_de_niveau = 4;
	private int[][] position_balle_depart_X = {
			{ 10, 10, 10, 10, 10, 10, 10, 10, 10, 10 },
			{ 10, 100, 10, 100, 10, 100, 10, 100, 10, 100 },
			{ 100, 96, 96, 95, 93, 90, 70, 40, 30, 10 },
			{ 100, 96, 96, 95, 93, 90, 70, 40, 30, 10 } };
	private int[][] position_balle_depart_Y = {
			{ 10, 10, 10, 10, 10, 10, 10, 10, 10, 10 },
			{ 10, 40, 50, 90, 100, 130, 140, 150, 180, 200 },
			{ 100, 96, 96, 95, 93, 90, 70, 40, 30, 10 },
			{ 100, 96, 96, 95, 93, 90, 70, 40, 30, 10 } };
	private int[][] sens_balle = { { +1, +1, +1, +1, +1, +1, +1, +1, +1, +1 },
			{ +1, -1, +1, -1, +1, -1, +1, -1, +1, -1 },
			{ 100, 96, 96, 95, 93, 90, 70, 40, 30, 10 },
			{ 100, 96, 96, 95, 93, 90, 70, 40, 30, 10 } };

	private int[][] top_depart = {
			{ +1, +10, +20, +30, +40, +50, +60, +70, +80, +90 },
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 100, 96, 96, 95, 93, 90, 70, 40, 30, 10 },
			{ 100, 96, 96, 95, 93, 90, 70, 40, 30, 10 } };

	public static byte niveau = 0;

	public static int getInit_la_balle() {
		return init_la_balle;
	}
	private int etatJeuFini = 0;


	// http://bruce-eckel.developpez.com/livres/java/traduction/tij2/?chap=3&page=0
	public void initialise_balle() {
		Random randomGenerator = new Random();
		couleur_au_pif = randomGenerator.nextInt(6);
		Random randomGenerator2 = new Random();
        //image_avion = new Drawable[1];

		son_au_pif = randomGenerator2.nextInt(3);
		quel_avion = randomGenerator.nextInt(20);
		avion_vert = false;
		largeur = 100;
		if (quel_avion < 3){
            image[0] =  context.getResources().getDrawable(
                    R.drawable.soucoupe_vert);
            avion_vert = true;
        }else{

            image[0] =  context.getResources().getDrawable(
                    R.drawable.ship);

        }

	}

	private int position_X_avant_depart;
	private int position_Y_avant_depart;

	private int calcul_vitesse_balle_en_l_air(int position_x, boolean sens) {

		// int vitesse_cible = 1;
		int vitesse_c = 0;
		int index = 0;
		index = ((position_x * 10) / largeur_ecran);
		if (index < 11)
			vitesse_c = carto_vitesse_nrmalle[index];

		return vitesse_c;
	}

	private int duree_sleep = 100;
    private camion le_cam;
    public void configure_le_camion(camion le_cam){
        this.le_cam = le_cam;
        //this.le_cam.position_y;
    }

	private void incremente_position() {
		switch (statut_balle) {
		case init_la_balle:
			if(niveau >4)break;
			if(num_balle_lance >10){
					num_balle_lance = 1;
		}
			Random randomGenerator = new Random();
			compteur_au_pif = randomGenerator.nextInt(50) + 1000;
			compteur_reaparition = 0;

			balle_dans_le_camion_etat_avant = false;
			if ((num_balle_lance > 1)) {
				position_y = position_balle_depart_X[niveau][num_balle_lance - 1];
			}
			position_y = 10;
			largeur = 100;
			sens = true;
			vitesse = 2;
			duree_sleep = 50;
			coordonnees_X_de_retour = largeur_ecran - largeur;
			if (top_depart[niveau][num_balle_lance - 1]-- < 0) {
				statut_balle = balle_en_l_air;
				vitesse = sens_balle[niveau][num_balle_lance - 1];
			}

			break;
		case balle_en_l_air:
			angle = 0;
			duree_sleep = (50 - calcul_vitesse_balle_en_l_air(position_x, sens))/2;

			vitesse = 2;
			if (sens) {
				position_x += vitesse;// vitesse;
				if (position_x < 10) {

					position_y += 5;

				} else if (position_x < 30) {

					position_y +=3;

				} else if (position_x > (coordonnees_X_de_retour)) {

					sens = false;

				} else if (position_x > (coordonnees_X_de_retour - 10)) {

					position_y += 4;

				} else if (position_x > (coordonnees_X_de_retour - 30)) {

					position_y += 2;

				}
			} else {
				position_x -= vitesse;
				if (position_x > (coordonnees_X_de_retour - 10)) {

					position_y += 4;

				} else if (position_x > (coordonnees_X_de_retour - 30)) {

					position_y += 2;

				} else if (position_x < 0) {
					sens = true;

				} else if (position_x < 10) {

					position_y += 4;

				} else if (position_x < 30) {

					position_y += 2;

				}
			}
			if (position_y >this.limite_Basse )
				statut_balle = balle_perdu;
			break;
		case balle_est_attrapee:
			position_Y_avant_depart = position_y;
			statut_balle = balle_tombe;
            setX_score(position_x);
            setY_score(position_y);
            setScore_a_afficher(true);
            duree_vie_score = 100;
			break;
		case balle_tombe:
			vitesse = calcul_vitesse_chute_balle(position_y);
			//System.out.println("balle tombe y =" +position_y + " remorque :" + hauteur_ecran );
            if (sens)
				position_x++;
			if (!sens)
				position_x--;
			duree_sleep = 50 / vitesse;
			position_y += vitesse;
			Random randomGenerator1 = new Random();
			pos_au_pif_dans_camion_X = randomGenerator1.nextInt(100);
			pos_au_pif_dans_camion_Y = randomGenerator1.nextInt(10);
			if (position_y > this.le_cam.position_y){// coordonnees_remorques_Y) {
				statut_balle = balle_dans_le_camion;
				balle_dans_camion_info = true;
			}
			break;
		case balle_dans_le_camion:
			duree_sleep = 100;
			if (compteur_reaparition < compteur_au_pif * 0.75) {
				if (largeur > 60)
					largeur--;
			} else {
				if (largeur < 100)
					largeur++;
			}
            //System.out.println("balle_dans_le_camion =" +this.le_cam.position_x + "  :" + position_y );

            position_x = this.le_cam.position_x + pos_au_pif_dans_camion_X;
			position_y = this.le_cam.position_y + pos_au_pif_dans_camion_Y;
			if (compteur_reaparition++ > compteur_au_pif) {
				statut_balle = balle_est_echappee;
				duree_sleep = 20;

			}

			vitesse++;
			position_X_avant_depart = position_x;
			position_Y_avant_depart = position_y;
			if (vitesse > 8)
				vitesse = 2;
			sens = true;
			break;
		case balle_est_echappee:

			balle_echappee_info = true;
			calcul_position_saut();

			balle_dans_la_remorque = false;
			if (position_x <= 0) {
				son3_a_relancer = true;
				position_y = 10;
				statut_balle = getInit_la_balle();
			}
			break;

			case balle_perdu:
				etatJeuFini = 1;
				break;

		}

		if (mutex_son == true) {
			if (duree_mutex++ > 500) {
				mutex_son = false;
				duree_mutex = 0;
			}

		}

	}

    public int getCouleur() {
		int retour = Color.BLACK;
		switch (couleur_au_pif) {
		case 0:
			retour = Color.YELLOW;
			break;
		case 1:
			retour = Color.RED;
			break;

		case 2:
			retour = Color.BLUE;
			break;

		case 3:
			retour = Color.GREEN;
			break;

		case 4:
			retour = Color.WHITE;
			break;

		case 5:
			retour = Color.CYAN;
			break;
		case 6:
			retour = Color.MAGENTA;
			break;

		}

		return retour;

	}

    private int calcul_vitesse_chute_balle(int position_y_C) {
		int index = 0;
		int vitesse_chute = 1;
		index = (position_Y_avant_depart * 10) / position_y_C;
		vitesse_chute = carto_vitesse_chute[index];
		return vitesse_chute;
	}

	private void calcul_position_saut() {
		int position_medianne_X;
		int coef_pos;
		int vitesse_Y;
		vitesse_Y = 1;

		position_medianne_X = position_X_avant_depart / 2;

		coef_pos = (position_x * 10) / position_X_avant_depart;

		if ((coef_pos < 11) && (coef_pos >= 0))
			vitesse_Y = carto_vitesse[coef_pos];
		vitesse = (char) vitesse_Y;
		duree_sleep = 50 / vitesse;
		if (position_x > position_medianne_X)
			position_y -= vitesse_Y;
		if (position_x < position_medianne_X)
			position_y += vitesse_Y;
		angle++;
		position_x--;

	}

	public int Get_Statut_de_la_Balle() {
		return statut_balle;
	}

	public void incremente_num_balle() {
		num_balle_lance++;
	}

	@Override
	public void run() {

		try {
			// while (etat) {
			while (!Thread.currentThread().isInterrupted()) {
				incremente_position();
				Thread.sleep(duree_sleep);
			}
		} catch (InterruptedException e) {

			return;

		}

	}


    public void dessine(Canvas canvas) {
        if (balle_affichee == true) {
            canvas.save();
            canvas.rotate(angle,
                    position_x,
                    position_y);
            image[0].setBounds(position_x,
                    position_y,
                    position_x
                            + largeur,
                    position_y
                            + largeur);


            image[0].draw(canvas);
            canvas.restore();
        }

    }

	public int getEtatJeuFini() {
		return etatJeuFini;
	}

    public int getLimite_Basse() {
        return limite_Basse;
    }

    public void setLimite_Basse(int limite_Basse) {
        this.limite_Basse = limite_Basse;
    }
}
