package com.example.todoboom;



public class ToDo {
    // Instance Variables
    private String one_mission;
    private Integer mission_done;

    static final int DONE = 1;
    static final int NOT_DONE = 0;

    // Constructor Declaration of Class
    public ToDo(String name, int state)
    {
        this.one_mission = name;
        this.mission_done = state;
    }

    public Integer get_mission_state()
    {
        return mission_done;
    }

    public void mark_mission_done()
    {
        mission_done = DONE;
        one_mission = one_mission + "  done!";
    }

    public String get_one_mission()
    {
        return one_mission;
    }

}
