package jeux;

public class gestion_niveaux {
	public gestion_niveaux() {

	}

	public String nom_niveau = "niveau 0";
	private byte num_niveau = 0;
	
	public byte get_niveau(){
		return num_niveau;
	}
	public void increment_niveau() {
		num_niveau++;
		nom_niveau = String.format("NIVEAU %d", num_niveau);

	}
	public int affiche_niveau_duree;
	public boolean test_si_niveau_fini(int nbre_de_balle_attrapees) {

		switch (num_niveau) {
		case 0:
			return false;
		case 1:
			if (nbre_de_balle_attrapees > 5)
				return true;
			break;
		case 2:
			if (nbre_de_balle_attrapees > 7)
				return true;
			break;
		case 3:
			if (nbre_de_balle_attrapees > 9)
				return true;
			break;

		}

		return false;
	}

}
