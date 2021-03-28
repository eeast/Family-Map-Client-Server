package TestService;

import Service.LoadService;
import request.LoadRequest;
import result.LoadResult;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class LoadServiceTest {
    private LoadRequest goodRequest;
    private LoadRequest badRequest;

    @BeforeEach
    void setUp() {
        try {
            Gson gson = new Gson();
            BufferedReader br = Files.newBufferedReader(Paths.get("src/test/json/LoadData.json"));
            goodRequest = gson.fromJson(br, LoadRequest.class);
            br.close();
        } catch (Exception e) {
            System.out.println("ERROR loading LOAD file");
        }
        try {
            Gson gson = new Gson();
            BufferedReader br = Files.newBufferedReader(Paths.get("src/test/json/BadLoadData.json"));
            badRequest = gson.fromJson(br, LoadRequest.class);
            br.close();
        } catch (Exception e) {
            System.out.println("ERROR loading LOAD file");
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void loadPass() {
        LoadService ls = new LoadService();
        LoadResult result = ls.load(goodRequest);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("Successfully added 2 users, 11 persons, and 19 events to the database.", result.getMessage());
    }

    @Test
    public void loadFail() {
        LoadService ls = new LoadService();
        LoadResult result = ls.load(badRequest);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("ERROR: User Object missing or has invalid value", result.getMessage());
    }
}