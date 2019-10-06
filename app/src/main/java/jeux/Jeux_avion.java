package jeux;


import android.app.Activity;
import android.os.Bundle;
import android.content.Context;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.Math;

import nicolas.console.pr.R;
import nicolas.console.pr.R.raw;

import android.graphics.drawable.Drawable;

import audio.jouer_son;

public class Jeux_avion extends Activity implements OnTouchListener {
    private static final int nbre_bal = 10;
    private static final int nbre_nuage = 5;
    private static final int nbre_membre_chasseur = 7;
    private SensorManager mSensorManager;
    private Capteur mcapteur;

    private Paint mPaint;
    private int[] mColors = null;
    private int orientation_X = 0;
    private int orientation_Y = 0;
    private int orientation_Z = 0;
    private float magnetic_1 = 0;
    private float magnetic_2 = 0;
    private float magnetic_3 = 0;
    private float accel_X = 0;
    private float accel_Y = 0;
    private float accel_Z = 0;
    private int x1;
    private int x2;
    private int y1;
    private int y2;
    private int offset_orientation_X = 0;
    private int offset_orientation_Y = 0;
    private int offset_orientation_Z = 0;
    private float offset_magnetic_1 = 0;
    private float offset_magnetic_2 = 0;
    private float offset_magnetic_3 = 0;
    private char Rouge = 100;
    private char Vert = 100;
    private char Bleu = 100;
    private char type_affichage = 0;
    private int filtre;
    private int pos_X;
    private int pos_Y;
    private int pos_Z;
    private int score;
    private int num;
    private boolean basc;
    private String chaine = "coucou";
    private int t;
    private List<balle> tab_balle = new ArrayList();

    //balle[] tab_balle;
    private poing tab_poing;
    private gestion_niveaux g_niveau;
    //gestion_appli g_gestion_appli;
    //nuage[] tab_nuages;
    private List<nuage> tab_nuages = new ArrayList();
    private affiche_score[] tab_score;

    private jouer_son mjouer_son;
    private int reduit_suite_capture = 0;
    private Bitmap mBitmap;
    // Drawable[] image_avion;
    private Drawable[] image_nuage;
    private Drawable[] image_poing;
    private Drawable[] image_gameOver;
    // Drawable[] image_chariot;
    private Drawable[] image_chasseur;
    //Drawable[] image_bras_gauche;
    //Drawable[] image_bras_droite;

    private Path mPath;
    private Paint mBitmapPaint;
    private Drawable mBackgroundImage;
    private int coordonnes_remorques_X;
    private int coordonnes_remorques_Y;

    private nuage[] mBackgroundNuagge;
    private int nm_balle_ds_camion = 0;
    private int num_ball = 0;
    private int num_ball_ds_remorque = 0;
    private int taille_avant = 0;
    int dx;
    private camion mcamion;
    private chasseur Lechasseur;

    private byte appuis_ecran = 0;
    private int position_y_score;
    private int Y_viseur = 0;
    private static final int MENU_1 = 1;
    private static final int MENU_2 = 2;
    private static final int MENU_QUIT = 5;
    private boolean initialise = false;

    private int etatJeuFini = 0;

    /**
     * Called when the activity is first created.
     */
    public static int getNbre_bal() {
        return nbre_bal;
    }

    public static int getNbre_nuage() {
        return nbre_nuage;
    }

    public static int getNbre_membre_chasseur() {
        return nbre_membre_chasseur;
    }

    public static int getMenu1() {
        return MENU_1;
    }

    public static int getMenu2() {
        return MENU_2;
    }

    public static int getMenuQuit() {
        return MENU_QUIT;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // *#*#4636#*#*


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return false;
    }

    private int hauteur_ecran;
    private int largeur_ecran;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        final float scale = getResources().getDisplayMetrics().density;
        setLargeur_ecran(getResources().getDisplayMetrics().widthPixels);
        setHauteur_ecran(getResources().getDisplayMetrics().heightPixels);

        balle.largeur_ecran = getLargeur_ecran();
        balle.hauteur_ecran = getHauteur_ecran();
        setPosition_y_score(getHauteur_ecran() - 150);

        setContentView(R.layout.main);
        setT(0);

