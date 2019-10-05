package jeux;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

/**
 * Created by nicolasdaout on 26/02/15.
 */
public class Jeux_generique extends Thread {
    int largeur_ecran = 0;
    //  int hauteur_ecran = 0;
    private int hauteur_de_l_ecran;
    private int positionXintiiale;
    private int positionXintiale;
    private int positionZintiale;
    public int position_x;
    public int position_y;
    public int largeur = 100;
    public int angle;
    public Drawable[] image;
    int position_z;
    Context context;
    public Jeux_generique(){
        image  = new Drawable[1];
    }
    public int getPositionXintiiale() {
        return positionXintiiale;
    }

    public void setPositionXintiiale(int positionXintiiale) {
        this.positionXintiiale = positionXintiiale;
    }

    public int getPositionXintiale() {
        return positionXintiale;
    }

    public void setPositionXintiale(int positionXintiale) {
        this.positionXintiale = positionXintiale;
    }

    public int getPositionZintiale() {
        return positionZintiale;
    }

    public void setPositionZintiale(int positionZintiale) {
        this.positionZintiale = positionZintiale;
    }
    private void calcul_hauteur_ecran(){
        getHauteur_de_l_ecran();
    }
    public int getHauteur_de_l_ecran() {
        // this.context.
        hauteur_de_l_ecran = this.context.getResources().getDisplayMetrics().heightPixels;
        return hauteur_de_l_ecran;
    }

    public void setHauteur_de_l_ecran(int hauteur_de_l_ecran) {
        this.hauteur_de_l_ecran = hauteur_de_l_ecran;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getPosition_y() {
        return position_y;
    }

    public void setPosition_y(int position_y) {
        this.position_y = position_y;
    }
    public void dessine_jeux(Canvas canvas) {

        Paint paint = new Paint();
        paint.setColor(Color.RED);

        canvas.save();
        canvas.rotate(angle, position_x, position_y);
        image[0].setBounds(position_x, position_y, position_x
                + largeur, position_y + largeur);

        canvas.restore();
        image[0].draw(canvas);
        //return canvas;
    }
}
