package controller;
import controller.DataController;
import static org.junit.jupiter.api.Assertions.*;

class DataControllerTest {

    @org.junit.jupiter.api.Test
    void analogInputStartTest() {
        DataController newDataController = new DataController();
        double[] bufferTest = newDataController.analogInputStart("0:0", 100, 100, 1000);
        assertNotNull(bufferTest, "BufferTest was null (no input from device)");


    }
}