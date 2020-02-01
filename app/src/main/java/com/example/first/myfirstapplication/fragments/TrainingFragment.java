package com.example.first.myfirstapplication.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.first.myfirstapplication.AddText;
import com.example.first.myfirstapplication.R;
import com.example.first.myfirstapplication.marf.SpeakerIdentApp;
import com.example.first.myfirstapplication.SoundRecorder.JavaSoundRecorder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TrainingFragment extends Fragment {

    View v;
    Button btn_train1, btn_train2,btn_train3,btn_train4;
    TextView tv_train_state;
    static int count;

    public static TrainingFragment newInstance() {
        TrainingFragment fragment = new TrainingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_training, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        v = getView();
        final String working_path = getActivity().getExternalFilesDir("training").getPath();
        final String path = getActivity().getExternalFilesDir("").getPath() + File.separator + "speakers.txt";
        final JavaSoundRecorder recorder = new JavaSoundRecorder();

        btn_train1 = v.findViewById(R.id.btn_train1);
        btn_train2 = v.findViewById(R.id.btn_train2);
        btn_train3 = v.findViewById(R.id.btn_train3);
        btn_train4 = v.findViewById(R.id.btn_train4);
        count=1;

        tv_train_state = v.findViewById(R.id.textView_train_state);


// Code for training from a folder
//        View.OnClickListener ftr = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                File file = new File(getActivity().getExternalFilesDir("training-simples").getPath());
//                final String[] MARF_ARGS = {"--train", "-silence", "-bandstop", "-aggr", "-cos", "-debug",getActivity().getExternalFilesDir("training-simples").getPath()+File.separator};
//                SpeakerIdentApp.main(MARF_ARGS);
//            }
//        };

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String file_name = new SimpleDateFormat("yyyyMMddHHmmss'.wav'").format(new Date());
                final String[] MARF_ARGS = {"--single-train", "-aggr" ,"-raw", "-eucl", working_path + File.separator + file_name};
                recorder.setFilepath(working_path);
                recorder.setRecordNameFile(file_name);
                //Set the length of the recognizing sample in milliseconds
                recorder.setRECORDDING_TIME(9000);
                recorder.startRecord();
                switch (v.getId()) {
                    case R.id.btn_train1://training The first person
                        Log.i("Button 1", "start");
                        tv_train_state.setText("Training P"+count +"start");
                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder.setTitle("Recording");
                        alertDialogBuilder.setMessage("Please talk while recording \n 00:10");
                        alertDialogBuilder.setPositiveButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        recorder.stopRecord();
                                        arg0.dismiss();
                                    }
                                });
                        final AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        new CountDownTimer(10000,1000 ){
                            @Override
                            public void onTick(long millisUntilFinished) {
                                if(alertDialog.isShowing())
                                    if(millisUntilFinished/1000 != 1)
                                        alertDialog.setMessage("Please talk while recording \n 00:0" + (millisUntilFinished/1000));
                                    else
                                        alertDialog.setMessage("Training");
                                else
                                    cancel();
                            }

                            @Override
                            public void onFinish() {
                                AddText addText = new AddText(path, 19, 10+count, file_name);
                                SpeakerIdentApp.main(MARF_ARGS);
                                alertDialog.dismiss();
                                tv_train_state.setText("Done training");
                                if(count<9) count++;

                            }
                        }.start();
                        break;
             /*       case R.id.btn_train2://Training the second person
                        Log.i("Button 2", "start");
                        tv_train_state.setText("Training P2 start");
                        final AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder2.setTitle("Recording");
                        alertDialogBuilder2.setMessage("Please talk while recording \n 00:10");
                        alertDialogBuilder2.setPositiveButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        recorder.stopRecord();
                                        arg0.dismiss();
                                    }
                                });
                        final AlertDialog alertDialog2 = alertDialogBuilder2.create();
                        alertDialog2.show();
                        new CountDownTimer(10000,1000 ){
                            @Override
                            public void onTick(long millisUntilFinished) {
                                if(alertDialog2.isShowing())
                                    if(millisUntilFinished/1000 != 1)
                                        alertDialog2.setMessage("Please talk while recording \n 00:0" + (millisUntilFinished/1000));
                                    else
                                        alertDialog2.setMessage("Training");
                                else
                                    cancel();
                            }

                            @Override
                            public void onFinish() {
                                AddText addText = new AddText(path, 15, 12, file_name);
                                SpeakerIdentApp.main(MARF_ARGS);
                                alertDialog2.dismiss();
                                tv_train_state.setText("Done training");
                            }
                        }.start();
                        break;
                    case R.id.btn_train3://training The third person
                        Log.i("Button 1", "start");
                        tv_train_state.setText("Training P3 start");
                        final AlertDialog.Builder alertDialogBuilder3 = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder3.setTitle("Recording");
                        alertDialogBuilder3.setMessage("Please talk while recording \n 00:10");
                        alertDialogBuilder3.setPositiveButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        recorder.stopRecord();
                                        arg0.dismiss();
                                    }
                                });
                        final AlertDialog alertDialog3 = alertDialogBuilder3.create();
                        alertDialog3.show();
                        new CountDownTimer(10000,1000 ){
                            @Override
                            public void onTick(long millisUntilFinished) {
                                if(alertDialog3.isShowing())
                                    if(millisUntilFinished/1000 != 1)
                                        alertDialog3.setMessage("Please talk while recording \n 00:0" + (millisUntilFinished/1000));
                                    else
                                        alertDialog3.setMessage("Training");
                                else
                                    cancel();
                            }

                            @Override
                            public void onFinish() {
                                AddText addText = new AddText(path, 15, 13, file_name);
                                SpeakerIdentApp.main(MARF_ARGS);
                                alertDialog3.dismiss();
                                tv_train_state.setText("Done training");
                            }
                        }.start();
                        break;
                    case R.id.btn_train4://training The third person
                        Log.i("Button 1", "start");
                        tv_train_state.setText("Training P4 start");
                        final AlertDialog.Builder alertDialogBuilder4 = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder4.setTitle("Recording");
                        alertDialogBuilder4.setMessage("Please talk while recording \n 00:10");
                        alertDialogBuilder4.setPositiveButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        recorder.stopRecord();
                                        arg0.dismiss();
                                    }
                                });
                        final AlertDialog alertDialog4 = alertDialogBuilder4.create();
                        alertDialog4.show();
                        new CountDownTimer(10000,1000 ){
                            @Override
                            public void onTick(long millisUntilFinished) {
                                if(alertDialog4.isShowing())
                                    if(millisUntilFinished/1000 != 1)
                                        alertDialog4.setMessage("Please talk while recording \n 00:0" + (millisUntilFinished/1000));
                                    else
                                        alertDialog4.setMessage("Training");
                                else
                                    cancel();
                            }

                            @Override
                            public void onFinish() {
                                AddText addText = new AddText(path, 15, 14, file_name);
                                SpeakerIdentApp.main(MARF_ARGS);
                                alertDialog4.dismiss();
                                tv_train_state.setText("Done training");
                            }
                        }.start();
                        break;*/
                }
            }
        };

        btn_train1.setOnClickListener(onClickListener);
        btn_train2.setOnClickListener(onClickListener);
        btn_train3.setOnClickListener(onClickListener);
        btn_train4.setOnClickListener(onClickListener);

    }


}