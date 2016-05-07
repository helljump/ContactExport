package ru.zipta.contactexport;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by helljump on 06.05.16.
 */
public class MyContact {

    public String id;
    public String name;
    public List<String> phones = new ArrayList<>();

    public MyContact(String id, String name) {
        this.id = id;
        this.name = name;
    }

}
