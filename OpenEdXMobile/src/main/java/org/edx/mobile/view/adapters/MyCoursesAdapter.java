package org.edx.mobile.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.edx.mobile.core.IEdxEnvironment;
import org.edx.mobile.model.api.CourseEntry;
import org.edx.mobile.model.api.EnrolledCoursesResponse;
import org.edx.mobile.util.images.CourseCardUtils;
import org.edx.mobile.view.common.AppDatabase;
import org.edx.mobile.view.common.LocalCourseDao;

import es.dmoral.toasty.Toasty;


public abstract class MyCoursesAdapter extends BaseListAdapter<EnrolledCoursesResponse> {
    private long lastClickTime;
    SharedPreferences pref;
    String subdata;
    LocalCourseDao localCourseDao;


    public MyCoursesAdapter(Context context, IEdxEnvironment environment) {
        super(context, CourseCardViewHolder.LAYOUT, environment);
        lastClickTime = 0;

        AppDatabase db = Room.databaseBuilder(context,
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        localCourseDao = db.userDao();
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void render(BaseViewHolder tag, final EnrolledCoursesResponse enrollment) {
        final CourseCardViewHolder holder = (CourseCardViewHolder) tag;

        if (localCourseDao.getAll().size() > 0) {
            for (int x = 0; x < localCourseDao.getAll().size(); x++) {
                Log.e("data", localCourseDao.getAll().get(x).name.toString());

            }
        }


        final CourseEntry courseData = enrollment.getCourse();
        holder.setCourseTitle(courseData.getName());
        holder.setCourseImage(courseData.getCourse_image(environment.getConfig().getApiHostURL()));

        if (enrollment.getCourse().hasUpdates()) {
            holder.setHasUpdates(courseData, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAnnouncementClicked(enrollment);
                }
            });
        } else {
            holder.setDetails(CourseCardUtils.getFormattedDate(getContext(), enrollment));
        }
    }

    @Override
    public BaseViewHolder getTag(View convertView) {
        return new CourseCardViewHolder(convertView);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View arg1, int position,
                            long arg3) {
        //This time is checked to avoid taps in quick succession
        final long currentTime = SystemClock.elapsedRealtime();
        if (currentTime - lastClickTime > MIN_CLICK_INTERVAL) {
            lastClickTime = currentTime;
            EnrolledCoursesResponse model = (EnrolledCoursesResponse) adapterView.getItemAtPosition(position);
            if (model != null) {


                if (localCourseDao.findUserWithName(model.getCourse().getName()).size() > 0) {
                    for (int x = 0; x < localCourseDao.findUserWithName(model.getCourse().getName()).size(); x++) {
                        Log.e("data", localCourseDao.findUserWithName(model.getCourse().getName()).get(x).name.toString());

                    }
                }

                if (localCourseDao.findUserWithName(model.getCourse().getName()).size() > 0) {
                    onItemClicked(model);

                } else {
                    Toasty.info(getContext(), "You have not subscribed to this course.\nGo to settings and subscribe to access the course.", Toast.LENGTH_LONG, true).show();


                }


            }
        }


    }

    public abstract void onItemClicked(EnrolledCoursesResponse model);

    public abstract void onAnnouncementClicked(EnrolledCoursesResponse model);
}
