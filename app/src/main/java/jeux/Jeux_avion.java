package jeux;


import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import audio.jouer_son;
import nicolas.console.pr.R;
import nicolas.console.pr.R.raw;


public class Jeux_avion extends Activity implements OnTouchListener {
    private int nbre_bal = 4;
    private static final int nbre_nuage = 5;
    private SensorManager mSensorManager;
    private Capteur mcapteur;

    private int orientation_X = 0;
    private int orientation_Y = 0;
    private int orientation_Z = 0;
    private float accel_X = 0;
    private float accel_Y = 0;
    private float accel_Z = 0;
    private int x1;

    private int pos_X;
    private int pos_Y;
    private int pos_Z;
    private int score;
     private String chaine = "coucou";
    private int t;
    private List <balle> tab_balle = new ArrayList();
    private int scoreGeneral = 0;

    private poing tab_poing;
    private gestion_niveaux g_niveau;
    private List <nuage> tab_nuages = new ArrayList();
    private affiche_score[] tab_score;

    private jouer_son mjouer_son;
    private int reduit_suite_capture = 0;
    private Bitmap mBitmap;
    private Drawable[] image_nuage;
    private Drawable[] image_poing;
    private Drawable[] image_gameOver;
    private Drawable[] image_chasseur;
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

    private int limitebasse_partie = 0;// = (hauteur_ecran*0.6);
    private int vie_perdue = 0;
    private int niveau_jeu = 1;
    private int niveau_affiche = 1;
    private int nombre_de_vie = 3;

    /**
     * Called when the activity is first created.
     */
    public int getNbre_bal() {
        return nbre_bal;
    }

    public static int getNbre_nuage() {
        return nbre_nuage;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return false;
    }

    private int hauteur_ecran;
    private int largeur_ecran;
    private int vitesse_vent = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        gestionVies titi = new gestionVies();
        titi.getAffiche_nuage();
        final float scale = getResources().getDisplayMetrics().density;
        setLargeur_ecran(getResources().getDisplayMetrics().widthPixels);
        setHauteur_ecran(getResources().getDisplayMetrics().heightPixels);

        balle.largeur_ecran = getLargeur_ecran();
        balle.hauteur_ecran = getHauteur_ecran();
        setPosition_y_score(getHauteur_ecran() - 150);
        limitebasse_partie = (int) (balle.hauteur_ecran * 0.6);// = (hauteur_ecran*0.6);
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


        if (getTab_balle().get(getNum_ball_ds_remorque()).statut_balle == balle.balle_dans_le_camion) {
            getMcamion().calcul_coordonees_dela_balle_dans_la_remorque(getNum_ball_ds_remorque());

        }

