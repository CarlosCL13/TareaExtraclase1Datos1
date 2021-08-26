//Imports que se necesitan
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import  java.util.List;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;

public class Servidor extends Cliente{

    public static void main(String[] args){

        InterfazServidor miinterfazs = new InterfazServidor();

        miinterfazs.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }
}

class InterfazServidor extends JFrame implements Runnable{

    public InterfazServidor(){

        setBounds(700,200,280,350);

        JPanel mipanel = new JPanel();

        mipanel.setLayout(new BorderLayout());

        areatexto = new JTextArea();

        mipanel.add(areatexto,BorderLayout.CENTER);

        add(mipanel);

        setVisible(true);

        setResizable(false);

        Thread mihilo = new Thread(this);

        mihilo.start();

    }

    public String calcular_monto(String mensaje){
        double monto;

        String resultado;

        StringTokenizer separador = new StringTokenizer(mensaje, ",");

        List<String> elementos = new ArrayList<String>();

        while(separador.hasMoreTokens()) {
            elementos.add(separador.nextToken());
        }

        monto = (double) parseInt(elementos.get(0)) * parseInt(elementos.get(1))/100 + (parseInt(elementos.get(2)) * 0.15);

        resultado = String.valueOf(monto);

        return resultado;
    }

    @Override
    public void run() {
        //System.out.println("Estoy a la escucha");

        try {
            ServerSocket servidor = new ServerSocket(9045);

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

                areatexto.append("\n" + "Cliente " + mensaje);

                Socket enviaDestinatario = new Socket("192.168.1.2",8586);

                //String[] calculo = mensaje.split(",");

                //double monto = ((parseInt(calculo[1]) * parseInt(calculo[3]))/100 + (parseInt(calculo[2]) * 0.15));

                //String resultado = String.valueOf(monto);

                PaqueteEnvio valores = new PaqueteEnvio();

                String nuevo_mensaje = calcular_monto(mensaje);

                valores.setMensaje(nuevo_mensaje);

                ObjectOutputStream paqueteReenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());

                paqueteReenvio.writeObject(valores);

                paqueteReenvio.close();

                enviaDestinatario.close();

                misocket.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private	JTextArea areatexto;

    /*class CalculoMonto {

        double monto;

        String resultado;

        StringTokenizer separador = new StringTokenizer(datos);

        List<String> elementos = new ArrayList<String>();

        while(separador.hasMoreTokens()) {

            elementos.add(separador.nextToken());
        }

        monto = (parseInt(elementos.get(1)) * parseInt(elementos.get(2))/100) + (parseInt(elementos.get(3)) * 0.15);

        resultado = String.valueOf(monto);
    }*/

}


