package cli.command;

import app.App;
import app.Config;
import file.Files;
import file.Key;
import message.messages.RemoveMessage;
import servent.Servent;

import java.io.File;

public class RemoveCommand implements Command{

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public void execute(String args) {

        File file = new File(args);

        if (!file.exists()) {
            App.error(String.format("Cannot open file or directory on path %s", args));
            return;
        }

        if(file.isFile()){
            Key key = new Key(file.getAbsolutePath());
            Servent servent = Config.NETWORK.getServent(key);

            App.send(new RemoveMessage(servent, key));
        }

    }

}
