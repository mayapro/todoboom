package com.example.todoboom;


import android.util.Log;

public class ToDo {
    // Instance Variables
    private String content ;
    private String oldContent;
    private String creation_timestamp;
    private String edit_timestamp;
    private Integer mission_id;
    private Integer is_done;


    static final int DONE = 1;
    static final int NOT_DONE = 0;

    // Constructor Declaration of Class
    public ToDo(String name, String oldName, String timestamp, String editstamp,  int state, int newId)
    {
        this.content = name;
        this.oldContent = oldName;
        this.creation_timestamp = timestamp;
        this.edit_timestamp = editstamp;
        this.is_done = state;
        this.mission_id = newId;
    }

    /**
     * @return the status of the mission - done or not
     */
    public Integer get_mission_state()
    {
        return is_done;
    }

    /**
     * changes the status of the mission to done
     */
    public void mark_mission_done()
    {
        is_done = DONE;
        this.oldContent = String.valueOf(content);
        this.content  = content  + "  done!";
    }

    /**
     * changes the status of the mission to done
     */
    public void mark_mission_not_done(String editstamp)
    {
        is_done = NOT_DONE;
        this.content =  String.valueOf(oldContent);
        this.edit_timestamp = editstamp;
    }

    public void edit_mission(String edited, String editstamp)
    {
        this.content = edited;
        this.edit_timestamp = editstamp;
    }

    /**
     * @return the mission (string)
     */
    public String get_one_mission()
    {
        return content ;
    }

    /**
     * @return the mission (string)
     */
    public String get_old_mission()
    {
        return oldContent ;
    }

    /**
     * @return the timestamp (string)
     */
    public String get_creation_timestamp()
    {
        return creation_timestamp ;
    }

    /**
     * @return the edit_timestamp (string)
     */
    public String get_edit_timestamp()
    {
        return edit_timestamp ;
    }

    /**
     * @return the mission_id (int)
     */
    public Integer get_mission_id()
    {
        return mission_id ;
    }


}
