package com.eventorium.presentation.calendar.utils;

import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class EventDecorator implements DayViewDecorator {
    private final int foregroundColor;
    private final int backgroundColor;
    private final List<CalendarDay> dates;

    public EventDecorator(int foregroundColor, int backgroundColor, CalendarDay date) {
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.dates = Collections.singletonList(date);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(foregroundColor));
        setCircleBackground(view, backgroundColor);
    }

    public void setCircleBackground(DayViewFacade view, int color) {
        ShapeDrawable circleDrawable = new ShapeDrawable(new OvalShape());
        circleDrawable.getPaint().setColor(color);
        circleDrawable.getPaint().setStyle(Paint.Style.FILL);
        view.setBackgroundDrawable(circleDrawable);
    }


}
