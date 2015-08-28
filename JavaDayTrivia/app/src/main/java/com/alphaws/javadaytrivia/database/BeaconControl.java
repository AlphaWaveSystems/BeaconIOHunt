package com.alphaws.javadaytrivia.database;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alphaws.javadaytrivia.beans.Beacon;

import java.util.ArrayList;

public class BeaconControl {
    Context context;

    public BeaconControl(Context context) {
        this.context = context;
    }

    public long addBeacon(Beacon beacon, DataBaseHandler dh) {
        long inserted = 0;
        SQLiteDatabase db = dh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataBaseHandler.BEACON_UUID, beacon.getBeaconUUID());
        values.put(DataBaseHandler.BEACON_MAJOR, beacon.getBeaconMajor());
        values.put(DataBaseHandler.BEACON_MINOR, beacon.getBeaconMinor());
        values.put(DataBaseHandler.BEACON_SEEN, beacon.getSeen());
        values.put(DataBaseHandler.BEACON_PLACE, beacon.getPlace());
        try {
            inserted = db.insertOrThrow(DataBaseHandler.BEACON, null, values);
        } catch (Exception e) {
            inserted = -1;
        }
        try {
            db.close();
        } catch (Exception e) {

        }
        db = null;
        values = null;
        return inserted;
    }

    public int updateBeacon(Beacon beacon, DataBaseHandler dh){
        SQLiteDatabase db = dh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataBaseHandler.BEACON_UUID, beacon.getBeaconUUID());
        values.put(DataBaseHandler.BEACON_MAJOR, beacon.getBeaconMajor());
        values.put(DataBaseHandler.BEACON_MINOR, beacon.getBeaconMinor());
        values.put(DataBaseHandler.BEACON_SEEN, beacon.getSeen());
        values.put(DataBaseHandler.BEACON_PLACE, beacon.getPlace());
        int count = db.update(DataBaseHandler.BEACON, values,
                DataBaseHandler.BEACON_PLACE + " = '" + beacon.getPlace() + "'", null);
        try{
            db.close();
        }catch(Exception e){
        }
        db = null;
        return count;
    }

    public Beacon getBeaconById(int beaconPlace, String strWhere,
                            String strOrderBy, DataBaseHandler dh) {
        Beacon beacon = null;
        String selectQuery = "Select "
                + DataBaseHandler.BEACON_UUID + ", "
                + DataBaseHandler.BEACON_MAJOR + ", "
                + DataBaseHandler.BEACON_MINOR + ", "
                + DataBaseHandler.BEACON_SEEN + ", "
                + DataBaseHandler.BEACON_PLACE
                + " FROM " + DataBaseHandler.BEACON
                + " WHERE " + DataBaseHandler.BEACON_PLACE
                + " = '" + beaconPlace + "'";

        if (strWhere != null) {
            selectQuery += " AND " + strWhere;
        }
        if (strOrderBy != null) {
            selectQuery += " ORDER BY " + strOrderBy;
        }
        SQLiteDatabase db = dh.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            beacon = new Beacon();
            beacon.setBeaconUUID(cursor.getString(0));
            beacon.setBeaconMajor(cursor.getString(1));
            beacon.setBeaconMinor(cursor.getString(2));
            beacon.setSeen(cursor.getInt(3));
            beacon.setPlace(cursor.getInt(4));
        }
        try {
            cursor.close();
            db.close();
        } catch (Exception e) {
        }
        db = null;
        cursor = null;
        return beacon;
    }

    public ArrayList<Beacon> getAllBeacons(DataBaseHandler dh){
        ArrayList<Beacon> beaconsList = new ArrayList<Beacon>();

        SQLiteDatabase db = dh.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from BEACON",null);

        if(cursor.moveToFirst()){
            do{
                Beacon beacon = new Beacon();
                beacon.setBeaconUUID(cursor.getString(0));
                beacon.setBeaconMajor(cursor.getString(1));
                beacon.setBeaconMinor(cursor.getString(2));
                beacon.setSeen(cursor.getInt(3));
                beacon.setPlace(cursor.getInt(4));
                beaconsList.add(beacon);
            }while(cursor.moveToNext());
        }

        try{
            cursor.close();
            db.close();
        }catch(Exception e){
        }
        cursor = null;
        db = null;

        return beaconsList;
    }

}
