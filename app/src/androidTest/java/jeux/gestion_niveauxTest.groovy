package jeux

class gestion_niveauxTest extends gestion_niveaux {
    void testGet_niveau() {
        //get_niveau tt = new gestion_niveaux();
       // tt._niveau = 1;
        //tt.increment_niveau() ;
        _niveau = 0;
        testIncrement_niveau();
        assertEquals(tt._niveau,3);
    }

    void testIncrement_niveau() {
    }

    void testTest_si_niveau_fini() {
    }
}
