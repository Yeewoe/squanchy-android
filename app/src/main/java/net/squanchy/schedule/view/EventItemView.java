package net.squanchy.schedule.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import net.squanchy.R;
import net.squanchy.eventdetails.widget.ExperienceLevelIconView;
import net.squanchy.schedule.domain.view.Event;

public class EventItemView extends FrameLayout {

    private TextView titleView;
    private TextView placeView;
    private View placeContainer;
    private View trackView;
    private TextView speakersView;
    private View speakersContainer;
    private ExperienceLevelIconView experienceLevelIconView;

    public EventItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        titleView = (TextView) findViewById(R.id.txtTitle);
        placeView = (TextView) findViewById(R.id.txtPlace);
        placeContainer = findViewById(R.id.layout_place);
        trackView = findViewById(R.id.txtTrack);
        speakersView = (TextView) findViewById(R.id.txtSpeakers);
        speakersContainer = findViewById(R.id.layout_speakers);
        experienceLevelIconView = (ExperienceLevelIconView) findViewById(R.id.experience_level_icon);
    }

    void updateWith(Event event) {
        titleView.setText(event.title());

        placeView.setText(event.place());
        placeContainer.setVisibility(event.placeVisibility());

        trackView.setVisibility(event.trackVisibility());

        speakersView.setText(event.speakersNames());
        speakersContainer.setVisibility(event.speakersVisibility());

        experienceLevelIconView.setExperienceLevel(event.experienceLevel());
    }
}
