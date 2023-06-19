package message.messages;

import app.App;
import app.Config;
import message.Message;
import servent.Servent;

import java.util.LinkedList;
import java.util.List;

public class CheckedMessage extends Message {


    private final Servent checkedServent;
    private final boolean stillThere;

    private final LinkedList<Servent> route;

    public CheckedMessage(LinkedList<Servent> route, Servent checkedServent, boolean stillThere) {
        super(null, Config.LOCAL, route.get(route.size()-1));

        this.route = route;
        this.checkedServent = checkedServent;
        this.stillThere = stillThere;
    }

    public CheckedMessage(CheckedMessage m) {
        super(m);

        this.route = m.route;
        this.checkedServent = m.checkedServent;
        this.stillThere = m.stillThere;
    }

    @Override
    protected Message copy() {
        return new CheckedMessage(this);
    }

    @Override
    public void handle() {

        if(!stillThere){
            return;
        }

        if(route.size()==1 && stillThere){
            Config.NETWORK.pong(this.checkedServent);
            return;
        }

        route.remove(route.size()-1);
        Servent last = route.get(route.size() - 1);
        App.send(this.setReceiver(last));

    }

    @Override
    public boolean shouldPrint() {
        return false;
    }

}
