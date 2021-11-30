package space.nasa.spaceapi.controllers;

import space.nasa.spaceapi.models.APOD;

/**
 * Used to initialize an {@link APOD} object needed for a controller class
 */
public interface InitializableAPOD{
	/**
	 * Should be called when at the begging of controller class instantiation to set up a needed APOD object within the
	 * class
	 * @param apod the desired {@code APOD} to send to the controller
	 */
	void initializeAPOD(APOD apod);
}
