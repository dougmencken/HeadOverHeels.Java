// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

import javax.sound.sampled.AudioFormat ;
import javax.sound.sampled.AudioInputStream ;
import javax.sound.sampled.AudioSystem ;
import javax.sound.sampled.Clip ;


class MusicPlaying implements Runnable
{
	/**
	 * the stream coming from the music file
	 */
	private AudioInputStream streamIn ;

	private boolean looping ;

	private boolean stopped = false ;

	MusicPlaying( String pathToMusic, boolean loopIt )
	{
		this.looping = loopIt ;
		this.streamIn = null ;

		java.io.File musicFile = new java.io.File( pathToMusic );
		if ( musicFile != null && musicFile.exists () ) {
			try {
				this.streamIn = AudioSystem.getAudioInputStream( musicFile );
			}
			  catch ( javax.sound.sampled.UnsupportedAudioFileException e ) {
				System.err.println( "audio file \"" + pathToMusic + "\" is not supported" );
			} catch ( java.io.IOException x ) {  x.printStackTrace ();  }
		}
	}

	MusicPlaying( String pathToMusic )
	{
		this( pathToMusic, false );
	}

	public void run ()
	{
		if ( this.streamIn == null ) return ;

		synchronized ( this.streamIn ) {
			// what to feed to the audio mixer
			AudioFormat pcmAudioFormat = new AudioFormat (
				this.streamIn.getFormat().getSampleRate(), 16, this.streamIn.getFormat().getChannels(), true, false
			);

			try (	// convert the audio input stream to the desired encoding
				AudioInputStream pcmAudioIn
					= AudioSystem.getAudioInputStream( pcmAudioFormat, /* source */ this.streamIn ) ;

				// get the line from the audio mixer for a preloaded clip
				Clip clip = AudioSystem.getClip () )
			{
				if ( clip != null ) {
					clip.open( pcmAudioIn );

					clip.setFramePosition( 0 ); // rewind
					/* clip.setLoopPoints( 0, clip.getFrameLength() - 1 ); */
					clip.loop( this.looping ? Clip.LOOP_CONTINUOUSLY : 0 );

					clip.start() ;

					final long period = clip.getMicrosecondLength () ;
					while ( ! this.stopped && ( this.looping || clip.getMicrosecondPosition() < period ) ) {
						try {
							Thread.sleep( 11 /* milliseconds */ );
						} catch ( InterruptedException e ) {  this.stopped = true ;  }
					}

					clip.stop () ;
				}
			} catch ( javax.sound.sampled.LineUnavailableException ex ) {
				System.err.println( "an audio mixer’s output line cannot be opened" );
			} catch ( java.io.IOException x ) {  x.printStackTrace ();  }
		}
	}

	void stopPlaying ()
	{
		this.stopped = true ;
		this.looping = false ;
	}

	void close()
	{
		synchronized ( this.streamIn ) {
			try {
				this.streamIn.close() ;
			} catch ( java.io.IOException ignored ) {}

			this.streamIn = null ;
		}
	}

}


public class SoundManager
{

	private static SoundManager instance = null ;

	public static SoundManager getInstance()
	{
		if ( instance == null ) instance = new SoundManager() ;
		return instance ;
	}

	/**
	 * the playlist as a mapping of the music file name (path) to the MusicPlaying object
	 */
	private java.util.Map < String, MusicPlaying > playlist = new java.util.HashMap < String, MusicPlaying > () ;

	public SoundManager () {}

	public void play( java.io.File musicFile, boolean loop )
	{
		this.play( musicFile.getAbsolutePath(), loop );
	}

	public void play( String pathToMusic, boolean loop )
	{
		MusicPlaying playMe = new MusicPlaying( pathToMusic, loop );
		this.playlist.put( pathToMusic, playMe );

		( new Thread( playMe ) ).start() ;
	}

	public void stop( java.io.File musicFile )
	{
		this.stop( musicFile.getAbsolutePath() );
	}

	public void stop( String path )
	{
		if ( this.playlist.containsKey( path ) ) {
			MusicPlaying playing = this.playlist.get( path );
			playing.stopPlaying ();
			playing.close() ;
			this.playlist.remove( path );
		}
	}

	public void stopAll ()
	{
		for ( String entry : this.playlist.keySet() ) {
			this.stop( entry );
		}
	}

}