        initialise_jeux();
        initialise_timer();
        init_capteur();
        initialise_draw();

    }

    void initialise_timer() {
        MyTimerTask myTask = new MyTimerTask();
        Timer myTimer = new Timer();
        myTimer.schedule(myTask, 100, 30);

    }

    private void gere_balle_dans_la_remorque() {
        if (getNum_ball_ds_remorque() >= getNbre_bal())
            setNum_ball_ds_remorque(0);

        if (getTab_balle().get(getNum_ball_ds_remorque()).balle_dans_camion_info == true) {
            getTab_balle().get(getNum_ball_ds_remorque()).balle_dans_camion_info = false;
            getMcamion().incremente_balle_dans_cammion(getNum_ball_ds_remorque());

        }

        if (getTab_balle().get(getNum_ball_ds_remorque()).balle_echappee_info == true) {
            getTab_balle().get(getNum_ball_ds_remorque()).balle_echappee_info = false;
            getMcamion().decremente_balle_dans_cammion(getNum_ball_ds_remorque());

        }

        if (getTab_balle().get(getNum_ball_ds_remorque()).statut_balle == balle.balle_dans_le_camion) {
            getMcamion().calcul_coordonees_dela_balle_dans_la_remorque(getNum_ball_ds_remorque());

        }

        setNum_ball_ds_remorque(getNum_ball_ds_remorque() + 1);

    }

    private void met_a_jour_postion_remorque_pour_les_balles() {
        balle.coordonnees_remorques_X = getMcamion().getPosition_y();
    }

    private boolean test_etat_balles() {
        boolean balle_attrapee = false;

        for (num_ball = 0; num_ball < nbre_bal; num_ball++) {

            int statut_temp = 0;
            statut_temp = getTab_balle().get(getNum_ball()).Get_Statut_de_la_Balle();
            //  if (statut_temp == balle.balle_en_l_air) {
            switch (statut_temp) {
                case balle.balle_en_l_air:

                    if ((getTab_balle().get(getNum_ball()).position_x > (getTab_poing().X_poing - 50))
                            && (getTab_balle().get(getNum_ball()).position_x < (getTab_poing().X_poing + 50))
                            && (getTab_balle().get(getNum_ball()).position_y > (getTab_poing().Y_poing - 50))
                            && (getTab_balle().get(getNum_ball()).position_y < (getTab_poing().Y_poing + 50))) {

                        getTab_balle().get(getNum_ball()).statut_balle = balle.balle_est_attrapee;
                        balle_attrapee = true;
                        int score_temp;
                        if (getTab_balle().get(getNum_ball()).avion_vert == true) {
                            score_temp = (-100);
                            setScore(getScore() + score_temp);
                            if (getTab_balle().get(getNum_ball()).mutex_son == false) {
                                getMjouer_son().playSound(3);
                                getTab_balle().get(getNum_ball()).mutex_son = true;
                            }

                        } else {
                            setReduit_suite_capture(50);

                            score_temp = (11 * getTab_balle().get(getNum_ball()).vitesse);
                            setScore(getScore() + score_temp);
                            if (getTab_balle().get(getNum_ball()).mutex_son == false) {
                                getMjouer_son().playSound(4);
                                getTab_balle().get(getNum_ball()).mutex_son = true;
                            }
                        }

                        getTab_score()[getNum_ball()].setX_score(getTab_balle().get(getNum_ball()).position_x);
                        getTab_score()[getNum_ball()].setY_score(getTab_balle().get(getNum_ball()).position_y);
                        getTab_score()[getNum_ball()].setScore_a_afficher(true);
                        getTab_score()[getNum_ball()].set_score(score_temp);
                        getTab_score()[getNum_ball()].setDuree_vie_score(100);

                    }
                    break;


                case balle.balle_perdu:
                    etatJeuFini = 1;

                    //  getImage_gameOver()[1].draw(canvas);
                    break;

                default:
                    //  throw new IllegalStateException("Unexpected value: " + statut_temp);
            }

        }
        return balle_attrapee;
    }

    private boolean test_balle_perdu() {
        boolean balle_attrapee = false;

        for (num_ball = 0; num_ball < nbre_bal; num_ball++) {

            int statut_temp = 0;
            statut_temp = getTab_balle().get(getNum_ball()).Get_Statut_de_la_Balle();
            //  if (statut_temp == balle.balle_en_l_air) {
            switch (statut_temp) {


                case balle.balle_perdu:
                    etatJeuFini = 1;

                    //  getImage_gameOver()[1].draw(canvas);
                    break;

                default:
                    //  throw new IllegalStateException("Unexpected value: " + statut_temp);
            }

        }
        return balle_attrapee;
    }


    private void demarre_les_nuages() {
        byte num_nuage = 0;
        for (num_nuage = 0; num_nuage < getNbre_nuage(); num_nuage++) {
            if (getTab_nuages().get(num_nuage).thread_lance == false) {
                getTab_nuages().get(num_nuage).start();
                getTab_nuages().get(num_nuage).thread_lance = true;
            }

        }
    }

    private int num_balle_a_lancer = 0;

    //
    private void demarre_les_balles() {
        if (num_balle_a_lancer++ >= nbre_bal)
            return;


        if (getTab_balle().get(getNum_balle_a_lancer()).thread_lance == false) {
            getTab_balle().get(getNum_balle_a_lancer()).start();
            getTab_balle().get(getNum_balle_a_lancer()).incremente_num_balle();
            getTab_balle().get(getNum_balle_a_lancer()).thread_lance = true;
            getTab_balle().get(getNum_balle_a_lancer()).balle_affichee = true;
            getTab_balle().get(getNum_balle_a_lancer()).statut_balle = 1;

        }

    }

    private void demarre_le_poing() {

        if (getTab_poing().thread_lance == false) {
            getTab_poing().start();
            getTab_poing().thread_lance = true;
        }

    }

    private void demarre_le_chasseur() {

        if (getLechasseur().thread_lance == false) {
            getLechasseur().start();
            getLechasseur().thread_lance = true;
        }

    }

    private void gere_son() {
        for (setNum_ball(0); getNum_ball() < getNbre_bal(); setNum_ball(getNum_ball() + 1)) {

            if (getTab_balle().get(getNum_ball()).son1_a_relancer == true) {
                getTab_balle().get(getNum_ball()).son1_a_relancer = false;

            }
            if (getTab_balle().get(getNum_ball()).son2_a_relancer == true) {
                getTab_balle().get(getNum_ball()).son2_a_relancer = false;

            }
            if (getTab_balle().get(getNum_ball()).son3_a_relancer == true) {
                getTab_balle().get(getNum_ball()).son3_a_relancer = false;
            }
        }
    }

    private void gere_score() {
        int b = getNbre_bal();
        //  for (setNum_ball(0); getNum_ball() < getNbre_bal(); setNum_ball(getNum_ball() + 1)) {
        for (int i = 0; i < b; i++) {
            if (getTab_score()[i].getDuree_vie_score() > 10) {
                getTab_score()[i].setY_score(getTab_score()[i].getY_score()-1);

            }

            if (getTab_score()[i].getDuree_vie_score() > 1500) {
                 getTab_score()[i].setScore_a_afficher(false);

            }
        }
           /* if (getTab_score()[getNum_ball()].setDuree_vie_score(getTab_score()[getNum_ball()].getDuree_vie_score() + 1) > 10) {
                getTab_score()[getNum_ball()].setY_score(getTab_score()[getNum_ball()].getY_score() - 4);
            }
            if (getTab_score()[getNum_ball()].setDuree_vie_score(getTab_score()[getNum_ball()].getDuree_vie_score() + 1) > 150) {
                getTab_score()[getNum_ball()].setScore_a_afficher(false);
            }*/
        //  }
    }

    private int temps_debut = 0;
    private int temps_fin = 0;
    private byte puissance_state = 0;

    private int force_de_tir = 0;
    private int puissance_temporaire = 0;

    private void gere_puissance_de_tir(int temps) {
        String debug;
        switch (getAppuis_ecran()) {
            case 1:

                setAppuis_ecran((byte) 0);

                switch (getPuissance_state()) {
                    case 0:
                        setTemps_debut(temps);
                        setPuissance_state((byte) 1);
                        setPuissance_temporaire(0);
                        break;
                    case 1:
                        setTemps_fin(temps);
                        setForce_de_tir((getTemps_fin() - getTemps_debut()) * 5);
                        setY_viseur(600 - getForce_de_tir());
                        setPuissance_state((byte) 0);
//ici
                        if (test_si_poing_en_bas() == 1) {
                            getTab_poing().lance_le_poing_vers_cible(getX1(), getY_viseur());
                        }
                        setPuissance_temporaire(0);
                        break;

                    case 2:
                        break;
                }
                break;

            case 0:
                switch (getPuissance_state()) {
                    case 0:

                        break;
                    case 1:
                        setPuissance_temporaire(getPuissance_temporaire() + 1);
                        if ((getTemps_debut() + 200) < getT()) {
                            setPuissance_state((byte) 0);
                            setPuissance_temporaire(0);
                        }
                        break;
                }
                break;
        }

    }

    void init_les_sons() {
        setMjouer_son(new jouer_son());
        getMjouer_son().initSounds(getBaseContext());
        getMjouer_son().addSound(3, raw.cestparti);
        getMjouer_son().addSound(3, nicolas.console.pr.R.raw.explosion);
        getMjouer_son().addSound(2, nicolas.console.pr.R.raw.explosion);
        getMjouer_son().addSound(1, nicolas.console.pr.R.raw.whawha2_1);
        getMjouer_son().addSound(4, nicolas.console.pr.R.raw.lam);
        getMjouer_son().addSound(5, nicolas.console.pr.R.raw.lam);
        getMjouer_son().addSound(6, nicolas.console.pr.R.raw.whawha2_1);
        getMjouer_son().addSound(7, nicolas.console.pr.R.raw.whawha2_m1);
        getMjouer_son().addSound(8, nicolas.console.pr.R.raw.whawha2_0);

    }

    void init_capteur() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setmSensorManager((SensorManager) getSystemService(SENSOR_SERVICE));
        setMcapteur(new Capteur(this));
    }

    void init_images() {
        setmBitmapPaint(new Paint(Paint.DITHER_FLAG));

        setTab_score(new affiche_score[getNbre_bal() + 1]);
        setImage_poing(new Drawable[2]);
        setImage_gameOver(new Drawable[1]);
        setImage_nuage(new Drawable[getNbre_nuage() + 1]);
        getImage_poing()[1] = getBaseContext().getResources().getDrawable(
                R.drawable.poingxcf);
        getImage_gameOver()[0] = getBaseContext().getResources().getDrawable(
                R.drawable.game_over);


    }

    void init_objet() {
        setTab_poing(new poing());
        setG_niveau(new gestion_niveaux());
        setMcamion(new camion(this.getBaseContext()));
        getMcamion().setContext(getBaseContext());
        getMcamion().initialise_camion();
        setLechasseur(new chasseur(this.getBaseContext()));
        getLechasseur().setContext(getBaseContext());
        for (byte i = 0; i < getNbre_nuage(); i++) {
            getTab_nuages().add(new nuage());
            getTab_nuages().add(new nuage());
        }

    }

    void initialise_draw() {

        for (byte i = 0; i < getNbre_nuage(); i++) {
            getImage_nuage()[i] = getBaseContext().getResources().getDrawable(
                    R.drawable.nuagephoto);
            // tab_nuages[i].init_nuage();
            getTab_nuages().get(i).init_nuage();
        }


        setContentView(getMcapteur());
        DrawView view1 = new DrawView(this);
        view1.setOnTouchListener(this);
        view1.setBackgroundColor(Color.argb(255, 0, 0, 0));
        setContentView(view1);

        Resources res = getBaseContext().getResources();
        setmBackgroundImage(getBaseContext().getResources().getDrawable(
                R.drawable.saint_mar));

    }

    void demarre_le_jeux() {

        demarre_les_nuages();
        demarre_le_chasseur();
        demarre_le_poing();
        getLechasseur().init_pos_chasseur();
        for (byte i = 0; i <= getNbre_bal(); i++) {

            getTab_balle().add(new balle(this.getBaseContext()));
            getTab_balle().get(i).initialise_balle();

            getTab_balle().get(i).configure_le_camion(getMcamion());
            getTab_balle().get(i).balle_affichee = false;
            getTab_score()[i] = new affiche_score();

        }

    }

    void initialise_jeux() {

        init_les_sons();
        init_images();
        init_objet();

    }

    public SensorManager getmSensorManager() {
        return mSensorManager;
    }

    public void setmSensorManager(SensorManager mSensorManager) {
        this.mSensorManager = mSensorManager;
    }

    public Capteur getMcapteur() {
        return mcapteur;
    }

    public void setMcapteur(Capteur mcapteur) {
        this.mcapteur = mcapteur;
    }

    public Paint getmPaint() {
        return mPaint;
    }

    public void setmPaint(Paint mPaint) {
        this.mPaint = mPaint;
    }

    public int[] getmColors() {
        return mColors;
    }

    public void setmColors(int[] mColors) {
        this.mColors = mColors;
    }

    public int getOrientation_X() {
        return orientation_X;
    }

    public void setOrientation_X(int orientation_X) {
        this.orientation_X = orientation_X;
    }

    public int getOrientation_Y() {
        return orientation_Y;
    }

    public void setOrientation_Y(int orientation_Y) {
        this.orientation_Y = orientation_Y;
    }

    public int getOrientation_Z() {
        return orientation_Z;
    }

    public void setOrientation_Z(int orientation_Z) {
        this.orientation_Z = orientation_Z;
    }

    public float getMagnetic_1() {
        return magnetic_1;
    }

    public void setMagnetic_1(float magnetic_1) {
        this.magnetic_1 = magnetic_1;
    }

    public float getMagnetic_2() {
        return magnetic_2;
    }

    public void setMagnetic_2(float magnetic_2) {
        this.magnetic_2 = magnetic_2;
    }

    public float getMagnetic_3() {
        return magnetic_3;
    }

    public void setMagnetic_3(float magnetic_3) {
        this.magnetic_3 = magnetic_3;
    }

    public float getAccel_X() {
        return accel_X;
    }

    public void setAccel_X(float accel_X) {
        this.accel_X = accel_X;
    }

    public float getAccel_Y() {
        return accel_Y;
    }

    public void setAccel_Y(float accel_Y) {
        this.accel_Y = accel_Y;
    }

    public float getAccel_Z() {
        return accel_Z;
    }

    public void setAccel_Z(float accel_Z) {
        this.accel_Z = accel_Z;
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public int getOffset_orientation_X() {
        return offset_orientation_X;
    }

    public void setOffset_orientation_X(int offset_orientation_X) {
        this.offset_orientation_X = offset_orientation_X;
    }

    public int getOffset_orientation_Y() {
        return offset_orientation_Y;
    }

    public void setOffset_orientation_Y(int offset_orientation_Y) {
        this.offset_orientation_Y = offset_orientation_Y;
    }

    public int getOffset_orientation_Z() {
        return offset_orientation_Z;
    }

    public void setOffset_orientation_Z(int offset_orientation_Z) {
        this.offset_orientation_Z = offset_orientation_Z;
    }

    public float getOffset_magnetic_1() {
        return offset_magnetic_1;
    }

    public void setOffset_magnetic_1(float offset_magnetic_1) {
        this.offset_magnetic_1 = offset_magnetic_1;
    }

    public float getOffset_magnetic_2() {
        return offset_magnetic_2;
    }

    public void setOffset_magnetic_2(float offset_magnetic_2) {
        this.offset_magnetic_2 = offset_magnetic_2;
    }

    public float getOffset_magnetic_3() {
        return offset_magnetic_3;
    }

    public void setOffset_magnetic_3(float offset_magnetic_3) {
        this.offset_magnetic_3 = offset_magnetic_3;
    }

    public char getRouge() {
        return Rouge;
    }

    public void setRouge(char rouge) {
        Rouge = rouge;
    }

    public char getVert() {
        return Vert;
    }

    public void setVert(char vert) {
        Vert = vert;
    }

    public char getBleu() {
        return Bleu;
    }

    public void setBleu(char bleu) {
        Bleu = bleu;
    }

    public char getType_affichage() {
        return type_affichage;
    }

    public void setType_affichage(char type_affichage) {
        this.type_affichage = type_affichage;
    }

    public int getFiltre() {
        return filtre;
    }

    public void setFiltre(int filtre) {
        this.filtre = filtre;
    }

    public int getPos_X() {
        return pos_X;
    }

    public void setPos_X(int pos_X) {
        this.pos_X = pos_X;
    }

    public int getPos_Y() {
        return pos_Y;
    }

    public void setPos_Y(int pos_Y) {
        this.pos_Y = pos_Y;
    }

    public int getPos_Z() {
        return pos_Z;
    }

    public void setPos_Z(int pos_Z) {
        this.pos_Z = pos_Z;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isBasc() {
        return basc;
    }

    public void setBasc(boolean basc) {
        this.basc = basc;
    }

    public String getChaine() {
        return chaine;
    }

    public void setChaine(String chaine) {
        this.chaine = chaine;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public List<balle> getTab_balle() {
        return tab_balle;
    }

    public void setTab_balle(List<balle> tab_balle) {
        this.tab_balle = tab_balle;
    }

    public poing getTab_poing() {
        return tab_poing;
    }

    private int test_si_poing_en_bas() {
        //System.out.println("le poing est en " + getTab_poing().Y_poing);
        if (getTab_poing().Y_poing > (getHauteur_ecran() * 0.6))
            return 1;//TODO mettre une methode pour calculer
        return 0;
    }

    public void setTab_poing(poing tab_poing) {
        this.tab_poing = tab_poing;
    }

    public gestion_niveaux getG_niveau() {
        return g_niveau;
    }

    public void setG_niveau(gestion_niveaux g_niveau) {
        this.g_niveau = g_niveau;
    }

    public List<nuage> getTab_nuages() {
        return tab_nuages;
    }

    public void setTab_nuages(List<nuage> tab_nuages) {
        this.tab_nuages = tab_nuages;
    }

    public affiche_score[] getTab_score() {
        return tab_score;
    }

    public void setTab_score(affiche_score[] tab_score) {
        this.tab_score = tab_score;
    }

    public jouer_son getMjouer_son() {
        return mjouer_son;
    }

    public void setMjouer_son(jouer_son mjouer_son) {
        this.mjouer_son = mjouer_son;
    }

    public int getReduit_suite_capture() {
        return reduit_suite_capture;
    }

    public void setReduit_suite_capture(int reduit_suite_capture) {
        this.reduit_suite_capture = reduit_suite_capture;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public Drawable[] getImage_nuage() {
        return image_nuage;
    }

    public void setImage_nuage(Drawable[] image_nuage) {
        this.image_nuage = image_nuage;
    }

    public Drawable[] getImage_poing() {
        return image_poing;
    }

    public void setImage_poing(Drawable[] image_poing) {
        this.image_poing = image_poing;
    }

    public Drawable[] getImage_chasseur() {
        return image_chasseur;
    }

    public void setImage_chasseur(Drawable[] image_chasseur) {
        this.image_chasseur = image_chasseur;
    }

    public Path getmPath() {
        return mPath;
    }

    public void setmPath(Path mPath) {
        this.mPath = mPath;
    }

    public Paint getmBitmapPaint() {
        return mBitmapPaint;
    }

    public void setmBitmapPaint(Paint mBitmapPaint) {
        this.mBitmapPaint = mBitmapPaint;
    }

    public Drawable getmBackgroundImage() {
        return mBackgroundImage;
    }

    public void setmBackgroundImage(Drawable mBackgroundImage) {
        this.mBackgroundImage = mBackgroundImage;
    }

    public int getCoordonnes_remorques_X() {
        return coordonnes_remorques_X;
    }

    public void setCoordonnes_remorques_X(int coordonnes_remorques_X) {
        this.coordonnes_remorques_X = coordonnes_remorques_X;
    }

    public int getCoordonnes_remorques_Y() {
        return coordonnes_remorques_Y;
    }

    public void setCoordonnes_remorques_Y(int coordonnes_remorques_Y) {
        this.coordonnes_remorques_Y = coordonnes_remorques_Y;
    }

    public nuage[] getmBackgroundNuagge() {
        return mBackgroundNuagge;
    }

    public void setmBackgroundNuagge(nuage[] mBackgroundNuagge) {
        this.mBackgroundNuagge = mBackgroundNuagge;
    }

    public int getNm_balle_ds_camion() {
        return nm_balle_ds_camion;
    }

    public void setNm_balle_ds_camion(int nm_balle_ds_camion) {
        this.nm_balle_ds_camion = nm_balle_ds_camion;
    }

    public int getNum_ball() {
        return num_ball;
    }

    public void setNum_ball(int num_ball) {
        this.num_ball = num_ball;
    }

    public int getNum_ball_ds_remorque() {
        return num_ball_ds_remorque;
    }

    public void setNum_ball_ds_remorque(int num_bfall_ds_remorque) {
        this.num_ball_ds_remorque = num_ball_ds_remorque;
    }

    public int getTaille_avant() {
        return taille_avant;
    }

    public void setTaille_avant(int taille_avant) {
        this.taille_avant = taille_avant;
    }

    public camion getMcamion() {
        return mcamion;
    }

    public void setMcamion(camion mcamion) {
        this.mcamion = mcamion;
    }

    public chasseur getLechasseur() {
        return Lechasseur;
    }

    public void setLechasseur(chasseur lechasseur) {
        Lechasseur = lechasseur;
    }

    public byte getAppuis_ecran() {
        return appuis_ecran;
    }

    public void setAppuis_ecran(byte appuis_ecran) {
        this.appuis_ecran = appuis_ecran;
    }

    public int getPosition_y_score() {
        return position_y_score;
    }

    public void setPosition_y_score(int position_y_score) {
        this.position_y_score = position_y_score;
    }

    public int getY_viseur() {
        return Y_viseur;
    }

    public void setY_viseur(int y_viseur) {
        Y_viseur = y_viseur;
    }

    public boolean isInitialise() {
        return initialise;
    }

    public void setInitialise(boolean initialise) {
        this.initialise = initialise;
    }

    public int getHauteur_ecran() {
        return hauteur_ecran;
    }

    public void setHauteur_ecran(int hauteur_ecran) {
        this.hauteur_ecran = hauteur_ecran;
    }

    public int getLargeur_ecran() {
        return largeur_ecran;
    }

    public void setLargeur_ecran(int largeur_ecran) {
        this.largeur_ecran = largeur_ecran;
    }

    public int getNum_balle_a_lancer() {
        return num_balle_a_lancer;
    }

    public void setNum_balle_a_lancer(int num_balle_a_lancer) {
        this.num_balle_a_lancer = num_balle_a_lancer;
    }

    public int getTemps_debut() {
        return temps_debut;
    }

    public void setTemps_debut(int temps_debut) {
        this.temps_debut = temps_debut;
    }

    public int getTemps_fin() {
        return temps_fin;
    }

    public void setTemps_fin(int temps_fin) {
        this.temps_fin = temps_fin;
    }

    public byte getPuissance_state() {
        return puissance_state;
    }

    public void setPuissance_state(byte puissance_state) {
        this.puissance_state = puissance_state;
    }

    public int getForce_de_tir() {
        return force_de_tir;
    }

    public void setForce_de_tir(int force_de_tir) {
        this.force_de_tir = force_de_tir;
    }

    public int getPuissance_temporaire() {
        return puissance_temporaire;
    }

    public void setPuissance_temporaire(int puissance_temporaire) {
        this.puissance_temporaire = puissance_temporaire;
    }

    public Drawable[] getImage_gameOver() {
        return image_gameOver;
    }

    public void setImage_gameOver(Drawable[] image_gameOver) {
        this.image_gameOver = image_gameOver;
    }


    class MyTimerTask extends TimerTask {
        private String mouch;

        @Override
        public void run() {

            int cx1;
            int cy1, cy2;
            if (!isInitialise()) {
                demarre_le_jeux();
                setInitialise(true);
                return;
                // initialise_jeux();
            }
            if (getT() < 1000) {
                if ((getT() % 100) == 0)
                    demarre_les_balles();
            }
            setT(getT() + 1);
            if (getG_niveau().affiche_niveau_duree > 0)
                getG_niveau().affiche_niveau_duree--;
            switch (getT()) {
                case 10:
                    getMjouer_son().playSound(3);
                    break;
                case 33:
                    getLechasseur().avance_le_chasseur = true;

                    break;

            }
            test_balle_perdu();
            if (getTab_poing().le_poing_est_parti == false) {
                getTab_poing().X_poing = getLechasseur().x_torse_haut - 50;
                getTab_poing().Y_poing = getLechasseur().y_torse_haut - 50;
                getLechasseur().etat_chasseur = 0;
            } else {

                if (test_etat_balles()) {

                }
              /*  if(etatJeuFini == 1){
                    //TODO afficher gameover
                }*/
                getLechasseur().etat_chasseur = 1;
            }

            switch (getT() % 4) {
                case 0:
                    gere_balle_dans_la_remorque();
                    break;
                case 1:
                    gere_score();
                    break;
                case 2:
                    gere_son();
                    break;
                case 3:
                    met_a_jour_postion_remorque_pour_les_balles();
                    break;
                case 9:
                    // canvas = dessine_jeux(canvas);
                    break;
            }
            // met_a_jour_postion_remorque_pour_les_balles();
            gere_puissance_de_tir(getT());
            balle.niveau = getG_niveau().get_niveau();
            if (getTab_poing().poing_initialise == false) {
                getTab_poing().position_initiale_Y = getPos_Y();
                getTab_poing().position_initiale_X = getPos_X();
                // tab_poing.initialise_position_poing();
                getTab_poing().poing_initialise = true;
                // return;
            }
            setX1((getPos_Z() * 3) + getLargeur_ecran() / 2);//+ (pos_X) / 5;

            getMcamion().position_x = getX1();
            getLechasseur().setPos_chasseur(getMcamion().position_x - 100);
            //if ((getT() % 50) == 0)
            //    getLechasseur().init_pos_chasseur();

            getMcamion().calcul_coordonees_remorques();
            cx1 = 200 - getPos_Z();
            cy1 = 700 + getPos_Y();
            dx = 20 + Math.abs((getPos_X()) / 5);
            // cx2 = cx1 + 20;// + dx;
            // cy2 = cy1 + 20;// + dx;

            setNm_balle_ds_camion(getMcamion().nombre_de_balle_dans_la_remorque);
        }

        public String getMouch() {
            return mouch;
        }

        public void setMouch(String mouch) {
            this.mouch = mouch;
        }
    }

    class UpdateBallTask extends TimerTask {
        @Override
        public void run() {
        }
    }

    private class Capteur extends View implements SensorListener {

        public Capteur(Context context) {
            super(context);

        }

        @Override
        public void onSensorChanged(int sensor, float[] values) {
            //  SensorManager.
            if (sensor == SensorManager.SENSOR_DELAY_GAME) {
                setOrientation_X((int) values[0]);
                setOrientation_Y((int) values[1]);
                setOrientation_Z((int) values[2]);

                setPos_X(getOrientation_X());
                setPos_Y(getOrientation_Y());
                setPos_Z(getOrientation_Z());

            }

            if (sensor == SensorManager.SENSOR_DELAY_FASTEST) {
                setAccel_X(values[0]);
                setAccel_Y(values[1]);
                setAccel_Z(values[2]);

            }
        }

        @Override
        public void onAccuracyChanged(int sensor, int accuracy) {

        }
    }

    public class DrawView extends View {

        public DrawView(Context c) {
            super(c);

            setmPath(new Path());
            setmBitmap(Bitmap.createBitmap(40, 69, Bitmap.Config.ARGB_8888));
            setmPath(new Path());
            // mCanvas = new Canvas(mBitmap);

        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {

            super.onSizeChanged(w, h, oldw, oldh);
            setmBitmap(Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888));
            // mCanvas = new Canvas(mBitmap);
        }

        public void touch_up(float a, float b) {

            getmPath().lineTo(10, 30);
            getmPath().reset();
        }

        @Override
        public void onDraw(Canvas canvas) {

            super.onDraw(canvas);
            if (isInitialise()) {
                canvas = dessine_jeux(canvas);
            }

            try {

            } catch (Exception exception) {

            }
            postInvalidate();

        }
    }

    private Canvas dessine_jeux(Canvas canvas) {
        getmBitmapPaint().setTextSize(40);
        getmBackgroundImage().draw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.RED);

        canvas.drawRect(10, getHauteur_ecran() - 300 - getPuissance_temporaire(), 30, getHauteur_ecran() - 300, paint);

        canvas.drawText("x:" + getPos_X() + " y:" + getPos_Y() + " z:" + getPos_Z(), 300,
                300, getmBitmapPaint());
        for (setNum_ball(0); getNum_ball() < getNbre_bal() - 1; setNum_ball(getNum_ball() + 1)) {
            getTab_balle().get(getNum_ball()).dessine(canvas);

        }

        canvas.save();
        canvas.rotate(getTab_poing().angle_poing, getTab_poing().X_poing + 50,
                getTab_poing().Y_poing + 50);// angle_poing
        getImage_poing()[1].setBounds(getTab_poing().X_poing, getTab_poing().Y_poing,
                getTab_poing().X_poing + 100, getTab_poing().Y_poing + 100);

        getImage_poing()[1].draw(canvas);
        canvas.restore();

        canvas.save();
        getLechasseur().dessine_jeux(canvas);


        getMcamion().image[0].setBounds(getMcamion().position_x,
                getMcamion().getPosition_y(), getMcamion().position_remorque_B,
                getMcamion().getPosition_y() + 150);
        getMcamion().image[0].draw(canvas);
        getmBitmapPaint().setColor(Color.MAGENTA);
        setChaine(String.format("score %d", getScore()));
        canvas.drawText(getChaine(), 50, getPosition_y_score(), getmBitmapPaint());
        setChaine(String.format("%d", getNm_balle_ds_camion()));
        canvas.drawText(getChaine(), getMcamion().position_x + 20,
                getMcamion().getPosition_y() + 50, getmBitmapPaint());
        byte num_nuage;
        for (num_nuage = 0; num_nuage < getNbre_nuage(); num_nuage++) {

            if (getTab_nuages().get(num_nuage).affiche_nuage == true) {
                getImage_nuage()[num_nuage].setBounds(

                        getTab_nuages().get(num_nuage).position_x,
                        getTab_nuages().get(num_nuage).position_y,
                        getTab_nuages().get(num_nuage).position_x + 200,
                        getTab_nuages().get(num_nuage).position_y + 200);
                getImage_nuage()[num_nuage].draw(canvas);
            }

        }


        for (int babal= 0; babal < getNbre_bal(); babal++) {
            if (getTab_score()[babal].isScore_a_afficher() == true) {
                setChaine(String.format("!!%d", getTab_score()[babal].get_score()));
                canvas.drawText(getChaine(), getTab_score()[babal].getX_score(),
                        getTab_score()[babal].getY_score(), getmBitmapPaint());
            }
        }
        if (getG_niveau().affiche_niveau_duree > 0) {
            setChaine(getG_niveau().nom_niveau);
            getmBitmapPaint().setTextSize(100);
            canvas.drawText(getChaine(), 50, 550, getmBitmapPaint());

        }
        if (etatJeuFini == 1) {
           /* getImage_gameOver()[0].setBounds(getTab_poing().X_poing, getTab_poing().Y_poing,
                    getTab_poing().X_poing + 100, getTab_poing().Y_poing + 100);*/
            getImage_gameOver()[0].setBounds(getLargeur_ecran() / 10, getHauteur_ecran() / 4, getLargeur_ecran() * 9 / 10, getHauteur_ecran() * 3 / 4);

            getImage_gameOver()[0].draw(canvas);
        }
        return canvas;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getmSensorManager().registerListener(getMcapteur(),
                SensorManager.SENSOR_ORIENTATION
                        | SensorManager.SENSOR_ACCELEROMETER,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getmSensorManager().unregisterListener(getMcapteur());

    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return false;
    }

    @Override
    public boolean onTouch(View arg0, MotionEvent arg1) {

        setAppuis_ecran((byte) 1);
        return false;
    }

}
