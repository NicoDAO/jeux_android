package jeux;


public class poing extends Jeux_generique {
	public poing() {

	}
	private int[] carto_vitesse = {11,10, 9, 7, 5, 5, 5, 4, 2, 2, 1};
	private int[] carto_angle 	= {100, 96, 96, 95, 93, 90, 70, 40, 30, 10};
	
	public void initialise_poing(int Y_depart) {

	}
	
	public boolean thread_lance = false;
	public boolean poing_initialise = false;
	public int position_initiale_X = 0;
	public int position_initiale_Y = 0;
	public int offset_Y = 0;
	public int offset_position_initiale = 0;
	public int X_poing;
	public int Y_poing;
	private int x_cible;
	private int y_cible;
	private byte etat_lance_le_poing = 0;
	private int duree_sleep = 200;
	public boolean le_poing_est_parti = false;
	public int angle_poing = 0;
	private int angle_temp = 0;

	public void lance_le_poing_vers_cible(int x, int y) {
		
		x_cible = x;
		y_cible = y;
		if (etat_lance_le_poing == 0) {
			position_initiale_X = X_poing;
			position_initiale_Y = Y_poing;

		}
		etat_lance_le_poing = 1;

	}

	public void initialise_position_poing() {
		int y = 0;// hauteur de l ecran
		int y1 = 0;
		for (y = 0; y < 800; y++) {
			y1 = (position_initiale_X * 7) + (position_initiale_Y) / 2 + y;
			if ((y1 > 400) && (y1 < 450)) {
				offset_position_initiale = y;
				break;
			}
		}
	}

	public int calcul_Y1() {
		// return (pos_Y * 7) + (pos_X) / 2 + 500 + (reduit_suite_capture * 3);
		return 0;
	}
	private int calcul_vitesse(int y_p)
	{
		int y_poing_calcule = 0;
		int coef = 0;
		int vitesse;
		if(y_cible<0)return 5;
		//if(y_cible>y_p)return 1;
		if(y_p == 0)return 5;
		
		
		coef = (y_cible * 10) / y_p;
		if(coef>10)coef =10;
		if(coef<0)coef =0;
		vitesse = carto_vitesse[coef];

		
		return vitesse;
	}
	private int y_poing_depard = 0;
	private void incremente_position_poing() {
		int vitesse = 0;
		switch (etat_lance_le_poing) {
		case 0:
			duree_sleep = 200;
			angle_temp = 0;
			//y_poing_depard = y_poing;
			le_poing_est_parti = false;
			break;
		case 1: //le poing vas vers la cible
			//calcul_vitesse(Y_poing);
			
			vitesse = calcul_vitesse(Y_poing);
			Y_poing -=vitesse;
			duree_sleep = (40/vitesse) + 5;
//			String debug;
//			debug = String.format("vitesse=%d,sleep=%d", vitesse,duree_sleep);
//			System.out.println(debug);
			angle_temp += 4;
			angle_poing = ((angle_temp % 360));
			le_poing_est_parti = true;
			if (Y_poing < (y_cible)) {

				etat_lance_le_poing = 2;

			}

			break;
		case 2://le poing revient au point de port
			vitesse = calcul_vitesse(Y_poing);
			Y_poing +=vitesse;
			duree_sleep = (40/vitesse) + 5;
			angle_temp += 4;
			angle_poing = ((angle_temp % 360));
			if (Y_poing > (position_initiale_Y)){
				etat_lance_le_poing = 0;

			}
			break;
		case 3:
			break;

		}

	}

	@Override
	public void run() {

		try {
			// while (etat) {
			while (!Thread.currentThread().isInterrupted()) {

				incremente_position_poing();
				Thread.sleep(duree_sleep);
			}
		} catch (InterruptedException e) {
			return;

		}

	}

}
