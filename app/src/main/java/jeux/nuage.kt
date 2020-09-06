package jeux

import java.util.*

class nuage : Jeux_generique() {
    @JvmField
	var affiche_nuage = true
    var vitesse_au_pic = 0
    var dessus_dessous = 0
    @JvmField
	var thread_lance = false
    @JvmField
    var vitesse_vent = 0
   /* fun getVitesseVent(){
        return vitesse_vent
    }*/
    fun init_nuage() {
        val randomGenerator = Random()
        vitesse_au_pic = randomGenerator.nextInt(200)
        position_y = randomGenerator.nextInt(200)
        position_x_camion = randomGenerator.nextInt(500)
    }

    fun incremente_position() {
        position_x_camion += vitesse_vent //vitesse_au_pic;
        if (position_x_camion > largeur_ecran) {
            position_x_camion = -300
            val randomGenerator = Random()
            position_y = randomGenerator.nextInt(500)
        }
        if (position_x_camion < -300) {
            position_x_camion = largeur_ecran + 300
            val randomGenerator = Random()
            position_y = randomGenerator.nextInt(500)
        }
    }

    override fun run() {
        try {
            // while (etat) {
            while (!Thread.currentThread().isInterrupted) {
                incremente_position()
                //Thread.sleep(vitesse_au_pic);
                Thread.sleep(100)
            }
        } catch (e: InterruptedException) {
            return
        }
    }

}