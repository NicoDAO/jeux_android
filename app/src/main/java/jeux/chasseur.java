package jeux;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import nicolas.console.pr.R;

public class chasseur extends Jeux_generique {

    // int position_x_camion;
    public int position_y_camion;
    private int etat1 = 0;
    private int etat2 = 0;
    private int etat_torse = 0;
    public boolean thread_lance = false;
    public boolean avance_le_chasseur = false;

    public int angle1 = 0;
    public boolean translate1 = false;
    public boolean tourne1 = false;
    private byte num_pas_patte1 = 0;
    private byte num_pas_patte2 = 0;
    public int angle2 = 0;
    public boolean translate2 = false;
    public boolean tourne2 = false;

    public int x_patte_1_bas = 30;
    public int y_patte_1_bas = 600;
    public int x_patte_1_haut = 60;
    public int y_patte_1_haut = 730;

    public int x_patte_2_bas = 90;
    public int y_patte_2_bas = 600;
    public int x_patte_2_haut = 120;
    public int y_patte_2_haut = 730;

    public int x_torse_bas = 40;
    public int y_torse_bas = 600;
    public int x_torse_haut = 110;
    public int y_torse_haut = 400;

    public int X_bras_droite = 80;
    public int Y_bras_droite = 60;
    public int largeur_bras_droite = 100;
    public int hauteur_bras_droite = 100;

    public int X_bras_gauche = 10;
    public int Y_bras_gauche = 10;
    public int largeur_bras_gauche = 50;
    public int hauteur_bras_gauche = 50;
    public int angle_bras_droite = 0;
    private int pos_chasseur = 0;
    public byte etat_chasseur;
    Drawable[] image_bras_gauche;
    Drawable[] image_bras_droite;
    public boolean affiche_torse = false;
    private byte sens = 1;
    static final int nbre_membre_chasseur = 7;
    public chasseur(Context C) {
        this.context = C;
        translate1 = true;
        tourne1 = true;

        angle1 = 10;

        translate2 = true;
        tourne2 = true;

        angle2 = -10;
        image= new Drawable[nbre_membre_chasseur + 1];
        image_bras_droite = new Drawable[1];
        image_bras_gauche = new Drawable[1];

        image_bras_droite[0] = C.getResources().getDrawable(
                R.drawable.bras_droite);

        image_bras_gauche[0] = C.getResources().getDrawable(
                R.drawable.bras_gauche);
        image[2] = C.getResources().getDrawable(
                R.drawable.corps);
        image[3] = C.getResources().getDrawable(
                R.drawable.tete);
        image[1] = C.getResources().getDrawable(
                R.drawable.jambes_droite);
        image[0] = C.getResources().getDrawable(
                R.drawable.jambes_gauche);
    }


    void intialise_bonhomme() {
        int bas = (int) (getHauteur_de_l_ecran() * 0.8);
        y_patte_1_bas = bas;
        x_patte_1_bas = 30;
        y_patte_1_bas = bas;
        x_patte_1_haut = 60;
        y_patte_1_haut = bas + 130;

        x_patte_2_bas = 90;
        y_patte_2_bas = bas;
        x_patte_2_haut = 120;
        y_patte_2_haut = bas + 130;

    }

    public int bound_x1, y1, x2, y2 = 0;

    public void cree_image() {
    }

    public void init_chasseur() {
    }

    private int x_patte_1_bas_init;
    private int x_patte_2_bas_init;
    private int x_tete_chasseur;
    private int y_tete_chasseur;
    private int x_chasseur_d_avant = 0;
    private int x_chasseur = 0;
    private byte sens1 = 0;

    private void avance_patte_1() {

        switch (etat1) {
            case 0:
                num_pas_patte1++;
                if (x_patte_1_bas < x_chasseur) {

                    sens1 = (-1);
                } else {

                    sens1 = 1;
                }
                angle1 = 0;
                x_patte_1_bas = x_chasseur - 30;
                etat1 = 4;
                break;
            case 1:
            case 2:
            case 3:
            case 4:

                translate1 = true;
                tourne1 = true;

                if (sens1 == (-1))
                    angle1 -= 2;
                if (sens1 == 1)
                    angle1 += 2;

                etat1++;

                break;

            case 5:
                etat1 = 0;

                break;

        }

    }

    private void avance_patte_2() {
       switch (etat2) {
            case 0:
                num_pas_patte2++;
                if (x_patte_2_bas < pos_chasseur) {

                    sens = (-1);
                } else {
                    x_patte_2_bas -= 1;
                    sens = 1;
                }
                angle2 = 0;
                x_patte_2_bas = pos_chasseur +30 ;
                etat2 = 4;
                break;
            case 1:
            case 2:
            case 3:
            case 4:

                translate2 = true;
                tourne2 = true;

                if (sens == (-1))
                    angle2 -= 2;
                if (sens == 1)
                    angle2 += 2;

                etat2++;

                break;

            case 5:
                etat2 = 0;

                break;

        }

    }