        setNum_ball_ds_remorque(getNum_ball_ds_remorque() + 1);

    }

    private void met_a_jour_postion_remorque_pour_les_balles() {
        balle.coordonnees_remorques_X = getMcamion().getPosition_y();
        ;
    }

    private boolean test_si_balle_attrapee(int num_ball) {
        if ((getTab_balle().get(num_ball).position_x_camion > (getTab_poing().X_poing - 50))
                && (getTab_balle().get(num_ball).position_x_camion < (getTab_poing().X_poing + 50))
                && (getTab_balle().get(num_ball).position_y > (getTab_poing().Y_poing - 50))
                && (getTab_balle().get(num_ball).position_y < (getTab_poing().Y_poing + 50))) {
            getTab_balle().get(num_ball).statut_balle = balle.balle_est_attrapee;
            return true;
        }

        return false;
    }

    private boolean test_etat_balles() {
        boolean balle_attrapee = false;

        for (int num_ball = 0; num_ball < getNbre_bal(); num_ball++) {

            int statut_temp = 0;
            statut_temp = getTab_balle().get(num_ball).Get_Statut_de_la_Balle();
            switch (statut_temp) {
                case balle.balle_en_l_air:

                    if (test_si_balle_attrapee(num_ball)) {

                        balle_attrapee = true;
                        int score_temp;
                        if (getTab_balle().get(num_ball).avion_vert == true) {
                            score_temp = (33 * getTab_balle().get(num_ball).vitesse);
                            setScore(getScore() + score_temp);
                            if (getTab_balle().get(num_ball).mutex_son == false) {
                                getMjouer_son().playSound(3);
                                getTab_balle().get(num_ball).mutex_son = true;
                            }

                        } else {
                            setReduit_suite_capture(50);

                            score_temp = (11 * getTab_balle().get(num_ball).vitesse);
                            setScore(getScore() + score_temp);
                            if (getTab_balle().get(num_ball).mutex_son == false) {
                                getMjouer_son().playSound(4);
                                getTab_balle().get(num_ball).mutex_son = true;
                            }
                        }
                        incrementeScoreGeneral(score_temp);
                        getTab_balle().get(num_ball).set_score(score_temp);
                        // getTab_score()[num_ball].set_score(score_temp);

                    }
                    break;
                case balle.balle_perdu:
                    vie_perdue = 1;

                    //  getImage_gameOver()[1].draw(canvas);
                    break;

                default:
                    //  throw new IllegalStateException("Unexpected value: " + statut_temp);
            }

        }
        return balle_attrapee;
    }

    private void test_balle_perdu() {
        boolean balle_attrapee = false;

        for (int num_balle = 0; num_balle < getNbre_bal(); num_balle++) {
            int  statut_temp = getTab_balle().get(num_balle).Get_Statut_de_la_Balle();
            //  if (statut_temp == balle.balle_en_l_air) {
            switch (statut_temp) {


                case balle.balle_perdu:
                    vie_perdue = 1;

                    //  getImage_gameOver()[1].draw(canvas);
                    break;

                default:
                    //  throw new IllegalStateException("Unexpected value: " + statut_temp);
            }

        }

    }


    private void demarre_les_nuages() {
        byte num_nuage = 0;
        for (num_nuage = 0; num_nuage < getNbre_nuage(); num_nuage++) {
            if (getTab_nuages().get(num_nuage).thread_lance == false) {
                getTab_nuages().get(num_nuage).vitesse_vent = vitesse_vent;
                getTab_nuages().get(num_nuage).start();
                getTab_nuages().get(num_nuage).thread_lance = true;
            }

        }
    }

    private int num_balle_a_lancer = 0;

    private void demarre_les_balles() {
        if (num_balle_a_lancer >= getNbre_bal())
            return;
        int num_babale = num_balle_a_lancer;
        System.out.println("demarre la balle " + num_balle_a_lancer);
        Random randomGenerator = new Random();
        vitesse_vent =  (randomGenerator.nextInt(4) )-2;
        tab_poing.setVitesse_vent(vitesse_vent);

        for (int num_nuage = 0; num_nuage < getNbre_nuage(); num_nuage++) {
            getTab_nuages().get(num_nuage).vitesse_vent = vitesse_vent;
            //TODO mettre au bon endroit

        }

        for (balle tab_balle : tab_balle) {
            if (tab_balle.isThread_lance() == false) {
                tab_balle.setLimite_Basse(limitebasse_partie);
                tab_balle.start();
                tab_balle.incremente_num_balle();
                tab_balle.setThread_lance(true);
                tab_balle.balle_affichee = true;
                tab_balle.statut_balle = 1;
            }
        }
        num_balle_a_lancer++;
    }

    private void demarre_le_poing() {

        if (getTab_poing().thread_lance == false) {
            getTab_poing().start();
            getTab_poing().thread_lance = true;
        }

    }

    private void demarre_le_chasseur() {

        if (getLechasseur().thread_lance == false) {
            getLechasseur().setNombre_de_vie(nombre_de_vie);
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

        //TODO remettre les sons
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
                R.drawable.balle);
        getImage_gameOver()[0] = getBaseContext().getResources().getDrawable(
                R.drawable.game_over);
        //TODO remettre les images


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
            nuage ng = new nuage();
            ng.setLargeur_ecran(largeur_ecran);
            getTab_nuages().add(ng);

            //getTab_nuages().add(new nuage());
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
        view1.setBackgroundColor(Color.argb(255, 0, 0, 100));
        setContentView(view1);

        setmBackgroundImage(getBaseContext().getResources().getDrawable(
                R.drawable.saint_mar));

    }

    void demarre_le_jeux() {

        demarre_les_nuages();
        demarre_le_chasseur();
        demarre_le_poing();
        getLechasseur().init_pos_chasseur();
        getTab_balle().clear();
        for (byte i = 0; i < getNbre_bal(); i++) {
            //System.out.println("  configure balle  " + i + " / " + getNbre_bal());
            Log.d("balle","configure balle  " + i +" / " + getNbre_bal());

            getTab_balle().add(new balle(this.getBaseContext()));
            getTab_balle().get(i).setNiveau(niveau_jeu);
            getTab_balle().get(i).setNum_balle_lance(i);
            getTab_balle().get(i).initialise_balle();

            getTab_balle().get(i).configure_le_camion(getMcamion());
            getTab_balle().get(i).balle_affichee = false;
            // getTab_score()[i] = new affiche_score();

        }
        getMcamion().initialise_camion();
        num_balle_a_lancer = 0;
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

    public void setAccel_X(float accel_X) {
        this.accel_X = accel_X;
    }

    public void setAccel_Y(float accel_Y) {
        this.accel_Y = accel_Y;
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

    public List <balle> getTab_balle() {
        return tab_balle;
    }

    public poing getTab_poing() {
        return tab_poing;
    }

    private int test_si_poing_en_bas() {
        //System.out.println("le poing est en " + getTab_poing().Y_poing);
        if (getTab_poing().Y_poing > limitebasse_partie)
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

    public List <nuage> getTab_nuages() {
        return tab_nuages;
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

    public void setReduit_suite_capture(int reduit_suite_capture) {
        this.reduit_suite_capture = reduit_suite_capture;
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
        this.num_ball_ds_remorque = num_bfall_ds_remorque;
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

    public int getScoreGeneral() {
        return scoreGeneral;
    }

    public void setScoreGeneral(int scoreGeneral) {
        this.scoreGeneral = scoreGeneral;
    }

    public void setNbre_bal(int nbre_bal) {
        this.nbre_bal = nbre_bal;
    }


    private int etatJeu = 0;
    private int dessine_balles = 0;

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            switch (etatJeu) {
                case 0:

                    if (!isInitialise()) {

                        demarre_le_jeux();
                        dessine_balles = 1;
                        setInitialise(true);
                        setT(0);

                        // return;
                        // initialise_jeux();
                    }
                    if ((getT()) == 111)
                        demarre_les_balles();

                    setT(getT() + 1);
                    if (getG_niveau().affiche_niveau_duree > 0)
                        getG_niveau().affiche_niveau_duree--;
                    switch (getT()) {
                        case 10:
                            getMjouer_son().playSound(3);

                            break;
                        case 33:
                            getLechasseur().avance_le_chasseur = true;
                            //test ici
                            break;

                    }
                    test_balle_perdu();
                    if (!getTab_poing().le_poing_est_parti) {
                        getTab_poing().X_poing = getLechasseur().getX_torse_haut() - 50;
                        getTab_poing().Y_poing = getLechasseur().getY_torse_haut() - 50;
                    } else {

                        test_etat_balles();

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
                    }
                    // met_a_jour_postion_remorque_pour_les_balles();
                    gere_puissance_de_tir(getT());
                    //  balle.setNiveau(getG_niveau().get_niveau());
                    if (getTab_poing().poing_initialise == false) {
                        getTab_poing().position_initiale_Y = getPos_Y();
                        getTab_poing().position_initiale_X = getPos_X();
                        getTab_poing().poing_initialise = true;
                    }
                    setX1((getPos_Z() * 6) + getLargeur_ecran() / 2);//+ (pos_X) / 5;

                    getMcamion().position_x_camion = getX1();
                    getLechasseur().setPos_chasseur(getMcamion().position_x_camion - 200);
                    getLechasseur().setNombre_de_vie(nombre_de_vie);
                    getMcamion().calcul_coordonees_remorques();
                    dx = 20 + Math.abs((getPos_X()) / 5);

                    setNm_balle_ds_camion(getMcamion().getNombre_de_balle_dans_la_remorque());
                    if (getMcamion().getNombre_de_balle_dans_la_remorque() >= (getNbre_bal())) {
                        getMcamion().initialise_camion();
                        niveau_jeu++;
                        etatJeu = 1;
                        niveau_affiche += 1;
                    }
                    if (vie_perdue == 1) {
                        nombre_de_vie --;
                        etatJeu = 1;
                    }

                    break;
                case 1://reinitialise balles;
                    setInitialise(false);//on relance le jeu

                    num_balle_a_lancer = 0;
                    etatJeu = 0;
                    dessine_balles = 0;
                    vie_perdue = 0;
                    setT(0);

                    for (int i = 0; i < getNbre_bal(); i++) {

                        getTab_balle().get(i).tueBalle();
                        getTab_balle().get(i).setThread_lance(false);
                        System.out.println("attente fin thread " + i);
                    }
                    if (niveau_jeu > 4) {//tous les 4 niveaux on augmente le nombre de balles
                        setNbre_bal(getNbre_bal() + 2);
                        niveau_jeu = 1;
                    }

                    System.out.println("on a maintenant " + getNbre_bal() + " balles");
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + etatJeu);
            }
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

            //noinspection EmptyTryBlock

            postInvalidate();

        }
    }

    private void incrementeScoreGeneral(int incr) {
        setScoreGeneral(getScoreGeneral() + incr);
    }

    private Canvas dessine_jeux(Canvas canvas) {

        getmBitmapPaint().setTextSize(40);
        getmBackgroundImage().draw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawRect(10, limitebasse_partie, getLargeur_ecran(), getHauteur_ecran(), paint);

        paint.setColor(Color.RED);
        canvas.drawRect(10, getHauteur_ecran() - 300 - getPuissance_temporaire(), 30, getHauteur_ecran() - 300, paint);
        if (dessine_balles == 1) {
            for (int bb = 0; bb < getNum_ball(); bb++) {
                getTab_balle().get(bb).setNiveau(niveau_jeu);
                getTab_balle().get(bb).dessine(canvas);

            }
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

        getMcamion().bonhomme1[0].setBounds(getMcamion().position_x_camion,
                getMcamion().getPosition_y(), getMcamion().getPosition_remorque_B(),
                getMcamion().getPosition_y() + 150);
        getMcamion().bonhomme1[0].draw(canvas);

        for (byte num_nuage = 0; num_nuage < getNbre_nuage(); num_nuage++) {

            if (getTab_nuages().get(num_nuage).affiche_nuage == true) {
                getImage_nuage()[num_nuage].setBounds(

                        getTab_nuages().get(num_nuage).position_x_camion,
                        getTab_nuages().get(num_nuage).position_y,
                        getTab_nuages().get(num_nuage).position_x_camion + 200,
                        getTab_nuages().get(num_nuage).position_y + 200);
                getImage_nuage()[num_nuage].draw(canvas);
            }

        }

        for (int babal = 0; babal < getNbre_bal(); babal++) {
            // if (getTab_score()[babal].isScore_a_afficher() == true) {
            if (getTab_balle().get(babal).isScore_a_afficher()) {
                getTab_balle().get(babal).calculYScore();
                setChaine(String.format("%d", getTab_balle().get(babal).get_score()));
                canvas.drawText(getChaine(), getTab_balle().get(babal).getX_score(),
                        getTab_balle().get(babal).getY_score(), getmBitmapPaint());
            }
        }
        if (getG_niveau().affiche_niveau_duree > 0) {
            setChaine(getG_niveau().nom_niveau);
            getmBitmapPaint().setTextSize(100);
            canvas.drawText(getChaine(), 50, 550, getmBitmapPaint());

        }
        getmBitmapPaint().setColor(Color.YELLOW);
        setChaine(String.format("score %d   %d vies", getScore(),nombre_de_vie));
        canvas.drawText("vie " + nombre_de_vie + "  niveau " + niveau_affiche, getLargeur_ecran() * 5/7 , getHauteur_ecran() / 10, getmBitmapPaint());
        getmBitmapPaint().setTextSize(100);
        canvas.drawText("" + getScoreGeneral(), getLargeur_ecran() * 3 / 4, getHauteur_ecran() / 15, getmBitmapPaint());

        getmBitmapPaint().setColor(Color.WHITE);
        getmBitmapPaint().setTextSize(40);

        canvas.drawText(getChaine(), 50, getPosition_y_score(), getmBitmapPaint());
        getmBitmapPaint().setColor(Color.RED);
        canvas.drawText("" + getMcamion().getNombre_de_balle_dans_la_remorque() + " / " + getNbre_bal(), getLargeur_ecran() * 3 / 4, getHauteur_ecran() / 8, getmBitmapPaint());
        //canvas.drawText("" + getScoreGeneral(), getLargeur_ecran() * 3 / 4, getHauteur_ecran() / 15, getmBitmapPaint());

        if(nombre_de_vie == 0) {
            getImage_gameOver()[0].setBounds(getLargeur_ecran() / 10, getHauteur_ecran() / 4, getLargeur_ecran() * 9 / 10, getHauteur_ecran() * 3 / 4);
            getImage_gameOver()[0].draw(canvas);
        }


        return canvas;
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        }

        }
    @Override
    protected void onResume() {
        super.onResume();
        getmSensorManager().registerListener(getMcapteur(),
                SensorManager.SENSOR_ORIENTATION
                        | SensorManager.SENSOR_ACCELEROMETER,
                SensorManager.SENSOR_DELAY_NORMAL);
    }
    protected void onPause() {
            super.onPause();

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
