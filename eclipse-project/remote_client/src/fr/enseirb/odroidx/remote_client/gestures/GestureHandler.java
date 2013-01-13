package fr.enseirb.odroidx.remote_client.gestures;

public interface GestureHandler {

	public boolean slidingLeft();
	public boolean slidingRight();
	public boolean slidingTop();
	public boolean slidingBottom();
	public boolean touchSingle();
	public boolean touchDouble();
	
}
