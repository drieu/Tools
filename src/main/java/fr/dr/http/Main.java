package fr.dr.http;

/**
 * Main class to send a POST request.
 * User: drieu
 * Date: 15/03/13
 * Time: 09:52
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args) {
        Post post = new Post("/home/drieu/Damien/config/Tools/http.properties");
        post.send("Alert !","01/01/13","WARN", "SERV1, SERV2");
    }
}
