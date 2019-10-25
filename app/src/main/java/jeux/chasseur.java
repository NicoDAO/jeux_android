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

    private int x_patte_1_bas = 30;
    private int y_bonhomme = 600;
    private int x_patte_1_haut = 60;
    private int y_patte_1_haut = 730;

    private int x_patte_2_bas = 90;


    private int x_torse_bas = 40;
    private int y_torse_bas = 600;
    private int x_torse_haut = 110;
    private int y_torse_haut = 500;


    private int pos_chasseur = 0;
    private byte etat_chasseur;
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
        bonhomme1 = new Drawable[nbre_membre_chasseur + 1];

        bonhomme1[1] = C.getResources().getDrawable(
                R.drawable.bonhomme1);
        bonhomme1[0] = C.getResources().getDrawable(
                R.drawable.bonhomme2);
    }


    void intialise_bonhomme() {
        int bas = (int) (getHauteur_de_l_ecran() * 0.8);
        y_bonhomme = bas;
        x_patte_1_bas = 30;
        x_patte_1_haut = 60;
        y_patte_1_haut = bas + 130;

        x_patte_2_bas = 90;

        x_tete_chasseur = 0;
        y_tete_chasseur = 0;
        x_chasseur_d_avant = 0;
        x_chasseur = 0;

    }

    public int bound_x1, y1, x2, y2 = 0;

    public void cree_image() {
    }



    private int x_tete_chasseur;
    private int y_tete_chasseur;
    private int x_chasseur_d_avant = 0;
    private int x_chasseur = 0;
    private byte sens1 = 0;


    public void init_pos_chasseur() {
        sens = 1;

        intialise_bonhomme();

    }


    private void avance_chasseur() {
        if (getPos_chasseur() > x_chasseur) {
            x_chasseur += 10;

        } else
            x_chasseur -= 10;

        avance_torse();


    }



    private void avance_torse() {

        switch (etat_torse) {
            case 0:
                etat_torse++;
                break;
            case 1:
                affiche_torse = true;
                x_torse_bas = x_chasseur - 20;
                setX_torse_haut(x_chasseur + 80); // + 30;//x_patte_1_haut;
                setY_torse_haut(y_bonhomme + 40);
                y_torse_bas = getY_torse_haut() - 100;
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
                setX_torse_haut(x_patte_2_bas + 80); // + 30;//x_patte_1_haut;
                setY_torse_haut(y_bonhomme + 40);
                y_torse_bas = getY_torse_haut() - 100;

                etat_torse = 5;
                break;

            case 5:
                etat_torse = 0;
                break;

        }

    }

    @Override
    public void dessine_jeux(Canvas canvas) {

        canvas.save();
        if(select_image_bonhome == 0){
            bonhomme1[0].setVisible(true,false);
            bonhomme1[1].setVisible(false,false);
            bonhomme1[0].setBounds(x_torse_bas,
                    y_torse_bas, x_torse_bas + 240,
                    y_torse_haut);
            bonhomme1[0].draw(canvas);
        }
        if(select_image_bonhome == 1){
            bonhomme1[1].setVisible(true,false);
            bonhomme1[0].setVisible(false,false);
            bonhomme1[1].setBounds(x_torse_bas,
                    y_torse_bas, x_torse_bas + 240,
                    y_torse_haut);
            bonhomme1[1].draw(canvas);
        }

        canvas.restore();


    }
    int select_image_bonhome = 0;
    int cpt = 0;
    @Override
    public void run() {

        try {
            // while (etat) {
            while (!Thread.currentThread().isInterrupted()) {
                // incremente_position();
                // Thread.sleep(vitesse_au_pic);
                if(( cpt  % 10)== 1)select_image_bonhome = 0;
                if(( cpt  % 20) == 1)select_image_bonhome = 1;
                cpt++;
                if (avance_le_chasseur) {
                    avance_chasseur();


                }
                Thread.sleep(33);
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

    public byte getEtat_chasseur() {
        return etat_chasseur;
    }

    public void setEtat_chasseur(byte etat_chasseur) {
        this.etat_chasseur = etat_chasseur;
    }

    public int getX_torse_haut() {
        return x_torse_haut;
    }

    public void setX_torse_haut(int x_torse_haut) {
        this.x_torse_haut = x_torse_haut;
    }

    public int getY_torse_haut() {
        return y_torse_haut;
    }

    public void setY_torse_haut(int y_torse_haut) {
        this.y_torse_haut = y_torse_haut;
    }
}
