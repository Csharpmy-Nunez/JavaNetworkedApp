/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.swing.JFrame;
/**
 *
 * @author franklinnunez
 */
public class ServerTest {
    
    public static void main(String[] args) {
         
        Server samy = new Server();
        samy.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        samy.startRunning();
    }
}
