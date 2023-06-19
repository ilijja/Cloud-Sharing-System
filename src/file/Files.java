package file;

import app.Config;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Files {

    private final Set<FileData> files;

    public Files() {
        files = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    public static String absolute(String directory, String file) {
        return directory.replace("{id}", String.valueOf(Config.LOCAL.getId())) + file;
    }

    public static String relative(String directory, String file) {
        return file.replace(directory.replace("{id}", String.valueOf(Config.LOCAL.getId())), "");
    }


    public boolean add(FileData data) {
        return files.add(data);
    }

    public FileData find(Key key){

        for (FileData fileData : files) {
            if(fileData.getKey().toString().equals(key.toString())){
                return fileData;
            }
        }

        return null;
    }

    public FileData remove(Key key){
        for (FileData fileData : files) {
            if(fileData.getKey().toString().equals(key.toString())){
                FileData data = fileData;
                files.remove(fileData);
                return data;
            }
        }

        return null;
    }


}
