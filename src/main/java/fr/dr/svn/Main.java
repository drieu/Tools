package fr.dr.svn;

/**
 * Main class to launch a checkout on all projetcs list in files.
 * User: drieu
 * Date: 11/03/13
 * Time: 14:11
 */
public class Main {

    public static void main(String[] args) {
        Checkout co = new Checkout("/home/drieu/Damien/config/Tools/svn/checkout.properties", "/home/drieu/Damien/config/Tools/svn/projects");
        co.doAllCheckout();

        // List svn directory.
        // co.listSvnDirectory("mapi-gin-gestioncampagnesentretiens");

    }
}