    private int offset_tete = 50;

    public void init_pos_chasseur() {
        sens = 1;

        intialise_bonhomme();

    }

    private byte sens_offset = 0;
    private int offset = 0;

    private int calcul_offset_tete() {

        switch (sens_offset) {
            case 0:
                if (offset++ > 80) sens_offset = 1;
                break;
            case 1:
                if (offset-- < 60) sens_offset = 0;
                break;
        }

        return offset;
    }

    private void avance_chasseur() {
        if (getPos_chasseur() > x_chasseur) {
            x_chasseur += 10;

        } else
            x_chasseur -= 10;
        y_tete_chasseur = y_torse_bas - calcul_offset_tete();


        // x_chasseur = pos_chasseur;
        avance_patte_1();
        avance_patte_2();
        avance_torse();
        avance_bras();

    }

    private void avance_bras() {
        X_bras_droite = x_chasseur + 20;
        X_bras_gauche = x_chasseur - 70;
        hauteur_bras_droite = 100;
        hauteur_bras_gauche = 100;
        largeur_bras_droite = 100;
        largeur_bras_gauche = 100;

        Y_bras_droite = y_torse_bas;
        Y_bras_gauche = y_torse_bas;
        if (etat_chasseur == 1) {
            angle_bras_droite = (-40);
        } else {
            angle_bras_droite = 0;
        }

    }

    private void avance_torse() {

        switch (etat_torse) {
            case 0:

                etat_torse++;

                break;
            case 1:
                affiche_torse = true;
                x_torse_bas = x_chasseur - 20;
                x_torse_haut = x_chasseur + 80; // + 30;//x_patte_1_haut;
                y_torse_haut = y_patte_1_bas + 40;
                y_torse_bas = y_torse_haut - 100;
                x_tete_chasseur = x_chasseur - 20;

                etat_torse = 0;
                break;
            case 2:
                etat_torse++;
                break;
            case 3:
                etat_torse = 4;

                break;

            case 4:
                affiche_torse = true;
                x_torse_bas = x_patte_1_bas - 20;
                x_torse_haut = x_patte_2_bas + 80; // + 30;//x_patte_1_haut;
                y_torse_haut = y_patte_1_bas + 40;
                y_torse_bas = y_torse_haut - 100;

                etat_torse = 5;
                break;

            case 5:
                etat_torse = 0;
                break;

        }

    }

    private void recule_chasseur() {

    }
    @Override
    public void dessine_jeux(Canvas canvas){
        canvas.rotate(angle_bras_droite,X_bras_droite,
                Y_bras_droite);
        image_bras_droite[0].setBounds(X_bras_droite,
                Y_bras_droite, X_bras_droite
                        + largeur_bras_droite,
                Y_bras_droite + hauteur_bras_droite);
        image_bras_droite[0].draw(canvas);
        canvas.restore();

        image_bras_gauche[0].setBounds(X_bras_gauche,
               Y_bras_gauche, X_bras_gauche
                        +largeur_bras_gauche,
                Y_bras_gauche + hauteur_bras_gauche);
        image_bras_gauche[0].draw(canvas);
        if (tourne1) {
            canvas.save();
            canvas.rotate(angle1, x_patte_1_haut + 20,
                    y_patte_1_haut - 200);
            image[0].setBounds(x_patte_1_bas,
                    y_patte_1_bas, x_patte_1_bas + 40,
                    y_patte_1_haut);
            image[0].draw(canvas);
            canvas.restore();
        }
        if (tourne2) {
            canvas.save();
            canvas.rotate(angle2, x_patte_2_haut + 20,
                    y_patte_2_haut - 200);
            image[1].setBounds(x_patte_2_bas,
                    y_patte_2_bas, x_patte_2_bas + 40,
                    y_patte_2_haut);
            image[1].draw(canvas);
            canvas.restore();
        }
        image[2].setBounds(x_torse_bas,
                y_torse_bas, x_torse_haut,
                y_torse_haut);

        image[2].draw(canvas);
        image[3].setBounds(x_tete_chasseur,
                y_tete_chasseur, x_tete_chasseur + 100,
                y_tete_chasseur + 100);
        image[3].draw(canvas);

    }
    @Override
    public void run() {

        try {
            // while (etat) {
            while (!Thread.currentThread().isInterrupted()) {
                // incremente_position();
                // Thread.sleep(vitesse_au_pic);
                if (avance_le_chasseur) {
                    avance_chasseur();

                }
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            return;

        }

    }

    public int getPos_chasseur() {
        return pos_chasseur;
    }

    public void setPos_chasseur(int pos_chasseur) {
        this.pos_chasseur = pos_chasseur;
    }
}
