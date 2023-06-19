package message.messages;

import app.App;
import app.Config;
import file.FileData;
import file.Files;
import file.KeyComparator;
import message.Message;
import servent.Servent;

import java.util.LinkedList;

public class PullTellMessage extends Message {

    private final FileData fileData;
    private final Servent destination;

    public PullTellMessage(Servent reciever, Servent destination, FileData fileData) {
        super(null, Config.LOCAL, reciever);

        this.destination = destination;
        this.fileData = fileData;
    }

    public PullTellMessage(PullTellMessage m){
        super(m);

        destination = m.destination;
        fileData = m.fileData;
    }

    @Override
    protected Message copy() {
        return new PullTellMessage(this);
    }

    @Override
    public void handle() {

        if(destination.equals(Config.LOCAL)){
            if(fileData==null){
                App.print("Can not pull file");
                return;
            }

            App.print(String.format("\nRecieved from Servent%s\n%s:\n%s\n", getSender().getId(), fileData.toString(), fileData.getContent()));
            return;
        }

        if(Config.NETWORK.getServents().contains(destination)){
            App.send(setReceiver(destination));
        }else{
            LinkedList<Servent> active = new LinkedList<>(Config.NETWORK.getServents());
            active.sort(new KeyComparator(destination.getKey()));
            App.send(new PullTellMessage(active.get(0), destination, fileData));
        }

    }

}
