package com.alphaws.javadaytrivia.database;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alphaws.javadaytrivia.beans.User;

import java.util.ArrayList;

public class UserControl {
    Context context;

    public UserControl(Context context){
        this.context = context;
    }

    public long addUser(User user, DataBaseHandler dh){
        long inserted = 0;
        SQLiteDatabase db = dh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataBaseHandler.USER_EMAIL, user.getEmail());
        values.put(DataBaseHandler.USER_FIRTSNAME, user.getFirstName());
        values.put(DataBaseHandler.USER_LASTSNAME, user.getLastName());
        values.put(DataBaseHandler.USER_OCCUPATION, user.getOccupation());
        try{
            inserted = db.insertOrThrow(DataBaseHandler.USER, null, values);
        }catch(Exception e){
            inserted = -1;
        }
        try{
            db.close();
        }catch(Exception e){

        }
        db = null;
        values = null;
        return inserted;
    }

    public int updateUser(User user, DataBaseHandler dh){
        SQLiteDatabase db = dh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataBaseHandler.USER_EMAIL, user.getEmail());
        values.put(DataBaseHandler.USER_FIRTSNAME, user.getFirstName());
        values.put(DataBaseHandler.USER_LASTSNAME, user.getLastName());
        values.put(DataBaseHandler.USER_OCCUPATION, user.getOccupation());
        int count = db.update(DataBaseHandler.USER, values,
                DataBaseHandler.USER_EMAIL + " = ?",
                new String[] {user.getEmail()});
        try{
            db.close();
        }catch(Exception e){
        }
        db = null;
        return count;
    }

    public void deleteUser(User user, DataBaseHandler dh){
        SQLiteDatabase db = dh.getWritableDatabase();
        db.delete(DataBaseHandler.USER, DataBaseHandler.USER_EMAIL + " = ? ",
                new String[] { user.getEmail()});
        try{
            db.close();
        }catch(Exception e){
        }
        db = null;
    }

    public User getUserById(String userEmail, String strWhere,
                            String strOrderBy, DataBaseHandler dh){
        User user = null;
        String selectQuery = "Select "
                + DataBaseHandler.USER_EMAIL + ", "
                + DataBaseHandler.USER_FIRTSNAME + ", "
                + DataBaseHandler.USER_LASTSNAME + ", "
                + DataBaseHandler.USER_OCCUPATION
                + " FROM " + DataBaseHandler.USER
                + " WHERE " + DataBaseHandler.USER_EMAIL
                + " = '" + userEmail + "'";

        if(strWhere != null){
            selectQuery += " AND " + strWhere;
        }
        if(strOrderBy != null){
            selectQuery += " ORDER BY " + strOrderBy;
        }
        SQLiteDatabase db = dh.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            user = new User();
            user.setEmail(cursor.getString(0));
            user.setFirstName(cursor.getString(1));
            user.setLastName(cursor.getString(2));
            user.setOccupation(cursor.getInt(3));
        }
        try{
            cursor.close();
            db.close();
        }catch(Exception e){
        }
        db = null;
        cursor = null;
        return user;
    }

    public ArrayList<User> getUserWhere(String strWhere, String strOrderBy,
                                        DataBaseHandler dh){
        ArrayList<User> userList = new ArrayList<User>();
        String select= "SELECT "
                + DataBaseHandler.USER_EMAIL + ", "
                + DataBaseHandler.USER_FIRTSNAME + ", "
                + DataBaseHandler.USER_LASTSNAME + ", "
                + DataBaseHandler.USER_OCCUPATION
                + " FROM " + DataBaseHandler.USER_EMAIL;

        if (strWhere != null) {
            select += " WHERE " + strWhere;
        }

        if (strOrderBy != null) {
            select += " ORDER BY " + strOrderBy;
        }

        SQLiteDatabase db = dh.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, null);
        if(cursor.moveToFirst()){
            do{
                User user = new User();
                user.setEmail(cursor.getString(0));
                user.setFirstName(cursor.getString(1));
                user.setLastName(cursor.getString(2));
                user.setOccupation(cursor.getInt(3));
                userList.add(user);
            }while(cursor.moveToNext());
        }

        try{
            cursor.close();
            db.close();
        }catch(Exception e){
        }
        cursor = null;
        db = null;

        return userList;
    }

    public ArrayList<User> getAllUsers(DataBaseHandler dh){
        ArrayList<User> userList = new ArrayList<User>();

        SQLiteDatabase db = dh.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from USER",null);

        if(cursor.moveToFirst()){
            do{
                User user = new User();
                user.setEmail(cursor.getString(0));
                user.setFirstName(cursor.getString(1));
                user.setLastName(cursor.getString(2));
                user.setOccupation(cursor.getInt(3));
                userList.add(user);
            }while(cursor.moveToNext());
        }

        try{
            cursor.close();
            db.close();
        }catch(Exception e){
        }
        cursor = null;
        db = null;

        return userList;
    }

}

