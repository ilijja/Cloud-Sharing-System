package message.messages;

import app.Config;
import message.Message;
import servent.Servent;

public class PongMessage extends Message {

    private static final long serialVersionUID = 1L;

    private final boolean check;

    public PongMessage(Servent receiver, boolean check) {
        super(null, Config.LOCAL, receiver);
        this.check = check;
    }

    public PongMessage(PongMessage m) {
        super(m);

        check = m.check;
    }

    @Override
    public boolean shouldPrint() {
        return false;
    }

    @Override
    protected Message copy() {
        return new PongMessage(this);
    }

    @Override
    public void handle() {
        if (isCheck()) {
            Config.NETWORK.uncheck(getSender());
        } else {
            Config.NETWORK.pong(getSender());
        }
    }

    public boolean isCheck() {
        return check;
    }
}
