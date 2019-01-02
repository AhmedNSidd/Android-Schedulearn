package com.schedulearn.schedulearn;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class AgendaFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_agenda, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.agenda_recycler_view);
        LessonsAdapter agendaAdapter = new LessonsAdapter(getContext(), MainActivity.mLessonsPictures, MainActivity.mLessonsNames, MainActivity.mLessonsLocations, MainActivity.mLessonsDates);
        recyclerView.setAdapter(agendaAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return v;
    }

    public class LessonsAdapter extends RecyclerView.Adapter<LessonsAdapter.LessonsViewHolder> {

        private Context mContext;
        private ArrayList<String> mLessonsPictures;
        private ArrayList<String> mLessonsNames;
        private ArrayList<String> mLessonsLocations;
        private ArrayList<Date> mLessonsDates;

        public class LessonsViewHolder extends RecyclerView.ViewHolder {

            private CircularImageView lessonPicture;
            private TextView lessonName;
            private TextView lessonLocation;
            private TextView lessonDate;
            private RelativeLayout listItemLayout;

            public LessonsViewHolder(@NonNull View itemView) {
                super(itemView);
                lessonPicture = itemView.findViewById(R.id.agenda_lesson_picture);
                lessonName = itemView.findViewById(R.id.agenda_lesson_name);
                lessonLocation = itemView.findViewById(R.id.agenda_lesson_location);
                lessonDate = itemView.findViewById(R.id.agenda_lesson_date);
                listItemLayout = itemView.findViewById(R.id.agenda_list_item);
            }
        }

        public LessonsAdapter(Context context, ArrayList<String> lessonsPictures,
                                   ArrayList<String> lessonsNames,
                                   ArrayList<String> lessonsLocations,
                                   ArrayList<Date> lessonsDates) {
            mContext = context;
            mLessonsPictures = lessonsPictures;
            mLessonsNames = lessonsNames;
            mLessonsLocations = lessonsLocations;
            mLessonsDates = lessonsDates;
        }

        @NonNull
        @Override
        public LessonsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                                        R.layout.list_item_agenda, viewGroup, false);
            return new LessonsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull LessonsViewHolder lessonsViewHolder, final int i) {
            Glide.with(mContext)
                    .asBitmap()
                    .load(mLessonsPictures.get(i))
                    .into(lessonsViewHolder.lessonPicture);
            Date startTimeDate = mLessonsDates.get(i);
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM'.' d 'at' h:mmza 'GMT'");
            String startTimeString = dateFormat.format(startTimeDate);
            lessonsViewHolder.lessonDate.setText(startTimeString);
            lessonsViewHolder.lessonName.setText(mLessonsNames.get(i));
            lessonsViewHolder.lessonLocation.setText(mLessonsLocations.get(i));

            lessonsViewHolder.listItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), mLessonsNames.get(i), Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mLessonsNames.size();
        }



    }
}