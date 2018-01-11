package com.nevermore.mapasignala.server;

public class ServerStatus {
    public final String version;
    public final String motd;

    public ServerStatus(String version, String motd) {
        this.version = version;
        this.motd = motd;
    }
}
