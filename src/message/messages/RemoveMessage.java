package message.messages;

import app.App;
import app.Config;
import file.FileData;
import file.Key;
import message.Message;
import servent.Servent;

public class RemoveMessage extends Message {

    private final Key key;

    public RemoveMessage(Servent reciever, Key key) {
        super(null, Config.LOCAL, reciever);

        this.key = key;
    }

    public RemoveMessage(RemoveMessage m) {
        super(m);

        this.key = m.key;
    }

    @Override
    protected Message copy() {
        return new RemoveMessage(this);
    }

    @Override
    public void handle() {

        Servent servent = Config.NETWORK.getServent(key);

        if(servent.equals(Config.LOCAL)){
            FileData removed = Config.STORAGE.remove(key);
            handleRoute(removed, getSender());
        }else{
            App.send(setReceiver(servent));
        }
    }

    private void handleRoute(FileData removed, Servent destination) {

        Servent[] servent = Config.NETWORK.getServents(destination.getKey(), false);

        App.send(new RemovedMessage(servent[0], destination, removed));

    }

}
