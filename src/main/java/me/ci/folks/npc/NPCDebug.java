package me.ci.folks.npc;

public class NPCDebug {

    private String state = "idle";
    private float[] path = new float[0];

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }

    public void setPath(float[] path) {
        this.path = path;
    }

    public float[] getPath() {
        return this.path;
    }
}
