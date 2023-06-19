package cli.command;

import app.App;
import app.Config;
import file.FileData;
import file.Key;
import message.messages.PullAskMessage;
import servent.Servent;

import java.io.File;

public class PullCommand implements Command{

    @Override
    public String getName() {
        return "pull";
    }

    @Override
    public void execute(String args) {

        File file = new File(args);

        if (!file.exists()) {
            App.error(String.format("Cannot open file or directory on path %s", args));
            return;
        }

        Key key = new Key(file.getAbsolutePath());
        Servent servent = Config.NETWORK.getServent(key);

        App.send(new PullAskMessage(servent, key));

    }

}

