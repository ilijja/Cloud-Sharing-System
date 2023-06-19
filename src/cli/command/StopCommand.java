package cli.command;

import app.ServentMain;

public class StopCommand implements Command {
    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public void execute(String args) {
        ServentMain.stop();
    }

}
