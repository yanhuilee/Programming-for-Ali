package com.example.design_pattern;

/**
 * @Author: Lee
 * @Date: 2019/04/20 21:15
 * 命令模式：队列请求，日志请求（记录和恢复）
 */
public class Command_ {
    public static void main(String[] args) {
        SimpleRemoteControl remote = new SimpleRemoteControl();
        Light light = new Light();
        Command lightOnCommand = new LightOnCommand(light);
        remote.setCommand(lightOnCommand);
        remote.buttonWasPassed();

        GarageDoor garageDoor = new GarageDoor();
        Command garageDoorCommand = new GarageDoorOpenCommand(garageDoor);
        remote.setCommand(garageDoorCommand);
        remote.buttonWasPassed();
    }
}


interface Command {
    void execute();
    void undo();
}

class LightOnCommand implements Command {
    Light light;

    public LightOnCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.on();
    }

    @Override
    public void undo() {
        light.off();
    }
}

class GarageDoorOpenCommand implements Command {
    GarageDoor garageDoor;

    public GarageDoorOpenCommand(GarageDoor garageDoor) {
        this.garageDoor = garageDoor;
    }

    @Override
    public void execute() {
        garageDoor.on();
    }

    @Override
    public void undo() {
        garageDoor.off();
    }
}

class NoCommand implements Command {

    @Override
    public void execute() {}

    @Override
    public void undo() {}
}

class Light {
    void on() {
        System.out.println("Light on");
    }
    void off() {
        System.out.println("Light off");
    }
}

class GarageDoor {
    void on() {
        System.out.println("GarageDoor on");
    }
    void off() {
        System.out.println("GarageDoor off");
    }
}

/**
 * 遥控器
 */
class SimpleRemoteControl {
    Command slot;

    void setCommand(Command command) {
        slot = command;
    }

    void buttonWasPassed() {
        slot.execute();
        slot.undo();
    }
}

class RemoteControl {
    Command[] onCommands;
    Command[] offCommands;

    public RemoteControl() {
        onCommands = new Command[7];
        offCommands = new Command[7];
        Command noCommand = new NoCommand();
    }
}