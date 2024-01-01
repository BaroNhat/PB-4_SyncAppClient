package syncClient;

import UI.formClient;

import java.io.File;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

public class Client {
    private final String IpAddress;
    private final int port;
    private Socket clientSocket;
    private final String folderPath;
    private final TrackingFolderChanges agent;
    private FileTransferProcessor fileProcessor;
    public Socket getClientSocket() {
        return clientSocket;
    }

    public String getFolderPath() {
        return folderPath;
    }
    public FileTransferProcessor getFileProcessor() {
        return fileProcessor;
    }

    public Client(String IpAddress, int port, String folderPath) {
        this.IpAddress = IpAddress;
        this.port = port;

        this.folderPath = folderPath;
        agent = new TrackingFolderChanges(folderPath);
    }

    public void connect(){
        try {
            clientSocket = new Socket(IpAddress, port);
            fileProcessor = new FileTransferProcessor(clientSocket, folderPath);
            fileProcessor.openSocket();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void requestSyncFromServer(){
        FileTransferProcessor firstSync = new FileTransferProcessor(clientSocket, folderPath);
        Map<String,String> clientFiles = firstSync.getMap(firstSync.getFilesFromFolder());

        firstSync.sendMap(clientFiles);

        firstSync.recieveAndDeletedFile();

        firstSync.receive();

        agent.resetChangesLists();
        agent.getInitialState();
    }
    public void SyncToServer(){
        System.out.println(fileProcessor);
        agent.resetChangesLists();
        agent.checkForChanges();
        ArrayList<String> filenameDeleted = agent.getFilesDeleted();
        ArrayList<File> filesAdd = agent.getFilesAdd();
        ArrayList<File> filesModified = agent.getFilesModified();

        sendFilesChangedToServer(filenameDeleted, filesAdd, filesModified);

    }
    public void sendFilesChangedToServer(ArrayList<String> filenameDeleted, ArrayList<File> filesAdd,
                                        ArrayList<File> filesModified) {

        if (!filenameDeleted.isEmpty()) {
            try {
                System.out.println("1");
                fileProcessor.sendMessage("List file deleted");
                fileProcessor.sendFileName(filenameDeleted);
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!filesAdd.isEmpty()) {
            try {
                System.out.println("2");
                fileProcessor.sendMessage("List file added");
                fileProcessor.send(filesAdd);
                Thread.sleep(1000);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!filesModified.isEmpty()) {
            try {
                System.out.println("3");
                fileProcessor.sendMessage("List file modified");
                fileProcessor.syncWithMD5(filesModified);
                Thread.sleep(1000);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (filenameDeleted.isEmpty() && filesAdd.isEmpty() && filesModified.isEmpty()) {
            try {
               formClient myForm = formClient.getInstance();
               myForm.showMessage("NO THING TO CHANGES \nDON'T NEED TO SYNCHRONIZE");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!filenameDeleted.isEmpty() || !filesAdd.isEmpty() || !filesModified.isEmpty()) {
            fileProcessor.sendMessage("Done");
        }
    }
    public void messageClose()
    {
        FileTransferProcessor fileProcessor = new FileTransferProcessor(clientSocket, folderPath);
        fileProcessor.openSocket();
        fileProcessor.sendMessage(clientSocket + " Disconnected !");
    }

    public void startListener()
    {
        ClientListener clientListener = new ClientListener(this,getClientSocket(), getFolderPath());
        new Thread(clientListener).start();
    }
}
