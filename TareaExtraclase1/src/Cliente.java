import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import static java.lang.Integer.parseInt;

/**
 * Esta es la clase Cliente, en la cual se programa lo necesario para comunicarse con el servidor.
 *
 * @author Carlos Andres Contreras Luna
 *
 * @version 1.0
 *
 * @see Servidor
 */

public class Cliente {

    /**
     * Este es el metodo main de la clase Cliente.
     *
     * @param args Contiene la instancia de la clase InterfazCliente.
     */

    public static void main (String[] args){

        InterfazCliente miinterfazc = new InterfazCliente();

        miinterfazc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}

/**
 * Esta es la clase InterfazCliente en la cual se encuentran metodos y atributos de la interfaz del cliente.
 */

class InterfazCliente extends JFrame{

    /**
     * En este metodo se programa algunas caracteristicas de  la interfaz del chat que vera el cliente, además, se instancia la clase ModeloInterfazCliente.
     */

    public InterfazCliente(){

        setBounds(300,200,280,350);

        ModeloInterfazCliente modelointerfazc = new ModeloInterfazCliente();

        add(modelointerfazc);

        setResizable(false);

        setVisible(true);
    }
}

/**
 * La clase ModeloInterfazCliente posee los elementos de la interfaz que permiten escribir y enviar texto al servidor, tambien implementa "Runnable" para poder crear un thread.
 */

class ModeloInterfazCliente extends JPanel implements Runnable{

    /**
     * Este método posee todos los elementos que se muestran en la interfaz del cliente.
     */

    public ModeloInterfazCliente(){

        JLabel texto= new JLabel("Chat Cliente-Servidor");

        texto.setBounds(0,12, 10,10);

        add(texto);

        campochat = new JTextArea(12,22);

        campochat.setEditable(false);

        scroll = new JScrollPane(campochat);

        add(scroll);

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
     * @return retorna un booleano para saber si el mensaje tiene solo letras, para que asi no se active el metodo de hacer el calculo.
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
     * @return retorna un numero tipo double con el resultado del monto.
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
     * El metodo run contiene los sockets y la parte del codigo que permite la comunicacion con el servidor y recibir mensajes.
     */

    @Override
    public void run() {
        try{

            ServerSocket servidor_cliente = new ServerSocket(8586);

            Socket cliente;

            String mensaje;

            PaqueteEnvio paquete_recibido;

            while (true){

                cliente = servidor_cliente.accept();

                ObjectInputStream paquete_datos = new ObjectInputStream(cliente.getInputStream());

                paquete_recibido = (PaqueteEnvio) paquete_datos.readObject();

                mensaje = paquete_recibido.getMensaje();

                campochat.append("\n" + "Servidor: " + mensaje);

//-----------------------------------------------------------------------------------------------------------------------------//
                if(mensaje.contains(".") || contieneSoloLetras(mensaje)){
                    System.out.print("");
                } else{
                    Socket enviaDestinatario = new Socket("192.168.1.2",9045);

                    PaqueteEnvio valores = new PaqueteEnvio();

                    String nuevo_mensaje = calcular_monto(mensaje);

                    valores.setMensaje(nuevo_mensaje);

                    ObjectOutputStream paqueteReenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());

                    paqueteReenvio.writeObject(valores);

                    paqueteReenvio.close();

                    enviaDestinatario.close();

                    cliente.close();
                }
            }
        } catch (Exception e){

            System.out.println(e.getMessage());
        }
    }

    /**
     * Esta clase permite, como su nombre lo indica, enviar texto o mejor dicho los mensaje hacia el servidor.
     */

    private class EnviaTexto implements ActionListener{

        /**
         * Este metodo empaqueta el mensaje escrito en el campo de texto y gracias al socket permite enviar el mensaje al servidor.
         * @param e Es el ActionEvent que permite conectar el boton enviar con el metodo para enviar el mensaje escrito.
         */

        @Override
        public void actionPerformed(ActionEvent e) {

            campochat.append("\n" + "Tu: " + campo1.getText());

            try {
                Socket misocket = new Socket("192.168.1.2",9045);

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
    private JTextField campo1;

    private JTextArea campochat;

    private JButton miboton;

    private JScrollPane scroll;

}

/**
 * Esta clase permite empaquetar el mensaje que se va a enviar.
 */

class PaqueteEnvio implements Serializable {

    /**
     * Aqui se encuentran los metodos que permiten cambiar y obtener los datos del atributo mensaje.
     */

    private String mensaje;

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

}
