package org.example.ui.actions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.domain.RoomService;
import org.example.service.domain.SerializeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Slf4j
@Component("exportToJsonAction")
@RequiredArgsConstructor
public class ExportToJsonAction implements Action {

    private final SerializeService serializeService;
    private final RoomService roomService;
    @Value("${jsonExportPath}")
    private String path;

    @Override
    @Transactional
    public void execute() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

         try {
             String jsonString = objectMapper.writerWithDefaultPrettyPrinter()
                     .writeValueAsString(roomService.getRoomsEager());

             serializeService.exportToJson(jsonString, path);
         } catch (IOException e) {
             log.error("Error exporting to json: {}", e.getMessage(), e);
         }
    }
}
