package ls.main.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;


public class PathRecord implements Parcelable{
	private LatLng startpoint;
	private LatLng endpoint;
	private List<LatLng> pathline = new ArrayList<LatLng>();
	private String distance;
	private String duration;
	private String averagespeed;
	private String date;
	private int id = 0;
	
	public PathRecord() {
		
	}
		
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LatLng getStartpoint() {
		return startpoint;
	}
	public void setStartpoint(LatLng startpoint) {
		this.startpoint = startpoint;
	}
	public LatLng getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(LatLng endpoint) {
		this.endpoint = endpoint;
	}
	public List<LatLng> getPathline() {
		return pathline;
	}
	public void setPathline(List<LatLng> pathline) {
		this.pathline = pathline;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getAveragespeed() {
		return averagespeed;
	}
	public void setAveragespeed(String averagespeed) {
		this.averagespeed = averagespeed;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void addpoint(LatLng point){
		pathline.add(point);
	}

	@Override
	public String toString() {
		StringBuilder record = new StringBuilder();
		record.append("point:"+getPathline().size()+", ");
		record.append("distance:"+getDistance()+"m, ");
		record.append("duration:"+getDuration()+"s");
		return record.toString();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(startpoint);
		dest.writeValue(endpoint);
		dest.writeTypedList(pathline);
		dest.writeString(distance);
		dest.writeString(duration);
		dest.writeString(averagespeed);
		dest.writeString(date);
		
	}
	
	protected PathRecord(Parcel parcel) {
		startpoint = (LatLng)parcel.readValue(LatLng.class.getClassLoader());
		endpoint = (LatLng)parcel.readValue(LatLng.class.getClassLoader());
		pathline = parcel.createTypedArrayList(LatLng.CREATOR);
		distance = parcel.readString();
		duration = parcel.readString();
		averagespeed = parcel.readString();
		date = parcel.readString();
	}
	
	
	public static final Creator<PathRecord> CREATOR = new Creator<PathRecord>() {
		public PathRecord createFromParcel(Parcel parcel) {
			return new PathRecord(parcel);
		}

		public PathRecord[] newArray(int size) {
			return new PathRecord[size];
		}
	};
	
	
}
