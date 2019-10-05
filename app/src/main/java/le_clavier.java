


public class le_clavier {
	public class class_touche {
		String message;
		String type;
		String texte;
		byte menu_d_apres;
		boolean menu_sortie;
		boolean sortie_activite;
		boolean touche_envoie_message_direct;

	}

	public class la_class_clavier {

		class_touche[] les_touches;

		la_class_clavier() {
			les_touches = new class_touche[20];
		}

		byte nombre_de_touches;
	}


}
