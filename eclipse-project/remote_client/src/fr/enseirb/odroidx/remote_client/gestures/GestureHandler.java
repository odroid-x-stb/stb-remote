package fr.enseirb.odroidx.remote_client.gestures;

public interface GestureHandler {

	public boolean slidingLeft();
	public boolean slidingRight();
	public boolean slidingUp();
	public boolean slidingDown();
	public boolean singleTap();
	public boolean doubleTap();
	
}
