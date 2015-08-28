package com.alphaws.javadaytrivia.beans;

import com.alphaws.javadaytrivia.tools.Commons;

public class Beacon {

	private int place, seen;
	private String beaconUUID, beaconMajor, beaconMinor;

	public Beacon() {
		beaconUUID = Commons.BEACONS_UUID_AWS;
		beaconMajor = "";
		beaconMinor = "";
	}

	public Beacon(String beaconMajor, String beaconMinor) {
		beaconUUID = Commons.BEACONS_UUID_AWS;
		this.beaconMajor = beaconMajor;
		this.beaconMinor = beaconMinor;
	}

	public Beacon(String beaconMajor, String beaconMinor, int place) {
		beaconUUID = Commons.BEACONS_UUID_AWS;
		this.beaconMajor = beaconMajor;
		this.beaconMinor = beaconMinor;
		this.place = place;
	}

	public int getPlace() {
		return place;
	}

	public void setPlace(int place) {
		this.place = place;
	}

	public String getBeaconMajor() {
		return beaconMajor;
	}

	public void setBeaconMajor(String beaconMajor) {
		this.beaconMajor = beaconMajor;
	}

	public String getBeaconMinor() {
		return beaconMinor;
	}

	public void setBeaconMinor(String beaconMinor) {
		this.beaconMinor = beaconMinor;
	}

	public int getSeen() {
		return seen;
	}

	public void setSeen(int seen) {
		this.seen = seen;
	}

	public String getBeaconUUID() {
		return beaconUUID;
	}

	public void setBeaconUUID(String beaconUUID) {
		this.beaconUUID = beaconUUID;
	}

	@Override
	public String toString() {
		return "Beacon{" +
				"place=" + place +
				", seen=" + seen +
				", beaconMajor='" + beaconMajor + '\'' +
				", beaconMinor='" + beaconMinor + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Beacon beacon = (Beacon) o;

		if (!getBeaconMajor().equals(beacon.getBeaconMajor())) return false;
		return getBeaconMinor().equals(beacon.getBeaconMinor());

	}

}
