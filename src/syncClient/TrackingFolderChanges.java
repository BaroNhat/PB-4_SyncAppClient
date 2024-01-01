package syncClient;

import java.io.File;
import java.util.*;

public class TrackingFolderChanges {
    private  String directoryPath;
    private  Map<String, FileInfo> initialState = new HashMap<>();
    public ArrayList<File> filesAdd = new ArrayList<>();
    public ArrayList<File> filesModified = new ArrayList<>();
    public ArrayList<String> filesDeleted = new ArrayList<>();
    public TrackingFolderChanges(String folderPath) {
        this.directoryPath = folderPath;
    }

    private  class FileInfo {
        long lastModified;
        long fileSize;

        public FileInfo(long lastModified, long fileSize) {
            this.lastModified = lastModified;
            this.fileSize = fileSize;
        }
    }

    public ArrayList<File> getFilesAdd() {
        return filesAdd;
    }

    public ArrayList<File> getFilesModified() {
        return filesModified;
    }

    public ArrayList<String> getFilesDeleted()
    {
        return filesDeleted;
    }
    //Kiem tra su thay doi trong folder
    public  void checkForChanges() {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        if (files != null) {
            Set<String> currentFiles = new HashSet<>();
            for (File file : files) {
                currentFiles.add(file.getAbsolutePath());
                FileInfo previousInfo = initialState.get(file.getAbsolutePath());
                if (previousInfo == null) {
                    System.out.println("File added: " + file.getAbsolutePath());
                    filesAdd.add(file);
                    initialState.put(file.getAbsolutePath(), new FileInfo(file.lastModified(), file.length()));
                } else {
                    long currentModificationTime = file.lastModified();
                    long previousModificationTime = previousInfo.lastModified;

                    if (currentModificationTime != previousModificationTime || file.length() != previousInfo.fileSize) {
                        System.out.println("File modified: " + file.getAbsolutePath());
                        filesModified.add(file);
                        initialState.put(file.getAbsolutePath(), new FileInfo(currentModificationTime, file.length()));
                    }
                }
            }

            Set<String> removedFiles = new HashSet<>(initialState.keySet());
            removedFiles.removeAll(currentFiles);

            for (String removedFile : removedFiles) {
                int lastSeparatorIndex = removedFile.lastIndexOf("\\");
                if (lastSeparatorIndex >= 0) {
                    String fileName = removedFile.substring(lastSeparatorIndex + 1);
                    filesDeleted.add(fileName);
                }
                System.out.println("File deleted: " + removedFile);
                initialState.remove(removedFile);
            }
        }
    }
    //get trang thai ban dau cua folder
    public  void getInitialState() {

        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                initialState.put(file.getAbsolutePath(), new FileInfo(file.lastModified(), file.length()));
            }
        }
        System.out.println("Initial state: " + initialState);
    }
    public void resetChangesLists() {
        filesAdd.clear();
        filesModified.clear();
        filesDeleted.clear();
    }

}