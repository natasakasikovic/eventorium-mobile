package com.eventorium.data.solution.mappers;

import static java.util.stream.Collectors.toList;

import com.eventorium.data.event.models.EventType;
import com.eventorium.data.solution.dtos.UpdateServiceRequestDto;
import com.eventorium.data.solution.models.Service;
import com.eventorium.data.solution.models.ServiceSummary;

public class ServiceMapper {

    public static ServiceSummary fromService(Service service) {
        return ServiceSummary.builder()
                .id(service.getId())
                .name(service.getName())
                .price(service.getPrice())
                .build();
    }
}
