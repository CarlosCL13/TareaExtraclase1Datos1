//Imports que se necesitan
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

        setBounds(300,200,280,350);

        ModeloInterfazCliente modelointerfazc = new ModeloInterfazCliente();

        add(modelointerfazc);

        setResizable(false);

        setVisible(true);
    }
}

class ModeloInterfazCliente extends JPanel implements Runnable{

    public ModeloInterfazCliente(){

        JLabel texto= new JLabel("Chat");

        texto.setBounds(0,15, 10,10);

        add(texto);

        campochat = new JTextArea(12,22);

        campochat.setEditable(false);

        add(campochat);

        campo1 = new JTextField(20);

        add(campo1);

        miboton=new JButton("Enviar");

        EnviaTexto mievento = new EnviaTexto();

        miboton.addActionListener(mievento);

        add(miboton);

        Thread mihilo = new Thread(this);

        mihilo.start();

    }

    @Override
    public void run() {
        try{

            ServerSocket servidor_cliente = new ServerSocket(8586);

            Socket cliente;

            String mensaje;

            PaqueteEnvio paqueteRecibido;

            while (true){

                cliente = servidor_cliente.accept();

                ObjectInputStream paqueteReenvio = new ObjectInputStream(cliente.getInputStream());

                paqueteRecibido = (PaqueteEnvio) paqueteReenvio.readObject();

                mensaje = paqueteRecibido.getMensaje();

                campochat.append("\n" + "Servidor: " + mensaje);
            }
        } catch (Exception e){

            System.out.println(e.getMessage());
        }
    }

    private class EnviaTexto implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            //System.out.println(campo1.getText());

            campochat.append("\n" + campo1.getText());

            try {
                Socket misocket = new Socket("192.168.1.2",9045);

                PaqueteEnvio datos = new PaqueteEnvio();

                datos.setMensaje(campo1.getText());

                ObjectOutputStream paquete_datos = new ObjectOutputStream(misocket.getOutputStream());

                paquete_datos.writeObject(datos);

                misocket.close();

				/*DataOutputStream flujo_salida = new DataOutputStream(misocket.getOutputStream());

				flujo_salida.writeUTF(campo1.getText());

				flujo_salida.close();*/

            } catch (IOException ioException) {
                ioException.printStackTrace();
                System.out.println(ioException.getMessage());
            }

        }
    }

    private JTextField campo1;

    private JTextArea campochat;

    private JButton miboton;

}

class PaqueteEnvio implements Serializable {

    private String mensaje;

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

}
