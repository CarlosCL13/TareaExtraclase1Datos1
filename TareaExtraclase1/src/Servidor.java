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

class InterfazServidor extends JFrame implements Runnable{

    public InterfazServidor(){

        setBounds(500,200,280,350);

        JPanel mipanel = new JPanel();

        mipanel.setLayout(new BorderLayout());

        areatexto = new JTextArea();

        mipanel.add(areatexto,BorderLayout.CENTER);

        add(mipanel);

        setVisible(true);

        Thread mihilo = new Thread(this);

        mihilo.start();

    }

    @Override
    public void run() {
        //System.out.println("Estoy a la escucha");

        try {
            ServerSocket servidor = new ServerSocket(8888);

            String mensaje;

            PaqueteEnvio paquete_recibido;

            while(true) {

                Socket misocket = servidor.accept();

                ObjectInputStream paquete_datos = new ObjectInputStream(misocket.getInputStream());

                paquete_recibido = (PaqueteEnvio) paquete_datos.readObject();

                mensaje = paquete_recibido.getMensaje();

                /*DataInputStream flujo_entrada = new DataInputStream(misocket.getInputStream());

                String mensaje_texto = flujo_entrada.readUTF();

                areatexto.append("\n" + mensaje_texto);*/

                areatexto.append("\n" + mensaje);

                /*Socket enviaDestinatario = new Socket(ip, 9090);

                ObjectOutputStream paqueteReenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());

                paqueteReenvio.writeObject(paquete_recibido);

                //paqueteReenvio.close();

                enviaDestinatario.close();*/

                misocket.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private	JTextArea areatexto;
}
