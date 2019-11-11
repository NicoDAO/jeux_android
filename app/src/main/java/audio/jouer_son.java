package audio;

import android.media.AudioManager;
import java.util.HashMap;
import android.media.SoundPool;
import android.content.Context;

public class jouer_son {

	private  SoundPool mSoundPool;
	//private  HashMap mSoundPoolMap;
	private HashMap<Integer, Integer> mSoundPoolMap;
	private  AudioManager  mAudioManager;
	private  Context mContext;
        
	public void initSounds(Context theContext) {
	    mContext = theContext;
	    mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
	    mSoundPoolMap = new HashMap();
	    mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
	}
	public void addSound(int index, int SoundID)
	{
	    mSoundPoolMap.put(index, mSoundPool.load(mContext, SoundID, 1));
	}
	public void playSound(int index)
	{
	float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	   // mSoundPool.play( mSoundPoolMap.get(index), streamVolume, streamVolume, 1, 0, 1f);
		mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume,
				1, 0, 1f);
		// mSoundPool.play(soundID, leftVolume, rightVolume, priority, loop, rate);
	    
	    
	}
	 
	public void playLoopedSound(int index)
	{
	    float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	    streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	    mSoundPool.play( mSoundPoolMap.get(index), streamVolume, streamVolume, 1, -1, 1f);
	}
}