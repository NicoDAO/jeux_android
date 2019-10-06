package jeux;

public class affiche_score {
    public affiche_score() {
    }

    private int x_score;
    private int y_score;
    private int _score;
    private int duree_vie_score;
    private boolean score_a_afficher;

    public int getX_score() {
        return x_score;
    }

    public void setX_score(int x_score) {
        this.x_score = x_score;
    }

    public int getY_score() {
        return y_score;
    }

    public void setY_score(int y_score) {
        this.y_score = y_score;
    }

    public int get_score() {
        return _score;
    }

    public void set_score(int _score) {
        this._score = _score;
    }

    public int getDuree_vie_score() {
        return duree_vie_score;
    }

    public void decremente_vie_score() {
        if (duree_vie_score > 0) duree_vie_score--;

    }

    public void setDuree_vie_score(int duree_vie_score) {
        this.duree_vie_score = duree_vie_score;
    }

    public boolean isScore_a_afficher() {
        return score_a_afficher;
    }

    public void setScore_a_afficher(boolean score_a_afficher) {
        this.score_a_afficher = score_a_afficher;
    }
}
