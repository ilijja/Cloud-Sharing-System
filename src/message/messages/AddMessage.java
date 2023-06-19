package message.messages;

import app.App;
import app.Config;
import file.FileData;
import message.Message;
import servent.Servent;

public class AddMessage extends Message {

    private static final long serialVersionUID = 1L;

    private final FileData data;

    public AddMessage(Servent receiver, FileData data) {
        super(null, Config.LOCAL, receiver);

        this.data = data;
    }

    public AddMessage(AddMessage m) {
        super(m);

        data = m.data;
    }

    @Override
    protected Message copy() {
        return new AddMessage(this);
    }

    @Override
    public void handle() {
        Servent servent = Config.NETWORK.getServent(getData().getKey());

        if(servent.equals(Config.LOCAL)){
            boolean added = Config.STORAGE.add(getData());

            if(added){
                App.print(String.format("Succesfully added: %s into Servent%s's storage", data.getPath(), servent.getId()));
            }else{
                App.print(String.format("File: %s alredy exists in Servent%s's storage", data.getPath(), servent.getId()));
            }

        }else{
            App.send(setReceiver(servent));
        }


    }

    @Override
    public String toString() {
        return super.toString() + " with data " + getData();
    }

    public FileData getData() {
        return data;
    }

}

