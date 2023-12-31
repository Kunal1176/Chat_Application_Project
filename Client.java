import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.TrayIcon.MessageType;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;


public class Client extends JFrame{
    

     Socket socket;
     BufferedReader br;
     PrintWriter out;


     //Declare components
     private JLabel heading=new JLabel("Client Area");
     private JTextArea messageArea = new JTextArea();
     private JTextField messageInput = new JTextField();
     private Font font=new Font("Roboto" , Font.PLAIN, 15);

      public Client()
      {
        try{
           System.out.println("sending request to server");
            socket = new Socket("127.0.0.1", 8887);
            System.out.println("Connection done.");
            
            br= new BufferedReader(new InputStreamReader(socket.getInputStream()));
        

           out = new PrintWriter(socket.getOutputStream());


           createGUI();
           handleEvent();

           startReading();
           startWriting();


        }catch(Exception e){
           


        }
    }

   private void handleEvent(){
   
    messageInput.addKeyListener(new KeyListener(){

        @Override
        public void keyTyped(KeyEvent e) {
            // TODO Auto-generated method stub
           // throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
        }

        @Override
        public void keyPressed(KeyEvent e) {
            // TODO Auto-generated method stub
           // throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
        }

        @Override
        public void keyReleased(KeyEvent e) {
            // TODO Auto-generated method stub
         System.out.println("Key released" + e.getKeyCode());
         if(e.getKeyCode() == 10){
        System.out.println("you have pressed enter button");
                           String contentToSend = messageInput.getText();
                           messageArea.append("Me : " + contentToSend+ "\n");
                           out.println(contentToSend);
                           out.flush();
                         messageInput.setText("");
                         messageInput.requestFocus();
          }

            //throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");

        }
        
     });



 }
   


    /**
     * 
     */
    private void createGUI(){
        //gui code

        this.setTitle("Client Messager [END]");
        this.setSize(500,500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       //coding for component

       heading.setFont(font);
       messageArea.setFont(font);
       messageInput.setFont(font);
         heading.setHorizontalAlignment(SwingConstants.CENTER);
      // heading.setHorizontalTextPosition(SwingConstants.BOTTOM);
       // heading.setVerticalTextPosition(SwingConstants.CENTER);
         heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

         messageArea.setEditable(false);
       messageInput.setHorizontalAlignment(SwingConstants.CENTER);



       //frame ka layout set karenge
       this.setLayout(new BorderLayout());

       //adding the components to frame
         this.add(heading,BorderLayout.NORTH);
         JScrollPane jScrollPane=new JScrollPane(messageArea);
         this.add(jScrollPane, BorderLayout.CENTER);
         this.add(messageInput, BorderLayout.SOUTH);

        this.setVisible(true); 
     }

        
        
 
    public void startReading(){

        //thread - read karke deta rhega 
   Runnable r1=()->{
   System.out.println("reader started...");

    try{
    while(true){
        
        String msg = br.readLine();
            if(msg.equals("exit"))
       {
           System.out.println("Server terminated the chat ");
           JOptionPane.showMessageDialog(this, "Server Terminated the chat");
           messageInput.setEnabled(false);
           socket.close();
        break;
    }

       //  System.out.println("Server : "+msg);
         messageArea.append("Server   :  "+msg+"\n");

    } 
    }catch(Exception e){
         //e.printStackTrace();
          System.out.println("connection closed");
        }
    };

    new Thread(r1).start();
    
}

    public void startWriting(){

         // thread - data user lega and then snd karega client tak
         Runnable r2=()->{

            System.out.println("writer started...");
             try{
            while(!socket.isClosed()){ 
                

                    BufferedReader br1= new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();

                    
                    out.println(content);
                    out.flush();

                if(content.equals("exit")){
                    socket.close();
                    break;
                 }

             
                }


                }catch(Exception e){
                 // compilation pr technical info ko print kr deta hai error jaise 
                 //   e.printStackTrace();
                          System.out.println("connection closed");

            }
         } ;
         new Thread(r2).start();
    }






    public static void main(String[] args)throws Exception {
        System.out.println("this is client");
new Client();

    }
}



    