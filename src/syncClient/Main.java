package syncClient;
import UI.formClient;

import java.net.InetAddress;

public class Main {
    private static final int port = 8888;
    public static void main(String[] args) {

       formClient client = new formClient();
        client.setFormClient(client);
        String folderPathClient = null;
        while(folderPathClient == null){
            folderPathClient = client.getFolderPath();
        }
        client.connectToServer(folderPathClient, getServerIP(),port);

        client.requestSync();

        client.startListenServer();


    }

    public static String getServerIP()
    {
        InetAddress address = null;
        try{
             address = InetAddress.getByName("SERVER-PBL4");
            System.out.println("IP: " + address.getHostAddress());
        }catch (Exception e){
            e.printStackTrace();
        }
        return address.getHostAddress();
    }
}
