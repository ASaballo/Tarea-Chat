import javax.swing.*;//Para la parte grafica
import java.awt.*;
import java.nio.ByteOrder;
import java.net.*;//Para los socket
import java.io.*;//Para recibir y enviar mensajes
import java.awt.event.*;//Manejo de eventos con swing


public class Servidor {
    //Se declaran los textfields, los sockets y, un boton y un scroll para los mensajes.
    int[] dato;
    JFrame ventana_chat=null;
    JButton btn_enviar=null;
    JTextField txt_mensaje=null;
    JTextArea area_chat=null;
    JPanel contenedor_areachat=null;
    JPanel contenedor_btntxt=null;
    JScrollPane scroll=null;
    ServerSocket servidor = null;
    Socket socket = null;
    BufferedReader lector=null;
    PrintWriter escritor = null;

    public Servidor(){

        hacerInterfaz();//Se llama al método hacerInterfaz para que se active
    }
    public void hacerInterfaz(){
        //Se construye la interfaz
        ventana_chat=new JFrame("Servidor");//crea la ventana de chat
        btn_enviar= new JButton("Enviar");//crea el boton para enviar
        txt_mensaje = new JTextField(4);//crea el espacio para escribir
        area_chat = new JTextArea(10,12);//crea area de chat
        scroll= new JScrollPane(area_chat);//crea scroll para bajar si se llena de mensajes
        contenedor_areachat=new JPanel();//Panel para chat
        contenedor_areachat.setLayout(new GridLayout(1,1));
        contenedor_areachat.add(scroll);//Añadir scroll
        contenedor_btntxt= new JPanel();
        contenedor_btntxt.setLayout(new GridLayout(1,2));
        contenedor_btntxt.add(txt_mensaje);
        contenedor_btntxt.add(btn_enviar);
        ventana_chat.setLayout(new BorderLayout());
        ventana_chat.add(contenedor_areachat, BorderLayout.NORTH);
        ventana_chat.add(contenedor_btntxt,BorderLayout.SOUTH);
        ventana_chat.setSize(300, 220);//Tamaño de la ventana
        ventana_chat.setVisible(true);//Hace visible a la ventana
        ventana_chat.setResizable(false);//Hace que la ventana no se le pueda cambiar el tamaño
        ventana_chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        Thread principal = new Thread(new Runnable() {
            public void run() {
                try {
                    servidor = new ServerSocket(9000);
                    while (true){
                        socket = servidor.accept();
                        leer();
                        escribir();
                    }
                }catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        principal.start();
        }
    public void leer(){
        int i=0;
        //int[] dato= new int[3];
        Thread leer_hilo = new Thread(new Runnable() {
            public void run() {
                try{
                    lector= new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while (true){
                        String mensaje_recibido=lector.readLine();
                        area_chat.append("Cliente 1: "+mensaje_recibido+"\n");
                        System.out.println(lector.readLine());

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
        new Servidor();
    }
}
