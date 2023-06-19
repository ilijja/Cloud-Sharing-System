package message.messages;

import app.App;
import app.Config;
import message.Message;
import servent.Servent;

public class FailMessage extends Message {

    private final Servent failedServent;

    public FailMessage(Servent servent) {
        super(null, Config.LOCAL, Config.BOOTSTRAP);

        this.failedServent = servent;
    }

    @Override
    protected Message copy() {
        return null;
    }

    @Override
    public void handle() {
        if (Config.NETWORK.removeServent(failedServent)) {
            App.print(String.format("Servent %s failed", this.failedServent));
        }
    }


}
