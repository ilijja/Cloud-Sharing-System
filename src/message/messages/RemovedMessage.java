package message.messages;

import app.App;
import app.Config;
import file.FileData;
import file.Key;
import file.KeyComparator;
import message.Message;
import servent.Servent;

import java.util.LinkedList;

public class RemovedMessage extends Message {

    private final FileData removed;
    private final Servent destination;

    public RemovedMessage(Servent reciever, Servent destination, FileData removed) {
        super(null, Config.LOCAL, reciever);

        this.destination = destination;
        this.removed = removed;
    }

    public RemovedMessage(RemovedMessage m) {
        super(m);

        this.destination = m.destination;
        this.removed = m.removed;
    }

    @Override
    protected Message copy() {
        return new RemovedMessage(this);
    }

    @Override
    public void handle() {

        if(destination.equals(Config.LOCAL)){

            if(removed == null){
                App.print("Can not remove data");
                return;
            }

            App.print(String.format("Succesfully removed: %s from Servent%s's storage", removed.getPath(), getSender().getId()));
            return;
        }

        if(Config.NETWORK.getServents().contains(destination)){
            App.send(setReceiver(destination));
        }else{
            LinkedList<Servent> active = new LinkedList<>(Config.NETWORK.getServents());
            active.sort(new KeyComparator(destination.getKey()));
            App.send(new PullTellMessage(active.get(0), destination, removed));
        }

    }

}
