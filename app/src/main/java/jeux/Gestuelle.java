package jeux;

public class Gestuelle {
    private byte appuis_ecran = 0;
    private float glisse_haut = 0;
    private float glisse_bas = 0;
    private float glisse_droite = 0;
    private float glisse_gauche = 0;
    private float x_debut=0;
    private float x_fin=0;
    private float y_debut=0;
    private float y_fin = 0;
    private float deplacement_x = 0;
    private float deplacement_y = 0;

    private boolean nouveauDeplacementX = false;
    private boolean nouveauDeplacementY = false;

    public byte getAppuis_ecran() {
        return appuis_ecran;
    }

    public void setAppuis_ecran(byte appuis_ecran) {
        this.appuis_ecran = appuis_ecran;
    }

    public float getGlisse_haut() {
        setNouveauDeplacementY(false);
        return glisse_haut;
    }

    public void setGlisse_haut(float glisse_haut) {
        setNouveauDeplacementY(true);
        this.glisse_haut = glisse_haut;
    }

    public float getGlisse_bas() {
        setNouveauDeplacementY(false);
        return glisse_bas;
    }

    public void setGlisse_bas(float glisse_bas) {
        this.glisse_bas = glisse_bas;
        setNouveauDeplacementY(true);
    }

    public float getGlisse_droite() {
        setNouveauDeplacementX(false);
        return glisse_droite;

    }

    public void setGlisse_droite(float glisse_droite) {
        this.glisse_droite = glisse_droite;
        setNouveauDeplacementX(true);
    }

    public float getGlisse_gauche() {
        setNouveauDeplacementX(false);
        return glisse_gauche;
    }

    public void setGlisse_gauche(float glisse_gauche) {
        this.glisse_gauche = glisse_gauche;
        setNouveauDeplacementX(true);
    }

    public float getX_debut() {
        return x_debut;
    }

    public void setX_debut(float x_debut) {
        this.x_debut = x_debut;
    }

    public float getX_fin() {
        return x_fin;
    }

    public void setX_fin(float x_fin) {
        this.x_fin = x_fin;
    }

    public float getY_debut() {
        return y_debut;
    }

    public void setY_debut(float y_debut) {
        this.y_debut = y_debut;
    }

    public float getY_fin() {
        return y_fin;
    }

    public void setY_fin(float y_fin) {
        this.y_fin = y_fin;
    }

    public float getDeplacement_x() {
        return deplacement_x;
    }

    public void setDeplacement_x(float deplacement_x) {
        this.deplacement_x = deplacement_x;
    }

    public float getDeplacement_y() {
        return deplacement_y;
    }

    public void setDeplacement_y(float deplacement_y) {
        this.deplacement_y = deplacement_y;
    }

    public boolean isNouveauDeplacementX() {
        return nouveauDeplacementX;
    }

    public void setNouveauDeplacementX(boolean nouveauDeplacementX) {
        this.nouveauDeplacementX = nouveauDeplacementX;
    }

    public boolean isNouveauDeplacementY() {
        return nouveauDeplacementY;
    }

    public void setNouveauDeplacementY(boolean nouveauDeplacementY) {
        this.nouveauDeplacementY = nouveauDeplacementY;
    }
}
