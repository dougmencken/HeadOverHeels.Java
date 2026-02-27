// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

import javax.sound.sampled.AudioFormat ;
import javax.sound.sampled.AudioInputStream ;
import javax.sound.sampled.AudioSystem ;
import javax.sound.sampled.Clip ;

import javax.sound.midi.Instrument ;
import javax.sound.midi.MidiChannel ;
import javax.sound.midi.Synthesizer ;


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
				System.err.println( "audio file " + StringUtilities.putInQuotes( pathToMusic ) + " is not supported" );
			} catch ( java.io.IOException x ) {  x.printStackTrace ();  }
		}
	}

	MusicPlaying( String pathToMusic )
	{
		this( pathToMusic, false );
	}

	public void run ()
	{
		// nothing here for Java 1.5
	}

	void stopPlaying ()
	{
		this.stopped = true ;
		this.looping = false ;
	}

	void close()
	{
		if ( this.streamIn == null ) return ;

		synchronized ( this.streamIn ) {
			try {
				this.streamIn.close() ;
			} catch ( java.io.IOException ignored ) {}
		}

		this.streamIn = null ;
	}

}


public class SoundManager
{

	private static SoundManager instance = null ;

	public static SoundManager getInstance()
	{
		if ( SoundManager.instance == null ) SoundManager.instance = new SoundManager() ;
		return SoundManager.instance ;
	}

	/**
	 * the playlist as a mapping of the music file name (path) to the MusicPlaying object
	 */
	private final java.util.Map < String, MusicPlaying > playlist ;

	public SoundManager ()
	{
		this.playlist = new java.util.HashMap < String, MusicPlaying > () ;
	}

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
		for ( String entry : this.playlist.keySet() )
			this.stop( entry );
	}

	/**
	 * list all the MIDI instruments
	 */
	public static void listMidiInstruments ()
	{
		Synthesizer midiSynthesizer = null ;
		try {
			midiSynthesizer = javax.sound.midi.MidiSystem.getSynthesizer() ;
			midiSynthesizer.open ();
		} catch ( javax.sound.midi.MidiUnavailableException ex ) { midiSynthesizer = null ; }

		if ( midiSynthesizer != null ) {
			Instrument [] instruments = midiSynthesizer.getAvailableInstruments() ;

			for ( int i = 0 ; i < instruments.length ; ++ i )
				System.out.println( StringUtilities.toStringWithOrdinalSuffix( i )
							+ " MIDI instrument is " + StringUtilities.putInQuotes( instruments[ i ].getName() ) );

			midiSynthesizer.close ();
		}
		else
			System.out.println( "can’t get MIDI synthesizer" );
	}

	public static Instrument loadMidiInstrumentByName ( Synthesizer midiSynth, String partOfName )
	{
		return loadMidiInstrumentByName( midiSynth, partOfName, "" );
	}

	public static Instrument loadMidiInstrumentByName ( Synthesizer midiSynth, String partOfName, String otherPartOfName )
	{
		if ( midiSynth == null ) return null ;

		Instrument [] instruments = midiSynth.getAvailableInstruments () ;

		for ( int i = 0 ; i < instruments.length ; ++ i )
			if ( instruments[ i ].getName().toLowerCase().contains( partOfName.toLowerCase() )
					&& instruments[ i ].getName().toLowerCase().contains( otherPartOfName.toLowerCase() ) )
				// load and return if found
				if ( midiSynth.loadInstrument( instruments[ i ] ) ) return instruments[ i ] ;

		return null ;
	}

	public static void main( String [] ignored )
	{
		listMidiInstruments ();

		Synthesizer midiSynthesizer = null ;
		try {
			midiSynthesizer = javax.sound.midi.MidiSystem.getSynthesizer() ;
			midiSynthesizer.open ();
		} catch ( javax.sound.midi.MidiUnavailableException ex ) { midiSynthesizer = null ; }

		if ( midiSynthesizer == null ) return ;

		MidiChannel [] voices = midiSynthesizer.getChannels () ;
		int first = 0 ;
		while ( first < voices.length && voices[ first ] == null ) ++ first ;
		int second = first + 1 ;
		while ( second < voices.length && voices[ second ] == null ) ++ second ;

		System.out.println( "got MIDI channels #" + first + " and #" + second );

		MidiChannel voiceOne = voices[ first ] ;
		MidiChannel voiceToo = voices[ second ] ;

		Instrument squareLead = loadMidiInstrumentByName( midiSynthesizer, "square", "lead" );
		Instrument vibraphone = loadMidiInstrumentByName( midiSynthesizer, "vibraphone" );

		System.out.println( "the square lead instrument is known as "
					+ StringUtilities.putInQuotes( squareLead != null ? squareLead.getName() : "null" ) );
		System.out.println( "the vibraphone instrument  is known as "
					+ StringUtilities.putInQuotes( vibraphone != null ? vibraphone.getName() : "null" ) );

		voiceOne.programChange( squareLead.getPatch().getProgram() );
		voiceToo.programChange( vibraphone.getPatch().getProgram() );

		java.util.Random random = new java.util.Random () ;

		for ( int n = 0 ; n < 40 ; ++ n ) {
			int note = 60 + random.nextInt( 25 );

			voiceOne.noteOn( /* note pitch from 0 to 127, 60 = middle C */ note, /* velocity */ 100 );
			try { Thread.sleep( 100 /* milliseconds */ ); } catch( InterruptedException ie ) {}
			voiceToo.noteOn( note, 80 );
			try { Thread.sleep( 50 /* milliseconds */ ); } catch( InterruptedException ie ) {}
			voiceToo.noteOff( note );
			try { Thread.sleep( 50 /* milliseconds */ ); } catch( InterruptedException ie ) {}
			voiceToo.noteOn( note, 80 );
			try { Thread.sleep( 50 /* milliseconds */ ); } catch( InterruptedException ie ) {}
			voiceToo.noteOff( note );
			voiceOne.noteOff( note );
		}

		midiSynthesizer.unloadInstrument( squareLead );
		midiSynthesizer.unloadInstrument( vibraphone );
		midiSynthesizer.close ();
	}

}
