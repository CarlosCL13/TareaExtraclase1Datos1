import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import  java.util.List;
import static java.lang.Integer.parseInt;

/**
 * Esta es la clase Servidor, en la cual se programa lo necesario para comunicarse con el cliente.
 *
 * @author Carlos Andres Contreras Luna
 *
 * @version 1.0
 *
 * @see Cliente
 */

public class Servidor {

    /**
     * Este es el metodo main de la clase Servidor.
     * @param args Contiene la instancia de la clase InterfazServidor.
     */

    public static void main(String[] args){

        InterfazServidor miinterfazs = new InterfazServidor();

        miinterfazs.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }
}

/**
 * Esta es la clase InterfazServidor en la cual se encuentran metodos y atributos de la interfaz del servidor.
 */

class InterfazServidor extends JFrame{

    /**
     * En este metodo se programa algunas características de la interfaz del chat que vera el servidor, ademas, se instancia la clase ModeloInterfazServidor.
     */

    public InterfazServidor(){

        setBounds(700,200,280,350);

        ModeloInterfazServidor modelointerfazs = new ModeloInterfazServidor();

        add(modelointerfazs);

        setResizable(false);

        setVisible(true);
    }
}

/**
 * La clase ModeloInterfazServidor posee los elementos de la interfaz que permiten escribir y enviar texto al cliente-Servidor, tambien implementa "Runnable" para poder crear un thread.
 */

class ModeloInterfazServidor extends JPanel implements Runnable{

    /**
     * Este metodo posee todos los elementos que se muestran en la interfaz del servidor.
     */

    public ModeloInterfazServidor(){

        JLabel texto= new JLabel("Chat Servidor");

        texto.setBounds(0,15, 10,10);

        add(texto);

        areatexto = new JTextArea(12,22);

        areatexto.setEditable(false);

        add(areatexto);

        campo1 = new JTextField(20);

        add(campo1);

        miboton=new JButton("Enviar");

        EnviaTexto mievento = new EnviaTexto();

        miboton.addActionListener(mievento);

        add(miboton);

        Thread mihilo = new Thread(this);

        mihilo.start();

    }

    /**
     * Este metodo permite ver si el mensaje recibido contiene solo letras.
     *
     * @param cadena es la variable tipo string que se recibe el metodo.
     *
     * @return retorna un booleano.
     */

    public static boolean contieneSoloLetras(String cadena) {

        for (int x = 0; x < cadena.length(); x++) {

            char c = cadena.charAt(x);

            if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == ' ')) {

                return false;
            }
        }
        return true;
    }

    /**
     * Este metodo permite calcular el monto con los valores recibidos.
     *
     * @param mensaje es la variable que recibe el string con los valores.
     *
     * @return retorna un número tipo double con el resultado del monto.
     */

    public String calcular_monto(String mensaje){
        double monto;

        String resultado;

        StringTokenizer separador = new StringTokenizer(mensaje,",");

        List<String> elementos = new ArrayList<String>();

        while(separador.hasMoreTokens()) {
            elementos.add(separador.nextToken());
        }

        monto = (double) parseInt(elementos.get(0)) * parseInt(elementos.get(2))/100 + (parseInt(elementos.get(1)) * 0.15);

        resultado = String.valueOf(monto);

        return resultado;
    }

    /**
     * El metodo run contiene los sockets y la parte del codigo que permite la comunicacion con el Cliente-Servidor y recibir mensajes.
     */

    @Override
    public void run() {

        try {
            ServerSocket servidor = new ServerSocket(9045);

            String mensaje;

            PaqueteEnvio paquete_recibido;

            while(true) {

                Socket misocket = servidor.accept();

                ObjectInputStream paquete_datos = new ObjectInputStream(misocket.getInputStream());

                paquete_recibido =  (PaqueteEnvio) paquete_datos.readObject();

                mensaje = paquete_recibido.getMensaje();

                areatexto.append("\n" + "Cliente-Servidor: " + mensaje);

//-----------------------------------------------------------------------------------------------------------------------------//

                if(mensaje.contains(".") || contieneSoloLetras(mensaje) == true){
                    System.out.print("");
                } else{
                    Socket enviaDestinatario = new Socket("192.168.1.2",8586);

                    PaqueteEnvio valores = new PaqueteEnvio();

                    String nuevo_mensaje = calcular_monto(mensaje);

                    valores.setMensaje(nuevo_mensaje);

                    ObjectOutputStream paqueteReenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());

                    paqueteReenvio.writeObject(valores);

                    paqueteReenvio.close();

                    enviaDestinatario.close();

                    misocket.close();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Esta clase permite, como su nombre lo indica, enviar texto o mejor dicho los mensaje hacia el Cliente-Servidor.
     */

    private class EnviaTexto implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            areatexto.append("\n" + "Tú: " + campo1.getText());

            try {
                Socket misocket = new Socket("192.168.1.2",8586);

                PaqueteEnvio datos = new PaqueteEnvio();

                datos.setMensaje(campo1.getText());

                ObjectOutputStream paquete_datos = new ObjectOutputStream(misocket.getOutputStream());

                paquete_datos.writeObject(datos);

                campo1.setText(null);

                misocket.close();

            } catch (IOException ioException) {
                ioException.printStackTrace();
                System.out.println(ioException.getMessage());
            }

        }
    }

    /**
     * Aqui se encuentran los elementos utilizados en la interfaz.
     */

    private JTextArea areatexto;

    private JTextField campo1;

    private JButton miboton;

}


