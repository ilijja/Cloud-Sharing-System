package message.messages;

import app.App;
import app.Config;
import file.FileData;
import file.Files;
import file.Key;
import message.Message;
import servent.Servent;

import java.util.LinkedList;

public class PullAskMessage extends Message {

    private final Key key;

    public PullAskMessage(Servent reciever, Key key) {
        super(null, Config.LOCAL, reciever);

        this.key = key;
    }

    public PullAskMessage(PullAskMessage m) {
        super(m);

        key = m.key;
    }

    @Override
    protected Message copy() {
        return new PullAskMessage(this);
    }

    @Override
    public void handle() {

        Servent servent = Config.NETWORK.getServent(key);

        if(servent.equals(Config.LOCAL)){
            FileData found = Config.STORAGE.find(key);
            handleRoute(found, getSender());
        }else{
            App.send(setReceiver(servent));
        }

    }

    private void handleRoute(FileData found, Servent destination) {

        Servent[] servent = Config.NETWORK.getServents(destination.getKey(), false);

        App.send(new PullTellMessage(servent[0], destination, found));

    }


}
