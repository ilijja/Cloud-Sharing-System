package message.messages;

import app.App;
import app.Config;
import file.KeyComparator;
import message.Message;
import servent.Servent;

import java.util.LinkedList;
import java.util.List;

public class CheckMessage extends Message {

    private static final long serialVersionUID = 1L;
    private final Servent serventToCheck;

    public CheckMessage(Servent receiver, Servent check) {
        super(null, Config.LOCAL, receiver);

        this.serventToCheck = check;
    }

    public CheckMessage(CheckMessage m) {
        super(m);
        this.serventToCheck = m.serventToCheck;
    }

    @Override
    protected Message copy() {
        return new CheckMessage(this);
    }

    @Override
    public void handle() {

        if(!Config.NETWORK.containsServent(serventToCheck)){
            List<Servent> active = new LinkedList<>(Config.NETWORK.getServents());
            active.remove(this.getSender());
            active.sort(new KeyComparator(serventToCheck.getKey()));
            App.send(this.setReceiver(active.get(0)));
            return;
        }

        Config.NETWORK.check(serventToCheck);
        boolean active = Config.NETWORK.isStillThere(serventToCheck);
        LinkedList<Servent> route = new LinkedList<>(this.getRoute());
        App.send(new CheckedMessage(route, serventToCheck, active));

//        App.send(new CheckedMessage(getSender(), serventToCheck, active));
    }

    @Override
    public boolean shouldPrint() {
        return false;
    }


}
