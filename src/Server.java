/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.sun.corba.se.impl.io.IIOPInputStream;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
 *
 * @author franklinnunez
 */
public class Server extends JFrame {
    
    private JTextField _userText;
    private JTextArea _chatWindow;
    private ObjectOutputStream _output;
    private ObjectInputStream _input; 
    private ServerSocket _server;
    private Socket _connection;
    
    //Constructor
    public Server(){
        super("Samy's Instant Messenger");
        _userText = new JTextField();
        _userText.setEditable(false);
         _userText.addActionListener(
         new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                 //Create Send method
                sendMessage(e.getActionCommand());
                _userText.setText("");
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                
              }
            
           }
                 
         );//End of ActionListener
         add(_userText, BorderLayout.NORTH);
         //Set the Chat Window
         _chatWindow = new JTextArea();
         add(new JScrollPane(_chatWindow));
         setSize(300, 150);
         setVisible(true);
         
    }//End of constructor
    
    
 //Set and run the Server ---------------------------------------------------------
    
    public void startRunning(){
        try
        {
           //Set the Server object
            _server = new ServerSocket(6789, 100);
            //Listen to incoming connections
            while(true){
                try{
                     
                    waitForConnection();
                    setupStreams();
                    whileChatting();
                    
                }catch(EOFException eofExc){
                    showMessage("\n Server ended the connection! ");
                }finally{
                    closeCrap();
                }
            }//End of while loop
            
        }catch(IOException ioExc ){
         ioExc.printStackTrace();
        }
            
    }//End of startRunning method
    
       //Create the waitForConnection method. Once connected, display connection information
        private void waitForConnection() throws IOException{
          showMessage(" Waiting for someone to connect... \n");  
          _connection = _server.accept();
          showMessage(" Now connected to " + _connection.getInetAddress().getHostAddress());
          
        };
        
       //Create the setupStreams method. Get  Streams to send and receive data
        private void setupStreams() throws IOException{
            
            //Set the ObjectOutputStream object
            _output = new ObjectOutputStream(_connection.getOutputStream());
            //Flush the connection
            _output.flush();
            //Set the ObjectInputStream object
            _input = new ObjectInputStream(_connection.getInputStream());
            showMessage(" Streams are now setup! \n");
                 
        }//End of setupStreams
        
       
        //Create the whileChatting method. During the chat conversation
        
        private void whileChatting() throws IOException{
            String message = " You are now connected!";
            sendMessage(message);
            ableToType(true);
            do
            {
                try{
                    message = (String) _input.readObject();
                    showMessage("\n" + message);
                    
                }
                catch(ClassNotFoundException ex){
                   showMessage("\n Idk what the user sent!"); 
                }
            }while(!message.equals("CLIENT - END"));
        }
        
        
        //Close Streams and Sockets after you are done chatting
        private void closeCrap(){
            showMessage("\n Closing connections... \n");
            ableToType(false);
            
            try{
                _output.close();
                _input.close();
                _connection.close();
                
            }catch(IOException ioex){
                ioex.printStackTrace();
            }
        }
        
        //Send message to client
        
        private void sendMessage(String message){
            try{
                
                _output.writeObject("SERVER - " + message);
                _output.flush();
                showMessage("\n SERVER - " + message);
                
            }catch(IOException ex){
                _chatWindow.append("\n ERROR: Message could not be sent ");
            }
        }
        
        //Updates chat window
        
       private void showMessage(final String text){
           SwingUtilities.invokeLater(
                new Runnable() {

               @Override
               public void run() {
                   //Update the chat window
                   _chatWindow.append(text);
                   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.    
               }//End of run
           }//End of Runnable
        );//End of invokeLater
     }//End of showMessage method
       
       //Create the ableToType method. Let the user type into their box
       
       private void ableToType(final boolean tof){
           
           SwingUtilities.invokeLater(
                new Runnable() {

               @Override
               public void run() {
                   //Update the chat window
                   _userText.setEditable(tof);
                   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.    
               }//End of run
           }//End of Runnable
        );//End of invokeLater
           
       }//End of ableToType
        
}//End of class ableToType. 


