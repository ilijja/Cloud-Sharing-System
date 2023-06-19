package message.messages;

import app.App;
import app.Config;
import message.Message;
import servent.Servent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PingMessage extends Message {

    private static final long serialVersionUID = 1L;

    private final boolean check;

    public PingMessage(Servent receiver, boolean check) {
        super(null, Config.LOCAL, receiver);

        this.check = check;
    }

    public PingMessage(PingMessage m) {
        super(m);

        check = m.check;
    }

    @Override
    public boolean shouldPrint() {
        return false;
    }

    @Override
    protected Message copy() {
        return new PingMessage(this);
    }

    @Override
    public void handle() {
        Config.NETWORK.addServent(getSender());
//        Config.NETWORK.refresh(getSender());
        App.send(new PongMessage(getSender(), isCheck()));
    }

    public boolean isCheck() {
        return check;
    }

}
