package cli.command;

import app.App;
import app.Config;
import file.FileData;
import file.Files;
import message.messages.AddMessage;
import servent.Servent;

import java.io.File;

public class AddCommand implements Command{

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public void execute(String args) {

        File file = new File(Files.absolute(Config.WORKSPACE_PATH, args));

        if (!file.exists()) {
            App.error(String.format("Cannot open file or directory on path %s", args));
            return;
        }

        if(file.isFile()){
            handleFile(file);
            return;
        }

        handleDirectory(file);

    }


    private void handleFile(File file){
            FileData filedData = new FileData(file.getPath());
            filedData.load(Config.WORKSPACE_PATH);
            filedData.setKey(file.getAbsolutePath());
            Servent servent = Config.NETWORK.getServent(filedData.getKey());
            App.send(new AddMessage(servent, filedData));
    }

    private void handleDirectory(File dir){

            for(File file: dir.listFiles()){
                this.handleFile(file);
            }
    }

}
