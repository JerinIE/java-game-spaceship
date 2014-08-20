package spaceship;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public final class Helper {
	
	/**
	 * Opens a file from URL, reads all of its bytes into
	 * an array and returns the created byte array.
	 * @param fileUrl
	 * @return Byte Array
	 */
	public static final byte[] fileToByteArray(URL fileUrl) {
		try {
			// Create instances to start reading
			File file = new File(fileUrl.toURI());
			FileInputStream fileInputStream = new FileInputStream(new File(fileUrl.toURI()));
			
			// Create a temporary byte array
			byte[] tmpByteArray = new byte[(int)file.length()];
			
			// Read file into temporary byte array
			fileInputStream.read(tmpByteArray);
			fileInputStream.close();
			
			// Return the temporary byte arrays
			return tmpByteArray;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Plays a sound from a byte array. Made so we don't need to
	 * constantly access the file system.
	 * @param soundBytes Byte Array of sound file
	 */
	public static final void playSoundFromByteArray(byte[] soundBytes) {
		try {
			// Open an audio input stream from the soundBytes array
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(soundBytes));
			
			// Create a sound clip from the audio input stream, and play the sound
			Clip clip = AudioSystem.getClip();
			clip.open(inputStream);
			inputStream.close();
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
