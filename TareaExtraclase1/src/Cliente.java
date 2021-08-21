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

        setBounds(500,200,280,350);

        ModeloInterfazCliente modelointerfazc = new ModeloInterfazCliente();

        add(modelointerfazc);

        setVisible(true);
    }
}

class ModeloInterfazCliente extends JPanel implements Runnable{

    public ModeloInterfazCliente(){

        JLabel texto= new JLabel("Chat");

        texto.setBounds(0,15, 10,10);

        add(texto);

        campochat = new JTextArea(12,22);

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

            ServerSocket servido_cliente = new ServerSocket(8080);

            Socket cliente;

            PaqueteEnvio paqueteRecibido;

            while (true){

                cliente = servido_cliente.accept();

                ObjectInputStream flujoentrada = new ObjectInputStream(cliente.getInputStream());

                paqueteRecibido = (PaqueteEnvio) flujoentrada.readObject();

                campochat.append("\n" + " Cliente" + " : " + paqueteRecibido.getMensaje());
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
                Socket misocket = new Socket("192.168.1.4",8888);

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
