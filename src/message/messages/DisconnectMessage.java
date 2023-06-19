package message.messages;

import app.Config;
import message.Message;
import servent.Network;
import servent.Servent;

public class DisconnectMessage extends Message {


    public DisconnectMessage(Servent servent) {
        super(null, Config.LOCAL, servent);

    }

    @Override
    protected Message copy() {
        return null;
    }

    @Override
    public void handle() {
        Config.NETWORK.removeServent(getSender());
    }


}
