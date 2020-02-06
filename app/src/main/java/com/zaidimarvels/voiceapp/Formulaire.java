package com.zaidimarvels.voiceapp;


import android.content.Intent;

import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import java.util.List;
import java.util.Locale;



public class Formulaire extends AppCompatActivity {
    private TextToSpeech tts;
    private SpeechRecognizer speechRecog;
    private TextView t1;
    private EditText text1,text2,text3,text4;
    List<String> resultss;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    boolean modify = false;
    boolean lastname = false;
    boolean firstname = false;
    boolean password = false;
    boolean adress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_formulaire2 );
        t1 =findViewById( R.id.textView );

        resultss = new ArrayList<>();

        text1=findViewById( R.id.editText );
        text2=findViewById( R.id.editText2 );
        text3=findViewById( R.id.editText3 );
        text4=findViewById( R.id.editText4 );

        initializeSpeechRecognizer();
        initializeTextToSpeech();


    }

    private void initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable( this )) {
            speechRecog = SpeechRecognizer.createSpeechRecognizer( this );
            speechRecog.setRecognitionListener( new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle results) {
                    List<String> result_arr = results.getStringArrayList( SpeechRecognizer.RESULTS_RECOGNITION );
                    processResult( result_arr.get( 0 ));
                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            } );
        }
    }

  private void initializeTextToSpeech() {
        tts = new TextToSpeech( this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (tts.getEngines().size() == 0) {
                    Toast.makeText( Formulaire.this, getString( R.string.tts_no_engines ), Toast.LENGTH_LONG ).show();
                    finish();
                } else {
                    tts.setLanguage( Locale.US );
                    speak( "Complete the name" );




                }




            }
        } );

    }

    private void speak(String message) {
        if (Build.VERSION.SDK_INT >= 21) {
            tts.speak( message, TextToSpeech.QUEUE_FLUSH, null, null );
        } else {
            tts.speak( message, TextToSpeech.QUEUE_FLUSH, null );
        }
        Intent intent = new Intent( RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
        speechRecog.startListening(intent);
    }


    private void processResult(String result_message ) {


      resultss.add( result_message );
    result_message = result_message.toLowerCase();

    if (resultss.size() == 1){

        text1.setText( resultss.get( 0 ) );
        speak( "complete your password" );
    }else if (resultss.size() == 2){
        text2.setText( resultss.get( 1 ) );
        speak( "complete the last name" );

    }else if (resultss.size() == 3){
        text3.setText( resultss.get( 2 ) );
        speak( "complete your adress" );

    } else if (resultss.size() >= 4) {
        text4.setText( resultss.get( 3 ) );
        speak( "are you sure to send your information" );

        if (result_message.indexOf("welcome") != -1) {
            speak( "Opening class" );
            Intent intent = new Intent( Formulaire.this, MainActivity.class );
            startActivity( intent );

        } else if (result_message.indexOf("android") != -1) {
            speak( "what do you want to change" );
            modify = true;

        }else if(modify){
            if (result_message.indexOf( "last name" )!= -1){
                speak( "ok enter the last name" );
                lastname = true;
            }else if (lastname){
                int nb = resultss.size();
                text3.setText( resultss.get( nb - 1)) ;
                resultss.set( 2, resultss.get( nb - 1) );
                lastname = false;
            }

            if (result_message.indexOf( "first name" )!= -1){
                speak( "ok enter the first name" );
                firstname = true;
            }else if (firstname){
                int nb = resultss.size();
                text1.setText( resultss.get( nb - 1)) ;
                resultss.set( 0, resultss.get( nb - 1) );
                firstname = false;
            }
            if (result_message.indexOf( "password" )!= -1){
                speak( "ok enter the password" );
                password = true;
            }else if (password){
                int nb = resultss.size();
                text2.setText( resultss.get( nb - 1)) ;
                resultss.set( 1, resultss.get( nb - 1) );
                password = false;

            }
            if (result_message.indexOf( "adress" )!= -1){
                speak( "ok enter your adress" );
                adress = true;
            }else if (adress){
                int nb = resultss.size();
                text4.setText( resultss.get( nb - 1)) ;
                resultss.set( 3, resultss.get( nb - 1) );
                adress = false;
            }
            else speak( "i don't undrestand" );
        }



        }

    }




    @Override
    protected void onResume() {
        super.onResume();
//        Reinitialize the recognizer and tts engines upon resuming from background such as after openning the browser
        initializeSpeechRecognizer();
        initializeTextToSpeech();
    }


}



