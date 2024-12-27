//package com.example.project;
//
//import android.content.Intent;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.speech.RecognizerIntent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ListView;
//
//import androidx.fragment.app.Fragment;
//
//public class VoiceFragment extends Fragment {
//    EditText myText;
//    Cursor matchedEmployees;
//    int voiceCode = 1;
//    ListView myList;
//    EmployeeHelperClass myHelper;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_voice, container, false);
//
//        myHelper = new EmployeeHelperClass(getActivity());
//        myList = (ListView) rootView.findViewById(R.id.listView1);
//        myText = (EditText) rootView.findViewById(R.id.myText);
//        ImageButton voiceBtn = (ImageButton) rootView.findViewById(R.id.voiceBtn);
//
//        return rootView;
//
//        voiceBtn.setOnClickListener(v -> {
//            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//            startActivityForResult(intent, voiceCode);
//        });
//
//    }
//}
