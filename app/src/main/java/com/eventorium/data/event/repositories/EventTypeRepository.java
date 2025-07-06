package com.eventorium.data.event.repositories;

import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.handleGeneralResponse;
import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.handleGetImage;
import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.handleSuccessAsBoolean;
import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.handleSuccessfulResponse;
import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.handleVoidResponse;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.event.models.eventtype.CreateEventType;
import com.eventorium.data.event.models.eventtype.EventType;
import com.eventorium.data.event.services.EventTypeService;
import com.eventorium.data.shared.models.Result;
import com.eventorium.data.shared.utils.FileUtil;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import okhttp3.MultipartBody;

public class EventTypeRepository {

    private final EventTypeService eventTypeService;

    @Inject
    public EventTypeRepository(EventTypeService eventTypeService) {
        this.eventTypeService = eventTypeService;
    }

    public LiveData<Result<EventType>> createEventType(CreateEventType eventType) {
        MutableLiveData<Result<EventType>> liveData = new MutableLiveData<>();
        eventTypeService.createEventType(eventType).enqueue(handleGeneralResponse(liveData));
        return liveData;
    }

    public LiveData<List<EventType>> getEventTypes() {
        MutableLiveData<List<EventType>> liveData = new MutableLiveData<>();
        eventTypeService.getEventTypes().enqueue(handleSuccessfulResponse(liveData));
        return liveData;
    }

    public LiveData<Result<Void>> updateEventType(EventType eventType) {
        MutableLiveData<Result<Void>> liveData = new MutableLiveData<>();
        eventTypeService.updateEventType(eventType.getId(), eventType).enqueue(handleVoidResponse(liveData));
        return liveData;
    }

    public LiveData<Result<Void>> deleteEventType(Long id) {
        MutableLiveData<Result<Void>> liveData = new MutableLiveData<>();
        eventTypeService.delete(id).enqueue(handleVoidResponse(liveData));
        return liveData;
    }

    public LiveData<Boolean> uploadImage(Long id, Context context, Uri uri) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        MultipartBody.Part part;

        try {
            part = FileUtil.getImageFromUri(context, uri, "image");
        } catch (IOException e) {
            result.setValue(false);
            return result;
        }
        eventTypeService.uploadImage(id, part).enqueue(handleSuccessAsBoolean(result));
        return result;
    }

    public LiveData<Bitmap> getImage(Long id) {
        MutableLiveData<Bitmap> result = new MutableLiveData<>();
        eventTypeService.getImage(id).enqueue(handleGetImage(result));
        return result;
    }

    public LiveData<Boolean> updateImage(Long id, Context context, Uri uri) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        MultipartBody.Part part;

        try {
            part = FileUtil.getImageFromUri(context, uri, "image");
        } catch (IOException e) {
            result.setValue(false);
            return result;
        }

        eventTypeService.updateImage(id, part).enqueue(handleSuccessAsBoolean(result));
        return result;
    }
}
