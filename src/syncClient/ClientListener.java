package syncClient;

import UI.formClient;

import java.net.Socket;
import java.util.Map;

public class ClientListener implements Runnable {
    private final Client client;
    private final Socket clientSocket;
    private final String folderPath;

    private FileTransferProcessor fileProcessor;
    public ClientListener(Client client,Socket clientSocket, String folderPath)
    {
        this.client = client;
        this.clientSocket = clientSocket;
        this.folderPath = folderPath;
    }

    @Override
    public void run() {
        try
        {
            System.out.println("Starting ClientListener");
            fileProcessor = client.getFileProcessor();
            ListenMessage();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void ListenMessage()
    {
        while (true) {
            String status = fileProcessor.receiveMessage();
            System.out.println("Receive from server: " + status);

            switch (status) {
                case "List file deleted":
                    fileProcessor.recieveAndDeletedFile();
                    break;
                case "List file added":
                    fileProcessor.receive();
                    break;
                case "List file modified":
                    Map<String,String> fileClient = fileProcessor.getMap(fileProcessor.getFilesFromFolder());
                    fileProcessor.sendMap(fileClient);
                    fileProcessor.receive();
                    break;
                default:
                    break;
            }
        }
    }
}
