import javax.swing.*;//Para la parte grafica
import java.awt.*;
import java.nio.ByteOrder;
import java.net.*;//Para los socket
import java.io.*;//Para recibir y enviar mensajes
import java.awt.event.*;//Manejo de eventos con swing
import java.util.StringTokenizer;

public class Cliente1 {
    JFrame ventana_chat=null;
    JButton btn_enviar=null;
    JTextField txt_mensaje=null;
    JTextArea area_chat=null;
    JPanel contenedor_areachat=null;
    JPanel contenedor_btntxt=null;
    JScrollPane scroll=null;
    Socket socket = null;
    BufferedReader lector=null;
    PrintWriter escritor = null;

    public Cliente1(){

        hacerInterfaz();
    }
    public void hacerInterfaz(){
        ventana_chat=new JFrame("Cliente 1");
        btn_enviar= new JButton("Enviar");
        txt_mensaje = new JTextField(4);
        area_chat = new JTextArea(10,12);
        scroll= new JScrollPane(area_chat);
        contenedor_areachat=new JPanel();
        contenedor_areachat.setLayout(new GridLayout(1,1));
        contenedor_areachat.add(scroll);
        contenedor_btntxt= new JPanel();
        contenedor_btntxt.setLayout(new GridLayout(1,2));
        contenedor_btntxt.add(txt_mensaje);
        contenedor_btntxt.add(btn_enviar);
        ventana_chat.setLayout(new BorderLayout());
        ventana_chat.add(contenedor_areachat, BorderLayout.NORTH);
        ventana_chat.add(contenedor_btntxt,BorderLayout.SOUTH);
        ventana_chat.setSize(300, 220);
        ventana_chat.setVisible(true);
        ventana_chat.setResizable(false);
        ventana_chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Thread principal = new Thread(new Runnable() {
            public void run() {
                try {
                    socket = new Socket("localhost",9000);
                    leer();
                    escribir();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        principal.start();
    }

    public void tests(){

        int i=0;
        int[] dato= new int[3];

        String txt;
        txt= "22,12,35";
        StringTokenizer tokens = new StringTokenizer(txt,",");//Convierte el string en un arreglo
        while (tokens.hasMoreTokens()){//ciclo para decir que hay palabras para separar

            dato[i]= Integer.parseInt(tokens.nextToken());
            System.out.println(dato[i]);
            i++;
            if (i==3){
                System.out.println(dato[0]+dato[1]+dato[2]);
            }
        }

    }

    public void leer(){

        Thread leer_hilo = new Thread(new Runnable() {
            public void run() {
                try{
                    lector= new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        while (true){
                            String mensaje_recibido=lector.readLine();
                            area_chat.append("Servidor: "+mensaje_recibido+"\n");
                            System.out.println(mensaje_recibido);
                            int i=0;
                            int[] dato= new int[3];

                            String txt;

                            StringTokenizer tokens = new StringTokenizer(mensaje_recibido,",");//Convierte el string en un arreglo
                            while (tokens.hasMoreTokens()){//ciclo para decir que hay palabras para separar

                                dato[i]= Integer.parseInt(tokens.nextToken());
                                System.out.println(dato[i]);
                                i++;
                                if (i==3){
                                    System.out.println(dato[0]+dato[1]+dato[2]);
                                }
                            }

                        }
                    }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        leer_hilo.start();

    }

    public void escribir(){
        Thread escribir_hilo = new Thread(new Runnable() {
            public void run() {
                try{
                    escritor= new PrintWriter(socket.getOutputStream(),true);
                    btn_enviar.addActionListener(new ActionListener() {//Sirve para hacer que el boton reciba acciones
                        public void actionPerformed(ActionEvent e) {
                            //tests();
                            String enviar_mensaje = txt_mensaje.getText();//agarra el texto de la caja de texto

                            escritor.println(enviar_mensaje);//enviar mensaje
                            txt_mensaje.setText("");
                        }
                    });
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        escribir_hilo.start();
    }

    public static void main(String[] args){

        new Cliente1();
    }
}
