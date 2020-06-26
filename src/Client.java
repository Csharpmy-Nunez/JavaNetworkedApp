/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.net.ssl.SSLSocket;
import javax.swing.*;
/**
 *
 * @author franklinnunez
 */
public class Client extends JFrame {
    
     private JTextField _userText;
     private JTextArea _chatWindow;
     private ObjectOutputStream _output;
     private ObjectInputStream _input;
     private String _message = "";
     private String _serverIP;
     private Socket _connection;
     
     
     //Constructor
     public Client(String host){
         super("Client");
         this._serverIP = host;
         _userText = new JTextField();
         _userText.setEditable(false);
         //Add action listener
         _userText.addActionListener(
                 new ActionListener() {

             @Override
             public void actionPerformed(ActionEvent e) {
                 sendData(e.getActionCommand());
                 _userText.setText("");
                 throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
             }
           }//End of new action listener
         );//End of action lister
         add(_userText, BorderLayout.NORTH);
         _chatWindow = new JTextArea();
         add(new JScrollPane(_chatWindow), BorderLayout.CENTER);
         setSize(300, 150);
         setVisible(true);
     }//End of constructor
     
     //Connect to server
     public void startRunning(){
         try{
            
             connectToServer();
             setupStreams();
             whileChatting();
             
             
         }catch(EOFException ex){
             showMessage("\n Client terminated the connection ");
         }catch(IOException ioEx){
             ioEx.printStackTrace();
         }finally{
             closeCrap();
         }
     }//End of startRunning
     
     
     //Connect to a server
     
     private void connectToServer() throws IOException{
         showMessage("Attempting connection...\n");
         _connection = new Socket(InetAddress.getByName(_serverIP), 6789);
         showMessage("Connected to: " + _connection.getInetAddress().getHostName());      
     }//End of method
     
     
     //Set Streams to send and receive messages
     private void setupStreams() throws IOException{
         _output = new ObjectOutputStream(_connection.getOutputStream());
         _output.flush();
         _input = new ObjectInputStream(_connection.getInputStream());
         showMessage("\n Your streams are now set up! \n");   
     }
     
     //While chatting with server
     private void whileChatting() throws IOException{
         ableToType(true);
         
         do{
             try{
                 //Read object message
                 _message = (String) _input.readObject();
                 showMessage("\n" + _message);
                 
             }catch(ClassNotFoundException cnfEx){
                 showMessage("\n I don't know that object type");
             }
             
         }while(!_message.equals("SERVER - END"));
     }//End of method
     
     //Close Streams and sockets 
     
     private void closeCrap(){
         showMessage("\n Closing everything down...");
         ableToType(false);
         
         try{
             _output.close();
             _input.close();
             _connection.close();
             
         }catch(IOException ioex){
              ioex.printStackTrace();
         }
     }
     
     //Send messages to the server
     private void sendData(String message){
         
         try{
             _output.writeObject("CLIENT - " + message);
             _output.flush();
             showMessage("\nCLIENT - " + message);
             
         }catch(IOException ioex){
             _chatWindow.append("\n Something went wrong!");
         }
         
     }//End of method
     
     //Change or update chat window
     
     private void showMessage(final String message){
         SwingUtilities.invokeLater(
              new Runnable() {

             @Override
             public void run() {
                 _chatWindow.append(message);
                 throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
             }
          }
       );
         
    }//End of method
     
     //give user permission to type into the textbox 
     private void ableToType(final boolean tof){
              SwingUtilities.invokeLater(
              new Runnable() {

             @Override
             public void run() {
                 _userText.setEditable(tof);
                 throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
             }
          }
       );
     }//End of method
     
     
     
    
}
