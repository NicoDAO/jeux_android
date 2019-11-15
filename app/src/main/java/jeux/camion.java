package jeux;

import android.content.Context;

import nicolas.console.pr.R;

import java.util.Random;
import java.util.Vector;

public class camion extends Jeux_generique {
    Vector <struct_ball_dans_remorque> vecteur_balle = new Vector <struct_ball_dans_remorque>();

    final static int nombre_de_balles_max_ds_remorque = 30;

    public camion(Context C) {
        bonhomme1[0] = C.getResources().getDrawable(
                R.drawable.camion);
    }

    public void initialise_camion() {

       vecteur_balle.clear();
        setPosition_y((int) (this.getHauteur_de_l_ecran() * 0.8));
    }


    public void incremente_balle_dans_cammion(int num_balle) {
          vecteur_balle.add(new struct_ball_dans_remorque());

    }

    public void calcul_coordonees_remorques() {
        setPosition_remorque_B(position_x_camion + largeur_remorque);

    }

    public void calcul_coordonees_dela_balle_dans_la_remorque(int num_b) {
        if (getNombre_de_balle_dans_la_remorque() > 0) {
            if (getNombre_de_balle_dans_la_remorque() < vecteur_balle.size()) {
                for (int b = 0; b < getNombre_de_balle_dans_la_remorque(); b++) {
                    //  if (tab_balle_dans_remorque[b].numero_balle == num_b) {
                    if (vecteur_balle.get(b).numero_balle == num_b) {
                        calcul_postion_balle_en_fonction_de_son_index(b);
                        return;
                    }
                }

            }
        }
    }

    private void calcul_postion_balle_en_fonction_de_son_index(int index) {

        int pos_x_au_pif;
        int pos_y_au_pif;
        Random randomGenerator = new Random();
        pos_x_au_pif = randomGenerator.nextInt(10);
        pos_y_au_pif = randomGenerator.nextInt(10);

        switch (index) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                position_x_balle_dans_remorque = position_x_camion + (index * 30)
                        + pos_x_au_pif;
                position_y_balle_dans_remorque = getPosition_y() + pos_y_au_pif
                        + 40;
               break;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                index -= 5;
                position_x_balle_dans_remorque = position_x_camion + (index * 30)
                        + pos_x_au_pif;
                position_y_balle_dans_remorque = getPosition_y() - 20
                        + pos_y_au_pif + 40;
                // debug = String.format("2_index %x",index);
                // System.out.println(debug);
                break;

        }
    }

    private int position_x_balle_dans_remorque = 0;
    private int position_y_balle_dans_remorque = 0;

    private int position_remorque_B = 0;
    private int nombre_de_balle_dans_la_remorque = 0;
    public int largeur_remorque = 200;

    public int getNombre_de_balle_dans_la_remorque() {
        return vecteur_balle.size();
       // return nombre_de_balle_dans_la_remorque;
    }

    public int getPosition_remorque_B() {
        return position_remorque_B;
    }

    public void setPosition_remorque_B(int position_remorque_B) {
        this.position_remorque_B = position_remorque_B;
    }
}
