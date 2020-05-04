package com.example.todoboom;

public class ToDo
{
    // Instance Variables
    private String one_mission;
    private boolean mission_done;

    // Constructor Declaration of Class
    public ToDo(String name)
    {
        this.one_mission = name;
        this.mission_done = false;
    }

    public boolean get_mission_done()
    {
        return mission_done;
    }

    public void mark_mission_done()
    {
        mission_done = true;
    }

    public String get_one_mission()
    {
        return one_mission;
    }
}
