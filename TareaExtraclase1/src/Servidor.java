//Imports que se necesitan
import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.*;

public class Servidor {

    public static void main(String[] args){

        InterfazServidor miinterfazs = new InterfazServidor();

        miinterfazs.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }
}

class InterfazServidor extends JFrame{

    public InterfazServidor(){

        setBounds(500,200,280,350);

        JPanel mipanel = new JPanel();

        mipanel.setLayout(new BorderLayout());

        areatexto = new JTextArea();

        mipanel.add(areatexto,BorderLayout.CENTER);

        add(mipanel);

        setVisible(true);



    }

    private	JTextArea areatexto;
}
