package file;

import app.App;
import app.Config;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class FileData implements Serializable {

    public static final int VERSION_LATEST = -1;

    private final String path;
    private final Map<Integer, String> history;
    private String content;

    private Key key;

    public FileData(String path) {
        this.path = Files.relative(Config.WORKSPACE_PATH, path);

        history = new ConcurrentHashMap<>();
    }


    public void load(String location) {
        try {
            setContent(new String(java.nio.file.Files.readAllBytes(Paths.get(Files.absolute(location, getPath()))), StandardCharsets.US_ASCII).trim());
        } catch (IOException e) {
            App.error(String.format("Cannot load file %s (%s)", getPath(), e.getMessage()));
        }
    }

    public String getPath() {
        return path;
    }

    public String getContent() {
        return content;
    }

    private void setContent(String content) {
        this.content = content;
    }


    public void setKey(String path) {
        this.key = new Key(path);
    }


    public Key getKey() {
        return key;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof FileData) {
            FileData other = (FileData) obj;
            return getKey().equals(other.getKey());
        }

        return false;
    }



    @Override
    public int hashCode() {
        return Objects.hash(getPath());
    }

    @Override
    public String toString() {
        return getPath();
    }
}
