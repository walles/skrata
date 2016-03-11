package com.gmail.walles.johan.skrata;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.LinkedList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements TextWatcher {
    private final static String TAG = "Skrata";

    private CharSequence beforeChange;

    private TextToSpeech textToSpeech;

    public MainActivityFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        textToSpeech = new TextToSpeech(activity, null);
    }

    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.shutdown();
            textToSpeech = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        EditText editText = (EditText)view.findViewById(R.id.edittext);
        editText.addTextChangedListener(this);

        return view;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        beforeChange = s.subSequence(start, start + count);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.i(TAG, "s=<" + s + "> start=" + start + " beforeChange=" + before + " count=" + count);

        if (s.length() == 0) {
            // No text, never mind
            return;
        }

        CharSequence afterChange = s.subSequence(start, start + count);
        if (TextUtils.equals(beforeChange, afterChange)) {
            // Nothing changed, never mind
            return;
        }

        List<CharSequence> toSpeak = new LinkedList<>();
        if (count == before + 1) {
            // A character was added, just assume it was last
            CharSequence lastChar = s.subSequence(start + count - 1, start + count);
            toSpeak.add(lastChar);
        }

        // FIXME: fullUpdate should really be current word
        CharSequence fullUpdate = s.subSequence(start, start + count);

        toSpeak.add(fullUpdate);
        toSpeak.add(s);
        speak(toSpeak);
    }

    private void speak(Iterable<CharSequence> toSpeak) {
        CharSequence lastPhrase = null;
        int queueMode = TextToSpeech.QUEUE_FLUSH;
        for (CharSequence phrase: toSpeak) {
            if (TextUtils.equals(lastPhrase, phrase)) {
                // We shouldn't repeat ourselves
                continue;
            }

            if (TextUtils.isEmpty(phrase)) {
                // Nothing to see here, never mind
                continue;
            }

            // FIXME: Add error handling
            Log.i(TAG, "Speaking: <" + phrase + ">");
            textToSpeech.speak(phrase, queueMode, null, null);

            lastPhrase = phrase;
            queueMode = TextToSpeech.QUEUE_ADD;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        // This method intentionally left blank
    }
}
