package app;



import cli.CLIParser;
import message.messages.JoinAskMessage;
import message.processing.MessageHandler;
import message.processing.MessageListener;
import servent.ServentPinger;

import java.io.File;

public class ServentMain {

    private static MessageHandler handler;
    private static MessageListener listener;
    private static CLIParser parser;
    private static ServentPinger pinger;

    private static boolean isServent;

    public static void main(String[] args) {

        int servent = Integer.parseInt(args[1]);

        Config.load(args[0], servent);

        isServent = servent > 0;

        if (!isServent) {
            Config.LOCAL = Config.BOOTSTRAP;
        }

        handler = new MessageHandler();
        new Thread(handler).start();

        listener = new MessageListener();
        new Thread(listener).start();

        parser = new CLIParser();
        new Thread(parser).start();

        if (isServent) {
            pinger = new ServentPinger();
            new Thread(pinger).start();


            App.print("Started servent " + Config.LOCAL);

            App.send(new JoinAskMessage());

        } else {
            App.print("Started bootstrap server " + Config.LOCAL);
        }
    }

    public static void stop() {
        if (isServent) {
            pinger.stop();
        }

        parser.stop();
        listener.stop();
        handler.stop();

        if (isServent) {
            App.print("Servent stopped");
        } else {
            App.print("Bootstrap stopped");
        }

        System.exit(0);
    }
}
