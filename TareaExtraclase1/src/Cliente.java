import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class Cliente {
    /**
     * @author Carlos Andrés Contreras Luna
     * @param  args This is the class client and have all the methods to comunicate with the server
     */

    public static void main (String[] args){

        InterfazCliente miinterfazc = new InterfazCliente();

        miinterfazc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}

class InterfazCliente extends JFrame{

    public InterfazCliente(){

        setBounds(500,200,280,350);

        ModeloInterfazCliente modelointerfazc = new ModeloInterfazCliente();

        add(modelointerfazc);

        setVisible(true);
    }
}

class ModeloInterfazCliente extends JPanel {

    public ModeloInterfazCliente(){

        JLabel texto= new JLabel("Chat");

        texto.setBounds(0,15, 10,10);

        add(texto);

        campochat = new JTextArea(12,22);

        add(campochat);

        campo1 = new JTextField(20);

        add(campo1);

        miboton=new JButton("Enviar");

        add(miboton);
    }

    private JTextField campo1;

    private JTextArea campochat;

    private JButton miboton;
}