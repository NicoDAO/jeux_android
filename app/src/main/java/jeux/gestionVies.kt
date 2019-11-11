package jeux

import java.util.*

class gestionVies {
    var affiche_nuage = true
    private var vitesse_au_pic = 0
    var dessus_dessous = 0
    var thread_lance = false
    var position_y = 0
    private val position_x = 0
    fun init_nuage() {
        val randomGenerator = Random()
        vitesse_au_pic = randomGenerator.nextInt(200)
        val position_y = randomGenerator.nextInt(200)
        val position_x = randomGenerator.nextInt(500)
    }

    fun incremente_position() {
        position_y = position_y + 2
    }

    fun run() {
        try {
            // while (etat) {

            while (!Thread.currentThread().isInterrupted) {
                incremente_position()
                //Thread.sleep(vitesse_au_pic);


                Thread.sleep(500 + vitesse_au_pic.toLong())
            }
        } catch (e: InterruptedException) {
            return
        }
    }
}