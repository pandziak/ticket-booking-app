package pl.pandziak.ticketbookingapp.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import pl.pandziak.ticketbookingapp.domain.screening.ScreeningFacade;
import pl.pandziak.ticketbookingapp.domain.screening.dtos.ScreeningRequestDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Service providing testing data to in-memory database.
 */
@Configuration
class DataProvider {

    private final Logger logger = LoggerFactory.getLogger(DataProvider.class);

    private static final String CSV_FILE_SEPARATOR = ";";
    private static final String DATA_FILE_NAME = "data.csv";
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);

    private final ScreeningFacade screeningFacade;

    DataProvider(ScreeningFacade screeningFacade) {
        this.screeningFacade = screeningFacade;
    }

    @EventListener
    public void populateInMemoryDatabase(ContextRefreshedEvent ctx) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(DATA_FILE_NAME);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = reader.readLine()) != null) {
                addRecord(line);
            }
        } catch (NullPointerException e) {
            logger.error("Cannot find file '{}' in resource folder. " +
                    "Please check it again and restart the application.", DATA_FILE_NAME);
        } catch (IndexOutOfBoundsException e) {
            logger.error("Error during parsing file from resource folder. " +
                    "Please make sure data in file '{}' is valid and restart the application. " +
                    "Valid data line: >> movieTitle;date;room <<", DATA_FILE_NAME);
        } catch (IOException e) {
            logger.error("Error during reading file from resource folder. " +
                    "Please make sure there is a '{}' file with valid data " +
                    "and restart the application. Valid data line: >> movieTitle;date;room <<", DATA_FILE_NAME);
        }
    }

    private void addRecord(String line) {
        String[] splittedLine = line.split(CSV_FILE_SEPARATOR);
        ScreeningRequestDto screeningRequest = new ScreeningRequestDto(
                splittedLine[0],
                LocalDateTime.parse(splittedLine[1], formatter),
                splittedLine[2]);
        screeningFacade.createScreening(screeningRequest);
    }

}
