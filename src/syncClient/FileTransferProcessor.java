package syncClient;
import java.io.*;
import java.net.Socket;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.security.*;

public class FileTransferProcessor {

    private final Socket socket;
    private final String folderPath;
    DataInputStream dis;
    DataOutputStream dos;
//    ObjectOutputStream oos;
//    ObjectInputStream ois;

    public FileTransferProcessor(Socket client, String folderPath) {
        this.socket = client;
        this.folderPath = folderPath;
    }
    public void openSocket ()
    {
        try {
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void closeSocket()
    {
        try {
             dis.close();
             dos.close();
             socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public ArrayList<File> getFilesFromFolder() {
        ArrayList<File> files = new ArrayList<>();

        File folder = new File(folderPath);
        if (folder.isDirectory()) {
            File[] fileList = folder.listFiles();
            if (fileList != null) {
                for (File file : fileList) {
                    if (file.isFile()) {
                        files.add(file);
                    }
                }
            }
        }
        return files;
    }
    public void send(ArrayList<File> files) {
        try {

            dos = new DataOutputStream(socket.getOutputStream());

            // Gửi tổng số file

            dos.writeInt(files.size());

            for (File file : files) {

                long length = file.length();

                dos.writeLong(length);

                dos.writeUTF(file.getName());

                dos.writeLong(file.lastModified());
                FileInputStream fis = new FileInputStream(file);

                BufferedInputStream bis = new BufferedInputStream(fis);



                // Sử dụng buffer cho việc đọc file

                byte[] buffer = new byte[4096];

                int bytesRead = 0;

                // Đọc đến khi không còn dữ liệu trong file

                while ((bytesRead = bis.read(buffer)) != -1) {

                    dos.write(buffer, 0, bytesRead);

                }



                bis.close();

            }

            System.out.println("Files sent successfully!");

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
    public void receive() {
            try {

                dis = new DataInputStream(socket.getInputStream());

                // Nhận số lượng file

                int filesCount = dis.readInt();
                System.out.println(filesCount);
                for (int i = 0; i < filesCount; i++) {

                    long fileLength = dis.readLong();

                    String fileName = dis.readUTF();


                    if (fileName == null || fileName.isEmpty()) {

                        System.out.println("Received file name is null or empty.");

                    }

                    long lastModified = dis.readLong();


                    System.out.println("Receiving file: " + fileName);
                    // Tạo luồng ghi file dựa trên tên và kích thước file nhận được
                    File file = new File(folderPath + File.separator + fileName);
                    FileOutputStream fos = new FileOutputStream(file);

                    BufferedOutputStream bos = new BufferedOutputStream(fos);

                    // Buffer để đọc dữ liệu

                    byte[] buffer = new byte[4096]; // 4KB buffer

                    int bytesRead;

                    long bytesReceived = 0;  // Đếm số byte đã nhận để xác định khi nào hoàn thành file

                    while (bytesReceived < fileLength && (bytesRead = dis.read(buffer, 0, Math.min(buffer.length, (int) (fileLength - bytesReceived)))) != -1) {

                        bos.write(buffer, 0, bytesRead);

                        bytesReceived += bytesRead;

                    }
                    bos.flush();
                    bos.close();  // Đóng luồng sau khi ghi file xong

                    boolean setLM = file.setLastModified(lastModified);

                    if (!setLM) {

                        System.err.println("Could not set last modified date for " + fileName);

                    }
                    System.out.println("Received successfully file : " +fileName);
                }

                System.out.println("Files received successfully!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    public void receive1() {
        try {
        //DataInputStream dis = new DataInputStream(socket.getInputStream());

                int filesCount = dis.readInt();
            System.out.println("Ham nhan da vao");
                System.out.println(filesCount);
                //File[] files = new File[filesCount];

                for (int i = 0; i < filesCount; i++) {

                    long fileLength = dis.readLong();

                    String fileName = dis.readUTF();
                    System.out.println("Receiving file : " + fileName);

                    File file = new File(folderPath + File.separator + fileName);

                    FileOutputStream fos = new FileOutputStream(file);

                    BufferedOutputStream bos = new BufferedOutputStream(fos);

                    for (int j = 0; j < fileLength; j++) bos.write(dis.read());

                    bos.close();

                    System.out.println("Received Success : " + fileName);

                }

                System.out.println("Files received successfully!, Total file: " + filesCount);


        } catch (IOException e) {

            e.printStackTrace();

        }
    }
//    public void send(ArrayList<File> files) {
//        try {
//            dos.writeInt(files.size());
//            dos.flush();
//
//            for (File file : files) {
//                dos.writeUTF(file.getName());
//
//                FileInputStream fis = new FileInputStream(file);
//                long fileSize = file.length(); // Get file size
//                dos.writeLong(fileSize); // Send file size
//                System.out.println("fileSize o client :"+ String.valueOf(fileSize));
//                byte[] buffer = new byte[4092];
//                int bytesRead;
//                while ((bytesRead = fis.read(buffer)) != -1) {
//                    dos.write(buffer, 0, bytesRead);
//                }
//                fis.close();
//            }
//            //dos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    public void receive() {
//        //ArrayList<File> receivedFiles = new ArrayList<>();
//        try {
//            int numFiles = dis.readInt(); // Đọc số lượng file
//
//            for (int i = 0; i < numFiles; i++) {
//                String fileName = dis.readUTF(); // Đọc tên file
//                long fileSize = dis.readLong(); // Đọc kích thước file
//
//                FileOutputStream fos = new FileOutputStream(folderPath + File.separator + fileName);
//                byte[] buffer = new byte[4092];
//                long remainingBytes = fileSize;
//                int bytesRead;
//                while (remainingBytes > 0 && (bytesRead = dis.read(buffer, 0, (int) Math.min(buffer.length, remainingBytes))) != -1) {
//                    fos.write(buffer, 0, bytesRead);
//                    remainingBytes -= bytesRead;
//                }
//                fos.close();
//
//                //receivedFiles.add(new File(fileName));
//            }
//            //dis.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    public void receive() {
//        try {
//
//            int numberOfFiles = dis.readInt();
//            System.out.println("Number of Files to be received: " + numberOfFiles);
//
//            for (int i = 0; i < numberOfFiles; i++) {
//                String fileName = dis.readUTF();
//                long fileSize = dis.readLong(); // Read the file size
//                System.out.println(fileSize);
//                System.out.println("Receiving file: " + fileName);
//
//                FileOutputStream fos = new FileOutputStream(folderPath + File.separator + fileName);
//
//                byte[] buffer = new byte[4092];
//                int bytesRead;
//                long totalBytesRead = 0;
//                while (totalBytesRead < fileSize && (bytesRead = dis.read(buffer, 0, (int) Math.min(buffer.length, fileSize - totalBytesRead))) != -1) {
//                    fos.write(buffer, 0, bytesRead);
//                    totalBytesRead += bytesRead;
//                    System.out.println("fileSize: " +fileSize);
//                    System.out.println("Dang nhan file " + fileName + "    " + totalBytesRead);
//                }
//                System.out.println("Nhan thanh cong file : " + fileName);
//                fos.close();
//            }
//            System.out.println("Ket thuc nhan file");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    public void sendFileToServer(ArrayList<File> files) {
//        try {
//            sendFileInfo(files);
//            sendData(files);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    public void receive() {
//        try {
//            receiveFileInfo();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    public void sendFileInfo(ArrayList<File> files) {
//        try {
//            dos.writeInt(files.size());
//            dos.flush();
//
//            for (File file : files) {
//                dos.writeUTF(file.getName());
//                dos.writeLong(file.length()); // Send file size
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void sendData(ArrayList<File> files) {
//        try {
//            for (File file : files) {
//                fis = new FileInputStream(file);
//                byte[] buffer = new byte[4092];
//                int bytesRead;
//                while ((bytesRead = fis.read(buffer)) != -1) {
//                    dos.write(buffer, 0, bytesRead);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void receiveFileInfo() {
//        try {
//            int numberOfFiles = dis.readInt();
//            System.out.println("Number of Files to be received: " + numberOfFiles);
//
//            for (int i = 0; i < numberOfFiles; i++) {
//                String fileName = dis.readUTF();
//                long fileSize = dis.readLong(); // Read the file size
//                System.out.println("Receiving file: " + fileName);
//
//                receiveData(fileName, fileSize);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void receiveData(String fileName, long fileSize) {
//        try {
//            fos = new FileOutputStream(folderPath + File.separator + fileName);
//
//            byte[] buffer = new byte[4092];
//            int bytesRead;
//            long totalBytesRead = 0;
//
//            while (totalBytesRead < fileSize && (bytesRead = dis.read(buffer, 0, (int) Math.min(buffer.length, fileSize - totalBytesRead))) != -1) {
//                fos.write(buffer, 0, bytesRead);
//                System.out.println("Dang nhan file");
//                totalBytesRead += bytesRead;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    public void sendFileName(ArrayList<String> fileName) {
        try {

            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            oos.writeObject(fileName);
            oos.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<String> reciveFileName() {
        ArrayList<String> filenamesToDelete = new ArrayList<>();
        try {
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            filenamesToDelete = (ArrayList<String>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filenamesToDelete;
    }
    public ArrayList<String> getFileNameFromFiles(ArrayList<File> files)
    {
        ArrayList<String> listFileName = new ArrayList<>();
        for(File file : files)
        {
            listFileName.add(file.getName());
        }
        return listFileName;
    }
    public void sendMD5(ArrayList<String> md5List) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            oos.writeObject(md5List);
            oos.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<String> reciveMD5List() {
        ArrayList<String> md5List = new ArrayList<>();
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            md5List = (ArrayList<String>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5List;
    }

    public ArrayList<String> filterFilesFromMD5(ArrayList<String> fileNameModified, ArrayList<File> fileDir, ArrayList<String> md5List) {
        ArrayList<String> fileNameNotChanged = new ArrayList<>();
        ArrayList<String> md5ListDir = getMD5(fileDir);
        try {
            for (int i = 0; i < fileDir.size(); i++) {
                for (int j = 0; j < fileNameModified.size(); j++) {
                    if ((fileDir.get(i)).getName().equals(fileNameModified.get(j))) {
                        if (md5ListDir.get(i).equals(md5List.get(j))) {
                            fileNameNotChanged.add(fileNameModified.get(j));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileNameNotChanged;
    }

    public String calculateMD5(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        InputStream inputStream = new FileInputStream(file);

        byte[] byteArray = new byte[8192];
        int bytesRead;
        while ((bytesRead = inputStream.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesRead);
        }

        byte[] hashedBytes = digest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte hashedByte : hashedBytes) {
            sb.append(String.format("%02x", hashedByte));
        }

        inputStream.close();
        return sb.toString();
    }

    public ArrayList<String> getMD5(ArrayList<File> fileList) {
        ArrayList<String> md5List = new ArrayList<>();

        for (File file : fileList) {
            try {
                String md5 = calculateMD5(file);
                md5List.add(md5);
            } catch (IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        return md5List;
    }

    public ArrayList<File> removeFilesByName(ArrayList<File> files, ArrayList<String> filenames) {

        Iterator<File> iterator = files.iterator(); // Tạo một iterator để duyệt qua ArrayList<File>.

        while (iterator.hasNext()) { // Duyệt qua từng phần tử trong ArrayList<File>.

            File file = iterator.next();

            if (filenames.contains(file.getName())) { // Kiểm tra xem tên file có trong ArrayList<String> không.

                iterator.remove(); // Xóa file có tên trùng khớp.

            }

        }
        return files;
    }

    public void recieveAndDeletedFile() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            ArrayList<String> filenamesToDelete = (ArrayList<String>) ois.readObject();
            deleteFiles(filenamesToDelete);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deleteFiles(ArrayList<String> filenamesToDelete) {

        for (String filename : filenamesToDelete) {
            File fileToDelete = new File(folderPath, filename);

            System.out.println(fileToDelete);
            if (fileToDelete.exists()) {
                boolean isDeleted = fileToDelete.delete();
                if (isDeleted) {
                    System.out.println("Deleted file: " + filename);
                } else {
                    System.out.println("Failed to delete file: " + filename);
                }
            } else {
                System.out.println("File does not exist: " + filename);
            }
        }
    }

    public void sendMessage(String message) {
        try {
            dos.writeUTF(message);
            dos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String receiveMessage() {
        String status = null;
        try {
            status = dis.readUTF();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }
    public void sendMap(Map<String, String> map) {
        try{
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(map);
            oos.flush();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public Map<String, String> receiveMap()
    {
        Map<String, String> map = new HashMap<String, String>();
        try{
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            map = (Map<String, String>) ois.readObject();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return map;
    }
    public Map<String, String> getMap(ArrayList<File> file){
        //ArrayList<File> allFile = getFilesFromFolder();
        ArrayList<String> md5List = getMD5(file);

        Map<String,String> mapFile = new HashMap<>();
        for(int i = 0; i < file.size(); i++){
            mapFile.put(file.get(i).getName(),md5List.get(i));
        }
        return mapFile;
    }
    public ArrayList<String> compareMD5(Map<String, String> sourceFile, Map<String, String> destinationFile){
        Set<String> fileNameSource = sourceFile.keySet();
        Set<String> fileNameDestination = destinationFile.keySet();

        ArrayList<String> similarFileName = new ArrayList<>();


        Set<String> intersection = new HashSet<>(fileNameSource);

        intersection.retainAll(fileNameDestination);

        for(String fileName : intersection){
            String md5Src = sourceFile.get(fileName);
            String md5Des = destinationFile.get(fileName);
            if(md5Src.equals(md5Des)) similarFileName.add(fileName);
        }

        return similarFileName;
    }
    public void syncWithMD5(ArrayList<File> files){
        Map<String, String> serverFiles = getMap(files);
        Map<String, String> clientFiles = receiveMap();
        ArrayList<String> similarFileName = compareMD5(serverFiles, clientFiles);
        ArrayList<File> fileToSync = removeFilesByName(files, similarFileName);
        send(fileToSync);
    }
}
